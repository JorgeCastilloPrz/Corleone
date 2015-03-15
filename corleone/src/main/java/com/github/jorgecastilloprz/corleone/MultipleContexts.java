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
 * To keep method calls simple, we want to keep the syntax this way:
 *
 * Corleone.context("MyContext").dispatchJobs();
 * Corleone.context("MyContext").provideParam("MyParamQualifier", paramValue);
 * Corleone.allContexts(jobInstance).provideParam("MyParamQualifier", paramValue);
 *
 * But there is a problem with that. Developers are not going to get any instances of their jobs,
 * as the instantiation logic is automatized into the library. So the only way to call
 * Corleone.allContexts(jobInstance).whateverMethod() would be from inside of a @Job. That makes
 * sense if we are going to call provideParam() method, like:
 * Corleone.allContexts(this).provideParam("ParamQualifier", paramValue);
 * By that way, we provide params to following jobs on all the contexts from the current job.
 * But there is no real sense on doing:
 * Corleone.allContexts(this).dispatchJobs(); as we are inside a job.
 *
 * So this interface will be used to return a Corleone instance casted to it on the
 * Corleone.allContexts() method, getting the needed method occlusion for the user. Developers
 * will not really notice the new method dynamic type because of the way this library is used in
 * code. The calls will not really vary in syntax to him, but some of them will not be available
 * and the IDE will not suggest them.
 *
 * @author Jorge Castillo Pérez
 */
interface MultipleContexts {

  void provideParam(String qualifier, Object paramValue);

  void keepGoing();
}
