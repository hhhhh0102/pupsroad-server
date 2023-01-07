CREATE TABLE IF NOT EXISTS tbl_pet
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    user_id                 BIGINT UNSIGNED                                                             NOT NULL
        COMMENT 'tbl_user.id',
    profile_resource_id     BIGINT UNSIGNED                                                                 NULL
        COMMENT '반려견의 프로필 이미지 (tbl_resource.id) fk 제약조건은 추후 적용',

    -- columns
    name                    VARCHAR(50)                                                                 NOT NULL
        COMMENT '반려견의 이름',
    breed                   VARCHAR(50)                                                                 NOT NULL
        COMMENT '견종',
    birthdate               DATETIME                                                                        NULL
        COMMENT '반려견의 생년월일',
    weight                  DOUBLE                                                                          NULL
        COMMENT '반려견의 몸무게 (kg)',
    gender                  VARCHAR(6)                                                                  NOT NULL
        COMMENT '반려견의 성별',
    neutralized             CHAR                                                                        NOT NULL
        COMMENT '반려견의 중성화 여부',

    -- common columns
    deleted                 CHAR            DEFAULT 'N'                                                 NOT NULL,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP                                   NOT NULL,
    updated_at              DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       NOT NULL,
    deleted_at              DATETIME                                                                        NULL,
    created_by              BIGINT UNSIGNED                                                                 NULL,
    updated_by              BIGINT UNSIGNED                                                                 NULL,
    deleted_by              BIGINT UNSIGNED                                                                 NULL,

    -- constraints
    INDEX tbl_pet_user_id_index (user_id),
    CONSTRAINT tbl_pet_user_fk FOREIGN KEY (user_id) REFERENCES tbl_user(id),
    CONSTRAINT tbl_pet_resource_fk FOREIGN KEY (profile_resource_id) REFERENCES tbl_resource(id)

    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'walk table';