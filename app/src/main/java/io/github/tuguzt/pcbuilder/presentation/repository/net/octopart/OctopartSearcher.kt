package io.github.tuguzt.pcbuilder.presentation.repository.net.octopart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.tuguzt.pcbuilder.presentation.repository.net.octopart.mock.MockOctopartAPI
import io.github.tuguzt.pcbuilder.presentation.repository.net.octopart.model.SearchResponse
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resumeWithException

/**
 * Client of the Octopart REST API defined by [OctopartAPI].
 */
internal object OctopartSearcher {
    @JvmStatic
    private val LOG_TAG = OctopartSearcher::class.simpleName

    @JvmStatic
    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://octopart.com/api/")
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .build()

    @JvmStatic
    private val octopartAPI: OctopartAPI = MockOctopartAPI
//        retrofit.create(OctopartAPI::class.java)

    @JvmStatic
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun searchComponentsSuspend(query: String, start: Int, limit: Int): List<SearchResult> {
        return suspendCancellableCoroutine { continuation ->
            octopartAPI.searchQuery(query, apiKey = "TOP-SECRET", start, limit)
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val searchResponse = requireNotNull(response.body())
                            val data = searchResponse.results.map { SearchResult(it) }
                            continuation.resume(data, null)
                            return
                        }
                        val exception = IllegalStateException(response.errorBody()?.string())
                        Log.e(LOG_TAG, "Retrofit failure!", exception)
                        continuation.resumeWithException(exception)
                    }

                    override fun onFailure(call: Call<SearchResponse>, exception: Throwable) {
                        Log.e(LOG_TAG, "Retrofit failure!", exception)
                        continuation.resumeWithException(exception)
                    }
                })
        }
    }

    @JvmStatic
    fun searchComponents(query: String, start: Int, limit: Int): LiveData<List<SearchResult>> {
        val mutableLiveData = MutableLiveData<List<SearchResult>>()
        octopartAPI.searchQuery(query, apiKey = "TOP-SECRET", start, limit)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>,
                ) {
                    if (response.isSuccessful) {
                        val searchResponse = requireNotNull(response.body())
                        CoroutineScope(Dispatchers.Main).launch {
                            mutableLiveData.value = searchResponse.results.map { SearchResult(it) }
                        }
                        return
                    }
                    Log.e(LOG_TAG, "Error response: ${response.errorBody()?.string()}")
                }

                override fun onFailure(call: Call<SearchResponse>, throwable: Throwable) {
                    Log.e(LOG_TAG, "Call failure!", throwable)
                }
            })
        return mutableLiveData
    }
}