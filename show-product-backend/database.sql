create database shopapp;
use shopapp;

create table categories(
    id int primary key auto_increment,
    name varchar(100) not null default '' comment 'Tên danh mục, ví dụ: Áo'
);

create table products(
    id int primary key auto_increment,
    name varchar(350) comment 'Tên sản phẩm',
    price float not null check(price >= 0),
    thumbnail varchar(300) default '',
    category_id int,
    foreign key (category_id) references categories(id)
); 