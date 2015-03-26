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
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.ChangeBookmarkStatusCallback;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.CheckConnectionCallback;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.contexts.CorleoneContexts;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class GameDetailsPresenterImpl implements GameDetailsPresenter {

  private View view;
  private ConnectivityManager connectivityManager;
  private MainThread mainThread;
  private Game gameModel;

  @Inject GameDetailsPresenterImpl(ConnectivityManager connectivityManager, MainThread mainThread) {
    this.connectivityManager = connectivityManager;
    this.mainThread = mainThread;
  }

  @Override public void setView(View view) {
    if (view == null) {
      throw new IllegalArgumentException("The view must not be null!");
    }
    this.view = view;
  }

  @Override public void setGameModel(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("Game model must not be null!");
    }
    this.gameModel = game;
  }

  @Override public void initialize() {
    view.setHeaderImage(gameModel.getImageUrl());
    view.setTitle(gameModel.getName());
    view.setDateAndAuthor(gameModel.getReleaseDate(), gameModel.getAuthor());
    view.setDescription(gameModel.getDescription());
  }

  @Override public void onUpNavigationClick() {
    view.closeDetails();
  }

  @Override public void onBookmarkButtonCick() {
    JobParams params = new JobParams().append("ConnectivityManager", connectivityManager)
        .append("MainThread", mainThread)
        .append("CheckConnectionCallback", getCheckConnectionCallback())
        .append("GameId", gameModel.getId())
        .append("ChangeBookmarkStatusCallback", getChangeBookmarkStatusCallback());

    Corleone.context(CorleoneContexts.BOOKMARK_GAME).dispatchJobs(params);
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

  private ChangeBookmarkStatusCallback getChangeBookmarkStatusCallback() {
    return new ChangeBookmarkStatusCallback() {
      @Override public void onBookMarkStatusChanged() {
        if (gameModel.isBookmarked()) {
          view.unmarkGameAsFavourite();
          gameModel.setBookmarked(false);
        } else {
          view.markGameAsFavourite();
          gameModel.setBookmarked(true);
        }
      }

      @Override public void onBookmarkError() {
        view.displayChangeBookmarkStatusError();
      }
    };
  }

  @Override public void resume() {

  }

  @Override public void pause() {

  }
}
