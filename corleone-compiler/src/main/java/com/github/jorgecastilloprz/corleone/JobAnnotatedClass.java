/*
 * Copyright (C) 2015 Jorge Castillo Pérez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jorgecastilloprz.corleone;

import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Param;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

/**
 * Data model for classes flagged with @Job annotation. This entity will be used to wrap all the
 * parsing logic and will not be used along the whole AP. It will be mapped to a JobEntity
 * later on.
 *
 * @author Jorge Castillo Pérez
 */
class JobAnnotatedClass {

  private TypeElement annotatedClassElement;
  private List<VariableElement> paramElements;
  private ExecutableElement executionMethod;
  private LinkedList<RuleDataModel> rules;
  private Elements elementUtils;

  public JobAnnotatedClass(TypeElement classElement, Elements elementUtils) {
    this.annotatedClassElement = classElement;
    this.elementUtils = elementUtils;
    this.rules = new LinkedList<>();
    parseRules();
    parseParamFields(elementUtils);
    parseExecutionMethod(elementUtils);
  }

  /**
   * While parsing rules we are going to access previousJob classes to get their canonical names.
   * Since annotation processing runs before compiling java sources, needed classes could
   * not be compiled yet. We need to catch MirroredTypeException to reach the TypeMirror of the
   * class and be able to get the name. The "try" case would happen if there is a compiled .class
   * with a @Job annotation into a third party jar.
   */
  private void parseRules() {
    Job jobAnnotation = annotatedClassElement.getAnnotation(Job.class);
    Rule[] ruleAnnotations = jobAnnotation.value();

    for (Rule rule : ruleAnnotations) {
      try {
        rules.add(new RuleDataModel(rule.context(), rule.previousJob().getCanonicalName()));
      } catch (MirroredTypeException exception) {
        DeclaredType classTypeMirror = (DeclaredType) exception.getTypeMirror();
        TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
        rules.add(
            new RuleDataModel(rule.context(), classTypeElement.getQualifiedName().toString()));
      }
    }
  }

  private void parseParamFields(Elements elementUtils) {
    paramElements = new ArrayList<>();
    List<? extends Element> elementMembers = elementUtils.getAllMembers(annotatedClassElement);
    List<VariableElement> fieldElements = ElementFilter.fieldsIn(elementMembers);
    for (VariableElement fieldElement : fieldElements) {
      if (fieldElement.getAnnotation(Param.class) != null) {
        paramElements.add(fieldElement);
        return;
      }
    }
  }

  private void parseExecutionMethod(Elements elementUtils) {
    List<? extends Element> elementMembers = elementUtils.getAllMembers(annotatedClassElement);
    List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
    for (ExecutableElement methodElement : elementMethods) {
      if (methodElement.getAnnotation(Execution.class) != null) {
        executionMethod = methodElement;
        return;
      }
    }
  }

  public List<VariableElement> getParamElements() {
    return paramElements;
  }

  public String getExecutionMethodName() {
    return executionMethod.getSimpleName().toString();
  }

  public List<RuleDataModel> getRules() {
    return rules;
  }

  public String getPreviousJobForContext(String context) {
    for (RuleDataModel currentRule : rules) {
      if (currentRule.getContext().equals(context)) {
        return currentRule.getPreviousJobQualifiedName();
      }
    }
    return "";
  }

  public String getPackageName() {
    return elementUtils.getPackageOf(annotatedClassElement).getQualifiedName().toString();
  }

  public String getClassName() {
    return annotatedClassElement.getSimpleName().toString();
  }

  public String getQualifiedName() {
    return annotatedClassElement.getQualifiedName().toString();
  }

  public TypeMirror getClassType() {
    return annotatedClassElement.asType();
  }
}
