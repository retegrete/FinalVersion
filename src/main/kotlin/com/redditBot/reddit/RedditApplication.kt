package com.redditBot.reddit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RedditApplication

fun main(args: Array<String>) {
	runApplication<RedditApplication>(*args)
}
