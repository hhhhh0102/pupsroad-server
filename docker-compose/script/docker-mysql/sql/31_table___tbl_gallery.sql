CREATE TABLE IF NOT EXISTS tbl_gallery
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    user_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_user.id',

    -- columns
    name                    VARCHAR(16)                                                                     NULL,
    is_default              CHAR                                                                        NOT NULL,

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
    INDEX tbl_gallery_user_id_index (user_id),
    CONSTRAINT tbl_gallery_fk FOREIGN KEY (user_id) REFERENCES tbl_user(id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'gallery table';