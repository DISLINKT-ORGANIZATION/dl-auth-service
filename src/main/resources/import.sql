insert into authorities (name) values ('ROLE_ADMINISTRATOR');
insert into authorities (name) values ('ROLE_AGENT');
insert into authorities (name) values ('ROLE_USER');

-- admins
insert into administrators (id, email, username, password) values (nextval('person_seq'), 'hopper@gmail.com', 'hopper-crime-stopper', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
-- agents
insert into agents (id, email, username, password) values (nextval('person_seq'), 'joyce@gmail.com', 'detective-byers', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
insert into agents (id, email, username, password) values (nextval('person_seq'), 'johnatan@gmail.com', 'stoned-johnny', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK');
-- users
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'max@gmail.com', 'mad-max', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Max', 'Mayfield', 913507200000, 0);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'el@gmail.com', 'bitchin-011', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Jane', 'Hopper', 913507200000, 0);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'mike@gmail.com', 'mike-bike', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Mike', 'Wheeler', 913507200000, 1);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'will@gmail.com', 'will-the-wizard', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Will', 'Byers', 913507200000, 1);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'dustin@gmail.com', 'dusty-bun', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Dustin', 'Handerson', 913507200000, 1);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'lucas@gmail.com', 'lucas-shoots', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Lucas', 'Sinclair', 913507200000, 1);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'steve@gmail.com', 'mommy-steve', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Steve', 'Harrington', 913507200000, 1);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'nancy@gmail.com', 'fancy-nancy', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Nancy', 'Wheeler', 913507200000, 0);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'erica@gmail.com', 'erica-america', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Erica', 'Sinclair', 913507200000, 0);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'robin@gmail.com', 'crackin-robin', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Robin', 'Buckley', 913507200000, 0);
insert into users (id, email, username, password, first_name, last_name, birth_date, gender) values (nextval('person_seq'), 'vecna@gmail.com', 'henry-001-vecna', '$2y$12$NFN7DJUX1lFfaDX1tc9/6uBtgls9SZOU9iwjhrlXJc0xr471vgKAK', 'Henry', 'Creel', 913507200000, 1);

insert into user_authority (user_id, authority_id) values (1, 1);
insert into user_authority (user_id, authority_id) values (2, 2);
insert into user_authority (user_id, authority_id) values (3, 2);
insert into user_authority (user_id, authority_id) values (4, 3);
insert into user_authority (user_id, authority_id) values (5, 3);
insert into user_authority (user_id, authority_id) values (6, 3);
insert into user_authority (user_id, authority_id) values (7, 3);
insert into user_authority (user_id, authority_id) values (8, 3);
insert into user_authority (user_id, authority_id) values (9, 3);
insert into user_authority (user_id, authority_id) values (10, 3);
insert into user_authority (user_id, authority_id) values (11, 3);
insert into user_authority (user_id, authority_id) values (12, 3);
insert into user_authority (user_id, authority_id) values (13, 3);
insert into user_authority (user_id, authority_id) values (14, 3);
