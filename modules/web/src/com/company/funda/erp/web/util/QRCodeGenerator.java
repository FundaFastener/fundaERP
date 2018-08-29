package com.company.funda.erp.web.util;

import com.company.funda.erp.enums.WorkOrderStatus;
import com.company.funda.erp.service.QRCodeService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class QRCodeGenerator extends AbstractWindow {
    @Inject
    private QRCodeService qrCodeService;

    @Inject
    private TextArea textArea;

    @Inject
    FlowBoxLayout flowBox;

    @Inject
    ComponentsFactory componentsFactory;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    public void doGenerateQRCode() {
        try {
            final String imageSize = "300px";
            byte[] qr = qrCodeService.getQRCodeByteArray(textArea.getRawValue());
            Image image = componentsFactory.createComponent(Image.class);
            Accordion accordion = componentsFactory.createComponent(Accordion.class);
            accordion.setWidth(imageSize);
            accordion.setHeight(imageSize);
            image.setCaption(textArea.getRawValue());
            image.setScaleMode(Image.ScaleMode.CONTAIN);
            image.setSource(StreamResource.class)
                    .setStreamSupplier(() -> new ByteArrayInputStream(qr))
                    .setBufferSize(1024);
            accordion.addTab("", image);

            flowBox.add(accordion);

            image.addClickListener(l->{
                l.getSource().getParent().setStyleName("borderImage");
                showOptionDialog("Delete QRCode",
                        "Are you sure delete this QRCoed("+l.getSource().getCaption()+")?"
                        , MessageType.CONFIRMATION,
                        new Action[] {
                                new DialogAction(DialogAction.Type.YES, Action.Status.NORMAL).withHandler(e -> {
                                    flowBox.remove(l.getSource().getParent());
                                }),
                                new DialogAction(DialogAction.Type.NO, Action.Status.NORMAL).withHandler(e ->{
                                    l.getSource().getParent().setStyleName(null);
                                })
                        }
                );

            });
            textArea.setValue(null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}