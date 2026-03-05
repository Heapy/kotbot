package io.heapy.kotbot.bot.admin

import io.heapy.kotbot.bot.dao.UserContext
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.database.enums.TelegramUserRole
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.heapy.kotbot.infra.web.KtorRoute
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.TBODY
import kotlinx.html.TR
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.meta
import kotlinx.html.option
import kotlinx.html.script
import kotlinx.html.select
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.textArea
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.unsafe

class AdminRoute(
    private val userContextDao: UserContextDao,
    private val transactionProvider: TransactionProvider,
    private val userNoteService: UserNoteService,
) : KtorRoute {
    override fun Routing.install() {
        get("/admin") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title("Kotbot Admin")
                    meta(charset = "utf-8")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    script(src = "https://unpkg.com/htmx.org@2.0.4") {}
                    style {
                        unsafe {
                            raw(CSS)
                        }
                    }
                }
                body {
                    h1 { +"Kotbot Admin" }
                    div {
                        id = "users-container"
                        attributes["hx-get"] = "/admin/users?page=0"
                        attributes["hx-trigger"] = "load"
                        attributes["hx-swap"] = "innerHTML"
                        +"Loading users..."
                    }
                    div { id = "modal-container" }
                }
            }
        }

        get("/admin/users") {
            val page = call.queryParameters["page"]?.toIntOrNull() ?: 0
            val pageSize = 50
            val offset = page * pageSize

            val (users, totalCount) = transactionProvider.transaction {
                val users = userContextDao.listAll(limit = pageSize, offset = offset)
                val count = userContextDao.countAll()
                users to count
            }

            val totalPages = (totalCount + pageSize - 1) / pageSize

            call.respondHtmlFragment {
                table {
                    thead {
                        tr {
                            th { +"ID" }
                            th { +"Telegram ID" }
                            th { +"Display Name" }
                            th { +"Role" }
                            th { +"Status" }
                            th { +"Tag" }
                            th { +"Note" }
                            th { +"Messages" }
                            th { +"Created" }
                            th { +"Last Message" }
                        }
                    }
                    tbody {
                        for (user in users) {
                            userRow(user)
                        }
                    }
                }
                if (totalPages > 1) {
                    div(classes = "pagination") {
                        if (page > 0) {
                            button {
                                attributes["hx-get"] = "/admin/users?page=${page - 1}"
                                attributes["hx-target"] = "#users-container"
                                attributes["hx-swap"] = "innerHTML"
                                +"Previous"
                            }
                        }
                        span { +"Page ${page + 1} of $totalPages ($totalCount users)" }
                        if (page < totalPages - 1) {
                            button {
                                attributes["hx-get"] = "/admin/users?page=${page + 1}"
                                attributes["hx-target"] = "#users-container"
                                attributes["hx-swap"] = "innerHTML"
                                +"Next"
                            }
                        }
                    }
                }
            }
        }

        post("/admin/users/{id}/role") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val formParameters = call.receiveParameters()
            val newRole = formParameters["role"]?.let { roleName ->
                TelegramUserRole.entries.find { it.literal == roleName }
            }
            if (newRole == null) {
                call.respondText("Invalid role", status = HttpStatusCode.BadRequest)
                return@post
            }

            val user = transactionProvider.transaction {
                val userContext = userContextDao.getByInternalId(id)
                if (userContext != null) {
                    userContextDao.updateRole(userContext, newRole)
                    userContextDao.getByInternalId(id)
                } else {
                    null
                }
            }

            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@post
            }

            val html = createHTML(prettyPrint = false).tr {
                userRowContent(user)
            }
            call.respondText(html, ContentType.Text.Html)
        }

        post("/admin/users/{id}/status") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val formParameters = call.receiveParameters()
            val newStatus = formParameters["status"]?.let { statusName ->
                TelegramUserStatus.entries.find { it.literal == statusName }
            }
            if (newStatus == null) {
                call.respondText("Invalid status", status = HttpStatusCode.BadRequest)
                return@post
            }

            val user = transactionProvider.transaction {
                val userContext = userContextDao.getByInternalId(id)
                if (userContext != null) {
                    userContextDao.updateStatus(userContext, newStatus)
                    userContextDao.getByInternalId(id)
                } else {
                    null
                }
            }

            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@post
            }

            val html = createHTML(prettyPrint = false).tr {
                userRowContent(user)
            }
            call.respondText(html, ContentType.Text.Html)
        }

        post("/admin/users/{id}/tag") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val formParameters = call.receiveParameters()
            val tag = formParameters["tag"]?.takeIf { it.isNotBlank() }

            val user = transactionProvider.transaction {
                val userContext = userContextDao.getByInternalId(id)
                if (userContext != null) {
                    userContextDao.updateBadge(userContext, tag)
                    userContextDao.getByInternalId(id)
                } else {
                    null
                }
            }

            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@post
            }

            val html = createHTML(prettyPrint = false).tr {
                userRowContent(user)
            }
            call.respondText(html, ContentType.Text.Html)
        }

        get("/admin/users/{id}/note") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val user = transactionProvider.transaction { userContextDao.getByInternalId(id) }
            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val html = createHTML(prettyPrint = false).div {
                noteModal(user)
            }
            call.respondText(html, ContentType.Text.Html)
        }

        post("/admin/users/{id}/note") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val formParameters = call.receiveParameters()
            val note = formParameters["note"]?.takeIf { it.isNotBlank() }

            val user = transactionProvider.transaction {
                val userContext = userContextDao.getByInternalId(id)
                if (userContext != null) {
                    userContextDao.updateNote(userContext, note)
                    userContextDao.getByInternalId(id)
                } else {
                    null
                }
            }

            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@post
            }

            val html = createHTML(prettyPrint = false).tr {
                userRowContent(user)
            }
            call.respondText(html, ContentType.Text.Html)
        }

        post("/admin/users/{id}/note/generate") {
            val userId = call.pathParameters["id"]?.toLongOrNull()
            if (userId == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@post
            }

            val user = transactionProvider.transaction { userContextDao.getByInternalId(userId) }
            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@post
            }

            with(user) { userNoteService.startGeneration() }

            val html = createHTML(prettyPrint = false).textArea {
                id = "note-textarea-$userId"
                name = "note"
                attributes["style"] = TEXTAREA_STYLE
                attributes["readonly"] = ""
                +"Generating in background… close and reopen this dialog when done."
            }
            call.respondText(html, ContentType.Text.Html)
        }
    }
}

