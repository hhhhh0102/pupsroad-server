CREATE TABLE IF NOT EXISTS tbl_walk_to_resource
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    walk_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_walk.id',
    resource_id             BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_pet.id',

    -- columns

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
    CONSTRAINT tbl_walk_to_resource_walk_fk FOREIGN KEY (walk_id) REFERENCES tbl_walk(id),
    CONSTRAINT tbl_walk_to_resource_resource_fk FOREIGN KEY (resource_id) REFERENCES tbl_resource(id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'walk_to_resource table';