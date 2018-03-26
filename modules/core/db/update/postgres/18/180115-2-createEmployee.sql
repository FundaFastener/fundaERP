alter table FE_EMPLOYEE add constraint FK_FE_EMPLOYEE_DEPARTMENT foreign key (DEPARTMENT_ID) references FE_DEPARTMENT(ID);
create unique index IDX_FE_EMPLOYEE_UK_NO_ on FE_EMPLOYEE (NO_) where DELETE_TS is null ;
create index IDX_FE_EMPLOYEE_DEPARTMENT on FE_EMPLOYEE (DEPARTMENT_ID);
