package com.kp.cms.helpers.timetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.Period;
import com.kp.cms.bo.admin.TimeAllocationBo;
import com.kp.cms.forms.timetable.ClassWiseTimeTableViewForm;
import com.kp.cms.to.timetable.ClassWiseTimeTableViewTo;
import com.kp.cms.to.timetable.StaffWiseTimeTableViewTo;

public class ClassWiseTimeTableViewHelper {

	private static Map<Integer, String> dayMap = null;
	static {
		dayMap = new HashMap<Integer, String>();
		dayMap.put(1, "Monday");
		dayMap.put(2, "Tuesday");
		dayMap.put(3, "Wednesday");
		dayMap.put(4, "Thursday");
		dayMap.put(5, "Friday");
		dayMap.put(6, "Saturday");
		dayMap.put(7, "Sunday");
	}
	
	public static ClassWiseTimeTableViewHelper classWiseHelper=null;
	
	public static ClassWiseTimeTableViewHelper getInstance() {
		if (classWiseHelper == null) {
			return new ClassWiseTimeTableViewHelper();
		}
		return classWiseHelper;
	}

	private ClassWiseTimeTableViewHelper() {
	}
	
	public void convertBoListToTimeTableViewToList(List<TimeAllocationBo> classTimeTable,ClassWiseTimeTableViewForm formObj)
	{
		HashMap<Integer, HashMap<Integer, String>> dayPeriodMapList = new HashMap<Integer, HashMap<Integer, String>>();
		ArrayList<ClassWiseTimeTableViewTo> subTimeList = new ArrayList<ClassWiseTimeTableViewTo>();
		ArrayList<Integer> periodIds = new ArrayList<Integer>();
		ArrayList<String> periodListOfStaff = getPeriodList(classTimeTable,
				periodIds);
		StringBuffer className = new StringBuffer();
		boolean firstRun = true;
		for (TimeAllocationBo bo : classTimeTable) {
			if (firstRun) {
				ClassSchemewise classScheme = bo.getClassId() ;
				className.append(classScheme.getClasses().getName());
				firstRun = false;
			}
			Period period = bo.getPeriodId();
			if (dayPeriodMapList.containsKey(bo.getDay())) {
				HashMap<Integer, String> innerPeriodMap = dayPeriodMapList
						.remove(bo.getDay());
				if (!innerPeriodMap.containsKey(period.getId())) {
					StringBuffer buffer = new StringBuffer();
					buffer.append(bo.getPreferredSubjectId().getName() + "-");
					buffer.append(bo.getRoomId().getRoomNo());
					innerPeriodMap.put(period.getId(), buffer.toString());
				}
				dayPeriodMapList.put(bo.getDay(), innerPeriodMap);
			} else {
				HashMap<Integer, String> innerMap = new HashMap<Integer, String>();
				StringBuffer buffer = new StringBuffer();
				buffer.append(bo.getPreferredSubjectId().getName() + "-");
				buffer.append(bo.getRoomId().getRoomNo());
				innerMap.put(period.getId(), buffer.toString());
				dayPeriodMapList.put(bo.getDay(), innerMap);
			}
		}
		for (int i = 1; i <= 7; i++) {
			if (dayPeriodMapList.containsKey(i)) {
				HashMap<Integer, String> map = dayPeriodMapList.get(i);
				ArrayList<String> periods = new ArrayList<String>();
				for (Integer periodId : periodIds) {
					if (map.containsKey(periodId)) {
						periods.add(map.get(periodId));
					} else {
						periods.add("");
					}
				}
				subTimeList.add(new ClassWiseTimeTableViewTo(dayMap.get(i),
						periods));
			} else {
				subTimeList.add(new ClassWiseTimeTableViewTo(dayMap.get(i),
						makeAdjustableList(periodIds.size())));
			}
		}
		formObj.setAcademicClass(className.toString());
		formObj.setSubTimeList(subTimeList);
		formObj.setPeriodList(periodListOfStaff);
	}
	
	private ArrayList<String> makeAdjustableList(int size) {
		ArrayList<String> adjustableList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			adjustableList.add("");
		}
		return adjustableList;
	}
	
	private ArrayList<String> getPeriodList(
			List<TimeAllocationBo> staffTimeTable, ArrayList<Integer> periodIds) {
		ArrayList<String> periodList = new ArrayList<String>();
		for (TimeAllocationBo bo : staffTimeTable) {
			StringBuffer buffer = new StringBuffer();
			Period period = bo.getPeriodId();
			buffer.append(period.getPeriodName());
			buffer.append("\n");
			buffer.append("Time:   " + period.getStartTime() + " to "
					+ period.getEndTime());
			if(!periodIds.contains(period.getId()))
				periodIds.add(period.getId());
			if(!periodList.contains(buffer.toString()))
				periodList.add(buffer.toString());	
		}

		return periodList;
	}
}
