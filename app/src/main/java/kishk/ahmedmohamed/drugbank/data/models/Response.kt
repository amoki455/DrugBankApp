package kishk.ahmedmohamed.drugbank.data.models

sealed class Response<T> {
    class Success<T>(val data: T) : Response<T>()
    class Failure<T>(val error: Throwable) : Response<T>()
}