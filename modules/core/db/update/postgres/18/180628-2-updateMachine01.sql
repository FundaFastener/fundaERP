alter table FE_MACHINE add column PROCESS_TYPE integer ^
update FE_MACHINE set PROCESS_TYPE = 1 where PROCESS_TYPE is null ;
alter table FE_MACHINE alter column PROCESS_TYPE set not null ;
alter table FE_MACHINE drop column PROCESS_TYPE_OLD cascade ;
