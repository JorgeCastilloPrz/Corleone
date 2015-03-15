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
import javax.tools.JavaFileObject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Test;
import org.truth0.Truth;

import static com.github.jorgecastilloprz.corleone.TestProcessors.corleoneProcessors;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author Jorge Castillo Pérez
 */
public class JobQueueTest {

  private JobQueue obtainGamesQueue;
  private JobQueue bookMarkGameQueue;
  private JobQueue commentGameQueue;

  @Before public void setup() {
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
            "      //callback.notifyNetworkStatus(networkAvailable);",
            "      if (networkAvailable) {",
            "          Corleone.context(\"ObtainGames\").keepGoing();",
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
            "      //callback.notifyNetworkStatus(networkAvailable);",
            "      if (networkAvailable) {",
            "          Corleone.context(\"ObtainGames\").keepGoing();",
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
            "      //callback.notifyNetworkStatus(networkAvailable);",
            "      if (networkAvailable) {",
            "          Corleone.context(\"ObtainGames\").keepGoing();",
            "      }",
            "   }",
            "}"));

    //Truth assertion
    Truth.ASSERT.about(javaSource())
        .that(source)
        .processedWith(corleoneProcessors())
        .compilesWithoutError();

    obtainGamesQueue = JobQueueManager.JOB_QUEUES.get("ObtainGames");
    bookMarkGameQueue = JobQueueManager.JOB_QUEUES.get("BookmarkGame");
    commentGameQueue = JobQueueManager.JOB_QUEUES.get("CommentGame");
  }

  @Test public void currentJobTest() {
    JobDataModel currentJob = obtainGamesQueue.getCurrentJob();
    Truth.ASSERT.that(currentJob.getClassName()).isNotNull();
    Truth.ASSERT.that(currentJob.getClassName()).isEqualTo("Test");
  }

  @Test public void addItemTest() {
    JobQueue jobQueue = new JobQueue("ObtainGames");

    Truth.ASSERT.that(jobQueue.getJobs()).isNotNull();

    jobQueue.addJob(obtainGamesQueue.getCurrentJob());
    Truth.ASSERT.that(jobQueue.getJobs().size() == 1);

    jobQueue.addJob(obtainGamesQueue.getCurrentJob());
    jobQueue.addJob(obtainGamesQueue.getCurrentJob());
    Truth.ASSERT.that(jobQueue.getJobs().size() == 3);
  }

  @Test public void getPotentialRootsTest() {

    int obtainGamesRootsCount = obtainGamesQueue.getPotentialJobRoots().size();
    int bookMarkGameRootsCount = bookMarkGameQueue.getPotentialJobRoots().size();
    int commentGameRootsCount = commentGameQueue.getPotentialJobRoots().size();

    Truth.ASSERT.that(obtainGamesRootsCount).isEqualTo(1);
    Truth.ASSERT.that(bookMarkGameRootsCount).isEqualTo(1);
    Truth.ASSERT.that(commentGameRootsCount).isEqualTo(1);
  }

  @Test public void queueContextTest() {
    Truth.ASSERT.that(obtainGamesQueue.getQueueContext()).isEqualTo("ObtainGames");
    Truth.ASSERT.that(bookMarkGameQueue.getQueueContext()).isEqualTo("BookmarkGame");
    Truth.ASSERT.that(commentGameQueue.getQueueContext()).isEqualTo("CommentGame");
  }

  @Test public void getJobAfterCurrentOneTest() {
    JobDataModel currentJob = obtainGamesQueue.getCurrentJob();
    JobDataModel nextJob = obtainGamesQueue.getJobAfter(currentJob);

    Truth.ASSERT.that(nextJob).isNotNull();
    Truth.ASSERT.that(nextJob.getClassName()).isEqualTo("Test2");

    nextJob = obtainGamesQueue.getJobAfter(nextJob);

    Truth.ASSERT.that(nextJob).isNotNull();
    Truth.ASSERT.that(nextJob.getClassName()).isEqualTo("Test3");
  }

  @Test public void moveToNextTest() {
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob()).isNotNull();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob().getClassName()).isEqualTo("Test");

    obtainGamesQueue.moveToNextJob();

    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob()).isNotNull();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob().getClassName()).isEqualTo("Test2");

    obtainGamesQueue.moveToNextJob();

    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob()).isNotNull();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob().getClassName()).isEqualTo("Test3");

    obtainGamesQueue.moveToNextJob();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob()).isNull();
  }

  @Test public void resetTest() {
    obtainGamesQueue.moveToNextJob();
    obtainGamesQueue.moveToNextJob();

    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob()).isNotNull();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob().getClassName()).isEqualTo("Test3");

    obtainGamesQueue.reset();
    Truth.ASSERT.that(obtainGamesQueue.getCurrentJob().getClassName()).isEqualTo("Test");
  }
}
