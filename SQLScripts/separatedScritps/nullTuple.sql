use MapDB;

CREATE TABLE MapDB.nullTuple (
	X varchar(10),
    Y varchar(10),
    C float(5,2)
);

insert into MapDB.nullTuple values(1,0,5);
insert into MapDB.nullTuple values(7,3,8);
insert into MapDB.nullTuple values(null,null,null);
