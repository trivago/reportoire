#reportoire

**Reportoire** is a simple set of classes to apply the repository pattern to your Android application.

For further discussions and information please take a look at our [blog post]().

![](https://travis-ci.org/trivago/reportoire.svg?branch=master)

#Installation

Simply add the corresponding gradle dependency to your application's `build.gradle`:

```groovy
dependencies {

    // Core
    compile 'com.trivago.reportoire:reportoire-core:{latest_version}'
    
    // rxV1
    compile 'com.trivago.reportoire:reportoire-rxV1:{latest_version}'
    
    // rxV2
    compile 'com.trivago.reportoire:reportoire-rxV2:{latest_version}'
}
```

##Sample

Here is an example implementation that would first ask a memory source and afterwards a network one.

```java
/**
 * Simple source that uses an API client to query data.
 */
class MyNetworkSource : CachedObservableSource<Model, String>() {

    // CachedObservableSource

    override fun onCreateResultObservable(input: String?): Observable<Result<Model>> {
        return mApiClient.getModel(input)
    }
}

/**
 * Repository that will try to get the cached data first before trying a network request.
 */
class RxMemoryNetworkRepository : Repository<Model>() {

    // Members

    private val mMemorySource = MemorySource<Model, String>()
    private val mNetworkSource = MyNetworkSource()

    // Public API

    fun getModel(input: String): Observable<Source.Result<Model>> {

        // Get the cached result
        val cachedResult = mMemorySource.getResult(input)

        // Check if it is a success
        if (cachedResult is Source.Result.Success) {

            // If so simply emit the result
            return Observable.just(cachedResult)
        }

        // Else subscribe to the network observable
        return mNetworkSource.resultObservable(input)
                .doOnNext { result ->

                    // And save the result in the memory source if it is a success
                    if (result is Source.Result.Success) {
                        mMemorySource.setModel(input, result.model)
                    }
                }
    }

    // Repository

    override fun allSources(): List<Source<Model, *>> {
        return listOf(mNetworkSource, mMemorySource) // Return a list of the used sources
    }
}
```
 
##core

##Repository

Base class for every repository you build. Here we combine every source. See above for an example.

###Source

This is the Base source interface. Every source must implement the following methods:

```kotlin
/**
 * Tells the source that its on-going task should be
 * cancelled.
 */
fun cancel()

/**
 * Tells the source that everything that has been 
 * cached so far should be reset.
 */
fun reset()

/**
 * Returns true if the source is currently getting the
 * result. Otherwise false.
 */
fun isGettingResult(): Boolean
```

###SyncSource

A source that immediately returns a result for a given input.

```kotlin
/**
 * Immediately returns the result.
 */
fun getResult(input: TInput?) : Source.Result<TModel>
```

###AsyncSource

A source that calls the given callback once there is a result.

```kotlin
/**
 * Pass in a callback that will return the result once
 * done or if an error occurs.
 */
fun fetchResult(input: TInput?, callback: ResultCallback<TModel>)
```

##rxV1 & rxV2

###RXSource

A simple rxjava based source.
 
```kotlin 
/**
 * Returns an observable emitting the result. 
 */
fun resultObservable(input: TInput?): Observable<Source.Result<TModel>>
```

###CachedObservable

An abstract RxSource source that will use the specified observable or resubscribe to the previous one if possible. Internally it uses the [**cache**](http://reactivex.io/documentation/operators/replay.html) operator.

```kotlin 
/**
 * Override this method and supply a new observable that will be used to fetch the result.
 */
abstract fun onCreateResultObservable(input: TInput?): Observable<Result<TModel>>
```
### Issues

If you have any issues running the app after including this library, concider to check this [RxRelay issue](https://github.com/JakeWharton/RxRelay/issues/13) and update RxRelay to either `1.2.0` or `2.0.0`.

### About

Reportoire was built by trivago 🏨

### License

Reportoire is licensed under Apache Version 2.0.
