package com.jada.admin.report;

public class ReportDisplayForm {
	String reportId;
	String reportName;
	String reportDesc;
	String lastRunDatetime;
	String lastRunBy;
	String systemRecord;
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
	public String getLastRunDatetime() {
		return lastRunDatetime;
	}
	public void setLastRunDatetime(String lastRunDatetime) {
		this.lastRunDatetime = lastRunDatetime;
	}
	public String getLastRunBy() {
		return lastRunBy;
	}
	public void setLastRunBy(String lastRunBy) {
		this.lastRunBy = lastRunBy;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
