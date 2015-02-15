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
package com.github.jorgecastilloprz.easymvp.ui.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.jorgecastilloprz.easymvp.R;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Jorge Castillo Pérez
 */
public class GameStaggeredAdapter extends RecyclerView.Adapter<GameStaggeredAdapter.ViewHolder> implements View.OnClickListener {

    private List<Game> gameCollection;
    private OnItemClickListener onItemClickListener;

    private static final int VIEWTYPE_SHORT = 0;
    private static final int VIEWTYPE_MEDIUM = 1;
    private static final int VIEWTYPE_LONG = 2;

    /**
     * Injectable constructor allows adapter injection
     */
    @Inject public GameStaggeredAdapter() {
        gameCollection = new ArrayList<>();
    }

    /**
     * Used to add games to collection. This method is called after initial adapter injection and 
     * everytime the presenter asks the view to append new games
     * @param collectionToAdd
     */
    public void addGamesToCollection(List<Game> collectionToAdd) {
        gameCollection.addAll(collectionToAdd);
    }
    
    public void clearGames() {
        gameCollection.clear();
    }
    
    public List<Game> getCurrentGames() {
        return gameCollection;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public GameStaggeredAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (VIEWTYPE_LONG == viewType) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_grid_item_long, parent, false);
        } else if (VIEWTYPE_MEDIUM == viewType) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_grid_item_medium, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_grid_item, parent, false);
        }
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(GameStaggeredAdapter.ViewHolder holder, int position) {
        Game gameItem = gameCollection.get(position);
        holder.gameImage.setImageBitmap(null);
        Picasso.with(holder.gameImage.getContext()).load(gameItem.getImage()).into(holder.gameImage);
        holder.itemView.setTag(gameItem);
    }

    @Override
    public int getItemCount() {
        return gameCollection.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    onItemClickListener.onItemClick(v, (Game) v.getTag());
                }
            }, 200);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        @InjectView(R.id.gameItemImage)
        ImageView gameImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Game game);
    }
}
