package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.RichBlockSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents a block in a rich formatted message. Currently, it can be any of the following types:
 *
 * * [RichBlockParagraph](https://core.telegram.org/bots/api/#richblockparagraph)
 * * [RichBlockSectionHeading](https://core.telegram.org/bots/api/#richblocksectionheading)
 * * [RichBlockPreformatted](https://core.telegram.org/bots/api/#richblockpreformatted)
 * * [RichBlockFooter](https://core.telegram.org/bots/api/#richblockfooter)
 * * [RichBlockDivider](https://core.telegram.org/bots/api/#richblockdivider)
 * * [RichBlockMathematicalExpression](https://core.telegram.org/bots/api/#richblockmathematicalexpression)
 * * [RichBlockAnchor](https://core.telegram.org/bots/api/#richblockanchor)
 * * [RichBlockList](https://core.telegram.org/bots/api/#richblocklist)
 * * [RichBlockBlockQuotation](https://core.telegram.org/bots/api/#richblockblockquotation)
 * * [RichBlockPullQuotation](https://core.telegram.org/bots/api/#richblockpullquotation)
 * * [RichBlockCollage](https://core.telegram.org/bots/api/#richblockcollage)
 * * [RichBlockSlideshow](https://core.telegram.org/bots/api/#richblockslideshow)
 * * [RichBlockTable](https://core.telegram.org/bots/api/#richblocktable)
 * * [RichBlockDetails](https://core.telegram.org/bots/api/#richblockdetails)
 * * [RichBlockMap](https://core.telegram.org/bots/api/#richblockmap)
 * * [RichBlockAnimation](https://core.telegram.org/bots/api/#richblockanimation)
 * * [RichBlockAudio](https://core.telegram.org/bots/api/#richblockaudio)
 * * [RichBlockPhoto](https://core.telegram.org/bots/api/#richblockphoto)
 * * [RichBlockVideo](https://core.telegram.org/bots/api/#richblockvideo)
 * * [RichBlockVoiceNote](https://core.telegram.org/bots/api/#richblockvoicenote)
 * * [RichBlockThinking](https://core.telegram.org/bots/api/#richblockthinking)
 */
@Serializable(with = RichBlockSerializer::class)
public sealed interface RichBlock
