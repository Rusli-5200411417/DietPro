package com.example.dietproapp.core.data.source.remote.network

data class ResourceLapor<out T>(val state: State, val lapor:  T?, val message:  String?)  {

    companion object    {

        fun <T> success(lapor: T?): ResourceLapor<T> {
            return ResourceLapor(State.SUCCESS, lapor, null)
        }

        fun <T> error(msg: String, lapor: T?): ResourceLapor<T> {
            return ResourceLapor(State.ERROR, lapor, msg)
        }

        fun <T> loading(lapor: T?): ResourceLapor<T> {
            return ResourceLapor(State.LOADING, lapor, null)
        }

    }

}
