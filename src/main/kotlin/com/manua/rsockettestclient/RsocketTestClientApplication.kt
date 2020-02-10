package com.manua.rsockettestclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RsocketTestClientApplication

fun main(args: Array<String>) {
    runApplication<RsocketTestClientApplication>(*args)
}