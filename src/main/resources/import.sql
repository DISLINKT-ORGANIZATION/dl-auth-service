INSERT INTO authorities (name) VALUES ('ROLE_ADMINISTRATOR');
INSERT INTO authorities (name) VALUES ('ROLE_AGENT');
INSERT INTO authorities (name) VALUES ('ROLE_USER');

insert into administrators (email, username, password) values ('admin@gmail.com', 'admin', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
insert into agents (email, username, password) values ('agent@gmail.com', 'agent', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
insert into users (email, username, password, first_name, last_name, birth_date, gender) values ('user@gmail.com', 'user', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'first', 'last', 'birth', 0);

insert into administrator_authority (administrator_id, authority_id) values (1, 1);
insert into agent_authority (agent_id, authority_id) values (1, 2);
insert into user_authority (user_id, authority_id) values (1, 3);