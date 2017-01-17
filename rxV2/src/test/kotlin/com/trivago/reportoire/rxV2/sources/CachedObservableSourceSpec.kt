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

package com.trivago.reportoire.rxV2.sources

import com.trivago.reportoire.core.sources.Source
import io.reactivex.Observable
import io.reactivex.Observable.just
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

// Spec

class CachedObservableSourceSpec : Spek({
    given("A CachedObservableSourceSpec") {
        val source = MockCachedObservableSource(
                mutableListOf(
                        just(Source.Result.Error(null)),
                        just(Source.Result.Error(null))
                )
        )

        context("not requesting the result") {
            source.reset()

            it("returns false when calling isGettingResult") {
                assertFalse(source.isGettingResult())
            }
        }

        context("requesting the result") {
            val firstObservable = source.resultObservable("Same input")

            context("when cancelled") {
                source.cancel()

                it("returns a different observable when requesting the result again") {
                    assertNotEquals(firstObservable, source.resultObservable("Same input"))
                }
            }

            context("when resetted") {
                source.reset()

                it("returns a different observable when requesting the result again") {
                    assertNotEquals(firstObservable, source.resultObservable("Same input"))
                }
            }

            it("returns true when calling isGettingResult") {
                assertTrue(source.isGettingResult())
            }
        }
    }
})

// Helper

class MockCachedObservableSource(private val pResultObservables: MutableList<Observable<Source.Result<Any?>>>) : CachedObservableSource<Any?, Any>() {
    override fun onCreateResultObservable(input: Any?): Observable<Source.Result<Any?>> {
        val observable = pResultObservables[0]

        if (pResultObservables.size > 1) {
            pResultObservables.removeAt(0)
        }

        return observable
    }
}