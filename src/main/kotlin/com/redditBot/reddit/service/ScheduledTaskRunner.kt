package com.redditBot.reddit.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Component
class ScheduledTaskRunner(
    private val sendService: SendService,

// Inject your service here
) {

    @Value("\${twilio.from}")
    private lateinit var fromNumber: String

    @Value("\${twilio.to}")
    private lateinit var toNumber: String
    @Scheduled(cron = "0 0 10 * * ?", zone = "America/Chicago")
    fun startScheduledTasks() {
        val toPhoneNumber = "whatsapp:$toNumber" // Replace with the destination phone number
        val fromPhoneNumber = "whatsapp:$fromNumber" // Replace with your Twilio phone number

        println("Task started: ${LocalDateTime.now()}")
        val image = sendService.getRandomNonRepeatedImage()
        if (image != null && image.subreddit == "kittens") {
            sendService.sendImageUrlViaTwilio(image.url, toPhoneNumber, fromPhoneNumber)
        }
        println("Task completed: ${LocalDateTime.now()}")
    }
}