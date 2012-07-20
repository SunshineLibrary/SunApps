PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO "android_metadata" VALUES('en_US');
CREATE TABLE books(_id INTEGER PRIMARY KEY , title TEXT , description TEXT , author TEXT , progress INTEGER);
CREATE TABLE sections(_id INTEGER PRIMARY KEY , name TEXT , parent_id INTEGER NOT NULL , FOREIGN KEY (parent_id) REFERENCES lessons(_id));
CREATE TABLE lessons(_id INTEGER PRIMARY KEY , name TEXT , parent_id INTEGER NOT NULL , FOREIGN KEY (parent_id) REFERENCES chapters(_id));
CREATE TABLE courses(_id INTEGER PRIMARY KEY , name TEXT);
CREATE TABLE packages(_id INTEGER PRIMARY KEY , name TEXT , version INTEGER);
CREATE TABLE api_sync_states(_id INTEGER PRIMARY KEY , table_name TEXT , last_update INTEGER);
CREATE TABLE chapters(_id INTEGER PRIMARY KEY , name TEXT , parent_id INTEGER NOT NULL , FOREIGN KEY (parent_id) REFERENCES courses(_id));

INSERT INTO "courses" VALUES(1,'course1');
INSERT INTO "courses" VALUES(2,'course2');

INSERT INTO "chapters" VALUES(1,'c1chapter1', 1);
INSERT INTO "chapters" VALUES(2,'c1chapter2', 1);
INSERT INTO "chapters" VALUES(3,'c2chapter1', 2);
INSERT INTO "chapters" VALUES(4,'c2chapter2', 2);

INSERT INTO "lessons" VALUES(1,'c1c1lesson1', 1);
INSERT INTO "lessons" VALUES(2,'c1c1lesson2', 1);
INSERT INTO "lessons" VALUES(3,'c1c2lesson1', 2);
INSERT INTO "lessons" VALUES(4,'c1c2lesson1', 2);
INSERT INTO "lessons" VALUES(5,'c2c1lesson1', 3);
INSERT INTO "lessons" VALUES(6,'c2c1lesson2', 3);
INSERT INTO "lessons" VALUES(7,'c2c2lesson1', 4);
INSERT INTO "lessons" VALUES(8,'c2c2lesson1', 4);

COMMIT;
