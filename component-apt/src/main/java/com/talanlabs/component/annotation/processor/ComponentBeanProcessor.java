package com.talanlabs.component.annotation.processor;

import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;
import com.talanlabs.component.annotation.GenerateDtos;
import com.talanlabs.component.annotation.factory.ComponentBeanBuilderFactory;
import com.talanlabs.component.annotation.factory.ComponentBeanFieldsFactory;
import com.talanlabs.component.annotation.factory.GenerateDtoFactory;
import com.talanlabs.component.annotation.factory.GenerateDtoMapperFactory;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({ "com.talanlabs.component.annotation.ComponentBean", "com.talanlabs.component.annotation.GenerateDto", "com.talanlabs.component.annotation.GenerateDtos" })
public class ComponentBeanProcessor extends AbstractGenProcessor {

    public ComponentBeanProcessor() {
        this(true);
    }

    public ComponentBeanProcessor(boolean addGeneratedInfo) {
        super(addGeneratedInfo);
    }

    @Override
    public void process(Element element) {
        if (element instanceof TypeElement) {
            TypeElement te = (TypeElement) element;

            ComponentBean sc = te.getAnnotation(ComponentBean.class);
            if (sc != null) {
                if (sc.createFields()) {
                    ComponentBeanFieldsFactory.processFields(this, te);
                }
                if (sc.createBuilder()) {
                    ComponentBeanBuilderFactory.processBuilder(this, te);
                }
            }

            List<GenerateDto> gds = new ArrayList<>();

            GenerateDtos generateDtos = te.getAnnotation(GenerateDtos.class);
            if (generateDtos != null && generateDtos.value() != null && generateDtos.value().length > 0) {
                gds.addAll(Arrays.asList(generateDtos.value()));
            }

            GenerateDto generateDto = te.getAnnotation(GenerateDto.class);
            if (generateDto != null) {
                gds.add(generateDto);
            }

            if (!gds.isEmpty()) {
                for (GenerateDto gd : gds) {
                    GenerateDtoFactory.processDto(this, gd, te);
                }

                GenerateDtoMapperFactory.processMapper(this, gds, te);
            }
        }
    }
}
