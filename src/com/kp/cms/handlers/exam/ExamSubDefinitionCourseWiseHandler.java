package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.ExamSubCoursewiseAttendanceMarksBO;
import com.kp.cms.bo.exam.ExamSubCoursewiseGradeDefnBO;
import com.kp.cms.bo.exam.ExamSubDefinitionCourseWiseBO;
import com.kp.cms.bo.exam.ExamSubjectSectionMasterBO;
import com.kp.cms.forms.exam.ExamSubjectDefCourseForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.helpers.exam.ExamSubDefinitionCourseWiseHelper;
import com.kp.cms.to.exam.ExamSubCoursewiseAttendanceMarksTO;
import com.kp.cms.to.exam.ExamSubCoursewiseGradeDefnTO;
import com.kp.cms.to.exam.ExamSubDefinitionCourseUpdateTO;
import com.kp.cms.to.exam.ExamSubDefinitionCourseWiseDisplayTO;
import com.kp.cms.to.exam.ExamSubDefinitionCourseWiseTO;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.transactionsimpl.exam.ExamSubDefinitionCourseWiseImpl;
import com.kp.cms.utilities.CommonUtil;

public class ExamSubDefinitionCourseWiseHandler extends ExamGenHandler {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory
			.getLog(ExamSubDefinitionCourseWiseHandler.class);
	ExamSubDefinitionCourseWiseImpl impl = new ExamSubDefinitionCourseWiseImpl();
	ExamSubDefinitionCourseWiseHelper helper = new ExamSubDefinitionCourseWiseHelper();

	@SuppressWarnings("unchecked")
	public List<ExamSubDefinitionCourseWiseTO> init() throws Exception {
		ArrayList<ExamSubDefinitionCourseWiseBO> listBO = new ArrayList(impl
				.select_ActiveOnly(ExamSubDefinitionCourseWiseBO.class));
		List<ExamSubDefinitionCourseWiseTO> list = helper.convertBOToTO(listBO);
		return list;

	}

