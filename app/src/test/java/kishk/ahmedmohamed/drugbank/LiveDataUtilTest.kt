package kishk.ahmedmohamed.drugbank

/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T?> {
        override fun onChanged(value: T?) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}


fun <T> observeLiveDataValueChanges(
    liveData: LiveData<T>,
    valuesSequence: List<T>,
    timeoutSeconds: Long = 2,
    call: () -> Unit
) {
    val latch = CountDownLatch(valuesSequence.size)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            val expectedValue = valuesSequence[valuesSequence.size - latch.count.toInt()]
            if (latch.count == 1L) {
                liveData.removeObserver(this)
            }
            assertEquals(expectedValue, value)
            latch.countDown()
        }
    }
    liveData.observeForever(observer)

    call()

    if (!latch.await(timeoutSeconds, TimeUnit.SECONDS)) {
        throw TimeoutException("LiveData value was never set.")
    }
}


fun <T> observeLiveDataUntil(
    liveData: LiveData<T>,
    timeoutSeconds: Long = 2,
    condition: (T) -> Boolean,
    call: () -> Unit
) {
    val latch = CountDownLatch(1)
    val observer = Observer<T> { value ->
        if (condition(value)) {
            latch.countDown()
        }
    }
    liveData.observeForever(observer)

    call()

    if (!latch.await(timeoutSeconds, TimeUnit.SECONDS)) {
        throw TimeoutException("LiveData value condition was never met.")
    }
}

fun <T> observeLiveDataValueChanges(
    liveData: LiveData<T>,
    valuesSequence: List<T>,
    onValue: (Int, T) -> Unit,
    timeoutSeconds: Long = 2,
    call: () -> Unit
) {
    val latch = CountDownLatch(valuesSequence.size)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            val index = valuesSequence.size - latch.count.toInt()
            val expectedValue = valuesSequence[index]
            if (latch.count == 1L) {
                liveData.removeObserver(this)
            }
            latch.countDown()
            assertEquals(expectedValue, value)
            onValue(index, value)
        }
    }
    liveData.observeForever(observer)

    call()

    if (!latch.await(timeoutSeconds, TimeUnit.SECONDS)) {
        throw TimeoutException("LiveData value was never set.")
    }
}
