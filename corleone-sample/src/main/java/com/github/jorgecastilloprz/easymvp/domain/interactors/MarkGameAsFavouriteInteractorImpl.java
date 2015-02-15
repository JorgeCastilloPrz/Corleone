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

import com.github.jorgecastilloprz.easymvp.executor.InteractorExecutor;
import com.github.jorgecastilloprz.easymvp.executor.MainThread;

import java.util.Random;

import javax.inject.Inject;

/**
 * Interactor used to simulate an internet query to mark a Game model as favourite for the current 
 * user.
 *  
 * @author Jorge Castillo Pérez
 */
public class MarkGameAsFavouriteInteractorImpl implements MarkGameAsFavouriteInteractor {
    
    private InteractorExecutor executor;
    private MainThread mainThread;
    
    private int gameId;
    private Callback callback;
    
    @Inject
    MarkGameAsFavouriteInteractorImpl(InteractorExecutor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
    
    @Override
    public void execute(int gameId, Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null!");
        }
        this.gameId = gameId;
        this.callback = callback;
        this.executor.run(this);
    }

    
    
    /**
     * Method used to simulate an internet http query to mark the game 
     * as favourite
     */
    private void waitDelayTime() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            //Empty
        }
    }

    @Override
    public void run() {
        waitDelayTime();
        if (haveToShowError()) {
            notifyError();
        } else {
            onFavouriteSuccess();
        }
    }

    private boolean haveToShowError() {
        Random random = new Random();
        return random.nextInt(10) < 3;
    }

    private void notifyError() {
        mainThread.post(new Runnable() {
            @Override public void run() {
                callback.onMarkAsFavouriteError("There was a problem when trying to mark the game as favourite. Please try again.");
            }
        });
    }

    private void onFavouriteSuccess() {
        mainThread.post(new Runnable() {
            @Override public void run() {
                callback.onFavouriteSuccess();
            }
        });
    }
}
