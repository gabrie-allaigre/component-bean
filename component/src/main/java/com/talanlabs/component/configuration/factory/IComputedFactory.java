package com.talanlabs.component.configuration.factory;

public interface IComputedFactory {

    <E> E createInstance(Class<E> clazz);

}
