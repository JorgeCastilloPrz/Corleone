package com.github.jorgecastilloprz.easymvp.domain.interactors;

import com.github.jorgecastilloprz.easymvp.executor.Interactor;

/**
 * @author Jorge Castillo Pérez
 */
public interface MarkGameAsFavouriteInteractor extends Interactor {
    void execute(int gameId, Callback callback);

    interface Callback {

        void onFavouriteSuccess();

        void onMarkAsFavouriteError(String errorMessage);
    }
}
