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
package com.github.jorgecastilloprz.easymvp.mvp.presenters;

import android.view.View;

import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

import java.util.List;

/**
 * Defined to abstract GameList view implementation to/from different possible
 * GameListPresenter implementations. 
 * *
 * @author Jorge Castillo Pérez
 */
public interface GameListPresenter {
    
    void setView(GameListPresenterImpl.View view);
    
    void onGameClick(Game game, View viewToShare);
    
    void getGamesForPage(int pageNumber);
    
    void refreshGames();
    
    void onLastGameViewed();
    
    void updateViewWithSafeGames(List<Game> safeGames);
}
