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
import com.trivago.reportoire.core.sources.SyncSource
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFalse

class SyncSourceSpec : Spek({
    given("A SyncSource simple implementation") {
        val syncSourceImpl = SyncSourceImplementation<String, String>()

        it("always emits false when checking if a result is currently gotten") {
            assertFalse(syncSourceImpl.isGettingResult())
        }
    }
})

class SyncSourceImplementation<out TInput, in TModel> : SyncSource<TInput, TModel> {
    override fun reset() {
        // Not important for test
    }

    override fun getResult(input: TModel?): Source.Result<TInput> {
        // Not important for test
        return Source.Result.Error(IllegalStateException("Not needed for test"))
    }
}
