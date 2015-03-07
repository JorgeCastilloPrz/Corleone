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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Filer;

/**
 * This class generates ParamBinder classes for every existent job by using JavaPoet square lib.
 * ParamBinder name will be JobName$$Context$$ParamBinder. Each
 * param binder will have the same package as the job class (to allow both package and public
 * job param visibility). Each job ParamBinder will have a method to iterate over all the params
 * for that job, resolving an assignation like
 * jobclass.paramname = getParamValueForQualifierAndContext(String qualifier, String context).
 *
 * Provided params will be organized by context, so their qualifiers can be repeated across
 * different contexts.
 *
 * @author Jorge Castillo Pérez
 */
final class ParamBinderHelper {

  static Map<Class<?>, ParamBinder> PARAM_BINDERS;
  static Map<String, List<ProvidedParamDataModel>> PROVIDED_PARAMS;
  private Filer filer;

  ParamBinderHelper(Filer filer) {
    PROVIDED_PARAMS = new LinkedHashMap<>();
    this.filer = filer;
  }

  static void addProvidedParam(String context, String qualifier, Class<?> value) {
    List<ProvidedParamDataModel> providedParamsForContext;
    providedParamsForContext = getOrCreateParamCollectionForContext(context);
    providedParamsForContext.add(new ProvidedParamDataModel(qualifier, value));
  }

  private static List<ProvidedParamDataModel> getOrCreateParamCollectionForContext(String context) {
    if (PROVIDED_PARAMS.containsKey(context)) {
      return PROVIDED_PARAMS.get(context);
    } else {
      return new ArrayList<>();
    }
  }

  public void generateParamBinders() throws IOException {
    PARAM_BINDERS = new LinkedHashMap<>();
    for (String context : JobQueueManager.JOB_QUEUES.keySet()) {
      JobQueue currentJobQueue = JobQueueManager.JOB_QUEUES.get(context);
      for (JobDataModel jobModel : currentJobQueue.getJobs()) {
        generateParamBinderForJob(jobModel);
      }
    }
  }

  private void generateParamBinderForJob(JobDataModel jobModel)
      throws IOException {
    ParamBinderGenerator paramBinderGenerator = new ParamBinderGenerator(jobModel);
    paramBinderGenerator.generate().writeTo(filer);
  }
}
