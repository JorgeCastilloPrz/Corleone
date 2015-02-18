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

import com.github.jorgecastilloprz.corleone.annotations.Dispatcher;
import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.MainThread;
import com.github.jorgecastilloprz.corleone.messager.CorleoneErrorMessager;
import com.github.jorgecastilloprz.corleone.messager.ErrorMessager;
import com.github.jorgecastilloprz.corleone.validator.AnnotationValidator;
import com.github.jorgecastilloprz.corleone.validator.SingleAnnotationInstanceValidator;
import com.google.auto.service.AutoService;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
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

  private AnnotationValidator annotationValidator;
  private ErrorMessager errorMessager;

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    typeUtils = processingEnv.getTypeUtils();
    filer = processingEnv.getFiler();
    errorMessager = new CorleoneErrorMessager(processingEnv.getMessager());
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
    supportTypes.add(Dispatcher.class.getCanonicalName());
    supportTypes.add(Execution.class.getCanonicalName());
    supportTypes.add(MainThread.class.getCanonicalName());
    return supportTypes;
  }

  @Override public boolean process(Set<? extends TypeElement> typeElements,
      RoundEnvironment roundEnvironment) {
    
    annotationValidator = new SingleAnnotationInstanceValidator(roundEnvironment, errorMessager);
    if (!annotationValidator.validate()) {
      return false;
    }
    
    /* do stuff */

    return false;
  }

  //Set<? extends Element> dispatcherElements =
  //    roundEnvironment.getElementsAnnotatedWith(Dispatcher.class);
  //Set<? extends Element> executionElements =
  //    roundEnvironment.getElementsAnnotatedWith(Execution.class);
}
