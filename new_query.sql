CREATE TABLE Drivers_Vehicles(
	did integer,
	vid char(6), 
	name char(30) not null,
	model char(30) not null, 
	modelyear integer CONSTRAINT fourDigits CHECK (modelyear between 0 and 9999), 
	seats integer,
	UNIQUE(vid),
	PRIMARY KEY (did)
);
CREATE TABLE Takes_Trip(
	tid integer, 
	did integer,
	rid integer,
	start datetime, 
	end datetime, 
	fee integer, 
	rating integer default 0,
	PRIMARY KEY(tid, rid),
	FOREIGN KEY(did) REFERENCES Drivers_Vehicles(did) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(rid) REFERENCES Makes_Request(rid) ON UPDATE CASCADE ON DELETE CASCADE 
);
CREATE TABLE Rates(
	tid integer,
	rid integer,
	pid integer,
	FOREIGN KEY(rid) REFERENCES Makes_Request(rid) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(tid) REFERENCES Takes_Trip(tid) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(tid, rid)
);
CREATE TABLE Passenger(
	pid integer, 
	name char(30) not null,
	PRIMARY KEY(pid)
);
CREATE TABLE Makes_Request(
	rid integer, 
	pid integer, 
	modelyear integer, 
	model char(30), 
	passengers integer, 
	taken boolean,
	PRIMARY KEY(rid),
	FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE,
);