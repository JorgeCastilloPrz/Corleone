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
package com.github.jorgecastilloprz.corleone.annotations;

import com.github.jorgecastilloprz.corleone.EmptyDefaultClass;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * This annotation must be used inside of a Job annotation. It defines a Job rule to apply on
 * compilation time. A single @Job annotation can contain multiple @Rule annotations.
 *
 * @author Jorge Castillo Pérez
 */
@Retention(CLASS) @Target(TYPE)
public @interface Rule {

  /**
   * Jobs will be grouped by this field
   */
  String context();

  /**
   * ClassName of the previous job in job chain. Its an optional param
   */
  Class previousJob() default EmptyDefaultClass.class;
}
