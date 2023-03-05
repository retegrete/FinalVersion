package com.redditBot.reddit.repositories

import com.redditBot.reddit.models.RedditImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RedditImageRepository: JpaRepository<RedditImage, Long>