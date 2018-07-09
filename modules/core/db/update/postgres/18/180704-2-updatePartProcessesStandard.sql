alter table FE_PART_PROCESSES_STANDARD add column OUTPUT_ integer ^
update FE_PART_PROCESSES_STANDARD set OUTPUT_ = 0 where OUTPUT_ is null ;
alter table FE_PART_PROCESSES_STANDARD alter column OUTPUT_ set not null ;
