alter table FE_PART_PROCESSES_STANDARD add constraint FK_FE_PART_PROCESSES_STANDARD_PART_NO foreign key (PART_NO_ID) references FE_INVENTORY_ITEM(ID);
alter table FE_PART_PROCESSES_STANDARD add constraint FK_FE_PART_PROCESSES_STANDARD_MACHINE foreign key (MACHINE_ID) references FE_MACHINE(ID);
create index IDX_FE_PART_PROCESSES_STANDARD_PART_NO on FE_PART_PROCESSES_STANDARD (PART_NO_ID);
create index IDX_FE_PART_PROCESSES_STANDARD_MACHINE on FE_PART_PROCESSES_STANDARD (MACHINE_ID);
