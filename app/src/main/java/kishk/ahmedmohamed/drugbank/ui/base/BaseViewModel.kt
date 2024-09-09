package kishk.ahmedmohamed.drugbank.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kishk.ahmedmohamed.drugbank.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val mError = MutableLiveData<Throwable?>(null)
    val error = mError

    protected fun runOnScope(
        onFailure: (suspend (Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) = viewModelScope.launch {
        runCatching {
            block()
        }.onFailure {
            if (it !is CancellationException){
                mError.postValue(it)
            }

            // BuildConfig is generated before each build so BUILD_TYPE may change.
            @Suppress("KotlinConstantConditions")
            if (BuildConfig.BUILD_TYPE == "debug") {
                it.printStackTrace()
            }
            onFailure?.invoke(it)
        }
    }

    fun clearErrorState() {
        mError.postValue(null)
    }
}