package com.example.feature.tracker

import com.example.core.network.WebSocketUrl
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(
    private val client: HttpClient,
    @WebSocketUrl private val url: String
) {
    fun connect(): Flow<WebSocketEvent> = flow {
        emit(WebSocketEvent.Connected)

        try {
            client.webSocket(url) {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val receivedText = frame.readText()
                        emit(WebSocketEvent.Message(receivedText))
                    }
                }
            }
            emit(WebSocketEvent.Disconnected(null))
        } catch (t: Throwable) {
            emit(WebSocketEvent.Disconnected(t))
        }
    }
}

sealed class WebSocketEvent {
    data object Connected : WebSocketEvent()
    data class Message(val text: String) : WebSocketEvent()
    data class Disconnected(val cause: Throwable? = null) : WebSocketEvent()
}
