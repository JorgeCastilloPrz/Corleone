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

  private List<JobDataModel> queue;
  private String context;
  private int currentHead;

  JobQueue(String context) {
    this.queue = new ArrayList<>();
    this.context = context;
    this.currentHead = 0;
  }

  void addJob(JobDataModel jobEntity) {
    queue.add(jobEntity);
  }

  List<JobDataModel> getPotentialJobRoots() {
    List<JobDataModel> potentialRoots = new ArrayList<>();
    for (JobDataModel currentJob : queue) {
      if (currentJob.getPreviousJobQualifiedName().equals("")) {
        potentialRoots.add(currentJob);
      }
    }
    return potentialRoots;
  }

  String getQueueContext() {
    return context;
  }

  JobDataModel getJobAfter(JobDataModel currentJob) {
    for (JobDataModel job : queue) {
      if (job.getPreviousJobQualifiedName().equals(currentJob.getQualifiedName())) {
        return job;
      }
    }
    return null;
  }

  List<JobDataModel> getJobs() {
    return queue;
  }

  JobDataModel getCurrentJob() {
    return queue.get(currentHead++);
  }
}
