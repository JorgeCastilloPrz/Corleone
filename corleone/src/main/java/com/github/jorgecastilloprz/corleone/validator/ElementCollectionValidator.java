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

import java.util.Set;
import javax.lang.model.element.Element;

/**
 * This class defines a simple annotation Validator for a collection of annotated elements
 * and hides minimal construction logic to it's descendants.
 *
 * @author Jorge Castillo Pérez
 */
public abstract class ElementCollectionValidator implements Validator {

  protected Set<? extends Element> elements;

  public ElementCollectionValidator(Set<? extends Element> elements) {
    this.elements = elements;
  }

  public abstract boolean validate();
}
