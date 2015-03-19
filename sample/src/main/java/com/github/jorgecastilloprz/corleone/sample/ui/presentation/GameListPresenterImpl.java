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
import com.github.jorgecastilloprz.corleone.sample.domain.model.LucasArtGame;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.CorleoneContexts;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.CheckConnectionCallback;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class GameListPresenterImpl implements GameListPresenter {

  private View view;
  private ConnectivityManager connectivityManager;
  private MainThread mainThread;

  @Inject GameListPresenterImpl(ConnectivityManager connectivityManager, MainThread mainThread) {
    this.connectivityManager = connectivityManager;
    this.mainThread = mainThread;
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
    JobParams params = new JobParams()
        .append("ConnectivityManager", connectivityManager)
        .append("MainThread", mainThread)
        .append("CheckConnectionCallback", getCheckConnectionCallback());

    Corleone.context(CorleoneContexts.OBTAIN_GAMES).dispatchJobs(params);
  }

  private Object getCheckConnectionCallback() {
    return new CheckConnectionCallback() {
      @Override public void onConnectionStatusChecked(boolean connectionAvailable) {

      }
    };
  }

  @Override public void onGameClicked(LucasArtGame game) {

  }
}
