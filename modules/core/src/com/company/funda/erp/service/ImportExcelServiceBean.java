package com.company.funda.erp.service;

import com.company.funda.erp.util.PrintUtil;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.app.filestorage.FileStorage;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;


@Service(ImportExcelService.NAME)
public class ImportExcelServiceBean implements ImportExcelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Collection<BaseGenericIdEntity> preview(FileDescriptor fileDescriptor, Map<String, Object> params)
            throws Exception {

        logger.info("123");
        logger.info(PrintUtil.printMultiLine(fileDescriptor));

//        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.NAME);
//        byte[] xlsFile = fileStorageAPI.loadFile(fileDescriptor);
        FileStorage fileStorage = AppBeans.get(FileStorage.NAME);
        File[] roots = fileStorage.getStorageRoots();
        if (roots.length == 0) {
            logger.error("No storage directories available");
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescriptor.getId().toString());
        }

        InputStream inputStream = null;
        Workbook workbook = null;
        for (File root : roots) {
            File dir = fileStorage.getStorageDir(root, fileDescriptor);

            File file = new File(dir, fileStorage.getFileName(fileDescriptor));
            if (!file.exists()) {
                logger.error("File " + file + " not found");
                continue;
            }
            workbook = WorkbookFactory.create(file);
            if(null != workbook){
                break;
            }
        }


        if (workbook == null)
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, "File was not loaded");

        Sheet sheet = workbook.getSheetAt(3);
        logger.info(PrintUtil.printMultiLine(sheet.getRow(5)));
        logger.info("456");
        return null;
    }

    @Override
    public String doImport(Map<String, Object> params) {

        return "";
    }


}