package com.github.jorgecastilloprz.corleone.sample.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.jorgecastilloprz.corleone.sample.R;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenter;
import com.github.jorgecastilloprz.corleone.sample.ui.presentation.GameListPresenterImpl;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class LucasGameListFragment extends BaseFragment implements GameListPresenterImpl.View {

  @Inject GameListPresenter presenter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_gamelist, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.setView(this);
    presenter.initialize();
  }
}
