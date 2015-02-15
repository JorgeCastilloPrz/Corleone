package com.github.jorgecastilloprz.easymvp.domain.interactors;

import android.graphics.Bitmap;

import com.github.jorgecastilloprz.easymvp.executor.Interactor;
import com.github.jorgecastilloprz.easymvp.mvp.model.InterfaceColors;

/**
 * Interactor abstraction created to allow interface color generation from a source Bitmap
 *
 * @author Jorge Castillo Pérez
 */
public interface GenerateInterfaceColorsInteractor extends Interactor {
    void execute(Bitmap sourceBitmap, Callback callback);

    interface Callback {
        void onColorsGenerated(InterfaceColors interfaceColors);
    }
}
