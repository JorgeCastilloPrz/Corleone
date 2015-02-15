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
import android.os.Build;

import com.github.jorgecastilloprz.easymvp.domain.interactors.GenerateInterfaceColorsInteractor;
import com.github.jorgecastilloprz.easymvp.domain.interactors.MarkGameAsFavouriteInteractor;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;
import com.github.jorgecastilloprz.easymvp.mvp.model.InterfaceColors;

import javax.inject.Inject;

/**
 * Controls game details presentation logic. The view just needs to do the drawing.
 *
 * @author Jorge Castillo Pérez
 */
public class GameDetailsPresenterImpl implements LifecycleCallbacks, GameDetailsPresenter {

    private View view;
    private Game gameModel;
    private GenerateInterfaceColorsInteractor generateInterfaceColorsInteractor;
    private MarkGameAsFavouriteInteractor markGameAsFavouriteInteractor;
    
    @Inject
    public GameDetailsPresenterImpl(GenerateInterfaceColorsInteractor generateInterfaceColorsInteractor, 
                                    MarkGameAsFavouriteInteractor markGameAsFavouriteInteractor) {
        
        this.generateInterfaceColorsInteractor = generateInterfaceColorsInteractor;
        this.markGameAsFavouriteInteractor = markGameAsFavouriteInteractor;
    }
    
    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null!");
        }
        this.view = view;
    }
    
    public void setGameModel(Game gameModel) {
        if (gameModel == null) {
            throw new IllegalArgumentException("Game model must not be null!");
        }
        this.gameModel = gameModel;
    }
    
    public Game getGameModel() {
        return gameModel;
    }

    @Override
    public void onStart() {
        view.loadBackgroundImage(gameModel.getImage());
        view.setTitle(gameModel.getName());
        view.setDescription(gameModel.getDescription());
        view.hideInstantToolbar();
        view.showDelayedToolbarAnimation();
        view.animateCardFadeIn();
    }

    @Override
    public void onBackgroundLoaded(Bitmap backgroundBitmap) {
        generateInterfaceColorsInteractor.execute(backgroundBitmap, new GenerateInterfaceColorsInteractor.Callback() {
            @Override
            public void onColorsGenerated(InterfaceColors interfaceColors) {
                view.setToolbarColor(interfaceColors.getToolbarColor());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setStatusBarColor(interfaceColors.getStatusBarColor());
                }
                
                view.setFloatingButtonNormalColor(interfaceColors.getFabNormalColor());
                view.setFloatingButtonPressedColor(interfaceColors.getFabPressedColor());
                view.setFloatingButtonRippleColor(interfaceColors.getFabRippleColor());
            }
        });
    }

    @Override
    public void updateViewWithSafeGameDetails(Game game) {
        gameModel = game;
        onStart();
    }

    @Override
    public void onToolbarHidden() {
        view.animateCardFadeOut();
    }

    @Override
    public void onCardViewHidden() {
        view.getBackToMainScreen();
    }

    @Override
    public void onFavouriteButtonClicked() {
        markGameAsFavouriteInteractor.execute(gameModel.getId(), new MarkGameAsFavouriteInteractor.Callback() {
            @Override
            public void onFavouriteSuccess() {
                view.markGameAsFavourite();
            }

            @Override
            public void onMarkAsFavouriteError(String errorMessage) {
                view.displayMarkAsFavouriteError(errorMessage);
            }
        });
    }

    @Override
    public void onLeaveButtonClick() {
        view.hideSmoothToolbar();
    }

    @Override
    public void onResume() {
        /*Empty*/
    }

    @Override
    public void onPause() {
        /*Empty*/
    }

    public interface View {
        
        void hideInstantToolbar();
        
        void showDelayedToolbarAnimation();
        
        void hideSmoothToolbar();
        
        void animateCardFadeIn();
        
        void animateCardFadeOut();
        
        void getBackToMainScreen();
        
        void loadBackgroundImage(String imageUrl);
        
        void setToolbarColor(int color);
        
        void setStatusBarColor(int color);
        
        void setFloatingButtonNormalColor(int color);
        
        void setFloatingButtonPressedColor(int color);

        void setFloatingButtonRippleColor(int color);
        
        void setTitle(String title);
        
        void setDescription(String description);
        
        void markGameAsFavourite();
        
        void displayMarkAsFavouriteError(String errorMessage);
    }
}
