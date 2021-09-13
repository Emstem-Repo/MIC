package com.kp.cms.utilities;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class PGIResponseServletRevaluationReg
 */
public class PGIResponseServletRevaluationReg extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PGIResponseServletRevaluationReg.class);    
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String responseMsg=request.getParameter("msg");
		System.out.println("responce Paage");
		String hash=request.getParameter("hash");
		String txnid=request.getParameter("txnid");
		String productinfo=request.getParameter("productinfo");
		String amount=request.getParameter("amount");
		String email=request.getParameter("email");
		String firstname=request.getParameter("firstname");
		String phone=request.getParameter("phone");
		String unmappedstatus=request.getParameter("unmappedstatus");
		String mihpayid=request.getParameter("mihpayid");
		String mode=request.getParameter("mode");
		String status=request.getParameter("status");
		String key=request.getParameter("key");
		String Error=request.getParameter("Error");
		String bank_ref_num=request.getParameter("bank_ref_num");
		String payuMoneyId=request.getParameter("payuMoneyId");
		String additionalCharges=request.getParameter("additionalCharges");
		
		
		request.setAttribute("hash", hash);
		request.setAttribute("txnid", txnid);
		request.setAttribute("productinfo", productinfo);
		request.setAttribute("amount", amount);
		request.setAttribute("email", email);
		request.setAttribute("firstname", firstname);
		request.setAttribute("phone",phone);
		request.setAttribute("unmappedstatus", unmappedstatus);
		request.setAttribute("mihpayid", mihpayid);
		request.setAttribute("mode", mode);
		request.setAttribute("status", status);
		request.setAttribute("key", key);
		request.setAttribute("Error", Error);
		request.setAttribute("bank_ref_num", bank_ref_num);
		request.setAttribute("payuMoneyId",payuMoneyId);
		HttpSession session=request.getSession(true);
		session.setAttribute("isStudent", 1);
		session.setAttribute("studentId", 0);
		
		if(additionalCharges!=null){
			request.setAttribute("additionalCharges",additionalCharges);
		}else{
			request.setAttribute("additionalCharges","0");
		}
		//request.setAttribute("responseMsg", responseMsg);
		RequestDispatcher dispatcher=request.getRequestDispatcher("./jsp/pgiRepsonseReceiverRevaluation.jsp");
		dispatcher.forward(request, response);
	}
 }


