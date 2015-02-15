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

import android.graphics.Bitmap;

import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

/**
 * Game Details presenter abstraction to allow different implementations.
 *
 * @author Jorge Castillo Pérez
 */
public interface GameDetailsPresenter {
    
    void setView(GameDetailsPresenterImpl.View view);
    
    void setGameModel(Game game);
    
    void onLeaveButtonClick();
    
    void onFavouriteButtonClicked();
    
    void onBackgroundLoaded(Bitmap backgroundBitmap);
    
    void updateViewWithSafeGameDetails(Game game);
    
    void onToolbarHidden();
    
    void onCardViewHidden();
}
