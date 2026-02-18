package io.heapy.kotbot.bot

import io.heapy.komok.tech.config.dotenv.dotenv
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

/**
 * Parameter resolver for [KotbotVerifier] class.
 * This resolver injects a single instance of [KotbotVerifier] for all tests
 * and closes it after all tests are finished.
 */
class KotbotVerifierParameterResolver : ParameterResolver {
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        return parameterContext.parameter.type == KotbotVerifier::class.java
    }

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any {
        return extensionContext
            .root
            .getStore(NAMESPACE)
            .computeIfAbsent(KEY) {
                val dotenv = dotenv().properties
                val kotbot = Kotbot(
                    token = dotenv.getValue("KOTBOT_TOKEN")
                )
                val qaUserId = dotenv.getValue("KOTBOT_QA_USER_ID").toLong()

                KotbotVerifier(
                    env = dotenv,
                    kotbot = kotbot,
                    qaUserId = qaUserId,
                )
            }
    }

    private companion object {
        private val NAMESPACE = ExtensionContext.Namespace.create(KotbotVerifierParameterResolver::class.java)
        private const val KEY = "kotbotVerifier"
    }
}
