alter table FE_DEPARTMENT add constraint FK_FE_DEPARTMENT_ON_MANAGED_BY foreign key (MANAGED_BY_ID) references FE_EMPLOYEE(ID);
create unique index IDX_FE_DEPARTMENT_UNIQ_NO_ on FE_DEPARTMENT (NO_) ;
create index IDX_FE_DEPARTMENT_ON_MANAGED_BY on FE_DEPARTMENT (MANAGED_BY_ID);
