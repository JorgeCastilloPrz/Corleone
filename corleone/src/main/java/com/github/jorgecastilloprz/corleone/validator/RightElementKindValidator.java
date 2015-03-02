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

import com.github.jorgecastilloprz.corleone.messager.ErrorMessagerImpl;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

/**
 * Common logic for all the element kind checking validators
 *
 * @author Jorge Castillo Pérez
 */
public abstract class RightElementKindValidator extends Validator {

  public RightElementKindValidator(Set<? extends Element> elements) {
    super(elements);
  }

  @Override public boolean validate() {
    for (Element currentElement : elements) {
      if (currentElement.getKind() != getElementKind()) {
        ErrorMessagerImpl.getInstance()
            .error("Annotation must be used to qualify elements of kind: %s.", getElementKind());
        return false;
      }
    }
    return true;
  }

  protected abstract ElementKind getElementKind();
}
