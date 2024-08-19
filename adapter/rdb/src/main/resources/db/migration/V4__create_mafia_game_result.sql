create table mafia_game_result
(
    mafia_game_result_id bigint   NOT NULL AUTO_INCREMENT COMMENT 'PK',
    game_result          text     NOT NULL COMMENT 'Serialized 마피아 게임 결과',
    created_at           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY PK_mafia_game_result (mafia_game_result_id),
    KEY                  IDX_createdat(created_at),
    KEY                  IDX_updatedat(updated_at)
) COMMENT '마피아 게임 결과';
