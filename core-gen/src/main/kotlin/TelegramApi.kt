import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun main() {
    val apiJson = {}::class.java
        .getResource("custom.json")
        .readText()

    Json.decodeFromString(TelegramApi.serializer(), apiJson)
        .generate()
}

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

@Serializable(ArgumentSerializer::class)
sealed class Argument

@Serializable
data class IntArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val default: Int? = null,
    val min: Int? = null,
    val max: Int? = null,
) : Argument()

@Serializable
data class FloatArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
) : Argument()

@Serializable
data class StringArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val default: String? = null,
    val min_len: Int? = null,
    val max_len: Int? = null,
    val enumeration: List<String>? = null,
) : Argument()

@Serializable
data class BooleanArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val default: Boolean? = null,
) : Argument()

@Serializable
data class ArrayArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val array: ApiType,
) : Argument()

@Serializable
data class ReferenceArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val reference: String,
) : Argument()

@Serializable
data class AnyOfArgument(
    val name: String,
    val description: String,
    val type: String,
    val required: Boolean,
    val any_of: List<ApiType>,
) : Argument()

@Serializable(ApiTypeSerializer::class)
sealed class ApiType

@Serializable
data class BooleanApiType(
    val type: String,
    val default: Boolean? = null,
) : ApiType()

@Serializable
data class ReferenceApiType(
    val type: String,
    val reference: String,
) : ApiType()

@Serializable
data class ArrayApiType(
    val type: String,
    val array: ApiType,
) : ApiType()

@Serializable
data class BasicApiType(
    val type: String,
) : ApiType()

@Serializable
data class AnyOfApiType(
    val type: String,
    val any_of: List<ApiType>,
) : ApiType()

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
            "integer" -> BasicApiType.serializer()
            "array" -> ArrayApiType.serializer()
            "string" -> BasicApiType.serializer()
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

object PropertySerializer : JsonContentPolymorphicSerializer<Property>(Property::class) {
    override fun selectDeserializer(element: JsonElement) =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "integer" -> IntProperty.serializer()
            "string" -> StringProperty.serializer()
            "bool" -> BooleanProperty.serializer()
            "reference" -> ReferenceProperty.serializer()
            "array" -> ArrayProperty.serializer()
            "float" -> FloatProperty.serializer()
            "any_of" -> AnyOfProperty.serializer()
            else -> error("Unknown argument type: $type")
        }
}

@Serializable(ObjectSerializer::class)
sealed class Object

@Serializable
data class EmptyObject(
    val name: String,
    val description: String,
    val documentation_link: String,
) : Object()

@Serializable
data class PropertiesObject(
    val name: String,
    val description: String,
    val documentation_link: String,
    val type: String,
    val properties: List<Property>,
) : Object()

@Serializable
data class AnyOfObject(
    val name: String,
    val description: String,
    val documentation_link: String,
    val type: String,
    val any_of: List<ApiType>,
) : Object()

@Serializable(PropertySerializer::class)
sealed class Property

@Serializable
data class ReferenceProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val reference: String,
) : Property()

@Serializable
data class IntProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val default: Int? = null,
) : Property()

@Serializable
data class BooleanProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val default: Boolean? = null,
) : Property()

@Serializable
data class StringProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val min_len: Int? = null,
    val max_len: Int? = null,
    val enumeration: List<String>? = null,
    val default: String? = null,
) : Property()

@Serializable
data class ArrayProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val array: ApiType,
) : Property()

@Serializable
data class FloatProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
) : Property()

@Serializable
data class AnyOfProperty(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: String,
    val any_of: List<ApiType>,
) : Property()
