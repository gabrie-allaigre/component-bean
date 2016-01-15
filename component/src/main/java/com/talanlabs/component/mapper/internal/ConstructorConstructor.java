package com.talanlabs.component.mapper.internal;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.InstanceFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ConstructorConstructor {

    private final Map<Type, InstanceFactory<?>> instanceCreators;

    public ConstructorConstructor(Map<Type, InstanceFactory<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();

        // first try an instance creator

        @SuppressWarnings("unchecked") // types must agree
        final InstanceFactory<T> typeCreator = (InstanceFactory<T>) instanceCreators.get(type);
        if (typeCreator != null) {
            return () -> typeCreator.createInstance(type);
        }

        // Next try raw type match for instance creators
        @SuppressWarnings("unchecked") // types must agree
        final InstanceFactory<T> rawTypeCreator = (InstanceFactory<T>) instanceCreators.get(rawType);
        if (rawTypeCreator != null) {
            return () -> rawTypeCreator.createInstance(type);
        }

        ObjectConstructor<T> defaultConstructor = newDefaultConstructor(rawType);
        if (defaultConstructor != null) {
            return defaultConstructor;
        }

        ObjectConstructor<T> defaultImplementation = newDefaultImplementationConstructor(type, rawType);
        if (defaultImplementation != null) {
            return defaultImplementation;
        }

        throw new RuntimeException(("Unable to invoke no-args constructor for " + type + ". " + "Register an InstanceFactory for this type may fix this problem."));
    }

    @SuppressWarnings("unchecked") // use runtime checks to guarantee that 'T' is what it is
    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> rawType) {
        try {
            final Constructor<? super T> constructor = rawType.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return () -> {
                try {
                    return (T) constructor.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException("Failed to invoke " + constructor + " with no args", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Failed to invoke " + constructor + " with no args", e.getTargetException());
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e);
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Constructors for common interface types like Map and List and their
     * subtypes.
     */
    @SuppressWarnings("unchecked") // use runtime checks to guarantee that 'T' is what it is
    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, Class<? super T> rawType) {
        if (Collection.class.isAssignableFrom(rawType)) {
            if (SortedSet.class.isAssignableFrom(rawType)) {
                return () -> (T) new TreeSet<>();
            } else if (EnumSet.class.isAssignableFrom(rawType)) {
                return () -> {
                    if (type instanceof ParameterizedType) {
                        Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
                        if (elementType instanceof Class) {
                            return (T) EnumSet.noneOf((Class) elementType);
                        } else {
                            throw new RuntimeException("Invalid EnumSet type: " + type.toString());
                        }
                    } else {
                        throw new RuntimeException("Invalid EnumSet type: " + type.toString());
                    }
                };
            } else if (Set.class.isAssignableFrom(rawType)) {
                return () -> (T) new LinkedHashSet<>();
            } else if (Queue.class.isAssignableFrom(rawType)) {
                return () -> (T) new LinkedList<>();
            } else {
                return () -> (T) new ArrayList<>();
            }
        }

        if (Map.class.isAssignableFrom(rawType)) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(rawType)) {
                return () -> (T) new ConcurrentSkipListMap<>();
            } else if (ConcurrentMap.class.isAssignableFrom(rawType)) {
                return () -> (T) new ConcurrentHashMap<>();
            } else if (SortedMap.class.isAssignableFrom(rawType)) {
                return () -> (T) new TreeMap<>();
            } else if (type instanceof ParameterizedType && !(String.class.isAssignableFrom(TypeToken.of(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType()))) {
                return () -> (T) new LinkedHashMap<>();
            } else {
                return () -> (T) new HashMap<String, Object>();
            }
        }

        return null;
    }
}
