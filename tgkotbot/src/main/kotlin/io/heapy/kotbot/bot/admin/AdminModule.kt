package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.openai.GptApiModule

@Module
open class AdminModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
    private val gptApiModule: GptApiModule,
) {
    open val userNoteService by lazy {
        UserNoteService(
            gptApi = gptApiModule.gptApi,
            updateDao = daoModule.updateDao,
            userContextDao = daoModule.userContextDao,
            transactionProvider = jdbcModule.transactionProvider,
        )
    }

    open val route by lazy {
        AdminRoute(
            userContextDao = daoModule.userContextDao,
            transactionProvider = jdbcModule.transactionProvider,
            userNoteService = userNoteService,
        )
    }
}