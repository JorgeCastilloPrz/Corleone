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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.github.jorgecastilloprz.corleone.Corleone;
import com.github.jorgecastilloprz.corleone.annotations.Execution;
import com.github.jorgecastilloprz.corleone.annotations.Job;
import com.github.jorgecastilloprz.corleone.annotations.Param;
import com.github.jorgecastilloprz.corleone.annotations.Rule;
import com.github.jorgecastilloprz.corleone.sample.domain.usecases.callbacks.CheckConnectionCallback;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;

/**
 * @author Jorge Castillo Pérez
 */
@Job({
    @Rule(context = "ObtainGames"), @Rule(context = "BookmarkGame"), @Rule(context = "CommentGame")
}) public class CheckConnection {

  @Param("ConnectivityManager") ConnectivityManager connectivityManager;
  @Param("MainThread") MainThread mainThread;
  @Param("CheckConnectionCallback") CheckConnectionCallback callback;

  @Execution public void execute() {
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    notifyNetworkStatus(isConnected);
  }

  private void notifyNetworkStatus(final boolean networkAvailable) {
    mainThread.post(new Runnable() {
      @Override public void run() {
        callback.onConnectionStatusChecked(networkAvailable);
      }
    });
    if (networkAvailable) {
      //We let all the chains continue if connection is available
      Corleone.allContexts(this).keepGoing();
    }
  }
}
