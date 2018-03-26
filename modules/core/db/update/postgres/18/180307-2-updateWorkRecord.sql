alter table FE_WORK_RECORD drop column RECORD_NO cascade ;
alter table FE_WORK_RECORD add column RECORD_NO bigint ^
update FE_WORK_RECORD set RECORD_NO = 0 where RECORD_NO is null ;
alter table FE_WORK_RECORD alter column RECORD_NO set not null ;
