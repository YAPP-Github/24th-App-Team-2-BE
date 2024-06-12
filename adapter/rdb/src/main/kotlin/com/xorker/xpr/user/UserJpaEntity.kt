package com.xorker.xpr.user

import com.xorker.xpr.BaseJpaEntity
import com.xorker.xpr.exception.InvalidUserStatusException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "users")
class UserJpaEntity : BaseJpaEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false)
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
        fun of(
            id: Long,
            name: String,
        ): UserJpaEntity = UserJpaEntity().apply {
            this.id = id
            this.name = name
        }

        fun from(user: User): UserJpaEntity =
            UserJpaEntity().apply {
                this.id = user.id.value
                this.name = user.name
            }
    }
}

fun UserJpaEntity.toDomain(): User = User(
    id = UserId(this.id),
    name = this.name ?: throw InvalidUserStatusException,
)
