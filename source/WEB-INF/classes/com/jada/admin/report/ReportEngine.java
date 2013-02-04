package com.jada.admin.report;

import java.sql.Connection;
import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.LabelValueBean;
import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.element.IDataSet;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.engine.api.script.element.IResultSetColumn;
import org.hibernate.Session;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.jada.dao.ReportDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Report;
import com.jada.util.Constants;
import com.jada.util.Format;

public class ReportEngine {
	String siteId;
	Long reportId;
	ServletContext servletContext;
	IReportEngine engine = null;
	IReportRunnable runnable = null;
	IRunAndRenderTask task = null;
	String outputFormat = Constants.REPORT_OUTPUT_HTML;
	
	public ReportEngine(String siteId, Long reportId, ServletContext servletContext) throws Exception {
		this.siteId = siteId;
		this.reportId = reportId;
		this.servletContext = servletContext;
		
		Report report = ReportDAO.load(siteId, reportId);
		EngineConfig config = new EngineConfig();
		config.setEngineHome("");
		IPlatformContext context = new PlatformServletContext(servletContext);
		config.setPlatformContext(context);

		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
			.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		engine = factory.createReportEngine(config);
				
		runnable = engine.openReportDesign(new StringInputStream(report.getReportText()));
		task = engine.createRunAndRenderTask(runnable);
	}
	
	public Collection<?> getReportParameters() {
		IGetParameterDefinitionTask parameterTask = engine.createGetParameterDefinitionTask(runnable);
		Collection<?> params = parameterTask.getParameterDefns(true);
		return params;
	}
	
	public LabelValueBean[] getReportParameterOptions(IParameterDefnBase parameter) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		LabelValueBean options[] = null;
		ScalarParameterHandle handle = (ScalarParameterHandle) parameter.getHandle();
		
		Vector<LabelValueBean> v = new Vector<LabelValueBean>();
		if (handle.getDataSetName() != null) {
			String columnName = extractSqlColumn(handle.getValueExpr());
			
			List<?> list = runnable.getDesignInstance().getDataSet(handle.getDataSetName()).getCachedResultSetColumns();
			Iterator<?> columnIterator = list.iterator();
			int position = 0;
			while (columnIterator.hasNext()) {
				IResultSetColumn column = (IResultSetColumn) columnIterator.next();
				String name = column.getName();
				if (name.equals(columnName)) {
					break;
				}
				position++;
			}
			
			IDataSet dataSet = runnable.getDesignInstance().getDataSet(handle.getDataSetName());
			String queryText = dataSet.getQueryText();
			String sortByColumn = extractSqlColumn(handle.getSortByColumn());
			queryText += " order by " + sortByColumn;
			queryText += " " + handle.getSortDirection();
			Query query = em.createNativeQuery(queryText);
			Iterator<?> iterator = query.getResultList().iterator();
			while (iterator.hasNext()) {
				Object object[] = (Object[]) iterator.next();
				LabelValueBean bean = new LabelValueBean((String) object[position], (String) object[position]);
				v.add(bean);
			}
		}
		else {
			Iterator<?> iterator = handle.getListProperty("selectionList").iterator();
			while (iterator.hasNext()) {
				SelectionChoice selectionChoice = (SelectionChoice) iterator.next();
				LabelValueBean bean = new LabelValueBean(selectionChoice.getLabel(), selectionChoice.getValue());
				v.add(bean);
			}
		}
		
		options = new LabelValueBean[v.size()];
		v.copyInto(options);
		return options;
	}
	
	public void setReportParameter(String name, String value, String type) throws ParseException {
		if (type.equals(Constants.REPORT_BIRT_PARAM_TYPE_DATE)) {
			java.sql.Date v = new java.sql.Date(Format.getDate(value).getTime());
			task.setParameterValue(name, v);
		}
		else {
			task.setParameterValue(name, value);
		}
	}
	
	private String extractSqlColumn(String expression) {
		String result = "";
		String tokens[] = expression.split("\"");
		if (tokens.length > 1) {
			return tokens[1];
		}
		return result;
	}
	
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	void generate(HttpServletResponse response) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Session session = ((org.hibernate.ejb.EntityManagerImpl) em).getSession();
		Connection connection = session.connection();
		task.getAppContext().put("OdaJDBCDriverPassInConnection", connection);
		
		IRenderOption options = new RenderOption();		
		if (outputFormat.equals(Constants.REPORT_OUTPUT_HTML)) {
			options.setOutputFormat("html");
			response.setContentType("text/html");
		}
		if (outputFormat.equals(Constants.REPORT_OUTPUT_PDF)) {
			options.setOutputFormat("pdf");
			response.setContentType("application/pdf");
		}
		options.setOutputStream(response.getOutputStream());
		task.setRenderOption(options);
		task.run();
	}
}
