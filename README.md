IComponent allows:

- Simplify the creation of Java Bean
- No need to write hashcode, equals, toString and serialization
- Methods for easier access to properties
- They make it possible to make multiple inheritance.
- Automatic creation of a Builder and the list of fields
- Creation of Dto and the mapper

To use it, add the dependency in the 'pom.xml' of the project:

```xml
<dependencies>
    <dependency>
        <groupId>com.talanlabs</groupId>
        <artifactId>component-bean</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

# Annotation Engine

To use the annation engine, you must add in the plugins of the Maven pom:

```xml
<plugin>
    <groupId>org.bsc.maven</groupId>
    <artifactId>maven-processor-plugin</artifactId>
    <version>2.2.1</version>
    <executions>
        <execution>
            <id>process</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>process</goal>
            </goals>
            <configuration>
                <processors>
                    <processor>com.talanlabs.component.annotation.processor.ComponentBeanProcessor</processor>
                </processors>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.talanlabs</groupId>
            <artifactId>component-bean-apt</artifactId>
            <version>1.0.1</version>
        </dependency>
    </dependencies>
</plugin>
```

# The base

Create an ICountry interface that implements the class IComponent:
    
```java
@ComponentBean
public interface ICountry extends IComponent {

}
```

We will see later the role of the announcement @ComponentBean

Then we can add the getter and setter of a field 'code' and a field 'meaning':

```java
@ComponentBean
public interface ICountry extends IComponent {

	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```

Now we can create a Main class, which will create an instance and fill in the fields:

```java
ICountry country = ComponentFactory.getInstance().createInstance(ICountry.class);
country.setCode("FRA");
country.setMeaning("France");
System.out.println(country);
```

Or with the @ComponentBean annotation, a builder class has been created.

```java
ICountry country = CountryBuilder.newBuilder().code("FRA").meaning("France").build();
System.out.println(country);
```

The hashcode and the equals are automatically managed by adding the annotation @EqualsKey in front of the getter.

```java
@ComponentBean
public interface ICountry extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```
```java
ICountry country1 = CountryBuilder.newBuilder().code("FRA").meaning("France").build();
ICountry country2 = CountryBuilder.newBuilder().code("FRA").meaning("Italie").build();
ICountry country3 = CountryBuilder.newBuilder().code("ESP").meaning("Espagne").build();

System.out.println(country1.equals(country2)); // true
System.out.println(country1.equals(country3)); // false
```

You can add multiple @EqualsKey to have multiple keys. It contains a parameter `nullEquals`, by default it is` true`, it allows to accept or not the comparison `null == null`.

We can add multiple inheritance, this allows to add fields easily:

```java
@ComponentBean
public interface ICountry extends IGeolocalisation, INommage, IComponent {
    ...
```

The @Computed annotation is used to create a calculated method. The annotation takes a class in parameter. It will be instantiated only once.
You can change the way you instantiate it by changing ComponentFactory.getInstance (). SetComputedFactory
It can take parameters. If the method starts with get will be considered as a getter and will look for a setter. It is proposed in the list of properties.

```java
@ComponentBean
public interface ICountry extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

    @Computed(CountryComputed.class)
    String total();
    
    @Computed(CountryComputed.class)
    String total(String separator);

}
```

The Computed class must contain the same method, same name, same return type. In first parameter the class of origin and then the parameters of the method to be computed.

```java
public class CountryComputed {

	public String total(ICountry country) {
		return total(country, " ");
	}

	public String total(ICountry country, String separator) {
		return country.getCode() + separator + country.getMeaning();
	}
}
```

```java
ICountry country = CountryBuilder.newBuilder().code("FRA").meaning("France").build();

System.out.println(country.total());
System.out.println(country.total(" - "));
```

# Default Java 8

Instead of using Computed, it is possible to do default methods

```java
@ComponentBean
public interface ICountry extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

    default String total() {
        return total( " ");
    }
    
    default String total(String separator) {
        return country.getCode() + separator + country.getMeaning();
    }

}
```

# Accessors

To read a value from a property you can either use the getter that matches either straightGetProperty or straightGetProperties

```java
System.out.println(country.getCode());
System.out.println(country.straightGetProperty("code"));
System.out.println(country.straightGetProperties().get("code"));
```

To avoid using String for property names, the @ComponentBean annotation creates a Class Fields

```java
System.out.println(country.straightGetProperty(CountryFields.code().name()));
System.out.println(country.straightGetProperty(CountryFields.code));
```

If you have an ICity class that contains an ICountry:

```java
@ComponentBean
public interface ICity extends IComponent {

