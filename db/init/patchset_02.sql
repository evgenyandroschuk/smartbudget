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