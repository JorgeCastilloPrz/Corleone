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

import java.util.HashMap;

/**
 * ParamBinders need to get accessed and instantiated during runtime to provide param injection.
 * Since reflection is too slow, we are going to need a cache strategy to stay with the already
 * loaded instances and to not search again every time we need to use them.
 *
 * @author Jorge Castillo Pérez
 */
class ParamBinderCache {

  private HashMap<String, ParamBinder> paramBinderInstances;

  ParamBinderCache() {
    paramBinderInstances = new HashMap<>();
  }

  ParamBinder getBinderForClassNameAndContext(String classQualifiedName, String context) {
    String binderName = NameUtils.getBinderClassName(classQualifiedName, context);

    if (paramBinderInstances.containsKey(binderName)) {
      return paramBinderInstances.get(binderName);
    } else {
      Class<?> paramBinderClass = ClassUtils.findInClassPath(binderName);
      ParamBinder paramBinder = (ParamBinder) ClassUtils.buildClassInstance(paramBinderClass);
      paramBinderInstances.put(binderName, paramBinder);
      return paramBinder;
    }
  }
}
