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
package com.github.jorgecastilloprz.easymvp.mvp.model;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Application game model. Attributes should not be private because of Parceller dependency (uses java
 * reflection API).
 *
 * @author Jorge Castillo Pérez
 */
@Parcel
public class Game {

    int id;
    String name;
    String image;
    String summary;
    String description;
    String releaseDate;
    ArrayList<String> platforms;

    public Game() {
        /*Required empty bean constructor*/
    }

    public Game(int id, String name, String image, String summary, String description, String releaseDate, ArrayList<String> platforms) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.summary = summary;
        this.description = description;
        this.releaseDate = releaseDate;
        this.platforms = platforms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = platforms;
    }
}
