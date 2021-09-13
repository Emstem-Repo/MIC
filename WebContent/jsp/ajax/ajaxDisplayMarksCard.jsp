<%@ page contentType="text/xml;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<response>
<c:if test="${msg==null}">
	<c:if test="${MarksCard!=null}">
			<logic:notEmpty name="MarksCard" property="subMap">
			<logic:iterate id="StudentMarksTo" name="MarksCard" property="subMap">
			<fields>
				<value> <bean:write name="StudentMarksTo" property="key"/> </value>
					<logic:iterate id="to" name="StudentMarksTo" property="value" indexId="count">
				<totalMark>
						<theory><bean:write name="to" property="theory"/></theory>
						<practical><bean:write name="to" property="practical"/></practical>
						<siNo> <c:out value="${count + 1}" /></siNo> 
						<subject><bean:write name="to" property="name"/></subject>
						<type><bean:write name="to" property="type"/></type>
						<cia><logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal>
							 <logic:notEqual value="-" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded" /> </logic:notEqual> 
							 <logic:empty name="to" property="ciaMarksAwarded">  </logic:empty>
						</cia>
						<ese><logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal>
							<logic:notEqual value="-" name="to" property="eseMaxMarks"> <bean:write name="to" property="eseMarksAwarded" /> </logic:notEqual>
							 <logic:empty name="to" property="eseMarksAwarded"> </logic:empty>
						</ese>
						<total><bean:write name="to" property="totalMarksAwarded"/></total>
						<credits><bean:write name="to" property="credits"/></credits>
						<grade><logic:equal value="-" name="to" property="grade">-</logic:equal>
							<logic:notEqual value="-" name="to" property="grade"> <bean:write name="to" property="grade" /> </logic:notEqual>
							 <logic:empty name="to" property="grade"> </logic:empty>
						</grade>
						<subjectCode>
							<bean:write name="to" property="code"/>
						</subjectCode>
						<gradePoints>
							<logic:equal value="-" name="to" property="theoryGradePnt">-</logic:equal>
							<logic:notEqual value="-" name="to" property="theoryGradePnt"> <bean:write name="to" property="theoryGradePnt" /> </logic:notEqual>
							<logic:empty name="to" property="theoryGradePnt"> </logic:empty>
						</gradePoints>
						<creditPoints>
							<bean:write name="to" property="creditPoint"/>
						</creditPoints>
						<ciaMaxMarks>
							<bean:write name="to" property="ciaMaxMarks"/>
						</ciaMaxMarks>
						<eseMaxMarks>
							<bean:write name="to" property="eseMaxMarks"/>
						</eseMaxMarks>
						<totalMaxMarks>
							<bean:write name="to" property="totalMaxMarks"/>
						</totalMaxMarks>
						<practicalCiaMarksAwarded>
							<bean:write name="to" property="practicalCiaMarksAwarded"/>
						</practicalCiaMarksAwarded>					
						<practicalEseMarksAwarded>
							<bean:write name="to" property="practicalEseMarksAwarded"/>
						</practicalEseMarksAwarded>
						<practicalTotalMarksAwarded>
							<bean:write name="to" property="practicalTotalMarksAwarded"/>
						</practicalTotalMarksAwarded>
						<practicalTotalMaxMarks>
							<bean:write name="to" property="practicalTotalMaxMarks"/>
						</practicalTotalMaxMarks>
						<practicalGradePnt>
							<logic:equal value="-" name="to" property="practicalGradePnt">-</logic:equal>
							<logic:notEqual value="-" name="to" property="practicalGradePnt"> <bean:write name="to" property="practicalGradePnt" /> </logic:notEqual>
							<logic:empty name="to" property="practicalGradePnt"> </logic:empty>
						</practicalGradePnt>
						<practicalGrade><logic:equal value="-" name="to" property="practicalGrade">-</logic:equal>
							<logic:notEqual value="-" name="to" property="practicalGrade"> <bean:write name="to" property="practicalGrade" /> </logic:notEqual>
							 <logic:empty name="to" property="practicalGrade"> </logic:empty>
						</practicalGrade>												
					</totalMark>
					</logic:iterate>
					
				</fields>
			</logic:iterate>
			</logic:notEmpty>
			<logic:notEmpty name="MarksCard" property="addOnCoursesubMap">
			<logic:iterate id="StudentMarksTo" name="MarksCard" property="addOnCoursesubMap">
			<fields>
				<value> <bean:write name="StudentMarksTo" property="key"/> </value>
					<logic:iterate id="to" name="StudentMarksTo" property="value" indexId="count">
				<totalMark>
						<siNo> <c:out value="${count + 1}" /></siNo>
						<subject><bean:write name="to" property="name"/></subject>
						<type><bean:write name="to" property="type"/></type>
						<cia><logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal>
							 <logic:notEqual value="-" name="to" property="ciaMaxMarks">   <bean:write name="to" property="ciaMarksAwarded" /> </logic:notEqual> 
							 <logic:empty name="to" property="ciaMarksAwarded">   </logic:empty>
						</cia>
						<ese><logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal>
							<logic:notEqual value="-" name="to" property="eseMaxMarks">   <bean:write name="to" property="eseMarksAwarded" /> </logic:notEqual>
							 <logic:empty name="to" property="eseMarksAwarded">    </logic:empty>
						</ese>
						<total><bean:write name="to" property="totalMarksAwarded"/></total>
						<credits><bean:write name="to" property="credits"/></credits>
						<grade><logic:equal value="-" name="to" property="grade">-</logic:equal>
							<logic:notEqual value="-" name="to" property="grade"> <bean:write name="to" property="grade"/> </logic:notEqual>
							 <logic:empty name="to" property="grade"> </logic:empty>
						</grade>
						<subjectCode>
							<bean:write name="to" property="code"/>
						</subjectCode>
						<gradePoints>
							<bean:write name="to" property="theoryGradePnt"/>
						</gradePoints>
						<creditPoints>
							<bean:write name="to" property="creditPoint"/>
						</creditPoints>		
						<ciaMaxMarks>
							<bean:write name="to" property="ciaMaxMarks"/>
						</ciaMaxMarks>
						<eseMaxMarks>
							<bean:write name="to" property="eseMaxMarks"/>
						</eseMaxMarks>
						<totalMaxMarks>
							<bean:write name="to" property="totalMaxMarks"/>
						</totalMaxMarks>
						<practicalTotalMarksAwarded>
							<bean:write name="to" property="practicalTotalMarksAwarded"/>
						</practicalTotalMarksAwarded>	
						<practicalTotalMaxMarks>
							<bean:write name="to" property="practicalTotalMaxMarks"/>
						</practicalTotalMaxMarks>								
					</totalMark>
					</logic:iterate>
					
				</fields>
			</logic:iterate>
			</logic:notEmpty>
			<result> <logic:equal value="-" name="MarksCard" property="result">-</logic:equal>
							<logic:notEqual value="-" name="MarksCard" property="result"> <bean:write name="MarksCard" property="result" /> </logic:notEqual>
							 <logic:empty name="MarksCard" property="result"> </logic:empty> </result>
			<resultClass>
				<bean:write name="MarksCard" property="resultClass" />
			</resultClass>
			
			<totalGradePoints>
				<bean:write name="MarksCard" property="totalGradePoints" />
			</totalGradePoints>
			
			<totalGrade>
				<bean:write name="MarksCard" property="totalGrade" />
			</totalGrade>
			
			<scpa>
				<bean:write name="MarksCard" property="sgpa" />
			</scpa>
			
			<totalCredits>
				<bean:write name="MarksCard" property="totalCredits" />
			</totalCredits>
			
			<practicalCIATotalMarks>
				<bean:write name="MarksCard" property="practicalCIATotalMarks"/>
			</practicalCIATotalMarks>
			
			<practicalESATotalMarks>
				<bean:write name="MarksCard" property="practicalESATotalMarks"/>
			</practicalESATotalMarks>
			
			<practicalTotalMarks>
				<bean:write name="MarksCard" property="practicalTotalMarks"/>
			</practicalTotalMarks>
			
			<hasPracticals>
				<bean:write name="MarksCard" property="hasPracticals"/>
			</hasPracticals>
			
			<grandTotalCIAMarks>
				<bean:write name="MarksCard" property="grandTotalCIAMarks"/>
			</grandTotalCIAMarks>
			
			<grandTotalESAMarks>
				<bean:write name="MarksCard" property="grandTotalESAMarks"/>
			</grandTotalESAMarks>
			
			<totalMarksAwarded>
				<bean:write name="MarksCard" property="totalMarksAwarded"/>
			</totalMarksAwarded>
			
			<maxCIAMarks>
				<bean:write name="MarksCard" property="maxCIAMarks"/>
			</maxCIAMarks>
			
			<maxESEMarks>
				<bean:write name="MarksCard" property="maxESEMarks"/>
			</maxESEMarks>
			
			<maxMarksPractical>
				<bean:write name="MarksCard" property="maxMarksPractical"/>
			</maxMarksPractical>
			
			<maxTotalMarks>
				<bean:write name="MarksCard" property="maxTotalMarks"/>
			</maxTotalMarks>
			
			<result>
				<bean:write name="MarksCard" property="result"/>
			</result>
			
			<dontShowPracticals>
				<bean:write name="MarksCard" property="dontShowPracticals"/>
			</dontShowPracticals>			
		
	</c:if>
	</c:if>
	<c:if test="${msg!=null}">
		<msg><bean:write name="msg"/> </msg>
	</c:if>
</response>