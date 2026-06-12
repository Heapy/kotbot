package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.RichTextSerializer
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * This object represents a rich formatted text. Currently, it can be either a String for plain text, an Array of [RichText](https://core.telegram.org/bots/api/#richtext), or any of the following types:
 *
 * * [RichTextBold](https://core.telegram.org/bots/api/#richtextbold)
 * * [RichTextItalic](https://core.telegram.org/bots/api/#richtextitalic)
 * * [RichTextUnderline](https://core.telegram.org/bots/api/#richtextunderline)
 * * [RichTextStrikethrough](https://core.telegram.org/bots/api/#richtextstrikethrough)
 * * [RichTextSpoiler](https://core.telegram.org/bots/api/#richtextspoiler)
 * * [RichTextDateTime](https://core.telegram.org/bots/api/#richtextdatetime)
 * * [RichTextTextMention](https://core.telegram.org/bots/api/#richtexttextmention)
 * * [RichTextSubscript](https://core.telegram.org/bots/api/#richtextsubscript)
 * * [RichTextSuperscript](https://core.telegram.org/bots/api/#richtextsuperscript)
 * * [RichTextMarked](https://core.telegram.org/bots/api/#richtextmarked)
 * * [RichTextCode](https://core.telegram.org/bots/api/#richtextcode)
 * * [RichTextCustomEmoji](https://core.telegram.org/bots/api/#richtextcustomemoji)
 * * [RichTextMathematicalExpression](https://core.telegram.org/bots/api/#richtextmathematicalexpression)
 * * [RichTextUrl](https://core.telegram.org/bots/api/#richtexturl)
 * * [RichTextEmailAddress](https://core.telegram.org/bots/api/#richtextemailaddress)
 * * [RichTextPhoneNumber](https://core.telegram.org/bots/api/#richtextphonenumber)
 * * [RichTextBankCardNumber](https://core.telegram.org/bots/api/#richtextbankcardnumber)
 * * [RichTextMention](https://core.telegram.org/bots/api/#richtextmention)
 * * [RichTextHashtag](https://core.telegram.org/bots/api/#richtexthashtag)
 * * [RichTextCashtag](https://core.telegram.org/bots/api/#richtextcashtag)
 * * [RichTextBotCommand](https://core.telegram.org/bots/api/#richtextbotcommand)
 * * [RichTextAnchor](https://core.telegram.org/bots/api/#richtextanchor)
 * * [RichTextAnchorLink](https://core.telegram.org/bots/api/#richtextanchorlink)
 * * [RichTextReference](https://core.telegram.org/bots/api/#richtextreference)
 * * [RichTextReferenceLink](https://core.telegram.org/bots/api/#richtextreferencelink)
 */
@Serializable(with = RichTextSerializer::class)
public sealed interface RichText

@JvmInline
@Serializable
public value class RichTextString(
    public val value: String,
) : RichText

@JvmInline
@Serializable
public value class RichTextList(
    public val value: List<RichText>,
) : RichText
