package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.*
import org.jsoup.nodes.Element

object TypeParser {
    /**
     * Parse a type string from the "Type" column in the docs table.
     * Examples: "Integer", "String", "Boolean", "True", "Float", "Float number",
     * "Array of PhotoSize", "Integer or String", "InputFile or String"
     */
    fun parseTypeString(s: String): ApiType {
        return when (s.trim()) {
            "Integer", "Int" -> IntApiType()
            "String" -> StringApiType()
            "Boolean" -> BooleanApiType()
            "True" -> BooleanApiType(default = true)
            "Float", "Float number" -> IntApiType() // placeholder, Floats not in ApiType
            else -> parseComplexType(s.trim())
        }
    }

    private fun parseComplexType(s: String): ApiType {
        // "Array of X"
        if (s.startsWith("Array of ")) {
            val inner = s.removePrefix("Array of ")
            val innerType = parseInnerArrayType(inner)
            return ArrayApiType(array = innerType)
        }

        // "X or Y" pattern
        if (s.contains(" or ")) {
            val types = splitOrTypes(s).map { parseTypeString(it.trim()) }
            return AnyOfApiType(any_of = types)
        }

        // Reference to an object
        return ReferenceApiType(reference = s)
    }

    /**
     * Parse the inner part of "Array of ..." which might be:
     * - A single type: "Array of PhotoSize"
     * - Nested array: "Array of Array of PhotoSize"
     * - Or type: "Array of InputMediaAudio or InputMediaVideo"
     * - Comma/and separated: "Array of InputMediaAudio, InputMediaDocument, InputMediaPhoto and InputMediaVideo"
     */
    private fun parseInnerArrayType(inner: String): ApiType {
        // Check for nested Array
        if (inner.startsWith("Array of ")) {
            return parseTypeString(inner)
        }

        // Check for comma-separated types (with optional "and" for last element)
        // e.g. "InputMediaAudio, InputMediaDocument, InputMediaPhoto and InputMediaVideo"
        if (inner.contains(",") || inner.contains(" and ")) {
            val types = splitOrTypes(inner)
            if (types.size > 1) {
                return AnyOfApiType(any_of = types.map { parseTypeString(it.trim()) })
            }
        }

        // Check for "or" pattern
        if (inner.contains(" or ")) {
            val types = splitOrTypes(inner)
            return AnyOfApiType(any_of = types.map { parseTypeString(it.trim()) })
        }

        return parseTypeString(inner)
    }

