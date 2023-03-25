package com.redditBot.reddit.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId
import org.slf4j.LoggerFactory
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
    @Scheduled(cron = "0 38 23 * * ?", zone = "America/Chicago")
    fun startScheduledTasks() {
        val toPhoneNumber = "whatsapp:$toNumber" // Replace with the destination phone number
        val fromPhoneNumber = "whatsapp:$fromNumber" // Replace with your Twilio phone number

        println("Task started: ${LocalDateTime.now()}")
        val image = sendService.getRandomNonRepeatedImage()
        if (image != null) {
            sendService.sendImageUrlViaTwilio(image.url, toPhoneNumber, fromPhoneNumber)
        }
        println("Task completed: ${LocalDateTime.now()}")
    }
}