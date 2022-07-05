<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<html:html>
	<head>
		<title><bean:message key="knowledgepro.title"/> </title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" src="jquery/collaps/jquery-1.10.2.js"></script>
		<link rel="stylesheet" type="text/css" href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
		<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
		<script language="JavaScript" src="js/calendar_us.js"></script>
		<link rel="stylesheet" href="css/calendar.css">
		<link rel="stylesheet" href="css/admission/css/stylesheet-pure-css.css"/>  

		<!-- for cache controling with html code-->
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		 
		 <!-- for cache controling with jsp code-->
		<% 
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
			response.setHeader("Expires", "0"); // Proxies.
		%>

		<style type="text/css">
			<!--
			body {
				margin-left: 0px;
				margin-top: 5px;
				margin-right: 0px;
				margin-bottom: 0px;
			}
			-->
		</style>

		<script type="text/javascript">
			var jq=$.noConflict();
			function IsNumeric(sText) {
		   		var ValidChars = "0123456789.";
		   		var IsNumber=true;
		   		var Char;
		   		var v;
		   		for (v = 0; v < sText.length && IsNumber == true; v++) { 
		      		Char = sText.charAt(v); 
		      		if(ValidChars.indexOf(Char) == -1) {
		         		IsNumber = false;
		         	}
		      	}
		   		return IsNumber;
		   	}
		
			function displayOtherForSubject(i) {
				var propertyName="detailMark.subjectid"+i;
				var propertyOtherName="detailMark.subjectOther"+i;
				var propertyOtherNameDisplay=""+propertyOtherNameDisplay+i;
				if((document.getElementById("detailMark.subjectid"+i).value=="0") || (document.getElementById("detailMark.subjectid"+i).value=="0") || (document.getElementById("detailMark.subjectid"+i).value=="0")){
					document.getElementById("propertyOtherNameDisplay"+i).style.display = "block";
				}else{
					document.getElementById("propertyOtherNameDisplay"+i).style.display = "none";
					document.getElementById("detailMark.subjectOther"+i).value = "";
				}
			}
		
			function resetDetailMarkDegree(count) {
				document.getElementById("totalMark").value="";
			 	document.getElementById("ObtainedMark").value="";
			 	if( document.getElementById("totallangMark")!=null)
			 		document.getElementById("totallangMark").value="";
			 	if(document.getElementById("ObtaintedlangMark")!=null)
			 		document.getElementById("ObtaintedlangMark").value="";
			 	if(document.getElementById("totalObtainedMarksCGPA")!=null)
			 		document.getElementById("totalObtainedMarksCGPA").value="";
			 	if(document.getElementById("totalMarksCGPA")!=null)
			 		document.getElementById("totalMarksCGPA").value="";
					
				var i;
				for(i=1;i<=count;i++) {
					if(i!=6 && i!=16) {
						if(document.getElementById("detailMark.subjectid"+i)!=null)
							document.getElementById("detailMark.subjectid"+i).value="";
					}
					if(document.getElementById("detailMark.subject"+i+"TotalMarks")!=null)
						document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
					if(document.getElementById("detailMark.subject"+i+"ObtainedMarks")!=null)
						document.getElementById("detailMark.subject"+i+"ObtainedMarks").value="";
					if(document.getElementById("detailMark.subjectOther"+i)!=null){
						document.getElementById("detailMark.subjectOther"+i).value="";
						document.getElementById("propertyOtherNameDisplay"+i).style.display = "none";
					}
					if(document.getElementById("detailMark.subject"+i+"Credit")!=null)
						document.getElementById("detailMark.subject"+i+"Credit").value="";
				}
			 	resetErrMsgs();
			}
		
			function updatetotalMarkPreCBCSS(count) {
				var totalmark=document.getElementById("totalMark").value;
				totalmark=0;
				var i;
				for(i=11;i<=count;i++) {
					var subjectmark=document.getElementById("detailMark.subject"+i+"TotalMarks").value;
					if(subjectmark.length==0) {
						subjectmark=0;
					}
					if(IsNumeric(totalmark)) {
						totalmark=parseFloat(subjectmark)+parseFloat(totalmark);
						document.getElementById("totalMark").value=totalmark;
					}
				}
			}
			function updateObtainMarkPreCBCSS(count) {
				var obtainmark=document.getElementById("ObtainedMark").value;
				obtainmark=0;
				var i;
				for(i=11;i<=count;i++) {
					var subjectmark=document.getElementById("detailMark.subject"+i+"ObtainedMarks").value;
					if(subjectmark.length==0) {
						subjectmark=0;
					}
					if(IsNumeric(obtainmark)) {
						obtainmark=parseFloat(subjectmark)+parseFloat(obtainmark);
						document.getElementById("ObtainedMark").value=obtainmark;
					}
				}
			}
		
			function checkObtainedCbcssMarks(count) {
				var i;
				for(i=1;i<=count;i++) {
					var subjecobtaintmark=document.getElementById("detailMark.subject"+i+"ObtainedMarks").value;
					var subjecttotalmark=document.getElementById("detailMark.subject"+i+"TotalMarks").value;
					if(i==1){
						 document.getElementById("totalMarksCGPA").value=subjecttotalmark;
					}
					if(subjecobtaintmark.length!=0 && subjecttotalmark.length!=0) {
						if(IsNumeric(subjecobtaintmark) && IsNumeric(subjecttotalmark)) {
							if(parseFloat(subjecobtaintmark)>parseFloat(subjecttotalmark)) {
								document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
								jq.confirm({
									
									'message'	: 'WARNING:  Entered marks are greater than Maximum marks ',
									'buttons'	: {
										'Ok'	: {
											'class'	: 'blue',
											'action': function(){
												jq.confirm.hide();
												
											}
										}
							        
									}
								});
							}
						}
					}
					if(subjecttotalmark.length!=0) {
						if(IsNumeric(subjecttotalmark)) {
							if(document.getElementById("CBCSSNEWRadio").checked==true) {
								if(parseFloat(subjecttotalmark)>10){
									document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
									jq.confirm({
										'message'	: 'WARNING:  Entered max grade point should be 10 ',
										'buttons'	: {
											'Ok'	: {
												'class'	: 'blue',
												'action': function(){
													jq.confirm.hide();
												}
											}
										}
									});				
								}
							}
						}
					}
				}
	
				var subjecobtaintmark=document.getElementById("totalObtainedMarksCGPA").value;
				var subjecttotalmark=document.getElementById("totalMarksCGPA").value;
			
				if(subjecobtaintmark.length!=0 && subjecttotalmark.length!=0) {
					if(IsNumeric(subjecobtaintmark) && IsNumeric(subjecttotalmark)) {
						if(parseFloat(subjecobtaintmark)>parseFloat(subjecttotalmark)) {
							document.getElementById("totalMarksCGPA").value="";
							jq.confirm({
								'message'	: 'WARNING:  Entered marks are greater than Maximum marks ',
								'buttons'	: {
									'Ok'	: {
										'class'	: 'blue',
										'action': function(){
											jq.confirm.hide();
										}
									}
								}
							});
						}
					}
				}
			}

			function checkObtainedPreCbcssMarks(count) {
				var i;
				for(i=11;i<=count;i++) {
					var subjecobtaintmark=document.getElementById("detailMark.subject"+i+"ObtainedMarks").value;
					var subjecttotalmark=document.getElementById("detailMark.subject"+i+"TotalMarks").value;
					if(subjecobtaintmark.length!=0 && subjecttotalmark.length!=0) {
						if(IsNumeric(subjecobtaintmark) && IsNumeric(subjecttotalmark)) {
							if(parseFloat(subjecobtaintmark)>parseFloat(subjecttotalmark)) {
								document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
								jq.confirm({
									'message'	: 'WARNING:  Entered marks are greater than Maximum marks ',
									'buttons'	: {
										'Ok'	: {
											'class'	: 'blue',
											'action': function(){
												jq.confirm.hide();
												
											}
										}
									}
								});
							}
						}
					}
				}
			}
		
			function submitAdmissionFormCalc(val) {
			    var outOf=false;
				if(document.getElementById("CBCSSNEWRadio").checked==true) {
					var count=document.getElementById("subjectcbcssCount").value;
					var i;
					for(i=1;i<=count;i++) {
						if(document.getElementById("detailMark.subjectid"+i)!=null && document.getElementById("detailMark.subject"+i+"TotalMarks")!=null && document.getElementById("detailMark.subject"+i+"ObtainedMarks")!=null){
							if(document.getElementById("detailMark.subjectid"+i).value!="" && document.getElementById("detailMark.subject"+i+"TotalMarks").value!="" && document.getElementById("detailMark.subject"+i+"ObtainedMarks").value!=""){
								var subject=document.getElementById("detailMark.subjectid"+i).value;
								var subjecttotal=parseFloat(document.getElementById("detailMark.subject"+i+"TotalMarks").value);
								var subjectobtained=parseFloat(document.getElementById("detailMark.subject"+i+"ObtainedMarks").value);
								if(IsNumeric(subjecttotal)) {
									if(document.getElementById("CBCSSNEWRadio").checked==true) {
										if(parseFloat(subjecttotal)>10) {
											document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
											jq.confirm({
												'message'	: 'WARNING:  Entered max grade point should be 10 ',
												'buttons'	: {
													'Ok'	: {
													'class'	: 'blue',
													'action': function(){
															jq.confirm.hide();
														}
													}
												}
											});				
										}
									}
								}
								if(subjecttotal==subjectobtained) {
									outOf=true;
								}
							}
						}
					}
				}
				if(document.getElementById("OTHERRadio").checked==true) {
					var count=document.getElementById("subjectprecbcssCount").value;
					var i;
				
					for(i=11;i<=count;i++)  {
						if(document.getElementById("detailMark.subjectid"+i)!=null && document.getElementById("detailMark.subject"+i+"TotalMarks")!=null && document.getElementById("detailMark.subject"+i+"ObtainedMarks")!=null){
							if(document.getElementById("detailMark.subjectid"+i).value!="" && document.getElementById("detailMark.subject"+i+"TotalMarks").value!="" && document.getElementById("detailMark.subject"+i+"ObtainedMarks").value!=""){
								var subject=document.getElementById("detailMark.subjectid"+i).value;
								var subjecttotal=parseFloat(document.getElementById("detailMark.subject"+i+"TotalMarks").value);
								var subjectobtained=parseFloat(document.getElementById("detailMark.subject"+i+"ObtainedMarks").value);
								if(subjecttotal==subjectobtained){
									outOf=true;
								}
								//newly added
								if(IsNumeric(subjecttotal)) {
									if(document.getElementById("CBCSSNEWRadio").checked==true) {
										if(parseFloat(subjecttotal)>10) {
											jq.confirm({
												'message'	: 'WARNING:  Entered max grade point should be 10 ',
												'buttons'	: {
													'Ok'	: {
														'class'	: 'blue',
														'action': function(){
															jq.confirm.hide();
														}
													}
												}
											});				
										}
									}
								}
							}
						}
					}
				}
				if(outOf) {
					jq.confirm({
						'message'	: 'WARNING: You have same marks in obtained and max marks,If you want to re check once please click on cancel.',
						'buttons'	: {
							'Ok'	: {
								'class'	: 'blue',
								'action': function(){
									jq.confirm.hide();
									
									 document.getElementById("method").value=val;
									 document.onlineApplicationForm.submit();
								}
							},
				        'Cancel'	:  {
								'class'	: 'gray',
								'action': function(){
									jq.confirm.hide();
								}
							}
						}
					});
				} else{
					document.getElementById("method").value=val;
					document.onlineApplicationForm.submit();
				}
			}
		
			function dupsub(val) {
				var flag=false;
				var i;
				var j;
				if(document.getElementById("CBCSSNEWRadio").checked==true) {
					var count=document.getElementById("subjectcbcssCount").value;
					var subject2=document.getElementById("detailMark.subjectid"+val).value;
					for(i=1;i<=count;i++) {
						if(val!=i) {
							if(document.getElementById("detailMark.subjectid"+i).value!="" &&document.getElementById("detailMark.subjectid"+val).value!="") {
								var subject1=document.getElementById("detailMark.subjectid"+i).value;
								if(subject1==subject2) {
									flag=true;
									document.getElementById("detailMark.subjectid"+val).value="";
								}
							}
						}
					}
				}
				if(document.getElementById("OTHERRadio").checked==true) {
					var count=document.getElementById("subjectprecbcssCount").value;
					var subject2=document.getElementById("detailMark.subjectid"+val).value;
					for(i=11;i<=count;i++) {
						if(val!=i) {
							if(document.getElementById("detailMark.subjectid"+i).value!="" &&document.getElementById("detailMark.subjectid"+val).value!="") {
								var subject1=document.getElementById("detailMark.subjectid"+i).value;
								if(subject1==subject2) {
									flag=true;
									document.getElementById("detailMark.subjectid"+val).value="";
								}
							}
						}
					}
				}
				if(flag)
				{
					jq.confirm({
						'message'	: 'WARNING: You entered same subject',
						'buttons'	: {
							'Ok'	: {
								'class'	: 'blue',
								'action': function(){
									jq.confirm.hide();
								}
							}
						}
					});
				}
			}
		
			function validate(evt) {
			  	var theEvent = evt || window.event;
			  	var key = theEvent.keyCode || theEvent.which;
			  	key = String.fromCharCode( key );
			  	var regex = /[0-9]|\./;
			  	if( !regex.test(key) ) {
					if (theEvent.keyCode!=8) {
			    		theEvent.returnValue = false;
			    		if(theEvent.preventDefault) theEvent.preventDefault();
				  	}
			  	}
			}
		
			jq(document).ready(function(){
				var options = {};
				if(jq('#CBCSSRadio').is(':checked')) {
					 jq(".CBCSS").show();
					 jq(".OTHER").hide();
				}else if(jq('#CBCSSNEWRadio').is(':checked')) {
					 jq(".CBCSS").show();
					 jq(".OTHER").hide();
				}
				else if(jq('#OTHERRadio').is(':checked')) {
					 jq(".OTHER").show();
					 jq(".CBCSS").hide();
				}
				else{
					jq(".CBCSS").hide();
					jq(".OTHER").hide();
				}
				jq("#CBCSSRadio").click(function(){
				 	jq(".CBCSS").show(2000);
				 	jq(".OTHER").hide();
			  	});
				jq("#CBCSSNEWRadio").click(function(){
				 	jq(".CBCSS").show(2000);
				 	jq(".OTHER").hide();
			  	});
				jq("#OTHERRadio").click(function(){
					jq(".CBCSS").hide();
					jq(".OTHER").show(2000);
				});
			});
		
			var hook = true;
		
			function appendMethodOnBrowserClose(){
				hook = false;
			}
			$(function() {
				 $("a,.formbutton").click(function(){
					hook =false;
				  });
			});
		
			window.onbeforeunload = confirmExit;
		  	function confirmExit() {
				if(hook){
			  		hook =false;
				}else{
					hook =true;
				}
		  	}
		
			$(document).ready(function() {	
				var ctrlKeyDown = false;
				$(document).on("keydown", keydown);
			    $(document).on("keyup", keyup);
		
			    function keydown(e) { 
			        if ((e.which || e.keyCode) == 116 || ((e.which || e.keyCode) == 82 && ctrlKeyDown)) {
			            // Pressing F5 or Ctrl+R
			            e.preventDefault();
			        } else if ((e.which || e.keyCode) == 17) {
			            // Pressing  only Ctrl
			            ctrlKeyDown = true;
			        }
			    };
		
			    function click(e){
			    	e.preventDefault();
			    };
			
			    function keyup(e){
			        // Key up Ctrl
			        if ((e.which || e.keyCode) == 17) 
			            ctrlKeyDown = false;
			    };
		    });
		</script>
	</head>
	
	<body>
		<input type="hidden" id="subjectcbcssCount" value="<%=CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS %>" />
		<input type="hidden" id="subjectprecbcssCount" value="<%=CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS %>" />

		<table width="80%" style="background-color: #F0F8FF" align="center">
			<tr><td></td></tr>
  			<tr><td  align="left"><div id="errorMessage"><html:errors/></div></td></tr>
  			<tr>
    			<td>
    				<table width="100%" height="347"  border="0" cellpadding="0" cellspacing="0">
              			<tr>
                   			<td height="10" colspan="6"  align="center"><h3><font color="red">Enter Your Qualifying Examination Marks</font></h3></td>
             			</tr>
             			<tr>
             				<td width="20%"></td>
             				<td height="25" colspan="5" >Note:</td>
             			</tr>
             			<tr>
             				<td  width="20%"></td>
             				<td height="25" colspan="5" >
             					1. Single main candidates need to enter the mark/grade obtained for the Core One subject in the space provided for Core 1.
             				</td>
             			</tr>
             			<tr>
             				<td width="20%"></td>
             				<td height="25" colspan="5" >
			 					2. Double main candidates must enter the marks/grades obtained for the Core Two subjects in the space for Core 1 and 2.
			  				</td>
			  			</tr>
			 			<tr>
			 				<td width="20%"></td>
			 				<td height="25" colspan="5" >
			 					3. Triple main students are required to enter their marks/grades obtained for the Core Three subject in the space provided for Core 1,2 and 3.
			  				</td>
			  			</tr>
			 			<tr>
			 				<td width="20%"></td>
			 				<td height="25" colspan="5" >
			 					4. Cadidates with Single Complementary Subject should enter their MARK/GRADE in the space provided for Complementary 1 only.
			  				</td>
			  			</tr>
             			<tr>
             				<td height="5" colspan="6" align="center"><h3><font color="blue">Choose the pattern which you studied for UnderGraduate program</font></h3></td>
             			</tr>
              			<tr>
							<td width="33%" class="heading" colspan="2" id="CBCSS" align="center">
			             		<fieldset style="border: 0px">
			             			<html:radio styleId="CBCSSNEWRadio" property="patternofStudy" value="CBCSS NEW"></html:radio>
                         			<label for="CBCSSNEWRadio"><span><span></span></span>CBCSS Pattern(Grade Points with Credits)</label> 
			             		</fieldset>
                        	</td>
                         	<td width="33%" class="heading" id="OTHER" colspan="2" >
                         		<fieldset style="border: 0px">
			             			<html:radio styleId="OTHERRadio" property="patternofStudy" value="OTHER"></html:radio>
                         			<label for="OTHERRadio"><span><span></span></span>Reversed CBCSS Pattern(mark entry)</label> 
			             		</fieldset>
			             		<script>
									console.log("university"+'<bean:write name="onlineApplicationForm" property="applicantDetails.ednQualificationList[0].universityId"/>');
									var unid='<bean:write name="onlineApplicationForm" property="applicantDetails.ednQualificationList[0].universityId"/>';
									
									if (unid!=20) {
										console.log("not  kerala university");
										document.getElementById("OTHERRadio").checked=true;
										document.getElementById('CBCSSNEWRadio').style.visibility = 'hidden';
										 document.getElementById("CBCSSNEWRadio").disabled = true;
										console.log(document.getElemenntById(OTHERRadio).value);
									}
								</script>
                        	</td>
         				</tr>
              			
              			<tr><td colspan="6"><br></br></td></tr>
              			
              			<tr class="CBCSS">
                   			<td height="35" colspan="6" class="body" >
                   				<table width="100%" cellspacing="1" cellpadding="2">
                     				<tr bgcolor="#FFFFFF"><td height="10" colspan="22"></td></tr>
                     				<tr>
                     				
										<td class="row-odd" width="30%"><bean:message key="admissionForm.detailmark.slno.label"/></td>
										<td class="row-odd" width="25%" ><div align="center"><bean:message key="knowledgepro.admin.subject.subject.name.disp"/> </div></td>
				 						<td height="25" class="row-odd" width="15%"><div align="center">Obtained CCPA/CGPA/Grade Points</div></td>
										<td height="25" class="row-odd" width="15%"><div align="center">Max CCPA/CGPA/Grade Points</div></td>
										<td height="25" class="row-odd" width="15%"><div align="center">Credit</div></td>
										
									</tr>
					
									<%
										int coreCount=1;
										int complementaryCount=1;
										int commonCount=1;
										int openCount=1;
										int foundationCount=1;
										int vocCount=1;
										int i=0;
										for(i=1;i<=CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS;i++) {
											String propertyName="detailMark.subjectid"+i;
											String dynaMandatory="detailMark.subject"+i+"Mandatory";
											String propertyDetailedName="detailMark.detailedSubjects"+i+".id";
											String propertyDetailedId="detailMark.detailedSubjects"+i;
											String totalMarkprop="detailMark.subject"+i+"TotalMarks";
											String obtainMarkprop="detailMark.subject"+i+"ObtainedMarks";
											String method = "putSubjectName('"+i+"',this.value)";
											String propertyOtherName="detailMark.subjectOther"+i;
											String degmaxcgpa="detailMark.subject"+i+"Credit";
											String propertyOtherNameDisplay="propertyOtherNameDisplay"+i;
											String dynajs="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");";
											String addMaxCCPA="addMaxCCPA('"+i+"',this.value)";
											
									%>
                     				<tr>
                     					<%if(i<=3) {%>
                         					<td class="row-even" > <%=i %>&nbsp;&nbsp;&nbsp;Core Course <%=coreCount%>
					                         	<% 
					                         		if(coreCount==2) {
					                         	%>	 
					                        	 		(For Model II / Double Main)
						                        <%	 
						                         	} else if(coreCount==3) {
						                        %>
					                          	 		(For Model III / Triple Main)
					                         	<%	 
					                         		}
					                         	%>
                         					</td>
											<td class="row-even" >
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' styleClass="combolarge" onchange='<%="dupsub("+i+");"%>' style="width:300px" >
                    								<html:option value="">--Select--</html:option>
                    								<html:optionsCollection property="admCoreMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6"  onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												coreCount++;
											}
											else if(i<=5) {
										%>
											<td class="row-even" > <%=i %>&nbsp;&nbsp;&nbsp;Complementary Course <%=complementaryCount%></td>
											<td class="row-even" >
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
	                    							<html:option value="">--Select--</html:option>
	                    							<html:optionsCollection property="admComplMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
									 				<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
									 					<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
									 				</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
									 					<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												complementaryCount++;
											}
											else if(i==6) {
										%>
											<td class="row-even" > <%=i %>&nbsp;&nbsp;&nbsp;Common <%=commonCount%> (English Language course)</td>
											<td class="row-even" >
												<nested:select disabled="false" property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+")" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admCommonMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
														<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
														<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>'  styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												commonCount++;
											}
											else if(i==7) {
										%>
											<td class="row-even"> <%=i %>&nbsp;&nbsp;&nbsp;Common <%=commonCount%> (Additional Language)</td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admSubMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												commonCount++;
											}
											else if(i<=8) {
										%>
											<td class="row-even"> <%=i %>&nbsp;&nbsp;&nbsp;Open Course <%=openCount%></td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admMainMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												openCount++;
											}
											else if(i==9) {
										%>
											<td class="row-even" > <%=i %>&nbsp;&nbsp;&nbsp;Vocational Course <%=vocCount%></td>
											<td class="row-even" >
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="vocMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												openCount++;
											}
											else if(i==10) {
										%>
											<td class="row-even" > <%=i %>&nbsp;&nbsp;&nbsp;Foundation Course <%=foundationCount%></td>
											<td class="row-even" >
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="foundationMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=degmaxcgpa %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=degmaxcgpa %>'></html:text></td>
										<%
												foundationCount++;
											}
										%>
									</tr>
									<%} %>
									<tr>
									     <td class="row-odd" >
									     <logic:equal value="20" property="tempUniversityId" name="onlineApplicationForm">
									     Candidate from University of kerala need to enter CCPA(S) Details in the total fields 
									     </logic:equal>
									     </td>
									    <td class="row-odd" align="center">Total</td>
									    <logic:equal value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">&nbsp;OBTAINED CCPA(S):<br>
										<html:text property="detailMark.totalObtainedMarksCGPA" size="7" maxlength="7" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' styleId="totalObtainedMarksCGPA" ></html:text><b></b>
										</td>
										</logic:equal>
										<logic:notEqual value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">&nbsp;OBTAINED CCPA/CGPA:<br>
										<html:text property="detailMark.totalObtainedMarksCGPA" size="7" maxlength="7" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' styleId="totalObtainedMarksCGPA" ></html:text><b></b>
										</td>
										</logic:notEqual>
										<logic:equal value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">MAX CCPA(S):<br>
										<html:text property="detailMark.totalMarksCGPA" styleId="totalMarksCGPA" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' size="7" maxlength="7" ></html:text><b></b>
										</td>
										</logic:equal>
										<logic:notEqual value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">MAX CCPA/CGPA:<br>
										<html:text property="detailMark.totalMarksCGPA" styleId="totalMarksCGPA" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' size="7" maxlength="7" ></html:text><b></b>
										</td>
										</logic:notEqual>
										 <logic:equal value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">Total of CREDIT CCPA(S):<br>
										<html:text property="detailMark.totalCreditCGPA" styleId="totalCreditCGPA" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' size="7" maxlength="7" ></html:text>
										</td>
										</logic:equal>
										<logic:notEqual value="20" property="tempUniversityId" name="onlineApplicationForm">
										<td class="row-odd" colspan="1" align="center">Total of CREDIT CCPA/CGPA:<br>
										<html:text property="detailMark.totalCreditCGPA" styleId="totalCreditCGPA" onblur='<%="checkObtainedCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+");" %>' size="7" maxlength="7" ></html:text>
										</td>
										</logic:notEqual>
									</tr>
								<!-- 	<tr>
									   
										<td class="row-even" colspan="1" align="center"></td>
										<td class="row-even" colspan="1">
									</tr>	
									<tr>	
									   
										<td class="row-even" colspan="1" align="right"></td>
									</tr>	 -->
                   				</table>
                   			</td>
                 		</tr>
                 		<tr class="CBCSS">
                   			<td height="35" colspan="6" class="body" >
								<%
									String resetmethod="resetDetailMarkDegree("+CMSConstants.MAX_CANDIDATE_DEGREE_CBCSS_SUBJECTS+")";
								%>
                				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
                  					<tr>
                    					<td width="45%" height="35" colspan="2">
                    						<div align="right">
                        						<html:button property="" onclick="submitAdmissionFormCalc('submitDetailMarkConfirmDegree')" styleClass="btn" value="Submit"></html:button>
                    						</div>
                    					</td>
                     					<td width="53%">
                    						&nbsp; <html:button property="" value="Clear" styleClass="btn1" onclick='<%=resetmethod %>' /> 
                    						&nbsp; <html:button property=""  styleClass="btn1" value="Cancel" onclick='submitConfirmCancelButton()'></html:button>
                    					</td>
                  					</tr>
                				</table>
                  			</td>
                 		</tr>
                 
                 		<!-- start of pre cbcss marks  -->
                 
                   		<tr class="OTHER">
                   			<td height="35" colspan="6" class="body" >
                   				<table width="100%" cellspacing="1" cellpadding="2">
                     				<tr bgcolor="#FFFFFF">
                       					<td height="10" colspan="22"></td>
                     				</tr>
					 				<tr>
										<td class="row-odd" width="20%"><bean:message key="admissionForm.detailmark.slno.label"/></td>
										<td class="row-odd" width="30%"><div align="center">Subject Name </div></td>
				 						<td height="25" class="row-odd" width="20%"><div align="center"><bean:message key="admissionForm.detailmark.obtain.label"/></div></td>
				 						<td height="25" class="row-odd" width="20%"><div align="center"><bean:message key="knowledgepro.admission.maxMark"/></div></td>
 									</tr>
									<%
										int totalCount=1;
										int mainCount=1;
										int subCount=1;
										commonCount=1;
										openCount=1;
										
										for(i=11;i<=CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS;i++) {
											//String propertyName="detailMark.subject"+i;
											String propertyName="detailMark.subjectid"+i;
											String dynaMandatory="detailMark.subject"+i+"Mandatory";
											String propertyDetailedName="detailMark.detailedSubjects"+i+".id";
											String propertyDetailedId="detailMark.detailedSubjects"+i;
											String totalMarkprop="detailMark.subject"+i+"TotalMarks";
											String dynajs="updatetotalMarkPreCBCSS("+CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS+");"+"checkObtainedPreCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS+");";
											String obtainMarkprop="detailMark.subject"+i+"ObtainedMarks";
											String dynajs2="updateObtainMarkPreCBCSS("+CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS+");"+"checkObtainedPreCbcssMarks("+CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS+");";
											String method = "putSubjectName('"+i+"',this.value)";
											String propertyOtherName="detailMark.subjectOther"+i;
											String degmaxcgpa="detailMark.subject"+i+"Credit";
											String propertyOtherNameDisplay="propertyOtherNameDisplay"+i;
									%>
                     				<tr>
                     					<%if(i<=13) {%>
                         					<td class="row-even"> <%=totalCount %>&nbsp;&nbsp;&nbsp;Core Course/Main  <%=mainCount%></td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>'  styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admCoreMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												mainCount++;
												totalCount++;
					                     	}
											else if(i<=15) {
										%>
											<td class="row-even" > <%=totalCount %>&nbsp;&nbsp;&nbsp;Complimentary/Subsidiary <%=subCount%></td>
											<td class="row-even" >
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admComplMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													 <div id="<%=propertyOtherNameDisplay%>" style="display: block;">
													 	<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													 </div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												subCount++;
												totalCount++;
											}
											else if(i==16) {
										%>
											<td class="row-even"> <%=totalCount %>&nbsp;&nbsp;&nbsp;Part <%=commonCount%> (English)</td>
											<td class="row-even">
												<nested:select disabled="false" property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+")" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admCommonMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												commonCount++;
												totalCount++;
											}
											else if(i==17) {
										%>
											<td class="row-even"> <%=totalCount %>&nbsp;&nbsp;&nbsp;Part <%=commonCount%> (Second Language)</td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admSubMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												commonCount++;
												totalCount++;
											}
											else if(i==18) {
										%>
						 					<td class="row-even"> <%=totalCount %>&nbsp;&nbsp;&nbsp;Open Course <%=openCount%></td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="admMainMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
											 		</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
									 					<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												openCount++;
												totalCount++;
											}
											else if(i==19) {
										%>
											<td class="row-even"> <%=totalCount %>&nbsp;&nbsp;&nbsp;Foundation Course <%=foundationCount%></td>
											<td class="row-even">
												<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' onchange='<%="displayOtherForSubject("+i+");"+"dupsub("+i+");" %>' styleClass="combolarge" style="width:300px">
					                    			<html:option value="">--Select--</html:option>
					                    			<html:optionsCollection property="foundationMap" name="onlineApplicationForm" label="value" value="key"/>
												</nested:select>
												<br/>
												<nested:notEmpty property="<%=propertyOtherName%>" name="onlineApplicationForm">
											 		<div id="<%=propertyOtherNameDisplay%>" style="display: block;">
											 			<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
										 			</div>
												</nested:notEmpty>
												<nested:empty property="<%=propertyOtherName%>" name="onlineApplicationForm">
													<div id="<%=propertyOtherNameDisplay%>" style="display: none;">
								 						<html:text property="<%=propertyOtherName %>" styleId='<%=propertyOtherName %>' name="onlineApplicationForm" size="10" maxlength="20"></html:text>
													</div>
												</nested:empty>
											</td>
											<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=obtainMarkprop %>'></html:text></td>
											<td class="row-even" align="center"><html:text property="<%=totalMarkprop %>" size="6" maxlength="6" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text></td>
										<%
												foundationCount++;
											}
										%>
									</tr>
									<%} %>
									<tr>
										<td class="row-odd" colspan="2" align="right"><div align="right">TOTAL MARKS / TOTAL GRADE POINTS:</div></td>
										<td class="row-even" align="center"><html:text property="detailMark.totalObtainedMarks" size="7" maxlength="7"  styleId="ObtainedMark" readonly="false"></html:text></td>
										<td class="row-even" align="center" ><html:text property="detailMark.totalMarks" styleId="totalMark" size="7" maxlength="7" readonly="false"></html:text></td>
									</tr>
                   				</table>
                  			</td>
                 		</tr>
                 		<tr class="OTHER">
                   			<td height="35" colspan="6" class="body" >
								<%
									String resetmethod1="resetDetailMarkDegree("+CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS+")";
								%>
                				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  					<tr>
                    					<td width="45%" height="35">
                    						<div align="right">
                        						<html:button property="" onclick="submitAdmissionFormCalc('submitDetailMarkConfirmDegree')" styleClass="btn" value="Submit"></html:button>
                    						</div>
                    					</td>
                   						<td width="53%">
                    						&nbsp; <html:button property="" value="Clear" styleClass="btn1" onclick='<%=resetmethod1 %>' /> 
                    						&nbsp; <html:button property=""  styleClass="btn1" value="Cancel" onclick='submitConfirmCancelButton()'></html:button>
                    					</td>
                  					</tr>
                				</table>
                 			</td>
                 		</tr>
                 		<tr>
                   			<td height="10" colspan="6" class="body" ></td>
                 		</tr>
              		</table>
            	</td>
  			</tr>
		</table>
	</body>
	<script type="text/javascript">
		var other='<%= CMSConstants.MAX_CANDIDATE_DEGREE_PRECBCSS_SUBJECTS %>';
		for(var k=1;k<=other;k++) {
			var propertyName="detailMark.subjectid"+k;
			var propertyOtherName="detailMark.subjectOther"+k;
			var propertyOtherNameDisplay=""+propertyOtherNameDisplay+k;
			if(document.getElementById("detailMark.subjectid"+k)!= null) {
				if((document.getElementById("detailMark.subjectid"+k).value=="0") || (document.getElementById("detailMark.subjectid"+k).value=="0") || (document.getElementById("detailMark.subjectid"+k).value=="0")) {
					document.getElementById("propertyOtherNameDisplay"+k).style.display = "block";
				}
			}
		}
	</script>
</html:html>
