enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "kotbot"

include(
    "core",
    "core-gen",
    "tgkotbot",
    "tgkotbot-dataops",
    "tgkotbot-database",
    "tgpt",
    "tgpt-dataops",
    "tgpt-database",
)
