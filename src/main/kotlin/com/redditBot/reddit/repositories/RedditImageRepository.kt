package com.redditBot.reddit.repositories

import com.redditBot.reddit.models.RedditImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RedditImageRepository: JpaRepository<RedditImage, Long>{

    @Query("SELECT * FROM public.images WHERE subreddit = :subreddit", nativeQuery = true)
    fun findMyImage(subreddit: String): List<RedditImage>

    @Query("SELECT r FROM public.images r WHERE r.sent = false ORDER BY RANDOM()", nativeQuery = true)
    fun getRandomNonRepeatedImage(): RedditImage?

    @Modifying
    @Query("UPDATE RedditImage r SET r.sent = :sent WHERE r.id = :id", nativeQuery = false)
    fun updateSentStatus(@Param("id") id: Long, @Param("sent") sent: Boolean)

    @Modifying
    @Query("UPDATE RedditImage r SET r.sent = false")
    fun resetSentStatusForAllImages()

}

