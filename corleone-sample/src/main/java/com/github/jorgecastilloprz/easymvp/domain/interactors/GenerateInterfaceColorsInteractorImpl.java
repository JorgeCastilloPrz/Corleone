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
package com.github.jorgecastilloprz.easymvp.domain.interactors;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.github.jorgecastilloprz.easymvp.R;
import com.github.jorgecastilloprz.easymvp.executor.InteractorExecutor;
import com.github.jorgecastilloprz.easymvp.executor.MainThread;
import com.github.jorgecastilloprz.easymvp.mvp.model.InterfaceColors;

import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class GenerateInterfaceColorsInteractorImpl implements GenerateInterfaceColorsInteractor {

    private Context appContext;
    private InteractorExecutor executor;
    private MainThread mainThread;
    
    private Callback callback;
    private Bitmap sourceBitmap;

    @Inject
    GenerateInterfaceColorsInteractorImpl(Context appContext, InteractorExecutor executor, MainThread mainThread) {
        this.appContext = appContext;
        this.executor = executor;
        this.mainThread = mainThread;
    }
    
    @Override
    public void execute(Bitmap sourceBitmap, Callback callback) {
        
        if (sourceBitmap == null) {
            throw new IllegalArgumentException("Bitmap must not be null! It is needed as a source to generate the colors.");
        }
        
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null or response would not be able to be notified.");
        }
        
        this.callback = callback;
        this.sourceBitmap = sourceBitmap;
        executor.run(this);
    }

    @Override
    public void run() {
        Palette.generateAsync(sourceBitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                
                int primary = appContext.getResources().getColor(R.color.fab_primary);
                int primaryDark = appContext.getResources().getColor(R.color.fab_primary_pressed);
                int primaryRipple = appContext.getResources().getColor(R.color.fab_ripple);
                
                InterfaceColors interfaceColors = new InterfaceColors(
                        palette.getVibrantColor(primary),
                        palette.getVibrantColor(primaryDark),
                        palette.getVibrantColor(primary),
                        palette.getDarkVibrantColor(primaryDark),
                        palette.getLightVibrantColor(primaryRipple));
                
                notifyColorsGenerated(interfaceColors);
            }
        });
    }

    private void notifyColorsGenerated(final InterfaceColors colors) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onColorsGenerated(colors);
            }
        });
    }
}
