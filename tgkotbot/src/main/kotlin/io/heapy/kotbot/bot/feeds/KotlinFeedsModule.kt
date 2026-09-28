package io.heapy.kotbot.bot.feeds

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.HttpClientModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.configuration.KotlinFeedsConfiguration
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.markdown.MarkdownModule

@Module
class KotlinFeedsModule(
    private val configurationModule: ConfigurationModule,
    private val kotbotModule: KotbotModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val httpClientModule: HttpClientModule,
    private val markdownModule: MarkdownModule,
) {
    val kotlinFeedsConfiguration: KotlinFeedsConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = KotlinFeedsConfiguration.serializer(),
                path = "kotlinFeeds",
            )
    }

    val feedItemDao: FeedItemDao by lazy {
        FeedItemDao()
    }

    val kotlinReleasesFeed: Feed by lazy {
        GithubReleasesFeed(
            client = httpClientModule.httpClient,
            token = kotlinFeedsConfiguration.githubToken,
            threadId = kotlinFeedsConfiguration.releasesThreadId,
            pollInterval = kotlinFeedsConfiguration.releasesPollInterval,
        )
    }

    val kotlinNewsFeed: Feed by lazy {
        BlogRssFeed(
            client = httpClientModule.httpClient,
            urls = kotlinFeedsConfiguration.newsUrls,
            threadId = kotlinFeedsConfiguration.newsThreadId,
            pollInterval = kotlinFeedsConfiguration.newsPollInterval,
        )
    }

    val kotlinFeedsJob: KotlinFeedsJob by lazy {
        KotlinFeedsJob(
            enabled = kotlinFeedsConfiguration.enabled,
            feeds = listOf(kotlinReleasesFeed, kotlinNewsFeed),
            feedItemDao = feedItemDao,
            kotbot = kotbotModule.kotbot,
            markdown = markdownModule.markdown,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
            chatId = kotlinFeedsConfiguration.chatId,
            sendInterval = kotlinFeedsConfiguration.sendInterval,
        )
    }
}
