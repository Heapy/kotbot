package io.heapy.kotbot.bot.admin

import io.heapy.kotbot.bot.dao.UserContext
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.database.enums.TelegramUserRole
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
import kotlinx.html.TBODY
import kotlinx.html.TR
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.id
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
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.unsafe

class AdminRoute(
    private val userContextDao: UserContextDao,
    private val transactionProvider: TransactionProvider,
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
    td { +user.status.literal }
    td { +"${user.messageCount}" }
    td { +"${user.created}" }
    td { +"${user.lastMessage}" }
}

private const val CSS = """
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: system-ui, -apple-system, sans-serif; padding: 2rem; background: #f5f5f5; }
    h1 { margin-bottom: 1.5rem; color: #333; }
    table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
    th, td { padding: 0.75rem 1rem; text-align: left; border-bottom: 1px solid #eee; }
    th { background: #f8f9fa; font-weight: 600; color: #555; }
    tr:hover { background: #f8f9fa; }
    select { padding: 0.25rem 0.5rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; }
    .pagination { margin-top: 1rem; display: flex; align-items: center; gap: 1rem; justify-content: center; }
    .pagination button { padding: 0.5rem 1rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; }
    .pagination button:hover { background: #e9ecef; }
    .pagination span { color: #666; }
"""
