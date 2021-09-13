package com.kp.cms.transactions.fee;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.Fee;
import com.kp.cms.bo.admin.FeeAccountAssignment;
import com.kp.cms.bo.admin.FeeGroup;


/**
 * 
 * @date 19/Jan/2009
 * This is an interface for FeeAccountTransaction.
 */
public interface IFeeTransaction {
	
	public boolean addFeeAssignment(Fee fee) throws ConstraintViolationException,Exception;
	public List<Fee> getAllFees() throws Exception;
	public boolean deleteFeeAssignment(Fee fee) throws Exception;
	public boolean activateFeeAssignment(Fee fee) throws Exception;
	public Fee getFeeAssignmentById(int feeId) throws Exception;
	public List<FeeAccountAssignment> getFeesAssignAccounts(int feeId,Set<Integer> accountSet,Set<Integer> applicableSet) throws Exception;
	public boolean updateFeeAssignment(Fee fee) throws Exception;
	public List<Fee> getFeesPaymentDetailsForApplicationNo(Set<Integer>courseSet,String year,Set<Integer> semSet, boolean isAidedStudent) throws Exception ;
	public Fee getFeeByCompositeKeys(Fee feeOld) throws Exception;
	public Map<Integer,String> getFeesGroupDetailsForCourse(Set<Integer>courseSet,String year) throws Exception;
	public List<FeeGroup> getFeeGroup()throws Exception;
}
