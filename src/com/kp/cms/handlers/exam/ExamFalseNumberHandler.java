package com.kp.cms.handlers.exam;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.kp.cms.bo.admin.EligibilityCriteria;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.attendance.ClassEntryHandler;
import com.kp.cms.helpers.admin.EligibilityCriteriaHelper;
import com.kp.cms.helpers.exam.ExamFalseNumberHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamDefinitionTO;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

public class ExamFalseNumberHandler {
	private static ExamFalseNumberHandler marksCardHandler=null;
	public static ExamFalseNumberHandler getInstance(){
		if(marksCardHandler==null){
			marksCardHandler = new ExamFalseNumberHandler();
		}
		return marksCardHandler;
	}
	
	IExamFalseNumberTransaction transaction = new ExamFalseNumberTransactionImpl();
	
	
	
	public void getcourseansScheme(NewExamMarksEntryForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		transaction.getcourseansScheme(marksCardForm);
		
	}
	public boolean duplicatecheck(NewExamMarksEntryForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		boolean isduplicated=false;
		List<ExamFalseNumberGen> list=transaction.getfalsenos(marksCardForm);
		if(list!=null && !list.isEmpty()){
			isduplicated=true;
		}
		return isduplicated;
	}

	public boolean saveFalseNumbers(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		// TODO Auto-generated method stub
		boolean isadded = ExamFalseNumberHelper.getInstance().convertTotoBo(newExamMarksEntryForm);
		return isadded;
	}
	

	public boolean saveFalseNumbersForEdit(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		// TODO Auto-generated method stub
		boolean isadded = ExamFalseNumberHelper.getInstance().convertTotoBoForEdit(newExamMarksEntryForm);
		return isadded;
	}
	public boolean updateFalseSiNo(NewExamMarksEntryForm marksCardForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.updateFalseSiNo(marksCardForm);
	}
	
	public Map<String, ExamFalseNumberGen> getFlaseNumberMap(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		return ExamFalseNumberHelper.getInstance().getFlaseNumberMap(newExamMarksEntryForm);
	}
	public boolean BarcodeGeneration(NewExamMarksEntryForm form) throws BarcodeException{
        // get a Barcode from the BarcodeFactory
		Barcode barcode = BarcodeFactory.createCode128B("My Barcode");
		 BufferedImage bImage = null;
        try {
        	 

            // Let the barcode image handler do the hard work
        	bImage=BarcodeImageHandler.getImage(barcode);
        	//Image scaledImage = new ImageIcon(bImage).getImage();
        	ImageIO.write(bImage , "png", new File("d:\\test\\image.png"));

        } catch (Exception e) {
            // Error handling here
        }
		return false;}
}
