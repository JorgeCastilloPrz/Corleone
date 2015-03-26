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
package com.github.jorgecastilloprz.corleone.sample.domain.usecases;

import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Param;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.ChangeBookmarkStatusCallback;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import java.util.Random;

/**
 * Use case that mocks an action for bookmark / unbookmark a game
 *
 * @author Jorge Castillo Pérez
 */
@Job({
    @Rule(context = "ChangeBookmarkGameStatus", previousJob = CheckConnection.class)
}) public class ChangeBookmarkGameStatus {

  @Param("GameId") int gameId;
  @Param("MainThread") MainThread mainThread;
  @Param("ChangeBookmarkStatusCallback") ChangeBookmarkStatusCallback callback;

  @Execution public void execute() {
    mockInterestingTime();
    if (hasToFail()) {
      notifyBookmarkGameError();
    } else {
      notifyBookmarkStatusChanged();
    }
  }

  private void mockInterestingTime() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //Empty
    }
  }

  private boolean hasToFail() {
    Random errorRandom = new Random();
    return errorRandom.nextInt(100) >= 95;
  }

  private void notifyBookmarkGameError() {
    mainThread.post(new Runnable() {
      @Override public void run() {
        callback.onBookmarkError();
      }
    });
  }

  private void notifyBookmarkStatusChanged() {
    mainThread.post(new Runnable() {
      @Override public void run() {
        callback.onBookMarkStatusChanged();
      }
    });
  }
}
