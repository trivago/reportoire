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
import org.jetbrains.spek.api.Spek
import org.mockito.Mockito.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// Spec

class RepositorySpec : Spek({
    given("A repository with some sources") {
        val source1: Source<Any, *> = mock()
        val source2: Source<Any, *> = mock()
        val repository: MockRepository = MockRepository(listOf(source1, source2))

        context("it is cancelled") {
            repository.cancel()

            it("cancels all underlying sources") {
                verify(source1, times(1)).cancel()
                verify(source2, times(1)).cancel()
            }
        }

        context("when it is resetted") {
            repository.reset()

            it("resets all underlying sources") {
                verify(source1, times(1)).reset()
                verify(source2, times(1)).reset()
            }
        }

        context("when getting the result") {
            context("and there is at least one source that is currently getting the result") {
                beforeEach {
                    `when`(source1.isGettingResult()).thenReturn(false)
                    `when`(source2.isGettingResult()).thenReturn(true)
                }

                it("returns true") {
                    assertTrue(repository.isGettingResult())
                }
            }

            context("and there is no source that is currently getting the result") {
                beforeEach {
                    `when`(source1.isGettingResult()).thenReturn(false)
                    `when`(source2.isGettingResult()).thenReturn(false)
                }

                it("returns false") {
                    assertFalse(repository.isGettingResult())
                }
            }
        }
    }
})

// Helper

inline fun <reified T : Any> mock(): T = mock(T::class.java)

class MockRepository(val pSources: List<Source<Any, *>>) : Repository<Any>() {
    override fun allSources(): List<Source<Any, *>> {
        return pSources
    }
}
