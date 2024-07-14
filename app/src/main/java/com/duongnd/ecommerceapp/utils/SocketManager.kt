package com.duongnd.ecommerceapp.utils

import android.annotation.SuppressLint
import android.support.annotation.Nullable
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketManager {
    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://192.168.99.52:8080/")
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    private fun on(event: String, listener: Emitter.Listener) {
        mSocket?.on(event, listener)
    }
    @SuppressLint("KotlinNullnessAnnotation")
    private fun emit(event: String, @Nullable obj: Any, @Nullable ack: Ack) {
        mSocket?.emit(event, obj, ack)
    }


    fun removeEvent(event: String) {
        mSocket?.off(event)
    }

    fun connect(listener: Emitter.Listener) {
        on(Socket.EVENT_CONNECT, listener)
        mSocket?.connect()
    }

    fun isConnected(): Boolean {
        return mSocket?.connected()?:false
    }

    fun clearSession() {
        mSocket?.off(Socket.EVENT_CONNECT)
        mSocket?.disconnect()
    }

    fun socket(): Socket? {
        return mSocket
    }
}