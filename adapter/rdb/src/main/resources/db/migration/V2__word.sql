create table word
(
    word_id     bigint      NOT NULL AUTO_INCREMENT COMMENT 'PK',
    category_ko varchar(50) NOT NULL COMMENT '카테고리 (한국어)',
    category_en varchar(50) NOT NULL COMMENT '카테고리 (영어)',
    keyword_ko  varchar(50) NOT NULL COMMENT '키워드 (한국어)',
    keyword_en  varchar(50) NOT NULL COMMENT '키워드 (영어)',
    PRIMARY KEY PK_word (word_id)
) COMMENT '마피아 단어';
