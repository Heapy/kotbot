package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.enums.JobExecutionStatus
import io.heapy.kotbot.database.tables.JobExecution.Companion.JOB_EXECUTION
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import java.time.LocalDateTime

class JobExecutionDao {
    context(_: TransactionContext)
    suspend fun getLastExecution(jobName: String): LocalDateTime? = useTx {
        dslContext
            .select(JOB_EXECUTION.STARTED)
            .from(JOB_EXECUTION)
            .where(JOB_EXECUTION.JOB_NAME.eq(jobName))
            .and(JOB_EXECUTION.STATUS.eq(JobExecutionStatus.COMPLETED))
            .orderBy(JOB_EXECUTION.STARTED.desc())
            .limit(1)
            .fetchOne()
            ?.getValue(JOB_EXECUTION.STARTED)
    }

    context(_: TransactionContext)
    suspend fun startExecution(jobName: String): Long = useTx {
        dslContext
            .insertInto(
                JOB_EXECUTION,
                JOB_EXECUTION.JOB_NAME,
                JOB_EXECUTION.STARTED,
                JOB_EXECUTION.STATUS,
            )
            .values(
                jobName,
                LocalDateTime.now(),
                JobExecutionStatus.RUNNING,
            )
            .returning(JOB_EXECUTION.ID)
            .fetchOne()
            ?.getValue(JOB_EXECUTION.ID)
            ?: error("Failed to insert job execution")
    }

    context(_: TransactionContext)
    suspend fun completeExecution(id: Long) = useTx {
        dslContext
            .update(JOB_EXECUTION)
            .set(JOB_EXECUTION.FINISHED, LocalDateTime.now())
            .set(JOB_EXECUTION.STATUS, JobExecutionStatus.COMPLETED)
            .where(JOB_EXECUTION.ID.eq(id))
            .execute()
    }

    context(_: TransactionContext)
    suspend fun failExecution(id: Long) = useTx {
        dslContext
            .update(JOB_EXECUTION)
            .set(JOB_EXECUTION.FINISHED, LocalDateTime.now())
            .set(JOB_EXECUTION.STATUS, JobExecutionStatus.FAILED)
            .where(JOB_EXECUTION.ID.eq(id))
            .execute()
    }
}
