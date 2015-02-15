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
package com.github.jorgecastilloprz.easymvp.ui.animators;

import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import javax.inject.Inject;

/**
 * @author Jorge Castillo Pérez
 */
public class CardViewAnimatorImpl implements CardViewAnimator {

    private final int CARD_ANIMATION_DURATION = 300;
    
    private CardViewAnimationListener animationListener;
    
    @Inject
    CardViewAnimatorImpl() {}

    @Override
    public void attachCardViewAnimationListener(CardViewAnimationListener listener) {
        this.animationListener = listener;
    }
    
    @Override
    public void animateCardIn(final View cardView) {
        cardView.postDelayed(new Runnable() {
            @Override
            public void run() {

                ValueAnimator cardFadeInAnimation = ObjectAnimator.ofFloat(cardView, "alpha", 0, 1, 1);
                cardFadeInAnimation
                        .setDuration(CARD_ANIMATION_DURATION)
                        .setInterpolator(new AccelerateInterpolator());

                cardFadeInAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animationListener != null) {
                            animationListener.onCardInAnimationFinished();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });

                cardFadeInAnimation.start();
            }
        }, 500);
    }

    @Override
    public void animateCardOut(View cardView) {
        ValueAnimator cardFadeInAnimation = ObjectAnimator.ofFloat(cardView, "alpha", 1, 0, 0);
        cardFadeInAnimation
                .setDuration(CARD_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator());

        cardFadeInAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onCardOutAnimationFinished();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        
        cardFadeInAnimation.start();
    }
}
