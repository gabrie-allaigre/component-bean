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
import java.io.Writer;
import java.util.List;

/**
 * {@link ClassGenerationHelper} is an internal class that provides common
 * routines only used by the annotation processors.
 *
 * @author Brendan Doherty
 * @author Florian Sauter
 * @author Stephen Haberman (concept)
 */
public class ClassGenerationHelper extends GenerationHelper {

    public ClassGenerationHelper(Writer sourceWriter) {
        super(sourceWriter);
    }

    /**
     * Generates a class header. Pass null to skip the parent class.
     * <p>
     * <p>
     * <b>Usage:</b>
     * </p>
     * <p>
     * <pre>
     * <code>
     *  generateClassHeader(Foo.class.getSimpleName(),
     *        HasName.class.getSimpleName,
     *        "MyGenericInterface{@literal <Foo>}"
     *  )
     *  </code>
     * </pre>
     * <p>
     * <b>Generated example:</b>
     * </p>
     * <p>
     * <pre>
     * <code>public class MyFoo extends Foo implements HasName, MyGenericInterface{@literal <Foo>} {</code>
     * </pre>
     *
     * @param className         the comparator class name
     * @param modifiers         the modifiers for the class
     * @param extendedClassName the parent class name
     * @param extraInterfaces   array of interface names to be implemented
     */
    public void generateClassHeader(String className, String extendedClassName, List<Modifier> modifiers, String... extraInterfaces) {
        println();
        print("{0}class {1}", generateModifierList(modifiers.toArray(new Modifier[modifiers.size()])), className);
        if (extendedClassName != null && !extendedClassName.isEmpty()) {
            String trimedExtendedClassName = extendedClassName.trim();
            printWithoutSpaces(" extends {0}", trimedExtendedClassName);
        }
        if (extraInterfaces != null && extraInterfaces.length > 0) {
            printWithoutSpaces(" implements {0}", implode(extraInterfaces, ","));
        }
        printWithoutSpaces(" { ");
    }

    public void generateInterfaceHeader(String className, List<Modifier> modifiers, String... extraInterfaces) {
        println();
        print("{0}interface {1}", generateModifierList(modifiers.toArray(new Modifier[modifiers.size()])), className);
        if (extraInterfaces != null && extraInterfaces.length > 0) {
            printWithoutSpaces(" extends {0}", implode(extraInterfaces, ","));
        }
        printWithoutSpaces(" { ");
    }
}