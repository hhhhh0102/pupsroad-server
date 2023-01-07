CREATE TABLE IF NOT EXISTS tbl_auth_private
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    user_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_user.id',

    -- columns
    principal_id            VARCHAR(100)                                                                    NULL,
    email                   VARCHAR(100)                                                                    NULL COMMENT 'User email',
    phone                   VARCHAR(30)                                                                    NULL COMMENT 'User phone number',
    provider                VARCHAR(255)                                                                    NULL COMMENT 'PHONE / OAUTH_GOOGLE / OAUTH_KAKAO',
    verified                 VARCHAR(4) DEFAULT 'N'                                                          NULL COMMENT 'Email verification' ,

    -- common columns
    deleted                 CHAR            DEFAULT 'N'                                                 NOT NULL,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP                                   NOT NULL,
    updated_at              DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       NOT NULL,
    deleted_at              DATETIME                                                                        NULL,
    created_by              BIGINT UNSIGNED                                                                 NULL,
    updated_by              BIGINT UNSIGNED                                                                 NULL,
    deleted_by              BIGINT UNSIGNED                                                                 NULL,

    -- constraints
    INDEX tbl_auth_private_user_id_index (user_id),
    CONSTRAINT tbl_auth_private_uindex UNIQUE KEY (user_id, provider),
    CONSTRAINT tbl_auth_private_user_fk FOREIGN KEY (user_id) REFERENCES tbl_user(id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'user auth private table';