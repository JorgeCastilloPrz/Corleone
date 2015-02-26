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
import com.github.jorgecastilloprz.corleone.datamodel.JobAnnotatedClass;
import com.github.jorgecastilloprz.corleone.messager.ErrorMessager;
import com.github.jorgecastilloprz.corleone.messager.ErrorMessagerImpl;
import com.github.jorgecastilloprz.corleone.validator.AnnotationValidator;
import com.github.jorgecastilloprz.corleone.validator.ClassAnnotationValidator;
import com.github.jorgecastilloprz.corleone.validator.FieldAnnotationValidator;
import com.github.jorgecastilloprz.corleone.validator.MethodAnnotationValidator;
import com.github.jorgecastilloprz.corleone.validator.SingleAnnotationInstanceValidator;
import com.google.auto.service.AutoService;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
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
@AutoService(Processor.class)
public class CorleoneProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private Types typeUtils;
  private Filer filer;

  private ErrorMessager errorMessager;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    typeUtils = processingEnv.getTypeUtils();
    filer = processingEnv.getFiler();
    errorMessager = new ErrorMessagerImpl(processingEnv.getMessager());
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
    return supportTypes;
  }

  @Override public boolean process(Set<? extends TypeElement> typeElements,
      RoundEnvironment roundEnvironment) {

    if (!annotationValidationSuccess(roundEnvironment)) {
      return false;
    }

    List<JobAnnotatedClass> jobAnnotatedClasses =
        parseJobs(roundEnvironment.getElementsAnnotatedWith(Job.class));

    return false;
  }

  private boolean annotationValidationSuccess(RoundEnvironment roundEnvironment) {
    AnnotationValidator singleAnnotationValidator, jobClassAnnotationValidator,
        ruleClassAnnotationValidator, executionMethodAnnotationValidator,
        paramFieldAnnotationValidator;

    jobClassAnnotationValidator =
        new ClassAnnotationValidator(roundEnvironment, errorMessager, Job.class);
    ruleClassAnnotationValidator =
        new ClassAnnotationValidator(roundEnvironment, errorMessager, Rule.class);
    executionMethodAnnotationValidator =
        new MethodAnnotationValidator(roundEnvironment, errorMessager, Execution.class);
    paramFieldAnnotationValidator =
        new FieldAnnotationValidator(roundEnvironment, errorMessager, Param.class);
    singleAnnotationValidator =
        new SingleAnnotationInstanceValidator(roundEnvironment, errorMessager, Execution.class);

    if (!jobClassAnnotationValidator.validate()
        || !ruleClassAnnotationValidator.validate()
        || !executionMethodAnnotationValidator.validate()
        || !paramFieldAnnotationValidator.validate()
        || !singleAnnotationValidator.validate()) {
      return false;
    }
    return true;
  }

  private List<JobAnnotatedClass> parseJobs(Set<? extends Element> jobElements) {
    List<JobAnnotatedClass> jobAnnotatedClasses = new LinkedList<>();

    for (Element jobElement : jobElements) {
      // We can cast it to TypeElement as we know its a class (validators)
      TypeElement jobTypeElement = (TypeElement) jobElement;
      jobAnnotatedClasses.add(new JobAnnotatedClass(jobTypeElement));
    }
    return jobAnnotatedClasses;
  }
}
