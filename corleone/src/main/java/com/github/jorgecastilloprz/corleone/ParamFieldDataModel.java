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
 * Data model to represent @Param tagged fields in the domain layer. We map all the
 *
 * @author Jorge Castillo Pérez
 * @Param VariableElements to this model to avoid dealing with complex reflection logic
 * all the time. It is better to do so for better semantics.
 */
public class ParamFieldDataModel {
  private String type;
  private String qualifier;
  private String name;

  ParamFieldDataModel(String type, String qualifier, String name) {
    this.type = type;
    this.qualifier = qualifier;
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public String getQualifier() {
    return qualifier;
  }

  public String getName() {
    return name;
  }
}
