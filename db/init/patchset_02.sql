create table t_vehicle_service_type(
  id SERIAL primary key,
  user_id int not null,
  service_type_id int not null,
  description varchar(200) not null,
  unique (user_id, service_type_id)
);


insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 1, 'Страховка');
insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 2, 'Сервис');
insert into t_vehicle_service_type(user_id, service_type_id, description) values (1, 3, 'Запчасти');



create table t_vehicle (
  id SERIAL primary key,
  user_id int not null,
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