CREATE TABLE IF NOT EXISTS tbl_gallery_to_resource
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    gallery_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_gallery.id',
    resource_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_resource.id',

    -- columns

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
    INDEX tbl_gallery_to_resource_gallery_id_index (gallery_id),
    INDEX tbl_gallery_to_resource_resource_id_index (resource_id),
    CONSTRAINT tbl_gallery_to_resource_gallery_id_fk FOREIGN KEY (gallery_id) REFERENCES tbl_gallery(id),
    CONSTRAINT tbl_gallery_to_resource_resource_id_fk FOREIGN KEY (resource_id) REFERENCES tbl_resource(id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'gallery table';