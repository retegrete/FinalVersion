package com.redditBot.reddit.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId

@Component
class ScheduledTaskRunner(
    private val sendService: SendService // Inject your service here
) {

    @Value("\${twilio.from}")
    private lateinit var fromNumber: String

    @Value("\${twilio.to}")
    private lateinit var toNumber: String
    @PostConstruct
    fun startScheduledTasks() {
        val toPhoneNumber = "whatsapp:$toNumber" // Replace with the destination phone number
        val fromPhoneNumber = "whatsapp:$fromNumber" // Replace with your Twilio phone number

        GlobalScope.launch {
            sendService.scheduleDailyTaskAt(15, 35, ZoneId.of("America/Chicago")) {
                val image = sendService.getRandomNonRepeatedImage()
                if (image != null) {
                    sendService.sendImageUrlViaTwilio(image.url, toPhoneNumber, fromPhoneNumber)
                } else {
                    sendService.resetSentStatusForAllImages()
                }
            }
        }
    }
}