INSERT INTO USER_ENTITY(firstname,lastname,username,password,email)
VALUES('Max','Mustermann','MMustermann','MaxMuster223','Max@Mustermann.de');

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email)
VALUES('Jan','Wellem','JanW','JanWellem','Jan@Wellem.de');

INSERT INTO ADMIN_ENTITY(username,password)
VALUES('JanW','JanWellem');

INSERT INTO PRODUCT_ENTITY(titel,description,cost,street,housenumber,postcode,city,surety,owner_user_id)
VALUES('Rasenmäher','Mäht den Rasen',30,'Universiätsstraße',1,40225,'Düsseldorf',10,1);

INSERT INTO PRODUCT_ENTITY(titel,description,cost,street,housenumber,postcode,city,surety,owner_user_id)
VALUES('Hammer','Um auf Dinge zu hämmern',20,'Universiätsstraße',2,40225,'Düsseldorf',5,2);

INSERT INTO LENDING_ENTITY(start,end,status,borrower_user_id,product_id)
VALUES('2019-02-12 12:00:00','2019-02-14 12:00:00',4,1,1);

INSERT INTO LENDING_ENTITY(start,end,status,borrower_user_id,product_id)
VALUES('2019-02-11 12:00:00','2019-02-12 12:00:00',4,2,2);