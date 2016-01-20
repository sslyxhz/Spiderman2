package net.kernal.spiderman.worker.extract;

import java.io.Serializable;

import net.kernal.spiderman.Properties;

public class ExtractResult implements Serializable {
	
	private static final long serialVersionUID = 2390695820923166121L;
	
	/**
	 * 所属页面名称
	 */
	private String pageName;
	/**
	 * 结果所属模型名称
	 */
	private String modelName;
	/**
	 * 字段值
	 */
	private Properties values;
	
	public ExtractResult(String pageName, String modelName, Properties values) {
		this.pageName = pageName;
		this.modelName = modelName;
		this.values = values;
	}

	public String getPageName() {
		return this.pageName;
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	public Properties getValues() {
		return this.values;
	}
	
	@Override
	public String toString() {
		return "ExtractResult [page=" + pageName + ", model=" + modelName + ", fields=" + values + "]";
	}
	
}
