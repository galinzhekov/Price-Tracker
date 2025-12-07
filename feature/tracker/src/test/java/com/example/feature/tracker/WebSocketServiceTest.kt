package com.example.feature.tracker

import app.cash.turbine.test
import com.example.feature.tracker.feed.data.remote.WebSocketEvent
import com.example.feature.tracker.feed.data.remote.WebSocketService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.WebSocketListener
import okhttp3.WebSocket
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WebSocketServiceTest {

    @Test
    fun `connect should emit Connected and then Message events on success`() = runTest {
        val server = MockWebServer()
        server.start()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("Hello from MockWebServer!")
                webSocket.send("It really works now")
                webSocket.close(1000, "Test finished")
            }
        }

        server.enqueue(MockResponse().withWebSocketUpgrade(listener))

        val client = HttpClient(CIO) {
            install(WebSockets)
        }

        val url = server.url("/raw").toString().replaceFirst("http", "ws")
        val service = WebSocketService(client, url)

        service.connect().test {
            assertEquals(WebSocketEvent.Connected, awaitItem())

            assertEquals("Hello from MockWebServer!", (awaitItem() as WebSocketEvent.Message).text)
            assertEquals("It really works now", (awaitItem() as WebSocketEvent.Message).text)

            val last = awaitItem()
            assertTrue(last is WebSocketEvent.Disconnected)
            awaitComplete()
        }

        client.close()
        server.shutdown()
    }

    @Test
    fun `connect should emit Disconnected on connection failure`() = runTest {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }

        val deadUrl = "ws://127.0.0.1:54321/raw"
        val service = WebSocketService(client, deadUrl)

        service.connect().test {
            assertEquals(WebSocketEvent.Connected, awaitItem())

            val ev = awaitItem()
            assertTrue("Event should be Disconnected", ev is WebSocketEvent.Disconnected)
            awaitComplete()
        }

        client.close()
    }
}
