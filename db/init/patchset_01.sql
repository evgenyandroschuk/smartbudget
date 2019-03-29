

create table t_user(
  id int primary key,
  description varchar(200),
  is_active boolean default true
);

insert  into t_user(id, description) values (1, 'First user');


-- t_user_system_params
create table t_user_system_params (
  id SERIAL primary key,
  user_id int references t_user(id),
  system_param_id int not null ,
  system_value numeric(10,2) not null,
  description varchar(200),
  update_date date not null ,
  UNIQUE(user_id, system_param_id)
);

insert into t_user_system_params (user_id, system_param_id, system_value, description, update_date)
values(1, 1, 20000.00, 'Expenses. Opening balance', now());


--currency
create table t_currency (
    id SERIAL primary key,
    user_id int references t_user(id),
    currency_id int not null,
    description varchar(100) not null,
    update_date date not null,
    price numeric(10,2) not null,
    unique(user_id, currency_id)
);

insert into t_currency(user_id, currency_id, description, update_date, price)
values (1, 1, 'Dollar', now(), 65.03);

insert into t_currency(user_id, currency_id, description, update_date, price)
values (1, 2, 'Euro', now(), 75.03);



create table t_vehicle_service_type(
  id SERIAL primary key,
  user_id int references t_user(id),
  service_type_id int not null,
  description varchar(200) not null,
  unique (user_id, service_type_id)
);


insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 1, 'Страховка');
insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 2, 'Сервис');
insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 3, 'Запчасти');



create table t_vehicle (
  id SERIAL primary key,
  user_id int references t_user(id),
  vehicle_id int not null,
  description varchar(200),
  license_plate varchar(20),
  vin varchar(200),
  sts varchar(200),
  unique (user_id, vehicle_id)
);

insert into t_vehicle(user_id, vehicle_id, description, license_plate, vin, sts)
    values (1, 1, 'VW Tiguan', 'Е222ЕЕ777', 'vin nr', 'sts nr');

insert into t_vehicle(user_id, vehicle_id, description, license_plate, vin, sts)
values (1, 2, 'BMW G310R', '2525ВМ77', 'vin nr', 'sts nr');


create sequence vehicle_seq maxvalue 999999999 start 1;

create table vehicle_data (
  id bigint primary key,
  user_id int references t_user(id),
  vehicle_id int references t_vehicle(id),
  vehicle_service_type_id int  references t_vehicle_service_type(id),
  description varchar(1000),
  mile_age integer,
  price numeric(10,2),
  update_date date
);

insert into vehicle_data(id, user_id, vehicle_id, vehicle_service_type_id, description, mile_age, price, update_date)
    values (nextval('vehicle_seq'), 1, 1, 1, 'description', 43000, 30000.00, now());