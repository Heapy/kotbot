---
name: update-telegram-bot-api
description: Update kotbot to a new Telegram Bot API version by capturing the official HTML snapshot, parsing and reviewing the specification, regenerating Kotlin bindings, updating current-version references, handling serializer changes, and adding focused tests. Use when asked to upgrade, update, regenerate, or add support for a Telegram Bot API release. Stop before publishing, tagging, or releasing artifacts.
---

# Update Telegram Bot API

Upgrade the checked-in API snapshot and generated Kotlin surface while preserving an auditable specification diff. Run every command from the repository root.

## Establish the version and baseline

1. Require a target Bot API version such as `10.2`. If the user asks for the latest version, inspect the official page before choosing the name.
2. Encode `major.minor` as `api<major><minor>0`; for example, `10.1` becomes `api1010` and `9.6` becomes `api960`.
3. Use these paths:
   - Raw full-page HTML without an extension: `core-gen/src/main/resources/apiXXXXX`
   - Derived Markdown: `core-gen/src/main/resources/apiXXXXX.md`
   - Current review copy: `core-gen/src/main/resources/latest.md`
4. Record `git status --short` before editing. Preserve every pre-existing change and use path-scoped diffs throughout the upgrade.
5. Check paths that the runners overwrite:

   ```sh
   git status --short -- \
     ':(glob)core-gen/src/main/resources/*.md' \
     core/src/generated
   ```

   Require these paths to have no pre-existing changes. If they are dirty, stop and use a clean worktree or ask the user how to preserve the overlap. Never stash, reset, or overwrite it automatically. A user-supplied raw `apiXXXXX` file is acceptable only after validating it as described below.
6. Read the current version pins and schema-sensitive tests:

   ```sh
   rg -n 'api[0-9]+|Bot API [0-9]+' \
     core-gen/src/main/kotlin core-gen/src/test/kotlin README.md
   ```

## Capture and validate the official source

1. Fetch only `https://core.telegram.org/bots/api/`. Save the complete page source, not rendered text or converted Markdown. Download into a temporary file so a failed validation cannot poison the destination:

   ```sh
   test ! -e core-gen/src/main/resources/apiXXXXX
   curl --fail --location --remove-on-error \
     --output /tmp/telegram-bot-api-apiXXXXX.html \
     https://core.telegram.org/bots/api/
   test -s /tmp/telegram-bot-api-apiXXXXX.html
   ```

2. Refuse to overwrite an existing raw snapshot unless the user explicitly requests a refresh.
3. Confirm that the temporary source contains `id="dev_page_content"` and the exact `Bot API X.Y` marker. Reject login pages, challenge pages, empty responses, and version mismatches:

   ```sh
   rg -nF 'id="dev_page_content"' /tmp/telegram-bot-api-apiXXXXX.html
   rg -nF 'Bot API X.Y' /tmp/telegram-bot-api-apiXXXXX.html
   ```

4. Move the validated temporary file into place. Delete only the rejected temporary file on failure:

   ```sh
   mv /tmp/telegram-bot-api-apiXXXXX.html \
     core-gen/src/main/resources/apiXXXXX
   ```

5. Keep the raw HTML snapshot unchanged after validation.

## Parse and review the specification

1. Append `apiXXXXX` to the archive list in `core-gen/src/main/kotlin/Parse.kt`. Do not replace or reorder historical entries.
2. Re-run the Markdown overlap check from the baseline. Stop if any `.md` output became dirty before parsing.
3. Run:

   ```sh
   ./gradlew :core-gen:parseTelegramBotApi
   ```

4. Review the process output. `Parse` rewrites Markdown for every archived snapshot and logs unsupported top-level elements as `Unknown tag` without failing. Investigate new warnings and unexpected changes to older `.md` files; extend `processVersion` and `ParseTest` when the new page shape would otherwise lose content.
5. Copy the new Markdown to the tracked review file and verify exact equality:

   ```sh
   cp core-gen/src/main/resources/apiXXXXX.md core-gen/src/main/resources/latest.md
   cmp -s core-gen/src/main/resources/apiXXXXX.md core-gen/src/main/resources/latest.md
   ```

6. Review the specification delta before generating code:

   ```sh
   git diff -- core-gen/src/main/resources/latest.md
   git status --short -- core-gen/src/main/resources
   ```

7. Inventory every added, changed, renamed, and removed method, object, field, parameter, return type, and union variant. Treat the upstream recent-changes section as a starting point, not an exhaustive list.

