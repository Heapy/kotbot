# @tgkotbot

Bot for Kotlin chats in the telegram.

## Docker Image

Project distributed as docker image, and available
in [GitHub Container Repository](https://github.com/Heapy/kotbot/pkgs/container/kotbot).

## Development

1. Use [@BotFather](https://t.me/BotFather) to create a bot which you will use for testing;
2. You need to pass bot token that you received from BotFather to Bot, here two available options:
    1. Create file `/src/main/resources/application.conf` with following content:
       ```
       bot {
         token = "your_bot_token"
       }   
       ```
   2. Set env variable `KOTBOT_TOKEN` to token value in Intellij IDEA's "Run/Debug Configuration";
3. Run main in `io.heapy.kotbot.Application`.

That's it, you have a running bot, now add him to some group and grant admin rules (in order to delete messages and kick
users), and test it.

## TODO

### Moderation

User can't send messages, until he add this bot and answer on bot's sign up questions.

#### Bot commands

* `/stats` Display your statistics: number of warnings and helpful flags.

#### Administrative commands

These commands only work for group admins.

* `/mute [reason]` Reply to a user to mute them for 24 hours;
* `/fmute [reason]` is forever mute;
* `/unmute` Reply to a user to unmute them;
* `/channelmode` Enable read-only mode. Only admins can send messages. Send the command again to disable the mode;
* `/warn [reason]` Reply to a user to warn them. User will receive personal warning messages through bot. First warn
  just send message, second - mutes for 24 hours, third - mutes user forever. Warnings expires over time.

#### Community commands

These commands work for everybody.

`/report` Reply to a message to report it to admins;
`+1` Reply to a message to mark it as helpful.

### Stats

#### Per User

* Flood %: (Avg. Message Length for chat) / (Avg. Message Length for user)
* Active Days: Number of days when user was active
* Messages: Number of sent messages
* Activity: Frequency of messages and the length

#### Values

* Total messages (week, month, year, lifetime)
* Avg. daily messages
* Avg. messages per hour
* Active users (week, month, year, lifetime)
* Avg. daily users
* Community health
  > Community health tells you how engaged the community is. 100% means that at least 20% of the community is engaged and creates at least 80% of the content. If this becomes more balanced (for eg 100% of the content is created by 50% of the people) you’ll see a number above 100. On the other hand, if the community is imbalanced and all the content is created by a handful of contributors, the community health will drop below 100%.
* User grows: Number of new users per day
* User loss: Number of users leaves per day

#### Graphs

* Active users: Users/Day (Users who posted at least 1 text message during the day.)
* Chat Activity: Messages/Day (Activity distribution is based on message length & count.)
* Active days of the week: Messages/Week Day
* Active hours: Messages/Day Time
* Most active months: Messages/Month
* Num of users

### Restrictions

* User can't send audios, videos, video notes and voice notes.
* User can't send animations, games, stickers and use inline bots.

### Automation

* Delete bot calls (if any)
* Delete joins
* Delete commands
* Delete videos & GIFs
* Delete games
* Delete voices
* Delete all stickers

### UI

* Web archive of messages (/room/year-month-day/)

### BotFather configuration:

User Commands:
```
spam – report spam, reply to a spam message with /spam
```

Moderator Commands:

```
spam - delete spam message and ban
```

Admin Commands:

```
spam - delete spam message and ban
chat_info - Get chat info
kotlin_meta - Send message to Kotlin Meta
kotlin_lang - Send message to Kotlin Lang
kotlin_jvm - Send message to Kotlin JVM
kotlin_start - Send message to Kotlin Start
kotlin_mobile - Send message to Kotlin Mobile
kotlin_native - Send message to Kotlin Native
kotlin_js - Send message to Kotlin JS
```
