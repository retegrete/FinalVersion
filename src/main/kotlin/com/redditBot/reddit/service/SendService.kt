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
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
@Service
class SendService(private val redditImageRepository: RedditImageRepository) {

    @Value("\${twilio.accountSid}")
    private lateinit var accountSid: String

    @Value("\${twilio.authToken}")
    private lateinit var authToken: String

    @Value("\${twilio.from}")
    private lateinit var fromNumber: String

    suspend fun scheduleDailyTaskAt(hour: Int, minute: Int, zoneId: ZoneId, task: () -> Unit) {
        while (true) {
            val now = LocalDateTime.now(zoneId)
            val target = now.with(LocalTime.of(hour, minute)).plusDays(if (now.hour >= hour) 1 else 0)
            val delayDuration = Duration.between(now, target)
            withTimeoutOrNull(delayDuration.toMillis()) {
                delay(delayDuration.toMillis())
            }
            task()
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