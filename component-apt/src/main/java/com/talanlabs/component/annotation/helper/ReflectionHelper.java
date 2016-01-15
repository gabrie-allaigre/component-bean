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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor6;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link ReflectionHelper} is an internal class that provides common routines only used by the annotation processors.
 *
 * @author Brendan Doherty
 * @author Florian Sauter
 * @author Stephen Haberman (concept)
 */
@SuppressWarnings("unchecked")
public class ReflectionHelper {

    private static final DefaultToDeclaredTypeString DEFAULT_TO_DECLARED_TYPE_STRING = new DefaultToDeclaredTypeString();

    private TypeElement classRepresenter;
    private ProcessingEnvironment environment;

    public ReflectionHelper(ProcessingEnvironment environment, TypeElement classRepresenter) {
        this.classRepresenter = classRepresenter;
        this.environment = environment;
    }

    /**
     * Returns only fields which are not annotated with one of the passed annotation.
     */
    public Collection<VariableElement> filterFields(Collection<VariableElement> fieldElements, Class<? extends Annotation>... annotations) {
        Collection<VariableElement> filteredFields = new ArrayList<>();
        filteredFields.addAll(fieldElements);
        for (VariableElement fieldElement : fieldElements) {
            for (Class<? extends Annotation> passedAnnotation : annotations) {
                Annotation fieldAnnotation = fieldElement.getAnnotation(passedAnnotation);
                if (fieldAnnotation != null) {
                    filteredFields.remove(fieldElement);
                    break;
                }
            }
        }
        return filteredFields;
    }

    /**
     * Returns only fields which do not contain one of the passed modifiers.
     */
    public Collection<VariableElement> filterFields(Collection<VariableElement> fieldElements, Modifier... modifiers) {
        Collection<VariableElement> filteredFields = new ArrayList<>();
        filteredFields.addAll(fieldElements);
        for (VariableElement fieldElement : fieldElements) {
            for (Modifier modifier : modifiers) {
                if (fieldElement.getModifiers().contains(modifier)) {
                    filteredFields.remove(fieldElement);
                    break;
                }
            }
        }
        return filteredFields;
    }

    public TypeElement getClassRepresenter() {
        return classRepresenter;
    }

    /**
     * Returns all fields.
     * <p>
     * <b>Important:</b> Fields are not sorted according
     * </p>
     */
    public Collection<VariableElement> getFields() {
        List<? extends Element> members = getElementUtils().getAllMembers(classRepresenter);
        return ElementFilter.fieldsIn(members);
    }

    public String getPackageName() {
        String res = null;
        PackageElement packageElement = getElementUtils().getPackageOf(classRepresenter);
        if (packageElement != null && packageElement.getQualifiedName() != null) {
            res = packageElement.getQualifiedName().toString();
        }
        return res;
    }

    public String getSimpleClassName() {
        return classRepresenter.getSimpleName().toString();
    }

    /**
     * Utility method.
     */
    protected Elements getElementUtils() {
        return environment.getElementUtils();
    }

    public List<ExecutableElement> getMethods() {
        List<? extends Element> members = getElementUtils().getAllMembers(classRepresenter);
        return ElementFilter.methodsIn(members);
    }

