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
    maxVolume INT DEFAULT 10,
    parentId INT,
    FOREIGN KEY (parentId) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE permissions (
    userId INT NOT NULL,
    resourceId INT NOT NULL,
    availableActions VARCHAR(3) NOT NULL,
    PRIMARY KEY (userId, resourceId),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (resourceId) REFERENCES resources(id) ON DELETE CASCADE
);
