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
package com.github.jorgecastilloprz.corleone.messager;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * @author Jorge Castillo Pérez
 */
public class ErrorMessagerImpl extends ErrorMessager {

  public ErrorMessagerImpl(Messager messager) {
    super(messager);
  }

  @Override public void multipleAnnotationError(String annotationName) {
    error("A single class is not allowed to have multiple " + annotationName + " methods");
  }

  @Override public void error(String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    messager.printMessage(ERROR, message);
  }

  @Override public void error(Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    messager.printMessage(ERROR, message, element);
  }
}
