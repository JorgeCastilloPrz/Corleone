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

import com.github.jorgecastilloprz.corleone.exceptions.CyclicReferenceException;
import com.github.jorgecastilloprz.corleone.exceptions.MultipleRootsException;
import com.github.jorgecastilloprz.corleone.exceptions.NoRootsException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Job queues will be saved here grouped by contexts and ordered by contained job rules, and they
 * will become avilable for Corleone.java to access them at runtime.
 *
 * @author Jorge Castillo Pérez
 */
class JobQueueManager {

  static final Map<String, JobQueue> JOB_QUEUES = new LinkedHashMap<>();

  JobQueueManager(List<JobAnnotatedClass> jobAnnotatedClasses)
      throws NoRootsException, MultipleRootsException, CyclicReferenceException {
    orderJobQueues(groupJobsByContext(jobAnnotatedClasses));
  }

  /**
   * This method just groups jobs by context but it does not order them. It maps
   * JobAnnotatedClasses to JobEntities to avoid keeping useless info any longer.
   *
   * @param jobAnnotatedClasses jobs to group
   */
  private Map<String, JobQueue> groupJobsByContext(List<JobAnnotatedClass> jobAnnotatedClasses)
      throws NoRootsException, MultipleRootsException, CyclicReferenceException {

    Map<String, JobQueue> groupedJobQueues = new LinkedHashMap<>();

    for (JobAnnotatedClass jobAnnotatedClass : jobAnnotatedClasses) {
      List<RuleEntity> currentJobRules = jobAnnotatedClass.getRules();
      for (RuleEntity rule : currentJobRules) {
        JobQueue queueForThisJob;
        if (groupedJobQueues.containsKey(rule.getContext())) {
          queueForThisJob = groupedJobQueues.get(rule.getContext());
        } else {
          queueForThisJob = new JobQueue(rule.getContext());
        }
        queueForThisJob.addJob(new JobEntity(jobAnnotatedClass, rule.getContext()));
        groupedJobQueues.put(rule.getContext(), queueForThisJob);
      }
    }
    return groupedJobQueues;
  }

  private void orderJobQueues(Map<String, JobQueue> queuesToOrder)
      throws NoRootsException, MultipleRootsException, CyclicReferenceException {

    for (String queueContext : queuesToOrder.keySet()) {
      JobQueue currentQueue = queuesToOrder.get(queueContext);
      checkRootCount(currentQueue);
      checkJobCycles(currentQueue);
      JOB_QUEUES.put(queueContext, orderQueue(currentQueue, queueContext));
    }
  }

  private void checkRootCount(JobQueue jobQueue) throws NoRootsException, MultipleRootsException {

    int potentialRootsSize = jobQueue.getPotentialJobRoots().size();
    if (potentialRootsSize < 1) {
      throw new NoRootsException(jobQueue.getQueueContext());
    } else if (potentialRootsSize > 1) {
      throw new MultipleRootsException(jobQueue.getQueueContext());
    }
  }

  private void checkJobCycles(JobQueue currentQueue) throws CyclicReferenceException {
    for (JobEntity currentJob : currentQueue.getJobs()) {
      followPathLookingForItself(currentJob, currentQueue);
    }
  }

  private void followPathLookingForItself(JobEntity job, JobQueue queue)
      throws CyclicReferenceException {
    
    JobEntity followingJob = queue.getJobAfter(job);
    while (followingJob != null) {
      if (hasTheSameQualifiedName(job, followingJob)) {
        throw new CyclicReferenceException(queue.getQueueContext());
      } else {
        followingJob = queue.getJobAfter(followingJob);
      }
    }
  }

  private boolean hasTheSameQualifiedName(JobEntity job1, JobEntity job2) {
    return job1.getAnnotatedClassElement().getQualifiedName().toString().
        equals(job2.getAnnotatedClassElement().getQualifiedName().toString());
  }

  private JobQueue orderQueue(JobQueue jobQueue, String context) {
    JobQueue orderedQueue = new JobQueue(context);
    JobEntity rootJob = jobQueue.getPotentialJobRoots().get(0);
    orderedQueue.addJob(rootJob);
    JobEntity currentJob = rootJob;
    while (jobQueue.getJobAfter(currentJob) != null) {
      currentJob = jobQueue.getJobAfter(currentJob);
      orderedQueue.addJob(currentJob);
    }

    return orderedQueue;
  }
}
