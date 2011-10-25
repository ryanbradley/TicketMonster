DROP TABLE User IF EXISTS CASCADE;
DROP TABLE Document IF EXISTS CASCADE;
DROP TABLE Revision IF EXISTS CASCADE;
DROP TABLE Venue IF EXISTS CASCADE;
DROP TABLE VenueLayout IF EXISTS CASCADE;
DROP TABLE Section IF EXISTS CASCADE;
DROP TABLE SectionRow IF EXISTS CASCADE;
DROP TABLE eventcategory IF EXISTS CASCADE;
DROP TABLE event IF EXISTS CASCADE;
DROP TABLE show IF EXISTS CASCADE;
DROP TABLE TicketCategory IF EXISTS CASCADE;
DROP TABLE PriceCategory IF EXISTS CASCADE;
DROP TABLE Allocation IF EXISTS CASCADE;
DROP SEQUENCE hibernate_sequence IF EXISTS CASCADE;

CREATE TABLE User (
id INTEGER NOT NULL PRIMARY KEY,
username varchar(255),
password varchar(255),
enabled boolean,
firstName varchar(255),
lastName varchar(255)
);

CREATE TABLE Document (
id INTEGER NOT NULL PRIMARY KEY,
revision_id INTEGER
);

CREATE TABLE Revision (
id INTEGER NOT NULL PRIMARY KEY,
document_id INTEGER,
created varchar(255),
createdBy varchar(255),
modified varchar(255),
modifiedBy varchar(255),
content varchar(255)
);

CREATE TABLE Venue (
id INTEGER NOT NULL PRIMARY KEY,
name varchar(255),
address varchar(255),
description_id INTEGER
);

CREATE TABLE VenueLayout (
id INTEGER NOT NULL PRIMARY KEY,
venue_id INTEGER,
name varchar(255),
capacity INTEGER
);

CREATE TABLE Section (
id INTEGER NOT NULL PRIMARY KEY,
name varchar(255),
description varchar(255),
capacity INTEGER,
layout_id INTEGER
);

CREATE TABLE SectionRow (
id INTEGER NOT NULL PRIMARY KEY,
name varchar(255),
capacity INTEGER,
section_id INTEGER
);

CREATE TABLE EventCategory (
id INTEGER NOT NULL PRIMARY KEY,
description varchar(255)
);

CREATE TABLE Event (
id INTEGER NOT NULL PRIMARY KEY,
name varchar(255),
document_id INTEGER,
startDate varchar(255),
endDate varchar(255),
category_id INTEGER,
major boolean
);

CREATE TABLE Show (
id INTEGER NOT NULL PRIMARY KEY,
event_id INTEGER,
venue_id INTEGER,
showdate varchar(255),
layout_id INTEGER
);

CREATE TABLE TicketCategory (
id INTEGER NOT NULL PRIMARY KEY,
description varchar(255)
);

CREATE TABLE PriceCategory (
id INTEGER NOT NULL PRIMARY KEY,
event_id INTEGER,
venue_id INTEGER,
section_id INTEGER,
category_id INTEGER,
price decimal(15,2)
);

CREATE TABLE Allocation (
id INTEGER NOT NULL PRIMARY KEY,
assigned varchar(255),
user_id INTEGER,
show_id INTEGER,
row_id INTEGER,
quantity INTEGER,
startSeat INTEGER,
endSeat INTEGER
);

CREATE SEQUENCE hibernate_sequence;
