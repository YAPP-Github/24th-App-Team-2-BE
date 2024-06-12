package com.xorker.xpr.auth.token

import com.xorker.xpr.BaseJpaEntity
import com.xorker.xpr.user.UserId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "auth_refresh_token")
internal class RefreshTokenJpaEntity : BaseJpaEntity() {
    @Id
    @Column(name = "token", nullable = false, columnDefinition = "BINARY(45)")
    lateinit var token: ByteArray
        protected set

    @Column(name = "user_id")
    var userId: Long = 0
        protected set

    @Column(name = "expired_at")
    lateinit var expiredAt: LocalDateTime
        protected set

    companion object {
        internal fun of(
            token: ByteArray,
            userId: UserId,
            expiredAt: LocalDateTime,
        ): RefreshTokenJpaEntity = RefreshTokenJpaEntity().apply {
            this.token = token
            this.userId = userId.value
            this.expiredAt = expiredAt
        }
    }
}
