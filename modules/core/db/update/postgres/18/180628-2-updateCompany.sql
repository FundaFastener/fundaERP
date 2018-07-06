-- alter table FE_COMPANY add column NOO varchar(50) ^
-- update FE_COMPANY set NOO = <default_value> ;
-- alter table FE_COMPANY alter column NOO set not null ;
alter table FE_COMPANY add column NOO varchar(50) ;
alter table FE_COMPANY drop column NO_ cascade ;
