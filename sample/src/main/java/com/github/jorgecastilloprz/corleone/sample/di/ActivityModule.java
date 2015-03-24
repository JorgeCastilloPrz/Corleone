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

import android.app.Activity;
import android.content.Context;
import com.github.jorgecastilloprz.corleone.sample.di.qualifiers.ActivityContext;
import com.github.jorgecastilloprz.corleone.sample.ui.activity.BaseActivity;
import com.github.jorgecastilloprz.corleone.sample.ui.activity.GameDetailsActivity;
import com.github.jorgecastilloprz.corleone.sample.ui.activity.MainActivity;
import com.github.jorgecastilloprz.corleone.sample.ui.fragment.LucasGameListFragment;
import dagger.Module;
import dagger.Provides;

/**
 * Dependency injection module used to provide activity scope context and satisfy activity/fragment
 * dependency needs
 *
 * @author Jorge Castillo Pérez
 */
@Module(
    injects = {
        MainActivity.class, LucasGameListFragment.class, GameDetailsActivity.class,
        BaseActivity.class
    },
    library = true, complete = false) public class ActivityModule {

  private final Activity activityContext;

  public ActivityModule(Activity activityContext) {
    this.activityContext = activityContext;
  }

  @Provides @ActivityContext Context provideActivityContext() {
    return activityContext;
  }
}
