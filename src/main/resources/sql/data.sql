INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Max','Mustermann','MMustermann','MaxMuster223','Max@Mustermann.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Jan','Wellem','JanW','JanWellem','Jan@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Sarah','Wellem','sarah','$2a$12$dbxtiDi5tWPvYU84mxkkkelVo5Vym5lvrpqKdxCKtRMOtPHcH2VvW','sarah@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Ron','Wesley','ron','ron','ron@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Max','Mustermann','admin','$2a$12$bKQttLmd96cP.dP8/ebGvue7PN8VU2q8ur22UOrAAmF0U8OEwan22','Max@Mustermann.de',1);

INSERT INTO PRODUCT_ENTITY(title,description,cost,street,housenumber,postcode,city,surety,owner_user_id)
VALUES('Rasenmäher','Mäht den Rasen',30,'Universiätsstraße',1,40225,'Düsseldorf',10,1);

INSERT INTO PRODUCT_ENTITY(title,description,cost,street,housenumber,postcode,city,surety,owner_user_id)
VALUES('Hammer','Um auf Dinge zu hämmern',20,'Universiätsstraße',2,40225,'Düsseldorf',5,3);

INSERT INTO LENDING_ENTITY(start,end,status,borrower_user_id,product_id,surety_reservationid,cost_reservationid)
VALUES('2019-02-12 12:00:00','2019-02-14 12:00:00',4,1,1,1,1);

INSERT INTO LENDING_ENTITY(start,               end,                  status,borrower_user_id,product_id,surety_reservationid,cost_reservationid)
VALUES(                   '2019-02-11 12:00:00','2019-02-12 12:00:00',0,      1,              2,        1,                    1);