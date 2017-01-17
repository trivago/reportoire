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

package com.trivago.reportoire.core.common

import com.trivago.reportoire.core.sources.Source
import com.trivago.reportoire.core.sources.SyncSource

/**
 * Source that stores a value within the memory for the given input.
 */
class MemorySource<TModel, in TInput> : SyncSource<TModel, TInput> {

    // Members

    private var mLatestInput: TInput? = null
    private var mCachedModel: TModel? = null

    // Public Api

    /**
     * Saves the model in relation to the given input.
     */
    fun setModel(input: TInput?, model: TModel?) {
        mLatestInput = input
        mCachedModel = model
    }

    // SyncSource

    override fun getResult(input: TInput?): Source.Result<TModel> {
        if (mCachedModel != null && input?.equals(mLatestInput)!!) {
            return Source.Result.Success(mCachedModel)
        } else {
            return Source.Result.Error(IllegalStateException("No model in memory found!"))
        }
    }

    // Source

    override fun reset() {
        setModel(null, null)
    }
}
