CREATE TABLE Drivers_Vehicles(
	did integer,
	vid char(6), 
	name char(30) not null,
	model char(30) not null, 
	modelyear integer, 
	seats integer,
	UNIQUE(vid),
	PRIMARY KEY (did)
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
	FOREIGN KEY(pid) REFERENCES Passenger(pid) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE Takes_Trip(
	tid integer, 
	did integer,
	rid integer,
	`start` datetime, 
	`end` datetime, 
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
INSERT INTO Drivers_Vehicles VALUES(1,'yd1559', 'thomas', 'bmw', 1999, 5);
INSERT INTO Drivers_Vehicles VALUES(2,'8019h6', 'amy', 'volvo', 2010, 7);
INSERT INTO Passenger VALUES(1000, 'A');
INSERT INTO Passenger VALUES(2000, 'B');
INSERT INTO Makes_Request VALUES(100, 1000, 1990, 'bmw', 3, 0);
INSERT INTO Makes_Request VALUES(200, 2000, 1992, 'volvo', 0, 1);
INSERT INTO Takes_Trip VALUES(10, 1, 100, '2018-11-19 12:00:00', '2018-11-19 12:30:00', 100, 5);
INSERT INTO Takes_Trip VALUES(20, 2, 200, '2018-11-19 12:00:00', '2018-11-19 12:30:00', 34, 2);
INSERT INTO Rates VALUES(10, 100, 1000);
INSERT INTO Rates VALUES(20, 200, 2000);