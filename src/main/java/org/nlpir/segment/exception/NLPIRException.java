package org.nlpir.segment.exception;

/**
 * 分词异常类
 * @author panhongyan
 *
 */
public class NLPIRException extends Exception{

	private static final long serialVersionUID = 6897134322647598254L;

	public NLPIRException(String erromsg) {
		super(erromsg);
	}
}
