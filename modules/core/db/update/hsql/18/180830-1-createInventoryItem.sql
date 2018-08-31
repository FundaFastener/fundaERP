create table FE_INVENTORY_ITEM (
    ID varchar(36) not null,
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
);
