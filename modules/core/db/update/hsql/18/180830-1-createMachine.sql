create table FE_MACHINE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NO_ varchar(10) not null,
    NAME varchar(50) not null,
    TYPE_ integer,
    BRAND varchar(30),
    DEPARTMENT_ID varchar(36),
    REMARK longvarchar,
    --
    primary key (ID)
);
