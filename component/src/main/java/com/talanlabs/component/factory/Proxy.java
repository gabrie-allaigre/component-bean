package com.talanlabs.component.factory;

import com.talanlabs.component.IComponent;

import java.lang.reflect.Type;

public interface Proxy {

    Class<? extends IComponent> straightGetComponentClass();

    Type straightGetComponentType();

}
