package com.github.jorgecastilloprz.corleone.sample.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.github.jorgecastilloprz.corleone.sample.R;
import java.util.List;

public class MainActivity extends BaseActivity {

  private Toolbar toolbar;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = ButterKnife.findById(this, R.id.mainToolbar);
    setSupportActionBar(toolbar);
  }

  @Override protected List<Object> getModules() {
    return null;
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
