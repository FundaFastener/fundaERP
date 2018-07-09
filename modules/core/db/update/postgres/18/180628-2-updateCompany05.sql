-- update FE_COMPANY set NO_ = <default_value> where NO_ is null ;
alter table FE_COMPANY alter column NO_ set not null ;
