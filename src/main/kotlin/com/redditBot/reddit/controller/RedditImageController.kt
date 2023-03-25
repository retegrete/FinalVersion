package com.redditBot.reddit.controller

import com.redditBot.reddit.models.RedditImage
import com.redditBot.reddit.repositories.RedditImageRepository
import com.redditBot.reddit.service.RedditImageService
import com.twilio.Twilio
import com.twilio.exception.TwilioException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.transaction.Transactional
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.http.ResponseEntity


@Controller
@RequestMapping("/reddit-images")
class RedditImageController(private val redditImageService: RedditImageService, private val redditImageRepository: RedditImageRepository) {

    @GetMapping("/fetch")
    fun fetchAndStoreSubredditImages(
        @RequestParam subreddit: String,
        @RequestParam limit: Int
    ): ResponseEntity<String> {
        redditImageService.fetchAndStoreSubredditImages(subreddit, limit)
        return ResponseEntity.ok("Images saved")
    }

    @GetMapping
    fun getAllImages(model: Model): String {
        val images = redditImageService.getAllImages()
        model.addAttribute("images", images)
        return "reddit-images"
    }
}