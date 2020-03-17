package org.nlpir.lucene.cn.ictclas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("D:\\Users\\nlpir-analysis-cn-ictclas\\src\\test\\resources\\nlpir.properties")));
        String data = prop.getProperty("data");
        int encoding = Integer.parseInt(prop.getProperty("encoding"));
        String sLicenceCode = prop.getProperty("sLicenceCode");
        String userDict = prop.getProperty("userDict");
        boolean bOverwrite = Boolean.parseBoolean(prop.getProperty("bOverwrite"));
        System.out.println("--"+sLicenceCode+"--");

        System.out.println(data);

    }
}
