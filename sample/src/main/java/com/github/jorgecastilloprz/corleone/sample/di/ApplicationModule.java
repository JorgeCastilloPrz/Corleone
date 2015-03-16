/*
 * Copyright (C) 2014 Jorge Castillo Pérez
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
import android.view.LayoutInflater;
import com.github.jorgecastilloprz.corleone.sample.CorleoneSampleApplication;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module used to inject application context generic dependencies
 *
 * @author Jorge Castillo Pérez
 */
@Module(
    injects = { CorleoneSampleApplication.class },
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
}