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
package com.github.jorgecastilloprz.corleone.datamodel;

import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Data model for classes flagged with @Job annotation.
 *
 * @author Jorge Castillo Pérez
 */
public class JobAnnotatedClass {

  private TypeElement annotatedClassElement;
  private LinkedList<RuleEntity> rules;

  public JobAnnotatedClass(TypeElement classElement) throws IllegalArgumentException {
    this.annotatedClassElement = classElement;
    this.rules = new LinkedList<>();
    parseRules();
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
        rules.add(new RuleEntity(rule.context(), rule.previousJob().getCanonicalName()));
      } catch (MirroredTypeException exception) {
        DeclaredType classTypeMirror = (DeclaredType) exception.getTypeMirror();
        TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
        rules.add(new RuleEntity(rule.context(), classTypeElement.getQualifiedName().toString()));
      }
    }
  }

  public List<RuleEntity> getRules() {
    return rules;
  }

  public ExecutableElement getExecutionMethod() {
    return null;
  }
}
