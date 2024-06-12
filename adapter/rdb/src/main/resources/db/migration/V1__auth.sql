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

create table auth_user
(
    auth_user_id     bigint       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    platform         varchar(20)  NOT NULL COMMENT '소셜 로그인 업체',
    platform_user_id varchar(100) NOT NULL COMMENT '소셜 로그인의 User Id',
    user_id          bigint       NOT NULL COMMENT 'FK - Users',
    created_at       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY PK_auth_user (auth_user_id),
    KEY              IDX_createdat(created_at),
    KEY              IDX_updatedat(updated_at),
    KEY              IDX_userid(user_id),
    KEY              IDX_platformUserId(platform_user_id)
) COMMENT '유저 소셜 로그인 정보';

create table auth_refresh_token
(
    token                 binary(45) NOT NULL COMMENT 'PK, 리프래쉬 토큰 내용물 (60자)',
    user_id               bigint   NOT NULL COMMENT 'FK - Users, 토큰 주인',
    expired_at            datetime NOT NULL COMMENT '리프래쉬 토큰 만료 날짜',
    created_at            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY PK_token (token),
    KEY                   IDX_userid(user_id),
    KEY                   IDX_createdat(created_at),
    KEY                   IDX_updatedat(updated_at)
) COMMENT '발급한 Refresh Token 목록';
