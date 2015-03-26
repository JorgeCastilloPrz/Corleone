package com.github.jorgecastilloprz.corleone.sample.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.InjectView;
import com.github.jorgecastilloprz.corleone.sample.R;
import com.github.jorgecastilloprz.corleone.sample.di.NavigationModule;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

  @InjectView(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    injectViews();
    setSupportActionBar(toolbar);
  }

  @Override protected List<Object> getModules() {
    List<Object> modulesToAppend = new ArrayList<>();
    modulesToAppend.add(NavigationModule.class);
    return modulesToAppend;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
