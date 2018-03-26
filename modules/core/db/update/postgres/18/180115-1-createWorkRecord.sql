create table FE_WORK_RECORD (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    WORK_ORDER_ID uuid not null,
    RECORD_NO varchar(6) not null,
    EMPLOYEE_ID uuid,
    START_TIME timestamp not null,
    END_TIME timestamp,
    STATUS integer not null,
    UNIT integer not null,
    UNIT_WEIGHT decimal(19, 2),
    FINISHED_QUANTITY decimal(19, 2),
    SETUP_LOSS_QUANTITY decimal(19, 2),
    NG_LOSS_QUANTITY decimal(19, 2),
    MATERIAL_LOSS_QUANTITY decimal(19, 2),
    SETUP_LOSS_UNIT integer,
    NG_LOSS_UNIT integer,
    MATERIAL_LOSS_UNIT integer,
    --
    primary key (ID)
);
