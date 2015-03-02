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

import com.github.jorgecastilloprz.corleone.internal.EmptyDefaultClass;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author Jorge Castillo Pérez
 */
class JobEntity {

  private TypeElement annotatedClassElement;
  private ExecutableElement executionMethod;
  private String previousJobQualifiedName;

  private List<VariableElement> paramElements;

  private final String NO_PREVIOUS_JOB = EmptyDefaultClass.class.getCanonicalName();

  JobEntity(JobAnnotatedClass annotatedClassToMapFrom, String context) {
    annotatedClassElement = annotatedClassToMapFrom.getAnnotatedClassElement();
    executionMethod = annotatedClassToMapFrom.getExecutionMethod();
    paramElements = annotatedClassToMapFrom.getParamElements();
    previousJobQualifiedName = annotatedClassToMapFrom.getPreviousJobForContext(context);
  }

  public String getPreviousJobQualifiedName() {
    return (previousJobQualifiedName.equals(NO_PREVIOUS_JOB)) ? "" : previousJobQualifiedName;
  }

  public TypeElement getAnnotatedClassElement() {
    return annotatedClassElement;
  }

  public ExecutableElement getExecutionMethod() {
    return executionMethod;
  }

  public List<VariableElement> getParamElements() {
    return paramElements;
  }
}
