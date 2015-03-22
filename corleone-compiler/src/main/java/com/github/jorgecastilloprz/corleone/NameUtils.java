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
 * @author Jorge Castillo Pérez
 */
class NameUtils {

  static final String SEPARATOR = "$$";
  static final String PARAM_BINDER_SUFFIX = "ParamBinder";
  static final String RUNTIME_QUEUE_SUFFIX = "RuntimeQueue";
  static final String RUNTIME_QUEUES_PACKAGE = "com.github.jorgecastilloprz.corleone";

  static String getBinderClassName(String jobDataModelClassName, String context) {
    return jobDataModelClassName
        + NameUtils.SEPARATOR
        + context
        + NameUtils.SEPARATOR
        + NameUtils.PARAM_BINDER_SUFFIX;
  }

  static String getRuntimeQueueQualifiedName(String context) {
    return RUNTIME_QUEUES_PACKAGE + "." + context + SEPARATOR + RUNTIME_QUEUE_SUFFIX;
  }
}
