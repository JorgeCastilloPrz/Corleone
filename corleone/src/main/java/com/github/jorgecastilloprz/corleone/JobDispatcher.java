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

import com.github.jorgecastilloprz.corleone.exceptions.UncaughtClassNotFoundException;
import com.github.jorgecastilloprz.corleone.exceptions.UncaughtIllegalAccessException;
import com.github.jorgecastilloprz.corleone.exceptions.UncaughtInstantiationException;
import com.github.jorgecastilloprz.corleone.internal.ParamBinder;

/**
 * @author Jorge Castillo Pérez
 */
final class JobDispatcher {

  static volatile JobDispatcher singleton = null;

  private JobDispatcher() {
  }

  static JobDispatcher getInstance() {
    if (singleton == null) {
      synchronized (JobDispatcher.class) {
        if (singleton == null) {
          singleton = new JobDispatcher();
        }
      }
    }
    return singleton;
  }

  void dispatchJobsWithContext(String context) {
    JobQueue jobQueue = JobQueueManager.JOB_QUEUES.get(context);
    if (jobQueue == null) {
      throw new IllegalStateException("There are no jobs declared for context: " + context);
    }
    jobQueue.reset();
    Class<?> jobClass = findInClassPath(jobQueue.getCurrentJob().getQualifiedName());
    Object job = buildJobClassInstance(jobClass);
    String executionMethodName = getExecutionMethodName(jobQueue.getCurrentJob());

    ParamBinder jobParamBinder = obtainJobParamBinder(jobClass.getSimpleName(), context);
    jobParamBinder.bindParams(job);

    dispatchJob(job, executionMethodName);
    jobQueue.moveToNextJob();
    //JobQueueManager.JOB_QUEUES.put(context, jobQueue); NO NEED ? Si el get es por referencia no
  }

  private Class<?> findInClassPath(String qualifiedName) {
    try {
      return Class.forName(qualifiedName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class " + qualifiedName + " could not be found.");
    }
  }

  private Object buildJobClassInstance(Class<?> jobClass) {
    try {
      return jobClass.newInstance();
    } catch (InstantiationException e) {
      throw new UncaughtInstantiationException(jobClass.getCanonicalName());
    } catch (IllegalAccessException e) {
      throw new UncaughtIllegalAccessException(jobClass.getCanonicalName());
    }
  }

  private String getExecutionMethodName(JobDataModel job) {
    return job.getExecutionMethod().getSimpleName().toString();
  }

  /**
   * We could end up not finding the binder or having problems to access or instantiate it. Even if
   * it should not be possible to happen, we are going to catch the exceptions and propagate it
   * to the final user.
   */
  private ParamBinder obtainJobParamBinder(String classSimpleName, String context) {
    try {
      return ParamBinderHelper.getBinderForClassNameAndContext(classSimpleName, context);
    } catch (ClassNotFoundException e) {
      throw new UncaughtClassNotFoundException(classSimpleName);
    } catch (IllegalAccessException e) {
      throw new UncaughtIllegalAccessException(classSimpleName);
    } catch (InstantiationException e) {
      throw new UncaughtInstantiationException(classSimpleName);
    }
  }

  private void dispatchJob(Object job, String executionMethodName) {
    ThreadExecutor.getInstance().submit(job, executionMethodName);
  }
}
