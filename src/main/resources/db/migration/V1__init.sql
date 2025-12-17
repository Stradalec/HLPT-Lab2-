DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL
);

CREATE TABLE resources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_volume INT DEFAULT 10,
    parent_id INT,
    FOREIGN KEY (parent_id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE permissions (
    user_id INT NOT NULL,
    resource_id INT NOT NULL,
    available_actions VARCHAR(3) NOT NULL,
    PRIMARY KEY (user_id, resource_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);