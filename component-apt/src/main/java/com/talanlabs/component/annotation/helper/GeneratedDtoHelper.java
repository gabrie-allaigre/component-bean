package com.talanlabs.component.annotation.helper;

import com.talanlabs.component.annotation.GenerateDto;
import com.talanlabs.component.annotation.GenerateDtos;
import com.talanlabs.component.annotation.processor.IProcessManager;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneratedDtoHelper {

    public static String getGeneratedDtoName(IProcessManager processManager, TypeMirror tm, String useName) {
        Element e = processManager.getEnvironment().getTypeUtils().asElement(tm);
        if (e instanceof TypeElement) {
            TypeElement te = (TypeElement) e;

            GenerateDto agd = te.getAnnotation(GenerateDto.class);
            GenerateDtos agds = te.getAnnotation(GenerateDtos.class);
            if (agd != null || (agds != null && agds.value() != null && agds.value().length > 0)) {
                List<String> names = new ArrayList<>();
                if (agd != null) {
                    names.add(createNameDto(te, agd.name()));
                }
                if (agds != null && agds.value() != null) {
                    names.addAll(Arrays.asList(agds.value()).stream().map(g -> createNameDto(te, g.name())).collect(Collectors.toList()));
                }
                if (names.size() >= 1) {
                    for (String name : names) {
                        if (name.equals(useName)) {
                            return useName;
                        }
                    }
                    return names.get(0);
                }
            }
        }
        return null;
    }

    private static String createNameDto(TypeElement te, String name) {
        if (StringUtils.isBlank(name)) {
            name = te.getSimpleName() + "Dto";
        }
        return name;
    }

    public static String getGeneratedDtoType(IProcessManager processManager, ReflectionHelper reflection, TypeMirror tm, Map<String, String> classMap, boolean allTypeArgument, String useName) {
        String typeMirrorName = reflection.typeToString(tm, allTypeArgument, null, null);

        if (useName == null) {
            useName = classMap.get(typeMirrorName);
        }

        String name = getGeneratedDtoName(processManager, tm, useName);
        if (name != null) {
            TypeElement te = (TypeElement) processManager.getEnvironment().getTypeUtils().asElement(tm);
            String generic = reflection.typeArgumentsToString(((DeclaredType) tm).getTypeArguments(), false, null, GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap));
            String s = te.toString();
            if (s.lastIndexOf(".") > 0) {
                s = s.substring(0, s.lastIndexOf(".") + 1);
            } else {
                s = "";
            }
            return s + name + generic;
        } else {
            Element e = processManager.getEnvironment().getTypeUtils().asElement(tm);
            if (e instanceof TypeElement) {
                TypeElement te = (TypeElement) e;
                String generic = reflection
                        .typeArgumentsToString(((DeclaredType) tm).getTypeArguments(), false, null, GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap));
                return te.toString() + generic;
            }
        }
        return typeMirrorName;
    }

    public static ReflectionHelper.IToDeclaredTypeString newGeneratedDtoToDeclaredTypeString(IProcessManager processManager, Map<String, String> classMap) {
        return new GeneratedDtoToDeclaredTypeString(processManager, classMap);
    }

    private static class GeneratedDtoToDeclaredTypeString extends ReflectionHelper.DefaultToDeclaredTypeString {

        final IProcessManager processManager;

        final Map<String, String> classMap;

        GeneratedDtoToDeclaredTypeString(IProcessManager processManager, Map<String, String> classMap) {
            super();

            this.processManager = processManager;
            this.classMap = classMap;
        }

        @Override
        public String toDeclaredTypeString(DeclaredType declaredType, char innerClassSeparator) {
            String current = super.toDeclaredTypeString(declaredType, innerClassSeparator);

            String useName = null;
            if (classMap.containsKey(current)) {
                useName = classMap.get(current);
            }

            String name = GeneratedDtoHelper.getGeneratedDtoName(processManager, declaredType, useName);
            if (name != null) {
                TypeElement type = (TypeElement) declaredType.asElement();
                String packageName = ReflectionHelper.getPackage(type).getQualifiedName().toString();
                String qualifiedName = type.getQualifiedName().toString();
                qualifiedName = qualifiedName.substring(0, qualifiedName.lastIndexOf(".") + 1) + name;

                if (packageName.isEmpty()) {
                    name = qualifiedName.replace('.', innerClassSeparator);
                } else {
                    name = packageName + '.' + qualifiedName.substring(packageName.length() + 1).replace('.', innerClassSeparator);
                }
            } else {
                name = current;
            }
            return name;
        }
    }
}
