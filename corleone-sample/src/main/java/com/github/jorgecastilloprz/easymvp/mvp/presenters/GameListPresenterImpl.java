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

import com.github.jorgecastilloprz.easymvp.domain.interactors.CheckConnectionInteractor;
import com.github.jorgecastilloprz.easymvp.domain.interactors.GetGamesByPageInteractor;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;
import com.github.jorgecastilloprz.easymvp.mvp.views.LoadingView;
import com.github.jorgecastilloprz.easymvp.ui.EasyMVPNavigator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implements obtaining games presentation logic.
 *
 * @author Jorge Castillo Pérez
 */
@Singleton
public class GameListPresenterImpl implements LifecycleCallbacks, GameListPresenter {
    
    private View view;
    private int lastPageLoaded;
    
    private CheckConnectionInteractor checkConnectionInteractor;
    private GetGamesByPageInteractor getGamesByPageInteractor;
    private EasyMVPNavigator navigator;

    @Inject GameListPresenterImpl(CheckConnectionInteractor checkConnectionInteractor, GetGamesByPageInteractor getGamesByPageInteractor, EasyMVPNavigator navigator) {
        this.checkConnectionInteractor = checkConnectionInteractor;
        this.getGamesByPageInteractor = getGamesByPageInteractor;
        this.navigator = navigator;
        this.lastPageLoaded = 0;
    }

    @Override
    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null!");
        }

        this.view = view;
    }
    
    @Override
    public void onStart() {
        view.hideFloatingButton();
        view.displayLoading();
        initGameSearch();
    }

    /**
     * Dispatchs CheckConnection interactor logic and declares callback for reacting to results
     */
    private void initGameSearch() {
        checkConnectionInteractor.execute(new CheckConnectionInteractor.Callback() {
            @Override
            public void onConnectionAvaiable() {
                getGamesForPage(lastPageLoaded+1);
            }

            @Override
            public void onConnectionError() {
                view.hideLoading();
                view.displayConnectionError();
            }
        });
    }

    /**
     * Dispatchs GetGamesByPage interactor logic and declares callback for reacting to results
     *
     * @param pageNumber to load
     */
    public void getGamesForPage(final int pageNumber) {
        getGamesByPageInteractor.execute(pageNumber, new GetGamesByPageInteractor.Callback() {

            @Override
            public void onGamePageLoaded(List<Game> games) {
                view.hideLoading();
                view.drawGames(games);
                view.attachLastGameScrollListener();
                view.displayFloatingButton();
                lastPageLoaded++;
            }

            @Override
            public void onGettingGamesError(String errorMessage) {
                view.hideLoading();
                view.displayGettingGamesError(errorMessage);
            }
        });
    }

    /**
     * We call onStart to initialize game loading logic again
     */
    @Override
    public void refreshGames() {
        view.clearGames();
        lastPageLoaded = 0;
        onStart();
    }

    @Override
    public void onLastGameViewed() {
        initGameSearch();
    }

    @Override
    public void updateViewWithSafeGames(List<Game> safeGames) {
        view.drawGames(safeGames);
    }

    @Override
    public void onGameClick(Game game, android.view.View viewToShare) {
        navigator.navigateToGameDetails(game, viewToShare);
    }

    @Override
    public void onResume() {
        //Empty
    }

    @Override
    public void onPause() {
        //Empty
    }

    /**
     * View used to decouple game list view implementation from model and vice versa
     */
    public interface View extends LoadingView {

        void displayFloatingButton();

        void hideFloatingButton();
        
        void drawGames(List<Game> games);
        
        void clearGames();
        
        void displayConnectionError();
        
        void displayGettingGamesError(String errorMessage);

        void attachLastGameScrollListener();
        
        void disableLastGameScrollListener();
    }
}
