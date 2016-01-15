package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.util.HashMap;
import java.util.Map;

public class ComponentToMapTypeAdapter implements IComponentMapperTypeAdapter<IComponent, Map<String, Object>> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!ComponentFactory.getInstance().isComponentType(srcTypeToken.getType()) || !Map.class.isAssignableFrom(dstTypeToken.getRawType())) {
                return null;
            }

            return (IComponentMapperTypeAdapter<T2, U2>) new ComponentToMapTypeAdapter();
        }
    };

    private ComponentToMapTypeAdapter() {
        super();
    }

    @Override
    public Map<String, Object> convert(IComponent src) {
        if (src == null) {
            return null;
        }

        Map<String, Object> dst = new HashMap<>();

        ComponentDescriptor<IComponent> srcCd = ComponentFactory.getInstance().getDescriptor(src);

        for (ComponentDescriptor.PropertyDescriptor propertyDescriptor : srcCd.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getPropertyName();
            Object value = src.straightGetProperty(propertyName);
            dst.put(propertyName, value);
        }

        return dst;
    }
}
