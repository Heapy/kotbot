import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.io.path.Path

private val debug = false

fun TelegramApi.generate() {
    val supertypes = objects.filterIsInstance<AnyOfObject>()
        .flatMap { anyOf ->
            anyOf.any_of
                .map { apiType -> apiType as ReferenceApiType }
                .map { apiType -> apiType.reference }
                .associateWith { anyOf.name }
                .map { it.key to it.value }
        }
        .toMap()

    val objectsFiles = objects.map { it.toFileSpec(supertypes) }
    val helperFiles = helpers.map(ClassName::toFileSpec).takeIf { debug } ?: listOf()

    val anyOfArgumentsFiles = objects.filterIsInstance<PropertiesObject>()
        .flatMap(PropertiesObject::toAnyOfArguments)
        .distinct()
        .map(AnyOfArgument::toFileSpec)

    (anyOfArgumentsFiles + helperFiles + objectsFiles).forEach {
        it.writeTo(Path("core", "src", "generated", "kotlin"))
    }
}

private val basePackageName = "io.heapy.kotbot.bot"
private val modelPackageName = "$basePackageName.model"

private val serializableAnnotation = ClassName("kotlinx.serialization", "Serializable")
private val booleanType = ClassName("kotlin", "Boolean")
private val intType = ClassName("kotlin", "Int")
private val stringType = ClassName("kotlin", "String")
private val longType = ClassName("kotlin", "Long")

private val emptyObjectAnnotation = ClassName(basePackageName, "EmptyObject")
private val anyOfObjectAnnotation = ClassName(basePackageName, "AnyOfObject")
private val anyOfObjectArgument = ClassName(basePackageName, "AnyOfArgument")

private val helpers = listOf(
    emptyObjectAnnotation,
    anyOfObjectAnnotation,
    anyOfObjectArgument,
)

private fun PropertiesObject.toAnyOfArguments() =
    properties.filterIsInstance<AnyOfArgument>()

