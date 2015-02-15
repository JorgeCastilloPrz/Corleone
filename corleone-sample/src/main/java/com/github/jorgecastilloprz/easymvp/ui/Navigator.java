package com.github.jorgecastilloprz.easymvp.ui;

import android.view.View;

import com.github.jorgecastilloprz.easymvp.mvp.model.Game;

/**
 * Decouples Navigator low level implementations to/from low level presenter implementations
 *
 * @author Jorge Castillo Pérez
 */
public interface Navigator {
    void navigateToGameDetails(Game game, View viewToShare);
}
