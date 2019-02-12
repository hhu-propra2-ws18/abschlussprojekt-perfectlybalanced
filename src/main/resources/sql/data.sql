INSERT INTO user(firstName,lastName,userName,password,mail)
SELECT 'Max','Mustermann','MMustermann','MaxMuster123','Max@Mustermann.de'
WHERE NOT EXISTS (SELECT * FROM user);

INSERT INTO product(description,title,surety,cost)
SELECT 'Mäht den Rasen','Rasenmäher',10,30
WHERE NOT EXISTS (SELECT * FROM product);

INSERT INTO address(street,housenumber,postcode,city)
SELECT 'Universitätsstraße',1,40225,'Düsseldorf'
WHERE NOT EXISTS (SELECT * FROM address);
