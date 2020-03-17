package org.nlpir.lucene.cn.ictclas.finersegmet;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.nlpir.segment.CNLPIRLibrary;
import org.nlpir.segment.exception.NLPIRException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FinerTokenizer extends Tokenizer {

	Logger logger = Logger.getLogger(FinerTokenizer.class);

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PositionLengthAttribute posArr = addAttribute(PositionLengthAttribute.class);

	private String[] buffer = null;
	private StringBuffer cbuffer = null;
	private String line = null;
	int start = 0;
	int end = 0;
	int current = 0;

	String data = null;
	int encoding = 1;
	String sLicenceCode = "";
	String userDict = null;
	boolean bOverwrite = false;

	public static boolean initState = false;


	public FinerTokenizer() {
	}

	/**
	 * Solr中使用的配有文件，用于加载NLPIR分词相关配置，分词初始化时进行加载。
	 * 

	 *            Dict bOverwrite
	 */
	public void defaultInit() {
		if (FinerTokenizer.initState)
			return;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("nlpir.properties")));
			data = prop.getProperty("data");
			encoding = Integer.parseInt(prop.getProperty("encoding"));
			sLicenceCode = prop.getProperty("sLicenceCode");
			userDict = prop.getProperty("userDict");
			bOverwrite = Boolean.parseBoolean(prop.getProperty("bOverwrite"));
			logger.info("NLPIR Data Path:" + data);
			logger.info("NLPIR Encoding Set:" + encoding);
			logger.info("NLPIR Licence Code:" + sLicenceCode);
			logger.info("NLPIR User Dict:" + userDict);
			logger.info("NLPIR User Dict bOverwrite:" + bOverwrite);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 默认初始化方法
	 * 
	 * @param factory
	 */
	public FinerTokenizer(AttributeFactory factory) {
		super(factory);
		this.defaultInit();
		this.init(data, encoding, sLicenceCode, userDict, bOverwrite);
	}

	/**
	 * 分词初始化
	 * 
	 * @param data
	 *            词典路径
	 * @param encoding
	 *            编码 0：GBK；1：UTF-8
	 * @param sLicenceCode
	 *            授权码，默认为""
	 * @param userDict
	 *            用户词典文件
	 * @param bOverwrite
	 *            用户词典引入方式
	 */
	public FinerTokenizer(String data, int encoding, String sLicenceCode, String userDict, boolean bOverwrite) {
		this.init(data, encoding, sLicenceCode, userDict, bOverwrite);
	}

	/**
	 * 分词初始化
	 * 
	 * @param data
	 *            词典路径
	 * @param encoding
	 *            编码 0：GBK；1：UTF-8
	 * @param sLicenceCode
	 *            授权码，默认为""
	 * @param userDict
	 *            用户词典文件
	 * @param bOverwrite
	 *            用户词典引入方式
	 */
	public FinerTokenizer(AttributeFactory factory, String data, int encoding, String sLicenceCode, String userDict,
			boolean bOverwrite) {
		super(factory);
		this.init(data, encoding, sLicenceCode, userDict, bOverwrite);
	}

	/**
	 * 分词初始化
	 * 
	 * @param data
	 *            词典路径
	 * @param encoding
	 *            编码 0：GBK；1：UTF-8
	 * @param sLicenceCode
	 *            授权码，默认为""
	 * @param userDict
	 *            用户词典文件
	 * @param bOverwrite
	 *            用户词典引入方式
	 */
	private void init(String data, int encoding, String sLicenceCode, String userDict, boolean bOverwrite) {
		if (FinerTokenizer.initState)
			return;
		FinerTokenizer.initState = CNLPIRLibrary.Instance.NLPIR_Init(data, encoding, "");
		logger.info("NLPIR 初始化：" + FinerTokenizer.initState);
		if (!FinerTokenizer.initState) {
			try {
				throw new NLPIRException(CNLPIRLibrary.Instance.NLPIR_GetLastErrorMsg());
			} catch (NLPIRException e) {
				e.printStackTrace();
			}
		} else if (userDict != null && !userDict.isEmpty() && !userDict.equals("\"\"")) {
			int state = CNLPIRLibrary.Instance.NLPIR_ImportUserDict(userDict, bOverwrite);
			if (state == 0)
				try {
					throw new NLPIRException(CNLPIRLibrary.Instance.NLPIR_GetLastErrorMsg());
				} catch (NLPIRException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (buffer != null && buffer.length < current + 1) {
			cbuffer = null;
			buffer = null;
			line = null;
			start = 0;
			end = 0;
			current = 0;
			return false;
		}
		if (cbuffer == null) {
			cbuffer = new StringBuffer();
			int c = 0;
			while ((c = input.read()) != -1) {
				cbuffer.append((char) c);
			}
			line = cbuffer.toString();
			if (line == null || line.replaceAll("\\s", "").length() == 0)
				line = "";
			buffer = CNLPIRLibrary.Instance.NLPIR_FinerSegment(line).split("\\s");
		}
		clearAttributes();
		int length = buffer[current].length();
		end = start + length;
		termAtt.copyBuffer(buffer[current].toCharArray(), 0, length);
		offsetAtt.setOffset(correctOffset(start), correctOffset(end));
		posArr.setPositionLength(current + 1);
		typeAtt.setType("word");
		start = end;
		current += 1;
		return true;
	}
}
