package com.mingyue.base.service;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.mingyue.base.dao.TestDao;
import com.mingyue.mingyue.utils.SetContentTypeUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;


@Service
public class PdfService {

    @Autowired
    private TestDao testDao;


    @Autowired
    private Configuration configuration;

    public void wordFlt2Pdf(String ftl, Object data, DocumentType wordDocumentType, HttpServletResponse response,String fileName) throws IOException, TemplateException, ExecutionException, InterruptedException {

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String templateString = getTemp(ftl,stringLoader,configuration,data);

        if (!wordDocumentType.equals(DocumentType.DOCX) && !wordDocumentType.equals(DocumentType.DOC)) {
            throw new RuntimeException("error type");
        }

//        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");
        response.setContentType(SetContentTypeUtil.map.get("pdf"));

        //获取输入流
        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(templateString.getBytes());
        //doc 转 pdf
        try {
            IConverter converter = LocalConverter.builder().build();
            converter.convert(tInputStringStream)
                    .as(wordDocumentType)
                    .to(response.getOutputStream())
                    .as(DocumentType.PDF)
                    .schedule()
                    .get();
        }catch (Exception e) {
            System.err.println("error->" + e);
            throw e;
        }
    }

    public void wordFtl2Word(String ftl, Object data, DocumentType wordDocumentType, HttpServletResponse response,String fileName) throws IOException, TemplateException {

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String templateString = getTemp(ftl,stringLoader,configuration,data);
        String type;
        if (wordDocumentType.equals(DocumentType.DOCX)) {
            type = "docx";
        } else if (wordDocumentType.equals(DocumentType.DOC)) {
            type = "doc";
        } else {
            throw new RuntimeException("error type");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType(SetContentTypeUtil.map.get("pdf"));

        fileName = fileName +  "." +  type;
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        response.getWriter().write(templateString);
    }

    public void html2Word(String html, Object data, DocumentType wordDocumentType, HttpServletResponse response,String fileName) throws IOException, TemplateException {

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String templateString = getTemp(html,stringLoader,configuration,data);

        String type;
        if (wordDocumentType.equals(DocumentType.DOCX)) {
            type = "docx";
        } else if (wordDocumentType.equals(DocumentType.DOC)) {
            type = "doc";
        } else {
            throw new RuntimeException("error type");
        }
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + "." + type);
        response.setContentType(SetContentTypeUtil.map.get(type));

        fileName = fileName +  "." +  type;
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.getWriter().write(templateString);


    }


    public void html2Pdf(String html, Object data, HttpServletResponse response,String fileName) throws TemplateException, IOException, DocumentException {

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String templateString = getTemp(html,stringLoader,configuration,data);

        response.setCharacterEncoding("utf-8");
        response.setContentType(SetContentTypeUtil.map.get("pdf"));
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");


        ITextRenderer renderer = new ITextRenderer();
        // step 3 解决中文支持
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //这边写入字体，前端页面body 标签上 全局加个style font-family = 下面的字体，不然也不能显示中文
        fontResolver.addFont("src/main/resources/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.setDocumentFromString(templateString);

        SharedContext sharedContext = renderer.getSharedContext();
//        sharedContext.setReplacedElementFactory(new Base64ImgReplacedElementFactory());
//        renderer.setPdfPageEvent(new PdfBuilder(null, waterMark, markStart));
        renderer.layout();
        renderer.createPDF(response.getOutputStream());
        renderer.finishPDF();

    }

    public String getTemp(String temp, StringTemplateLoader stringLoader,Configuration configuration,Object data) throws IOException, TemplateException {
        configuration.setDefaultEncoding("utf-8");
        stringLoader.putTemplate("pdftemp",temp);
        configuration.setTemplateLoader(stringLoader);
        StringWriter stringWriter = new StringWriter();
        Template template = configuration.getTemplate("pdftemp");
        template.setOutputEncoding("utf-8");
        //填充数据
        template.process(data,stringWriter);
        return stringWriter.toString();
    }
}
