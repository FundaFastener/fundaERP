create unique index IDX_FE_MACHINE_PROCESSES_UNQ on FE_MACHINE_PROCESSES (MACHINE_ID, PROCESS_TYPE) where DELETE_TS is null ;