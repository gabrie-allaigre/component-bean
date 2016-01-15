package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.util.Map;

public class MapToComponentTypeAdapter<T, U> implements IComponentMapperTypeAdapter<Map<T, U>, IComponent> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!Map.class.isAssignableFrom(srcTypeToken.getRawType()) || !ComponentFactory.getInstance().isComponentType(dstTypeToken.getType())) {
                return null;
            }

            return (IComponentMapperTypeAdapter<T2, U2>) new MapToComponentTypeAdapter(mapper, dstTypeToken);
        }
    };

    private final ComponentMapper mapper;
    private final TypeToken<IComponent> dstTypeToken;

    private MapToComponentTypeAdapter(ComponentMapper mapper, TypeToken<IComponent> dstTypeToken) {
        super();

        this.mapper = mapper;
        this.dstTypeToken = dstTypeToken;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IComponent convert(Map<T, U> src) {
        if (src == null) {
            return null;
        }

        IComponent dstComponent = ComponentFactory.getInstance().createInstance(dstTypeToken.getType());

        ComponentDescriptor<IComponent> dstCd = ComponentFactory.getInstance().getDescriptor(dstTypeToken.getType());

        for (ComponentDescriptor.PropertyDescriptor propertyDescriptor : dstCd.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getPropertyName();

            if (src.containsKey(propertyName)) {
                Object value = src.get(propertyName);
                Object res = null;
                if (value != null) {
                    TypeToken<?> srcTypeToken = TypeToken.of(value.getClass());
                    TypeToken<?> dstTypeToken = TypeToken.of(propertyDescriptor.getPropertyType());

                    IComponentMapperTypeAdapter<Object, Object> typeAdapter = (IComponentMapperTypeAdapter<Object, Object>) mapper.getTypeAdapter(srcTypeToken, dstTypeToken);
                    res = typeAdapter.convert(value);
                }
                dstComponent.straightSetProperty(propertyName, res);
            }
        }

        return dstComponent;
    }
}
