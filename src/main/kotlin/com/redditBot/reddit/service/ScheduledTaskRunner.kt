package com.redditBot.reddit.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId

@Component
class ScheduledTaskRunner(
    private val sendService: SendService
    private val logger = LoggerFactory.getLogger(ScheduledTaskRunner::class.java)
// Inject your service here
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
            logger.info("Scheduling task to send images via Twilio")
            sendService.scheduleDailyTaskAt(15, 55, ZoneId.of("America/Chicago")) {
                val image = sendService.getRandomNonRepeatedImage()
                if (image != null) {
                    logger.info("Sending image with ID: ${image.id} to $toPhoneNumber")
                    sendService.sendImageUrlViaTwilio(image.url, toPhoneNumber, fromPhoneNumber)
                    logger.info("Sent image with ID: ${image.id}")
                } else {
                    logger.info("All images have been sent. Resetting sent status for all images")
                    sendService.resetSentStatusForAllImages()
                    logger.info("Sent status reset for all images")
                }
            }
        }
    }
}