drop table if exists coin cascade;
drop table if exists operation_type cascade;
drop table if exists transaction cascade;
drop table if exists transaction_route cascade;


create table coin (id  bigserial not null, sym varchar(255), primary key (id));
create table operation_type (id  bigserial not null, type varchar(255), primary key (id));
create table transaction (id int8 not null, coin_amount int8, date_time timestamp, price_usd int8, coin_id int8 not null, operation_id int8 not null, route_id int8 not null, primary key (id));
create table transaction_route (id  bigserial not null, from_place varchar(255), to_place varchar(255), primary key (id));

alter table transaction add constraint coin_type_id foreign key (coin_id) references coin;
alter table transaction add constraint operation_type_id foreign key (operation_id) references operation_type;
alter table transaction add constraint route_type_id foreign key (route_id) references transaction_route;
