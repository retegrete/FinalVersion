package com.redditBot.reddit.service

import com.redditBot.reddit.models.RedditImage
import com.redditBot.reddit.repositories.RedditImageRepository
import org.springframework.stereotype.Service

@Service
class RedditImageService(
    private val redditClient: RedditClientService,
    private val redditImageRepository: RedditImageRepository
) {
    fun fetchAndStoreSubredditImages(subreddit: String, limit: Int) {
        val urls = redditClient.fetchSubredditImages(subreddit, limit)
        val redditImages = urls.map {
            RedditImage(url = it, subreddit = subreddit, title = "N/A", sent = false)
        }
        redditImageRepository.saveAll(redditImages)
    }
}