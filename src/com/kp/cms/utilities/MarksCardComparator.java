package com.kp.cms.utilities;

import java.util.Comparator;

import com.kp.cms.to.admin.SubjectTO;

public class MarksCardComparator implements Comparator<SubjectTO> {
	public int compare(SubjectTO arg0,SubjectTO arg1) {
		if(arg0!=null && arg1!=null){
			if (arg0.getSubOrder()!= 0 && arg1.getSubOrder()!=0){
			if(arg0.getSubOrder() >  arg1.getSubOrder())
				return 1;
			}
		}
		return 0;
	}
}
