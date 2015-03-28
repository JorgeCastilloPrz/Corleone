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
final class JobDispatcher {

  static volatile JobDispatcher singleton = null;
  private RuntimeQueueCache queueCache;
  private ParamBinderCache paramBinderCache;

  private JobDispatcher() {
    queueCache = new RuntimeQueueCache();
    paramBinderCache = new ParamBinderCache();
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
    RuntimeQueue queue = queueCache.getQueueForContext(context);
    queue.reset();
    queue.bindQueueValues();
    dispatchCurrentJob(queue, context);
  }

  private void dispatchCurrentJob(RuntimeQueue jobQueue, String context) {
    if (!jobQueue.hasMoreJobs()) {
      return;
    }

    Class<?> jobClass = ClassUtils.findInClassPath(jobQueue.getCurrentJob().getQualifiedName());
    Object job = ClassUtils.buildClassInstance(jobClass);
    String executionMethodName = jobQueue.getCurrentJob().getExecutionMethodName();

    ParamBinder jobParamBinder = obtainJobParamBinder(jobClass.getCanonicalName(), context);
    jobParamBinder.bindParams(job);

    ThreadExecutor.getInstance().submit(job, executionMethodName);
  }

  /**
   * We could end up not finding the binder or having problems to access or instantiate it. Even if
   * it should not be possible to happen, we are going to catch the exceptions and propagate it
   * to the final user.
   */
  private ParamBinder obtainJobParamBinder(String classQualifiedName, String context) {
    return paramBinderCache.getBinderForClassNameAndContext(classQualifiedName, context);
  }

  void keepGoing(String context) {
    RuntimeQueue queue = queueCache.getQueueForContext(context);
    queue.moveToNextJob();
    dispatchCurrentJob(queue, context);
  }
}
