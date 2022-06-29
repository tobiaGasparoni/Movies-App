package com.example.mr_kotlin.utils.EventualConectivity


import android.content.ContentValues.TAG
import android.util.Log
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory


/**
 * Send a ping to googles primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    suspend fun execute(socketFactory: SocketFactory): Boolean = withContext(Dispatchers.IO)
        {
            return@withContext try{
                Log.d(TAG, "PINGING google.")
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
                Log.d(TAG, "PING success.")
                true
            }catch (e: IOException){
                Log.e(TAG, "No internet connection. ${e}")
                false
            }
    }

    /*
    fun execute(socketFactory: SocketFactory): Boolean {
    return try{
      Log.d(TAG, "PINGING google.")
      val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
      socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
      socket.close()
      Log.d(TAG, "PING success.")
      true
    }catch (e: IOException){
      Log.e(TAG, "No internet connection. ${e}")
      false
    }
  }
     */

}