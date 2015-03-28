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
package com.github.jorgecastilloprz.corleone;

import com.github.jorgecastilloprz.corleone.exceptions.UncaughtIllegalAccessException;
import com.github.jorgecastilloprz.corleone.exceptions.UncaughtInvocationTargetException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  private static volatile ThreadExecutor instance = null;

  private int corePoolSize = 3;
  private int maxPoolSize = 5;
  private long keepAliveTime = 120;
  private TimeUnit timeUnit = TimeUnit.SECONDS;
  private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

  private ThreadPoolExecutor threadPoolExecutor;

  private ThreadExecutor() {
    threadPoolExecutor =
        new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue);
  }

  static ThreadExecutor getInstance() {
    if (instance == null) {
      synchronized (ThreadExecutor.class) {
        if (instance == null) {
          instance = new ThreadExecutor();
        }
      }
    }
    return instance;
  }

  public void setCorePoolSize(int corePoolSize) {
    this.corePoolSize = corePoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public void setKeepAliveTime(long keepAliveTime) {
    this.keepAliveTime = keepAliveTime;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public void setBlockingQueue(BlockingQueue<Runnable> workQueue) {
    this.workQueue = workQueue;
  }

  public void submit(final Object jobInstance, String methodName) {
    try {
      final Method executionMethod = jobInstance.getClass().getDeclaredMethod(methodName);
      threadPoolExecutor.submit(new Runnable() {
        @Override public void run() {
          invokeExecutionMethod(jobInstance, executionMethod);
        }
      });
    } catch (NoSuchMethodException e) {
      /*Code will never reach this point, as we controlled method mandatory needs in
      the compilation time */
      e.printStackTrace();
    }
  }

  /**
   * If the Execution method throws an exception, it will be wrapper into an
   * {@link InvocationTargetException}. We must catch it here and throw an uncaught exception
   * to inform the user about the existent problem.
   */
  private void invokeExecutionMethod(Object jobInstance, Method executionMethod) {
    try {
      executionMethod.invoke(jobInstance);
    } catch (IllegalAccessException e) {
      throw new UncaughtIllegalAccessException(executionMethod.getName());
    } catch (InvocationTargetException e) {
      throw new UncaughtInvocationTargetException(executionMethod.getName());
    }
  }
}
