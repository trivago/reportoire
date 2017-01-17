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

package com.trivago.reportoire.core

import com.trivago.reportoire.core.sources.Source

/**
 * Base class for every repository you build.
 */
abstract class Repository<out TModel> {

    // Members

    private var mSources: List<Source<TModel, *>> = emptyList()

    // Public API

    /**
     * Returns true if any of the underlying source is currently getting the result.
     */
    fun isGettingResult(): Boolean {
        return cachedSources().any { it.isGettingResult() }
    }

    /**
     * Cancels all underlying sources.
     */
    fun cancel() {
        cachedSources().forEach { it.cancel() }
    }

    /**
     * Resets all underlying sources.
     */
    fun reset() {
        cachedSources().forEach { it.reset() }
    }

    // Abstract API

    /**
     * Return all sources of this repository.
     */
    abstract fun allSources(): List<Source<TModel, *>>

    // Private API

    private fun cachedSources(): List<Source<TModel, *>> {
        if (mSources.isEmpty()) {
            mSources = allSources()
        }

        return mSources
    }
}
