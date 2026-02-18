package io.heapy.kotbot.bot.service

import io.heapy.kotbot.WithMockTransaction
import io.heapy.kotbot.bot.BanUserInReplyCallbackData
import io.heapy.kotbot.bot.model.Chat
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.use_case.callback.createFlattenCallbackDataServiceModule
import io.heapy.kotbot.database.tables.pojos.CallbackData
import io.heapy.kotbot.infra.jdbc.MockTransactionContext
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jooq.JSONB
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

@WithMockTransaction
class CallbackDataServiceTest {
    context(_: MockTransactionContext)
    @Test
    fun getById() = runTest {
        // prepare
        val created = LocalDateTime.now()
        val uuid = UUID.fromString("60ed0b49-659a-4dec-b799-11e7319b7ff9")
        val json =
            """{"type":"io.heapy.kotbot.bot.BanUserInReplyCallbackData","message":{"message_id":1,"date":0,"chat":{"id":1,"type":"group"}},"reason":"Spam"}"""
        val modules = createFlattenCallbackDataServiceModule {
            daoModule {
                callbackDataDao {
                    mockk {
                        coEvery {
                            getOnceById(uuid)
                        } returns CallbackData(
                            id = uuid,
                            data = JSONB.valueOf(json),
                            created = created,
                        )
                    }
                }
            }
        }

        val dao = modules.daoModule.callbackDataDao
        val service = modules.callbackDataServiceModule.callbackDataService

        val callbackData = BanUserInReplyCallbackData(
            message = Message(
                message_id = 1,
                date = 0,
                chat = Chat(
                    id = 1,
                    type = "group",
                ),
            ),
            reason = "Spam",
        )

        // act
        val data = service.getById(
            id = uuid.toString(),
        )

        assertEquals(
            callbackData,
            data
        )

        // verify
        coVerifySequence {
            dao.getOnceById(id = uuid)
        }

        confirmVerified(dao)

    }

    context(_: MockTransactionContext)
    @Test
    fun insert() = runTest {
        // prepare
        val uuid = UUID.fromString("60ed0b49-659a-4dec-b799-11e7319b7ff9")
        val json =
            """{"type":"io.heapy.kotbot.bot.BanUserInReplyCallbackData","message":{"message_id":1,"date":0,"chat":{"id":1,"type":"group"}},"reason":"Spam"}"""
        val modules = createFlattenCallbackDataServiceModule {
            daoModule {
                callbackDataDao {
                    mockk {
                        coEvery {
                            insert(jsonData = json)
                        } returns uuid
                    }
                }
            }
        }

        val dao = modules.daoModule.callbackDataDao
        val service = modules.callbackDataServiceModule.callbackDataService

        val message = Message(
            message_id = 1,
            date = 0,
            chat = Chat(
                id = 1,
                type = "group",
            ),
        )

        // act
        val id = service.insert(
            BanUserInReplyCallbackData(
                message = message,
                reason = "Spam",
            )
        )

        assertEquals(
            uuid.toString(),
            id
        )

        // verify
        coVerifySequence {
            dao.insert(jsonData = json)
        }

        confirmVerified(dao)
    }
}
