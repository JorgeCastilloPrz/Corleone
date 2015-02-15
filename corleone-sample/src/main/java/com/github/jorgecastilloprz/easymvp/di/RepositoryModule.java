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

import com.github.jorgecastilloprz.easymvp.domain.repository.GameRepository;
import com.github.jorgecastilloprz.easymvp.domain.repository.GiantBombRestGameRepository;
import com.github.jorgecastilloprz.easymvp.domain.repository.MockGameRepository;
import com.github.jorgecastilloprz.easymvp.domain.repository.mapper.ApiResponseMapper;
import com.github.jorgecastilloprz.easymvp.domain.repository.mapper.GiantBombResponseMapper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for repository injects
 * * *
 * Created by jorge on 18/01/15.
 */

@Module(
        complete = false, library = true)
public class RepositoryModule {

    private final String apiKey = "07dce392be760422f40d67bc7945d7f1aaac7f4e";
    private final String apiBaseUrl = "http://www.giantbomb.com/api";

    @Provides
    @Named("api_key")
    public String provideApiKey() {
        return apiKey;
    }

    @Provides
    @Named("api_base_url")
    public String provideApiBaseUrl() {
        return apiBaseUrl;
    }
    
    @Provides
    @Singleton
    public ApiResponseMapper provideApiResponseMapper() {
        return new GiantBombResponseMapper();
    } 

    @Provides
    public GameRepository provideGameRepository(@Named("api_base_url") String apiBaseUrl, @Named("api_key") String apiKey, ApiResponseMapper responseMapper) {
        return new GiantBombRestGameRepository(apiBaseUrl, apiKey, responseMapper);
    }

    @Provides
    @Named("mock_api")
    public GameRepository provideMockGameRepository() {
        return new MockGameRepository();
    }
}
