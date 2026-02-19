package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.*

object HtmlApiParser {
    fun parse(html: String): TelegramApi {
        val extracted = HtmlExtractor.extract(html)
        return convert(extracted)
    }

    private fun convert(extracted: ExtractedApi): TelegramApi {
        val version = parseVersion(extracted.versionText)
        val recentChanges = parseRecentChanges(extracted.recentChangesText)

        val methods = extracted.methods.map { convertMethod(it) }
        val objects = extracted.objects.map { convertObject(it) }

        return TelegramApi(
            version = version,
            recent_changes = recentChanges,
            methods = methods,
            objects = objects,
        )
    }

    private fun parseVersion(text: String): Version {
        val digits = text.dropWhile { !it.isDigit() }.trimEnd('.')
        val parts = digits.split('.')
        return Version(
            major = parts[0].toInt(),
            minor = parts[1].toInt(),
            patch = 0,
        )
    }

    private fun parseRecentChanges(text: String): RecentChanges {
        val months = mapOf(
            "January" to 1, "February" to 2, "March" to 3, "April" to 4,
            "May" to 5, "June" to 6, "July" to 7, "August" to 8,
            "September" to 9, "October" to 10, "November" to 11, "December" to 12,
        )
        // Format: "February 12, 2025"
        val parts = text.trim().split(" ")
        val month = months[parts[0]] ?: error("Unknown month: ${parts[0]}")
        val day = parts[1].trimEnd(',').toInt()
        val year = parts[2].toInt()
        return RecentChanges(year = year, month = month, day = day)
    }

    private fun convertMethod(raw: RawMethod): Method {
        val name = raw.name
        val descriptionHtml = raw.descriptionElements.joinToString("\n") { it.outerHtml() }
        val description = MarkdownConverter.convert(descriptionHtml)

        val returnType = ReturnTypeExtractor.extract(raw.descriptionElements)

        val args = raw.arguments.map { convertArgument(it) }
        val arguments = args.ifEmpty { null }

        val multipartOnly = arguments?.any { arg ->
            argTypeContainsInputFile(arg)
        } ?: false

        return Method(
            name = name,
            description = description,
            multipart_only = multipartOnly,
            documentation_link = raw.documentationLink,
            arguments = arguments,
            return_type = returnType,
        )
    }

    private fun argTypeContainsInputFile(arg: Argument): Boolean {
        return when (arg) {
            is ReferenceArgument -> arg.reference.startsWith("Input") && arg.reference != "InputPollOption"
            is AnyOfArgument -> arg.any_of.any { apiTypeContainsInputFile(it) }
            is ArrayArgument -> apiTypeContainsInputFile(arg.array)
            else -> false
        }
    }

    private fun apiTypeContainsInputFile(type: ApiType): Boolean {
        return when (type) {
            is ReferenceApiType -> type.reference.startsWith("Input") && type.reference != "InputPollOption"
            is AnyOfApiType -> type.any_of.any { apiTypeContainsInputFile(it) }
            is ArrayApiType -> apiTypeContainsInputFile(type.array)
            else -> false
        }
    }

    private fun convertArgument(raw: RawArgument): Argument {
        val required = raw.required == "Yes"
        val descriptionHtml = raw.descriptionElement.outerHtml()
        val description = MarkdownConverter.convert(descriptionHtml)
        val plainDescription = raw.descriptionElement.text()

        return TypeParser.parseArgumentType(
            typeStr = raw.type,
            name = raw.name,
            description = description,
            required = required,
            descriptionElement = raw.descriptionElement,
        )
    }

    private fun convertObject(raw: RawObject): Object {
        val name = raw.name
        val descriptionHtml = raw.descriptionElements.joinToString("\n") { it.outerHtml() }
        val description = MarkdownConverter.convert(descriptionHtml)
        val documentationLink = raw.documentationLink

        return when (val data = raw.data) {
            is RawObjectData.Fields -> {
                if (data.fields.isEmpty()) {
                    EmptyObject(
                        name = name,
                        description = description,
                        documentation_link = documentationLink,
                    )
                } else {
                    val properties = data.fields.map { convertField(it) }
                    PropertiesObject(
                        name = name,
                        description = description,
                        documentation_link = documentationLink,
                        properties = properties,
                    )
                }
            }
            is RawObjectData.Elements -> {
                val anyOf = data.elements.map { TypeParser.parseTypeString(it) }
                AnyOfObject(
                    name = name,
                    description = description,
                    documentation_link = documentationLink,
                    any_of = anyOf,
                )
            }
        }
    }

    private fun convertField(raw: RawField): Argument {
        val plainDescription = raw.descriptionElement.text()
        val required = !plainDescription.startsWith("Optional.")
        val descriptionHtml = raw.descriptionElement.outerHtml()
        val description = MarkdownConverter.convert(descriptionHtml)

        return TypeParser.parseArgumentType(
            typeStr = raw.type,
            name = raw.name,
            description = description,
            required = required,
            descriptionElement = raw.descriptionElement,
        )
    }
}
