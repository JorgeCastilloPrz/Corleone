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
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * AnnotationValidator implementation based on a single instance of a given
 * annotation per class criteria.
 *
 * @author Jorge Castillo Pérez
 */
public class SingleAnnotationInstanceValidator extends AnnotationValidator {

  public SingleAnnotationInstanceValidator(RoundEnvironment roundEnvironment,
      ErrorMessager errorMessager, Class annotation) {
    super(roundEnvironment, errorMessager, annotation);
  }

  @Override public boolean validate() {
    return !checkMultipleAnnotationInstances();
  }

  private boolean checkMultipleAnnotationInstances() {
    for (Element rootElement : roundEnvironment.getRootElements()) {
      TypeElement rootTypeElement = findEnclosingTypeElement(rootElement);
      if (checkMultipleInstancesForThisRoot(rootTypeElement)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkMultipleInstancesForThisRoot(TypeElement rootTypeElement) {

    boolean annotationFoundOnThisClass = false;

    for (ExecutableElement method : ElementFilter.methodsIn(
        rootTypeElement.getEnclosedElements())) {

      if (method.getAnnotation(annotation) != null) {
        if (annotationFoundOnThisClass) {
          errorMessager.multipleAnnotationError(annotation.getName());
          return true;
        } else {
          annotationFoundOnThisClass = true;
        }
      }
    }
    return false;
  }

  public static TypeElement findEnclosingTypeElement(Element e) {
    while (e != null && !(e instanceof TypeElement)) {
      e = e.getEnclosingElement();
    }
    return TypeElement.class.cast(e);
  }
}
