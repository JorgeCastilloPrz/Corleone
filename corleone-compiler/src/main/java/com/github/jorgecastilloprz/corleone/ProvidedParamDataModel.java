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

/**
 * Data model for runtime provided params. Provided params will be grouped by context to
 * allow qualifier repetition across different contexts.
 *
 * @author Jorge Castillo Pérez
 */
class ProvidedParamDataModel {

  private String qualifier;
  private Object value;

  ProvidedParamDataModel(String qualifier, Object value) {
    this.qualifier = qualifier;
    this.value = value;
  }

  public String getQualifier() {
    return qualifier;
  }

  public Object getValue() {
    return value;
  }
}
