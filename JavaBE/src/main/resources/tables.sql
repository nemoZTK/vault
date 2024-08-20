CREATE TABLE vault_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE repositories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    vault_user_id BIGINT NOT NULL,
    size BIGINT DEFAULT 0,
    path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (vault_user_id) REFERENCES vault_users(id)
);

CREATE TABLE sections (
    id SERIAL PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (repository_id) REFERENCES repositories(id)
);

CREATE TABLE folders (
    id SERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL,
    parent_id BIGINT DEFAULT NULL,
    name VARCHAR(255) NOT NULL,
    size BIGINT DEFAULT 0,
    path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (section_id) REFERENCES sections(id),
    FOREIGN KEY (parent_id) REFERENCES folders(id)
);

CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    folder_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    content BYTEA,
    type VARCHAR(255),
    language VARCHAR(255),
    size BIGINT DEFAULT 0,
    path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (folder_id) REFERENCES folders(id)
);





//old
CREATE TABLE vault_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    path VARCHAR(255) NULL
);


