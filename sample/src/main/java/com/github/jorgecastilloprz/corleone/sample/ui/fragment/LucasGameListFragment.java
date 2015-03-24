package com.github.jorgecastilloprz.corleone.sample.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.InjectView;
import com.github.jorgecastilloprz.corleone.sample.R;
import com.github.jorgecastilloprz.corleone.sample.domain.model.Game;
import com.github.jorgecastilloprz.corleone.sample.domain.model.LucasArtGame;
import com.github.jorgecastilloprz.corleone.sample.domain.model.SerializableGameCollection;
import com.github.jorgecastilloprz.corleone.sample.ui.adapters.GameListAdapter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenterImpl;
import java.util.List;
import javax.inject.Inject;
import org.parceler.Parcels;

/**
 * A placeholder fragment containing a simple view.
 */
public class LucasGameListFragment extends BaseFragment
    implements GameListPresenterImpl.View, GameListAdapter.OnItemClickListener {

  private static final String LOADED_GAMES = "loaded_games";

  @Inject GameListPresenter presenter;
  @InjectView(R.id.gameListRecycler) RecyclerView gameList;

  private GameListAdapter gameAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_gamelist, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initGameList();
    presenter.setView(this);
    presenter.initialize();
  }

  private void initGameList() {
    gameList.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    gameAdapter = new GameListAdapter();
    gameAdapter.setOnItemClickListener(this);
    gameList.setAdapter(gameAdapter);
  }

  @Override public void onResume() {
    super.onResume();
    presenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    presenter.pause();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(LOADED_GAMES,
        Parcels.wrap(new SerializableGameCollection((presenter.getCurrentGamesLoaded()))));
  }

  @Override public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      final SerializableGameCollection serializableGameCollection =
          Parcels.unwrap(savedInstanceState.getParcelable(LOADED_GAMES));
      presenter.restoreLoadedGames(serializableGameCollection.getGames());
    }
  }

  @Override public void drawGames(List<Game> games) {
    gameAdapter.drawGames(games);
  }

  @Override public void onItemClick(View view, LucasArtGame game) {
    presenter.onGameClicked(game);
  }

  @Override public void displayConnectionError() {
    Toast.makeText(gameList.getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
  }

  @Override public void displayLoadGamesError() {
    Toast.makeText(gameList.getContext(), R.string.load_games_error, Toast.LENGTH_LONG).show();
  }

  @Override public void displayGamesStoredIndication() {
    Toast.makeText(gameList.getContext(), R.string.games_stored, Toast.LENGTH_LONG).show();
  }

  @Override public void displayStoreGamesError() {
    Toast.makeText(gameList.getContext(), R.string.store_games_error, Toast.LENGTH_LONG).show();
  }
}
