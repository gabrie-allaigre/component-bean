package com.talanlabs.component.annotation.factory;

import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.helper.ClassGenerationHelper;
import com.talanlabs.component.annotation.helper.ComponentBeanMethod;
import com.talanlabs.component.annotation.helper.GenerationHelper;
import com.talanlabs.component.annotation.helper.ReflectionHelper;
import com.talanlabs.component.annotation.processor.IProcessManager;
import org.apache.commons.lang3.tuple.Pair;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class ComponentBeanFieldsFactory {

    private ComponentBeanFieldsFactory() {
        super();
    }

    public static void processFields(IProcessManager processManager, TypeElement typeElement) {
        ClassGenerationHelper writer = null;
        try {
            ReflectionHelper reflection = new ReflectionHelper(processManager.getEnvironment(), typeElement);
            String dtoElementSimpleName = reflection.getSimpleClassName();
            String dtoSimpleName = GenerationHelper.rename(dtoElementSimpleName) + "Fields";
            String packageName = reflection.getPackageName();
            String dtoClassName = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName : dtoSimpleName;

            processManager.printMessage("Generating '" + dtoClassName + "' from '" + dtoElementSimpleName + "'.");

            Writer sourceWriter = processManager.getEnvironment().getFiler().createSourceFile(dtoClassName, typeElement).openWriter();
            writer = new ClassGenerationHelper(sourceWriter);

            if (processManager.isAddGeneratedInfo()) {
                writer.println("// {0} - Generating fields from {1}", SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.DEFAULT).format(new Date()), typeElement.asType().toString());
            }

            if (packageName != null && !packageName.isEmpty()) {
                writer.generatePackageDeclaration(packageName);
                writer.println();
            }
            writer.println("import com.talanlabs.component.field.*;");
            writer.println("import com.talanlabs.component.helper.*;");

            writer.println("@com.talanlabs.component.annotation.GeneratedFrom({0}.class)", reflection.rawTypeToString(typeElement.asType(), '.'));

            writer.generateClassHeader(dtoSimpleName, null, Arrays.asList(Modifier.PUBLIC, Modifier.FINAL));
            writer.println();
            writer.println();

            writer.println("\tprivate {0}() { super(); }", dtoSimpleName);
            writer.println();

            writer.println("\tpublic static final String CLASS_NAME = \"{0}\";", reflection.rawTypeToString(typeElement.asType(), '.'));
            writer.println();

            Set<String> dejaVus = new HashSet<>();

            for (ExecutableElement executableElement : reflection.getMethods()) {
                ComponentBeanMethod cbm = ComponentBeanMethod.which(executableElement);
                switch (cbm) {
                case COMPUTED_GET:
                case DEFAULT_GET:
                case GET:
                    String propertyName = cbm.inferName(executableElement);
                    if (!dejaVus.contains(propertyName)) {
                        dejaVus.add(propertyName);

                        Pair<TypeElement, Boolean> returnType = getReturnType(processManager, typeElement, executableElement);

                        String propertyNameUnderscrore = propertyName + "_";

                        writer.println("\tpublic static final String {0} = \"{1}\";", propertyName, propertyName);
                        if (returnType.getRight()) {
                            ReflectionHelper rh = new ReflectionHelper(processManager.getEnvironment(), returnType.getLeft());
                            String sn = GenerationHelper.rename(rh.getSimpleClassName()) + "Fields.SubFields";
                            String pn = rh.getPackageName();
                            String cn = pn != null && !pn.isEmpty() ? pn + "." + sn : sn;

                            writer.println("\tprivate static IDotField<{1}> {0} = null;", propertyNameUnderscrore, cn);
                            writer.println("\tpublic static synchronized IDotField<{1}> {0}() { if ({2} == null) { {2} = new DefaultDotField<{1}>(new {1}({0}),{0}); } return {2}; }", propertyName, cn,
                                    propertyNameUnderscrore);
                        } else {
                            writer.println("\tprivate static IField {0} = null;", propertyNameUnderscrore);
                            writer.println("\tpublic static synchronized IField {0}() { if ({1} == null) { {1} = new DefaultField({0}); } return {1}; }", propertyName, propertyNameUnderscrore);
                        }
                        writer.println();
                    }
                    break;
                default:
                    break;
                }
            }

            writer.println("\tpublic static final class SubFields {");
            writer.println();
            writer.println("\t\tprivate String parentName;");
            writer.println();
            writer.println("\t\tpublic SubFields(String parentName) { super(); this.parentName = parentName; }");
            writer.println();

            dejaVus.clear();
            for (ExecutableElement executableElement : reflection.getMethods()) {
                ComponentBeanMethod cbm = ComponentBeanMethod.which(executableElement);
                switch (cbm) {
                case COMPUTED_GET:
                case DEFAULT_GET:
                case GET:
                    String propertyName = cbm.inferName(executableElement);
                    if (!dejaVus.contains(propertyName)) {
                        dejaVus.add(propertyName);

                        Pair<TypeElement, Boolean> returnType = getReturnType(processManager, typeElement, executableElement);
                        String propertyNameUnderscrore = propertyName + "_";
                        if (returnType.getRight()) {
                            ReflectionHelper rh = new ReflectionHelper(processManager.getEnvironment(), returnType.getLeft());
                            String sn = GenerationHelper.rename(rh.getSimpleClassName()) + "Fields.SubFields";
                            String pn = rh.getPackageName();
                            String cn = pn != null && !pn.isEmpty() ? pn + '.' + sn : sn;

                            writer.println("\t\tprivate IDotField<{1}> {0} = null;", propertyNameUnderscrore, cn);
                            writer.println(
                                    "\t\tpublic synchronized IDotField<{1}> {0}() { if ({2} == null) { {2} = new DefaultDotField<{1}>(new {1}(ComponentHelper.PropertyDotBuilder.build(parentName,{0})),ComponentHelper.PropertyDotBuilder.build(parentName,{0})); } return {2}; }",
                                    propertyName, cn, propertyNameUnderscrore);
                        } else {
                            writer.println("\t\tprivate IField {0} = null;", propertyNameUnderscrore);
                            writer.println(
                                    "\t\tpublic synchronized IField {0}() { if ({1} == null) { {1} = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,{0})); } return {1}; }",
                                    propertyName, propertyNameUnderscrore);
                        }
                        writer.println();
                    }
                    break;
                default:
                    break;
                }
            }
            writer.println("\t}");

            writer.generateFooter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static Pair<TypeElement, Boolean> getReturnType(IProcessManager processManager, Element element, ExecutableElement executableElement) {
        TypeElement te = null;
        boolean anno = false;
        ExecutableType et = (ExecutableType) processManager.getEnvironment().getTypeUtils().asMemberOf((DeclaredType) (element.asType()), executableElement);
        if (et.getReturnType() instanceof DeclaredType) {
            DeclaredType dt = (DeclaredType) et.getReturnType();
            if (dt.asElement() instanceof TypeElement) {
                te = (TypeElement) dt.asElement();
                anno = te.getAnnotation(ComponentBean.class) != null;
            }
        }
        return Pair.of(te, anno);
    }
}
