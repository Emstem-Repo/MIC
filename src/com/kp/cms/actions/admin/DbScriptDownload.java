package com.kp.cms.actions.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.hibernate.cfg.Configuration;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admin.DatabaseForm;


public class DbScriptDownload extends DownloadAction {

	private static final Logger log = Logger.getLogger(DbScriptDownload.class);
	@Override
	protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}
	
	
	/**
	 * This method gives month name if month value is provided.
	 * @param month value.
	 * @return month name.
	 */
	
	public String getMonthForInt(int monthValue) {
	    String month = "invalid";
	    DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getMonths();
	    if (monthValue >= 0 && monthValue <= 11 ) {
	        month = months[monthValue];
	    }
	    return month;
	}
}