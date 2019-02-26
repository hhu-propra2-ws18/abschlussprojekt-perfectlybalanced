INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Max','Mustermann','MMustermann','MaxMuster223','Max@Mustermann.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Jan','Wellem','JanW','JanWellem','Jan@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Sarah','Wellem','sarah','$2a$12$dbxtiDi5tWPvYU84mxkkkelVo5Vym5lvrpqKdxCKtRMOtPHcH2VvW','sarah@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Ron','Wesley','ron','ron','ron@Wellem.de',0);

INSERT INTO USER_ENTITY(firstname,lastname,username,password,email,role)
VALUES('Max','Mustermann','admin','$2a$12$bKQttLmd96cP.dP8/ebGvue7PN8VU2q8ur22UOrAAmF0U8OEwan22','admin@verleih.de',1);

INSERT INTO PRODUCT_ENTITY(id ,cost, description, city, housenumber, postcode, street, surety, title, owner_user_id)
VALUES(0, '500', 'man kann darauf schreiben', 'Blattstadt', '123', '47929', 'Blattstr.', '50', 'ein Blatt Papier', 1);

INSERT INTO LENDING_ENTITY(end, start, status, borrower_user_id, product_id)
VALUES(current_timestamp, current_timestamp, 3, 2, 0);