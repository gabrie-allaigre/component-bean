package com.talanlabs.component.annotation.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

public interface IProcessManager {

    boolean isAddGeneratedInfo();

    ProcessingEnvironment getEnvironment();

    /**
     * Prints a message.
     *
     * @param message the message
     */
    default void printMessage(String message) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    /**
     * Prints an error.
     *
     * @param message the error message
     */
    default void printError(String message) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }
}
