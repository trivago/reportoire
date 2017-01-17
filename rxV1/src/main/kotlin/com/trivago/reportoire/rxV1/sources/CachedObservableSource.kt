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

package com.trivago.reportoire.rxV1.sources

import com.trivago.reportoire.core.sources.Source
import rx.Observable

/**
 * Abstract RxSource source that will use the specified observable or resubscribe to the previous
 * one if possible.
 */
abstract class CachedObservableSource<TModel, in TInput> : RxSource<TModel, TInput> {

    // Members

    private var mLatestInput: TInput? = null
    private var mResultObservable: Observable<Source.Result<TModel>>? = null

    // Abstract API

    /**
     * Override this method and supply a new observable that will be used to fetch the result.
     */
    abstract fun onCreateResultObservable(input: TInput?): Observable<Source.Result<TModel>>

    // RxSource

    override fun resultObservable(input: TInput?): Observable<Source.Result<TModel>> {
        if (mResultObservable == null || input?.equals(mLatestInput) == false) {
            mLatestInput = input
            mResultObservable = onCreateResultObservable(input).cache()
        }

        return mResultObservable!!
    }

    override fun isGettingResult(): Boolean {
        return mResultObservable != null
    }

    override fun reset() {
        mLatestInput = null
        mResultObservable = null
    }

    override fun cancel() {
        reset()
    }
}
