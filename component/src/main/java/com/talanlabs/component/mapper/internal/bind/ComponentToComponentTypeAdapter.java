package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.util.Set;

public class ComponentToComponentTypeAdapter implements IComponentMapperTypeAdapter<IComponent, IComponent> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!ComponentFactory.getInstance().isComponentType(srcTypeToken.getType()) || !ComponentFactory.getInstance().isComponentType(dstTypeToken.getType())) {
                return null;
            }
            return (IComponentMapperTypeAdapter<T2, U2>) new ComponentToComponentTypeAdapter(mapper, (TypeToken<IComponent>) dstTypeToken);
        }
    };

    private final ComponentMapper mapper;
    private final TypeToken<IComponent> dstTypeToken;

    private ComponentToComponentTypeAdapter(ComponentMapper mapper, TypeToken<IComponent> dstTypeToken) {
        super();

        this.mapper = mapper;
        this.dstTypeToken = dstTypeToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IComponent convert(IComponent src) {
        if (src == null) {
            return null;
        }

        IComponent dstComponent = ComponentFactory.getInstance().createInstance(dstTypeToken.getType());

        ComponentDescriptor<IComponent> srcCd = ComponentFactory.getInstance().getDescriptor(src);
        ComponentDescriptor<IComponent> dstCd = ComponentFactory.getInstance().getDescriptor(dstTypeToken.getType());

        Set<String> srcPropertyNames = srcCd.getPropertyNames();

        for (ComponentDescriptor.PropertyDescriptor propertyDescriptor : dstCd.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getPropertyName();
            if (srcPropertyNames.contains(propertyName)) {
                Object value = src.straightGetProperty(propertyDescriptor.getPropertyName());
                Object res = null;
                if (value != null) {
                    TypeToken srcTypeToken = TypeToken.of(srcCd.getPropertyType(propertyName));
                    TypeToken dstTypeToken = TypeToken.of(propertyDescriptor.getPropertyType());

                    IComponentMapperTypeAdapter<Object, Object> typeAdapter = new TypeAdapterRuntimeTypeWrapper<>(mapper,
                            (IComponentMapperTypeAdapter<Object, Object>) mapper.getTypeAdapter(srcTypeToken, dstTypeToken), srcTypeToken, dstTypeToken);
                    res = typeAdapter.convert(value);
                }
                dstComponent.straightSetProperty(propertyName, res);
            }
        }
        return dstComponent;
    }
}
