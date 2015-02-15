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
package com.github.jorgecastilloprz.easymvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.github.jorgecastilloprz.easymvp.di.qualifiers.ActivityContext;
import com.github.jorgecastilloprz.easymvp.mvp.model.Game;
import com.github.jorgecastilloprz.easymvp.mvp.views.DetailsActivity;

import org.parceler.Parcels;

import javax.inject.Inject;

/**
 * Entity used to implement navigation between activities and fragments
 * * * 
 * Created by jorge on 17/01/15.
 */
public class EasyMVPNavigator implements Navigator {
    
    private final Context activityContext;
    
    @Inject
    public EasyMVPNavigator(@ActivityContext Context activityContext) {
        this.activityContext = activityContext;
    }

    /**
     * Navigates to game details activity with some arguments into the bundle and applying interesting 
     * transition animations with a shared view
     * @param game to display details about
     * @param viewToshare for the lollipop animated transition
     */
    public void navigateToGameDetails(Game game, View viewToshare) {
        
        Intent detailsActivityIntent = new Intent(activityContext, DetailsActivity.class);
        detailsActivityIntent.putExtra(DetailsActivity.GAME_EXTRA, Parcels.wrap(game));
        
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) activityContext, viewToshare, "sharedImage");

        ActivityCompat.startActivity((Activity) activityContext, detailsActivityIntent, options.toBundle());
    }
}
