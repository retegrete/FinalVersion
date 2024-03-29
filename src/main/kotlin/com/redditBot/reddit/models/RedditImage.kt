package com.redditBot.reddit.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table

@Entity
@Table(name = "images", schema = "public")
data class RedditImage(
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val url: String = "",
    val subreddit: String ="",
    val title: String= "",
    var sent: Boolean = false
)