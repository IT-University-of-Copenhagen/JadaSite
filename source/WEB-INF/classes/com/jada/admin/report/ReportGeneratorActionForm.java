package com.jada.admin.report;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class ReportGeneratorActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -369540706402722978L;
	String reportId;
	String reportName;
	String reportDesc;
	String reportOutputMode;
	boolean generate;
	ReportParamDisplayForm reportParameters[];
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String id = "reportParameter.*name";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		reportParameters = new ReportParamDisplayForm[count];
		for (int i = 0; i < reportParameters.length; i++) {
			reportParameters[i] = new ReportParamDisplayForm();
		}
	}
	public ReportParamDisplayForm getReportParameter(int index) {
		return reportParameters[index];
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportDesc() {
		return reportDesc;
	}
	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	public ReportParamDisplayForm[] getReportParameters() {
		return reportParameters;
	}
	public void setReportParameters(ReportParamDisplayForm[] reportParameters) {
		this.reportParameters = reportParameters;
	}
	public String getReportOutputMode() {
		return reportOutputMode;
	}
	public void setReportOutputMode(String reportOutputMode) {
		this.reportOutputMode = reportOutputMode;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
}
