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
import com.github.jorgecastilloprz.corleone.sample.domain.model.LucasArtGame;
import com.github.jorgecastilloprz.corleone.sample.ui.adapters.GameListRecyclerAdapter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenterImpl;
import java.util.List;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class LucasGameListFragment extends BaseFragment
    implements GameListPresenterImpl.View, GameListRecyclerAdapter.OnItemClickListener {

  @Inject GameListPresenter presenter;
  @InjectView(R.id.gameListRecycler) RecyclerView gameListRecycler;

  private GameListRecyclerAdapter gameAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_gamelist, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initGameRecycler();
    presenter.setView(this);
    presenter.initialize();
  }

  private void initGameRecycler() {
    gameListRecycler.setLayoutManager(
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    gameAdapter = new GameListRecyclerAdapter();
    gameAdapter.setOnItemClickListener(this);
    gameListRecycler.setAdapter(gameAdapter);
  }

  @Override public void drawGames(List<LucasArtGame> games) {
    gameAdapter.drawGames(games);
  }

  @Override public void onItemClick(View view, LucasArtGame game) {
    presenter.onGameClicked(game);
  }

  @Override public void displayConnectionError() {
    Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG);
  }
}
