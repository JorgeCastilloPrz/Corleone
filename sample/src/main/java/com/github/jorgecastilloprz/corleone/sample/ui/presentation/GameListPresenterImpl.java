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
package com.github.jorgecastilloprz.corleone.sample.ui.presentation;

import android.net.ConnectivityManager;
import com.github.jorgecastilloprz.corleone.Corleone;
import com.github.jorgecastilloprz.corleone.JobParams;
import com.github.jorgecastilloprz.corleone.sample.domain.model.Game;
import com.github.jorgecastilloprz.corleone.sample.domain.model.GameCatalog;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.CheckConnectionCallback;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.GetGamesFromDataSourceCallback;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.StoreGamesInDatabaseCallback;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.contexts.CorleoneContexts;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import com.github.jorgecastilloprz.corleone.sample.ui.navigation.Navigator;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class GameListPresenterImpl implements GameListPresenter {

  private View view;
  private ConnectivityManager connectivityManager;
  private MainThread mainThread;
  private GameCatalog gameCatalog;
  private List<Game> currentGamesLoaded;
  private Navigator navigator;

  @Inject GameListPresenterImpl(ConnectivityManager connectivityManager, MainThread mainThread,
      GameCatalog gameCatalog, Navigator navigator) {
    this.connectivityManager = connectivityManager;
    this.mainThread = mainThread;
    this.gameCatalog = gameCatalog;
    this.navigator = navigator;
  }

  @Override public void setView(View view) {
    if (view == null) {
      throw new IllegalArgumentException("The view must not be null!");
    }
    this.view = view;
  }

  @Override public void initialize() {
    dispatchObtainGamesTasks();
  }

  @Override public void resume() {
  }

  @Override public void pause() {
  }

  /**
   * All tasks for ObtainGames context will be dispatched in order
   */
  private void dispatchObtainGamesTasks() {
    JobParams params = new JobParams().append("ConnectivityManager", connectivityManager)
        .append("MainThread", mainThread)
        .append("CheckConnectionCallback", getCheckConnectionCallback())
        .append("GameCatalog", gameCatalog)
        .append("GetGamesFromDataSourceCallback", getGamesFromDataSourceCallback())
        .append("StoreGamesInDatabaseCallback", getStoreGamesInDatabaseCallback());

    Corleone.context(CorleoneContexts.OBTAIN_GAMES).dispatchJobs(params);
  }

  private CheckConnectionCallback getCheckConnectionCallback() {
    return new CheckConnectionCallback() {
      @Override public void onConnectionStatusChecked(boolean connectionAvailable) {
        if (!connectionAvailable) {
          view.displayConnectionError();
        }
      }
    };
  }

  private GetGamesFromDataSourceCallback getGamesFromDataSourceCallback() {
    return new GetGamesFromDataSourceCallback() {
      @Override public void onGamesLoaded(List<Game> gamesLoaded) {
        view.drawGames(gamesLoaded);
        currentGamesLoaded = gamesLoaded;
      }

      @Override public void onLoadGamesError() {
        view.displayLoadGamesError();
      }
    };
  }

  private StoreGamesInDatabaseCallback getStoreGamesInDatabaseCallback() {
    return new StoreGamesInDatabaseCallback() {
      @Override public void onGamesStored() {
        view.displayGamesStoredIndication();
      }

      @Override public void onStoreGamesError() {
        view.displayStoreGamesError();
      }
    };
  }

  @Override public void onGameClicked(Game game) {
    navigator.displayGameDetails(game);
  }

  @Override public List<Game> getCurrentGamesLoaded() {
    return currentGamesLoaded;
  }

  @Override public void restoreLoadedGames(List<Game> games) {
    currentGamesLoaded = games;
    view.drawGames(games);
  }
}
