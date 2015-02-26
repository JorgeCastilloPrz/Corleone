/*
 * Copyright (C) 2014 Jorge Castillo Pérez
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
package com.github.jorgecastilloprz.corleone.internal;

import com.github.jorgecastilloprz.corleone.datamodel.JobAnnotatedClass;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Job dispatcher. Contains the initial executor setup.
 * Using {@link ThreadPoolExecutor} as the executor implementation.
 *
 * Using package visibility to not allow users to reach it from outside.
 *
 * Created by Jorge Castillo Pérez
 */
class ThreadExecutor {

  private static ThreadExecutor INSTANCE = new ThreadExecutor();

  private int corePoolSize = 3;
  private int maxPoolSize = 5;
  private int keepAliveTime = 120;
  private TimeUnit timeUnit = TimeUnit.SECONDS;
  private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

  private ThreadPoolExecutor threadPoolExecutor;

  private ThreadExecutor() {
    threadPoolExecutor =
        new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
            timeUnit, workQueue);
  }

  public static ThreadExecutor getInstance() {
    return INSTANCE;
  }

  public void setCorePoolSize(int corePoolSize) {
    this.corePoolSize = corePoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public void setKeepAliveTime(int keepAliveTime) {
    this.keepAliveTime = keepAliveTime;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
    this.workQueue = workQueue;
  }

  public void run(final JobAnnotatedClass job) {
    if (job == null) {
      throw new IllegalArgumentException("Job must not be null");
    }
    threadPoolExecutor.submit(new Runnable() {
      @Override public void run() {
        job.getExecutionMethod();
      }
    });
  }
}
