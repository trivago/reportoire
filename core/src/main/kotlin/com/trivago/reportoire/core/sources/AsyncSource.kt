/*********************************************************************************
 * Copyright 2017-present trivago GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **********************************************************************************/

package com.trivago.reportoire.core.sources

/**
 * Simple asynchronous source interface.
 */
interface AsyncSource<out TModel, in TInput> : Source<TModel, TInput> {

    // IResultCallback

    /**
     * The result callback.
     */
    interface ResultCallback<in TModel> {

        // Interface API

        /**
         * Called when there is a new result.
         */
        fun onResult(result: Source.Result<TModel>)
    }

    // Interface API

    /**
     * Pass in a callback that will return the result once done or if an error occurs.
     */
    fun fetchResult(input: TInput?, callback: ResultCallback<TModel>)
}
