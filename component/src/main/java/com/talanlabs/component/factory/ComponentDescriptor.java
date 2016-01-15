package com.talanlabs.component.factory;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ComponentDescriptor, describe a component class <link>IComponent</link>
 *
 * @author Gaby
 */
public class ComponentDescriptor<E extends IComponent> {

    private static final Logger LOG = LogManager.getLogger(ComponentDescriptor.class);

    private IToStringFactory toStringFactory;

    private Map<String, PropertyDescriptor> componentFieldMap;
    private Set<PropertyDescriptor> propertyDescriptors;
    private TypeToken<E> componentTypeToken;
    private Class<E> componentClass;
    private Set<String> propertyNames;
    private Map<String, ComponentBeanMethod> methodMap;
    private Map<String, ComputedMethodDescriptor> computedMethodDescriptorMap;
    private Set<String> equalsKeyPropertyNames;
    private Map<String, Boolean> nullEqualsKeyMap;

    @SuppressWarnings("unchecked")
    ComponentDescriptor(TypeToken<E> componentTypeToken) {
        super();

        this.componentTypeToken = componentTypeToken;
        this.componentClass = (Class<E>) componentTypeToken.getRawType();

        initialize();
    }

    private void initialize() {
        componentFieldMap = new HashMap<>();
        propertyDescriptors = new LinkedHashSet<>();
        equalsKeyPropertyNames = new LinkedHashSet<>();
        propertyNames = new LinkedHashSet<>();
        methodMap = new HashMap<>();
        computedMethodDescriptorMap = new HashMap<>();
        nullEqualsKeyMap = new HashMap<>();

        addMethods(componentClass);
        addMethods(Object.class);

        propertyDescriptors = Collections.unmodifiableSet(propertyDescriptors);
        equalsKeyPropertyNames = Collections.unmodifiableSet(equalsKeyPropertyNames);
        propertyNames = Collections.unmodifiableSet(propertyNames);
    }

