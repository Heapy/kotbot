package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.PassportElementErrorSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents an error in the Telegram Passport element which was submitted that should be resolved by the user. It should be one of:
 *
 * * [PassportElementErrorDataField](https://core.telegram.org/bots/api/#passportelementerrordatafield)
 * * [PassportElementErrorFrontSide](https://core.telegram.org/bots/api/#passportelementerrorfrontside)
 * * [PassportElementErrorReverseSide](https://core.telegram.org/bots/api/#passportelementerrorreverseside)
 * * [PassportElementErrorSelfie](https://core.telegram.org/bots/api/#passportelementerrorselfie)
 * * [PassportElementErrorFile](https://core.telegram.org/bots/api/#passportelementerrorfile)
 * * [PassportElementErrorFiles](https://core.telegram.org/bots/api/#passportelementerrorfiles)
 * * [PassportElementErrorTranslationFile](https://core.telegram.org/bots/api/#passportelementerrortranslationfile)
 * * [PassportElementErrorTranslationFiles](https://core.telegram.org/bots/api/#passportelementerrortranslationfiles)
 * * [PassportElementErrorUnspecified](https://core.telegram.org/bots/api/#passportelementerrorunspecified)
 */
@Serializable(with = PassportElementErrorSerializer::class)
public sealed interface PassportElementError
