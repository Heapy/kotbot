enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "kotbot"

include(
    "api-parser",
    "core",
    "core-md",
    "core-gen",
    "tgkotbot",
    "tgkotbot-dataops",
    "tgkotbot-database",
    "tgpt",
    "tgpt-dataops",
    "tgpt-database",
)
