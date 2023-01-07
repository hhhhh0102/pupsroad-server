CREATE TABLE IF NOT EXISTS tbl_walk_to_pet
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    walk_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_walk.id',
    pet_id                  BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_pet.id',

    -- columns
    calorie                 DOUBLE                                                                      NOT NULL,

    -- common columns
    deleted                 CHAR            DEFAULT 'N'                                                 NOT NULL,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP                                   NOT NULL,
    updated_at              DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       NOT NULL,
    deleted_at              DATETIME                                                                        NULL,
    created_by              BIGINT UNSIGNED                                                                 NULL,
    updated_by              BIGINT UNSIGNED                                                                 NULL,
    deleted_by              BIGINT UNSIGNED                                                                 NULL,

    -- constraints
    INDEX tbl_walk_to_pet_walk_id_index (walk_id),
    CONSTRAINT tbl_walk_to_pet_walk_fk FOREIGN KEY (walk_id) REFERENCES tbl_walk(id),
    CONSTRAINT tbl_walk_to_pet_pet_fk FOREIGN KEY (pet_id) REFERENCES tbl_pet(id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'walk table';