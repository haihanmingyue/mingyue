package com.mingyue.mingyue.utils;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

/**
 * 页码

 */
public class PageMarker implements IEventHandler {

    /** 字体 */
    private PdfFont pdfFont;

    public PageMarker(PdfFont pdfFont) {
        this.pdfFont = pdfFont;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.getLastContentStream(), page.getResources(), pdf);
        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        float  x      = (pageSize.getLeft() + pageSize.getRight()) / 2;
        float  y      = pageSize.getBottom() + 15;
        Paragraph p = new Paragraph(pdf.getPageNumber(page) + "/" + pdf.getNumberOfPages())
                .setFontSize(12)
                .setFont(pdfFont);
        canvas.showTextAligned(p, x, y, TextAlignment.CENTER);
        canvas.close();
    }
}
