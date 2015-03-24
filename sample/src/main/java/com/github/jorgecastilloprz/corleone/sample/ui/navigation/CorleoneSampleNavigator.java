/*
 * Copyright (C) 2015 Jorge Castillo Pérez
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
package com.github.jorgecastilloprz.corleone.sample.ui.navigation;

import android.content.Context;
import android.content.Intent;
import com.github.jorgecastilloprz.corleone.sample.di.qualifiers.ActivityContext;
import com.github.jorgecastilloprz.corleone.sample.domain.model.Game;
import com.github.jorgecastilloprz.corleone.sample.ui.activity.GameDetailsActivity;
import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class CorleoneSampleNavigator implements Navigator {

  private Context context;

  @Inject CorleoneSampleNavigator(@ActivityContext Context context) {
    this.context = context;
  }

  @Override public void displayGameDetails(Game game) {
    Intent displayGameDetailsIntent = GameDetailsActivity.getLaunchIntent(context, game);
    context.startActivity(displayGameDetailsIntent);
  }
}
