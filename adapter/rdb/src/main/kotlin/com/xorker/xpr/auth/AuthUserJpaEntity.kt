package com.xorker.xpr.auth

import com.xorker.xpr.BaseJpaEntity
import com.xorker.xpr.user.UserJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType

@Entity
@Table(name = "auth_user")
internal class AuthUserJpaEntity : BaseJpaEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_user_id", nullable = false)
    var id: Long = 0
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", columnDefinition = "varchar(20)")
    lateinit var platform: AuthPlatform
        protected set

    @Column(name = "platform_user_id")
    lateinit var platformUserId: String
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Cascade(CascadeType.PERSIST)
    lateinit var user: UserJpaEntity

    companion object {
        internal fun of(
            platform: AuthPlatform,
            platformUserId: String,
            user: UserJpaEntity,
        ): AuthUserJpaEntity = AuthUserJpaEntity().apply {
            this.platform = platform
            this.platformUserId = platformUserId
            this.user = user
        }
    }
}
