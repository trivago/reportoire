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
 * Base interface for every Source you build.
 */
interface Source<out TModel, in TInput> {

    // Result

    /**
     * The result type that wil be delivered by any source.
     */
    sealed class Result<out TModel> {

        /**
         * Success cases which may contain a model.
         */
        class Success<out TModel>(val model: TModel?) : Result<TModel>()

        /**
         * Error cases which may contain a throwable.
         */
        class Error<out TModel>(val throwable: Throwable?) : Result<TModel>()
    }

    // Interface API

    /**
     * Tells the source that its on-going task should be cancelled.
     */
    fun cancel()

    /**
     * Tells the source that everything that has been cached so far should be reset.
     */
    fun reset()

    /**
     * Returns true if the source is currently getting the result. Otherwise false.
     */
    fun isGettingResult(): Boolean
}