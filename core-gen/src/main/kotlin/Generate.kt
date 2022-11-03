@file:JvmName("Generate")

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.json.Json
import kotlin.io.path.Path

fun main() {
    // https://ark0f.github.io/tg-bot-api/custom.json
    val apiJson = {}::class.java
        .getResource("custom.json")
        ?.readText()
        ?: error("custom.json not found")

    Json.decodeFromString(TelegramApi.serializer(), apiJson)
        .generate()
}

fun TelegramApi.generate() {
    val supertypeMapping = objects.filterIsInstance<AnyOfObject>()
        .flatMap { anyOf ->
            anyOf.any_of
                .map { apiType -> apiType as ReferenceApiType }
                .map { apiType -> apiType.reference }
                .associateWith { anyOf.name }
                .map { it.key to it.value }
        }
        .toMap()

    val methodFiles = methods.map(Method::toFileSpec)

    val objectsFiles = objects.map { it.toFileSpec(supertypeMapping) }

    val anyOfArgumentsFiles = objects.filterIsInstance<PropertiesObject>()
        .flatMap(PropertiesObject::toAnyOfArguments)
        .distinct()
        .map(AnyOfArgument::toFileSpec)

    val anyOfApiTypeFiles = methods.toAnyOfApiTypes()
        .distinct()
        .map(AnyOfApiType::toFileSpec)

    (anyOfApiTypeFiles + anyOfArgumentsFiles + objectsFiles + methodFiles).forEach {
        it.writeTo(Path("core", "src", "generated", "kotlin"))
    }
}

private val basePackageName = "io.heapy.kotbot.bot"
private val modelPackageName = "$basePackageName.model"
private val methodPackageName = "$basePackageName.method"

private val botMethodType = ClassName("io.heapy.kotbot.bot", "Method")
private val kotbotType = ClassName("io.heapy.kotbot.bot", "Kotbot")
private val responseType = ClassName("io.heapy.kotbot.bot", "Response")
private val jvmInlineAnnotation = ClassName("kotlin.jvm", "JvmInline")
private val serializableAnnotation = ClassName("kotlinx.serialization", "Serializable")
private val kSerializerType = ClassName("kotlinx.serialization", "KSerializer")
private val listSerializerType = ClassName("kotlinx.serialization.builtins", "ListSerializer")
private val listType = ClassName("kotlin.collections", "List")
private val stringType = ClassName("kotlin", "String")
private val booleanType = ClassName("kotlin", "Boolean")
private val intType = ClassName("kotlin", "Int")
private val longType = ClassName("kotlin", "Long")
private val doubleType = ClassName("kotlin", "Double")

private val knownApiTypes = mapOf(
    AnyOfApiType(
        any_of = listOf(
            ReferenceApiType(
                reference = "Message",
            ),
            BooleanApiType(
                default = true,
            )
        )
    ) to ClassName(modelPackageName, "MessageOrTrue"),
)

private fun PropertiesObject.toAnyOfArguments() =
    properties.filterIsInstance<AnyOfArgument>()

private fun List<Method>.toAnyOfApiTypes() =
    map { it.return_type }
        .filterIsInstance<AnyOfApiType>()

