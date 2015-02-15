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
package com.github.jorgecastilloprz.easymvp.domain.repository;

import com.github.jorgecastilloprz.easymvp.domain.repository.exceptions.ObtainGamesException;
import com.github.jorgecastilloprz.easymvp.domain.repository.mapper.ApiResponseMapper;
import com.github.jorgecastilloprz.easymvp.domain.restmodel.GiantBombResponse;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by jorge on 18/01/15.
 */
public class GiantBombRestGameRepository implements GameRepository {

    private final String JSON_FORMAT = "json";
    private RetrofitGiantBombService retrofitGiantBombService;

    private String endPoint;
    private String apiKey;
    private ApiResponseMapper responseMapper;
    
    @Inject
    public GiantBombRestGameRepository(@Named("api_base_url") String apiBaseUrl, @Named("api_key") String apiKey, ApiResponseMapper responseMapper) {
        endPoint = apiBaseUrl;
        this.apiKey = apiKey;
        this.responseMapper = responseMapper;
        init();
    }
    
    private void init() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .build();

        retrofitGiantBombService = restAdapter.create(RetrofitGiantBombService.class);
    }
    
    @Override
    public List<Game> obtainGamesByPage(int pageNumber) throws ObtainGamesException {
        
        try {
            GiantBombResponse obtainedGames = retrofitGiantBombService.obtainGamesByPage(apiKey, JSON_FORMAT, (pageNumber-1) * 20, 20);
            return responseMapper.mapToGameList(obtainedGames.getResults());
        }
        catch (RetrofitError retrofitError) {
            ObtainGamesException obtainGamesException = new ObtainGamesException(retrofitError.getMessage());
            obtainGamesException.setStackTrace(retrofitError.getStackTrace());
            throw obtainGamesException;
        }
    }
}
