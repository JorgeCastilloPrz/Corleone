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
package com.github.jorgecastilloprz.easymvp.mvp.views;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.jorgecastilloprz.easymvp.R;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;
import com.github.jorgecastilloprz.easymvp.mvp.presenters.GameListPresenterImpl;
import com.github.jorgecastilloprz.easymvp.ui.BaseFragment;
import com.github.jorgecastilloprz.easymvp.ui.adapters.GameStaggeredAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Holds a Game List into a {@link RecyclerView} component ported by appCompat v7 library. User can 
 * click on FAB to refresh the list. Views and dependencies are injected in BaseFragment through
 * the activity scope graph
 *
 * Created by jorge on 24/01/15.
 */
public class GameListFragment extends BaseFragment implements GameStaggeredAdapter.OnItemClickListener,
        GameListPresenterImpl.View {
    
    private static final String EXTRA_CURRENT_GAMES_LOADED = "com.github.jorgecastilloprz.easymvp.currentgamesloaded";

    @Inject GameListPresenterImpl gameListPresenter;
    @Inject GameStaggeredAdapter gameAdapter;

    @InjectView(R.id.progress_wheel) ProgressWheel progressWheel;
    @InjectView(R.id.gameRecyclerView) RecyclerView gameRecyclerView;
    @InjectView(R.id.fab) FloatingActionButton fabButton;
    
    private StaggeredGridLayoutManager layoutManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGameRecyclerView();
        initFab();
        setPresenterView();
        gameListPresenter.onStart();
    }
    
    private void initGameRecyclerView() {

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        gameRecyclerView.setLayoutManager(layoutManager);
        
        gameAdapter.setOnItemClickListener(this);
        gameRecyclerView.setAdapter(gameAdapter);
        gameRecyclerView.setHasFixedSize(true);
    }
    
    private void initFab() {
        fabButton.attachToRecyclerView(gameRecyclerView);
        fabButton.hide();
    }

    private void setPresenterView() {
        gameListPresenter.setView(this);
    }

    /**
     * Saving current games loaded to restore them if the view lifecycle is restarted
     * @param outState with current games loaded parcel
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Game> currentGames = gameAdapter.getCurrentGames();

        Parcelable currentGamesParcel = Parcels.wrap(currentGames);
        outState.putParcelable(EXTRA_CURRENT_GAMES_LOADED, currentGamesParcel);
    }

    /**
     * Trying to restore games stored when lifecycle got stopped
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable safeGamesLoadedParcel = savedInstanceState.getParcelable(EXTRA_CURRENT_GAMES_LOADED);
            List<Game> safeGamesLoaded = Parcels.unwrap(safeGamesLoadedParcel);
            gameListPresenter.updateViewWithSafeGames(safeGamesLoaded);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gameListPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameListPresenter.onPause();
    }
    
    @OnClick(R.id.fab)
    public void onFabClick() {
        gameListPresenter.refreshGames();
    }

    @Override
    public void onItemClick(View view, Game game) {
        gameListPresenter.onGameClick(game, view.findViewById(R.id.gameItemImage));
    }


    @Override
    public void displayFloatingButton() {
        fabButton.show(true);
    }

    @Override
    public void hideFloatingButton() {
        fabButton.hide(true);
    }

    @Override
    public void drawGames(List<Game> games) {
        gameAdapter.addGamesToCollection(games);
        gameAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearGames() {
        gameAdapter.clearGames();
        gameAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayConnectionError() {
        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayGettingGamesError(String errorMessage) {
        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void attachLastGameScrollListener() {
        gameRecyclerView.setOnScrollListener(new FinishScrollListener());
    }

    @Override
    public void disableLastGameScrollListener() {
        gameRecyclerView.setOnScrollListener(null);
    }

    @Override
    public void displayLoading() {
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void hideLoading() {
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
    }

    private class FinishScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            int[] itemPositions = new int[2];
            layoutManager.findLastVisibleItemPositions(itemPositions);

            int lastVisibleItemPosition = (itemPositions[1] != 0) ? ++itemPositions[1] : ++itemPositions[0];
            
            if (lastVisibleItemPosition >= gameAdapter.getItemCount()) {
                gameListPresenter.onLastGameViewed();
            }
        }
    }
}
