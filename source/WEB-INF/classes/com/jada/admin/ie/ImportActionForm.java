package com.jada.admin.ie;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class ImportActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 1L;
	private String importType;
	private String importLocation;
	private String hostFileName;
	private boolean skipFirstLine;
	private FormFile localFile;
	private String ieProfileHeaderId;
	private LabelValueBean ieProfileHeaderList[];
	private String messageText;
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		skipFirstLine = false;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getImportLocation() {
		return importLocation;
	}
	public void setImportLocation(String importLocation) {
		this.importLocation = importLocation;
	}
	public String getHostFileName() {
		return hostFileName;
	}
	public void setHostFileName(String hostFileName) {
		this.hostFileName = hostFileName;
	}
	public FormFile getLocalFile() {
		return localFile;
	}
	public void setLocalFile(FormFile localFile) {
		this.localFile = localFile;
	}
	public boolean isSkipFirstLine() {
		return skipFirstLine;
	}
	public void setSkipFirstLine(boolean skipFirstLine) {
		this.skipFirstLine = skipFirstLine;
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
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
}
