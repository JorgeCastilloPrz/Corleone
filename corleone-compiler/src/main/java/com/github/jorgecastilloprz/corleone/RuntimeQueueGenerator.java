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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.LinkedList;
import javax.lang.model.element.Modifier;

/**
 * @author Jorge Castillo Pérez
 */
class RuntimeQueueGenerator {

  private JobQueue jobQueue;

  RuntimeQueueGenerator(JobQueue jobQueue) {
    this.jobQueue = jobQueue;
  }

  JavaFile generate() {
    TypeSpec queueBinder = TypeSpec.classBuilder(getRuntimeQueueClassName())
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.FINAL)
        .addSuperinterface(TypeName.get(RuntimeQueue.class))
        .addField(TypeName.INT, "currentHead", Modifier.PRIVATE)
        .addField(getJobListType(), "jobs", Modifier.PRIVATE)
        .addMethod(generateConstructor())
        .addMethod(generateBindQueueValuesMethod())
        .addMethod(generateGetCurrentJobMethod())
        .addMethod(generateResetMethod())
        .addMethod(generateMoveToNextMethod())
        .addMethod(generateHasMoreJobsMethod())
        .build();

    JavaFile javaFile = JavaFile.builder(NameUtils.RUNTIME_QUEUES_PACKAGE, queueBinder).build();
    return javaFile;
  }

  private String getRuntimeQueueClassName() {
    return jobQueue.getQueueContext() + NameUtils.SEPARATOR + NameUtils.RUNTIME_QUEUE_SUFFIX;
  }

  private ParameterizedTypeName getJobListType() {
    return ParameterizedTypeName.get(LinkedList.class, JobDataModel.class);
  }

  private MethodSpec generateConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addStatement("this.$N = 0", "currentHead")
        .addStatement("this.$N = new $T()", "jobs", getJobListType())
        .build();
  }

  private MethodSpec generateBindQueueValuesMethod() {
    MethodSpec.Builder bindQueueValuesMethod = MethodSpec.methodBuilder("bindQueueValues")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(void.class);

    bindQueueValuesMethod.addStatement("JobDataModel job");
    for (JobDataModel job : jobQueue.getJobs()) {

      bindQueueValuesMethod.addStatement("job = new $T($S,$S,$S,$S,$S,$S)", JobDataModel.class,
          job.getExecutionMethodName(),
          (job.getPreviousJobQualifiedName().equals("") ? EmptyDefaultClass.class.getCanonicalName()
              : job.getPreviousJobQualifiedName()), job.getPackageName(), job.getClassName(),
          job.getQualifiedName(), job.getContext());

      for (ParamFieldDataModel paramField : job.getParams()) {
        bindQueueValuesMethod.addStatement("job.addParam(new $T($S,$S,$S))",
            ParamFieldDataModel.class, paramField.getType(), paramField.getQualifier(),
            paramField.getName());
      }

      bindQueueValuesMethod.addStatement("jobs.add(job)");
    }

    return bindQueueValuesMethod.build();
  }

  private MethodSpec generateGetCurrentJobMethod() {
    MethodSpec.Builder getCurrentJobMethod = MethodSpec.methodBuilder("getCurrentJob")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(JobDataModel.class);

    getCurrentJobMethod.addStatement(
        "return (currentHead < jobs.size()) ? jobs.get(currentHead) : null");

    return getCurrentJobMethod.build();
  }

  private MethodSpec generateResetMethod() {
    MethodSpec.Builder resetMethod = MethodSpec.methodBuilder("reset")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(void.class);

    resetMethod.addStatement("currentHead = 0");
    return resetMethod.build();
  }

  private MethodSpec generateMoveToNextMethod() {
    MethodSpec.Builder moveToNextMethod = MethodSpec.methodBuilder("moveToNextJob")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(void.class);

    moveToNextMethod.addStatement("currentHead++");
    return moveToNextMethod.build();
  }

  private MethodSpec generateHasMoreJobsMethod() {
    MethodSpec.Builder hasMoreJobsMethod = MethodSpec.methodBuilder("hasMoreJobs")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(boolean.class);

    hasMoreJobsMethod.addStatement("return getCurrentJob() != null");
    return hasMoreJobsMethod.build();
  }
}
