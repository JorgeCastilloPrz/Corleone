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
package com.github.jorgecastilloprz.corleone.validator;

import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.messager.ErrorMessager;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

/**
 * @author Jorge Castillo Pérez
 */
public class MethodAnnotationValidator extends AnnotationValidator {

  private Set<? extends Element> executionElements;

  public MethodAnnotationValidator(RoundEnvironment roundEnvironment, ErrorMessager errorMessager) {
    super(roundEnvironment, errorMessager);
  }

  @Override public boolean validate() {
    executionElements = roundEnvironment.getElementsAnnotatedWith(Execution.class);
    return validateExecution();
  }

  private boolean validateExecution() {
    return validateElements(executionElements, Execution.class.getSimpleName());
  }

  private boolean validateElements(Set<? extends Element> elementsToValidate, String annotation) {
    for (Element currentElement : elementsToValidate) {
      if (currentElement.getKind() != ElementKind.METHOD) {
        errorMessager.error("@%s annotation must be used to qualify methods.", annotation);
        return false;
      }
    }
    return true;
  }
}
