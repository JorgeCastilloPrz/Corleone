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

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;
import org.truth0.Truth;

import static com.github.jorgecastilloprz.corleone.TestProcessors.corleoneProcessors;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Tests to assure annotation validation behavior. Based on a compile-testing / truth
 * testing framework mix. This class defines three tests to check all the validation possibilities
 * out.
 * *
 * Compile-testing is a google library that allows the user to write mock java sources for using
 * them as input and output of testing subjects. It is a useful way to run tests at javac
 * compilation time.
 * *
 * Test subjects are composed and executed by Truth. Truth is a google testing library to create
 * testing subjects in a very humanized language level.
 *
 * @author Jorge Castillo Pérez
 */
public class SingleAnnotationInstanceValidatorTest {

  @Test public void correctAnnotationCountTest() {

    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.annotations.Dispatcher;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.MainThread;",
            "public class Test {", "  @Dispatcher", "  public void execute() {",
            "    System.out.print(\"Testing dispatcher method!\");", "  }", "  @Execution",
            "  public void run() {", "    System.out.print(\"Testing execution method!\");", "  }",
            "  @MainThread", "  private void notifyOnConnectionAvaiable() {",
            "    //mainThread.post(new Runnable() {", "    //  @Override",
            "    //  public void run() {", "    //callback.onConnectionAvaiable();", "    //  }",
            "    //});", "  }", "  @MainThread", "  private void notifyOnConnectionError() {",
            "  }", "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .compilesWithoutError();
    //.and()
    //.generatesSources(expectedSource);
  }

  @Test public void wrongExecutionCountTest() {

    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.annotations.Dispatcher;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.MainThread;",
            "public class Test {", "  @Execution", "  public void execute() {",
            "    System.out.print(\"Testing dispatcher method!\");", "  }", "  @Execution",
            "  public void run() {", "    System.out.print(\"Testing execution method!\");", "  }",
            "  @MainThread", "  private void notifyOnConnectionAvaiable() {",
            "    //mainThread.post(new Runnable() {", "    //  @Override",
            "    //  public void run() {", "    //callback.onConnectionAvaiable();", "    //  }",
            "    //});", "  }", "  @MainThread", "  private void notifyOnConnectionError() {",
            "  }", "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .failsToCompile();
  }

  @Test public void wrongDispatcherCountTest() {

    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.annotations.Dispatcher;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.MainThread;",
            "public class Test {", "  @Dispatcher", "  public void execute() {",
            "    System.out.print(\"Testing dispatcher method!\");", "  }", "  @Dispatcher",
            "  public void run() {", "    System.out.print(\"Testing execution method!\");", "  }",
            "  @MainThread", "  private void notifyOnConnectionAvaiable() {",
            "    //mainThread.post(new Runnable() {", "    //  @Override",
            "    //  public void run() {", "    //callback.onConnectionAvaiable();", "    //  }",
            "    //});", "  }", "  @MainThread", "  private void notifyOnConnectionError() {",
            "  }", "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .failsToCompile();
  }
}