	public void reactivate(int id, String userId) throws Exception {
		impl.reActivate_IExamGenBO(id, userId,
				ExamSubDefinitionCourseWiseBO.class);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ExamSubDefinitionCourseWiseTO> getSubj_course_scheme_year(
			int courseId, int shemeId, int schemeNo, int academicYear) {
		ArrayList<ExamSubDefinitionCourseWiseBO> listBO = new ArrayList(impl
				.select_course_scheme_year(courseId, shemeId, schemeNo,
						academicYear));
		return helper.convertBOToTO(listBO);
	}

	public ExamSubDefinitionCourseWiseTO createFormObjcet(
			ExamSubjectDefCourseForm form) throws Exception {

		return helper.createBOToTO(impl.select_subcode_subname(Integer
				.parseInt(form.getSubId())));
	}

	@SuppressWarnings("unchecked")
	public ArrayList<KeyValueTO> getSubjectSectioneList() {
		List<ExamSubjectSectionMasterBO> listBO = new ArrayList(impl
				.select_ActiveOnly(ExamSubjectSectionMasterBO.class));
		return helper.convertBOToTO_SubjectSection_List(listBO);

	}

	// to display all course details
	@SuppressWarnings("unchecked")
	public ArrayList<ExamSubCoursewiseGradeDefnTO> displayGradeDefinition(
			String subject_Id, String course_Id) throws Exception {
		int subjectId = 0;
		if (subject_Id != null && subject_Id.trim().length() != 0) {
			subjectId = Integer.parseInt(subject_Id);
		}
		int courseId = 0;
		if (course_Id != null && course_Id.trim().length() != 0) {
			courseId = Integer.parseInt(course_Id);
		}
		ArrayList<ExamSubCoursewiseGradeDefnBO> listBO = new ArrayList(impl
				.select_All_grade_details(subjectId, courseId));

		return helper.convertBOToTO_GradeDetails(listBO);

	}

	@SuppressWarnings("unchecked")
	public ArrayList<ExamSubCoursewiseAttendanceMarksTO> displayAttendanceDetails(
			String subject_Id, String course_Id) throws Exception {
		int subjectId = 0;
		if (subject_Id != null && subject_Id.trim().length() != 0) {
			subjectId = Integer.parseInt(subject_Id);
		}
		int courseId = 0;
		if (course_Id != null && course_Id.trim().length() != 0) {
			courseId = Integer.parseInt(course_Id);
		}
		ArrayList<ExamSubCoursewiseAttendanceMarksBO> listBO = new ArrayList(
				impl.select_All_attendance_details(subjectId, courseId));
		return helper.convertBOToTOAttendance(listBO);

	}

	public void addGDAdd(List<Integer> listSubjectId, String startPercentage,
			String endPercentage, String grade, String interpretation,
			String resultClass, String gradePoint, String userId, String mode,
			String course_Id) throws Exception {
		BigDecimal startPercBD = new BigDecimal(startPercentage);
		BigDecimal endPercBD = new BigDecimal(endPercentage);
		BigDecimal gradePointBD = new BigDecimal(gradePoint);
		ExamSubCoursewiseGradeDefnBO objBO = null;
		int courseId = 0;
		if (course_Id != null && course_Id.trim().length() != 0) {
			courseId = Integer.parseInt(course_Id);
		}
		for (Integer i : listSubjectId) {

			objBO = new ExamSubCoursewiseGradeDefnBO(i.intValue(), startPercBD,
					endPercBD, grade, interpretation, resultClass,
					gradePointBD, courseId);

			if (!(impl.isDuplicated(0, i.intValue(), startPercBD, endPercBD,
					grade, mode, courseId)))

			{

				impl.insert(objBO);
			}

		}

	}

	public ArrayList<ExamSubCoursewiseGradeDefnTO> select(
			ArrayList<Integer> listSubId) {
		return helper.convertBOToTO(impl.select_GradeDefinition(listSubId));

	}

	public ExamSubjectDefCourseForm getUpdatableForm(
			ExamSubjectDefCourseForm objform, String mode) throws Exception {
		objform = helper.createFormObjcet(objform, impl
				.loadExamSubCoursewiseGradeDefnBO(Integer.parseInt(objform
						.getId())));

		return objform;
	}

	public void delete_SubjectCourseWiseGradeDef(int subDefId) throws Exception {
		impl.delete_SubDefCoursewiseEntryDetails(subDefId);
	}

	// ----------define attendance-----------------
	public ExamSubjectDefCourseForm getAttendanceUpdatableForm(
			ExamSubjectDefCourseForm form, String mode) throws Exception {

		form = helper.createFormObjcetAttendance(form, impl
				.loadExamSubCoursewiseAttendanceMarksBO(Integer.parseInt(form
						.getId())));

		return form;
	}

	public void updateAttendance(int id, int subjectID, String startPercentage,
			String endPercentage, String marks, boolean update, String course_Id)
			throws Exception {
		BigDecimal startPercBD = new BigDecimal(startPercentage);
		BigDecimal endPercBD = new BigDecimal(endPercentage);
		BigDecimal attendanceMarks = new BigDecimal(marks);

		int courseId = 0;
		if (course_Id.trim().length() != 0 && course_Id != null) {
			courseId = Integer.parseInt(course_Id);
		}
		String mode = "update";

		if (update) {
			impl.updateAttendance(id, subjectID, startPercBD, endPercBD,
					attendanceMarks);
		}

		else if (!(impl.isDuplicatedAttendance(id, subjectID, startPercBD,
				endPercBD, attendanceMarks, mode, courseId)))

		{
			impl.updateAttendance(id, subjectID, startPercBD, endPercBD,
					attendanceMarks);
		}

	}

	public void deleteAttendanceMarks(int id, String userId) {
		impl.delete_ExamSubCoursewiseAttendanceMarksBO(id);

	}

	public void addAttendance(List<Integer> subId, String startPercentage,
			String endPercentage, String marks, String userId, String mode,
			String course_Id) throws Exception {

		BigDecimal startPercBD = null;
		BigDecimal endPercBD = null;
		BigDecimal marksBD = null;
		int courseId = 0;
		if (course_Id != null && course_Id.trim().length() != 0) {
			courseId = Integer.parseInt(course_Id);
		}
		if (startPercentage.trim().length() != 0) {
			startPercBD = new BigDecimal(startPercentage);
		}
		if (endPercentage.trim().length() != 0) {
			endPercBD = new BigDecimal(endPercentage);
		}
		if (marks.trim().length() != 0) {
			marksBD = new BigDecimal(marks);
		}

		for (Integer i : subId) {

			 ExamSubCoursewiseAttendanceMarksBO objBO = new ExamSubCoursewiseAttendanceMarksBO(i.intValue(),
					marksBD, startPercBD, endPercBD, courseId);

			if (!(impl.isDuplicatedAttendance(0, i.intValue(), startPercBD,
					endPercBD, marksBD, mode, courseId)))

			{
				impl.insert(objBO);
			}

		}
	}

	public void addSubDefCourseDetails(ExamSubjectDefCourseForm objform, int schemeNo)
			throws Exception {
		ExamSubDefinitionCourseWiseBO objBO = null;

		if (objform != null) {
			int subjectOrder = 0;
			int courseId = 0;
			int theoryHours = 0;
			int theoryCredit = 0;
			int practicalHours = 0;
			int practicalCredit = 0;
			int isActiveGradeDefn = 0;
			int isActiveAttndnc = 0;
			int academicYear = 0;
			objform = checkNull(objform);
			String userId = objform.getUserId();
			String universitySubjectCode = objform.getUniversitySubjectCode();
			int dontShowSubType = (objform.getSubjectType().equals("on") ? 1
					: 0);
			int dontShowMaxMarks = (objform.getMaxMarks().equals("on") ? 1 : 0);
			int dontShowMinMarks = (objform.getMinMarks().equals("on") ? 1 : 0);
			int dontShowAttMarks = (objform.getAttendanceMarks().equals("on") ? 1
					: 0);
			int dontAddTotMarkClsDecln = (objform.getDoNotAdd().equals("on") ? 1
					: 0);
			int dontAddInGroupTotal = (objform.getDoNotAddGroup().equals("on") ? 1
					: 0);
			int dontConsiderFailureTotalResult = (objform.getDoNotConsider()
					.equals("on") ? 1 : 0);
			int showInternalFinalMarkAdded = (objform.getShowInternalMarks()
					.equals("on") ? 1 : 0);
			int showOnlyGrade = (objform.getShowOnlyGrade().equals("on") ? 1
					: 0);
			
			int showOnlyCredits = (objform.getShowOnlyCredits().equals("on") ? 1
					: 0);

			int subjectId = Integer.parseInt(objform.getSubId());

			if (objform.getSubjectOrder() != null
					&& objform.getSubjectOrder().length() != 0) {
				subjectOrder = Integer.parseInt(objform.getSubjectOrder());

			}

			int subjectSectionId = Integer
					.parseInt(objform.getSubjectSection());

			if (objform.getPractCredit() != null
					&& objform.getPractCredit().length() != 0) {
				practicalCredit = Integer.parseInt(objform.getPractCredit());

			}
			if (objform.getTheoryHours() != null
					&& objform.getTheoryHours().length() != 0) {
				theoryHours = Integer.parseInt(objform.getTheoryHours());

			}
			if (objform.getTheoryCredit() != null
					&& objform.getTheoryCredit().length() != 0) {
				theoryCredit = Integer.parseInt(objform.getTheoryCredit());

			}
			if (objform.getPractHours() != null
					&& objform.getPractHours().length() != 0) {
				practicalHours = Integer.parseInt(objform.getPractHours());

			}

			if (objform.getCourseId() != null
					&& objform.getCourseId().length() != 0) {
				courseId = Integer.parseInt(objform.getCourseId());

			}
			if(objform.getAcademicYear()!=null && objform.getAcademicYear().length() !=0){
				academicYear = Integer.parseInt(objform.getAcademicYear());
			}
			if (impl.checkCommonSubject(subjectId, courseId,academicYear))
				impl.duplicateOrderCheck(subjectOrder, subjectId, courseId, schemeNo,academicYear);

			if (theoryHours == 0 && theoryCredit == 0) {

				objBO = new ExamSubDefinitionCourseWiseBO(subjectId,
						subjectOrder, universitySubjectCode, subjectSectionId,
						theoryHours, theoryCredit, practicalHours,
						practicalCredit, dontShowSubType, dontShowMaxMarks,
						dontShowMinMarks, dontShowAttMarks,
						dontAddTotMarkClsDecln, dontConsiderFailureTotalResult,
						showInternalFinalMarkAdded, showOnlyGrade,
						isActiveGradeDefn, isActiveAttndnc, userId, courseId, schemeNo,academicYear,dontAddInGroupTotal,showOnlyCredits);

			} else if (practicalHours == 0 && practicalCredit == 0) {
				// if theory
				objBO = new ExamSubDefinitionCourseWiseBO(subjectId,
						subjectOrder, universitySubjectCode, subjectSectionId,
						theoryHours, theoryCredit, dontShowSubType,
						dontShowMaxMarks, dontShowMinMarks, dontShowAttMarks,
						dontAddTotMarkClsDecln, dontConsiderFailureTotalResult,
						showInternalFinalMarkAdded, showOnlyGrade,
						isActiveGradeDefn, isActiveAttndnc, userId, courseId, schemeNo,academicYear,dontAddInGroupTotal,showOnlyCredits);
			} else {
				// if both
				objBO = new ExamSubDefinitionCourseWiseBO(subjectId,
						subjectOrder, universitySubjectCode, subjectSectionId,
						theoryHours, theoryCredit, practicalHours,
						practicalCredit, dontShowSubType, dontShowMaxMarks,
						dontShowMinMarks, dontShowAttMarks,
						dontAddTotMarkClsDecln, dontConsiderFailureTotalResult,
						showInternalFinalMarkAdded, showOnlyGrade,
						isActiveGradeDefn, isActiveAttndnc, userId, courseId, schemeNo,academicYear,dontAddInGroupTotal,showOnlyCredits);

			}
			if (impl.isPresentDetails(subjectId, courseId,schemeNo,academicYear) > 0) {
				int id = Integer.parseInt(objform.getId());
				objBO = new ExamSubDefinitionCourseWiseBO(id, subjectId,
						subjectOrder, universitySubjectCode, subjectSectionId,
						theoryHours, theoryCredit, practicalHours,
						practicalCredit, dontShowSubType, dontShowMaxMarks,
						dontShowMinMarks, dontShowAttMarks,
						dontAddTotMarkClsDecln, dontConsiderFailureTotalResult,
						showInternalFinalMarkAdded, showOnlyGrade,
						isActiveGradeDefn, isActiveAttndnc, userId, courseId, schemeNo,academicYear,dontAddInGroupTotal,showOnlyCredits);

				impl.update(objBO);

			} else if (impl.checkOptionalAndSecondLang(subjectId)) {

				impl.insert(objBO);
			} else if (!impl.isDuplicatedSubject(subjectId, subjectOrder,
					courseId, schemeNo,academicYear)) {
				impl.insert(objBO);
			}

		}

	}

	private ExamSubjectDefCourseForm checkNull(ExamSubjectDefCourseForm objform) {
		if (objform.getSubjectType() == null) {
			objform.setSubjectType("0");
		}
		if (objform.getMaxMarks() == null) {
			objform.setMaxMarks("0");
		}
		if (objform.getMinMarks() == null) {
			objform.setMinMarks("0");
		}
		if (objform.getAttendanceMarks() == null) {
			objform.setAttendanceMarks("0");
		}
		if (objform.getDoNotAdd() == null) {
			objform.setDoNotAdd("0");
		}
		if (objform.getDoNotAddGroup() == null) {
			objform.setDoNotAddGroup("0");
		}
		if (objform.getDoNotConsider() == null) {
			objform.setDoNotConsider("0");
		}
		if (objform.getShowInternalMarks() == null) {
			objform.setShowInternalMarks("0");
		}
		if (objform.getShowOnlyGrade() == null) {
			objform.setShowOnlyGrade("0");
		}

		if (objform.getTheoryHours() == null
				|| objform.getTheoryHours().length() == 0) {
			objform.setTheoryHours("0");

		}
		if (objform.getTheoryCredit() == null
				|| objform.getTheoryCredit().length() == 0) {
			objform.setTheoryCredit("0");
		}
		if (objform.getPractCredit() == null
				|| objform.getPractCredit().length() == 0) {
			objform.setPractCredit("0");
		}
		if (objform.getPractHours() == null
				|| objform.getPractHours().length() == 0) {
			objform.setPractHours("0");
		}
		if(objform.getShowOnlyCredits()==null)
			objform.setShowOnlyCredits("0");

		return objform;
	}

	// fetch details for a subject

	public List<ExamSubDefinitionCourseWiseDisplayTO> getSubjectDetailsFromSubjectId(
			int subjectId, ExamSubjectDefCourseForm objform) {
		List<ExamSubDefinitionCourseWiseBO> listBO = null;

		listBO = impl.getSubjectDetailsFromSubjectId(subjectId, Integer
				.parseInt(objform.getCourseId()),Integer.parseInt(objform.getSchemeNo()),Integer.parseInt(objform.getAcademicYear()));

		List<ExamSubDefinitionCourseWiseDisplayTO> listTO = helper
				.convertBOToDisplayTO(listBO);
		setToform(objform, listTO);

		return listTO;
	}

	private void setToform(ExamSubjectDefCourseForm objform,
			List<ExamSubDefinitionCourseWiseDisplayTO> listTO) {
		objform.resetFields();
		for (ExamSubDefinitionCourseWiseDisplayTO to : listTO) {
			objform.setDupdoNotAdd(to.getDontAddTotMarkClsDecln());
			//basim
			objform.setDupdoNotAddGroup(to.getDontAddInGroupTotal());
			objform.setDupdoNotConsider(to.getDontConsiderFailureTotalResult());
			objform.setDupshowOnlyGrade(to.getShowOnlyGrade());
			objform.setDupshowInternalMarks(to.getShowInternalFinalMarkAdded());
			objform.setDupsubjectType(to.getDontShowSubType());
			objform.setDupmaxMarks(to.getDontShowMaxMarks());
			objform.setDupminMarks(to.getDontShowMinMarks());
			objform.setDupattendanceMarks(to.getDontShowAttMarks());
			objform.setSubjectCode(to.getSubjectCode());
			objform.setSubId(Integer.toString(to.getSubjectId()));
			objform.setSubjectSection(Integer
					.toString(to.getSubjectSectionId()));

			objform.setSubjectOrder(Integer.toString(to.getSubjectOrder()));
			objform.setUniversitySubjectCode(to.getUniversitySubjectCode());
			objform.setTheoryCredit(Integer.toString(to.getTheoryCredit()));
			objform.setTheoryHours(Integer.toString(to.getTheoryHours()));
			objform.setPractCredit(Integer.toString(to.getPracticalCredit()));
			objform.setPractHours(Integer.toString(to.getPracticalHours()));
			objform.setId(Integer.toString(to.getId()));
			objform.setDupShowOnlyCredits(to.getShowOnlyCredits());

		}

	}

	public String getacademicYear(int academicYear) {

		String academicYearName = null;
		if (academicYear != 0) {
			academicYearName = Integer.toString(academicYear).concat("-")
					+ Integer.toString(academicYear + 1);
		}
		return academicYearName;
	}

	public void update(ExamSubDefinitionCourseUpdateTO updateTO, String mode,
			boolean update) throws Exception {
		BigDecimal startPercBD = new BigDecimal(updateTO.getStartPercentage()
				.trim());
		BigDecimal endPercBD = new BigDecimal(updateTO.getEndPercentage()
				.trim());
		BigDecimal gradePointBD = new BigDecimal(updateTO.getGradePoint()
				.trim());

		BigDecimal OrgStartPercBD = new BigDecimal(updateTO
				.getOrgStartPercentage());
		BigDecimal OrgEndPercBD = new BigDecimal(updateTO.getOrgEndPercentage());

		int subId = updateTO.getSubjectId();
		int id = updateTO.getId();
		int courseId = updateTO.getCourseId();

		// isDuplicatedGradeForCourse

		if ((startPercBD.doubleValue() == OrgStartPercBD.doubleValue())
				&& (endPercBD.doubleValue() == OrgEndPercBD.doubleValue())) {

			if (updateTO.getGrade().equalsIgnoreCase(updateTO.getOrgGrade())) {

				impl.update(id, subId, startPercBD, endPercBD, updateTO
						.getGrade(), updateTO.getInterpretation(), updateTO
						.getResultClass(), gradePointBD, updateTO.getUserId());
			} else if (!(impl.isDuplicatedGradeForSubject(id, subId,
					startPercBD, endPercBD, updateTO.getGrade(), mode)))

			{

				impl.update(id, subId, startPercBD, endPercBD, updateTO
						.getGrade(), updateTO.getInterpretation(), updateTO
						.getResultClass(), gradePointBD, updateTO.getUserId());
			}

		}

		else if (!(impl.isDuplicated(id, subId, startPercBD, endPercBD,
				updateTO.getGrade(), mode, courseId)))

		{

			impl.update(id, subId, startPercBD, endPercBD, updateTO.getGrade(),
					updateTO.getInterpretation(), updateTO.getResultClass(),
					gradePointBD, updateTO.getUserId());
		}

	}

	public String checkIfTheoryOrPractical(int subjectId) {

		return impl.checkIfTheoryOrPractical(subjectId);
	}

	public ExamSubjectDefCourseForm retainAllValues(
			ExamSubjectDefCourseForm objform,HttpServletRequest req) {

		if (CommonUtil.checkForEmpty(objform.getCourse())) {
//			objform.setCourse(objform.getCourse());
			objform.setSchemeMapList(getSchemeNo_SchemeIDByCourseIdAcademicId(
					objform.getCourse(), objform.getAcademicYear()));
//			objform.setScheme(objform.getScheme());
		}
		
		if(CommonUtil.checkForEmpty(objform.getProgramId())){
			Map<Integer, String> courseMap = CommonAjaxHandler.getInstance().getCourseByProgramInOrder(Integer.parseInt(objform.getProgramId()));
			req.setAttribute("courseMap", courseMap);
		}

		return objform;
	}

}
