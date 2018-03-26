alter table FE_MACHINE add constraint FK_FE_MACHINE_DEPARTMENT foreign key (DEPARTMENT_ID) references FE_DEPARTMENT(ID);
create unique index IDX_FE_MACHINE_UK_NO_ on FE_MACHINE (NO_) where DELETE_TS is null ;
create index IDX_FE_MACHINE_DEPARTMENT on FE_MACHINE (DEPARTMENT_ID);
