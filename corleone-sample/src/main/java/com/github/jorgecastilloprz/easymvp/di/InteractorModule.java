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

import com.github.jorgecastilloprz.easymvp.domain.interactors.CheckConnectionInteractor;
import com.github.jorgecastilloprz.easymvp.domain.interactors.CheckConnectionInteractorImpl;
import com.github.jorgecastilloprz.easymvp.domain.interactors.GenerateInterfaceColorsInteractor;
import com.github.jorgecastilloprz.easymvp.domain.interactors.GenerateInterfaceColorsInteractorImpl;
import com.github.jorgecastilloprz.easymvp.domain.interactors.GetGamesByPageInteractor;
import com.github.jorgecastilloprz.easymvp.domain.interactors.GetGamesByPageInteractorImpl;
import com.github.jorgecastilloprz.easymvp.domain.interactors.MarkGameAsFavouriteInteractorImpl;
import com.github.jorgecastilloprz.easymvp.domain.interactors.MarkGameAsFavouriteInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Interactor injection module
 *
 * @author Jorge Castillo Pérez
 */
@Module(
        library = true, complete = false
)
public class InteractorModule {
    
    @Provides
    GetGamesByPageInteractor provideGetGamesByPageInteractor(GetGamesByPageInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    CheckConnectionInteractor provideCheckConnectionInteractor(CheckConnectionInteractorImpl interactor) {
        return interactor;
    }
    
    @Provides
    GenerateInterfaceColorsInteractor provideGenerateInterfaceColorsInteractor(GenerateInterfaceColorsInteractorImpl interactor) {
        return interactor;
    }
    
    @Provides
    MarkGameAsFavouriteInteractor provideMarkGameAsFavouriteInteractor(MarkGameAsFavouriteInteractorImpl interactor) {
        return interactor;
    }
}
