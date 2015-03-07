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
 * Tests to assure job queue correct organization. Based on a compile-testing / truth
 * testing framework mix.
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
public class JobQueueManagerTest {

  @Test public void correctQueueGenerationTest() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.Corleone;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.Job;",
            "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
            "import com.github.jorgecastilloprz.corleone.annotations.Param;",
            "@Job({",
            "    @Rule(context = \"ObtainGames\"),",
            "    @Rule(context = \"BookmarkGame\"),",
            "    @Rule(context = \"CommentGame\")",
            "})",
            "public class Test {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test.class)",
            "})",
            "class Test2 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test2.class)",
            "})",
            "class Test3 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .compilesWithoutError();
  }

  @Test public void multipleRootNumberTest() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.Corleone;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.Job;",
            "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
            "import com.github.jorgecastilloprz.corleone.annotations.Param;",
            "@Job({",
            "    @Rule(context = \"ObtainGames\"),",
            "    @Rule(context = \"BookmarkGame\"),",
            "    @Rule(context = \"CommentGame\")",
            "})",
            "public class Test {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test.class)",
            "})",
            "class Test2 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\")",
            "})",
            "class Test3 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .failsToCompile();
  }

  @Test public void zeroRootNumberTest() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.Corleone;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.Job;",
            "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
            "import com.github.jorgecastilloprz.corleone.annotations.Param;",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test2.class),",
            "    @Rule(context = \"BookmarkGame\", previousJob = Test2.class),",
            "    @Rule(context = \"CommentGame\", previousJob = Test2.class)",
            "})",
            "public class Test {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test.class)",
            "})",
            "class Test2 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test.class)",
            "})",
            "class Test3 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .failsToCompile();
  }

  @Test public void queueJobCycleTest() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;",
            "import com.github.jorgecastilloprz.corleone.Corleone;",
            "import com.github.jorgecastilloprz.corleone.annotations.Execution;",
            "import com.github.jorgecastilloprz.corleone.annotations.Job;",
            "import com.github.jorgecastilloprz.corleone.annotations.Rule;",
            "import com.github.jorgecastilloprz.corleone.annotations.Param;",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test3.class),",
            "    @Rule(context = \"BookmarkGame\"),",
            "    @Rule(context = \"CommentGame\")",
            "})",
            "public class Test {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test.class)",
            "})",
            "class Test2 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\", previousJob = Test2.class)",
            "})",
            "class Test3 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}",
            "@Job({",
            "    @Rule(context = \"ObtainGames\")",
            "})",
            "class Test4 {",
            "\n",
            "   @Param(\"MyString\") String providedString;",
            "   @Execution",
            "   public void run() {",
            "       notifyNetworkStatus(true);",
            "   }",
            "   private void notifyNetworkStatus(final boolean networkAvailable) {",
            "       //mainThread.post(new Runnable() {",
            "       //    @Override",
            "       //    public void run() {",
            "       //      callback.notifyNetworkStatus(networkAvailable);",
            "       //    }",
            "      //});",
            "      if (networkAvailable) {",
            "          Corleone.keepGoing();",
            "      }",
            "   }",
            "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .failsToCompile();
  }
}
