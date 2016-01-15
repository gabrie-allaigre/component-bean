package com.talanlabs.component.factory;

import com.talanlabs.component.IComponent;

import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public enum ComponentBeanMethod {

    DEFAULT_GET {
        @Override
        public boolean matches(Method method) {
            return method.isDefault() && GET.matches(method) && !method.isAnnotationPresent(IComponent.NoProperty.class);
        }

        @Override
        public String inferName(Method method) {
            return GET.inferName(method);
        }
    }, DEFAULT {
        @Override
        public boolean matches(Method method) {
            return method.isDefault();
        }
    }, COMPUTED_GET {
        @Override
        public boolean matches(Method method) {
            return method.isAnnotationPresent(IComponent.Computed.class) && GET.matches(method) && !method.isAnnotationPresent(IComponent.NoProperty.class);
        }

        @Override
        public String inferName(Method method) {
            return GET.inferName(method);
        }
    }, COMPUTED {
        @Override
        public boolean matches(Method method) {
            return method.isAnnotationPresent(IComponent.Computed.class);
        }
    }, GET {
        @Override
        public String inferName(Method method) {
            if (isBooleanProperty(method) && method.getName().startsWith(IS_PREFIX)) {
                return Introspector.decapitalize(method.getName().substring(2));
            }
            return super.inferName(method);
        }

        @Override
        public boolean matches(Method method) {
            if (method.getParameterTypes().length > 0) {
                return false;
            }

            if (isBooleanProperty(method)) {
                return true;
            }

            String name = method.getName();
            return !"getClass".equals(name) && name.startsWith(GET_PREFIX) && name.length() > 3;
        }
    }, SET {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            String name = method.getName();
            return name.startsWith(SET_PREFIX) && name.length() > 3;
        }
    }, TO_STRING {
        @Override
        public boolean matches(Method method) {
            if (!String.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length > 0) {
                return false;
            }
            String name = method.getName();
            return name.equals("toString");
        }
    }, EQUALS {
        @Override
        public boolean matches(Method method) {
            if (!boolean.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!(Object.class.equals(method.getParameterTypes()[0]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("equals");
        }
    }, HASHCODE {
        @Override
        public boolean matches(Method method) {
            if (!int.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length > 0) {
                return false;
            }
            String name = method.getName();
            return name.equals("hashCode");
        }
    }, ADD_PROPERTY_CHANGE_LISTENER {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!(PropertyChangeListener.class.equals(method.getParameterTypes()[0]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("addPropertyChangeListener");
        }
    }, ADD_PROPERTY_CHANGE_LISTENER_WITH_PROPERTY_NAME {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 2) {
                return false;
            }
            if (!(String.class.equals(method.getParameterTypes()[0]) && PropertyChangeListener.class.equals(method.getParameterTypes()[1]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("addPropertyChangeListener");
        }
    }, REMOVE_PROPERTY_CHANGE_LISTENER {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!(PropertyChangeListener.class.equals(method.getParameterTypes()[0]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("removePropertyChangeListener");
        }
    }, REMOVE_PROPERTY_CHANGE_LISTENER_WITH_PROPERTY_NAME {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 2) {
                return false;
            }
            if (!(String.class.equals(method.getParameterTypes()[0]) && PropertyChangeListener.class.equals(method.getParameterTypes()[1]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("removePropertyChangeListener");
        }
    }, STRAIGHT_GET_PROPERTIES {
        @Override
        public boolean matches(Method method) {
            if (!Map.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 0) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightGetProperties");
        }
    }, STRAIGHT_GET_PROPERTY {
        @Override
        public boolean matches(Method method) {
            if (!Object.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!String.class.equals(method.getParameterTypes()[0])) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightGetProperty");
        }
    }, STRAIGHT_GET_PROPERTY_NAMES {
        @Override
        public boolean matches(Method method) {
            if (!Set.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 0) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightGetPropertyNames");
        }
    }, STRAIGHT_GET_PROPERTY_CLASS {
        @Override
        public boolean matches(Method method) {
            if (!Class.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!String.class.equals(method.getParameterTypes()[0])) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightGetPropertyClass");
        }
    }, STRAIGHT_SET_PROPERTIES {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            if (!Map.class.equals(method.getParameterTypes()[0])) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightSetProperties");
        }
    }, STRAIGHT_SET_PROPERTY {
        @Override
        public boolean matches(Method method) {
            if (!void.class.equals(method.getReturnType())) {
                return false;
            }
            if (method.getParameterTypes().length != 2) {
                return false;
            }
            if (!(String.class.equals(method.getParameterTypes()[0]) && Object.class.equals(method.getParameterTypes()[1]))) {
                return false;
            }
            String name = method.getName();
            return name.equals("straightSetProperty");
        }
    }, CALL {
        /**
         * Matches all leftover methods.
         */
        @Override
        public boolean matches(Method method) {
            return true;
        }
    };

    private static final String GET_PREFIX = "get";

    private static final String HAS_PREFIX = "has";

    private static final String IS_PREFIX = "is";

    private static final String SET_PREFIX = "set";

    /**
     * Returns {@code true} if the method matches {@code boolean isFoo()} or {@code boolean hasFoo()} property accessors.
     */
    private static boolean isBooleanProperty(Method method) {
        if (Boolean.class.equals(method.getReturnType()) || boolean.class.equals(method.getReturnType())) {
            String name = method.getName();
            if (name.startsWith(IS_PREFIX) && name.length() > 2) {
                return true;
            }
            if (name.startsWith(HAS_PREFIX) && name.length() > 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine which Action a method maps to.
     */
    public static ComponentBeanMethod which(Method method) {
        for (ComponentBeanMethod action : ComponentBeanMethod.values()) {
            if (action.matches(method)) {
                return action;
            }
        }
        throw new RuntimeException("CALL should have matched");
    }

    /**
     * Infer the name of a property from the method.
     */
    public String inferName(Method method) {
        if (this != GET && this != SET) {
            throw new UnsupportedOperationException("Cannot infer a property name for a CALL-type method");
        }
        return Introspector.decapitalize(method.getName().substring(3));
    }

    /**
     * Returns {@code true} if the BeanLikeMethod matches the method.
     */
    public abstract boolean matches(Method method);
}
