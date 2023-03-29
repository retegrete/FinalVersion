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
import java.net.URI
import java.time.*

@Service
class SendService(private val redditImageRepository: RedditImageRepository) {

    @Value("\${twilio.accountSid}")
    private lateinit var accountSid: String

    @Value("\${twilio.authToken}")
    private lateinit var authToken: String


    fun sendImageUrlViaTwilio(imageUrl: String, toPhoneNumber: String, fromPhoneNumber: String) {
        Twilio.init(accountSid, authToken)
        val message = Message.creator(
            PhoneNumber(toPhoneNumber),
            PhoneNumber(fromPhoneNumber),
            "Holi Hermosa :)"
        ).setMediaUrl(listOf(URI.create(imageUrl)))
            .create()

        println("Sent message: ${message.sid}")
    }

    @Transactional
    fun getRandomNonRepeatedImage(): RedditImage? {
        val image = redditImageRepository.getRandomNonRepeatedImage()
        if (image[0] != null) {
            image[0]?.id?.let { redditImageRepository.updateSentStatus(it, true) }
        }
        return image[0]
    }

}