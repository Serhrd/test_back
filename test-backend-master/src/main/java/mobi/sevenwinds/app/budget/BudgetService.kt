package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.budget.AuthorEntity.Companion.findById
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                if (body.authorId != null) {
                    this.authorId = body.authorId
                }
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val paginatedQuery = BudgetTable
                .select { BudgetTable.year eq param.year }
                .limit(param.limit, param.offset)
                .orderBy(BudgetTable.month)
                .orderBy(BudgetTable.amount,SortOrder.DESC)

            val data = BudgetEntity.wrapRows(paginatedQuery).map { it.toResponse() }

            val total = BudgetTable
                .select { BudgetTable.year eq param.year }
                .count()

            val sumByType = BudgetTable
                .select { BudgetTable.year eq param.year }
                .groupBy { it[BudgetTable.type] }

                .mapValues { (_, rows) ->
                    rows.sumOf { it[BudgetTable.amount] }
                }
                .mapKeys { it.key.name }

            BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}