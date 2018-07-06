alter table FE_MACHINE add column PROCESS_TYPE_OLD integer ^
update FE_MACHINE set PROCESS_TYPE_OLD = 1 where PROCESS_TYPE_OLD is null ;
alter table FE_MACHINE alter column PROCESS_TYPE_OLD set not null ;
alter table FE_MACHINE drop column PROCESS_TYPE cascade ;
