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

import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Corleone gateway for external apps. Used to create instances of Corleone to work on defined
 * contexts. Providing param and job dispatching functionalities are exposed to the user by this
 * class too. This class has a normal singleton behavior to get a single instance across all
 * the app execution. To work with it, the user will not call getInstance() method, as the instance
 * will be provided by context() and allContexts() methods.
 * Corleone actions will be like: Corleone.context("MyJobContext").dispatchJobs();
 *
 * @author Jorge Castillo Pérez
 */
public class Corleone implements MultipleContexts {

  static volatile Corleone singleton = null;
  private static List<String> contexts = new ArrayList<>();

  private Corleone() {
  }

  private static Corleone getInstance() {
    if (singleton == null) {
      synchronized (Corleone.class) {
        if (singleton == null) {
          singleton = new Corleone();
        }
      }
    }
    return singleton;
  }

  /**
   * Provides a Corleone instance to work on the given context.
   *
   * @param jobContext to work on
   * @return Corleone singleton instance
   */
  public static Corleone context(String jobContext) {
    if (jobContext == null || jobContext.equals("")) {
      throw new IllegalArgumentException("Job context must not be null or empty.");
    }
    contexts.clear();
    contexts.add(jobContext);
    return getInstance();
  }

  /**
   * Provides Corleone instance to work on every context of the given job.
   *
   * @param job to get the contexts from
   * @return Corleone singleton instance as {@link MultipleContexts} to hide some functionality
   */
  public static MultipleContexts allContexts(Object job) {
    if (job == null) {
      throw new IllegalArgumentException("Job class must not be null.");
    }

    Job jobAnnotation = job.getClass().getAnnotation(Job.class);
    if (jobAnnotation == null) {
      throw new IllegalArgumentException(
          "You need a @Job annotated class to provide params for all it's contexts.");
    }

    contexts.clear();
    Rule[] jobRules = jobAnnotation.value();
    for (Rule rule : jobRules) {
      contexts.add(rule.context());
    }
    return getInstance();
  }

  /**
   * Method making the user capable of providing params to the bus
   *
   * @param qualifier for the param
   */
  @Override public void provideParam(String qualifier, Object paramValue) {
    for (String context : contexts) {
      provideParamForContext(context, qualifier, paramValue);
    }
  }

  /**
   * Gives the user the capability of providing new params for a given context.
   */
  private void provideParamForContext(String context, String qualifier, Object paramValue) {
    if (context == null || context.equals("")) {
      throw new IllegalArgumentException("Context must not be null or empty.");
    }
    if (qualifier == null || qualifier.equals("")) {
      throw new IllegalArgumentException("Param qualifier must not be null or empty.");
    }
    if (paramValue == null) {
      throw new IllegalArgumentException("Param value must not be null.");
    }
    ParamBinderHelper.addProvidedParam(context, qualifier, paramValue);
  }

  public void dispatchJobs(JobParams jobParams) {
    if (jobParams == null) {
      throw new IllegalArgumentException("JobParams must not be null.");
    }

    Map<String, Object> params = jobParams.getParams();
    for (String paramQualifier : params.keySet()) {
      provideParam(paramQualifier, params.get(paramQualifier));
    }
    dispatchJobs();
  }

  public void dispatchJobs() {
    for (String context : contexts) {
      JobDispatcher.getInstance().dispatchJobsWithContext(context);
    }
  }

  @Override public void keepGoing() {
    for (String context : contexts) {
      JobDispatcher.getInstance().keepGoing(context);
    }
  }
}
