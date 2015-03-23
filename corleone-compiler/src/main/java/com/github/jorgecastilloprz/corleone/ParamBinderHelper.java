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
 * job param visibility). Each ParamBinder will have the code for iterating over the params from
 * the corresponding job class asking this helper to find their value on runtime.
 *
 * Provided params will be organized by context, so their qualifiers can be repeated across
 * different contexts.
 *
 * @author Jorge Castillo Pérez
 */
public final class ParamBinderHelper {

  static Map<String, List<ProvidedParamDataModel>> PROVIDED_PARAMS;
  private Filer filer;

  ParamBinderHelper(Filer filer) {
    this.filer = filer;
  }

  static void addProvidedParam(String context, String qualifier, Object value) {
    if (PROVIDED_PARAMS == null) {
      PROVIDED_PARAMS = new LinkedHashMap<>();
    }

    List<ProvidedParamDataModel> providedParamsForContext;
    providedParamsForContext = getOrCreateParamCollectionForContext(context);
    providedParamsForContext.add(new ProvidedParamDataModel(qualifier, value));
    PROVIDED_PARAMS.put(context, providedParamsForContext);
  }

  private static List<ProvidedParamDataModel> getOrCreateParamCollectionForContext(String context) {
    return PROVIDED_PARAMS.containsKey(context) ? PROVIDED_PARAMS.get(context)
        : new ArrayList<ProvidedParamDataModel>();
  }

  public void generateParamBinders() throws IOException {
    for (String context : JobQueueManager.JOB_QUEUES.keySet()) {
      JobQueue currentJobQueue = JobQueueManager.JOB_QUEUES.get(context);
      for (JobDataModel jobModel : currentJobQueue.getJobs()) {
        generateParamBinderForJob(jobModel);
      }
    }
  }

  private void generateParamBinderForJob(JobDataModel jobModel) throws IOException {
    ParamBinderGenerator paramBinderGenerator = new ParamBinderGenerator(jobModel);
    paramBinderGenerator.generate().writeTo(filer);
  }

  public static Object getParamsValueForQualifierAndContext(String qualifier, String context)
      throws IllegalStateException {
    if (PROVIDED_PARAMS.containsKey(context)) {
      List<ProvidedParamDataModel> paramsForContext = PROVIDED_PARAMS.get(context);
      for (ProvidedParamDataModel providedParam : paramsForContext) {
        if (providedParam.getQualifier().equals(qualifier)) {
          return providedParam.getValue();
        }
      }
      throw new IllegalStateException("You must provide a param value for qualifier: " + qualifier);
    } else {
      throw new IllegalStateException("There are not provided params for context " + context);
    }
  }

  static ParamBinder getBinderForClassNameAndContext(String classQualifiedName, String context)
      throws ClassNotFoundException, IllegalAccessException, InstantiationException {
    String binderName = NameUtils.getBinderClassName(classQualifiedName, context);
    return (ParamBinder) Class.forName(binderName).newInstance();
  }
}
