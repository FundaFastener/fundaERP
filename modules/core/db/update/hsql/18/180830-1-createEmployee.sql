create table FE_EMPLOYEE (
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
    FIRST_NAME_CH varchar(10),
    LAST_NAME_CH varchar(10),
    FIRST_NAME_EN varchar(30),
    LAST_NAME_EN varchar(10),
    FIRST_NAME_OTHER varchar(30),
    LAST_NAME_OTHER varchar(30),
    DEPARTMENT_ID varchar(36),
    --
    primary key (ID)
);
