package mou.util

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

//------------------------------------------------------
//  http_get BEGAN
//  Summary: input String of url, return String of response data
fun http_get(sz_url: String, callback: (_: String) -> Unit): Deferred<String> {
    return GlobalScope.async {
        var ret = ""
        try {
            with(URL(sz_url).openConnection() as HttpURLConnection) {
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    ret = response.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        callback(ret)
        ret
    }
}
//  http_get ENDED
//------------------------------------------------------

//------------------------------------------------------
//  http_post BEGAN
//  Summary: input String of url, and String of json post body, return String of response data
fun http_post(sz_url: String, sz_json: String, callback: (_: String) -> Unit): Deferred<String> {
    return GlobalScope.async {
        var ret = ""
        try {
            with(URL(sz_url).openConnection() as HttpURLConnection) {
                requestMethod = "POST"

                val wr = OutputStreamWriter(getOutputStream());
                wr.write(sz_json);
                wr.flush();
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    ret = response.toString()
                    it.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        callback(ret)
        ret
    }
}
//  http_post BEGAN
//------------------------------------------------------
