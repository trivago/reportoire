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
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// Spec

class MemorySourceSpec : Spek({

    given("A MemoryCacheSource") {
        val memorySource = MemorySource<String, String>()

        context("if it has an invalid model") {
            beforeEach {
                memorySource.setModel("key", null)
            }

            it("returns an Error when getting the result") {
                val result = memorySource.getResult("key")
                assertTrue(result is Source.Result.Error && result.throwable is IllegalStateException)
            }
        }

        context("if it has a valid model") {
            beforeEach {
                memorySource.setModel("key", "value")
            }

            it("returns an Error when accessing a non existing key") {
                val result = memorySource.getResult("some different key")
                assertTrue(result is Source.Result.Error && result.throwable is IllegalStateException)
            }

            it("returns the correct model when requesting it") {
                val result = memorySource.getResult("key")
                assertTrue(result is Source.Result.Success && result.model == "value")
            }

            context("when it is resetted") {
                beforeEach {
                    memorySource.reset()
                }

                it("returns an Error when getting the result") {

                    val result = memorySource.getResult("key")
                    assertTrue(result is Source.Result.Error && result.throwable is IllegalStateException)
                }
            }
        }

        it("returns false when checking if a result is currently gotten") {
            val isGettingModel = memorySource.isGettingResult()
            assertFalse(isGettingModel)
        }
    }
})
