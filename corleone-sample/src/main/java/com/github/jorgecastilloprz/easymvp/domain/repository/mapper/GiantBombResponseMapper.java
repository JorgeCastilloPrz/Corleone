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
package com.github.jorgecastilloprz.easymvp.domain.repository.mapper;

import android.text.Html;
import android.util.Log;

import com.github.jorgecastilloprz.easymvp.domain.restmodel.Image;
import com.github.jorgecastilloprz.easymvp.domain.restmodel.Platform;
import com.github.jorgecastilloprz.easymvp.domain.restmodel.Result;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to map GiantBomb rest api game responses to {@link Game model}
 *
 * Created by jorge on 24/01/15.
 */
public class GiantBombResponseMapper implements ApiResponseMapper<Result> {

    /**
     * Maps a GiantBomb game list from rest model to app model
     * @param giantBombGameList
     * @return App model game list
     */
    public List<Game> mapToGameList(List<Result> giantBombGameList) {
        
        List<Game> gameList = new ArrayList<>();
        for (Result giantBombGameResult : giantBombGameList) {
            if (isADisplayableGame(giantBombGameResult)) {
                gameList.add(mapToGame(giantBombGameResult));
            }
        }
        
        return gameList;
    }

    public Game mapToGame(Result gameResult) {

        ArrayList<String> platForms = new ArrayList<>();

        for (Platform platform : gameResult.getPlatforms()) {
            platForms.add(platform.getName());
        }

        Game game = new Game(gameResult.getId(),
                gameResult.getName(),
                getGameImageUrl(gameResult),
                Html.fromHtml(getGameDeck(gameResult)).toString(),
                Html.fromHtml(getGameDescription(gameResult)).toString(),
                gameResult.getOriginalReleaseDate(),
                platForms
        );

        return game;
    }
    
    private boolean isADisplayableGame(Result gameResult) {
        return gameResult.getImage() != null && !getGameDescription(gameResult).equals("");
    }
    
    private String getGameImageUrl(Result gameResult) {
        return gameResult.getImage().getMediumUrl();
    }
    
    private String getGameDeck(Result gameResult) {
        return (gameResult.getDeck() != null) ? gameResult.getDeck() : "No info avaiable.";
    }
    
    private String getGameDescription(Result gameResult) {
        return (gameResult.getDescription() != null) ? gameResult.getDescription() : getGameDeck(gameResult);
    }
}
