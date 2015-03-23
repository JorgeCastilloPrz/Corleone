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
package com.github.jorgecastilloprz.corleone.sample.domain.model;

import java.util.List;
import org.parceler.Parcel;

/**
 * @author Jorge Castillo Pérez
 */
@Parcel public class SerializableGameCollection {
  List<Game> games;

  public SerializableGameCollection() {
    /* Needed for parceler */
  }

  public SerializableGameCollection(List<Game> games) {
    this.games = games;
  }

  public List<Game> getGames() {
    return games;
  }
}
