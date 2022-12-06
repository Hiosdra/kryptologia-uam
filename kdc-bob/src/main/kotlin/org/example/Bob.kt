package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@SpringBootApplication
class DemoApplication {
    @Bean
    fun keyService(): KeyService = KeyService()
    @Bean
    fun fileService(): FileService = FileService()
}
