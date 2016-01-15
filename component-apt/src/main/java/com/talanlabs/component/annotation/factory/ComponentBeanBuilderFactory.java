package com.talanlabs.component.annotation.factory;

import com.talanlabs.component.annotation.helper.ClassGenerationHelper;
import com.talanlabs.component.annotation.helper.ComponentBeanMethod;
import com.talanlabs.component.annotation.helper.GenerationHelper;
import com.talanlabs.component.annotation.helper.ReflectionHelper;
import com.talanlabs.component.annotation.processor.IProcessManager;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ComponentBeanBuilderFactory {

    private ComponentBeanBuilderFactory() {
        super();
    }

    public static void processBuilder(IProcessManager processManager, TypeElement typeElement) {
        ClassGenerationHelper writer = null;
        try {
            ReflectionHelper reflection = new ReflectionHelper(processManager.getEnvironment(), typeElement);
            String dtoElementSimpleName = reflection.getSimpleClassName();
            String dtoSimpleName = GenerationHelper.rename(dtoElementSimpleName) + "Builder";
            String packageName = reflection.getPackageName();
            String dtoClassName = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName : dtoSimpleName;

            processManager.printMessage("Generating '" + dtoClassName + "' from '" + dtoElementSimpleName + "'.");

            Writer sourceWriter = processManager.getEnvironment().getFiler().createSourceFile(dtoClassName, typeElement).openWriter();
            writer = new ClassGenerationHelper(sourceWriter);

            if (processManager.isAddGeneratedInfo()) {
                writer.println("// {0} - Generating builder from {1}", SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.DEFAULT).format(new Date()), typeElement.asType().toString());
            }

            if (packageName != null && !packageName.isEmpty()) {
                writer.generatePackageDeclaration(packageName);
                writer.println();
            }

            List<? extends TypeMirror> typeArguments = ((DeclaredType) typeElement.asType()).getTypeArguments();
            String globalGenericString = reflection.typeArgumentsToString(typeArguments, true, null, null);

            String genericString = "";
            if (typeElement.getTypeParameters() != null && !typeElement.getTypeParameters().isEmpty()) {
                List<String> ss = typeElement.getTypeParameters().stream().map(tpe -> tpe.asType().toString()).collect(Collectors.toList());
                genericString = "<" + String.join(",", ss) + ">";
            }

            boolean useGeneric = StringUtils.isNotEmpty(genericString);

            writer.println("@com.talanlabs.component.annotation.GeneratedFrom({0}.class)", reflection.rawTypeToString(typeElement.asType(), '.'));

            writer.generateClassHeader(dtoSimpleName + globalGenericString, null, Arrays.asList(Modifier.PUBLIC, Modifier.FINAL));
            writer.println();
            writer.println();

            if (useGeneric) {
                writer.println("\tprivate final java.lang.reflect.Type type;");
            }
            writer.println("\tprivate final java.util.Map<String,Object> builder;");
            writer.println();

            if (useGeneric) {
                writer.println("\tprivate {0}(java.lang.reflect.Type type) {", dtoSimpleName);
                writer.println("\t\tthis.type = type;");
            } else {
                writer.println("\tprivate {0}() {", dtoSimpleName);
            }
            writer.println("\t\tthis.builder = new java.util.HashMap<String,Object>();", dtoElementSimpleName);
            writer.println("\t}");
            writer.println();

            if (useGeneric) {
                writer.println("\tpublic static {0} {1}{2} newBuilder(java.lang.reflect.Type type) {", globalGenericString, dtoSimpleName, genericString);
                writer.println("\t\treturn new {0}{1}(type);", dtoSimpleName, genericString);
            } else {
                writer.println("\tpublic static {0} newBuilder() {", dtoSimpleName);
                writer.println("\t\treturn new {0}();", dtoSimpleName);
            }

            writer.println("\t}");
            writer.println();

            writer.println("\tpublic {0}{1} _copy({2}{3} component) {", dtoSimpleName, genericString, reflection.getClassRepresenter().getQualifiedName(), genericString);
            writer.println("\t\tif (component != null) {");
            writer.println("\t\t\tbuilder.putAll(component.straightGetProperties());");
            writer.println("\t\t}");
            writer.println("\t\treturn this;");
            writer.println("\t}");
            writer.println();

            Set<String> dejaVus = new HashSet<>();

            for (ExecutableElement executableElement : reflection.getMethods()) {
                ComponentBeanMethod cbm = ComponentBeanMethod.which(executableElement);
                switch (cbm) {
                case SET:
                    String propertyName = cbm.inferName(executableElement);
                    if (!dejaVus.contains(propertyName)) {
                        dejaVus.add(propertyName);

                        ExecutableType et = (ExecutableType) processManager.getEnvironment().getTypeUtils().asMemberOf((DeclaredType) (typeElement.asType()), executableElement);

                        TypeMirror tm = et.getParameterTypes().get(0);

                        writer.println("\tpublic {0} {1}{2} {3}({4} {3}) {", reflection.typeArgumentsToString(et.getTypeVariables(), true, null, null), dtoSimpleName, genericString, propertyName,
                                tm.toString());
                        writer.println("\t\tbuilder.put(\"{0}\",{0});", propertyName);
                        writer.println("\t\treturn this;");
                        writer.println("\t}");
                        writer.println();
                    }
                    break;
                default:
                    break;
                }
            }

            writer.println("\tpublic {0}{1} build() {", reflection.getClassRepresenter().getQualifiedName(), genericString);
            if (useGeneric) {
                writer.println("\t\t{0}{1} component = ({0}{1})com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance(type);",
                        reflection.getClassRepresenter().getQualifiedName(), genericString);
            } else {
                writer.println("\t\t{0} component = com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance({0}.class);", reflection.getClassRepresenter().getQualifiedName());
            }
            writer.println("\t\tcomponent.straightSetProperties(builder);");
            writer.println("\t\treturn component;");
            writer.println("\t}");

            writer.generateFooter();
        } catch (Exception e) {
            processManager.printError("Error with " + typeElement);
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
