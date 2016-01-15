Les IComponent permettent :

- Simplifier la création de Bean Java
- Pas besoin d'ecrire le hashcode, equals, toString et la serialisation
- Des methodes pour accéder plus facilement aux propriétés
- Ils permetent de faire de l'héritage multiple.
- Création automatique d'un Builder et de la liste des champs
- Création de Dto et du mapper

Pour l'utiliser, il faut ajouter dans le 'pom.xml' du projet la dépendance :

```xml
<dependencies>
    <dependency>
        <groupId>com.talanlabs</groupId>
        <artifactId>component-bean</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

# Moteur d'annotation

Pour utiliser le moteur d'annation, il faut rajouter dans les plugins du pom Maven :

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
            <version>1.1.0</version>
        </dependency>
    </dependencies>
</plugin>
```

# La base

Créer une interface ICountry qui implémente la class IComponent :
    
```java
@ComponentBean
public interface ICountry extends IComponent {

}
```

On verra par la suite le role de l'annonation @ComponentBean

Ensuite on peut rajouter les getter et setter d'un champs 'code' et un champs 'meaning' :

```java
@ComponentBean
public interface ICountry extends IComponent {

	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```

Maintenant nous pouvons créer une class Main, qui créera une instance et remplira les champs :

```java
ICountry country = ComponentFactory.getInstance().createInstance(ICountry.class);
country.setCode("FRA");
country.setMeaning("France");
System.out.println(country);
```

Ou grâce à l'annotation @ComponentBean, une classe builder a été créée.

```java
ICountry country = CountryBuilder.newBuilder().code("FRA").meaning("France").build();
System.out.println(country);
```

Le hashcode et le equals sont gérés automatiquement en rajoutant l'annotation @EqualsKey devant le getter.

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

Vous pouvez ajouter plusieurs @EqualsKey pour avoir des clefs multiples. Il contient un paramètre `nullEquals`, par défaut il est à `true`, il permet d'accepter ou pas la comparaison `null == null`.

On peut rajouter de l'héritage multiple, cela permet d'ajouter des champs facilement :

```java
@ComponentBean
public interface ICountry extends IGeolocalisation, INommage, IComponent {
    ...
```

L'annotation @Computed permet de créer une methode calculé. L'annotation prend une class en paramètre. Celle-ci sera instancié qu'une fois.
Vous pouvez changer la façon de l'instancier en changer ComponentFactory.getInstance().setComputedFactory
elle peut prendre des paramètres. Si la méthode commence par get sera considéré comme un getter et recherchera un setter. Elle se proposé dans la liste des properties.

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

La class Computed, doit contenir la même methode, même nom, même type de retour. En premier paramètre la class d'origine et ensuite les paramètres de la méthode à calculer.

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

Au lieu d'utiliser Computed, il est possible de faire des méthodes default

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

# Accesseurs

Pour lire une valeur d'une propriété vous pouvez soit utiliser le getter qui correspond, soit straightGetProperty ou soit straightGetProperties

```java
System.out.println(country.getCode());
System.out.println(country.straightGetProperty("code"));
System.out.println(country.straightGetProperties().get("code"));
```

Pour ne pas utiliser les String pour le nom des propriétés, l'annotation @ComponentBean créée une class Fields

```java
System.out.println(country.straightGetProperty(CountryFields.code().name()));
System.out.println(country.straightGetProperty(CountryFields.code));
```

Si vous avez une classe ICity qui contient un ICountry :

```java
@ComponentBean
public interface ICity extends IComponent {

    ...

    ICountry getCountry();
    
    void setCountry(ICountry country);

    ...
}
```

A partir de CityFields vous pouvez obtenir le code du ICountry

```java
System.out.println(CityFields.country().dot().code().name()); // "country.code"
ComponentHelper.getValue(city, CityFields.country().dot().code().name());
```

Pour écrire une valeur

```java
country.setCode("FRA");
country.straightSetProperty(CountryFields.code().name(), "FRA");
Map<String, Object> map = country.straightGetProperties();
map.put(CountryFields.code().name(), "FRA");
country.straightSetProperties(map);
```

Connaitre la liste des propriétés et leur type

```java
System.out.println(country.straightGetPropertyNames());
System.out.println(country.straightGetPropertyClass(CountryFields.code().name()));
```

**La liste des propriétées contient tous les getter (normal, default ou computed)**

On peut aussi utiliser le ComponentDescriptor à partir de la classe :

```java
System.out.println(ComponentFactory.getInstance().isComponentType(country)); // true
Class<ICountry> componentClass = ComponentFactory.getInstance().getComponentClass(country);
ComponentDescriptor cd = ComponentFactory.getInstance().getDescriptor(ICountry.class);
ComponentDescriptor cd = ComponentFactory.getInstance().getDescriptor(country);
```

A partir du ComponentDescriptor on peut avoir accés à toutes les propriétées.

```java
cd.getPropertyDescriptors();
cd.getPropertyDescriptor(CityFields.country);
```

# ToStringFactory

Le `toString` des Component peuvent être modifié à partir de ToStringFactory :

```java
ComponentFactory.setInstance(new ComponentFactory(ComponentFactoryConfigurationBuilder.newBuilder().toStringFactory(IToStringFactory.simple()).build()));
```

Il existe 3 types da factory par défaut :

