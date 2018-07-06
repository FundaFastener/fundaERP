-- alter table FE_PART_PROCESSES_STANDARD add column PROCESS_TYPE_ID uuid ^
-- update FE_PART_PROCESSES_STANDARD set PROCESS_TYPE_ID = <default_value> ;
-- alter table FE_PART_PROCESSES_STANDARD alter column PROCESS_TYPE_ID set not null ;
alter table FE_PART_PROCESSES_STANDARD add column PROCESS_TYPE_ID uuid not null ;
alter table FE_PART_PROCESSES_STANDARD alter column OUTPUT_UNIT drop not null ;
alter table FE_PART_PROCESSES_STANDARD alter column TIME_UNIT drop not null ;
alter table FE_PART_PROCESSES_STANDARD alter column OUTPUT_ drop not null ;
