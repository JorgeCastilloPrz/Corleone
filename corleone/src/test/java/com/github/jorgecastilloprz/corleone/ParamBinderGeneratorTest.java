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
public class ParamBinderGeneratorTest {

  @Test public void paramBinderGenerationTest() {

    JavaFileObject source =
        JavaFileObjects.forSourceString("com.github.jorgecastilloprz.corleone.Test", Joiner.on('\n')
                .join("package com.github.jorgecastilloprz.corleone;",
                    "import com.github.jorgecastilloprz.corleone.Corleone;",
                    "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
                    "import com.github.jorgecastilloprz.corleone.annotations.Job;",
                    "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
                    "import com.github.jorgecastilloprz.corleone.annotations.Param;", "@Job({",
                    "    @Rule(context = \"ObtainGames\"),",
                    "    @Rule(context = \"BookmarkGame\"),",
                    "    @Rule(context = \"CommentGame\")", "})", "public class Test {", "\n",
                    "   @Param(\"MyString\") String providedString;",
                    "   @Param(\"TestParam2\") String myTestParam2;",
                    "   @Param(\"MyNewArgument\") String myNewArg;", "\n", "   @Execution",
                    "   public void run() {", "       notifyNetworkStatus(true);", "   }",
                    "   private void notifyNetworkStatus(final boolean networkAvailable) {",
                    "       //mainThread.post(new Runnable() {", "       //    @Override",
                    "       //    public void run() {",
                    "       //      callback.notifyNetworkStatus(networkAvailable);",
                    "       //    }", "      //});", "      if (networkAvailable) {",
                    "          Corleone.allContexts(this).keepGoing();", "      }", "   }", "}"));

    JavaFileObject firstSourceGenerated = JavaFileObjects.forSourceString(
        "com.github.jorgecastilloprz.corleone.Test$$ObtainGames$$ParamBinder",
        "package com.github.jorgecastilloprz.corleone;\n"
            + "\n"
            + "import java.lang.Override;\n"
            + "\n"
            + "public final class Test$$ObtainGames$$ParamBinder implements ParamBinder<Test> {\n"
            + "  @Override\n"
            + "  public void bindParams(Test target) {\n"
            + "    target.providedString = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyString\",\"ObtainGames\");\n"
            + "    target.myTestParam2 = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"TestParam2\",\"ObtainGames\");\n"
            + "    target.myNewArg = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyNewArgument\",\"ObtainGames\");\n"
            + "  }\n"
            + "}");

    JavaFileObject secondSourceGenerated = JavaFileObjects.forSourceString(
        "com.github.jorgecastilloprz.corleone.Test$$BookmarkGame$$ParamBinder",
        "package com.github.jorgecastilloprz.corleone;\n"
            + "\n"
            + "import java.lang.Override;\n"
            + "\n"
            + "public final class Test$$BookmarkGame$$ParamBinder implements ParamBinder<Test> {\n"
            + "  @Override\n"
            + "  public void bindParams(Test target) {\n"
            + "    target.providedString = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyString\",\"BookmarkGame\");\n"
            + "    target.myTestParam2 = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"TestParam2\",\"BookmarkGame\");\n"
            + "    target.myNewArg = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyNewArgument\",\"BookmarkGame\");\n"
            + "  }\n"
            + "}");

    JavaFileObject thirdSourceGenerated = JavaFileObjects.forSourceString(
        "com.github.jorgecastilloprz.corleone.Test$$CommentGame$$ParamBinder",
        "package com.github.jorgecastilloprz.corleone;\n"
            + "\n"
            + "import java.lang.Override;\n"
            + "\n"
            + "public final class Test$$CommentGame$$ParamBinder implements ParamBinder<Test> {\n"
            + "  @Override\n"
            + "  public void bindParams(Test target) {\n"
            + "    target.providedString = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyString\",\"CommentGame\");\n"
            + "    target.myTestParam2 = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"TestParam2\",\"CommentGame\");\n"
            + "    target.myNewArg = (java.lang.String) ParamBinderHelper"
            + "       .getParamsValueForQualifierAndContext(\"MyNewArgument\",\"CommentGame\");\n"
            + "  }\n"
            + "}");

    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(firstSourceGenerated, secondSourceGenerated, thirdSourceGenerated);
  }
}
