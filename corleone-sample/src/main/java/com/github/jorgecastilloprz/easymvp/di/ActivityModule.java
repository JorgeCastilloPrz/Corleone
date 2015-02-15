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
package com.github.jorgecastilloprz.easymvp.di;

import android.app.Activity;
import android.content.Context;

import com.github.jorgecastilloprz.easymvp.di.qualifiers.ActivityContext;
import com.github.jorgecastilloprz.easymvp.ui.EasyMVPNavigator;
import com.github.jorgecastilloprz.easymvp.ui.Navigator;
import com.github.jorgecastilloprz.easymvp.ui.adapters.GameStaggeredAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency injection module used to provide activity scope context and satisfy activity/fragment
 * dependency needs
 *
 * @author Jorge Castillo Pérez
 */

@Module(
        includes = {ActivityGraphInjectModule.class, PresenterModule.class},
        library = true, complete = false
)
public class ActivityModule {

    private final Activity activityContext;

    public ActivityModule(Activity activityContext) {
        this.activityContext = activityContext;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return activityContext;
    }
    
    @Provides
    GameStaggeredAdapter provideGameAdapter() {
        return new GameStaggeredAdapter();
    }
    
    @Provides
    Navigator provideNavigator(EasyMVPNavigator navigator) {
        return navigator;
    }
}
