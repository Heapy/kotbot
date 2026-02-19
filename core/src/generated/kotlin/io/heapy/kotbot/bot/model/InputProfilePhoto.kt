package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputProfilePhotoSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes a profile photo to set. Currently, it can be one of
 *
 * * [InputProfilePhotoStatic](https://core.telegram.org/bots/api/#inputprofilephotostatic)
 * * [InputProfilePhotoAnimated](https://core.telegram.org/bots/api/#inputprofilephotoanimated)
 */
@Serializable(with = InputProfilePhotoSerializer::class)
public sealed interface InputProfilePhoto
