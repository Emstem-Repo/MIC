package com.kp.cms.transactions.admission;

import java.util.List;
import java.util.Set;

import com.kp.cms.forms.admission.SendAllotmentMemoSmsForm;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.bo.admission.GenerateMemoMail;

public interface ISendAllotmentSmsTransaction {

	List<Integer> getSentMemoList(SendAllotmentMemoSmsForm sbForm) throws ApplicationException;

	List<Student> getSureStudentList(SendAllotmentMemoSmsForm sbForm, List<Integer> sentMemoList, Set<Integer> tempcourseset) throws ApplicationException;

	List<Student> getChanceStudentList(SendAllotmentMemoSmsForm sbForm,
			List<Integer> sentMemoList, Set<Integer> tempcourseset) throws ApplicationException;

	boolean saveMailList(List<GenerateMemoMail> list) throws Exception;

	List<Integer> getSentMemoMailList(SendAllotmentMemoSmsForm sbForm) throws ApplicationException;

}
