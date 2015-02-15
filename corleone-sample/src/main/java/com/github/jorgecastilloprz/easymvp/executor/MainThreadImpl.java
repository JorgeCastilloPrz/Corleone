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
package com.github.jorgecastilloprz.easymvp.executor;

import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;

/**
 * {@link MainThread} implementation. Will make
 * interactor Callbacks able to get executed in the Android UI thread*
 * * *
 * Created by jorge on 11/01/15.
 */
public class MainThreadImpl implements MainThread {

    private Handler handler;

    @Inject
    MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
