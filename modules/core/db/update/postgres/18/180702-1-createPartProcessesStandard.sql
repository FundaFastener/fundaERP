create table FE_PART_PROCESSES_STANDARD (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PART_NO_ID uuid not null,
    MACHINE_ID uuid not null,
    FROM_ date not null,
    TO_ date,
    OUTPUT_UNIT integer not null,
    TIME_UNIT integer not null,
    MAJOR_SETUP_TIME decimal(19, 2),
    MINOR_SETUP_TIME decimal(19, 2),
    --
    primary key (ID)
);