    private void addMethods(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            ComponentBeanMethod bm = ComponentBeanMethod.which(method);
            methodMap.put(method.toGenericString(), bm);
            addMethod(method, bm);
        }
    }

    private void addMethod(Method method, ComponentBeanMethod bm) {
        switch (bm) {
        case DEFAULT_GET:
        case DEFAULT:
            addDefaultMethod(method, bm);
            break;
        case COMPUTED_GET:
        case COMPUTED:
            addComputedMethod(method, bm);
            break;
        case GET:
            addPropertyName(method, bm, bm.inferName(method), false, false);
            break;
        default:
            break;
        }
    }

    private void addComputedMethod(Method method, ComponentBeanMethod bm) {
        IComponent.Computed computed = method.getAnnotation(IComponent.Computed.class);
        Class<?> clazz = computed.value();
        Method computedMethod = findComputedMethodInComputedClass(clazz, method);

        boolean getter = false;
        String setterLink = null;
        String name = method.toGenericString();
        if (ComponentBeanMethod.COMPUTED_GET == bm) {
            getter = true;

            name = bm.inferName(method);

            Method setterMethod = MethodUtils.getAccessibleMethod(componentClass, "set" + StringUtils.capitalize(name), method.getReturnType());
            if (setterMethod != null && setterMethod.isAnnotationPresent(IComponent.Computed.class)) {
                setterLink = setterMethod.toGenericString();
            }
        }

        ComputedMethodDescriptor computedMethodDescriptor = new ComputedMethodDescriptor(method, clazz, computedMethod, getter, setterLink);
        computedMethodDescriptorMap.put(name, computedMethodDescriptor);

        if (ComponentBeanMethod.COMPUTED_GET == bm) {
            addPropertyName(method, bm, name, true, false);
        }
    }

    private void addDefaultMethod(Method method, ComponentBeanMethod bm) {
        boolean getter = false;
        String setterLink = null;
        String name = method.toGenericString();
        if (ComponentBeanMethod.DEFAULT_GET == bm) {
            getter = true;

            name = bm.inferName(method);

            Method setterMethod = MethodUtils.getAccessibleMethod(componentClass, "set" + StringUtils.capitalize(name), method.getReturnType());
            if (setterMethod != null && setterMethod.isDefault()) {
                setterLink = setterMethod.toGenericString();
            }
        }

        ComputedMethodDescriptor computedMethodDescriptor = new ComputedMethodDescriptor(method, null, null, getter, setterLink);
        computedMethodDescriptorMap.put(name, computedMethodDescriptor);

        if (ComponentBeanMethod.DEFAULT_GET == bm) {
            addPropertyName(method, bm, name, true, false);
        }
    }

    private Method findComputedMethodInComputedClass(Class<?> clazz, Method method) {
        if (clazz != null) {
            List<Class<?>> ps = new ArrayList<>();
            ps.add(componentClass);
            ps.addAll(Arrays.asList(method.getParameterTypes()));
            Method computedMethod = MethodUtils.getAccessibleMethod(clazz, method.getName(), ps.toArray(new Class<?>[ps.size()]));
            if (computedMethod != null) {
                return computedMethod;
            }
            throw new IllegalArgumentException(
                    "Not found method for component=" + componentClass + " for " + method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(ps.toArray()) + ") in computed class "
                            + clazz);
        } else {
            throw new IllegalArgumentException("Computed class must not null in method=" + method);
        }
    }

    private void addPropertyName(Method method, ComponentBeanMethod bm, String propertyName, boolean computed, boolean defaulted) {
        IComponent.EqualsKey a = method.getAnnotation(IComponent.EqualsKey.class);
        boolean equalsKey = a != null;

        TypeToken<?> typeToken = componentTypeToken.method(method).getReturnType();

        PropertyDescriptor cf = new PropertyDescriptor(method, propertyName, typeToken, typeToken.getRawType(), equalsKey, computed, defaulted);
        componentFieldMap.put(propertyName, cf);
        propertyDescriptors.add(cf);
        propertyNames.add(propertyName);
        if (equalsKey) {
            equalsKeyPropertyNames.add(propertyName);
            nullEqualsKeyMap.put(propertyName, a.nullEquals());
        }
    }

    /**
     * Get a comparator name from component class
     *
     * @return
     */
    public final String getName() {
        return componentClass.getName();
    }

    public final Type getComponentType() {
        return componentTypeToken.getType();
    }

    /**
     * Get the component class
     *
     * @return
     */
    public final Class<? extends IComponent> getComponentClass() {
        return componentClass;
    }

    /**
     * Test if o is instance of component class
     *
     * @param o
     * @return
     */
    public final boolean isInstance(Object o) {
        return o != null && componentClass.isInstance(o);
    }

    /**
     * Get a list of properties names
     *
     * @return
     */
    public final Set<String> getPropertyNames() {
        return propertyNames;
    }

    /**
     * Get a <code>ComponentBeanMethod</code>
     *
     * @param keyMethod
     * @return
     */
    public final ComponentBeanMethod getComponentBeanMethod(String keyMethod) {
        if (keyMethod == null) {
            throw new IllegalArgumentException("keyMethod is null");
        }
        return methodMap.get(keyMethod);
    }

    /**
     * Get a compute method descriptor
     *
     * @param key (can be propertyName if getter, or GenericString)
     * @return
     */
    public final ComputedMethodDescriptor getComputedMethodDescriptor(String key) {
        if (key == null) {
            throw new IllegalArgumentException("keyMethod is null");
        }
        return computedMethodDescriptorMap.get(key);
    }

    /**
     * Get a type of property name
     *
     * @param propertyName
     * @return a type of property
     */
    public final Class<?> getPropertyClass(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        return componentFieldMap.containsKey(propertyName) ? componentFieldMap.get(propertyName).getPropertyClass() : null;
    }

    /**
     * @param propertyName property name
     * @return Get a type of property name
     */
    public final Type getPropertyType(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        return componentFieldMap.containsKey(propertyName) ? componentFieldMap.get(propertyName).getPropertyType() : null;
    }

    /**
     * @param propertyName property name
     * @return Get a component field by property name
     */
    public final PropertyDescriptor getPropertyDescriptor(String propertyName) {
        return componentFieldMap.get(propertyName);
    }

    /**
     * @return Get property descriptors
     */
    public final Set<PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }

    /**
     * @return Get properties use for equals and hashcode
     */
    public final Set<String> getEqualsKeyPropertyNames() {
        return equalsKeyPropertyNames;
    }

    /**
     * @param propertyName equals key property name
     * @return true if nullEquals
     */
    public final boolean isNullEqualsKeyPropertyName(String propertyName) {
        Boolean b = nullEqualsKeyMap.get(propertyName);
        return b != null && b;
    }

    /**
     * @return Get toString factory
     */
    public IToStringFactory getToStringFactory() {
        return toStringFactory;
    }

    /**
     * @param toStringFactory toString factory
     */
    public void setToStringFactory(IToStringFactory toStringFactory) {
        this.toStringFactory = toStringFactory;
    }

    public String buildToString(E component) {
        if (toStringFactory != null) {
            return toStringFactory.buildToString(this, component);
        }
        if (ComponentFactory.getInstance().getComponentFactoryConfiguration().getToStringFactory() != null) {
            return ComponentFactory.getInstance().getComponentFactoryConfiguration().getToStringFactory().buildToString(this, component);
        }
        return null;
    }

    public final static class ComputedMethodDescriptor {

        private final Method method;
        private final Class<?> computeClass;
        private final Method computedMethod;
        private final boolean getter;
        private final String setterLink;

        public ComputedMethodDescriptor(Method method, Class<?> computeClass, Method computedMethod, boolean getter, String setterLink) {
            super();
            this.method = method;
            this.computedMethod = computedMethod;
            this.computeClass = computeClass;
            this.getter = getter;
            this.setterLink = setterLink;
        }

        public Method getMethod() {
            return method;
        }

        public Class<?> getComputeClass() {
            return computeClass;
        }

        public Method getComputedMethod() {
            return computedMethod;
        }

        public boolean isGetter() {
            return getter;
        }

        public String getSetterLink() {
            return setterLink;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("method", method).add("computeClass", computeClass).add("computedMethod", computedMethod).add("getter", getter).add("setterLink", setterLink)
                    .toString();
        }
    }

    public final static class PropertyDescriptor {

        private final Method method;
        private final String propertyName;
        private final TypeToken<?> typeToken;
        private final Class<?> propertyClass;
        private final boolean equalsKey;
        private final boolean computed;
        private final boolean defaulted;

        PropertyDescriptor(Method method, String propertyName, TypeToken<?> typeToken, Class<?> propertyClass, boolean equalsKey, boolean computed, boolean defaulted) {
            super();

            this.method = method;
            this.propertyName = propertyName;
            this.typeToken = typeToken;
            this.propertyClass = propertyClass;
            this.equalsKey = equalsKey;
            this.computed = computed;
            this.defaulted = defaulted;
        }

        public Method getMethod() {
            return method;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public boolean isEqualsKey() {
            return equalsKey;
        }

        public Type getPropertyType() {
            return typeToken.getType();
        }

        public Class<?> getPropertyClass() {
            return propertyClass;
        }

        public boolean isComputed() {
            return computed;
        }

        public boolean isDefaulted() {
            return defaulted;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("method", method).add("propertyName", propertyName).add("typeToken", typeToken).add("propertyClass", propertyClass).add("equalsKey", equalsKey)
                    .add("computed", computed).add("defaulted", defaulted).toString();
        }
    }
}
