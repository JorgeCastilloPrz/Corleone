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

import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameDetailsPresenter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameDetailsPresenterImpl;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * @author Jorge Castillo Pérez
 */
@Module(
    injects = { GameListPresenterImpl.class, GameDetailsPresenterImpl.class },
    library = true, complete = false) public class PresenterModule {

  @Provides GameListPresenter provideGameListPresenter(GameListPresenterImpl presenter) {
    return presenter;
  }

  @Provides GameDetailsPresenter provideGameDetailsPresenter(GameDetailsPresenterImpl presenter) {
    return presenter;
  }
}
