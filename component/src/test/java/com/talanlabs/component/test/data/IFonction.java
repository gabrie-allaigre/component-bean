package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IFonction<E> extends IComponent {

    E getType();

    void setType(E type);

}
