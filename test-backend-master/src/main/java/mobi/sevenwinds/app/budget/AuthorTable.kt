package mobi.sevenwinds.app.budget

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object AuthorTable : IntIdTable("Author") {
    val fullName = varchar("fullName",255)
    val createdAt = datetime("created_at")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)
    var fullName by AuthorTable.fullName
    var createdAt by  AuthorTable.createdAt

    fun toResponse(): AuthorRecord {
        return AuthorRecord(fullName,createdAt)
    }
}
data class AuthorRecord(
    val fullName: String,
    val createdAt:DateTime
)
