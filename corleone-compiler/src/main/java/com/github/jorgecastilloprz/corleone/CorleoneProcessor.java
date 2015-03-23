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

import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Param;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import com.github.jorgecastilloprz.corleone.messager.ErrorMessagerImpl;
import com.github.jorgecastilloprz.corleone.validator.ClassKindValidator;
import com.github.jorgecastilloprz.corleone.validator.ElementCollectionValidator;
import com.github.jorgecastilloprz.corleone.validator.FieldKindValidator;
import com.github.jorgecastilloprz.corleone.validator.MethodKindValidator;
import com.github.jorgecastilloprz.corleone.validator.NoExecutionArgumentsValidator;
import com.github.jorgecastilloprz.corleone.validator.SingleAnnotationValidator;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Allows the user to generate a simple thread pool executor implementation for java applications
 * based on annotations.
 *
 * @author Jorge Castillo Pérez
 */
public final class CorleoneProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private Types typeUtils;
  private Filer filer;
  private boolean isFirstProcessingRound;

  private Set<? extends Element> rootElements, jobElements, ruleElements, paramElements,
      executionElements;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    typeUtils = processingEnv.getTypeUtils();
    filer = processingEnv.getFiler();
    isFirstProcessingRound = true;
    ErrorMessagerImpl.getInstance().setMessager(processingEnv.getMessager());
  }

  /**
   * We want to support java versions up to the current JDK one
   *
   * @return latest supported java version
   */
  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  /**
   * @return annotations supported by this processor
   */
  @Override public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportTypes = new LinkedHashSet<>();
    supportTypes.add(Execution.class.getCanonicalName());
    supportTypes.add(Job.class.getCanonicalName());
    supportTypes.add(Rule.class.getCanonicalName());
    supportTypes.add(Param.class.getCanonicalName());
    return supportTypes;
  }

  @Override public boolean process(Set<? extends TypeElement> typeElements,
      RoundEnvironment roundEnvironment) {

    //Its important not to generate classes again for following processing rounds
    if (isFirstProcessingRound) {
      isFirstProcessingRound = false;
    } else {
      return false;
    }

    obtainAnnotatedElements(roundEnvironment);
    if (!annotationValidationSuccess()) {
      return false;
    }

    List<JobAnnotatedClass> jobAnnotatedClasses = parseJobs(jobElements);
    JobQueueManager jobQueueManager;
    try {
      jobQueueManager = new JobQueueManager(jobAnnotatedClasses);
    } catch (IllegalStateException exception) {
      ErrorMessagerImpl.getInstance().error(exception.getMessage());
      return false;
    }

    ParamBinderHelper paramBinderHelper = new ParamBinderHelper(filer);
    try {
      paramBinderHelper.generateParamBinders();
    } catch (IOException exception) {
      ErrorMessagerImpl.getInstance().error(exception.getMessage());
      return false;
    }

    RuntimeQueueHelper runtimeQueueHelper = new RuntimeQueueHelper(filer);
    try {
      runtimeQueueHelper.generateRuntimeQueues();
    } catch (IOException exception) {
      ErrorMessagerImpl.getInstance().error(exception.getMessage());
      return false;
    }

    return false;
  }

  private void obtainAnnotatedElements(RoundEnvironment roundEnvironment) {
    rootElements = roundEnvironment.getRootElements();
    jobElements = roundEnvironment.getElementsAnnotatedWith(Job.class);
    ruleElements = roundEnvironment.getElementsAnnotatedWith(Rule.class);
    paramElements = roundEnvironment.getElementsAnnotatedWith(Param.class);
    executionElements = roundEnvironment.getElementsAnnotatedWith(Execution.class);
  }

  private boolean annotationValidationSuccess() {
    ElementCollectionValidator singleAnnotationValidator,
        jobClassKindValidator,
        ruleClassKindValidator,
        executionMethodKindValidator,
        paramFieldKindValidator,
        noExecutionArgumentsValidator;

    jobClassKindValidator = new ClassKindValidator(jobElements);
    ruleClassKindValidator = new ClassKindValidator(ruleElements);
    executionMethodKindValidator = new MethodKindValidator(executionElements);
    paramFieldKindValidator = new FieldKindValidator(paramElements);
    singleAnnotationValidator = new SingleAnnotationValidator(rootElements, Execution.class);
    noExecutionArgumentsValidator = new NoExecutionArgumentsValidator(executionElements);

    if (!jobClassKindValidator.validate()
        || !ruleClassKindValidator.validate()
        || !executionMethodKindValidator.validate()
        || !paramFieldKindValidator.validate()
        || !singleAnnotationValidator.validate()
        || !noExecutionArgumentsValidator.validate()) {
      return false;
    }
    return true;
  }

  private List<JobAnnotatedClass> parseJobs(Set<? extends Element> jobElements) {
    List<JobAnnotatedClass> jobAnnotatedClasses = new LinkedList<>();

    for (Element jobElement : jobElements) {
      // We can cast it to TypeElement as we know its a class (validators)
      TypeElement jobTypeElement = (TypeElement) jobElement;
      jobAnnotatedClasses.add(new JobAnnotatedClass(jobTypeElement, elementUtils));
    }
    return jobAnnotatedClasses;
  }
}
