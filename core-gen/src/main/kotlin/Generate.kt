import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

val packageName = "io.heapy.kotbot.bot"
val serializable = ClassName("kotlinx.serialization", "Serializable")

fun TelegramApi.generate() {
    val greeterClass = ClassName(packageName, "Greeter")

    val fileBuilder = FileSpec.builder(packageName, "_model")
        .addObjects()
        .addFunction(
            FunSpec.builder("main")
                .addAnnotation(serializable)
                .addParameter("args", String::class, KModifier.VARARG)
                .addStatement("%T(args[0]).greet()", greeterClass)
                .build()
        )
        .build()

    fileBuilder.writeTo(System.out)
}

context(TelegramApi)
private fun FileSpec.Builder.addObjects(): FileSpec.Builder {
    objects
        .map { o ->
            when (o) {
                is AnyOfObject -> TypeSpec.classBuilder(o.name)
                    .addAnnotation(serializable)
                    .addKdoc(CodeBlock.of(o.description))
                    .build()

                is EmptyObject -> TypeSpec.classBuilder(o.name)
                    .addAnnotation(serializable)
                    .addKdoc(CodeBlock.of(o.description))
                    .build()

                is PropertiesObject -> TypeSpec.classBuilder(o.name)
                    .addAnnotation(serializable)
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .also { b ->
                                o.properties.forEach { p ->
                                    when(p) {
                                        is AnyOfProperty -> b.addParameter(p.name, String::class)
                                        is ArrayProperty -> b.addParameter(p.name, String::class)
                                        is BooleanProperty -> b.addParameter(p.name, Boolean::class)
                                        is FloatProperty -> b.addParameter(p.name, Float::class)
                                        is IntProperty -> b.addParameter(p.name, Int::class)
                                        is ReferenceProperty -> b.addParameter(p.name, ClassName(packageName, p.reference))
                                        is StringProperty -> b.addParameter(p.name, String::class)
                                    }
                                }
                            }
                            .build()
                    )
                    .addKdoc(CodeBlock.of(o.description))
                    .build()
            }
        }
        .forEach(::addType)

    return this
}
