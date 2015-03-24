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
package com.github.jorgecastilloprz.corleone.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import com.github.jorgecastilloprz.corleone.sample.R;
import com.github.jorgecastilloprz.corleone.sample.domain.model.Game;
import java.util.List;
import org.parceler.Parcels;

/**
 * @author Jorge Castillo Pérez
 */
public class GameDetailsActivity extends BaseActivity {

  private static final String GAME_EXTRA = "game_extra";

  public static Intent getLaunchIntent(final Context context, Game game) {
    if (game == null) {
      throwIllegalArgExceptionForNullGame();
    }
    Intent intent = new Intent(context, GameDetailsActivity.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable(GAME_EXTRA, Parcels.wrap(game));
    return intent.putExtras(bundle);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    Toolbar toolbar = ButterKnife.findById(this, R.id.mainToolbar);
    setSupportActionBar(toolbar);

    Bundle extras = getIntent().getExtras();
    if (extras == null) {
      throwIllegalArgExceptionForNullGame();
    }

    Game game = Parcels.unwrap(extras.getParcelable(GAME_EXTRA));
  }

  @Override protected List<Object> getModules() {
    return null;
  }

  private static void throwIllegalArgExceptionForNullGame() {
    throw new IllegalArgumentException("Game must not be null if you want to show its details!.");
  }
}
