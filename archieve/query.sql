CREATE TABLE Drivers(
	id integer,
	name char(30) not null,
	primary key(id)
);

CREATE TABLE Vehicles(
	id char(6), 
	model char(30) not null, 
	modelyear integer CONSTRAINT fourDigits CHECK (modelyear between 0 and 9999), 
	seats integer,
	primary key (id)
);

CREATE TABLE Passenger(
	id integer, 
	name char(30) not null,
	primary key(id)
);

CREATE TABLE Request(
	id integer, 
	pid integer, 
	modelyear integer, 
	model char(30), 
	passengers integer, 
	taken boolean,
	primary key (id)
);

CREATE TABLE Trip(
	id integer, 
	did integer, 
	pid integer, 
	start datetime, 
	end datetime, 
	fee integer, 
	rating integer default 0,
	primary key(id)
);

CREATE TABLE Drives(
	did integer,
	vid integer,
	UNIQUE(did),
	UNIQUE(vid),
	primary key(did,vid),
	foreign key(did) references Drivers(id) on update cascade on delete no action,
	foreign key(vid) references Vehicles(id) on update cascade on delete no action
);

CREATE TABLE Takes(
	did integer,
	tid integer,
	rid integer,
	foreign key(did) references Drivers(id) on update cascade on delete cascade,
	foreign key(tid) references Trip(id) on update cascade on delete no action,
	foreign key(rid) references Request(id) on update cascade on delete cascade,
	primary key(tid),
	unique(rid),
	unique(tid),
);

CREATE TABLE Makes(
	rid integer,
	pid integer,
	UNIQUE(rid),
	foreign key(rid) references Request(id) on update cascade on delete cascade,
	foreign key(pid) references Passenger(id) on update cascade on delete no action
);

CREATE TABLE Rates(
	tid integer,
	pid integer,
	foreign key(tid) references Trip(id) on update cascade on delete cascade,
	foreign key(pid) references Passenger(id) on update cascade on delete cascade,
	primary key(tid),
	unique(tid)
);

/*CREATE TABLE Trip(did integer REFERENCES Drivers(id), tid integer REFERENCES Trip(id) ON UPDATE CASCADE ON DELETE NO ACTION,  rid integer REFERENCES Mak    e_Request(id) ON UPDATE CASCADE ON DELETE NO ACTION,  start datetime, end datetime, fee integer);
CREATE TABLE Drivers_Vehicle(did integer, name char(30) not null, vid char()*/
