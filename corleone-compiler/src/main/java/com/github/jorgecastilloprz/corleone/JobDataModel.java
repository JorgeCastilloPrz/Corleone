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

import com.github.jorgecastilloprz.corleone.annotations.Param;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Data model to represent a job for the domain logic. It's much better to map classes to
 * domain data models, as reflection api is a little bit tricky and we do not want to keep
 * dealing with it all the time.
 *
 * @author Jorge Castillo Pérez
 */
public class JobDataModel {

  private String executionMethodName;
  private String previousJobQualifiedName;
  private List<ParamFieldDataModel> params;
  private String packageName;
  private String className;
  private String qualifiedName;
  private String context;
  private TypeMirror classType;

  private final String NO_PREVIOUS_JOB = EmptyDefaultClass.class.getCanonicalName();

  JobDataModel(JobAnnotatedClass annotatedClassToMapFrom, String context) {
    executionMethodName = annotatedClassToMapFrom.getExecutionMethodName();
    params = mapParamsToDataModel(annotatedClassToMapFrom.getParamElements());
    previousJobQualifiedName = annotatedClassToMapFrom.getPreviousJobForContext(context);
    packageName = annotatedClassToMapFrom.getPackageName();
    className = annotatedClassToMapFrom.getClassName();
    qualifiedName = annotatedClassToMapFrom.getQualifiedName();
    this.context = context;
    this.classType = annotatedClassToMapFrom.getClassType();
  }

  /**
   * As job queues structure is not going to survive beyond compilation time, we will need to
   * autogenerate classes to fill new runtime queues with JobDataModel values. We will not be
   * able to pass JobDataModels from the compilation time just by reference, because thouse
   * refs will not be available on runtime. We will need to copy all the values to the runtime
   * versions of JobDataModels. See {@link RuntimeQueueGenerator}
   */
  JobDataModel(String executionMethodName, String previousJobQualifiedName,
      String packageName, String className, String qualifiedName, String context) {

    this.executionMethodName = executionMethodName;
    this.previousJobQualifiedName = previousJobQualifiedName;
    this.packageName = packageName;
    this.className = className;
    this.qualifiedName = qualifiedName;
    this.context = context;
    this.params = new ArrayList<>();
  }

  public String getPreviousJobQualifiedName() {
    return (previousJobQualifiedName.equals(NO_PREVIOUS_JOB)) ? "" : previousJobQualifiedName;
  }

  public String getExecutionMethodName() {
    return executionMethodName;
  }

  public List<ParamFieldDataModel> getParams() {
    return params;
  }

  public void addParam(ParamFieldDataModel param) {
    params.add(param);
  }

  private List<ParamFieldDataModel> mapParamsToDataModel(List<VariableElement> paramElements) {
    List<ParamFieldDataModel> paramDataModels = new ArrayList<>();
    for (VariableElement paramElement : paramElements) {
      Param paramAnnotation = paramElement.getAnnotation(Param.class);
      String qualifier = paramAnnotation.value();
      paramDataModels.add(new ParamFieldDataModel(paramElement.asType().toString(), qualifier,
          paramElement.getSimpleName().toString()));
    }
    return paramDataModels;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getContext() {
    return context;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public TypeMirror getClassType() {
    return classType;
  }
}
