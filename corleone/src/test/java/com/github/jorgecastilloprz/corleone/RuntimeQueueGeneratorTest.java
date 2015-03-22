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
 * To tests the param binder generation functionality, i am using compile-testing (to mock java
 * source files for processor I/O, and Truth to compose the assertion subjects.
 *
 * This example uses a single mock source class that must generate 3 different ParamBinders.
 *
 * @author Jorge Castillo Pérez
 */
public class RuntimeQueueGeneratorTest {

  @Test public void runtimeQueueGenerationTest() {

    JavaFileObject source =
        JavaFileObjects.forSourceString("com.github.jorgecastilloprz.corleone.Test", Joiner.on('\n')
            .join("package com.github.jorgecastilloprz.corleone;",
                "import com.github.jorgecastilloprz.corleone.Corleone;",
                "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
                "import com.github.jorgecastilloprz.corleone.annotations.Job;",
                "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
                "import com.github.jorgecastilloprz.corleone.annotations.Param;", "@Job({",
                "    @Rule(context = \"ObtainGames\"),", "    @Rule(context = \"BookmarkGame\"),",
                "    @Rule(context = \"CommentGame\")", "})", "public class Test {", "\n",
                "   @Param(\"MyString\") String providedString;", "   @Execution",
                "   public void run() {", "       notifyNetworkStatus(true);", "   }",
                "   private void notifyNetworkStatus(final boolean networkAvailable) {",
                "       //mainThread.post(new Runnable() {", "       //    @Override",
                "       //    public void run() {",
                "       //      callback.notifyNetworkStatus(networkAvailable);", "       //    }",
                "      //});", "      if (networkAvailable) {",
                "          Corleone.allContexts(this).keepGoing();", "      }", "   }", "}"));

    JavaFileObject firstSourceGenerated = JavaFileObjects.forSourceString(
        "com.github.jorgecastilloprz.corleone.ObtainGames$$RuntimeQueue",
        "package com.github.jorgecastilloprz.corleone;\n"
            + "\n"
            + "import java.lang.Override;\n"
            + "import java.util.LinkedList;\n"
            + "\n"
            + "public final class ObtainGames$$RuntimeQueue implements RuntimeQueue {\n"
            + "  private int currentHead;\n"
            + "\n"
            + "  private LinkedList<JobDataModel> jobs;\n"
            + "\n"
            + "  public ObtainGames$$RuntimeQueue() {\n"
            + "    this.currentHead = 0;\n"
            + "    this.jobs = new LinkedList<JobDataModel>();\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public void bindQueueValues() {\n"
            + "    JobDataModel job;\n"
            + "    job = new JobDataModel(\"run\",\"com.github.jorgecastilloprz.corleone.EmptyDefaultClass\","
            + "    \"com.github.jorgecastilloprz.corleone\","
            + "    \"Test\","
            + "    \"com.github.jorgecastilloprz.corleone.Test\","
            + "    \"ObtainGames\");\n"
            + "    job.addParam(new ParamFieldDataModel(\"java.lang.String\",\"MyString\",\"providedString\"));\n"
            + "    jobs.add(job);\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public JobDataModel getCurrentJob() {\n"
            + "    return (currentHead < jobs.size()) ? jobs.get(currentHead) : null;\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public void reset() {\n"
            + "    currentHead = 0;\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public void moveToNextJob() {\n"
            + "    currentHead++;\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public boolean hasMoreJobs() {\n"
            + "    return getCurrentJob() != null;\n"
            + "  }\n"
            + "}");

    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(firstSourceGenerated);
  }
}
