package com.company.funda.erp.service;


import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.IOException;

public interface QRCodeService {
    String NAME = "fe_QRCodeService";

    byte[] getQRCodeByteArray(String content) throws IOException;
    FileDescriptor getQRCode(String content) throws IOException, FileStorageException;

}