CREATE TABLE IF NOT EXISTS tbl_user
(
    -- primary key
    id                  BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- columns
    client_id           VARCHAR(30)                     NOT NULL,
    nickname            VARCHAR(100)                        NULL,
    role                VARCHAR(16)                     NOT NULL,

    -- fk columns
#     profile_resource_id     BIGINT UNSIGNED                      NOT NULL COMMENT 'tbl_resource.id',

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
    CONSTRAINT tbl_user_client_id_uindex UNIQUE KEY (client_id),
    CONSTRAINT tbl_user_nickname_uindex UNIQUE KEY (nickname)
#     CONSTRAINT tbl_user_fk FOREIGN KEY (profile_resource_id) REFERENCES tbl_resource(id)

    -- common fk columns

    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'user table';