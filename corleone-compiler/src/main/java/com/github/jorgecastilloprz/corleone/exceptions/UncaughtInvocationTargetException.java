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
package com.github.jorgecastilloprz.corleone.exceptions;

import java.lang.reflect.InvocationTargetException;

/**
 * RuntimeException wrapper for an {@link InvocationTargetException} used to propagate errors
 * to the user when the @Execution method from a Job class throws an uncaught exception. That
 * kind of exceptions are wrapper into an InvocationTargetException since we are invoking
 * the method using reflection.
 *
 * @author Jorge Castillo Pérez
 */
public class UncaughtInvocationTargetException extends RuntimeException {

  public UncaughtInvocationTargetException(String methodName) {
    super("An exception was thrown by @Execution method " + methodName + " .");
  }
}