| Class  | Usage | Description  |
|---|---|---|
| `SimpleToStringFactory` | `IToStringFactory.simple()` | N'affiche que le nom du Component et son hashcode |
| `CompleteToStringFactory` | `IToStringFactory.complete()` | Par défaut affiche une ligne avec le nom du Component, son hashcode et tous les champs, dans l'ordre les EqualsKey puis par ordre alphabétique |
| `CompleteToStringFactory` | `new CompleteToStringFactory(...)` | Permet de plus personaliser le Complete : caché le Header, multiligne, cacher les valeurs null, changer l'ordre d'affichage des champs, inclure/exclure des champs... |
| `ClassToStringFactory` | `new ClassToStringFactory(defaultToStringFactory)` | Permet de spécifier le toString selon le Component : `classToStringFactory.put(IMonComponent.class,monToStringFactory)` |

Pour le tri des champs, il est possible de cummuler les tri :

| Class  | Usage | Description  |
|---|---|---|
| Aucune | `IPropertyComparator.natural()` | Les champs sont triés par ordre alphabétique |
| `EqualsKeyPropertyComparator` | `IPropertyComparator.equalsKey()` | Les champs `@EqualsKey` sont mis en premier |
| `FirstPropertyComparator` | `IPropertyComparator.first(propertyNames)` | Les champs donnée seront mis en premier |
| `FirstPropertyComparator` | `IPropertyComparator.first(componentClass)` | Les champs contenues dans le ComponentClass seront mis en premier |
| `LastPropertyComparator` | `IPropertyComparator.last(propertyNames)` | Les champs donnée seront mis en dernier |
| `LastPropertyComparator` | `IPropertyComparator.last(componentClass)` | Les champs contenues dans le ComponentClass seront mis en dernier |
| `NullValuePropertyComparator` | `IPropertyComparator.nullsLast()` | Les champs null sont mis en dernier |
| `NullValuePropertyComparator` | `IPropertyComparator.nullsFirst()` | Les champs null sont mis en premier |
| Aucune | `IPropertyComparator.compose(propertyComparators)` | Les champs sont triés par rapport à l'ordre des comparateurs | 

Exemple :

```java
// Dans l'ordre en premier les EqualsKey, "version", et en dernier les champs de ITracable et les valeurs nulles et chaque groupe trié par ordre alphabétique
IPropertyComparator defaultPropertyComparator = IPropertyComparator
                .compose(IPropertyComparator.equalsKey(), IPropertyComparator.first(EntityFields.version), IPropertyComparator.last(ITracable.class), new NullValuePropertyComparator(),
                        IPropertyComparator.natural());
```

# Generateur de DTO

Il suffit d'ajouter au Component l'annotation `@GenerateDto`

```java
@ComponentBean
@GenerateDto
public interface ICountry extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```

Une class similaire finissant par **Dto** sera crée :

```java
@ComponentBean
@GeneratedFrom(ICountry.class)
public interface ICountryDto extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```

Il est possible d'inclure ou d'exclure des champs, d'inclure ou d'exclure des interfaces

```java
@ComponentBean
@GenerateDto(includeFields = { CountryFields.code }, excludeExtends = { TracableFields.CLASS_NAME })
public interface ICountry extends ITracable, IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

	String getMeaning();

	void setMeaning(String meaning);

}
```

Et la classe créée :

```java
@ComponentBean
@GeneratedFrom(ICountry.class)
public interface ICountryDto extends IComponent {

    @EqualsKey
	String getCode();

	void setCode(String code);

}
```

A partir d'un component, il est possible de définir plusieurs Dto avec des noms différents

```java
@ComponentBean
@GenerateDto
@GenerateDto(name = "SimpleCoutryDto", includeFields = { CountryFields.code }, excludeExtends = { TracableFields.CLASS_NAME })
public interface ICountry extends ITracable, IComponent {
...
```

ou

```java
@ComponentBean
@GenerateDtos({ @GenerateDtos(), @GenerateDto(name = "ISimpleCoutryDto", includeFields = { CountryFields.code }, excludeExtends = { TracableFields.CLASS_NAME }) })
public interface ICountry extends ITracable, IComponent {
...
```

Dans ce cas 2 nouvelles classes seront créées :

```java
@ComponentBean
@GeneratedFrom(ICountry.class)
public interface ICountryDto extends ITracable, IComponent {
...
```

```java
@ComponentBean
@GeneratedFrom(ICountry.class)
public interface ISimpleCoutryDto extends IComponent {
...
```

Si dans un component vous faites référence à un autre component, il sera lui aussi transformé en Dto si le component le permet

```java
ICountry getCountry();

void setCountry(ICountry country);
```

La nouvelle classe créée sera :

```java
ICountryDto getCountry();

void setCountry(ICountryDto country);
```

Cela fonctionne aussi pour toute les classes génériques (List<>, Map<>, etc).

Une classe Mapper est générée pour passer du component au Dto et inversement.

```java
ICountry countryResult = CountryMapper.toCountry(countryDto);
ICountryDto countryDtoResult = CountryMapper.toCountryDto(country);
ISimpleCountryDto simpleCountryDtoResult = CountryMapper.toSimpleCountryDto(country);
```

# Mapper

La classe `ComponentMapper` permet de faire des conversions sur les components.

- Soit d'un Component à autre Component

```java
ICountryDto countryDtoResult = ComponentMapper.getInstance().toComponent(country, ICountryDto.class);
ICountry countryResult = ComponentMapper.getInstance().toComponent(countryDto, ICountry.class);
```

- Soit d'un Bean à un Component et inversement

```java
ICountry countryResult = ComponentMapper.getInstance().toComponent(countryBean, ICountry.class);
CountryBean countryBeanResult = ComponentMapper.getInstance().fromComponent(country, CountryBean.class);
```