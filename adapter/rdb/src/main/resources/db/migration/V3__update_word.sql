drop table word;
create table word
(
    word_id        bigint       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    locale         varchar(20)  NOT NULL COMMENT '국가',
    category       varchar(100) NOT NULL COMMENT '카테고리',
    keyword        varchar(100) NOT NULL COMMENT '키워드',
    parent_word_id bigint COMMENT '동의어',
    PRIMARY KEY PK_word (word_id)
) COMMENT '마피아 단어';
