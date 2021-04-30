package com.kp.cms.utilities.images;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.kp.cms.utilities.HibernateUtil;

public class core {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		//courseAllotment();
	
		
		// create two dates
		try{
			 
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			   //get current date time with Date()
			   Date date1 = new Date();
			   System.out.println(dateFormat.format(date1));
		 
			
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date date2 = (Date) sdf.parse("2015-05-31");
 
        	System.out.println(sdf.format(date1));
        	System.out.println(sdf.format(date2));
 
        	if(date1.compareTo(date2)>0){
        		System.out.println("Date1 is after Date2");
        		
        		
        		
        	}
        	
        	/*else if(date1.compareTo(date2)<0){
        		System.out.println("Date1 is before Date2");
        	}else if(date1.compareTo(date2)==0){
        		System.out.println("Date1 is equal to Date2");
        	}else{
        		System.out.println("How to get here?");
        	}
        	 */
    	}catch(ParseException ex){
    		ex.printStackTrace();
    	}
		
		
	}
	
	
	
	
	
	
	

	public static void  courseAllotment() throws Exception {
		
		
		/*
	}
		
		List<core1> l=new LinkedList<core1>();
		
		int totseats=13;
		int scseat=2;
		int stseat=2;
		int bcseat=5;
		int genseat=4;
		
		
		core1 c1=new core1();
		c1.setCourseid(1);
		c1.setMarks(100);
		c1.setRank(1);
		c1.setCaste("sc");
		c1.setAllotment(false);
		l.add(c1);
		
		core1 c2=new core1();
		c2.setCourseid(1);
		c2.setMarks(99);
		c2.setRank(2);
		c2.setCaste("oc");
		c2.setAllotment(false);
		l.add(c2);
		
		core1 c3=new core1();
		c3.setCourseid(1);
		c3.setMarks(99);
		c3.setRank(3);
		c3.setCaste("bc");
		c3.setAllotment(false);
		l.add(c3);
		
		core1 c4=new core1();
		c4.setCourseid(1);
		c4.setMarks(98);
		c4.setRank(4);
		c4.setCaste("oc");
		c4.setAllotment(false);
		l.add(c4);
		
		core1 c5=new core1();
		c5.setCourseid(1);
		c5.setMarks(97);
		c5.setRank(5);
		c5.setCaste("st");
		c5.setAllotment(false);
		l.add(c5);
		
		core1 c6=new core1();
		c6.setCourseid(1);
		c6.setMarks(90);
		c6.setRank(6);
		c6.setCaste("bc");
		c6.setAllotment(false);
		l.add(c6);
		
		core1 c=new core1();
		c.setCourseid(1);
		c.setMarks(88);
		c.setRank(7);
		c.setCaste("oc");
		c.setAllotment(false);
		l.add(c);
	
		
		core1 c8=new core1();
		c8.setCourseid(1);
		c8.setMarks(80);
		c8.setRank(8);
		c8.setCaste("bc");
		c8.setAllotment(false);
		l.add(c8);
		
		core1 c9=new core1();
		c9.setCourseid(1);
		c9.setMarks(75);
		c9.setRank(9);
		c9.setCaste("bc");
		c9.setAllotment(false);
		l.add(c9);
		
		core1 c10=new core1();
		c10.setCourseid(1);
		c10.setMarks(70);
		c10.setRank(10);
		c10.setCaste("sc");
		c10.setAllotment(false);
		l.add(c10);
		
		core1 c7=new core1();
		c7.setCourseid(1);
		c7.setMarks(60);
		c7.setRank(11);
		c7.setCaste("bc");
		c7.setAllotment(false);
		l.add(c7);
		
		core1 c11=new core1();
		c11.setCourseid(1);
		c11.setMarks(60);
		c11.setRank(12);
		c11.setCaste("oc");
		c11.setAllotment(false);
		l.add(c11);
		
		core1 c12=new core1();
		c12.setCourseid(1);
		c12.setMarks(60);
		c12.setRank(13);
		c12.setCaste("sc");
		c12.setAllotment(false);
		l.add(c12);
		
		int sccount=0;
		int stcount=0;
		int bccount=0;
		int gencount=0;
		int totcount=0;
		
		
		
		//general
		Iterator<core1> i3=l.iterator();
		while(i3.hasNext()){
			core1 co=i3.next();
		
			if(!co.isAllotment())
			if(gencount<genseat){
				co.setAllotment(true);
				gencount++;
			}
		}
		
		
		//sc
		Iterator<core1> i=l.iterator();
		while(i.hasNext()){
			core1 co=i.next();
			//if(totcount<totseats){
				
			if(!co.isAllotment())
			if(co.getCaste().equalsIgnoreCase("sc")){
				if(sccount<scseat){
					co.setAllotment(true);
					sccount++;
				}
			}
			
			
			
		}
		
		//sc left seat
		if(sccount<scseat){
			genseat=genseat+(scseat-sccount);
		}
		
		//st
			Iterator<core1> i1=l.iterator();
			while(i1.hasNext()){
				core1 co=i1.next();
			
				if(!co.isAllotment())
			 if(co.getCaste().equalsIgnoreCase("st")){
				if(stcount<stseat){
					co.setAllotment(true);
					stcount++;
				}
				
				
			}
			 
			}
			
			
			//st left seat
			if(stcount<stseat){
				genseat=genseat+(stseat-stcount);
			}
			
			//bc
			Iterator<core1> i2=l.iterator();
			while(i2.hasNext()){
				core1 co=i2.next();
			
				if(!co.isAllotment())
			 if(co.getCaste().equalsIgnoreCase("bc")){
				if(bccount<bcseat){
					co.setAllotment(true);
					bccount++;
				}
				
			}
			 
			}	 
			
			//bc left seat
			if(bccount<bcseat){
				genseat=genseat+(bcseat-bccount);
			}
			 
			//remain general
			Iterator<core1> i4=l.iterator();
			while(i4.hasNext()){
				core1 co=i4.next();
			
				if(!co.isAllotment())
				if(gencount<genseat){
					co.setAllotment(true);
					gencount++;
				}
			}
			
		//}
			//if(co.isAllotment()){
				//totcount++;
			//}
			
		//}//if 
			
		//}//while
		
	
		
		
		Iterator<core1> i5=l.iterator();
		while(i5.hasNext()){
			core1 co=i5.next();
			System.out.println(co.getCaste()+"==="+co.getRank()+"==="+co.isAllotment());
		}
		
		
		
	*/
		
	
	/*List<Integer> l=new ArrayList<Integer>();
	l.add(1);
	l.add(2);
	l.add(3);
	
	System.out.println(l.size());
	System.out.println(l.get(l.size()-1));
	
	Map<Integer,String> map=new HashMap<Integer, String>();
	map.put(1, "A");
	map.put(2, "B");
	map.put(3, "C");
	System.out.println("============outside"+map);
	new core().a(map);
	
	
	}
	
	void a(Map<Integer,String> map){
		map.put(1, "AAA");
		System.out.println("==============inside"+map);
		
	}
*/
	
	}
}