private fun TBODY.userRow(user: UserContext) {
    tr {
        userRowContent(user)
    }
}

private fun TR.userRowContent(user: UserContext) {
    id = "user-row-${user.internalId}"
    td { +"${user.internalId}" }
    td { +"${user.telegramId}" }
    td { +(user.displayName ?: "-") }
    td {
        form {
            attributes["hx-post"] = "/admin/users/${user.internalId}/role"
            attributes["hx-target"] = "#user-row-${user.internalId}"
            attributes["hx-swap"] = "outerHTML"
            select {
                name = "role"
                attributes["onchange"] = "this.form.requestSubmit()"
                for (r in TelegramUserRole.entries) {
                    option {
                        value = r.literal
                        selected = r == user.role
                        +r.literal
                    }
                }
            }
        }
    }
    td {
        form {
            attributes["hx-post"] = "/admin/users/${user.internalId}/status"
            attributes["hx-target"] = "#user-row-${user.internalId}"
            attributes["hx-swap"] = "outerHTML"
            select {
                name = "status"
                attributes["onchange"] = "this.form.requestSubmit()"
                for (s in TelegramUserStatus.entries) {
                    option {
                        value = s.literal
                        selected = s == user.status
                        +s.literal
                    }
                }
            }
        }
    }
    td {
        form {
            attributes["hx-post"] = "/admin/users/${user.internalId}/tag"
            attributes["hx-target"] = "#user-row-${user.internalId}"
            attributes["hx-swap"] = "outerHTML"
            attributes["hx-trigger"] = "change"
            input(type = InputType.text) {
                name = "tag"
                value = user.badge ?: ""
                maxLength = "16"
                attributes["style"] = "width: 120px"
            }
        }
    }
    td {
        button(classes = "note-btn") {
            attributes["hx-get"] = "/admin/users/${user.internalId}/note"
            attributes["hx-target"] = "#modal-container"
            attributes["hx-swap"] = "innerHTML"
            if (user.note != null) +"Edit note" else +"Add note"
        }
    }
    td { +"${user.messageCount}" }
    td { +"${user.created}" }
    td { +"${user.lastMessage}" }
}

