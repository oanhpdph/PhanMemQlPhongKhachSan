if OBJECT_ID('QLPhongKhachSan') is not null
drop database QLPhongKhachSan
go
create database QLPhongKhachSan
go

use QLPhongKhachSan
-- them table client
if OBJECT_ID('Client') is not null
drop table Client
go
create table Client
(id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
code varchar(20) unique,
[name] nvarchar(50),
dateOfBirth date,
sex nvarchar(5),
[address] nvarchar(200),
idPersonCard varchar(12) unique,
customPhone varchar(10) 
)

-- table staff
if OBJECT_ID('Staff') is not null
drop table Staff
go
create table Staff
(id Uniqueidentifier primary key default newid(),
code varchar(20) unique,
[name] nvarchar(50),
dateOfBirth date,
sex nvarchar(5),
[address] nvarchar(200),
idPersonCard varchar(12) unique,
phone varchar(10),
[user] varchar(30) unique,
pass varchar(20),
[rule] varchar(20)
)


--table bill
if OBJECT_ID('Bill') is not null
drop table Bill
go
create table bill
(
id Uniqueidentifier primary key default newid(),
idClient Uniqueidentifier ,
idStaff Uniqueidentifier,
code varchar(20) unique,
[name] nvarchar(50),
Price DECIMAL(20,0) DEFAULT 0,
[address] nvarchar(200),
hotelPhone varchar(10),
[status] int default 0,
[date] smalldatetime,
constraint FK_idClient foreign key (idClient ) references Client(id),
constraint FK_idStaff foreign key (idStaff ) references Staff(id),
)



--table item
if OBJECT_ID('Item') is not null
drop table Item
go
create table item
(id uniqueidentifier primary key default newid(),
code varchar(20),
[name] nvarchar(50)
)

-- table promotionsService
if OBJECT_ID('promotionS') is not null
drop table promotionS
go
create table promotionS
(Id uniqueidentifier primary key default newid(),
code varchar(10) unique,
[value] decimal(20,0) default 0,
dateStart date,
dateEnd date,
)


--table service
if OBJECT_ID('service') is not null
drop table [service]
go
create table [service]
(
id uniqueidentifier primary key default newid(),
idpromotion uniqueidentifier,
code varchar(20) unique,
[name] nvarchar(50),
price decimal(20,0) default 0,
[notes] nvarchar(50),
constraint fk_idpromotionS foreign key (idpromotion) references promotionS(id)
)


-- table promotionROom
if OBJECT_ID('promotionR') is not null
drop table promotionR
go
create table promotionR
(Id uniqueidentifier primary key default newid(),
code varchar(10) unique,
[value] decimal(20,0) default 0,
dateStart date,
dateEnd date,
)


--Table room
if OBJECT_ID('Room') is not null
drop table Room
go
create table Room
(id uniqueidentifier primary key default newid(),
[status] int default 1,-- 1 san sang, 2 co khach, 3 chua don, 4 dang don, 5 dang sua
KindofRoom int default 1,-- 1 phong don, 2 phong doi, 3 phong vip
idPromotion uniqueidentifier,
code varchar(20) unique,
roomNumber varchar(10),
area varchar(10),
[location] nvarchar(50),
price decimal(20,0) default 0
constraint fk_idPromotion foreign key (idPromotion) references promotionR(id)
)

-- Table roomItem
if OBJECT_ID('roomItem') is not null
drop table roomItem
go
create table roomItem
(roomId uniqueidentifier,
itemId uniqueidentifier,
[status] nvarchar(50),
amount int default 0
constraint pk_roomItem primary key (roomId,itemId),
constraint fk_roomid foreign key (roomid) references room(id),
constraint fk_itemid foreign key (itemid) references item(id)
)


--Table roomBill
if OBJECT_ID('RoomBill') is not null
drop table RoomBill
go
create table RoomBill
(RoomId uniqueidentifier,
BillId uniqueidentifier unique,
priceRoom decimal(20,0) default 0,
promotionRoom decimal(20,0) default 0,
dateCheckIn smalldatetime,
dateCheckOut smalldatetime
conStraint pk_roombill primary key(RoomId,BillId),
constraint fk_roomId1 foreign key (roomid) references room(id),
constraint fk_BillId foreign key (billid) references bill(id),
) 


--Table RoomBillService
if OBJECT_ID('RoomBillService') is not null
drop table RoomBillService
go
create table RoomBillService
(IdBill uniqueidentifier,
IdService uniqueidentifier,
IdRoom uniqueidentifier,
priceService decimal(20,0) default 0,
promotionService decimal(20,0) default 0,
dateOfHire nvarchar(200),
times int
constraint pk_roomBillService primary key(IdBill,IdService,IdRoom),
constraint fk_idBill foreign key (idbill) references roomBill(BillId),
constraint fk_idService foreign key (idservice) references [service](id),
)



 








