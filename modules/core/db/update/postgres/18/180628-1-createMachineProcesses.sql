create table FE_MACHINE_PROCESSES (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MACHINE_ID uuid not null,
    PROCESS_TYPE integer not null,
    ATTRIBUTE integer,
    --
    primary key (ID)
);
