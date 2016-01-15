package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.util.Map;

public class BeanToComponentTypeAdapter<T> implements IComponentMapperTypeAdapter<T, IComponent> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!Object.class.isAssignableFrom(srcTypeToken.getRawType()) || !ComponentFactory.getInstance().isComponentType(dstTypeToken.getType())) {
                return null;
            }
            return (IComponentMapperTypeAdapter<T2, U2>) new BeanToComponentTypeAdapter(mapper, BeanHelper.getBoundFields(mapper, srcTypeToken), dstTypeToken);
        }
    };

    private final ComponentMapper mapper;
    private final Map<String, BeanHelper.BoundField> boundFields;
    private final TypeToken<IComponent> dstTypeToken;

    private BeanToComponentTypeAdapter(ComponentMapper mapper, Map<String, BeanHelper.BoundField> boundFields, TypeToken<IComponent> dstTypeToken) {
        super();

        this.mapper = mapper;
        this.boundFields = boundFields;
        this.dstTypeToken = dstTypeToken;
    }

    @Override
    public IComponent convert(T src) {
        if (src == null) {
            return null;
        }

        IComponent dstComponent = ComponentFactory.getInstance().createInstance(dstTypeToken.getType());
        ComponentDescriptor<IComponent> dstCd = ComponentFactory.getInstance().getDescriptor(dstTypeToken.getType());

        try {
            for (ComponentDescriptor.PropertyDescriptor propertyDescriptor : dstCd.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getPropertyName();

                if (boundFields.containsKey(propertyName)) {
                    TypeToken<?> dstTypeToken = TypeToken.of(propertyDescriptor.getPropertyType());
                    Object value = boundFields.get(propertyName).get(src, dstTypeToken);
                    dstComponent.straightSetProperty(propertyName, value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }

        return dstComponent;
    }
}
