package io.heapy.kotbot.bot

/**
 * Configuration required for TG bot.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
public interface BotConfiguration {
    public val token: String
    public val name: String
}

public interface CasConfiguration {
    public val allowlist: Set<Long>
}

public interface FamilyConfiguration {
    public val ids: Set<Long>
    public val admins: Map<String, List<Long>>
}
