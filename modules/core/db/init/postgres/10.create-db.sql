-- begin FE_COMPANY
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
)^
-- end FE_COMPANY
-- begin FE_DEPARTMENT
create table FE_DEPARTMENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NO_ varchar(6) not null,
    NAME varchar(50) not null,
    MANAGED_BY_ID uuid,
    --
    primary key (ID)
)^
-- end FE_DEPARTMENT
-- begin FE_EMPLOYEE
create table FE_EMPLOYEE (
    ID uuid,
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
    DEPARTMENT_ID uuid,
    --
    primary key (ID)
)^
-- end FE_EMPLOYEE
-- begin FE_INVENTORY_ITEM
create table FE_INVENTORY_ITEM (
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
    NAME1 varchar(200),
    NAME2 varchar(200),
    CATEGORY varchar(50),
    MATERIAL_WEIGHT decimal(19, 2),
    NET_WEIGHT decimal(19, 2),
    --
    primary key (ID)
)^
-- end FE_INVENTORY_ITEM
-- begin FE_MACHINE
create table FE_MACHINE (
    ID uuid,
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
    PROCESS_TYPE integer not null,
    BRAND varchar(30),
    DEPARTMENT_ID uuid,
    REMARK text,
    --
    primary key (ID)
)^
-- end FE_MACHINE
-- begin FE_WORK_ORDER
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
    REMARK text,
    --
    primary key (ID)
)^
-- end FE_WORK_ORDER
-- begin FE_WORK_RECORD
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
    OPERATE_TYPE integer,
    RECORD_NO bigint not null,
    EMPLOYEE_ID uuid,
    WORK_HOUR_TYPE integer,
    START_TIME timestamp not null,
    END_TIME timestamp,
    TIME_USED integer,
    STATUS integer not null,
    UNIT integer,
    UNIT_WEIGHT decimal(19, 2),
    FINISHED_QUANTITY decimal(19, 2),
    SETUP_LOSS_QUANTITY decimal(19, 2),
    NG_LOSS_QUANTITY decimal(19, 2),
    MATERIAL_LOSS_QUANTITY decimal(19, 2),
    SETUP_LOSS_UNIT integer,
    NG_LOSS_UNIT integer,
    MATERIAL_LOSS_UNIT integer,
    REMARK text,
    --
    primary key (ID)
)^
-- end FE_WORK_RECORD
