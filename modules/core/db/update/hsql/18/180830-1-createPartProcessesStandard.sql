create table FE_PART_PROCESSES_STANDARD (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PART_NO_ID varchar(36) not null,
    PROCESS_TYPE_ID varchar(36) not null,
    FROM_ date not null,
    TO_ date,
    OUTPUT_ integer,
    OUTPUT_UNIT integer,
    TIME_UNIT integer,
    MAJOR_SETUP_TIME decimal(19, 2),
    MINOR_SETUP_TIME decimal(19, 2),
    --
    primary key (ID)
);
