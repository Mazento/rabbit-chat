CREATE
USER 'rchat_user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON rabbit_chat.* TO
'rchat_user'@'%';
CREATE TABLE chat_message
(
    id             INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(30) NOT NULL,
    text           VARCHAR(500),
    date           DATETIME,
    file_record_id INTEGER
);
CREATE TABLE file_record
(
    id       INTEGER      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    url      VARCHAR(255),
    size     BIGINT,
    status   ENUM('INIT', 'PROCESSING', 'SUCCESS', 'FAILURE')
);
