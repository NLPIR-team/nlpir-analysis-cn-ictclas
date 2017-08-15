package org.nlpir.lucene.cn.ictclas;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 
 * @author panhongyan
 *
 */
public class NLPIRTokenizerAnalyzer extends Analyzer{

	String data=null;
	int encoding=1;
	String sLicenceCode=null;
	String userDict=null;
	boolean bOverwrite=false;
	
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
	 * @param nOverwrite
	 *            用户词典引入方式
	 */
	public NLPIRTokenizerAnalyzer(String data,int encoding,String sLicenceCode,String userDict,boolean bOverwrite) {
		this.data=data;
		this.encoding=encoding;
		this.sLicenceCode=sLicenceCode;
		this.userDict=userDict;
		this.bOverwrite=bOverwrite;
	}
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer tokenizer = new NLPIRTokenizer(this.data,this.encoding,this.sLicenceCode,this.userDict,this.bOverwrite);
		return new TokenStreamComponents(tokenizer);
	}

}
