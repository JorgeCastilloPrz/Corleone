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

/**
 * Interface colors model returned by GetInterfaceColors interactor.
 *
 * @author Jorge Castillo Pérez
 */
public class InterfaceColors {

    private int toolbarColor;
    private int statusBarColor;
    private int fabNormalColor;
    private int fabPressedColor;
    private int fabRippleColor;

    public InterfaceColors(int toolbarColor, int statusBarColor, int fabNormalColor, int fabPressedColor, int fabRippleColor) {
        this.toolbarColor = toolbarColor;
        this.statusBarColor = statusBarColor;
        this.fabNormalColor = fabNormalColor;
        this.fabPressedColor = fabPressedColor;
        this.fabRippleColor = fabRippleColor;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getFabPressedColor() {
        return fabPressedColor;
    }

    public int getFabNormalColor() {
        return fabNormalColor;
    }

    public int getFabRippleColor() {
        return fabRippleColor;
    }
}