private fun AnyOfArgument.toFileSpec() =
    FileSpec.builder(modelPackageName, className())
        .addType(
            TypeSpec.interfaceBuilder(className())
                .addModifiers(KModifier.SEALED)
                .whenDebug { addAnnotation(anyOfObjectArgument) }
                .addAnnotation(
                    AnnotationSpec.builder(serializableAnnotation)
                        .addMember("with = %T::class", ClassName(basePackageName, "${className()}Serializer"))
                        .build()
                )
                .build()
        )
        .apply {
            any_of.forEach { type ->
                when(type) {
                    is IntApiType -> addType(
                        TypeSpec.classBuilder("Int${className()}")
                            .addModifiers(KModifier.VALUE)
                            .addAnnotation(JvmInline::class)
                            .addAnnotation(serializableAnnotation)
                            .addSuperinterface(ClassName(modelPackageName, className()))
                            .addProperty(
                                PropertySpec.builder("value", intType)
                                    .initializer("value")
                                    .build()
                            )
                            .primaryConstructor(
                                FunSpec.constructorBuilder()
                                    .addParameter("value", intType)
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

private fun ClassName.toFileSpec(): FileSpec =
    FileSpec.builder(packageName, simpleName)
        .addType(
            TypeSpec.annotationBuilder(simpleName)
                .build()
        )
        .build()

fun <T> T.whenDebug(apply: T.() -> T): T = if (debug) apply() else this

private fun Object.toFileSpec(
    supertypes: Map<String, String>,
): FileSpec {
    return FileSpec.builder(modelPackageName, name)
        .apply {
            val type = when (val obj = this@toFileSpec) {
                is AnyOfObject -> TypeSpec.interfaceBuilder(obj.name)
                    .addModifiers(KModifier.SEALED)
                    .addAnnotation(
                        AnnotationSpec.builder(serializableAnnotation)
                            .addMember("with = %T::class", ClassName(basePackageName, "${obj.name}Serializer"))
                            .build()
                    )
                    .whenDebug { addAnnotation(anyOfObjectAnnotation) }
                    .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                    .build()

                is EmptyObject -> TypeSpec.classBuilder(obj.name)
                    .addAnnotation(serializableAnnotation)
                    .whenDebug { addAnnotation(emptyObjectAnnotation) }
                    .addKdoc(CodeBlock.of(obj.description.asKdoc()))
                    .build()

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
            }
            addType(type)
        }
        .build()
}

private fun Argument.propertyInitializer(): CodeBlock = CodeBlock.of(name)

private fun AnyOfArgument.className(): String {
    return name.split('_')
        .joinToString(separator = "") { part ->
            part.replaceFirstChar { char ->
                char.uppercaseChar()
            }
        }
}

/**
 * Replaces spaces with non-breaking spaces.
 * Replaces control symbols with html entities.
 */
private fun String.asKdoc(): String {
    return replace(' ', '·')
        .replace("»", "&raquo;")
        .replace("«", "&laquo;")
}

private fun Argument.asPropertySpec(): PropertySpec {
    return when (val arg = this) {
        is AnyOfArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.className())
                        .copy(nullable = nullable)
                )
                .whenDebug { addAnnotation(anyOfObjectArgument) }
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is ArrayArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName("kotlin.collections", "List")
                        .parameterizedBy(when (arg.array) {
                            is AnyOfApiType -> ClassName(modelPackageName, "AnyOfTodo")
                            is ArrayApiType -> when(val sub = arg.array.array) {
                                is AnyOfApiType -> ClassName(modelPackageName, "AnyOfTodo")
                                is ArrayApiType -> TODO("recursive")
                                is StringApiType -> stringType
                                is IntApiType -> intType
                                is BooleanApiType -> booleanType
                                is ReferenceApiType -> ClassName(modelPackageName, sub.reference)
                            }
                            is StringApiType -> stringType
                            is IntApiType -> intType
                            is BooleanApiType -> booleanType
                            is ReferenceApiType -> ClassName(modelPackageName, arg.array.reference)
                        })
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is BooleanArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = booleanType
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is FloatArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName("kotlin", "Double")
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is IntArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = if (description.contains("64-bit")) longType.copy(nullable = nullable)
                    else intType.copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is ReferenceArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.reference)
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()

        is StringArgument ->
            PropertySpec
                .builder(
                    name = name,
                    type = ClassName("kotlin", "String")
                        .copy(nullable = nullable)
                )
                .addKdoc(description.asKdoc())
                .initializer(propertyInitializer())
                .build()
    }
}

private fun Argument.asParameterSpec(): ParameterSpec {
    return when (val arg = this) {
        is AnyOfArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.className())
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is ArrayArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName("kotlin.collections", "List")
                        .parameterizedBy(when (arg.array) {
                            is AnyOfApiType -> ClassName(modelPackageName, "AnyOfTodo")
                            is ArrayApiType -> when (val sub = arg.array.array) {
                                is AnyOfApiType -> ClassName(modelPackageName, "AnyOfTodo")
                                is ArrayApiType -> TODO("recursive")
                                is StringApiType -> stringType
                                is IntApiType -> intType
                                is BooleanApiType -> booleanType
                                is ReferenceApiType -> ClassName(modelPackageName, sub.reference)
                            }

                            is StringApiType -> stringType
                            is IntApiType -> intType
                            is BooleanApiType -> booleanType
                            is ReferenceApiType -> ClassName(modelPackageName, arg.array.reference)
                        })
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is BooleanArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = booleanType
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is FloatArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName("kotlin", "Double")
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is IntArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = if (description.contains("64-bit")) longType.copy(nullable = nullable)
                    else intType.copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is ReferenceArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName(modelPackageName, arg.reference)
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()

        is StringArgument ->
            ParameterSpec
                .builder(
                    name = name,
                    type = ClassName("kotlin", "String")
                        .copy(nullable = nullable)
                )
                .apply { if (nullable) defaultValue("null") }
                .build()
    }
}
