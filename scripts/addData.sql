INSERT INTO users (id, login, salt, hash) VALUES 
(1, 'alice', 'saltAlice', '0ded4a676ee2fcd61ab5772e67ac33ef2ada6a929470cac9cb703cc9e6315c85');

INSERT INTO resources (id, name, maxVolume, parentId) VALUES 
(1, 'root', 100, NULL),
(2, 'A', 50, 1),
(3, 'B', 20, 2),
(4, 'C', 10, 3),
(5, 'D', 10, 1);

INSERT INTO permissions (userId, resourceId, availableActions) VALUES 
(1, 2, 'R--'),
(1, 3, 'R--'),
(1, 4, 'R--');
