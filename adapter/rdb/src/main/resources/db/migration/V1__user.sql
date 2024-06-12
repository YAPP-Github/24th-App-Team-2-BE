create table users
(
    user_id    bigint   NOT NULL AUTO_INCREMENT COMMENT 'PK',
    name       varchar(20) COMMENT '유저 이름 form 소셜 플랫폼',
    deleted    bit      NOT NULL COMMENT 'Soft Delete 용 필드',
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY PK_user (user_id),
    KEY        IDX_createdat(created_at),
    KEY        IDX_updatedat(updated_at)
) COMMENT '유저 정보';
