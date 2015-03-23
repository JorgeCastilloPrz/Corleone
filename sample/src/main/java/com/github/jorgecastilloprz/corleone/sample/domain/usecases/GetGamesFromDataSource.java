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

import com.github.jorgecastilloprz.corleone.Corleone;
import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Param;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import com.github.jorgecastilloprz.corleone.sample.domain.model.Game;
import com.github.jorgecastilloprz.corleone.sample.domain.model.GameCatalog;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.GetGamesFromDataSourceCallback;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import java.util.List;
import java.util.Random;

/**
 * Use case to obtain games from mocked datasource {@link GameCatalog} and fakes some kind of
 * repository query.
 *
 * @author Jorge Castillo Pérez
 */
@Job({
    @Rule(context = "ObtainGames", previousJob = CheckConnection.class)
}) public class GetGamesFromDataSource {

  @Param("GameCatalog") GameCatalog gameCatalog;
  @Param("MainThread") MainThread mainThread;
  @Param("GetGamesFromDataSourceCallback") GetGamesFromDataSourceCallback callback;

  @Execution public void execute() {
    mockLoadingTime();
    if (hasToFail()) {
      notifyLoadingGamesError();
    } else {
      List<Game> games = gameCatalog.getGames();
      notifyGamesLoaded(games);
    }
  }

  private void mockLoadingTime() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      //Empty
    }
  }

  private boolean hasToFail() {
    Random errorRandom = new Random();
    return errorRandom.nextInt(10) >= 9;
  }

  private void notifyLoadingGamesError() {
    mainThread.post(new Runnable() {
      @Override public void run() {
        callback.onLoadGamesError();
      }
    });
  }

  private void notifyGamesLoaded(final List<Game> gamesLoaded) {
    mainThread.post(new Runnable() {
      @Override public void run() {
        callback.onGamesLoaded(gamesLoaded);
      }
    });
    Corleone.allContexts(this).provideParam("GamesLoaded", gamesLoaded);
    Corleone.allContexts(this).keepGoing();
  }
}
