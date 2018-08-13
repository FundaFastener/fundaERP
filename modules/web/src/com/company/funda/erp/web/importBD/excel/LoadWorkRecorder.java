package com.company.funda.erp.web.importBD.excel;

import com.company.funda.erp.service.ImportExcelService;
import com.company.funda.erp.util.PrintUtil;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class LoadWorkRecorder extends AbstractWindow {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    private FileUploadField uploadField;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataSupplier dataSupplier;
    @Inject
    private ImportExcelService importExcelService;

    private static int ROW_START_INDEX = 6;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        uploadField.addFileUploadSucceedListener(event -> {
            // here you can get the file uploaded to the temporary storage if you need it
            File file = fileUploadingAPI.getFile(uploadField.getFileId());

            try (InputStream is = new FileInputStream(file);

                 Workbook wb = StreamingReader.builder()
                         .sstCacheSize(100)
                         .open(is);) {
                Sheet sheet = wb.getSheetAt(3);

                logger.info("1sheetName:{}", sheet.getSheetName());

                int count = 0;
                for (Row row : sheet) {
                    count++;
                    if (count <= ROW_START_INDEX) {
                        continue;
                    }
                    if (count > 10) {
                        break;
                    }
                    String firstCellValue = row.getCell(0).getStringCellValue();
                    if (StringUtils.isBlank(firstCellValue)) {
                        continue;
                    }
                    for (Cell cell : row) {
                        logger.info("{}:{}",cell.getColumnIndex() ,cell.getStringCellValue());
                    }
                }
                logger.info("------ ----- -----");

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                showNotification(e.getMessage());
            }
        });

        uploadField.addFileUploadErrorListener(event ->
                showNotification("File upload error"));
    }
}