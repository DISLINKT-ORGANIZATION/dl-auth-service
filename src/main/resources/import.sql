INSERT INTO authorities (name) VALUES ('ROLE_ADMINISTRATOR');
INSERT INTO authorities (name) VALUES ('ROLE_AGENT');
INSERT INTO authorities (name) VALUES ('ROLE_USER');

insert into administrators (id, email, username, password) values (nextval('person_seq'), 'admin@gmail.com', 'admin', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
insert into agents (id, email, username, password) values (nextval('person_seq'), 'agent@gmail.com', 'agent', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'user@gmail.com', 'user', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'first', 'last', 913507200000, 0);

insert into user_authority (user_id, authority_id) values (1, 1);
insert into user_authority (user_id, authority_id) values (2, 2);
insert into user_authority (user_id, authority_id) values (3, 3);