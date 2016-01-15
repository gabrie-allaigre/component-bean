package com.talanlabs.component.test.unit;

import com.google.common.collect.Sets;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.field.DefaultField;
import com.talanlabs.component.helper.ComponentHelper;
import com.talanlabs.component.test.data.IAddress;
import com.talanlabs.component.test.data.IUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentHelperTest {

    @Test
    public void testBuildComponentsMap() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("sandra");

        Map<String, List<IUser>> map = ComponentHelper.buildComponentsMap(Arrays.asList(user1, user2, user3), "login");

        Assertions.assertThat(map).hasSize(2).containsOnlyKeys("gaby", "sandra");
        Assertions.assertThat(map.get("gaby")).isNotNull().isNotEmpty().hasSize(2);
        Assertions.assertThat(map.get("sandra")).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void testExtractValues() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("sandra");

        List<String> res = ComponentHelper.extractValues(Arrays.asList(user1, user2, user3), "login");

        Assertions.assertThat(res).hasSize(3).containsOnly("gaby", "gaby", "sandra");
    }

    @Test
    public void testGetComponentClass() {
        Assertions.assertThat(ComponentHelper.getComponentClass(IUser.class, "login")).isEqualTo(IUser.class);
        Assertions.assertThat(ComponentHelper.getComponentClass(IUser.class, "address.city")).isEqualTo(IAddress.class);
    }

    @Test
    public void testGetFinalClass() {
        Assertions.assertThat(ComponentHelper.getFinalClass(IUser.class, "login")).isEqualTo(String.class);
        Assertions.assertThat(ComponentHelper.getFinalClass(IUser.class, "address")).isEqualTo(IAddress.class);
        Assertions.assertThat(ComponentHelper.getFinalClass(IUser.class, "address.city")).isEqualTo(String.class);
    }

    @Test
    public void testGetPropertyName() {
        Assertions.assertThat(ComponentHelper.getPropertyName(IUser.class, "login")).isEqualTo("login");
        Assertions.assertThat(ComponentHelper.getPropertyName(IUser.class, "address")).isEqualTo("address");
        Assertions.assertThat(ComponentHelper.getPropertyName(IUser.class, "address.city")).isEqualTo("city");
    }

    @Test
    public void testGetFinalPropertyDescriptor() {
        Assertions.assertThat(ComponentHelper.getFinalPropertyDescriptor(IUser.class, "login").getPropertyClass()).isEqualTo(String.class);
        Assertions.assertThat(ComponentHelper.getFinalPropertyDescriptor(IUser.class, "address").getPropertyClass()).isEqualTo(IAddress.class);
        Assertions.assertThat(ComponentHelper.getFinalPropertyDescriptor(IUser.class, "address.city").getPropertyClass()).isEqualTo(String.class);
    }

    @Test
    public void testClone() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setCity("Valence");
        user1.setAddress(address);

        IUser user2 = ComponentHelper.clone(user1);

        Assertions.assertThat(user2).isEqualTo(user1);
        Assertions.assertThat(user2).isNotSameAs(user1);
        Assertions.assertThat(user2.getAddress()).isNotEqualTo(user1.getAddress());
        Assertions.assertThat(user2.getAddress()).isNotSameAs(user1.getAddress());
    }

    @Test
    public void testFindComponentBy() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("sandra");

        IUser res = ComponentHelper.findComponentBy(Arrays.asList(user1, user2, user3), "login", "sandra");
        Assertions.assertThat(res).isSameAs(user3);
    }

    @Test
    public void testFindComponentsBy() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("sandra");

        List<IUser> res = ComponentHelper.findComponentsBy(Arrays.asList(user1, user2, user3), "login", "sandra");
        Assertions.assertThat(res).hasSize(1).containsOnly(user3);
    }

    @Test
    public void testIndexComponentOf() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setLogin("sandra");

        int res = ComponentHelper.indexComponentOf(Arrays.asList(user1, user2, user3), "login", "sandra");
        Assertions.assertThat(res).isEqualTo(2);
    }

    @Test
    public void testGetValue() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setCity("Valence");
        user1.setAddress(address);

        Assertions.assertThat(ComponentHelper.<String>getValue(user1, "login")).isNotNull().isEqualTo("gaby");
        Assertions.assertThat(ComponentHelper.<String>getValue(user1, "name")).isNull();
        Assertions.assertThat(ComponentHelper.<String>getValue(user1, "address.city")).isNotNull().isEqualTo("Valence");
        Assertions.assertThat(ComponentHelper.<String>getValue(user1, "address.country")).isNull();

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSerializeComponent() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setCity("Valence");
        user1.setAddress(address);

        Map<String, Serializable> map = ComponentHelper.serializeComponent(user1);

        Assertions.assertThat(map).hasSize(3).containsOnlyKeys("login", "address", "fullname");
        Assertions.assertThat(map.get("login")).isNotNull().isEqualTo("gaby");
        Assertions.assertThat(map.get("address")).isNotNull();
        Assertions.assertThat((Map<String, Serializable>) map.get("address")).hasSize(2).containsOnlyKeys("city", "fullname");
        Assertions.assertThat(((Map<String, Serializable>) map.get("address")).get("city")).isEqualTo("Valence");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeserializeComponent() {
        Map<String, Serializable> map = new HashMap<>();
        map.put("login", "gaby");
        Map<String, Serializable> map2 = new HashMap<>();
        map2.put("city", "Valence");
        map.put("address", (Serializable) map2);

        IUser user = ComponentHelper.deserializeComponent(IUser.class, map);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getLogin()).isNotNull().isEqualTo("gaby");
        Assertions.assertThat(user.getNikename()).isNull();
        Assertions.assertThat(user.getAddress()).isNotNull();
        Assertions.assertThat(user.getAddress().getCity()).isNotNull().isEqualTo("Valence");
        Assertions.assertThat(user.getAddress().getCountry()).isNull();
    }

    @Test
    public void testLoadComponent() throws ClassNotFoundException {
        Assertions.assertThat(ComponentHelper.loadComponentClass(IUser.class.getName())).isEqualTo(IUser.class);
        Assertions.assertThat(Assertions.catchThrowable(() -> ComponentHelper.loadComponentClass("nexistepas"))).isInstanceOf(ClassNotFoundException.class);
    }

    @Test
    public void testSetNullValues() {
        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);
        user2.setLogin("gaby");
        user2.setEmail("mail");

        Assertions.assertThat(user1.getLogin()).isNotNull();
        ComponentHelper.setNullValues(user1, "login");
        Assertions.assertThat(user1.getLogin()).isNull();

        Assertions.assertThat(user2.getLogin()).isNotNull();
        Assertions.assertThat(user2.getEmail()).isNotNull();
        ComponentHelper.setNullValues(user2, "nexistepas");
        Assertions.assertThat(user2.getLogin()).isNotNull();
        Assertions.assertThat(user2.getEmail()).isNotNull();

        ComponentHelper.setNullValues(user2, "login", "email");
        Assertions.assertThat(user2.getLogin()).isNull();
        Assertions.assertThat(user2.getEmail()).isNull();
    }

    @Test
    public void testCopy() {
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setCity("Valence");
        address.setCountry("France");

        IUser user1 = ComponentFactory.getInstance().createInstance(IUser.class);
        user1.setLogin("gaby");
        user1.setEmail("mail");
        user1.setAddress(address);
        user1.setGroups(Arrays.asList("admin", "dev"));

        IUser user2 = ComponentFactory.getInstance().createInstance(IUser.class);

        ComponentHelper.copy(user1, user2, Sets.newHashSet("login"));
        Assertions.assertThat(user2.getLogin()).isEqualTo("gaby");
        Assertions.assertThat(user2.getEmail()).isNull();
        Assertions.assertThat(user2.getAddress()).isNull();

        IUser user3 = ComponentFactory.getInstance().createInstance(IUser.class);
        user3.setEmail("none");

        ComponentHelper.copy(user1, user3, Sets.newHashSet("login", "address.city"));
        Assertions.assertThat(user3.getLogin()).isEqualTo("gaby");
        Assertions.assertThat(user3.getEmail()).isEqualTo("none");
        Assertions.assertThat(user3.getAddress()).isNotNull();
        Assertions.assertThat(user3.getAddress().getCity()).isEqualTo("Valence");
        Assertions.assertThat(user3.getAddress().getCountry()).isNull();
    }

    @Test
    public void testPropertyDotBuilder() {
        Assertions.assertThat(ComponentHelper.PropertyDotBuilder.build("name")).isEqualTo("name");
        Assertions.assertThat(ComponentHelper.PropertyDotBuilder.build("address", "city")).isEqualTo("address.city");
        Assertions.assertThat(ComponentHelper.PropertyDotBuilder.build("address", "city")).isEqualTo("address.city");
        Assertions.assertThat(ComponentHelper.PropertyDotBuilder.build(new DefaultField("address"), new DefaultField("city"))).isEqualTo("address.city");

        Assertions.assertThat(new ComponentHelper.PropertyDotBuilder().addPropertyNames("name").addPropertyNames("city", "rue").addPropertyNames(new DefaultField("numero")).build())
                .isEqualTo("name.city.rue.numero");
    }

    @Test
    public void testPropertyArrayBuilder() {
        Assertions.assertThat(ComponentHelper.PropertyArrayBuilder.build("name")).containsOnly("name");
        Assertions.assertThat(ComponentHelper.PropertyArrayBuilder.build("name.city", "prenom")).containsOnly("name.city", "prenom");
        Assertions.assertThat(ComponentHelper.PropertyArrayBuilder.build(new DefaultField("name"), new DefaultField("prenom"))).containsOnly("name", "prenom");

        Assertions.assertThat(new ComponentHelper.PropertyArrayBuilder().addPropertyNames("name", "address.city").addDotPropertyName("offre", "code")
                .addPropertyNames(new DefaultField("age"), new DefaultField("prenom")).addDotPropertyName(new DefaultField("date"), new DefaultField("annee")).build())
                .containsOnly("name", "address.city", "offre.code", "age", "prenom", "date.annee");
    }
}