private fun AnyOfArgument.toFileSpec() =
    FileSpec.builder(modelPackageName, className())
        .addType(
            TypeSpec.interfaceBuilder(className())
                .addModifiers(KModifier.SEALED)
                .addAnnotation(
                    AnnotationSpec.builder(serializableAnnotation)
                        .addMember("with = %T::class", ClassName(basePackageName, "${className()}Serializer"))
                        .build()
                )
                .build()
        )
        .apply {
            any_of.forEach { type ->
                when (type) {
                    is IntApiType -> addType(
                        TypeSpec.classBuilder("Long${className()}")
                            .addModifiers(KModifier.VALUE)
                            .addAnnotation(JvmInline::class)
                            .addAnnotation(serializableAnnotation)
                            .addSuperinterface(ClassName(modelPackageName, className()))
                            .addProperty(
                                PropertySpec.builder("value", longType)
                                    .initializer("value")
                                    .build()
                            )
                            .primaryConstructor(
                                FunSpec.constructorBuilder()
                                    .addParameter("value", longType)
                                    .build()
                            )
                            .build()
                    )

                    is StringApiType -> addType(
                        TypeSpec.classBuilder("String${className()}")
                            .addModifiers(KModifier.VALUE)
                            .addAnnotation(JvmInline::class)
                            .addAnnotation(serializableAnnotation)
                            .addSuperinterface(ClassName(modelPackageName, className()))
                            .addProperty(
                                PropertySpec.builder("value", stringType)
                                    .initializer("value")
                                    .build()
                            )
                            .primaryConstructor(
                                FunSpec.constructorBuilder()
                                    .addParameter("value", stringType)
                                    .build()
                            )
                            .build()
                    )

                    is ReferenceApiType -> addType(
                        TypeSpec.classBuilder("${type.reference}${className()}")
                            .addModifiers(KModifier.VALUE)
                            .addAnnotation(JvmInline::class)
                            .addAnnotation(serializableAnnotation)
                            .addSuperinterface(ClassName(modelPackageName, className()))
                            .addProperty(
                                PropertySpec.builder("value", ClassName(modelPackageName, type.reference))
                                    .initializer("value")
                                    .build()
                            )
                            .primaryConstructor(
                                FunSpec.constructorBuilder()
                                    .addParameter("value", ClassName(modelPackageName, type.reference))
                                    .build()
                            )
                            .build()
                    )

                    else -> error("Unsupported type $type")
                }
            }
        }
        .build()

private fun AnyOfApiType.toFileSpec() =
    knownApiTypes[this]?.let { className ->
        FileSpec.builder(className.packageName, className.simpleName)
            .addType(
                TypeSpec.interfaceBuilder(className.simpleName)
                    .addModifiers(KModifier.SEALED)
                    .addAnnotation(
                        AnnotationSpec.builder(serializableAnnotation)
                            .addMember("with = %T::class", ClassName(basePackageName, "${className.simpleName}Serializer"))
                            .build()
                    )
                    .build()
            )
            .apply {
                when (className.simpleName) {
                    "MessageOrTrue" -> {
                        addType(
                            TypeSpec.classBuilder("MessageValue")
                                .addModifiers(KModifier.VALUE)
                                .addAnnotation(JvmInline::class)
                                .addAnnotation(serializableAnnotation)
                                .addSuperinterface(className)
                                .addProperty(
                                    PropertySpec.builder("value", ClassName(modelPackageName, "Message"))
                                        .initializer("value")
                                        .build()
                                )
                                .primaryConstructor(
                                    FunSpec.constructorBuilder()
                                        .addParameter("value", ClassName(modelPackageName, "Message"))
                                        .build()
                                )
                                .build()
                        )

                        addType(
                            TypeSpec.classBuilder("BooleanValue")
                                .addModifiers(KModifier.VALUE)
                                .addAnnotation(JvmInline::class)
                                .addAnnotation(serializableAnnotation)
                                .addSuperinterface(className)
                                .addProperty(
                                    PropertySpec.builder("value", booleanType)
                                        .initializer("value")
                                        .build()
                                )
                                .primaryConstructor(
                                    FunSpec.constructorBuilder()
                                        .addParameter("value", booleanType)
                                        .build()
                                )
                                .build()
                        )
                    }

                    else -> error("Unsupported type $className")
                }
            }
            .build()
    } ?: error("Unknown type $this")

