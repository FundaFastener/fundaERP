package com.company.funda.erp.web.importBD.excel.transfer;

import com.company.funda.erp.annotations.ExcelPointer;
import com.company.funda.erp.entity.WorkRecord;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class WorkRecordTransformer extends WorkRecord implements ExcelTransformer {


    @Override
    public <E extends StandardEntity> Map<String, String> transfer(Sheet sheet, List<E> list) {
        //WorkRecord wr = (WorkRecord)list.get(0);
        //TODO-H  *** back to start here.
        return null;
    }


    public static void main(String... args) {
        Class<WorkRecord> workRecordClass = WorkRecord.class;
//        for (Field f : workRecordClass.getDeclaredFields()) {
//            System.out.println(f.getName()+":"+f.getDeclaredAnnotations().length);
//            //System.out.println(f.getAnnotationsByType(ExcelPointer.class));
//            Annotation annotation = f.getAnnotation(ExcelPointer.class);
//
//            if(annotation instanceof ExcelPointer){
//                ExcelPointer myAnnotation = (ExcelPointer) annotation;
//                System.out.println("index: " + myAnnotation.index()[0]);
//                System.out.println("formater: " + myAnnotation.formater());
//            }
//        }

        FieldUtils.getFieldsListWithAnnotation(WorkRecord.class,ExcelPointer.class).forEach(
                field -> {
                    Annotation annotation = field.getAnnotation(ExcelPointer.class);
                    if(annotation instanceof ExcelPointer){
                        ExcelPointer myAnnotation = (ExcelPointer) annotation;
                        System.out.println("index: " + myAnnotation.index()[0]);
                        System.out.println("formater: " + myAnnotation.formater());
                    }
                }
        );
    }
}
