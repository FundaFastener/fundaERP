create table FE_WORK_ORDER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NO_ varchar(20) not null,
    INVENTORY_ITEM_ID varchar(36) not null,
    QUANTITY decimal(20) not null,
    UNIT integer not null,
    PROCESS_TYPE integer not null,
    DEFAULT_MACHINE_ID varchar(36) not null,
    BACKUP_MACHINES_ID varchar(36),
    STATUS integer,
    REMARK longvarchar,
    --
    primary key (ID)
);
