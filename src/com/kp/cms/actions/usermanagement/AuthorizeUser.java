package com.kp.cms.actions.usermanagement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

//import com.kp.cms.bo.admin.UsageLog;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.utilities.HibernateUtil;

/**
 * A filter action authorize actions agenest user name and password.
 */
@SuppressWarnings("deprecation")
public class AuthorizeUser implements Filter {
	
//	private static final Log log = LogFactory.getLog(AuthorizeUser.class);
	
	private String onErrorUrl;
	private String onErrorOnlineUrl;

	public void destroy() {	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException{

 		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try{
		if (req.getRequestURI() != null
				&& (req.getRequestURI().endsWith("Login.do") || 
					req.getRequestURI().endsWith("LogoutAction.do") || 
					req.getRequestURI().endsWith("LoginAction.do") || 
					req.getRequestURI().endsWith("StudentLoginAction.do") || 
					req.getRequestURI().endsWith("AdmissionStatus.do") || 
					req.getRequestURI().endsWith("StudentLogin1.do") || 
					req.getRequestURI().contains("LoginAction.do") || 
					
					req.getRequestURI().endsWith("uniqueIdRegistration.do"))) {
			
				HttpSession session = req.getSession(false);
				String method=request.getParameter("method");
				
				if(method!=null && (method.equalsIgnoreCase("logout") || 
				   method.equalsIgnoreCase("initLoginAction") || 
				   method.equalsIgnoreCase("initOnlineApplicationRegistration") || 
				   method.equalsIgnoreCase("initOutsideAccessStatus") || 
				   method.equalsIgnoreCase("studentLoginAction") || 
				   method.equalsIgnoreCase("initOnlineApplicationRegistration") || 
				   method.equalsIgnoreCase("initOnlineApplicationLogin") || 
				   method.equalsIgnoreCase("initOnlineApplicationForgotUniqueId"))){
					
					if(session!=null)
					session.invalidate();
					
				}
				
				if(method==null && session!=null){
					session.invalidate();
				}
				
				chain.doFilter(request, response);
		}
		else {
			HttpSession session= req.getSession(false);
			ActionErrors errors = new ActionErrors();
			String user="";
			/*String tempLogin=request.getParameter("tempLogin");
			if(tempLogin != null && !StringUtils.isEmpty(tempLogin) && tempLogin.equals("1")){
				session = req.getSession(true);
				if(session.getAttribute("loginform") == null) {
					LoginForm loginForm = new LoginForm();
					session.setAttribute("loginform",loginForm);
				}
				session.setAttribute("onlineuser",1);
				session.setAttribute("uid",CMSConstants.ONLINE_USERID);
				
			} else {
				session = req.getSession();
			}*/
			
			//check session already is there he already loged in
			if(session!=null){
				
				user = (String) session.getAttribute("uid");
				if (user == null || session == null) {
					if (req.getRequestURI() != null && (req.getRequestURI().contains("uniqueIdRegistration.do")))
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
				}
				
				if(session.getAttribute("isOnline")!=null){
					if (req.getRequestURI() != null
							&& req.getRequestURI().endsWith("AdmissionStatus.do") 
									|| req.getRequestURI().contains("onlineApplicationSubmit.do")
									|| req.getRequestURI().endsWith("uniqueIdRegistration.do")
									|| req.getRequestURI().endsWith("AjaxRequest.do")
									|| req.getRequestURI().endsWith("DocumentDownloadAction.do")) {

						session.removeAttribute("isStudent");
						session.removeAttribute("isEmployee");
						session.removeAttribute("isEmployeeOTPChecked");
						String method=request.getParameter("method");
						if(method!=null && (method.equalsIgnoreCase("initAdmissionStatus"))){
							
							//invalid access
							
							req.getRequestDispatcher(onErrorOnlineUrl).forward(req, res);
							
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
							req.setAttribute(Globals.ERROR_KEY, errors);
							
							session.invalidate();
						
							
						}
						//valid user
						else{
							
							if(session.getAttribute("loginform") == null) {
								LoginForm loginForm = new LoginForm();
								session.setAttribute("loginform",loginForm);
							}
							session.setAttribute("onlineuser",1);
							session.setAttribute("uid",CMSConstants.ONLINE_USERID);
							
						}
					
						
					}
					//invalid access
					else{
						
						req.getRequestDispatcher(onErrorOnlineUrl).forward(req, res);
						
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
						req.setAttribute(Globals.ERROR_KEY, errors);
						
						session.invalidate();
					}
					
					
					
				}
				else if(session.getAttribute("isStudent")!=null && ( session.getAttribute("admApplnId")!=null || session.getAttribute("studentId")!=null)){
					
					session.removeAttribute("isOnline");
					session.removeAttribute("isEmployee");
					session.removeAttribute("isEmployeeOTPChecked");
					if (req.getRequestURI() != null
							&& req.getRequestURI().endsWith("StudentLoginAction.do") 
									|| req.getRequestURI().contains("ChangePassword.do")
									|| req.getRequestURI().endsWith("studentWiseAttendanceSummary.do")
									|| req.getRequestURI().endsWith("ExtraCocurricularLeaveEntry.do")
									|| req.getRequestURI().endsWith("studentFeedBack.do")
									|| req.getRequestURI().endsWith("NewStudentCertificateCourse.do")
									|| req.getRequestURI().endsWith("newSupplementaryImpApp.do")
									|| req.getRequestURI().endsWith("EvaluationStudentFeedback.do")
									|| req.getRequestURI().endsWith("AjaxRequest.do")
									|| req.getRequestURI().endsWith("DocumentDownloadAction.do")
									|| req.getRequestURI().endsWith("OptionalCourseApplication.do")) {
						
						String method=request.getParameter("method");
						if(method!=null && (method.equalsIgnoreCase("initStudentWiseAttendanceSummary") 
								|| method.equalsIgnoreCase("initSupplementaryImpApplication") 
								|| method.equalsIgnoreCase("initSupplementaryImpApplicationForAll"))){
							
							//invalid method access
							
							req.getRequestDispatcher("/StudentLogin.do").forward(req, res);
							
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
							req.setAttribute(Globals.ERROR_KEY, errors);
							
							session.invalidate();
						
							
						}
						//valid user
						else{
							
							if(session.getAttribute("loginform") == null) {
								LoginForm loginForm = new LoginForm();
								session.setAttribute("loginform",loginForm);
							}
							session.setAttribute("onlineuser",0);
							session.setAttribute("uid",user);
							
						}
						
					}
					//invalid method access
					else{
						
						req.getRequestDispatcher("/StudentLogin.do").forward(req, res);
						
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
						req.setAttribute(Globals.ERROR_KEY, errors);
						
						session.invalidate();
					}
					
					
					
				}
				else if(session.getAttribute("isEmployee")!=null &&  session.getAttribute("rid")!=null 
						&& ( session.getAttribute("isEmployeeOTPChecked")!=null || !CMSConstants.OTPROLEIDSLIST.contains(Integer.parseInt(session.getAttribute("rid").toString())) )){
					
					//check roleid for otp
					
					
					session.removeAttribute("isOnline");
					session.removeAttribute("isStudent");
					//String method=request.getParameter("method");
					if(req.getRequestURI().endsWith("StudentLoginAction.do") 
							|| req.getRequestURI().endsWith("uniqueIdRegistration.do") 
							|| req.getRequestURI().endsWith("onlineApplicationSubmit.do")){
						
						//invalid method access for employee
						req.getRequestDispatcher(onErrorUrl).forward(req, res);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
						req.setAttribute(Globals.ERROR_KEY, errors);
						
						session.invalidate();
					
						
					}else{
						if(session.getAttribute("loginform") == null) {
							LoginForm loginForm = new LoginForm();
							session.setAttribute("loginform",loginForm);
						}
						session.setAttribute("onlineuser",0);
						session.setAttribute("uid",user);
					}
					
					
					
				}
				//un authorised person
				else{
					
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
					req.setAttribute(Globals.ERROR_KEY, errors);
					req.getRequestDispatcher(onErrorUrl).forward(req, res);
					session.invalidate();
				}
				
			}
			//if session null throw out side
			else{
				if(req.getRequestURI().endsWith("uniqueIdRegistration.do") || req.getRequestURI().endsWith("onlineApplicationSubmit.do")) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
					req.setAttribute(Globals.ERROR_KEY, errors);
					req.getRequestDispatcher(onErrorOnlineUrl).forward(req, res);
				}
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("knowledgepro.admin.invalidsession"));
				req.setAttribute(Globals.ERROR_KEY, errors);
				req.getRequestDispatcher(onErrorUrl).forward(req, res);
			}
			
			//errors not there allow him to access our server
			if (errors.isEmpty()) {
				if(req.getParameter("menuName") != null){
					session.setAttribute("realPath",request.getRealPath(""));
					
					//added by dIlIp
					FileWriter fw= new FileWriter(request.getRealPath("")+ "//TempFiles//menu.txt", true);
					BufferedWriter bw=new BufferedWriter(fw);
					bw.write("Menu Name :"+req.getParameter("menuName")+"    User Id:"+user+"  Time :"+new Date());
					bw.newLine();
					bw.close();
					
					//insert into table
					//if(Integer.parseInt(req.getParameter("menuId"))!=0){
						//raghu
						/*UsageLog usageLog = new UsageLog();
						
						Menus menu = new Menus();
						menu.setId(Integer.parseInt(req.getParameter("menuId")));
						usageLog.setMenuId(menu);
						//usageLog.setReportMenuId();
						
						Users userNew = new Users();
						userNew.setId(Integer.parseInt(user));
						usageLog.setUserId(userNew);
						
						usageLog.setDate(new Date());
						usageLog.setTime(new Time(new Date().getTime()));
						
						usageLog.setCreatedBy(user);
						usageLog.setCreatedDate(new Date());
						
						PropertyUtil prop = PropertyUtil.getInstance();
						prop.save(usageLog);*/
					//}
					
				}
				chain.doFilter(request, response);
			} 
			
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("=====" + e +"=====");
		}finally{
			HibernateUtil.closeSession();
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		onErrorUrl = filterConfig.getInitParameter("onError");
		onErrorOnlineUrl= filterConfig.getInitParameter("onErrorOnlineUrl");
		if (onErrorUrl == null || "".equals(onErrorUrl)) {
			onErrorUrl = "/index.jsp";
		}
		onErrorOnlineUrl="/OnlineApplicationServlet";
	}

}
