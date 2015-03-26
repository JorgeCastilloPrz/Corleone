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
package com.github.jorgecastilloprz.corleone.sample.di;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import com.github.jorgecastilloprz.corleone.sample.SampleApplication;
import com.github.jorgecastilloprz.corleone.sample.domain.model.GameCatalog;
import com.github.jorgecastilloprz.corleone.sample.domain.model.LucasArtCatalog;
import com.github.jorgecastilloprz.corleone.sample.ui.animator.ToolbarAnimator;
import com.github.jorgecastilloprz.corleone.sample.ui.animator.ToolbarAnimatorImpl;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThread;
import com.github.jorgecastilloprz.corleone.sample.ui.mainthread.MainThreadImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module used to inject application context generic dependencies
 *
 * @author Jorge Castillo Pérez
 */
@Module(
    injects = { SampleApplication.class },
    library = true, complete = false) public class ApplicationModule {

  private final Context appContext;

  public ApplicationModule(Context appContext) {
    this.appContext = appContext;
  }

  @Provides Context provideApplicationContext() {
    return appContext;
  }

  @Provides LayoutInflater provideLayoutInflater() {
    return LayoutInflater.from(appContext);
  }

  @Provides ConnectivityManager provideConnectivityManager() {
    return (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides MainThread provideMainThread(MainThreadImpl mainThread) {
    return mainThread;
  }

  @Provides GameCatalog provideGameCatalog(LucasArtCatalog catalog) {
    return catalog;
  }

  @Provides ToolbarAnimator provideToolbarAnimator(ToolbarAnimatorImpl animator) {
    return animator;
  }
}