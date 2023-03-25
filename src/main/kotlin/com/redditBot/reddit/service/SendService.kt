package com.redditBot.reddit.service

import com.redditBot.reddit.models.RedditImage
import com.redditBot.reddit.repositories.RedditImageRepository
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.transaction.Transactional
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.*

@Service
class SendService(private val redditImageRepository: RedditImageRepository) {

    @Value("\${twilio.accountSid}")
    private lateinit var accountSid: String

    @Value("\${twilio.authToken}")
    private lateinit var authToken: String

    @Value("\${twilio.from}")
    private lateinit var fromNumber: String

    suspend fun scheduleDailyTaskAt(hour: Int, minute: Int, zoneId: ZoneId, task: () -> Unit) {
        val now = ZonedDateTime.now(zoneId)
        val targetTime = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        val nextExecutionTime = if (targetTime.isBefore(now)) targetTime.plusDays(1) else targetTime
        val delayMillis = Duration.between(now, nextExecutionTime).toMillis()

        println("Current time: $now")
        println("Next execution time: $nextExecutionTime")
        println("Delay (ms): $delayMillis")

        println("Starting delay...")
        val intervalMillis = 60_000L // 1 minute
        val totalIntervals = delayMillis / intervalMillis

        for (i in 1..totalIntervals) {
            println("Delay interval $i of $totalIntervals...")
            delay(intervalMillis)
        }
        println("Delay completed, starting task execution loop")
        while (true) {
            task()
            delay(Duration.ofDays(1).toMillis())
        }
    }

    fun sendImageUrlViaTwilio(imageUrl: String, toPhoneNumber: String, fromPhoneNumber: String) {
        Twilio.init(accountSid, authToken)
        val message = Message.creator(
            PhoneNumber(toPhoneNumber),
            PhoneNumber(fromPhoneNumber),
            imageUrl
        ).create()

        println("Sent message: ${message.sid}")
    }

    @Transactional
    fun getRandomNonRepeatedImage(): RedditImage? {
        val image = redditImageRepository.getRandomNonRepeatedImage()
        if (image != null) {
            image.id?.let { redditImageRepository.updateSentStatus(it, true) }
        }
        return image
    }

    fun resetSentStatusForAllImages() {
        redditImageRepository.resetSentStatusForAllImages()
    }

}