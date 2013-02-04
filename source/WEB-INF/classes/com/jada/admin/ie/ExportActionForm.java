package com.jada.admin.ie;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class ExportActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -2903245897340800934L;
	private String exportType;
	private String exportLocation;
	private String ieProfileHeaderId;
	private String hostFileName;
	private boolean generate;
	private LabelValueBean ieProfileHeaderList[];
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	public String getHostFileName() {
		return hostFileName;
	}
	public void setHostFileName(String hostFileName) {
		this.hostFileName = hostFileName;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
	public String getExportLocation() {
		return exportLocation;
	}
	public void setExportLocation(String exportLocation) {
		this.exportLocation = exportLocation;
	}
	public String getIeProfileHeaderId() {
		return ieProfileHeaderId;
	}
	public void setIeProfileHeaderId(String ieProfileHeaderId) {
		this.ieProfileHeaderId = ieProfileHeaderId;
	}
	public LabelValueBean[] getIeProfileHeaderList() {
		return ieProfileHeaderList;
	}
	public void setIeProfileHeaderList(LabelValueBean[] ieProfileHeaderList) {
		this.ieProfileHeaderList = ieProfileHeaderList;
	}
}
