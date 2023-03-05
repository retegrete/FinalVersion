package com.redditBot.reddit.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

class RedditClientService {
    fun fetchSubredditImages(subreddit: String, limit: Int): List<String> {
        val url = "https://www.reddit.com/r/$subreddit/hot.json?limit=$limit"
        val headers = HttpHeaders().apply {
            set("User-Agent", "my-bot/0.0.1")
        }
        val request = HttpEntity<String>(headers)
        val response = RestTemplate().exchange(url, HttpMethod.GET, request, String::class.java)
        val jsonNode = ObjectMapper().readTree(response.body)
        return jsonNode["data"]["children"].map {
            it["data"]["url"].asText()
        }
    }
}