    public static PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }

    /**
     * Returns a string for {@code type}. Primitive types are always boxed.
     */
    public String typeToString(TypeMirror type, boolean firstTypeArgument, String typeSuffix, IToDeclaredTypeString toDeclaredTypeString) {
        StringBuilder result = new StringBuilder();
        typeToString(type, result, '.', firstTypeArgument, typeSuffix, toDeclaredTypeString);
        return result.toString();
    }

    /**
     * Returns a string for the raw type of {@code type}. Primitive types are always boxed.
     */
    public String rawTypeToString(TypeMirror type, char innerClassSeparator) {
        if (!(type instanceof DeclaredType)) {
            throw new IllegalArgumentException("Unexpected type: " + type);
        }
        DeclaredType declaredType = (DeclaredType) type;
        return DEFAULT_TO_DECLARED_TYPE_STRING.toDeclaredTypeString(declaredType, innerClassSeparator);
    }

    public String typeArgumentsToString(List<? extends TypeMirror> typeArguments, boolean firstTypeArgument, String typeSuffix, IToDeclaredTypeString toDeclaredTypeString) {
        StringBuilder result = new StringBuilder();
        if (typeArguments != null && !typeArguments.isEmpty()) {
            result.append("<");
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i != 0) {
                    result.append(", ");
                }
                typeToString(typeArguments.get(i), result, '.', firstTypeArgument, typeSuffix, toDeclaredTypeString);
            }
            result.append(">");
        }
        return result.toString();
    }

    public void typeToString(final TypeMirror type, final StringBuilder result, final char innerClassSeparator, final boolean firstTypeArgument, String typeSuffix,
            final IToDeclaredTypeString toDeclaredTypeString) {
        type.accept(new SimpleTypeVisitor6<Void, Void>() {
            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v) {
                if (toDeclaredTypeString != null) {
                    result.append(toDeclaredTypeString.toDeclaredTypeString(declaredType, innerClassSeparator));
                } else {
                    result.append(DEFAULT_TO_DECLARED_TYPE_STRING.toDeclaredTypeString(declaredType, innerClassSeparator));
                }
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    result.append("<");
                    for (int i = 0; i < typeArguments.size(); i++) {
                        if (i != 0) {
                            result.append(", ");
                        }
                        typeToString(typeArguments.get(i), result, innerClassSeparator, firstTypeArgument, typeSuffix, toDeclaredTypeString);
                    }
                    result.append(">");
                }
                return null;
            }

            @Override
            public Void visitPrimitive(PrimitiveType primitiveType, Void v) {
                result.append(type);
                return null;
            }

            @Override
            public Void visitArray(ArrayType arrayType, Void v) {
                TypeMirror type = arrayType.getComponentType();
                if (type instanceof PrimitiveType) {
                    result.append(type.toString()); // Don't box, since this is an array.
                } else {
                    typeToString(arrayType.getComponentType(), result, innerClassSeparator, firstTypeArgument, typeSuffix, toDeclaredTypeString);
                }
                result.append("[]");
                return null;
            }

            @Override
            public Void visitTypeVariable(TypeVariable typeVariable, Void v) {
                result.append(typeVariable.asElement().getSimpleName()).append(typeSuffix != null ? typeSuffix : "");
                if (firstTypeArgument) {
                    if (!TypeKind.NULL.equals(typeVariable.getLowerBound().getKind())) {
                        result.append(" super ");
                        typeToString(typeVariable.getLowerBound(), result, innerClassSeparator, false, typeSuffix, toDeclaredTypeString);
                    } else if (!TypeKind.NULL.equals(typeVariable.getUpperBound().getKind())) {
                        result.append(" extends ");
                        typeToString(typeVariable.getUpperBound(), result, innerClassSeparator, false, typeSuffix, toDeclaredTypeString);
                    }
                }
                return null;
            }

            @Override
            public Void visitIntersection(IntersectionType t, Void aVoid) {
                boolean first = true;
                for (TypeMirror tm : t.getBounds()) {
                    if (!first) {
                        result.append(" & ");
                    } else {
                        first = false;
                    }

                    typeToString(tm, result, innerClassSeparator, false, typeSuffix, toDeclaredTypeString);
                }
                return null;
            }

            @Override
            public Void visitWildcard(WildcardType t, Void aVoid) {
                TypeMirror extendsBound = t.getExtendsBound();
                if (extendsBound != null && !TypeKind.NULL.equals(extendsBound.getKind())) {
                    result.append("? extends ");
                    typeToString(extendsBound, result, innerClassSeparator, false, typeSuffix, toDeclaredTypeString);
                }
                TypeMirror superBound = t.getSuperBound();
                if (superBound != null && !TypeKind.NULL.equals(superBound.getKind())) {
                    result.append("? super ");
                    typeToString(superBound, result, innerClassSeparator, false, typeSuffix, toDeclaredTypeString);
                }
                return null;
            }

            @Override
            public Void visitError(ErrorType errorType, Void v) {
                // Error type found, a type may not yet have been generated, but we need the type
                // so we can generate the correct code in anticipation of the type being available
                // to the compiler.

                // Paramterized types which don't exist are returned as an error type whose name is "<any>"
                if ("<any>".equals(errorType.toString())) {
                    throw new RuntimeException("Type reported as <any> is likely a not-yet generated parameterized type.");
                }
                // TODO(cgruber): Figure out a strategy for non-FQCN cases.
                result.append(errorType.toString());
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror typeMirror, Void v) {
                throw new UnsupportedOperationException("Unexpected TypeKind " + typeMirror.getKind() + " for " + typeMirror);
            }
        }, null);
    }

    public interface IToDeclaredTypeString {

        String toDeclaredTypeString(DeclaredType declaredType, char innerClassSeparator);

    }

    public static class DefaultToDeclaredTypeString implements IToDeclaredTypeString {

        @Override
        public String toDeclaredTypeString(DeclaredType declaredType, char innerClassSeparator) {
            TypeElement type = (TypeElement) declaredType.asElement();
            String packageName = ReflectionHelper.getPackage(type).getQualifiedName().toString();
            String qualifiedName = type.getQualifiedName().toString();
            if (packageName.isEmpty()) {
                return qualifiedName.replace('.', innerClassSeparator);
            } else {
                return packageName + '.' + qualifiedName.substring(packageName.length() + 1).replace('.', innerClassSeparator);
            }
        }
    }
}