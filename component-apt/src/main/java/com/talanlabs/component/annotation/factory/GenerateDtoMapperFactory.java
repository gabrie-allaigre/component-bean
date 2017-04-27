package com.talanlabs.component.annotation.factory;

import com.talanlabs.component.annotation.GenerateDto;
import com.talanlabs.component.annotation.helper.ClassGenerationHelper;
import com.talanlabs.component.annotation.helper.GeneratedDtoHelper;
import com.talanlabs.component.annotation.helper.GenerationHelper;
import com.talanlabs.component.annotation.helper.ReflectionHelper;
import com.talanlabs.component.annotation.processor.IProcessManager;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class GenerateDtoMapperFactory {

    private GenerateDtoMapperFactory() {
        super();
    }

    public static void processMapper(IProcessManager processManager, List<GenerateDto> generateDtos, TypeElement typeElement) {
        ClassGenerationHelper writer = null;
        try {
            ReflectionHelper reflection = new ReflectionHelper(processManager.getEnvironment(), typeElement);
            String dtoElementSimpleName = reflection.getSimpleClassName();
            String dtoSimpleName = GenerationHelper.rename(dtoElementSimpleName) + "Mapper";
            String packageName = reflection.getPackageName();
            String dtoClassName = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName : dtoSimpleName;

            processManager.printMessage("Generating mapper '" + dtoClassName + "' from '" + dtoElementSimpleName + "'.");

            Writer sourceWriter = processManager.getEnvironment().getFiler().createSourceFile(dtoClassName, typeElement).openWriter();
            writer = new ClassGenerationHelper(sourceWriter);

            if (processManager.isAddGeneratedInfo()) {
                writer.println("// {0} - Generating mapper from {1}", SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.DEFAULT).format(new Date()), typeElement.asType().toString());
            }

            if (packageName != null && !packageName.isEmpty()) {
                writer.generatePackageDeclaration(packageName);
                writer.println();
            }

            writer.println("@com.talanlabs.component.annotation.GeneratedFrom({0}.class)", reflection.rawTypeToString(typeElement.asType(), '.'));
            writer.println("@javax.annotation.Generated(\"com.talanlabs.component.annotation.processor.ComponentBeanProcessor\")");
            writer.generateClassHeader(dtoSimpleName, null, Arrays.asList(Modifier.PUBLIC, Modifier.FINAL));
            writer.println();
            writer.println();

            writer.println("\tprivate {0}() { super(); }", dtoSimpleName);
            writer.println();

            for (GenerateDto generateDto : generateDtos) {
                processSubMapper(processManager, reflection, writer, generateDto, typeElement);
            }

            writer.generateFooter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static void processSubMapper(IProcessManager processManager, ReflectionHelper reflection, ClassGenerationHelper writer, GenerateDto generateDto, TypeElement typeElement) {
        String dtoSimpleName1 = reflection.getSimpleClassName();
        String dtoSimpleName2;
        if (!"".equals(generateDto.name())) {
            dtoSimpleName2 = generateDto.name();
        } else {
            dtoSimpleName2 = dtoSimpleName1 + "Dto";
        }
        String packageName = reflection.getPackageName();
        String dtoClassName1 = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName1 : dtoSimpleName1;
        String dtoClassName2 = packageName != null && !packageName.isEmpty() ? packageName + '.' + dtoSimpleName2 : dtoSimpleName2;

        String name1 = GenerationHelper.rename(dtoSimpleName1);
        String name2 = GenerationHelper.rename(dtoSimpleName2);

        Map<String, String> classMap = new HashMap<>();
        for (String classToClass : generateDto.classToClasss()) {
            String t = classToClass.substring(0, classToClass.indexOf("->"));
            String f = classToClass.substring(classToClass.indexOf("->") + 2);
            classMap.put(t, f);
        }

        String generic = "";
        String generic1 = "";
        String generic2 = "";
        List<? extends TypeMirror> typeArguments = ((DeclaredType) typeElement.asType()).getTypeArguments();
        if (typeArguments != null && !typeArguments.isEmpty()) {
            generic += "<";
            generic1 += "<";
            generic2 += "<";

            for (int i = 0; i < typeArguments.size(); i++) {
                if (i != 0) {
                    generic += ", ";
                    generic1 += ", ";
                    generic2 += ", ";
                }
                TypeVariable tv = (TypeVariable) typeArguments.get(i);
                String n = tv.asElement().getSimpleName().toString();
                String n1 = n + "_1";
                String n2 = n + "_2";

                generic += n1;
                if (!TypeKind.NULL.equals(tv.getLowerBound().getKind())) {
                    generic += " super " + reflection.typeToString(tv.getLowerBound(), false, "_1", null);
                } else if (!TypeKind.NULL.equals(tv.getUpperBound().getKind())) {
                    generic += " extends " + reflection.typeToString(tv.getUpperBound(), false, "_1", null);
                }
                generic += ", ";
                generic += n2;
                if (!TypeKind.NULL.equals(tv.getLowerBound().getKind())) {
                    generic += " super " + reflection.typeToString(tv.getLowerBound(), false, "_2", GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap));
                } else if (!TypeKind.NULL.equals(tv.getUpperBound().getKind())) {
                    generic += " extends " + reflection.typeToString(tv.getUpperBound(), false, "_2", GeneratedDtoHelper.newGeneratedDtoToDeclaredTypeString(processManager, classMap));
                }

                generic1 += n1;
                generic2 += n2;
            }

            generic += ">";
            generic1 += ">";
            generic2 += ">";
        }

        String param1 = name1.substring(0, 1).toLowerCase() + name1.substring(1);
        String param2 = name2.substring(0, 1).toLowerCase() + name2.substring(1);

        if (StringUtils.isNotEmpty(generic)) {
            writer.println("\tpublic static {0} {1}{2} to{3}({4}{5} {6}, java.lang.reflect.Type type) {", generic, dtoClassName1, generic1, name1, dtoClassName2, generic2, param2);
            writer.println("\t\treturn ({0}{1})com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent({2}, type);", dtoClassName1, generic1, param2);
            writer.println("\t}");
            writer.println();

            writer.println("\tpublic static {0} {1}{2} to{3}({4}{5} {6}, java.lang.reflect.Type type) {", generic, dtoClassName2, generic2, name2, dtoClassName1, generic1, param1);
            writer.println("\t\treturn ({0}{1})com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent({2}, type);", dtoClassName2, generic2, param1);
            writer.println("\t}");
        } else {
            writer.println("\tpublic static {0} to{1}({2} {3}) {", dtoClassName1, name1, dtoClassName2, param2);
            writer.println("\t\treturn com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent({0}, {1}.class);", param2, dtoClassName1);
            writer.println("\t}");
            writer.println();

            writer.println("\tpublic static {0} to{1}({2} {3}) {", dtoClassName2, name2, dtoClassName1, param1);
            writer.println("\t\treturn com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent({0}, {1}.class);", param1, dtoClassName2);
            writer.println("\t}");
        }
    }

    private static void toBeanDtoMethod() {

    }
}
