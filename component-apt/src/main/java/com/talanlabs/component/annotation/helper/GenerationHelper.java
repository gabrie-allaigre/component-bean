/**
 * Copyright 2011 ArcBees Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.talanlabs.component.annotation.helper;

import javax.lang.model.element.Modifier;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * {@link GenerationHelper} is an internal class that provides common routines
 * only used by the annotation processors.
 *
 * @author Brendan Doherty
 * @author Florian Sauter
 * @author Stephen Haberman (concept)
 */
public class GenerationHelper implements Closeable {

    /**
     * Construct a single string from an array of strings, gluing them together
     * with the specified delimiter.
     *
     * @param segments  array of strings
     * @param delimiter character that glues the passed strings together
     * @return imploded and glued list of strings
     */
    public static String implode(Object[] segments, String delimiter) {
        String implodedString;
        if (segments.length == 0) {
            implodedString = "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(segments[0]);
            for (int i = 1; i < segments.length; i++) {
                if (segments[i] != null && !segments[i].toString().isEmpty()) {
                    sb.append(delimiter);
                    sb.append(segments[i]);
                }
            }
            implodedString = sb.toString();
        }
        return implodedString;
    }

    /**
     * Replaces each placeholder of this string that matches a parameter index.
     * <p>
     * <b>Placeholder format:</b> {int}
     * </p>
     * <p>
     * <p>
     * <b>Usage:</b>
     * </p>
     * <p>
     * <pre>
     * <code>replaceParameters("{0} int myField = {1};", "private", 20);</code>
     * </pre>
     *
     * @param target     the string to be replace.
     * @param parameters the replacement parameters
     * @return the resulting string.
     * <p>
     * For example:
     * </p>
     * <code>private int myField = 20;</code>
     */
    public static String replaceParameters(String target, Object... parameters) {
        String result = target;
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                result = result.replace("{" + i + "}", String.valueOf(parameters[i]));
            }
        }
        return result;
    }

    /**
     * Rename component
     *
     * @param name
     * @return
     */
    public static String rename(String name) {
        String res = name;
        if (name.length() > 2) {
            String second = name.substring(1, 2);
            boolean startI = name.substring(0, 1).equals("I") && second.toUpperCase().equals(second);
            if (startI) {
                res = name.substring(1);
            }
        }
        return res;
    }

    private int whitespaces = 0;

    private PrintWriter writer;

    public GenerationHelper(Writer sourceWriter) {
        initializeSourceWriter(sourceWriter);
    }

    @Override
    public void close() {
        writer.close();
    }

    public void generateFooter() {
        println("}");
    }

    public String generateModifierList(Modifier... modifiers) {
        String fieldModifier = "";
        if (modifiers != null && modifiers.length > 0) {
            fieldModifier = implode(modifiers, " ");
        }
        return fieldModifier.isEmpty() ? fieldModifier : fieldModifier + " ";
    }

    /**
     * Generates a package declaration.
     * <p>
     * <p>
     * <b>Generated example:</b>
     * </p>
     * <code>package com.gwtplatform.dispatch.annotation.processor;</code>
     */
    public void generatePackageDeclaration(String packageName) {
        println("package {0};", packageName);
    }

    public void print(Object o) {
        writer.print(manufactureIndentation() + o);
    }

    public void print(String s, Object... parameters) {
        print(replaceParameters(s, parameters));
    }

    public void println() {
        writer.println();
    }

    public void println(Object o) {
        writer.println(manufactureIndentation() + o);
    }

    public void println(String s, Object... parameters) {
        println(replaceParameters(s, parameters));
    }

    public void printWithoutSpaces(String s, Object... parameters) {
        writer.print(replaceParameters(s, parameters));
    }

    protected String manufactureIndentation() {
        String space = "";
        for (int i = 0; i < whitespaces; i++) {
            space += " ";
        }
        return space;
    }

    private void initializeSourceWriter(Writer sourceWriter) {
        BufferedWriter bufferedWriter = new BufferedWriter(sourceWriter);
        writer = new PrintWriter(bufferedWriter);
    }

}