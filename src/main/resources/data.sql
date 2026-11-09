INSERT INTO category(name)
VALUES ('Eurogames');
INSERT INTO category(name)
VALUES ('Ameritrash');
INSERT INTO category(name)
VALUES ('Familiar');

INSERT INTO author(name, nationality)
VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality)
VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality)
VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality)
VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality)
VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality)
VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id)
VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id)
VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id)
VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id)
VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id)
VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id)
VALUES ('Azul', '8', 3, 5);


INSERT INTO client(name)
VALUES ('JUAN MA');
INSERT INTO client(name)
VALUES ('LIONEL MESSI');
INSERT INTO client(name)
VALUES ('BLASCO IBAÑEZ');
INSERT INTO client(name)
VALUES ('CRISTIANO RONALDO');

INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (1, 1, '2020-01-01', '2020-01-06');
INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (5, 2, '2024-03-01', '2024-03-08');
INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (3, 4, '2026-05-01', '2026-05-06');
INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (2, 2, '2026-11-01', '2026-11-06');
INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (4, 1, '2020-10-01', '2026-10-06');
INSERT INTO loan (game_id, client_id, start_date, end_date)
VALUES (6, 2, '2023-01-01', '2023-01-06');