    ...

    ICountry getCountry();
    
    void setCountry(ICountry country);

    ...
}
```

From CityFields you can get the ICountry code

```java
System.out.println(CityFields.country().dot().code().name()); // "country.code"
ComponentHelper.getValue(city, CityFields.country().dot().code().name());
```

To write a value

```java
country.setCode("FRA");
country.straightSetProperty(CountryFields.code().name(), "FRA");
Map<String, Object> map = country.straightGetProperties();
map.put(CountryFields.code().name(), "FRA");
country.straightSetProperties(map);
```

Know the list of properties and their type

```java
System.out.println(country.straightGetPropertyNames());
System.out.println(country.straightGetPropertyClass(CountryFields.code().name()));
```

**The list of properties contains all getter (normal, default or computed)**

You can also use the ComponentDescriptor from the class:

```java
System.out.println(ComponentFactory.getInstance().isComponentType(country)); // true
Class<ICountry> componentClass = ComponentFactory.getInstance().getComponentClass(country);
ComponentDescriptor cd = ComponentFactory.getInstance().getDescriptor(ICountry.class);
ComponentDescriptor cd = ComponentFactory.getInstance().getDescriptor(country);
```

From the ComponentDescriptor one can have access to all the properties.

```java
cd.getPropertyDescriptors();
cd.getPropertyDescriptor(CityFields.country);
```

# ToStringFactory

The `toString` of Component can be changed from ToStringFactory:

```java
ComponentFactory.setInstance(new ComponentFactory(ComponentFactoryConfigurationBuilder.newBuilder().toStringFactory(IToStringFactory.simple()).build()));
```

There are 3 default factory types:

| Class  | Usage | Description  |
|---|---|---|
| `SimpleToStringFactory` | `IToStringFactory.simple()` | Displays only the name of the Component and its hashcode |
| `CompleteToStringFactory` | `IToStringFactory.complete()` | By default displays a line with the name of the Component, its hashcode and all the fields, in order the EqualsKey then in alphabetical order |
| `CompleteToStringFactory` | `new CompleteToStringFactory(...)` | Allows to customize the Complete: hidden the Header, multiline, hide the null values, change the order of display of fields, include / exclude fields ... |
| `ClassToStringFactory` | `new ClassToStringFactory(defaultToStringFactory)` | Allows you to specify the toString according to the Component: `classToStringFactory.put(IMonComponent.class,monToStringFactory)` |

For the sorting of fields, it is possible to cumulate the sorting:

| Class  | Usage | Description  |
|---|---|---|
| None | `IPropertyComparator.natural()` | Fields are sorted alphabetically |
| `EqualsKeyPropertyComparator` | `IPropertyComparator.equalsKey()` | The `@EqualsKey` fields are first |
| `FirstPropertyComparator` | `IPropertyComparator.first(propertyNames)` | The given fields will be put first |
| `FirstPropertyComparator` | `IPropertyComparator.first(componentClass)` | The fields contained in the ComponentClass will be put first |
| `LastPropertyComparator` | `IPropertyComparator.last(propertyNames)` | The given fields will be set last |
| `LastPropertyComparator` | `IPropertyComparator.last(componentClass)` | The fields contained in the ComponentClass will be set last |
| `NullValuePropertyComparator` | `IPropertyComparator.nullsLast()` | Null fields are set last |
| `NullValuePropertyComparator` | `IPropertyComparator.nullsFirst()` | Null fields are listed first |
| None | `IPropertyComparator.compose(propertyComparators)` | Fields are sorted according to the order of the comparators | 

Example:

```java
// In order first the EqualsKey, "version", and last the fields of ITracable and the null values ​​and each group sorted in alphabetical order
IPropertyComparator defaultPropertyComparator = IPropertyComparator
                .compose(IPropertyComparator.equalsKey(), IPropertyComparator.first(EntityFields.version), IPropertyComparator.last(ITracable.class), new NullValuePropertyComparator(),
                        IPropertyComparator.natural());
```
