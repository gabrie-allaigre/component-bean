package com.talanlabs.component.annotation.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractGenProcessor extends AbstractProcessor implements IProcessManager {

    private static final Logger LOG = LogManager.getLogger(AbstractGenProcessor.class);

    private final boolean addGeneratedInfo;

    private ProcessingEnvironment environment;

    protected AbstractGenProcessor(boolean addGeneratedInfo) {
        super();

        this.addGeneratedInfo = addGeneratedInfo;
    }

    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        this.environment = environment;
    }

    public boolean isAddGeneratedInfo() {
        return this.addGeneratedInfo;
    }

    /**
     * Returns the current processing environment.
     *
     * @return the processing environment.
     */
    public ProcessingEnvironment getEnvironment() {
        return environment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            onProcessingStarted();

            Set<Element> elements = new HashSet<>();

            printMessage("Searching for " + getSupportedAnnotationTypes() + " annotations.");
            for (String supportedAnnotationName : getSupportedAnnotationTypes()) {
                try {
                    Class<?> supportedAnnotationClass = Class.forName(supportedAnnotationName);
                    if (supportedAnnotationClass.isAnnotation()) {
                        elements.addAll(roundEnvironment.getElementsAnnotatedWith((Class<? extends Annotation>) supportedAnnotationClass).stream().collect(Collectors.toSet()));
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("Not found class for annotation=" + supportedAnnotationName, e);

                    printError("Annotation not found: " + supportedAnnotationName);
                }
            }

            for (Element annotatedElement : elements) {
                try {
                    printMessage("Found " + annotatedElement.toString() + ".");
                    process(annotatedElement);
                } catch (Exception e) {
                    LOG.error("Not process for element=" + annotatedElement, e);

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    printError("Error for element: " + annotatedElement + " -> " + sw.toString());
                }
            }

            onProcessingCompleted();
        }
        return true;
    }

    /**
     * Override this function to receive elements which you've declared in supported annotations.
     *
     * @param annotatedElement the annotated element.
     */
    public abstract void process(Element annotatedElement);

    /**
     * Utility method called after processing has started.
     */
    public void onProcessingStarted() {
        printMessage(getClass().getName() + " started.");
    }

    /**
     * Utility method called after the processing is finished.
     */
    public void onProcessingCompleted() {
        printMessage(getClass().getName() + " finished.");
    }
}
