

-- t_user_system_params
create table t_user_system_params (
  id SERIAL primary key,
  user_id int not null,
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
    user_id int not null,
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

