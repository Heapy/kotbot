package io.heapy.tgpt.bot.admin

import io.heapy.tgpt.bot.dao.AllowedUserDao
import io.heapy.tgpt.bot.dao.ApiCallDao
import io.heapy.tgpt.bot.dao.ThreadDao
import io.heapy.tgpt.bot.dao.ThreadMessageDao
import io.heapy.tgpt.infra.jdbc.TransactionProvider
import io.heapy.tgpt.infra.web.KtorRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.InputType
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.a
import kotlinx.html.script
import kotlinx.html.span
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
    private val allowedUserDao: AllowedUserDao,
    private val apiCallDao: ApiCallDao,
    private val threadDao: ThreadDao,
    private val threadMessageDao: ThreadMessageDao,
    private val transactionProvider: TransactionProvider,
) : KtorRoute {
    override fun Routing.install() {
        // User management page
        get("/admin") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title("TGPT Admin")
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
                    nav {
                        a(href = "/admin") { +"Users" }
                        a(href = "/admin/stats") { +"Statistics" }
                    }
                    h1 { +"TGPT Admin - Users" }
                    div(classes = "add-form") {
                        id = "add-user-form"
                        h2 { +"Add User" }
                        form {
                            attributes["hx-post"] = "/admin/users"
                            attributes["hx-target"] = "#users-container"
                            attributes["hx-swap"] = "innerHTML"
                            input(type = InputType.number, name = "telegram_id") {
                                placeholder = "Telegram ID"
                                required = true
                            }
                            input(type = InputType.text, name = "username") {
                                placeholder = "Username (optional)"
                            }
                            input(type = InputType.text, name = "display_name") {
                                placeholder = "Display Name (optional)"
                            }
                            button(type = kotlinx.html.ButtonType.submit) { +"Add User" }
                        }
                    }
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

        // Users list
        get("/admin/users") {
            val page = call.queryParameters["page"]?.toIntOrNull() ?: 0
            val pageSize = 50
            val offset = page * pageSize

            val (users, totalCount) = transactionProvider.transaction {
                val users = allowedUserDao.listAll(limit = pageSize, offset = offset)
                val count = allowedUserDao.countAll()
                users to count
            }

            val totalPages = (totalCount + pageSize - 1) / pageSize

            call.respondHtmlFragment {
                table {
                    thead {
                        tr {
                            th { +"ID" }
                            th { +"Telegram ID" }
                            th { +"Username" }
                            th { +"Display Name" }
                            th { +"Active" }
                            th { +"Created" }
                            th { +"Actions" }
                        }
                    }
                    tbody {
                        for (user in users) {
                            tr {
                                id = "user-row-${user.id}"
                                td { +"${user.id}" }
                                td { +"${user.telegramId}" }
                                td { +(user.username ?: "-") }
                                td { +(user.displayName ?: "-") }
                                td { +"${user.isActive}" }
                                td { +"${user.created}" }
                                td {
                                    if (user.isActive == true) {
                                        button {
                                            attributes["hx-delete"] = "/admin/users/${user.id}"
                                            attributes["hx-target"] = "#users-container"
                                            attributes["hx-swap"] = "innerHTML"
                                            attributes["hx-confirm"] = "Deactivate this user?"
                                            +"Deactivate"
                                        }
                                    } else {
                                        span { +"Inactive" }
                                    }
                                }
                            }
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

        // Add user
        post("/admin/users") {
            val formParameters = call.receiveParameters()
            val telegramId = formParameters["telegram_id"]?.toLongOrNull()
            if (telegramId == null) {
                call.respondText("Invalid Telegram ID", status = HttpStatusCode.BadRequest)
                return@post
            }
            val username = formParameters["username"]?.takeIf { it.isNotBlank() }
            val displayName = formParameters["display_name"]?.takeIf { it.isNotBlank() }

            transactionProvider.transaction {
                allowedUserDao.addUser(telegramId, username, displayName)
            }

            // Re-render user list
            val (users, totalCount) = transactionProvider.transaction {
                val users = allowedUserDao.listAll(limit = 50, offset = 0)
                val count = allowedUserDao.countAll()
                users to count
            }

            call.respondHtmlFragment {
                table {
                    thead {
                        tr {
                            th { +"ID" }
                            th { +"Telegram ID" }
                            th { +"Username" }
                            th { +"Display Name" }
                            th { +"Active" }
                            th { +"Created" }
                            th { +"Actions" }
                        }
                    }
                    tbody {
                        for (user in users) {
                            tr {
                                id = "user-row-${user.id}"
                                td { +"${user.id}" }
                                td { +"${user.telegramId}" }
                                td { +(user.username ?: "-") }
                                td { +(user.displayName ?: "-") }
                                td { +"${user.isActive}" }
                                td { +"${user.created}" }
                                td {
                                    if (user.isActive == true) {
                                        button {
                                            attributes["hx-delete"] = "/admin/users/${user.id}"
                                            attributes["hx-target"] = "#users-container"
                                            attributes["hx-swap"] = "innerHTML"
                                            attributes["hx-confirm"] = "Deactivate this user?"
                                            +"Deactivate"
                                        }
                                    } else {
                                        span { +"Inactive" }
                                    }
                                }
                            }
                        }
                    }
                }
                span { +"$totalCount users" }
            }
        }

        // Deactivate user
        delete("/admin/users/{id}") {
            val userId = call.pathParameters["id"]?.toLongOrNull()
            if (userId == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@delete
            }

            transactionProvider.transaction {
                allowedUserDao.deactivateUser(userId)
            }

            // Re-render user list
            val (users, totalCount) = transactionProvider.transaction {
                val users = allowedUserDao.listAll(limit = 50, offset = 0)
                val count = allowedUserDao.countAll()
                users to count
            }

            call.respondHtmlFragment {
                table {
                    thead {
                        tr {
                            th { +"ID" }
                            th { +"Telegram ID" }
                            th { +"Username" }
                            th { +"Display Name" }
                            th { +"Active" }
                            th { +"Created" }
                            th { +"Actions" }
                        }
                    }
                    tbody {
                        for (user in users) {
                            tr {
                                id = "user-row-${user.id}"
                                td { +"${user.id}" }
                                td { +"${user.telegramId}" }
                                td { +(user.username ?: "-") }
                                td { +(user.displayName ?: "-") }
                                td { +"${user.isActive}" }
                                td { +"${user.created}" }
                                td {
                                    if (user.isActive == true) {
                                        button {
                                            attributes["hx-delete"] = "/admin/users/${user.id}"
                                            attributes["hx-target"] = "#users-container"
                                            attributes["hx-swap"] = "innerHTML"
                                            attributes["hx-confirm"] = "Deactivate this user?"
                                            +"Deactivate"
                                        }
                                    } else {
                                        span { +"Inactive" }
                                    }
                                }
                            }
                        }
                    }
                }
                span { +"$totalCount users" }
            }
        }

        // Statistics page
        get("/admin/stats") {
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title("TGPT Stats")
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
                    nav {
                        a(href = "/admin") { +"Users" }
                        a(href = "/admin/stats") { +"Statistics" }
                    }
                    h1 { +"TGPT Admin - Statistics" }
                    div {
                        id = "stats-summary"
                        attributes["hx-get"] = "/admin/stats/summary"
                        attributes["hx-trigger"] = "load"
                        attributes["hx-swap"] = "innerHTML"
                        +"Loading summary..."
                    }
                    h2 { +"Per-User Statistics" }
                    div {
                        id = "stats-users"
                        attributes["hx-get"] = "/admin/stats/users"
                        attributes["hx-trigger"] = "load"
                        attributes["hx-swap"] = "innerHTML"
                        +"Loading user stats..."
                    }
                }
            }
        }

        // Stats summary
        get("/admin/stats/summary") {
            val (totalSpend, totalCalls, totalThreads, totalMessages) = transactionProvider.transaction {
                val spend = apiCallDao.totalSpend()
                val calls = apiCallDao.totalApiCalls()
                val threads = threadDao.countThreads()
                val messages = threadMessageDao.countMessages()
                StatsData(spend, calls, threads, messages)
            }

            call.respondHtmlFragment {
                div(classes = "stats-cards") {
                    div(classes = "stat-card") {
                        div(classes = "stat-value") { +"$${"%.4f".format(totalSpend)}" }
                        div(classes = "stat-label") { +"Total Spend" }
                    }
                    div(classes = "stat-card") {
                        div(classes = "stat-value") { +"$totalCalls" }
                        div(classes = "stat-label") { +"API Calls" }
                    }
                    div(classes = "stat-card") {
                        div(classes = "stat-value") { +"$totalThreads" }
                        div(classes = "stat-label") { +"Threads" }
                    }
                    div(classes = "stat-card") {
                        div(classes = "stat-value") { +"$totalMessages" }
                        div(classes = "stat-label") { +"Messages" }
                    }
                }
            }
        }

        // Per-user stats
        get("/admin/stats/users") {
            val userStats = transactionProvider.transaction {
                apiCallDao.perUserStats()
            }

            call.respondHtmlFragment {
                table {
                    thead {
                        tr {
                            th { +"Telegram ID" }
                            th { +"Username" }
                            th { +"Prompt Tokens" }
                            th { +"Completion Tokens" }
                            th { +"Total Tokens" }
                            th { +"Cost ($)" }
                            th { +"Threads" }
                        }
                    }
                    tbody {
                        for (stat in userStats) {
                            tr {
                                td { +"${stat.telegramUserId}" }
                                td { +(stat.username ?: "-") }
                                td { +"${stat.promptTokens}" }
                                td { +"${stat.completionTokens}" }
                                td { +"${stat.totalTokens}" }
                                td { +"${"%.6f".format(stat.costUsd)}" }
                                td { +"${stat.threads}" }
                            }
                        }
                    }
                }
            }
        }
    }

    private data class StatsData(
        val totalSpend: java.math.BigDecimal,
        val totalCalls: Int,
        val totalThreads: Int,
        val totalMessages: Int,
    )
}

private const val CSS = """
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: system-ui, -apple-system, sans-serif; padding: 2rem; background: #f5f5f5; }
    h1 { margin-bottom: 1.5rem; color: #333; }
    h2 { margin: 1.5rem 0 1rem; color: #333; }
    nav { margin-bottom: 1.5rem; display: flex; gap: 1rem; }
    nav a { color: #0066cc; text-decoration: none; font-weight: 500; padding: 0.5rem 1rem; border-radius: 4px; background: white; border: 1px solid #ddd; }
    nav a:hover { background: #e9ecef; }
    table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
    th, td { padding: 0.75rem 1rem; text-align: left; border-bottom: 1px solid #eee; }
    th { background: #f8f9fa; font-weight: 600; color: #555; }
    tr:hover { background: #f8f9fa; }
    button { padding: 0.5rem 1rem; border: 1px solid #ddd; border-radius: 4px; background: white; cursor: pointer; }
    button:hover { background: #e9ecef; }
    .pagination { margin-top: 1rem; display: flex; align-items: center; gap: 1rem; justify-content: center; }
    .pagination span { color: #666; }
    .add-form { background: white; padding: 1.5rem; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 1.5rem; }
    .add-form form { display: flex; gap: 0.5rem; align-items: end; margin-top: 0.75rem; }
    .add-form input { padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; }
    .stats-cards { display: flex; gap: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap; }
    .stat-card { background: white; padding: 1.5rem; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); flex: 1; min-width: 150px; text-align: center; }
    .stat-value { font-size: 1.5rem; font-weight: 700; color: #333; }
    .stat-label { font-size: 0.875rem; color: #666; margin-top: 0.25rem; }
"""
