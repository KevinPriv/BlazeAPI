package me.kbrewster.blazeapi.utils.net

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class URLReader(val url: String) {

    /**
     * Response type. Possible values are SUCCESS and misc. fail reasons.
     *
     * @return Response type.
     */
    val responseType: ResponseType

    /**
     * @return JSON representation of data fetched from [the url][.getURL].
     */
    var json: JsonObject? = null
        private set

    init {
        this.responseType = setup()
    }

    /**
     * Reads the content from the URL, returning the response type and parsing the
     * [JSON][.getJson] in the process.
     *
     * @return [Response type][.getResponseType].
     */
    private fun setup(): ResponseType {
        return try {
            val text = URL(url).readText()
            json = Gson().fromJson(text, JsonObject::class.java)
            ResponseType.SUCCESS
        } catch (e: MalformedURLException) {
            ResponseType.FAILED_MALFORMED_URL
        } catch (e: IOException) {
            ResponseType.FAILED_IO
        }

    }

    enum class ResponseType {
        SUCCESS, FAILED_IO, FAILED_MALFORMED_URL, LOADING
    }
}