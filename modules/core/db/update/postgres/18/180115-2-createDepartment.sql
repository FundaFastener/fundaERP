alter table FE_DEPARTMENT add constraint FK_FE_DEPARTMENT_MANAGED_BY foreign key (MANAGED_BY_ID) references FE_EMPLOYEE(ID);
create unique index IDX_FE_DEPARTMENT_UK_NO_ on FE_DEPARTMENT (NO_) where DELETE_TS is null ;
