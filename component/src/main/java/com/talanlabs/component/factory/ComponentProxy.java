package com.talanlabs.component.factory;

import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

class ComponentProxy<E extends IComponent> implements InvocationHandler, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("componentTypeToken", TypeToken.class), new ObjectStreamField("propertyValueMap", Map.class) };

    private static final Logger LOG = LogManager.getLogger(ComponentProxy.class);

    private static final long serialVersionUID = -1210441501657033411L;

    private static final Constructor<MethodHandles.Lookup> constructor;

    static {
        Constructor<MethodHandles.Lookup> c;
        try {
            c = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            if (!c.isAccessible()) {
                c.setAccessible(true);
            }
        } catch (NoSuchMethodException e) {
            LOG.error("Not found constructor in MethodHandles.Lookup.class with [Class.class, int.class]", e);
            c = null;
        }
        constructor = c;
    }

    private TypeToken<E> componentTypeToken;
    private transient Class<E> componentClass;
    private Map<String, Object> propertyValueMap;

    private transient ComponentDescriptor<E> componentDescriptor;
    private transient List<PropertyChangeListener> listeners;
    private transient Map<String, List<PropertyChangeListener>> listenerMap;
    private transient Map<Class<?>, Object> computeInstanceMap;

    @SuppressWarnings("unchecked")
    ComponentProxy(TypeToken<E> componentTypeToken) {
        super();

        this.componentTypeToken = componentTypeToken;
        this.componentClass = (Class<E>) componentTypeToken.getRawType();

        initialize();
    }

    private void initialize() {
        propertyValueMap = new LinkedHashMap<>();
        for (String propertyName : getDescriptor().getPropertyNames()) {
            propertyValueMap.put(propertyName, PrimitiveHelper.determineValue(getDescriptor().getPropertyClass(propertyName)));
        }
    }

    private ComponentDescriptor<E> getDescriptor() {
        if (componentDescriptor == null) {
            componentDescriptor = ComponentFactory.getInstance().getDescriptor(componentTypeToken.getType());
        }
        return componentDescriptor;
    }

    private List<PropertyChangeListener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        return listeners;
    }

    private Map<String, List<PropertyChangeListener>> getListenerMap() {
        if (listenerMap == null) {
            listenerMap = new HashMap<>();
        }
        return listenerMap;
    }

    private Map<Class<?>, Object> getComputeInstanceMap() {
        if (computeInstanceMap == null) {
            computeInstanceMap = new HashMap<>();
        }
        return computeInstanceMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isStraightGetComponentTypeMethod(method)) {
            return componentTypeToken.getType();
        }
        if (isStraightGetComponentClassMethod(method)) {
            return componentClass;
        }
        ComponentBeanMethod bm = getDescriptor().getComponentBeanMethod(method.toGenericString());
        return invoke(bm, proxy, method, args);
    }

    private boolean isStraightGetComponentClassMethod(Method method) {
        if (!Class.class.equals(method.getReturnType())) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        String name = method.getName();
        return name.equals("straightGetComponentClass");
    }

    private boolean isStraightGetComponentTypeMethod(Method method) {
        if (!Type.class.equals(method.getReturnType())) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        String name = method.getName();
        return name.equals("straightGetComponentType");
    }

    @SuppressWarnings("unchecked")
    private Object invoke(ComponentBeanMethod bm, Object proxy, Method method, Object[] args) throws Throwable {
        Object res = null;
        switch (bm) {
        case DEFAULT_GET:
        case COMPUTED_GET:
            res = computed(proxy, bm.inferName(method), args, true, false);
            break;
        case DEFAULT:
        case COMPUTED:
            res = computed(proxy, method.toGenericString(), args, false, false);
            break;
        case GET:
            res = getter(proxy, bm.inferName(method));
            break;
        case SET:
            setter(proxy, bm.inferName(method), args[0]);
            break;
        case TO_STRING:
            res = buildToString(proxy);
            break;
        case EQUALS:
            res = isEquals(proxy, args[0]);
            break;
        case HASHCODE:
            res = getHashCode(proxy);
            break;
        case ADD_PROPERTY_CHANGE_LISTENER:
            addPropertyChangeListener((PropertyChangeListener) args[0]);
            break;
        case ADD_PROPERTY_CHANGE_LISTENER_WITH_PROPERTY_NAME:
            addPropertyChangeListener((String) args[0], (PropertyChangeListener) args[1]);
            break;
        case REMOVE_PROPERTY_CHANGE_LISTENER:
            removePropertyChangeListener((PropertyChangeListener) args[0]);
            break;
        case REMOVE_PROPERTY_CHANGE_LISTENER_WITH_PROPERTY_NAME:
            removePropertyChangeListener((String) args[0], (PropertyChangeListener) args[1]);
            break;
        case STRAIGHT_GET_PROPERTIES:
            res = straightGetProperties(proxy);
            break;
        case STRAIGHT_GET_PROPERTY:
            res = straightGetProperty(proxy, (String) args[0]);
            break;
        case STRAIGHT_GET_PROPERTY_NAMES:
            res = straightGetPropertyNames(proxy);
            break;
        case STRAIGHT_GET_PROPERTY_CLASS:
            res = straightGetPropertyClass(proxy, (String) args[0]);
            break;
        case STRAIGHT_SET_PROPERTIES:
            straightSetProperties(proxy, (Map<String, Object>) args[0]);
            break;
        case STRAIGHT_SET_PROPERTY:
            straightSetProperty(proxy, (String) args[0], args[1]);
            break;
        case CALL:
            if (method.getDeclaringClass() == Object.class) {
                res = method.invoke(this, args);
            }
            break;
        default:
            break;
        }
        if (res == null && method.getReturnType().isPrimitive() && !method.getReturnType().equals(void.class)) {
            throw new IllegalArgumentException("Method " + method + " return a primitive, result is null");
        }
        return res;
    }

    private Object computed(Object proxy, String name, Object[] args, boolean property, boolean setter) {
        ComponentDescriptor.ComputedMethodDescriptor computedMethodDescriptor = getDescriptor().getComputedMethodDescriptor(name);
        if (computedMethodDescriptor == null) {
            throw new IllegalArgumentException("Not invoke computed or default method for name=" + name);
        }
        if (property) {
            if (!computedMethodDescriptor.isGetter()) {
                throw new IllegalArgumentException("Not invoke computed or default method for " + computedMethodDescriptor.getComputedMethod() + " is not a getter method");
            }
            if (setter) {
                if (computedMethodDescriptor.getSetterLink() == null) {
                    return null;
                }

                computedMethodDescriptor = getDescriptor().getComputedMethodDescriptor(computedMethodDescriptor.getSetterLink());
                if (computedMethodDescriptor == null) {
                    throw new IllegalArgumentException("Not found computed or default method for  setter of " + name + " is not a setter method");
                }
            }
        }

        if (computedMethodDescriptor.getComputeClass() == null && computedMethodDescriptor.getMethod().isDefault()) {
            Method method = computedMethodDescriptor.getMethod();
            Class<?> declaringClass = method.getDeclaringClass();
            try {
                return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE).unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Not invoke default method for " + method, e);
            }
        } else {
            Class<?> clazz = computedMethodDescriptor.getComputeClass();
            Object instance = getComputeInstanceMap().get(clazz);
            if (instance == null) {
                instance = ComponentFactory.getInstance().getComponentFactoryConfiguration().getComputedFactory().createInstance(clazz);
                getComputeInstanceMap().put(clazz, instance);
            }
            try {
                List<Object> list = new ArrayList<>();
                list.add(proxy);
                if (args != null && args.length > 0) {
                    list.addAll(Arrays.asList(args));
                }
                return computedMethodDescriptor.getComputedMethod().invoke(instance, list.toArray());
            } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
                throw new IllegalArgumentException("Not invoke computed method for " + computedMethodDescriptor.getComputedMethod(), e);
            }
        }
    }

    private void straightSetProperties(Object proxy, Map<String, Object> properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties is null");
        }
        for (Entry<String, Object> entry : properties.entrySet()) {
            straightSetProperty(proxy, entry.getKey(), entry.getValue());
        }
    }

    private void straightSetProperty(Object proxy, String propertyName, Object value) {
        if (getDescriptor().getComputedMethodDescriptor(propertyName) != null) {
            computed(proxy, propertyName, new Object[] { value }, true, true);
            return;
        }
        setter(proxy, propertyName, value);
    }

    private Map<String, Object> straightGetProperties(Object proxy) {
        Map<String, Object> res = new HashMap<>();
        for (String propertyName : getDescriptor().getPropertyNames()) {
            res.put(propertyName, straightGetProperty(proxy, propertyName));
        }
        return res;
    }

    private Object straightGetProperty(Object proxy, String propertyName) {
        if (getDescriptor().getComputedMethodDescriptor(propertyName) != null) {
            return computed(proxy, propertyName, null, true, false);
        }
        return getter(proxy, propertyName);
    }

    private Set<String> straightGetPropertyNames(Object proxy) {
        return getDescriptor().getPropertyNames();
    }

    private Class<?> straightGetPropertyClass(Object proxy, String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        return getDescriptor().getPropertyClass(propertyName);
    }

    private Object getter(Object proxy, String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        return propertyValueMap.get(propertyName);
    }

    private void setter(Object proxy, String propertyName, Object value) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        if (!propertyValueMap.containsKey(propertyName)) {
            throw new IllegalArgumentException("propertyName=" + propertyName + " is not property class " + proxy);
        }
        Class<?> propertyClass = componentDescriptor.getPropertyClass(propertyName);
        if (propertyClass == null) {
            throw new IllegalArgumentException("propertyName=" + propertyName + " is not assignable with null class value=" + value + " " + proxy);
        }
        Class<?> fakeType = Primitives.wrap(propertyClass);
        if (value != null && !fakeType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("propertyName=" + propertyName + " is not assignable with class=" + value.getClass() + " value=" + value + " " + proxy);
        }
        if (value == null && propertyClass.isPrimitive()) {
            throw new IllegalArgumentException("propertyName=" + propertyName + " is primitive and value is null " + proxy);
        }
        Object oldValue = propertyValueMap.get(propertyName);
        propertyValueMap.put(propertyName, value);
        firePropertyChange(proxy, propertyName, oldValue, value);
    }

    private void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l is null");
        }
        getListeners().add(l);
    }

    private void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        if (l == null) {
            throw new IllegalArgumentException("l is null");
        }
        List<PropertyChangeListener> list = getListenerMap().get(propertyName);
        if (list == null) {
            list = new ArrayList<>();
            getListenerMap().put(propertyName, list);
        }
        list.add(l);
    }

    private void removePropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l is null");
        }
        getListeners().remove(l);
    }

    private void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }
        if (l == null) {
            throw new IllegalArgumentException("l is null");
        }
        List<PropertyChangeListener> list = getListenerMap().get(propertyName);
        if (list != null) {
            list.remove(l);
        }
    }

    private void firePropertyChange(Object proxy, String propertyName, Object oldValue, Object newValue) {
        PropertyChangeEvent evt = new PropertyChangeEvent(proxy, propertyName, oldValue, newValue);
        for (PropertyChangeListener l : getListeners()) {
            l.propertyChange(evt);
        }
        List<PropertyChangeListener> list = getListenerMap().get(propertyName);
        if (list != null) {
            for (PropertyChangeListener l : list) {
                l.propertyChange(evt);
            }
        }
    }

    private int getHashCode(Object proxy) {
        ComponentDescriptor<E> cd = getDescriptor();
        if (cd.getEqualsKeyPropertyNames() != null && !cd.getEqualsKeyPropertyNames().isEmpty()) {
            int hashCode = 23;
            for (String propertyName : cd.getEqualsKeyPropertyNames()) {
                Object value = propertyValueMap.get(propertyName);
                hashCode = (hashCode * 37) + hash(value);
            }
            return hashCode;
        } else {
            return hashCode();
        }
    }

    private int hash(Object value) {
        int hash;
        if (value instanceof Object[]) {
            hash = Arrays.deepHashCode((Object[]) value);
        } else if (value instanceof byte[]) {
            hash = Arrays.hashCode((byte[]) value);
        } else if (value instanceof short[]) {
            hash = Arrays.hashCode((short[]) value);
        } else if (value instanceof int[]) {
            hash = Arrays.hashCode((int[]) value);
        } else if (value instanceof long[]) {
            hash = Arrays.hashCode((long[]) value);
        } else if (value instanceof char[]) {
            hash = Arrays.hashCode((char[]) value);
        } else if (value instanceof float[]) {
            hash = Arrays.hashCode((float[]) value);
        } else if (value instanceof double[]) {
            hash = Arrays.hashCode((double[]) value);
        } else if (value instanceof boolean[]) {
            hash = Arrays.hashCode((boolean[]) value);
        } else {
            hash = value != null ? value.hashCode() : 0;
        }
        return hash;
    }

    private boolean isEquals(Object proxy, Object o) {
        ComponentDescriptor<E> cd = getDescriptor();
        if (cd.getEqualsKeyPropertyNames() != null && !cd.getEqualsKeyPropertyNames().isEmpty()) {
            if (proxy == o) {
                return true;
            }
            if (!(o instanceof IComponent)) {
                return false;
            }
            Class<? extends IComponent> clazz = ComponentFactory.getInstance().getComponentClass((IComponent) o);
            if (!componentClass.equals(clazz)) {
                return false;
            }
            IComponent other = (IComponent) o;
            for (String propertyName : cd.getEqualsKeyPropertyNames()) {
                Object value1 = propertyValueMap.get(propertyName);
                Object value2 = other.straightGetProperty(propertyName);

                if (!cd.isNullEqualsKeyPropertyName(propertyName) && (value1 == null || value2 == null)) {
                    return false;
                }

                if (!Objects.deepEquals(value1, value2)) {
                    return false;
                }
            }
            return true;
        } else {
            return proxy == o;
        }
    }

    private String buildToString(Object proxy) {
        return getDescriptor().buildToString((E) proxy);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(componentTypeToken);
        out.writeObject(propertyValueMap);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        componentTypeToken = (TypeToken<E>) in.readObject();
        componentClass = (Class<E>) componentTypeToken.getRawType();
        propertyValueMap = (Map<String, Object>) in.readObject();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
