create table FE_WORK_ORDER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NO_ varchar(20) not null,
    INVENTORY_ITEM_ID uuid not null,
    QUANTITY decimal(20) not null,
    UNIT integer not null,
    PROCESS_TYPE integer not null,
    DEFAULT_MACHINE_ID uuid not null,
    BACKUP_MACHINES_ID uuid,
    STATUS integer,
    --
    primary key (ID)
);
