package com.talanlabs.component.test.it;

import com.google.common.collect.Queues;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.factory.IComponentFactoryListener;
import com.talanlabs.component.test.data.IAddress;
import com.talanlabs.component.test.data.IIntArrayTest;
import com.talanlabs.component.test.data.INotNullEqualsKey;
import com.talanlabs.component.test.data.IOffer;
import com.talanlabs.component.test.data.IPerson;
import com.talanlabs.component.test.data.IStringArrayTest;
import com.talanlabs.component.test.data.ITest3;
import com.talanlabs.component.test.data.IUser;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ComponentFactoryIT {

    @Test
    public void testNotNull() {
        Assertions.assertThat(ComponentFactory.getInstance()).isNotNull();
        Assertions.assertThat(ComponentFactory.getInstance().getComponentFactoryConfiguration()).isNotNull();
        Assertions.assertThat(ComponentFactory.getInstance().getComponentFactoryConfiguration().getComputedFactory()).isNotNull();
    }

    @Test
    public void testInstance() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        Assertions.assertThat(user).isInstanceOf(IUser.class);
    }

    @Test
    public void testProperties() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        user.setLogin("gaby");
        user.straightSetProperty("nikename", "mouton");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(user.getLogin()).isEqualTo("gaby");
        softAssertions.assertThat(user.straightGetProperty("login")).isEqualTo("gaby");
        softAssertions.assertThat(user.straightGetProperties().get("login")).isEqualTo("gaby");
        softAssertions.assertThat(user.getNikename()).isEqualTo("mouton");
        softAssertions.assertThat(user.straightGetProperty("nikename")).isEqualTo("mouton");
        softAssertions.assertThat(user.straightGetProperties().get("nikename")).isEqualTo("mouton");
        softAssertions.assertAll();
    }

    @Test
    public void testMapProperties() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        Map<String, Object> map = new HashMap<>();
        map.put("login", "gaby");
        map.put("nikename", "mouton");
        user.straightSetProperties(map);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(user.getLogin()).isEqualTo("gaby");
        softAssertions.assertThat(user.straightGetProperty("login")).isEqualTo("gaby");
        softAssertions.assertThat(user.straightGetProperties().get("login")).isEqualTo("gaby");
        softAssertions.assertThat(user.getNikename()).isEqualTo("mouton");
        softAssertions.assertThat(user.straightGetProperty("nikename")).isEqualTo("mouton");
        softAssertions.assertThat(user.straightGetProperties().get("nikename")).isEqualTo("mouton");
        softAssertions.assertAll();
    }

    @Test
    public void testDefault() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        user.setLogin("gaby");
        user.setNikename("mouton");

        Assertions.assertThat(user.getFullname()).isEqualTo("gaby mouton");
    }

    @Test
    public void testTypeProperties() {
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);

        Assertions.assertThat(user.straightGetPropertyNames()).containsOnly("login", "nikename", "email", "groups", "address", "addresses", "addresseMap", "ints", "addresses2", "fullname");

        Assertions.assertThat(user.straightGetPropertyClass("login")).isEqualTo(String.class);
        Assertions.assertThat(user.straightGetPropertyClass("groups")).isEqualTo(List.class);
        Assertions.assertThat(user.straightGetPropertyClass("address")).isEqualTo(IAddress.class);
    }

    @Test
    public void testComputed() {
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setPostalZip("78000");
        address.setCity("Versailles");
        address.setCountry("France");

        Assertions.assertThat(address.getFullname()).isEqualTo("78000 Versailles France");
    }

    @Test
    public void testEquals() {
        IAddress address1 = ComponentFactory.getInstance().createInstance(IAddress.class);
        address1.setPostalZip("78000");
        address1.setCity("Versailles");
        address1.setCountry("France");

        IAddress address2 = ComponentFactory.getInstance().createInstance(IAddress.class);
        address2.setPostalZip("78000");
        address2.setCity("Versailles");
        address2.setCountry("France");

        Assertions.assertThat(address1).isEqualTo(address1);
        Assertions.assertThat(address2).isEqualTo(address2);
        Assertions.assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    public void testEqualsKey() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("78000");

        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("78000");

        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("92000");

        IOffer offer1 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer1.setId(1L);
        IOffer offer2 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer2.setId(1L);
        IOffer offer3 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer3.setId(2L);
        IOffer offer4 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer4.setId(null);
        IOffer offer5 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer4.setId(null);
        ITest3 test3 = ComponentFactory.getInstance().createInstance(ITest3.class);
        ITest3 test4 = ComponentFactory.getInstance().createInstance(ITest3.class);
        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        user.setLogin("Gaby");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(user1).isEqualTo(user1);
        softAssertions.assertThat(user2).isEqualTo(user2);
        softAssertions.assertThat(user3).isEqualTo(user3);
        softAssertions.assertThat(user1).isEqualTo(user2);
        softAssertions.assertThat(user1).isNotEqualTo(user3);
        softAssertions.assertThat(user2).isNotEqualTo(user3);
        softAssertions.assertThat(offer1.equals(offer1)).isTrue();
        softAssertions.assertThat(offer4.equals(offer5)).isTrue();
        softAssertions.assertThat(offer1.equals(offer2)).isTrue();
        softAssertions.assertThat(offer1.equals(offer3)).isFalse();
        softAssertions.assertThat(offer1.equals(offer4)).isFalse();
        softAssertions.assertThat(offer1.equals(null)).isFalse();
        softAssertions.assertThat(offer1.equals(test3)).isFalse();
        softAssertions.assertThat(test3.equals(test3)).isTrue();
        softAssertions.assertThat(test3.equals(test4)).isFalse();
        softAssertions.assertThat(offer1.equals(user)).isFalse();
        softAssertions.assertThat(user.equals(offer1)).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testGetterComputed() {
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setDataXml("data");

        Assertions.assertThat(address.getDataXml()).isEqualTo("data");
        Assertions.assertThat(address.straightGetProperty("dataXml")).isEqualTo("data");
        Assertions.assertThat(address.straightGetProperties().get("dataXml")).isEqualTo("data");
    }

    @Test
    public void testSetterComputed() {
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setDataXml("data");

        Assertions.assertThat(address.getDataXml()).isEqualTo("data");
        address.straightSetProperty("dataXml", "data2");
        Assertions.assertThat(address.getDataXml()).isEqualTo("data2");

        Map<String, Object> map = new HashMap<>();
        map.put("dataXml", "data3");
        address.straightSetProperties(map);
        Assertions.assertThat(address.getDataXml()).isEqualTo("data3");
    }

    @Test
    public void testDefaults() {
        ITest3 test3 = ComponentFactory.getInstance().createInstance(ITest3.class);

        test3.setName("toto");
        Assertions.assertThat(test3.getName()).isEqualTo("coucou");
        Assertions.assertThat(test3.straightGetProperty("name")).isEqualTo("coucou");
        Assertions.assertThat(test3.straightGetProperties().get("name")).isEqualTo("coucou");

        Assertions.assertThat(test3.straightGetPropertyNames()).containsOnly("name", "name3");
    }

    @Test
    public void testMultiEquals() {
        IPerson person1 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person1.setFirstName("Gabriel");
        person1.setLastName("Allaigre");

        IPerson person2 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person2.setFirstName("Gabriel");
        person2.setLastName("Allaigre");

        IPerson person3 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person3.setFirstName("Sandra");
        person3.setLastName("Allaigre");

        IPerson person4 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person4.setFirstName("Sandra");
        person4.setLastName(null);

        IPerson person5 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person5.setFirstName("Sandra");
        person5.setLastName(null);

        IPerson person6 = ComponentFactory.getInstance().createInstance(IPerson.class);
        person6.setFirstName("Gabriel");
        person6.setLastName(null);

        IPerson person7 = ComponentFactory.getInstance().createInstance(IPerson.class);
        IPerson person8 = ComponentFactory.getInstance().createInstance(IPerson.class);

        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        user.setLogin("Gaby");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(person1.equals(person2)).isTrue();
        softAssertions.assertThat(person1.equals(person3)).isFalse();
        softAssertions.assertThat(person1.equals(person4)).isFalse();
        softAssertions.assertThat(person3.equals(person4)).isFalse();
        softAssertions.assertThat(person4.equals(person5)).isTrue();
        softAssertions.assertThat(person4.equals(person6)).isFalse();
        softAssertions.assertThat(person7.equals(person8)).isTrue();
        softAssertions.assertAll();
    }

    @Test
    public void testIntArraysEqualsKey() {
        IIntArrayTest intArrayTest1 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        intArrayTest1.setIds(new int[] { 0, 1, 2 });
        IIntArrayTest intArrayTest2 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        intArrayTest2.setIds(new int[] { 0, 1, 2 });
        IIntArrayTest intArrayTest3 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        intArrayTest3.setIds(new int[] { 0, 1 });
        IIntArrayTest intArrayTest4 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        intArrayTest4.setIds(new int[] {});
        IIntArrayTest intArrayTest5 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        intArrayTest5.setIds(new int[] {});
        IIntArrayTest intArrayTest6 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);
        IIntArrayTest intArrayTest7 = ComponentFactory.getInstance().createInstance(IIntArrayTest.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(intArrayTest1.equals(intArrayTest2)).isTrue();
        softAssertions.assertThat(intArrayTest1.equals(intArrayTest2)).isTrue();
        softAssertions.assertThat(intArrayTest1.equals(intArrayTest3)).isFalse();
        softAssertions.assertThat(intArrayTest1.equals(intArrayTest4)).isFalse();
        softAssertions.assertThat(intArrayTest4.equals(intArrayTest5)).isTrue();
        softAssertions.assertThat(intArrayTest6.equals(intArrayTest7)).isTrue();
        softAssertions.assertThat(intArrayTest1.hashCode()).isEqualTo(intArrayTest2.hashCode());
        softAssertions.assertThat(intArrayTest1.hashCode()).isNotEqualTo(intArrayTest3.hashCode());
        softAssertions.assertThat(intArrayTest4.hashCode()).isEqualTo(intArrayTest5.hashCode());
        softAssertions.assertThat(intArrayTest6.hashCode()).isEqualTo(intArrayTest7.hashCode());
        softAssertions.assertAll();
    }

    @Test
    public void testStringArraysEqualsKey() {
        IStringArrayTest stringArrayTest1 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest1.setIds(new String[] { "0", "1", "2" });
        IStringArrayTest stringArrayTest2 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest2.setIds(new String[] { "0", "1", "2" });
        IStringArrayTest stringArrayTest3 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest3.setIds(new String[] { "0", "1" });
        IStringArrayTest stringArrayTest4 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest4.setIds(new String[] {});
        IStringArrayTest stringArrayTest5 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest5.setIds(new String[] {});
        IStringArrayTest stringArrayTest6 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        IStringArrayTest stringArrayTest7 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        IStringArrayTest stringArrayTest8 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest8.setIds(new String[] { null });
        IStringArrayTest stringArrayTest9 = ComponentFactory.getInstance().createInstance(IStringArrayTest.class);
        stringArrayTest9.setIds(new String[] { null });

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest2)).isTrue();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest2)).isTrue();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest3)).isFalse();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest4)).isFalse();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest6)).isFalse();
        softAssertions.assertThat(stringArrayTest1.equals(stringArrayTest8)).isFalse();
        softAssertions.assertThat(stringArrayTest4.equals(stringArrayTest5)).isTrue();
        softAssertions.assertThat(stringArrayTest6.equals(stringArrayTest7)).isTrue();
        softAssertions.assertThat(stringArrayTest8.equals(stringArrayTest9)).isTrue();
        softAssertions.assertThat(stringArrayTest1.hashCode()).isEqualTo(stringArrayTest2.hashCode());
        softAssertions.assertThat(stringArrayTest1.hashCode()).isNotEqualTo(stringArrayTest3.hashCode());
        softAssertions.assertThat(stringArrayTest4.hashCode()).isEqualTo(stringArrayTest5.hashCode());
        softAssertions.assertThat(stringArrayTest6.hashCode()).isEqualTo(stringArrayTest7.hashCode());
        softAssertions.assertThat(stringArrayTest8.hashCode()).isEqualTo(stringArrayTest9.hashCode());
        softAssertions.assertAll();
    }

    @Test
    public void testHashCode() {
        IOffer offer1 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer1.setId(1L);
        IOffer offer2 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer2.setId(1L);
        IOffer offer3 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer3.setId(2L);
        IOffer offer4 = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer4.setId(null);
        ITest3 test3 = ComponentFactory.getInstance().createInstance(ITest3.class);
        ITest3 test4 = ComponentFactory.getInstance().createInstance(ITest3.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(offer1.hashCode()).isNotNull().isEqualTo(offer2.hashCode());
        softAssertions.assertThat(offer1.hashCode()).isNotNull().isNotEqualTo(offer3.hashCode());
        softAssertions.assertThat(offer3.hashCode()).isNotNull().isNotEqualTo(offer4.hashCode());
        softAssertions.assertThat(test3.hashCode()).isNotNull().isNotEqualTo(test4.hashCode());
        softAssertions.assertAll();
    }

    @Test
    public void testNotNullEqualsKey() {
        INotNullEqualsKey notNullEqualsKey1 = ComponentFactory.getInstance().createInstance(INotNullEqualsKey.class);
        notNullEqualsKey1.setFirstName("Gabriel");
        INotNullEqualsKey notNullEqualsKey2 = ComponentFactory.getInstance().createInstance(INotNullEqualsKey.class);
        notNullEqualsKey2.setFirstName("Gabriel");
        INotNullEqualsKey notNullEqualsKey3 = ComponentFactory.getInstance().createInstance(INotNullEqualsKey.class);
        INotNullEqualsKey notNullEqualsKey4 = ComponentFactory.getInstance().createInstance(INotNullEqualsKey.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(notNullEqualsKey1.equals(notNullEqualsKey2)).isTrue();
        softAssertions.assertThat(notNullEqualsKey1.equals(notNullEqualsKey3)).isFalse();
        softAssertions.assertThat(notNullEqualsKey3.equals(notNullEqualsKey4)).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testPropertyChangeListener() throws InterruptedException {
        BlockingQueue<PropertyChangeEvent> queue = Queues.newLinkedBlockingQueue();
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    queue.put(evt);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Put is no executed", e);
                }
            }
        };

        IPerson person = ComponentFactory.getInstance().createInstance(IPerson.class);
        person.addPropertyChangeListener(propertyChangeListener);

        person.setLastName("allaigre");
        PropertyChangeEvent evt = queue.take();
        Assertions.assertThat(evt).isNotNull();
        Assertions.assertThat(evt.getPropertyName()).isEqualTo("lastName");
        Assertions.assertThat(evt.getOldValue()).isNull();
        Assertions.assertThat(evt.getNewValue()).isEqualTo("allaigre");
    }

    @Test
    public void testComponentFactoryListener() throws InterruptedException {
        BlockingQueue<IComponent> queue = Queues.newLinkedBlockingQueue();
        IComponentFactoryListener componentFactoryListener = new IComponentFactoryListener() {
            @Override
            public <G extends IComponent> void afterCreated(Class<G> interfaceClass, G instance) {
                try {
                    queue.put(instance);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Put is no executed", e);
                }
            }
        };

        ComponentFactory.getInstance().addComponentFactoryListener(componentFactoryListener);

        IPerson person = ComponentFactory.getInstance().createInstance(IPerson.class);

        IComponent instance = queue.take();
        Assertions.assertThat(instance).isEqualTo(person);
    }
}
