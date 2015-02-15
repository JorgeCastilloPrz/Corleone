package com.github.jorgecastilloprz.easymvp.ui.animators;

/**
 * Listener to dispatch toolbar animation finish events
 *
 * @author Jorge Castillo Pérez
 */
public interface ToolbarAnimationListener {
    
    void onToolbarTotallyDisplayed();
    
    void onToolbarTotallyHidden();
}
