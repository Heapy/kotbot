package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.bot.challenge.ChallengeGenerator
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.templates.CollectionPipelineTemplate
import io.heapy.kotbot.bot.challenge.templates.MapOperationsTemplate
import io.heapy.kotbot.bot.challenge.templates.NullSafetyTemplate
import io.heapy.kotbot.bot.challenge.templates.PairDestructuringTemplate
import io.heapy.kotbot.bot.challenge.templates.RangeExpressionTemplate
import io.heapy.kotbot.bot.challenge.templates.ScopeTransformTemplate
import io.heapy.kotbot.bot.challenge.templates.StringOperationsTemplate
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.bot.use_case.callback.CallbackDataServiceModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.configuration.JoinChallengeConfiguration
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.markdown.MarkdownModule

@Module
open class JoinChallengeModule(
    private val configurationModule: ConfigurationModule,
    private val kotbotModule: KotbotModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val daoModule: DaoModule,
    private val callbackDataServiceModule: CallbackDataServiceModule,
    private val markdownModule: MarkdownModule,
) {
    open val joinChallengeConfiguration: JoinChallengeConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = JoinChallengeConfiguration.serializer(),
                path = "joinChallenge",
            )
    }

    open val distractorGenerator: DistractorGenerator by lazy {
        DistractorGenerator()
    }

    open val challengeTemplates: List<ChallengeTemplate> by lazy {
        listOf(
            CollectionPipelineTemplate(distractorGenerator),
            StringOperationsTemplate(distractorGenerator),
            ScopeTransformTemplate(distractorGenerator),
            MapOperationsTemplate(distractorGenerator),
            RangeExpressionTemplate(distractorGenerator),
            PairDestructuringTemplate(distractorGenerator),
            NullSafetyTemplate(distractorGenerator),
        )
    }

    open val challengeGenerator: ChallengeGenerator by lazy {
        ChallengeGenerator(challengeTemplates)
    }

    open val joinSessionDao: JoinSessionDao by lazy {
        JoinSessionDao()
    }

    open val verifiedUserDao: VerifiedUserDao by lazy {
        VerifiedUserDao()
    }

    open val challengeAttemptDao: ChallengeAttemptDao by lazy {
        ChallengeAttemptDao()
    }

    open val resolvedJoinChallengeConfig: ResolvedJoinChallengeConfig by lazy {
        ResolvedJoinChallengeConfig(
            config = joinChallengeConfiguration,
            groupsConfig = kotlinChatBotConfigurationModule.groupsConfiguration,
        )
    }

    open val joinChallengeProcessor: JoinChallengeProcessor by lazy {
        JoinChallengeProcessor(
            kotbot = kotbotModule.kotbot,
            challengeGenerator = challengeGenerator,
            joinSessionDao = joinSessionDao,
            verifiedUserDao = verifiedUserDao,
            challengeAttemptDao = challengeAttemptDao,
            resolvedConfig = resolvedJoinChallengeConfig,
            callbackDataService = callbackDataServiceModule.callbackDataService,
            markdown = markdownModule.markdown,
        )
    }

    open val joinChallengeExpiryJob: JoinChallengeExpiryJob by lazy {
        JoinChallengeExpiryJob(
            joinSessionDao = joinSessionDao,
            kotbot = kotbotModule.kotbot,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
            markdown = markdownModule.markdown,
        )
    }

    open val existingMemberBootstrapJob: ExistingMemberBootstrapJob by lazy {
        ExistingMemberBootstrapJob(
            userContextDao = daoModule.userContextDao,
            verifiedUserDao = verifiedUserDao,
            jobExecutionDao = daoModule.jobExecutionDao,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
