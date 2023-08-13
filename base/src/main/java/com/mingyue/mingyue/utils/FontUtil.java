package com.mingyue.mingyue.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 字体工具
 *
 * @author 钱凯
 * @version 1.0
 * @date 2021-03-16 14:07:40
 */
public class FontUtil {

    private static final Logger logger = LoggerFactory.getLogger(FontUtil.class);


    /**
     * 获取黑体
     *
     * @return pdf font the pdf font
     * @author 钱凯
     * @date 2021-03-16 14:08:04
     */
    public static PdfFont getFontSimHei(){
        PdfFont pdfFont = null;
        try {
            logger.info("开始初始化字体...................");
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream msyh = loader.getResourceAsStream("fonts/simhei.ttf");
            pdfFont = PdfFontFactory.createFont(toByteArray(msyh), PdfEncodings.IDENTITY_H, false);
        }catch (Exception e){
            logger.error("初始化字体失败",e);
        }
        return pdfFont;
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
