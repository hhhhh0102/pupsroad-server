CREATE TABLE IF NOT EXISTS tbl_pet
(
    -- primary key
    id                  BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- columns
    specie                      VARCHAR(16)             NOT NULL,
    gender                      VARCHAR(6)              NOT NULL,
    neutralized                 CHAR                    NOT NULL,
    age                         INT                     NULL,
    weight                      DOUBLE                  NULL,

    -- fk columns
    user_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_user.id',
#     profile_resource_id     BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_resource.id',

    -- common columns
    version                 BIGINT UNSIGNED DEFAULT '0'                                                 NOT NULL,
    deleted                 CHAR            DEFAULT 'N'                                                 NOT NULL,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP                                   NOT NULL,
    updated_at              DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       NOT NULL,
    deleted_at              DATETIME                                                                        NULL,
    created_by              BIGINT UNSIGNED                                                                 NULL,
    updated_by              BIGINT UNSIGNED                                                                 NULL,
    deleted_by              BIGINT UNSIGNED                                                                 NULL,

    -- constraints
    INDEX tbl_pet_user_id_index (user_id),
    CONSTRAINT tbl_pet_fk FOREIGN KEY (user_id) REFERENCES tbl_user(id)
#     CONSTRAINT tbl_pet_profile_resource_id_fk FOREIGN KEY (profile_resource_id) REFERENCES tbl_resource(id)

    -- common fk columns

    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'pet table';