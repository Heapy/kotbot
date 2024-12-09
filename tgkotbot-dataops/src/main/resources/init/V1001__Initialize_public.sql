INSERT INTO garbage_messages (id, text, type, match, action)
VALUES (1, 'hi', 'hello message', 'EXACT', 'WARN'),
       (2, 'hello', 'hello message', 'EXACT', 'WARN'),
       (3, 'привет', 'hello message', 'EXACT', 'WARN'),
       (4, 'приветствую', 'hello message', 'EXACT', 'WARN'),
       (5, 'здравствуйте', 'hello message', 'EXACT', 'WARN'),
       (6, 'long time no see.', 'longtime no see spam', 'EXACT', 'BAN'),
       (7, 'what''s going on?', 'longtime no see spam', 'EXACT', 'BAN'),
       (8, 'how is everything?', 'longtime no see spam', 'EXACT', 'BAN');
