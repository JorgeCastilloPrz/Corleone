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

    Class<?> jobClass = findInClassPath(jobQueue.getCurrentJob().getQualifiedName());
    Object job = getJobClassInstance(jobClass);
    ThreadExecutor.getInstance().submit(job);
  }

  private Class<?> findInClassPath(String qualifiedName) {
    try {
      return Class.forName(qualifiedName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class " + qualifiedName + " could not be found.");
    }
  }

  private Object getJobClassInstance(Class<?> jobClass) {
    try {
      return jobClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(
          "Class " + jobClass.getCanonicalName() + " could not be instantiated.");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(
          "Class " + jobClass.getCanonicalName() + " could not be accessed.");
    }
  }
}