`latest.md` is a review artifact. Code generation reads the raw `apiXXXXX` HTML resource.

## Advance current-version references

1. Replace `apiResource` in `core-gen/src/main/kotlin/Generate.kt` with `apiXXXXX`.
2. Advance current-schema resource references wherever they exist. Search first; schema-sensitive tests in newer checkouts include:
   - `core-gen/src/test/kotlin/InputFileArgumentGenerationTest.kt`
   - `core-gen/src/test/kotlin/PolymorphicSchemaTest.kt`
   - `core-gen/src/test/kotlin/PolymorphicDispatchGenerationTest.kt`
   If these files are absent, follow the repository's existing parser and generator test structure and add equivalent focused coverage where practical.
3. Update the `Bot API X.Y` heading in `README.md`. Do not change the library release version merely because the upstream API version advanced.
4. Search again for version literals. Append in `Parse.kt`, replace the current pins in generation, tests, and README, and retain historical fixture references intentionally.

## Generate and inspect Kotlin changes

1. Confirm `git status --short -- core/src/generated` is empty. Stop rather than overwrite any pre-existing generated-source change.
2. Run:

   ```sh
   ./gradlew :core-gen:generateTelegramBotApi
   ```

3. Inspect both tracked and untracked outputs:

   ```sh
   git status --short -- core/src/generated
   git diff --stat -- core/src/generated
   git diff --name-status -- core/src/generated
   git diff -- core/src/generated
   git ls-files --others --exclude-standard -- core/src/generated
   ```

   Open and review every file listed by `git ls-files --others`; ordinary `git diff` does not show untracked file contents.

4. Map every reviewed specification change to generated or handwritten code. Verify:
   - New methods and models exist with correct names and documentation.
   - Required and optional fields have correct nullability and defaults.
   - Method parameters and return types preserve arrays, integer widths, `ChatId`, and `InputFile` semantics.
   - Primitive, list, object, and mixed unions receive explicit representations.
   - New polymorphic variants appear in generated dispatch and serializer coverage.
   - Response unions preserve unknown variants where required; request-only unions reject unsupported variants.
5. Inspect `core/src/main/kotlin/io/heapy/kotbot/bot/Serializers.kt` whenever the release changes a union or wire shape. Generated models alone are not sufficient for polymorphic serialization.
6. Review removals manually. KotlinPoet overwrites emitted files but does not delete stale files for removed or renamed API entities. Delete only files proven stale from the specification delta. Never clean the whole `core/src/generated` tree; it contains checked-in special cases such as `Cover.kt` and `Thumbnail.kt`.
7. Treat generator failures and suspiciously small diffs as schema-review signals. `Generate.kt` contains shape-specific handling, including return unions, file references, integer widths, and rich-text forms.

## Add focused regression coverage

1. When present, update exact current-schema assertions rather than weakening them:
   - File-reference argument inventory in `InputFileArgumentGenerationTest`.
   - Response-versus-request union sets and discriminator branches in `PolymorphicSchemaTest`.
   - Generated dispatch, unknown-type, and handwritten-wrapper checks in `PolymorphicDispatchGenerationTest`.
2. Add the smallest runtime tests that prove each new behavior. Prefer tests for:
   - Decoding every new response variant.
   - Encoding new request variants.
   - Changed method results through `Kotbot.execute` with Ktor `MockEngine`.
   - Bare string file IDs or URLs for media arguments.
   - Primitive, list, and object union forms.
   - Unknown response-variant preservation.
3. Copy the raw snapshot into `api-parser/src/test/resources`, add its reviewed versioned JSON tree alongside the existing fixtures, and add a version case to `HtmlApiParserTest`; parameterize the test if needed. Never mechanically bless generated JSON without inspecting its complete semantic diff.

## Validate and hand off

1. Run targeted tests, then the repository check:

   ```sh
   ./gradlew :api-parser:test :core-gen:test :core:test
   ./gradlew check
   git diff --check
   ```

2. Re-run both codegen tasks and verify they introduce no change beyond the already reviewed diff. Recheck that `apiXXXXX.md` and `latest.md` are byte-identical.
3. Review the final path-scoped diff against the original status snapshot. Do not include unrelated worktree changes.
4. Report the source version, Markdown delta, generated and handwritten changes, stale-file decisions, tests added, commands run, and any remaining uncertainty.
5. Stop. Do not publish, commit, push, tag, or create a release unless the user explicitly requests that separate workflow.
