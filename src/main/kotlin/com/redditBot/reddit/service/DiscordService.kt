package com.redditBot.reddit.service

import com.redditBot.reddit.repositories.RedditImageRepository
import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.channel.Channel
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.rest.util.Color
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class DiscordService(
    @Value("\${discord.bot.token}")
    private val botToken: String,

    private val imageRepository: RedditImageRepository
) {
    private lateinit var client: GatewayDiscordClient

    @PostConstruct
    fun init() {
        client = DiscordClientBuilder.create(botToken)
            .build()
            .login()
            .block()!!

        client.on(MessageCreateEvent::class.java)
            .subscribe { event ->
                val content = event.message.content
                if (content.equals("!sendimage", ignoreCase = true)) {
                    sendImageToRedditFunServer()
                }
            }

        client.onDisconnect().subscribe()
    }

    fun sendImageToRedditFunServer() {
        val imageUrlMono = getRandomUnsentImageUrl()
        val guildMono = getRedditFunServerGuild()

        guildMono.flatMap { guild ->
            println("Processing guild: ${guild.name}") // Add this line
            guild.channels
                .filter { it.type == Channel.Type.GUILD_TEXT && it.name.equals("general", ignoreCase = true) }
                .next()
                .cast(TextChannel::class.java)
        }.flatMap { channel ->
            imageUrlMono.flatMap { imageUrl ->
                println("Processing channel: ${channel.name}")
                channel.createMessage { spec ->
                    spec.setEmbed { embedSpec ->
                        embedSpec.setColor(Color.BLUE)
                            .setImage(imageUrl)
                            .setTimestamp(Instant.now())
                    }
                }
            }
        }.doOnNext {
            println("Message sent successfully") // Add this line
        }.doOnError { error ->
            println("Error occurred: ${error.localizedMessage}") // Add this line
        }.block()
    }

    private fun getRedditFunServerGuild(): Mono<Guild> {
        return client.guilds
            .filter { it.name.equals("RedditFun's server", ignoreCase = true) }
            .doOnNext { println("Found guild: ${it.name}") } // Add this line
            .next()
    }

    private fun getRandomUnsentImageUrl(): Mono<String> {
        return Mono.fromCallable {
            val unsentImages = imageRepository.getRandomNonRepeatedImage().filter { it!!.url.startsWith("http") || it.url.startsWith("https") && it.subreddit != "kittens" }
            if (unsentImages.isEmpty()) {
                throw IllegalStateException("No unsent images found.")
            }

            val randomImage = unsentImages.first()
            val fixedImageUrl = convertGifvToGif(randomImage!!.url)
            if (randomImage.subreddit != "kittens") {
                randomImage.id?.let { imageRepository.updateSentStatus(it, true) }
            }

            fixedImageUrl
        }.doOnNext { imageUrl -> println("Image URL: $imageUrl") }
    }

    fun convertGifvToGif(url: String): String {
        return if (url.endsWith(".gifv")) {
            url.replace(".gifv", ".gif")
        } else {
            url
        }
    }
}
