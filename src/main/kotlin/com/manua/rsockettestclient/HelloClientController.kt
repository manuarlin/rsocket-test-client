package com.manua.rsockettestclient

import io.rsocket.resume.ClientResume
import io.rsocket.resume.PeriodicResumeStrategy
import io.rsocket.resume.ResumeStrategy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Publisher
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.connectTcpAndAwait
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration


@RestController
class HelloClientController {

    @GetMapping("helloClient")
    fun hello(): Unit = runBlocking {
        val publisher = flow {
            for (category in arrayOf("Sporting Goods", "Electronics")) {
                delay(4000)
                emit(category)
            }
        }
        RSocketRequester.builder()
                .rsocketFactory {
                    it.resumeSessionDuration(Duration.ofMinutes(5))
                            .resumeStrategy { VerboseResumeStrategy(PeriodicResumeStrategy(Duration.ofSeconds(1))) }
                            .resume()
                }
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connectTcpAndAwait("localhost", 7001)
//                .connectWebSocketAndAwait(URI("ws://localhost:7001"))
                .route("items")
                .data(publisher)
                .retrieveFlow<String>()
                .collect { println(it) }
    }
}

class VerboseResumeStrategy(private val resumeStrategy: ResumeStrategy) : ResumeStrategy {

    override fun apply(clientResume: ClientResume, throwable: Throwable): Publisher<*> {
        return Flux.from(resumeStrategy.apply(clientResume, throwable))
                .doOnNext { println("Disconnected. Trying to resume connection...") }
    }

}
