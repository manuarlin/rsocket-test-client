package com.manua.rsockettestclient

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.connectWebSocketAndAwait
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
class HelloClientController {

    @GetMapping("helloClient")
    fun hello(): Unit = runBlocking {
        val publisher = flow {
            for (category in arrayOf("Sporting Goods", "Electronics")) {
                delay(2000)
                emit(category)
            }
        }
        RSocketRequester.builder()
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connectWebSocketAndAwait(URI("ws://localhost:7000"))
                .route("items")
                .data(publisher)
                .retrieveFlow<String>()
                .collect { println(it) }
    }
}