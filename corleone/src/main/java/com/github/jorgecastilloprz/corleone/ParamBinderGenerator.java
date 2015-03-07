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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import javax.lang.model.element.Modifier;

/**
 * This class wraps all the logic for generating a ParamBinder java source file. It is using
 * Square's JavaPoet for source generation.
 *
 * @author Jorge Castillo Pérez
 */
class ParamBinderGenerator {

  private final String SEPARATOR = "$$";
  private final String SUFFIX = "ParamBinder";
  private JobDataModel jobDataModel;

  ParamBinderGenerator(JobDataModel jobDataModel) {
    this.jobDataModel = jobDataModel;
  }

  JavaFile generate() {
    TypeSpec paramBinder = TypeSpec.classBuilder(
        jobDataModel.getClassName() + SEPARATOR + jobDataModel.getContext() + SEPARATOR + SUFFIX)
        .addModifiers(Modifier.FINAL)
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ParamBinder.class),
            TypeVariableName.get(jobDataModel.getClassName())))
        .addMethod(generateBindParamsMethod())
        .build();

    JavaFile javaFile = JavaFile.builder(jobDataModel.getPackageName(), paramBinder).build();
    return javaFile;
  }

  private MethodSpec generateBindParamsMethod() {
    MethodSpec bindParamsMethod = MethodSpec.methodBuilder("bindParams")
        .addModifiers(Modifier.PRIVATE)
        .returns(void.class)
        .addParameter(ClassName.get(jobDataModel.getClassType()), "target")
        .beginControlFlow("for (ParamFieldDataModel paramField : jobDataModel.getParams())")
        .addStatement("String paramQualifier = paramField.getQualifier();")
        .addStatement("String jobContext = jobDataModel.getContext();")
        .beginControlFlow("if (ParamBinderHelper.PROVIDED_PARAMS.containsKey(jobContext))")
        .addStatement("List<ProvidedParamDataModel> providedParamsForContext =\n"
            + "            ParamBinderHelper.PROVIDED_PARAMS.get(jobContext);")
        .addStatement("boolean paramFound = false;")
        .beginControlFlow("for (ProvidedParamDataModel providedParam : providedParamsForContext)")
        .beginControlFlow("if (providedParam.getQualifier().equals(paramQualifier))")
        .addStatement("paramFound = true;")
        .addStatement("target.\" + paramField.getName() + \" = \" + providedParam.getValue() + \"")
        .endControlFlow()
        .endControlFlow()
        .beginControlFlow("!paramFound")
        .addStatement(
            "throw new IllegalStateException(\"You must provide a param value for \" + paramField.getName());")
        .endControlFlow()
        .nextControlFlow("else")
        .addStatement(
            "throw new IllegalStateException(\"There are not provided params for context \" + jobContext);")
        .endControlFlow()
        .endControlFlow()

        .build();

    //for (ParamFieldDataModel paramField : jobDataModel.getParams()) {
    //  String paramQualifier = paramField.getQualifier();
    //  String jobContext = jobDataModel.getContext();

    //if (ParamBinderHelper.PROVIDED_PARAMS.containsKey(jobContext)) {
    //  List<ProvidedParamDataModel> providedParamsForContext =
    //      ParamBinderHelper.PROVIDED_PARAMS.get(jobContext);

    //boolean paramFound = false;
    //for (ProvidedParamDataModel providedParam : providedParamsForContext) {
    //  if (providedParam.getQualifier().equals(paramQualifier)) {
    //    paramFound = true;
    //    target.paramField.getName() = providedParam.getValue();
    //  }
    //}
    //if (!paramFound) {
    //  throw new IllegalStateException("You must provide a param value for " + paramField.getName());
    //}
    //} else {
    //  throw new IllegalStateException("There are not provided params for context " + jobContext);
    //}
    //}

    return bindParamsMethod;
  }
}
