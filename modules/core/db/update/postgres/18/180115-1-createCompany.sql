create table FE_COMPANY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NO_ varchar(50) not null,
    NAME varchar(80) not null,
    SHORT_NAME varchar(20),
    --
    primary key (ID)
);
