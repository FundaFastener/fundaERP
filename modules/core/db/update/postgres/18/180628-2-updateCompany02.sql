-- alter table FE_COMPANY add column NO_ varchar(50) ^
-- update FE_COMPANY set NO_ = <default_value> ;
-- alter table FE_COMPANY alter column NO_ set not null ;
alter table FE_COMPANY add column NO_ varchar(50) ;
alter table FE_COMPANY drop column NOO cascade ;
