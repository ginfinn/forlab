package com.saprikin.factory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class FactoryApplication
fun main(args: Array<String>) {
    runApplication<FactoryApplication>(*args)
}