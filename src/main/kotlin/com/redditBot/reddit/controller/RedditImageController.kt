package com.redditBot.reddit.controller

import com.redditBot.reddit.repositories.RedditImageRepository
import com.redditBot.reddit.service.RedditImageService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.http.ResponseEntity


@Controller
@RequestMapping("/reddit-images")
class RedditImageController(private val redditImageService: RedditImageService) {

    @GetMapping("/fetch")
    fun fetchAndStoreSubredditImages(
        @RequestParam subreddit: String,
        @RequestParam limit: Int
    ): ResponseEntity<String> {
        redditImageService.fetchAndStoreSubredditImages(subreddit, limit)
        return ResponseEntity.ok("Images saved")
    }
}