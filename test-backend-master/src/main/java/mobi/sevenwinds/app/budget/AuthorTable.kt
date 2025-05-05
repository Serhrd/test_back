package mobi.sevenwinds.app.budget

import com.fasterxml.jackson.annotation.JsonFormat
import org.jetbrains.annotations.Nullable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime
import java.util.*

object AuthorTable : IntIdTable("Author") {
    val fullName = varchar("fullname",255)

    val createdAt = datetime("created_at")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)
    var fullName by AuthorTable.fullName
    var createdAt by  AuthorTable.createdAt

    fun toResponse(): AuthorRecord {
        return AuthorRecord(fullName,createdAt.toString("yyyy-MM-dd'T'HH:mm"))
    }


}
data class AuthorRecord(
    val fullName: String,

    val createdAt: String
)
