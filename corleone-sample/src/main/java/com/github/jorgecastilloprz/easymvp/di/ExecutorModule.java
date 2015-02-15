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
package com.github.jorgecastilloprz.easymvp.di;

import com.github.jorgecastilloprz.easymvp.executor.InteractorExecutor;
import com.github.jorgecastilloprz.easymvp.executor.MainThread;
import com.github.jorgecastilloprz.easymvp.executor.MainThreadImpl;
import com.github.jorgecastilloprz.easymvp.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jorge on 11/01/15.
 */
@Module(library = true)
public class ExecutorModule {

    @Provides
    @Singleton
    InteractorExecutor provideExecutor(ThreadExecutor threadExecutor) {
        return threadExecutor;
    }

    @Provides
    @Singleton
    MainThread provideMainThread(MainThreadImpl mainThread) {
        return mainThread;
    }
}
