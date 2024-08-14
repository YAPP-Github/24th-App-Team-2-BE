package com.xorker.draw.user

import com.xorker.draw.BaseJpaEntity
import com.xorker.draw.exception.InvalidUserStatusException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
internal class UserJpaEntity : BaseJpaEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    var id: Long = 0
        protected set

    @Column(name = "name")
    var name: String? = null
        protected set

    @Column(name = "deleted")
    var deleted: Boolean = false
        protected set

    fun withdrawal() {
        name = null
        deleted = true
    }

    companion object {
        internal fun of(id: Long): UserJpaEntity =
            UserJpaEntity().apply { this.id = id }

        internal fun of(name: String): UserJpaEntity =
            UserJpaEntity().apply { this.name = name }

        internal fun from(user: User): UserJpaEntity =
            UserJpaEntity().apply {
                this.id = user.id.value
                this.name = user.name
            }

        internal fun of(
            id: Long,
            name: String,
        ): UserJpaEntity = UserJpaEntity().apply {
            this.id = id
            this.name = name
        }
    }
}

internal fun UserJpaEntity.toDomain(): User = User(
    id = UserId(this.id),
    name = this.name ?: throw InvalidUserStatusException,
)
