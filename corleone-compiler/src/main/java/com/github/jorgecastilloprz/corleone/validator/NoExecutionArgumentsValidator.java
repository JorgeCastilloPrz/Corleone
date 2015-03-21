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
import com.github.jorgecastilloprz.corleone.messager.ErrorMessagerImpl;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

/**
 * Validates {@link Execution} methods to forbid more than 0 arguments.
 *
 * @author Jorge Castillo Pérez
 */
public class NoExecutionArgumentsValidator extends ElementCollectionValidator {

  public NoExecutionArgumentsValidator(Set<? extends Element> executionMethodElems) {
    super(executionMethodElems);
  }

  @Override public boolean validate() {
    for (Element element : elements) {
      if (((ExecutableElement) element).getParameters().size() > 0) {
        ErrorMessagerImpl.getInstance()
            .error(
                "@Execution methods are not allowed to have any arguments. If you need arguments "
                    + "to get passed into the Job instance, use the @Param annotation and "
                    + "provideParams() Corleone method.");
        return false;
      }
    }
    return true;
  }
}
