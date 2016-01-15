package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

@ComponentBean
@GenerateDto
public interface ITest<F extends IFonction<? extends IFonction<G>>, E extends IUser, G extends Enum<G>> extends IFonction<String>, IComponent {

    int getPrimitif();

    void setPrimitif(int primitif);

    int[] getPrimitifs();

    void setPrimitifs(int[] primitifs);

    E getName();

    void setName(E name);

    <U extends IFonction<F>> U getStatus();

    <U extends IFonction<F>> void setStatus(U status);

    IFonction<? super String> getRien();

    void setRien(IFonction<? super String> rien);
}
