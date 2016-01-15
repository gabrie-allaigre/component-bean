package com.talanlabs.component.configuration.factory;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.factory.tostring.CompleteToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.SimpleToStringFactory;
import com.talanlabs.component.factory.ComponentDescriptor;

public interface IToStringFactory {

    /**
     * Show only header of class (name + hashcode)
     */
    static IToStringFactory simple() {
        return new SimpleToStringFactory();
    }

    /**
     * Show complete class
     */
    static IToStringFactory complete() {
        return new CompleteToStringFactory();
    }

    /**
     * Show complete class
     *
     * @param hideHeader    hide header
     * @param multiline     active multline
     * @param hideNullValue hide null value
     */
    static IToStringFactory complete(boolean hideHeader, boolean multiline, boolean hideNullValue) {
        return new CompleteToStringFactory(hideHeader, multiline, hideNullValue);
    }

    /**
     * Build to String for component
     *
     * @param componentDescriptor descriptor of component
     * @param component           component
     * @return to String for component
     */
    <E extends IComponent> String buildToString(ComponentDescriptor<E> componentDescriptor, E component);
}
