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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jorge Castillo Pérez
 */
class JobQueue {

  private List<JobEntity> queue;
  private String context;
  private int currentHead;

  JobQueue(String context) {
    this.queue = new ArrayList<>();
    this.context = context;
    this.currentHead = 0;
  }

  void addJob(JobEntity jobEntity) {
    queue.add(jobEntity);
  }

  List<JobEntity> getPotentialJobRoots() {
    List<JobEntity> potentialRoots = new ArrayList<>();
    for (JobEntity currentJob : queue) {
      if (currentJob.getPreviousJobQualifiedName().equals("")) {
        potentialRoots.add(currentJob);
      }
    }
    return potentialRoots;
  }

  String getQueueContext() {
    return context;
  }

  JobEntity getJobAfter(JobEntity currentJob) {
    for (JobEntity job : queue) {
      if (job.getPreviousJobQualifiedName()
          .equals(currentJob.getAnnotatedClassElement().getQualifiedName().toString())) {
        return job;
      }
    }
    return null;
  }

  List<JobEntity> getJobs() {
    return queue;
  }

  JobEntity getCurrentJob() {
    return queue.get(currentHead++);
  }
}
