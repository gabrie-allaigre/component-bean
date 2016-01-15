package com.talanlabs.component.annotation.factory;

import com.talanlabs.component.annotation.GenerateDto;
import com.talanlabs.component.annotation.helper.ClassGenerationHelper;
import com.talanlabs.component.annotation.helper.ComponentBeanMethod;
import com.talanlabs.component.annotation.helper.GeneratedDtoHelper;
import com.talanlabs.component.annotation.helper.ReflectionHelper;
import com.talanlabs.component.annotation.processor.IProcessManager;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GenerateDtoFactory {

    private GenerateDtoFactory() {
        super();
    }

    public static void processDto(final IProcessManager processManager, GenerateDto generateDto, TypeElement typeElement) {
        ClassGenerationHelper writer = null;
        try {
            ReflectionHelper reflection = new ReflectionHelper(processManager.getEnvironment(), typeElement);
            String dtoElementSimpleName = reflection.getSimpleClassName();
            String dtoSimpleName;
            if (!"".equals(generateDto.name())) {
                dtoSimpleName = generateDto.name();
            } else {
                dtoSimpleName = dtoElementSimpleName + "Dto";
            }
            String packageName = reflection.getPackageName();
            String dtoClassName = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName : dtoSimpleName;

            processManager.printMessage("Generating dto '" + dtoClassName + "' from '" + dtoElementSimpleName + "'.");

            Writer sourceWriter = processManager.getEnvironment().getFiler().createSourceFile(dtoClassName, typeElement).openWriter();
            writer = new ClassGenerationHelper(sourceWriter);

            if (processManager.isAddGeneratedInfo()) {
                writer.println("// {0} - Generating dto from {1}", SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.DEFAULT).format(new Date()), typeElement.asType().toString());
            }

            if (packageName != null && !packageName.isEmpty()) {
                writer.generatePackageDeclaration(packageName);
                writer.println();
            }

            Map<String, String> classMap = new HashMap<>();
            for (String classToClass : generateDto.classToClasss()) {
                String t = classToClass.substring(0, classToClass.indexOf("->"));
                String f = classToClass.substring(classToClass.indexOf("->") + 2);
                classMap.put(t, f);
            }

            List<String> includeExtends = Arrays.asList(generateDto.includeExtends());
            List<String> excludeExtends = Arrays.asList(generateDto.excludeExtends());

            Set<String> extraInterfaces = new HashSet<>(Collections.singleton("com.talanlabs.component.IComponent"));
            if (typeElement.getInterfaces() != null && !typeElement.getInterfaces().isEmpty()) {
                for (TypeMirror tm : typeElement.getInterfaces()) {
                    String typeMirrorName = reflection.typeToString(tm, false, null, null);
                    String name = getName(processManager, reflection, includeExtends, excludeExtends, typeMirrorName, tm, classMap);
                    if (name != null) {
                        extraInterfaces.add(name);
                    }
                }
            }

            writer.println("@com.talanlabs.component.annotation.ComponentBean(createBuilder = true, createFields = true)");
            writer.println("@com.talanlabs.component.annotation.GeneratedFrom({0}.class)", reflection.rawTypeToString(typeElement.asType(), '.'));

            String generic = reflection
                    .typeArgumentsToString(((DeclaredType) typeElement.asType()).getTypeArguments(), true, null, GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap));

            writer.generateInterfaceHeader(dtoSimpleName + generic, Collections.singletonList(Modifier.PUBLIC), extraInterfaces.toArray(new String[extraInterfaces.size()]));
            writer.println();
            writer.println();

            List<String> includeFields = Arrays.asList(generateDto.includeFields());
            List<String> excludeFields = Arrays.asList(generateDto.excludeFields());

            for (ExecutableElement executableElement : reflection.getMethods()) {
                if (typeElement.equals(executableElement.getEnclosingElement())) {
                    ExecutableType et = (ExecutableType) processManager.getEnvironment().getTypeUtils().asMemberOf((DeclaredType) (typeElement.asType()), executableElement);
                    ComponentBeanMethod cbm = ComponentBeanMethod.which(executableElement);
                    switch (cbm) {
                    case GET: {
                        String propertyName = cbm.inferName(executableElement);
                        TypeMirror tm = executableElement.getReturnType();

                        boolean array = false;
                        if (TypeKind.ARRAY.equals(tm.getKind())) {
                            ArrayType ay = (ArrayType) tm;
                            tm = ay.getComponentType();
                            array = true;
                        }

                        String name = getName(processManager, reflection, includeFields, excludeFields, propertyName, tm, classMap);
                        if (name != null) {
                            for (AnnotationMirror am : executableElement.getAnnotationMirrors()) {
                                if (am.toString().equals("@com.talanlabs.component.IComponent.EqualsKey")) {
                                    writer.println("\t{0}", am.toString());
                                }
                            }

                            writer.println("\t{0} {1}{2} {3}();",
                                    reflection.typeArgumentsToString(et.getTypeVariables(), true, null, GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap)), name,
                                    array ? "[]" : "", executableElement.getSimpleName());
                            writer.println();
                        }
                        break;
                    }
                    case SET: {
                        String propertyName = cbm.inferName(executableElement);
                        TypeMirror tm = et.getParameterTypes().get(0);

                        boolean array = false;
                        if (TypeKind.ARRAY.equals(tm.getKind())) {
                            ArrayType ay = (ArrayType) tm;
                            tm = ay.getComponentType();
                            array = true;
                        }

                        String name = getName(processManager, reflection, includeFields, excludeFields, propertyName, tm, classMap);
                        if (name != null) {
                            for (AnnotationMirror am : executableElement.getAnnotationMirrors()) {
                                if (am.toString().equals("@com.talanlabs.component.IComponent.EqualsKey")) {
                                    writer.println("\t{0}", am.toString());
                                }
                            }

                            writer.println("\t{0} void {1}({2}{3} {4});",
                                    reflection.typeArgumentsToString(et.getTypeVariables(), true, null, GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap)),
                                    executableElement.getSimpleName(), name, array ? "[]" : "", executableElement.getParameters().get(0));
                            writer.println();
                        }
                        break;
                    }
                    }
                }
            }

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

    private static String getName(IProcessManager processManager, ReflectionHelper reflection, List<String> includes, List<String> excludes, String key, TypeMirror tm, Map<String, String> classMap) {
        String name = null;
        if (!includes.isEmpty()) {
            Iterator<String> it = includes.iterator();
            while (it.hasNext() && name == null) {
                String includeExtend = it.next();
                if (includeExtend != null) {
                    if (includeExtend.contains("->")) {
                        String t = includeExtend.substring(0, includeExtend.indexOf("->"));
                        String f = includeExtend.substring(includeExtend.indexOf("->") + 2);
                        if (t.equals(key)) {
                            name = GeneratedDtoHelper.getGeneratedDtoType(processManager, reflection, tm, classMap, false, f);
                        }
                    } else if (includeExtend.equals(key)) {
                        name = GeneratedDtoHelper.getGeneratedDtoType(processManager, reflection, tm, classMap, false, null);
                    }
                }
            }
        } else if (!excludes.isEmpty()) {
            if (!excludes.contains(key)) {
                name = GeneratedDtoHelper.getGeneratedDtoType(processManager, reflection, tm, classMap, false, null);
            }
        } else {
            name = GeneratedDtoHelper.getGeneratedDtoType(processManager, reflection, tm, classMap, false, null);
        }
        return name;
    }
}
