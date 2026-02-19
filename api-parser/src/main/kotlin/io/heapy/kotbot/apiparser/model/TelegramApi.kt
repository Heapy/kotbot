package io.heapy.kotbot.apiparser.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class TelegramApi(
    val version: Version,
    val recent_changes: RecentChanges,
    val methods: List<Method>,
    val objects: List<Object>,
)

@Serializable
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}

@Serializable
data class RecentChanges(
    val year: Int,
    val month: Int,
    val day: Int,
)

@Serializable
data class Method(
    val name: String,
    val description: String,
    val multipart_only: Boolean,
    val documentation_link: String,
    val arguments: List<Argument>? = null,
    val return_type: ApiType
)

@Serializable(ApiTypeSerializer::class)
sealed interface ApiType

@Serializable
data class BooleanApiType(
    val type: String = "bool",
    val default: Boolean? = null,
) : ApiType

@Serializable
data class ReferenceApiType(
    val type: String = "reference",
    val reference: String,
) : ApiType

@Serializable
data class ArrayApiType(
    val type: String = "array",
    val array: ApiType,
) : ApiType

@Serializable
data class StringApiType(
    val type: String = "string",
) : ApiType

@Serializable
data class IntApiType(
    val type: String = "integer",
) : ApiType

@Serializable
data class AnyOfApiType(
    val type: String = "any_of",
    val any_of: List<ApiType>,
) : ApiType

object ArgumentSerializer : JsonContentPolymorphicSerializer<Argument>(Argument::class) {
    override fun selectDeserializer(element: JsonElement) =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "bool" -> BooleanArgument.serializer()
            "integer" -> IntArgument.serializer()
            "float" -> FloatArgument.serializer()
            "array" -> ArrayArgument.serializer()
            "string" -> StringArgument.serializer()
            "reference" -> ReferenceArgument.serializer()
            "any_of" -> AnyOfArgument.serializer()
            else -> error("Unknown argument type: $type")
        }
}

object ApiTypeSerializer : JsonContentPolymorphicSerializer<ApiType>(ApiType::class) {
    override fun selectDeserializer(element: JsonElement) =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "bool" -> BooleanApiType.serializer()
            "integer" -> IntApiType.serializer()
            "array" -> ArrayApiType.serializer()
            "string" -> StringApiType.serializer()
            "reference" -> ReferenceApiType.serializer()
            "any_of" -> AnyOfApiType.serializer()
            else -> error("Unknown argument type: $type")
        }
}

object ObjectSerializer : JsonContentPolymorphicSerializer<Object>(Object::class) {
    override fun selectDeserializer(element: JsonElement) =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            null -> EmptyObject.serializer()
            "properties" -> PropertiesObject.serializer()
            "any_of" -> AnyOfObject.serializer()
            else -> error("Unknown argument type: $type")
        }
}

@Serializable(ObjectSerializer::class)
sealed interface Object {
    val name: String
}

@Serializable
data class EmptyObject(
    override val name: String,
    val description: String,
    val documentation_link: String,
) : Object

@Serializable
data class PropertiesObject(
    override val name: String,
    val description: String,
    val documentation_link: String,
    val type: String = "properties",
    val properties: List<Argument>,
) : Object

@Serializable
data class AnyOfObject(
    override val name: String,
    val description: String,
    val documentation_link: String,
    val type: String = "any_of",
    val any_of: List<ApiType>,
) : Object

@Serializable(ArgumentSerializer::class)
sealed interface Argument {
    val type: String
    val name: String
    val description: String
    val required: Boolean
}

val Argument.nullable: Boolean get() = !required

@Serializable
data class ReferenceArgument(
    override val type: String = "reference",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val reference: String,
) : Argument

@Serializable
data class IntArgument(
    override val type: String = "integer",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val enumeration: List<Int>? = null,
    val default: Int? = null,
    val min: Int? = null,
    val max: Int? = null,
) : Argument

@Serializable
data class BooleanArgument(
    override val type: String = "bool",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val default: Boolean? = null,
) : Argument

@Serializable
data class StringArgument(
    override val type: String = "string",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val min_len: Int? = null,
    val max_len: Int? = null,
    val enumeration: List<String>? = null,
    val default: String? = null,
) : Argument

@Serializable
data class ArrayArgument(
    override val type: String = "array",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val array: ApiType,
) : Argument

@Serializable
data class FloatArgument(
    override val type: String = "float",
    override val name: String,
    override val description: String,
    override val required: Boolean,
) : Argument

@Serializable
data class AnyOfArgument(
    override val type: String = "any_of",
    override val name: String,
    override val description: String,
    override val required: Boolean,
    val any_of: List<ApiType>,
) : Argument
