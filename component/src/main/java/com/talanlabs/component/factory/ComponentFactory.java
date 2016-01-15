package com.talanlabs.component.factory;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.ComponentFactoryConfigurationBuilder;
import com.talanlabs.component.configuration.IComponentFactoryConfiguration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComponentFactory : create a component instance
 *
 * @author Gaby
 */
public class ComponentFactory {

    private static ComponentFactory instance;
    private final IComponentFactoryConfiguration configuration;
    private Map<Type, ComponentDescriptor> descriptorMap;
    private List<IComponentFactoryListener> componentFactoryListeners;

    public ComponentFactory(IComponentFactoryConfiguration configuration) {
        super();

        this.configuration = configuration;

        this.descriptorMap = Collections.synchronizedMap(new HashMap<>());
        this.componentFactoryListeners = new ArrayList<>();
    }

    public static synchronized ComponentFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new ComponentFactory(ComponentFactoryConfigurationBuilder.newBuilder().build());
        return instance;
    }

    public static synchronized void setInstance(ComponentFactory instance) {
        ComponentFactory.instance = instance;
    }

    /**
     * Get a current configuration
     *
     * @return configuration
     */
    public IComponentFactoryConfiguration getComponentFactoryConfiguration() {
        return this.configuration;
    }

    /**
     * Add componentFactoryListener
     */
    public void addComponentFactoryListener(IComponentFactoryListener l) {
        componentFactoryListeners.add(l);
    }

    /**
     * Remove componentFactoryListener
     */
    public void removeComponentFactoryListener(IComponentFactoryListener l) {
        componentFactoryListeners.remove(l);
    }

    /*
     * Fire after created instance
     */
    protected <G extends IComponent> void fireAfterCreated(Class<G> interfaceClass, G instance) {
        for (IComponentFactoryListener componentFactoryListener : componentFactoryListeners) {
            componentFactoryListener.afterCreated(interfaceClass, instance);
        }
    }

    /**
     * Create a instance of component class
     *
     * @param componentClass a component class
     * @return a instance of component class
     */
    @SuppressWarnings("unchecked")
    public <G extends IComponent> G createInstance(Class<G> componentClass) {
        return (G) createInstance((Type) componentClass);
    }

    /**
     * Create a instance of component type
     *
     * @param componentType a component type
     * @return a instance of component type
     */
    @SuppressWarnings("unchecked")
    public <G extends IComponent> G createInstance(Type componentType) {
        if (!isComponentType(componentType)) {
            throw new IllegalArgumentException("componentType is not a IComponent or with type parameters unknown " + componentType);
        }
        TypeToken<G> tt = (TypeToken<G>) TypeToken.of(componentType);
        Class<G> interfaceClass = (Class<G>) tt.getRawType();
        ClassLoader classLoader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[] { interfaceClass, com.talanlabs.component.factory.Proxy.class };
        InvocationHandler proxy = newProxy(tt);
        G instance = (G) Proxy.newProxyInstance(classLoader, interfaces, proxy);
        fireAfterCreated(interfaceClass, instance);
        return instance;
    }

    /**
     * Get descriptor of interfaceClass
     *
     * @param componentClass component class
     * @return a component descriptor
     */
    public <G extends IComponent> ComponentDescriptor<G> getDescriptor(Class<? extends IComponent> componentClass) {
        return getDescriptor((Type) componentClass);
    }

    /**
     * Get a descriptor for type
     *
     * @param componentType type
     * @return a component descriptor
     */
    @SuppressWarnings("unchecked")
    public <G extends IComponent> ComponentDescriptor<G> getDescriptor(Type componentType) {
        if (!isComponentType(componentType)) {
            throw new IllegalArgumentException("componentType is not a IComponent or with type parameters unknown " + componentType);
        }
        ComponentDescriptor res = descriptorMap.get(componentType);
        if (res == null) {
            res = newDescriptor((TypeToken<G>) TypeToken.of(componentType));
            descriptorMap.put(componentType, res);
        }
        return res;
    }

    /**
     * Get a descriptor of instance
     *
     * @param instance component instance
     * @return a component descriptor for component
     */
    public <G extends IComponent> ComponentDescriptor<G> getDescriptor(G instance) {
        if (instance == null) {
            throw new IllegalArgumentException("instance is null");
        }
        return getDescriptor(getComponentType(instance));
    }

    private <G extends IComponent> ComponentDescriptor<G> newDescriptor(TypeToken<G> typeToken) {
        return new ComponentDescriptor<>(typeToken);
    }

    private <G extends IComponent> InvocationHandler newProxy(TypeToken<G> typeToken) {
        return new ComponentProxy<>(typeToken);
    }

    /**
     * Get Component class for instance
     *
     * @param instance component instance
     * @return class
     */
    @SuppressWarnings("unchecked")
    public final <E extends IComponent> Class<E> getComponentClass(E instance) {
        if (instance instanceof com.talanlabs.component.factory.Proxy) {
            return (Class<E>) (((com.talanlabs.component.factory.Proxy) instance).straightGetComponentClass());
        }
        return null;
    }

    /**
     * Get type for component
     *
     * @param instance component instance
     * @return type
     */
    public final <E extends IComponent> Type getComponentType(E instance) {
        if (instance instanceof com.talanlabs.component.factory.Proxy) {
            return ((com.talanlabs.component.factory.Proxy) instance).straightGetComponentType();
        }
        return null;
    }

    /**
     * Verify if type is a component, interface and a not generic with unknow type variable
     *
     * @param type type
     * @return true if type is component and not generic with unknow type variable
     */
    public boolean isComponentType(Type type) {
        TypeToken tt = TypeToken.of(type);
        if (!IComponent.class.isAssignableFrom(tt.getRawType())) {
            return false;
        }
        if (tt.getRawType() == tt.getType()) {
            return tt.getRawType().getTypeParameters() == null || tt.getRawType().getTypeParameters().length == 0;
        }
        return !containTypeVariable(type);
    }

    private boolean containTypeVariable(Type type) {
        if (type instanceof Class) {
            return false;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] typeArguments = pt.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                if (containTypeVariable(typeArgument)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
