package com.kp.cms.helpers.exam;

/**
 * Mar 2, 2010
 * Created By 9Elements Team
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kp.cms.bo.exam.ExamInternalRetestApplicationBO;
import com.kp.cms.bo.exam.ExamInternalRetestApplicationSubjectsBO;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.to.exam.ExamInternalRetestApplicationSubjectsTO;
import com.kp.cms.to.exam.ExamInternalRetestApplicationTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationTO;
import com.kp.cms.transactionsimpl.exam.ExamInternalRetestApplicationImpl;
import com.kp.cms.utilities.CommonUtil;

@SuppressWarnings("unchecked")
public class ExamInternalRetestApplicationHelper extends ExamGenHelper {

	// On SEARCH to get the student details

	public ArrayList<ExamSupplementaryImpApplicationTO> converBoToTO_Student_GetDetails(
			List<Object[]> list) throws BusinessException {

		ExamSupplementaryImpApplicationTO to;
		ArrayList<ExamSupplementaryImpApplicationTO> retutnList = new ArrayList<ExamSupplementaryImpApplicationTO>();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {

			Object[] row = (Object[]) itr.next();
			to = new ExamSupplementaryImpApplicationTO();
			to.setClassName((String) row[0]);
			to.setRegNumber((String) row[1]);
			to.setRollNumber((String) row[2]);
			to.setStudentName((String) row[3]);
			to.setId((Integer) row[4]);
			retutnList.add(to);
		}
		Collections.sort(retutnList);
		return retutnList;
	}

	// ADD - to get the subjects for a particular student
	public ExamInternalRetestApplicationTO convertBOToTO_details(
			List<Object[]> subjectDetails, List<Object[]> examDetails,
			String edit) throws BusinessException {

		ExamInternalRetestApplicationTO to = new ExamInternalRetestApplicationTO();
		ArrayList<ExamInternalRetestApplicationSubjectsTO> listSubject = new ArrayList<ExamInternalRetestApplicationSubjectsTO>();
		ExamInternalRetestApplicationSubjectsTO iTO;
		int k = 0;
		Iterator itr = examDetails.iterator();
		while (itr.hasNext()) {
			Object[] row = (Object[]) itr.next();
			if (row[0] == null) {
				k = 1;
			} else {
				if (edit.equalsIgnoreCase("edit")) {
					k = Integer.parseInt(row[0].toString());
				} else {
					k = Integer.parseInt(row[0].toString()) + 1;
				}
			}
			to.setChance(k);
			to.setClassName((String) row[1]);
			to.setStudentName((String) row[2]);
			to.setRegNumber((String) row[3]);
			to.setRollNumber((String) row[4]);
			to.setStudentId((Integer) row[5]);
			to.setId((Integer) row[6]);
			to.setClassId((Integer) row[7]);

		}

		Iterator itr1 = subjectDetails.iterator();
		while (itr1.hasNext()) {
			Integer theory = null;
			Integer practical = null;
			Object[] row = (Object[]) itr1.next();
			iTO = new ExamInternalRetestApplicationSubjectsTO();
			iTO.setSubjectCode((String) row[0]);
			iTO.setSubjectName((String) row[1]);
			iTO.setFees((String) row[2]);
			if (row[3] != null) {
				theory = Integer.parseInt(row[3].toString());
				if (theory == 1) {
					iTO.setIsCheckedDummy(true);

				} else {
					iTO.setIsCheckedDummy(false);

				}
			}
			if (row[4] != null) {
				practical = Integer.parseInt(row[4].toString());
				if (practical == 1) {
					iTO.setIsCheckedDummyPractical(true);

				} else {
					iTO.setIsCheckedDummyPractical(false);

				}
			}

			iTO.setSubjectId(row[5].toString());
			listSubject.add(iTO);

		}

		to.setSubjectList(listSubject);
		//Collections.sort(to,new ExamInternalRetestApplicationSubjectsTOComparator());
		return to;
	}

	// To get class name based on examId & roll/register no
	public HashMap<Integer, String> convertTOToMapClass(
			List classByExamNameRegNoOnly){

		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Iterator itr = classByExamNameRegNoOnly.iterator();
		while (itr.hasNext()) {
			Object[] row = (Object[]) itr.next();

			map.put(Integer.parseInt(row[0].toString()), row[1].toString());
		}
		map = (HashMap<Integer, String>) CommonUtil.sortMapByValue(map);
		return map;
	}
	
	public List<ExamInternalRetestApplicationTO> convertBOToTO_details( List<Object[]> examDetails,int examId,
			String edit) throws BusinessException {

		ExamInternalRetestApplicationImpl impl = new ExamInternalRetestApplicationImpl();
		List<ExamInternalRetestApplicationTO> toList =new ArrayList<ExamInternalRetestApplicationTO>();
		
		ExamInternalRetestApplicationSubjectsTO iTO;
		int k = 0;
		Iterator itr = examDetails.iterator();
		while (itr.hasNext()) {
			Object[] row = (Object[]) itr.next();
			ExamInternalRetestApplicationTO to = new ExamInternalRetestApplicationTO();
			int id=impl.getFromRetestAppliication((Integer) row[4]);
			if (id!=0) {
				to.setConatinDat(true);
				to.setAdded(true);
			}
			to.setClassName((String) row[0]);
			to.setStudentName((String) row[1]);
			to.setRegNumber((String) row[2]);
			to.setRollNumber((String) row[3]);
			to.setStudentId((Integer) row[4]);
			to.setClassId((Integer) row[5]);
			List<Object[]> subjectDetails = impl.get_subjectsListFor(examId, (Integer) row[4],
					to.getRollNumber());
			if (!subjectDetails.isEmpty()) {
				System.out.println("yes");
			}
			Iterator itr1 = subjectDetails.iterator();
			ArrayList<ExamInternalRetestApplicationSubjectsTO> listSubject = new ArrayList<ExamInternalRetestApplicationSubjectsTO>();
			while (itr1.hasNext()) {
				String theory = null;
				String practical = null;
				Object[] sub = (Object[]) itr1.next();
				iTO = new ExamInternalRetestApplicationSubjectsTO();
				iTO.setSubjectCode((String) sub[2]);
				iTO.setSubjectName((String) sub[1]);
				iTO.setFees("");
				if (sub[8] != null) {
					theory = String.valueOf(sub[8]);
					if (theory == "T") {
						iTO.setIsCheckedDummy(true);

					} else {
						iTO.setIsCheckedDummy(false);

					}
				}
				if (sub[4] != null) {
					practical =  String.valueOf(sub[8]);
					if (practical == "P") {
						iTO.setIsCheckedDummyPractical(true);

					} else {
						iTO.setIsCheckedDummyPractical(false);

					}
				}

				iTO.setSubjectId(sub[0].toString());
				listSubject.add(iTO);

			}

			to.setSubjectList(listSubject);
			toList.add(to);
		}
		
		//Collections.sort(to,new ExamInternalRetestApplicationSubjectsTOComparator());
		return toList;
	}

	public List<ExamInternalRetestApplicationBO> convertToBo(List<ExamInternalRetestApplicationTO> stListTo) {
			List<ExamInternalRetestApplicationBO> boList=new ArrayList();
			for (ExamInternalRetestApplicationTO to : stListTo) {
				ExamInternalRetestApplicationBO bo=new ExamInternalRetestApplicationBO();
				bo.setAcademicYear(to.getAcademicYear());
				bo.setClassId(to.getClassId());
				bo.setIsActive(true);
				bo.setStudentId(to.getStudentId());
				bo.setIsAdded(1);
				bo.setCreatedDate(new Date());
				Set<ExamInternalRetestApplicationSubjectsBO> subBoSet=new HashSet();
				for (ExamInternalRetestApplicationSubjectsTO subto : to.getSubjectList()) {
					ExamInternalRetestApplicationSubjectsBO subBo=new ExamInternalRetestApplicationSubjectsBO();
					subBo.setSubjectId(Integer.parseInt(subto.getSubjectId()));
					if (subto.getIsCheckedDummy()) {
						subBo.setIsTheory(1);
					}
					if (subto.getIsCheckedDummyPractical()) {
						subBo.setIsPractical(1);
					}
					//subBo.setExamInternalRetestApplicationBO(bo);
					subBoSet.add(subBo);
				}
				bo.setSubList(subBoSet);
				boList.add(bo);
			}
		return boList;
	}

}
