package com.jada.admin.report;

import java.util.Vector;
import com.jada.admin.AdminMaintActionForm;

public class ReportGeneratorListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 6549052017434292024L;
	String srReportName;
	Vector<ReportDisplayForm> reports;
	String reportId;
	public String getSrReportName() {
		return srReportName;
	}
	public void setSrReportName(String srReportName) {
		this.srReportName = srReportName;
	}
	public Vector<ReportDisplayForm> getReports() {
		return reports;
	}
	public void setReports(Vector<ReportDisplayForm> reports) {
		this.reports = reports;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
