package io.heapy.kotbot.bot

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

public interface ApiMethod<Req, Res> {
    public val name: String
    public val serializer: KSerializer<Req>
    public val deserializer: KSerializer<Res>
    public val request: Req
}

@Serializable
public data class ApiResponse<T>(
    public val ok: Boolean,
    public val result: T? = null,
    public val error_code: Int? = null,
    public val description: String? = null,
    public val parameters: ResponseParameters? = null,
)

@Serializable
public data class ResponseParameters(
    public val migrate_to_chat_id: Long?,
    public val retry_after: Int?,
)

@Serializable
public class GetUpdates(
    public val offset: Int? = null,
    public val limit: Int? = null,
    public val timeout: Int? = null,
    public val allowed_updates: List<String>? = null,
) : ApiMethod<GetUpdates.Request, List<ApiUpdate>> {
    @Serializable
    public data class Request(
        val offset: Int? = null,
        val limit: Int? = null,
        val timeout: Int? = null,
        val allowed_updates: List<String>? = null,
    )
    override val name: String = "getUpdates"
    override val deserializer: KSerializer<List<ApiUpdate>> = ListSerializer(ApiUpdate.serializer())
    override val serializer: KSerializer<Request> = Request.serializer()
    override val request: Request
        get() = Request(offset, limit, timeout, allowed_updates)
}

@Serializable
public class GetMe : ApiMethod<GetMe.Request, User> {
    @Serializable
    public class Request

    override val name: String = "getMe"
    override val deserializer: KSerializer<User> = User.serializer()
    override val serializer: KSerializer<Request> = Request.serializer()
    override val request: Request
        get() = Request()
}