    /**
     * Split type strings by "or", ",", and "and" delimiters.
     * Handles patterns like:
     * - "Integer or String"
     * - "InputMediaAudio, InputMediaDocument, InputMediaPhoto and InputMediaVideo"
     */
    private fun splitOrTypes(s: String): List<String> {
        // First replace " and " with ", " to normalize
        val normalized = s.replace(" and ", ", ").replace(" or ", ", ")
        return normalized.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() && it.first().isUpperCase() }
    }

    /**
     * Parse an argument/field type, enriching with constraints from description.
     */
    fun parseArgumentType(
        typeStr: String,
        name: String,
        description: String,
        required: Boolean,
        descriptionElement: Element,
    ): Argument {
        val baseType = parseBaseType(typeStr)

        return when (baseType) {
            is ParsedType.Integer -> {
                val constraints = DescriptionParser.extractConstraints(descriptionElement)
                IntArgument(
                    name = name,
                    description = description,
                    required = required,
                    default = constraints.defaultValue?.toIntOrNull() ?: baseType.default,
                    min = constraints.min?.toIntOrNull() ?: baseType.min,
                    max = constraints.max?.toIntOrNull() ?: baseType.max,
                    enumeration = constraints.intEnumeration?.ifEmpty { null },
                )
            }
            is ParsedType.Str -> {
                val constraints = DescriptionParser.extractConstraints(descriptionElement)
                StringArgument(
                    name = name,
                    description = description,
                    required = required,
                    default = constraints.defaultValue ?: baseType.default,
                    min_len = constraints.min?.toIntOrNull() ?: baseType.minLen,
                    max_len = constraints.max?.toIntOrNull() ?: baseType.maxLen,
                    enumeration = constraints.stringEnumeration?.ifEmpty { null },
                )
            }
            is ParsedType.Bool -> {
                val constraints = DescriptionParser.extractConstraints(descriptionElement)
                val defaultVal = constraints.defaultValue?.lowercase()?.toBooleanStrictOrNull()
                    ?: baseType.default
                BooleanArgument(
                    name = name,
                    description = description,
                    required = required,
                    default = defaultVal,
                )
            }
            is ParsedType.FloatType -> {
                FloatArgument(
                    name = name,
                    description = description,
                    required = required,
                )
            }
            is ParsedType.Array -> {
                ArrayArgument(
                    name = name,
                    description = description,
                    required = required,
                    array = parsedTypeToApiType(baseType.inner),
                )
            }
            is ParsedType.Reference -> {
                ReferenceArgument(
                    name = name,
                    description = description,
                    required = required,
                    reference = baseType.name,
                )
            }
            is ParsedType.Or -> {
                AnyOfArgument(
                    name = name,
                    description = description,
                    required = required,
                    any_of = baseType.types.map { parsedTypeToApiType(it) },
                )
            }
        }
    }

    /**
     * Parse base type from type string column, returning intermediate ParsedType
     * that supports Float (unlike ApiType).
     */
    private fun parseBaseType(s: String): ParsedType {
        return when (s.trim()) {
            "Integer", "Int" -> ParsedType.Integer()
            "String" -> ParsedType.Str()
            "Boolean" -> ParsedType.Bool()
            "True" -> ParsedType.Bool(default = true)
            "Float", "Float number" -> ParsedType.FloatType
            else -> parseComplexBaseType(s.trim())
        }
    }

    private fun parseComplexBaseType(s: String): ParsedType {
        if (s.startsWith("Array of ")) {
            val inner = s.removePrefix("Array of ")
            // Handle nested arrays
            if (inner.startsWith("Array of ")) {
                return ParsedType.Array(parseBaseType(inner))
            }
            // Handle comma/and separated types
            if (inner.contains(",") || inner.contains(" and ") || inner.contains(" or ")) {
                val types = splitOrTypes(inner)
                if (types.size > 1) {
                    return ParsedType.Array(ParsedType.Or(types.map { parseBaseType(it.trim()) }))
                }
            }
            return ParsedType.Array(parseBaseType(inner))
        }

        if (s.contains(" or ") || (s.contains(",") && s.contains(" and "))) {
            val types = splitOrTypes(s)
            if (types.size > 1) {
                return ParsedType.Or(types.map { parseBaseType(it.trim()) })
            }
        }

        return ParsedType.Reference(s)
    }

    private fun parsedTypeToApiType(type: ParsedType): ApiType {
        return when (type) {
            is ParsedType.Integer -> IntApiType()
            is ParsedType.Str -> StringApiType()
            is ParsedType.Bool -> BooleanApiType(default = type.default)
            is ParsedType.FloatType -> IntApiType() // Float not in ApiType hierarchy
            is ParsedType.Array -> ArrayApiType(array = parsedTypeToApiType(type.inner))
            is ParsedType.Reference -> ReferenceApiType(reference = type.name)
            is ParsedType.Or -> AnyOfApiType(any_of = type.types.map { parsedTypeToApiType(it) })
        }
    }
}

sealed interface ParsedType {
    data class Integer(
        val default: Int? = null,
        val min: Int? = null,
        val max: Int? = null,
    ) : ParsedType

    data class Str(
        val default: String? = null,
        val minLen: Int? = null,
        val maxLen: Int? = null,
    ) : ParsedType

    data class Bool(val default: Boolean? = null) : ParsedType

    data object FloatType : ParsedType

    data class Array(val inner: ParsedType) : ParsedType

    data class Reference(val name: String) : ParsedType

    data class Or(val types: List<ParsedType>) : ParsedType
}
