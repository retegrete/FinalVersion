package com.redditBot.reddit.controller

import com.redditBot.reddit.service.DiscordService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DiscordController(private val discordService: DiscordService) {

    @PostMapping("/sendImage")
    fun sendImage(): ResponseEntity<String> {
        return try {
            discordService.sendImageToRedditFunServer()
            ResponseEntity.ok("Image sent successfully.")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Failed to send image: ${e.message}")
        }
    }
}
