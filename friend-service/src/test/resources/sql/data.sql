INSERT INTO friends(id, user_id, subscriber_id, status, is_deleted)
VALUES (1, 3, 4, 'FRIEND', false),
       (2, 6, 9, 'SUBSCRIBER', false),
       (3, 5, 2, 'SUBSCRIBER', false);

SELECT SETVAL('public.friends_id_seq', (SELECT MAX(id) FROM friends));