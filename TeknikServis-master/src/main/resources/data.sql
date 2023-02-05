
-- for Service
delete  from SERVICE;
Insert into SERVICE (SERVICE_ID,SERVICE_NAME,DESKTOP,LAPTOP,MAC,DURATION) values ('1','Formatlama','50','50','200','2');
Insert into SERVICE (SERVICE_ID,SERVICE_NAME,DESKTOP,LAPTOP,MAC,DURATION) values ('2','Virüs Temizliği','100','100','100','4');
Insert into SERVICE (SERVICE_ID,SERVICE_NAME,DESKTOP,LAPTOP,MAC,DURATION) values ('3','Diskten Veri kurtarma','200','200','400','10');
Insert into SERVICE (SERVICE_ID,SERVICE_NAME,DESKTOP,LAPTOP,MAC,DURATION) values ('4','Fan ve Termal macun temizliği','30','100','200','1');

--for Product

delete from PRODUCT;
Insert into PRODUCT (PRODUCT_ID,NAME) values ('1','İşlemci');
Insert into PRODUCT (PRODUCT_ID,NAME) values ('2','Ekran Kartı');
Insert into PRODUCT (PRODUCT_ID,NAME) values ('3','Ram');
Insert into PRODUCT (PRODUCT_ID,NAME) values ('4','Anakart');

-- @HASHBYTES('MD5','1234')
-- hashBytes('SHA','1234')
-- dbms_crypto.encrypt(UTL_I18N.string_to_raw('password1'), 'AES', 'secret-key')
--for Users
delete from USERS;
Insert into USERS (USER_ID,USERNAME,PASSWORD,USER_EMAIL) values ('1','admin','$2a$10$V2UuPZLY7Pzu6ihGbA0Yc.lCHmZ7KCr0Ahdm5IPcYkES/HO0bD1NO','bilge@gmail.com');
Insert into USERS (USER_ID,USERNAME,PASSWORD,USER_EMAIL) values ('2','user','$2a$10$WDtUVEjZuuQ7YNOfweEOu.5BxWq/yRH/LIyU9jv0g.TYjP8DfiMfC','huseyinb@gmail.com');

--for Role
DELETE FROM ROLE;
Insert into ROLE (NAME) values ('ROLE_ADMIN');
Insert into ROLE (NAME) values ('ROLE_USER');

-- for User_Roles
delete from USER_ROLES;
Insert into USER_ROLES (ROLE,USER_ID) values ('ROLE_ADMIN','1');
Insert into USER_ROLES (ROLE,USER_ID) values ('ROLE_USER','2');

COMMIT;