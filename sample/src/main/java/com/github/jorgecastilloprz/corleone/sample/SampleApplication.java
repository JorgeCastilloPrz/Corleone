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
package com.github.jorgecastilloprz.corleone.sample;

import android.app.Application;
import com.github.jorgecastilloprz.corleone.sample.di.ApplicationModule;
import com.github.jorgecastilloprz.corleone.sample.di.PresenterModule;
import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jorge Castillo Pérez
 */
public class SampleApplication extends Application {

  private ObjectGraph objectGraph;

  @Override public void onCreate() {
    super.onCreate();
    initDependencyInjection();
  }

  /**
   * Object graph injection shortcut for injectable classes
   *
   * @param object instance to get injected
   */
  public void inject(Object object) {
    objectGraph.inject(object);
  }

  /**
   * Used to obtain a NEW graph with aditional modules from a concrete scope.
   * Original graph does not get modified by plus method.
   *
   * @param modules to be added
   * @return new graph
   */
  public ObjectGraph buildGraphWithAditionalModules(List<Object> modules) {
    if (modules == null) {
      throw new IllegalArgumentException(
          "You can't plus a null module, review your getModules() implementation");
    }
    return objectGraph.plus(modules.toArray());
  }

  private void initDependencyInjection() {
    objectGraph = ObjectGraph.create(getApplicationModules().toArray());
    objectGraph.inject(this);
    objectGraph.injectStatics();
  }

  private List<Object> getApplicationModules() {
    List<Object> applicationModules = new ArrayList<>();
    applicationModules.add(new ApplicationModule(this));
    applicationModules.add(new PresenterModule());
    return applicationModules;
  }
}
