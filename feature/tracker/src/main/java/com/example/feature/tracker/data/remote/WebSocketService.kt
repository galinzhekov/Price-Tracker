package com.example.feature.tracker.data.remote

import com.example.core.network.WebSocketUrl
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(
    private val client: HttpClient,
    @WebSocketUrl private val url: String
) {
    private var session: WebSocketSession? = null

    val events: Flow<WebSocketEvent> = flow {
        try {
            client.webSocket(url) {
                session = this
                emit(WebSocketEvent.Connected)
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        emit(WebSocketEvent.Message(frame.readText()))
                    }
                }
            }
        } catch (e: Exception) {
            emit(WebSocketEvent.Disconnected(e))
        } finally {
            session = null
            emit(WebSocketEvent.Disconnected())
        }
    }.shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.WhileSubscribed()) // Make it a hot flow

    // Method to send a message
    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

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
