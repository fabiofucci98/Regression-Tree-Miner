use MapDB;

CREATE TABLE MapDB.allIdenticalValues (
	X varchar(10),
    Y varchar(10),
    C float(5,2)
);

insert into MapDB.allIdenticalValues values('A','B',1);
insert into MapDB.allIdenticalValues values('A','B',1);
insert into MapDB.allIdenticalValues values('A','B',1);
insert into MapDB.allIdenticalValues values('A','B',1);
insert into MapDB.allIdenticalValues values('A','B',1.5);
insert into MapDB.allIdenticalValues values('A','B',1.5);
insert into MapDB.allIdenticalValues values('A','B',1.5);
insert into MapDB.allIdenticalValues values('A','B',10);
insert into MapDB.allIdenticalValues values('A','B',1.5);
insert into MapDB.allIdenticalValues values('A','B',1.5);
insert into MapDB.allIdenticalValues values('A','B',10);
insert into MapDB.allIdenticalValues values('A','B',10);
insert into MapDB.allIdenticalValues values('A','B',10);
insert into MapDB.allIdenticalValues values('A','B',10);
insert into MapDB.allIdenticalValues values('A','B',1);
