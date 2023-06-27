package com.redditBot.reddit.repositories

import com.redditBot.reddit.models.RedditImage
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RedditImageRepository: JpaRepository<RedditImage, Long>{

    @Query("SELECT * FROM public.images WHERE subreddit = :subreddit", nativeQuery = true)
    fun findMyImage(subreddit: String): List<RedditImage>

    @Transactional
    @Query("SELECT * FROM public.images WHERE sent = false ORDER BY RANDOM()", nativeQuery = true)
    fun getRandomNonRepeatedImage(): List<RedditImage?>

    @Transactional
    @Modifying
    @Query("UPDATE RedditImage r SET r.sent = :sent WHERE r.id = :id", nativeQuery = false)
    fun updateSentStatus(@Param("id") id: Long, @Param("sent") sent: Boolean)

}

