# kotbot [![Build Status](https://travis-ci.com/KotlinBy/kotbot.svg?branch=master)](https://travis-ci.com/KotlinBy/kotbot)

Bot for Kotlin chats in telegram.

## Why?

Combot becomes paid.

## Docs

* [Telegram Bots](https://core.telegram.org/bots)
* [rubenlagus/TelegramBots](https://github.com/rubenlagus/TelegramBots)

## TODO

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
