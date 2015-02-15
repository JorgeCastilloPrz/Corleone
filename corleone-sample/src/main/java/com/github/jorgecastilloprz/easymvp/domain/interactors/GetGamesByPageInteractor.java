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
package com.github.jorgecastilloprz.easymvp.domain.interactors;

import com.github.jorgecastilloprz.easymvp.executor.Interactor;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

import java.util.List;

/**
 * This behaviour defines a get games by page generic logic. Interactors are executed by 
 * InteractorExecutor.
 *
 * @author Jorge Castillo Pérez
 */
public interface GetGamesByPageInteractor extends Interactor {
    
    void execute(int pageNumber, Callback callback);
    
    interface Callback {
        
        void onGamePageLoaded(List<Game> games);
        
        void onGettingGamesError(String errorMessage);
    }
}