private fun Object.toFileSpec(
    supertypes: Map<String, String>,
): FileSpec {
    return FileSpec.builder(modelPackageName, name)
        .addType(when (val obj = this) {
            is AnyOfObject -> TypeSpec.interfaceBuilder(obj.name)
                .addModifiers(KModifier.SEALED)
                .addAnnotation(
                    AnnotationSpec.builder(serializableAnnotation)
                        .addMember("with = %T::class", ClassName(basePackageName, "${obj.name}Serializer"))
                        .build()
                )
                .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                .build()

            is EmptyObject -> when (name) {
                "InputFile" -> TypeSpec.classBuilder(obj.name)
                    .addAnnotation(serializableAnnotation)
                    .addAnnotation(jvmInlineAnnotation)
                    .addModifiers(KModifier.VALUE)
                    .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                    .addProperty(
                        PropertySpec.builder("value", stringType)
                            .initializer("value")
                            .build()
                    )
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("value", stringType)
                            .build()
                    )
                    .build()

                else -> TypeSpec.classBuilder(obj.name)
                    .addAnnotation(serializableAnnotation)
                    .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                    .build()
            }

            is PropertiesObject -> TypeSpec.classBuilder(obj.name)
                .addAnnotation(serializableAnnotation)
                .addModifiers(KModifier.DATA)
                .apply {
                    supertypes[obj.name]?.let { supertype ->
                        addSuperinterface(ClassName(modelPackageName, supertype))
                    }
                }
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameters(obj.properties.map(Argument::asParameterSpec))
                        .build()
                )
                .addProperties(obj.properties.map(Argument::asPropertySpec))
                .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                .build()
        })
        .build()
}

private fun Argument.propertyInitializer(): CodeBlock =
    CodeBlock.of(name)

private fun AnyOfArgument.className(): String =
    name.snakeToTitle()

private fun String.snakeToTitle(): String =
    split('_')
        .joinToString(separator = "") { part ->
            part.replaceFirstChar(Char::uppercaseChar)
        }

private fun String.camelToTitle(): String = replaceFirstChar(Char::uppercaseChar)

/**
 * Replaces spaces with non-breaking spaces.
 * Replaces control symbols with html entities.
 */
private fun String.asKdoc(): String =
    replace("\\_", "_")
        .replace(' ', '·')
        .replace("»", "&raquo;")
        .replace("«", "&laquo;")

private fun ArrayArgument.generic(): TypeName {
    return when (val type = array) {
        is AnyOfApiType -> ClassName(modelPackageName, when (name) {
            "media" -> "InputMedia"
            else -> name.snakeToTitle()
        })
        is StringApiType -> stringType
        is IntApiType -> intType
        is BooleanApiType -> booleanType
        is ReferenceApiType -> ClassName(modelPackageName, type.reference)
        is ArrayApiType -> listType
            .parameterizedBy(type.array.generic())
    }
}

private fun ApiType.generic(): TypeName {
    return when (val type = this) {
        is AnyOfApiType -> knownApiTypes[type] ?: error("Unknown type $type")
        is BooleanApiType -> booleanType
        is IntApiType -> intType
        is ReferenceApiType -> ClassName(modelPackageName, type.reference)
        is StringApiType -> stringType
        is ArrayApiType -> listType
            .parameterizedBy(type.array.generic())
    }
}

private fun Argument.asPropertySpec(): PropertySpec =
    when (val arg = this) {
        is AnyOfArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, when (name) {
                        "from_chat_id" -> "ChatId"
                        "photo", "png_sticker" -> "InputFile"
                        else -> arg.className()
                    }).copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is ArrayArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = listType
                        .parameterizedBy(arg.generic())
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is BooleanArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = booleanType.copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is FloatArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = doubleType.copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is IntArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = if (arg.isActuallyLong()) longType.copy(nullable = nullable)
                    else intType.copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is ReferenceArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.reference).copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is StringArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = stringType.copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()
    }

