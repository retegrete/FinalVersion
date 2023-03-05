package com.redditBot.reddit.controller

import com.redditBot.reddit.service.RedditImageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/reddit-images")
class RedditImageController(private val redditImageService: RedditImageService) {

    @GetMapping("/fetch")
    fun fetchAndStoreSubredditImages(
        @RequestParam subreddit: String,
        @RequestParam limit: Int
    ): String {
        redditImageService.fetchAndStoreSubredditImages(subreddit, limit)
        println("holi")
        return "redirect:/reddit-images"
    }

    @GetMapping
    fun getAllImages(model: Model): String {
        val images = redditImageService.getAllImages()
        model.addAttribute("images", images)
        return "reddit-images"
    }
}