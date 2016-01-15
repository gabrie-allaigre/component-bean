package com.talanlabs.component.test.unit;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.test.data.IFonction;
import com.talanlabs.component.test.data.IUser;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.lang.reflect.Type;

public class ComponentFactoryTest {

    @Test
    public void test0() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ComponentFactory.getInstance().isComponentType(IUser.class)).isTrue();
        softAssertions.assertThat(ComponentFactory.getInstance().isComponentType(IFonction.class)).isFalse();
        softAssertions.assertThat(ComponentFactory.getInstance().isComponentType(new TypeToken<IFonction<IFonction<String>>>() {
        }.getType())).isTrue();
        softAssertions.assertThat(ComponentFactory.getInstance().isComponentType(toto())).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void test1() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        Assertions.assertThat(ComponentFactory.getInstance().getComponentClass(user)).isEqualTo(IUser.class);
        Assertions.assertThat(ComponentFactory.getInstance().isComponentType(IUser.class)).isTrue();
    }

    private static <E extends IUser> Type toto() {
        return new TypeToken<IFonction<E>>() {
        }.getType();
    }
}