private fun Argument.asParameterSpec(): ParameterSpec {
    return when (val arg = this) {
        is AnyOfArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, when (name) {
                        "from_chat_id" -> "ChatId"
                        "photo", "png_sticker" -> "InputFile"
                        else -> arg.className()
                    }).copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is ArrayArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = listType
                        .parameterizedBy(arg.generic())
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is BooleanArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = booleanType.copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .apply { if (arg.default != null) defaultValue("%L", arg.default) }
                .build()

        is FloatArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = doubleType.copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is IntArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = if (arg.isActuallyLong()) longType.copy(nullable = nullable)
                    else intType.copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .apply { if (arg.default != null) defaultValue("%L", arg.default) }
                .build()

        is ReferenceArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.reference).copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is StringArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = stringType.copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .apply { if (arg.default != null) defaultValue("%S", arg.default) }
                .build()
    }
}

private fun IntArgument.isActuallyLong(): Boolean =
    when {
        name == "file_size" -> true
        name == "user_id" -> true
        name.contains("chat_id") -> true
        description.contains("64-bit") -> true
        description.contains("Unix time", ignoreCase = true) -> true
        else -> false
    }

private fun ApiType.serializer(): CodeBlock {
    return when (val type = this) {
        is AnyOfApiType -> knownApiTypes[type]?.let { CodeBlock.of("%T.serializer()", it) }
            ?: error("Unknown type $type")
        is BooleanApiType -> CodeBlock.of("%T.serializer()", booleanType)
        is IntApiType -> CodeBlock.of("%T.serializer()", intType)
        is ReferenceApiType -> CodeBlock.of("%T.serializer()", ClassName(modelPackageName, type.reference))
        is StringApiType -> CodeBlock.of("%T.serializer()", stringType)
        is ArrayApiType -> CodeBlock.of("%T(%L)", listSerializerType, type.array.serializer())
    }
}

private fun Method.toFileSpec(): FileSpec =
    FileSpec.builder(methodPackageName, name.camelToTitle())
        .addImport("io.heapy.kotbot.bot", "requestForJson", "unwrap")
        .addImport("io.ktor.client.statement", "bodyAsText")
        .addImport("kotlinx.serialization.builtins", "serializer")
        .addType(
            TypeSpec.classBuilder(name.camelToTitle())
                .addAnnotation(serializableAnnotation)
                .apply {
                    arguments?.let {
                        addModifiers(KModifier.DATA)
                            .primaryConstructor(
                                FunSpec.constructorBuilder()
                                    .addParameters(arguments.map(Argument::asParameterSpec))
                                    .build()
                            )
                            .addProperties(arguments.map(Argument::asPropertySpec))
                    }
                }
                .addSuperinterface(botMethodType.parameterizedBy(return_type.generic()))
                .addKdoc(CodeBlock.of(description.asKdoc()))
                .addFunction(
                    FunSpec.builder("execute")
                        .addModifiers(
                            KModifier.OVERRIDE,
                            KModifier.SUSPEND,
                        )
                        .receiver(kotbotType)
                        .returns(return_type.generic())
                        .addCode(
                            CodeBlock.builder()
                                .add("return requestForJson(\n")
                                .indent()
                                    .add("name = %S,\n", name)
                                    .add("serialize = {\n")
                                    .indent()
                                        .add("json.encodeToString(\n")
                                        .indent()
                                            .add("serializer(),\n")
                                            .add("this@%N\n", name.camelToTitle())
                                        .unindent()
                                        .add(")\n")
                                    .unindent()
                                    .add("},\n")
                                    .add("deserialize = {\n")
                                    .indent()
                                        .add("json.decodeFromString(deserializer, it.bodyAsText()).unwrap()\n")
                                    .unindent()
                                    .add("}\n")
                                .unindent()
                                .add(")\n")
                                .build()
                        )
                        .build()
                )
                .addType(
                    TypeSpec.companionObjectBuilder()
                        .addProperty(
                            PropertySpec
                                .builder(
                                    "deserializer",
                                    kSerializerType.parameterizedBy(
                                        responseType.parameterizedBy(return_type.generic())
                                    ),
                                )
                                .initializer(
                                    CodeBlock.of(
                                        "Response.serializer(%L)",
                                        return_type.serializer()
                                    )
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        )
        .build()