private fun FlowContent.noteModal(user: UserContext) {
    div {
        id = "note-modal-overlay"
        attributes["style"] = "position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);z-index:1000;display:flex;align-items:center;justify-content:center"
        attributes["onclick"] = "if(event.target===this)document.getElementById('modal-container').innerHTML=''"
        div {
            attributes["style"] = "background:white;padding:2rem;border-radius:8px;width:640px;max-width:90vw;box-shadow:0 20px 60px rgba(0,0,0,0.3)"
            attributes["onclick"] = "event.stopPropagation()"
            h2 {
                attributes["style"] = "margin-bottom:1rem;color:#333"
                +"Note: ${user.displayName ?: "User #${user.internalId}"}"
            }
            textArea {
                id = "note-textarea-${user.internalId}"
                name = "note"
                attributes["style"] = TEXTAREA_STYLE
                +(user.note ?: "")
            }
            div {
                attributes["style"] = "margin-top:1rem;display:flex;gap:0.5rem;justify-content:flex-end"
                button(classes = "modal-btn") {
                    attributes["hx-post"] = "/admin/users/${user.internalId}/note/generate"
                    attributes["hx-target"] = "#note-textarea-${user.internalId}"
                    attributes["hx-swap"] = "outerHTML"
                    attributes["hx-disabled-elt"] = "this"
                    +"Auto-generate"
                }
                button(classes = "modal-btn modal-btn-primary") {
                    attributes["hx-post"] = "/admin/users/${user.internalId}/note"
                    attributes["hx-include"] = "#note-textarea-${user.internalId}"
                    attributes["hx-target"] = "#user-row-${user.internalId}"
                    attributes["hx-swap"] = "outerHTML"
                    attributes["hx-on::after-request"] = "document.getElementById('modal-container').innerHTML=''"
                    +"Save"
                }
                button(classes = "modal-btn") {
                    attributes["onclick"] = "document.getElementById('modal-container').innerHTML=''"
                    +"Cancel"
                }
            }
        }
    }
}

private const val TEXTAREA_STYLE = "width:100%;height:200px;padding:0.5rem;border:1px solid #ddd;border-radius:4px;font-family:inherit;font-size:0.9rem;resize:vertical;display:block"

private const val CSS = """
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: system-ui, -apple-system, sans-serif; padding: 2rem; background: #f5f5f5; }
    h1 { margin-bottom: 1.5rem; color: #333; }
    table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
    th, td { padding: 0.75rem 1rem; text-align: left; border-bottom: 1px solid #eee; }
    th { background: #f8f9fa; font-weight: 600; color: #555; }
    tr:hover { background: #f8f9fa; }
    select { padding: 0.25rem 0.5rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; }
    input[type="text"] { padding: 0.25rem 0.5rem; border: 1px solid #ddd; border-radius: 4px; background: white; }
    .pagination { margin-top: 1rem; display: flex; align-items: center; gap: 1rem; justify-content: center; }
    .pagination button { padding: 0.5rem 1rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; }
    .pagination button:hover { background: #e9ecef; }
    .pagination span { color: #666; }
    .note-btn { padding: 0.25rem 0.6rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; font-size: 0.85rem; white-space: nowrap; }
    .note-btn:hover { background: #e9ecef; }
    .modal-btn { padding: 0.5rem 1rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; font-size: 0.9rem; }
    .modal-btn:hover { background: #e9ecef; }
    .modal-btn:disabled { opacity: 0.6; cursor: not-allowed; }
    .modal-btn-primary { background: #0d6efd; color: white; border-color: #0d6efd; }
    .modal-btn-primary:hover { background: #0b5ed7; }
"""
