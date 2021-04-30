package com.kp.cms.actions.admission;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DownloadAction;
import org.apache.struts.actions.DownloadAction.FileStreamInfo;
import org.apache.struts.actions.DownloadAction.StreamInfo;

import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.employee.EmpImages;
import com.kp.cms.forms.admission.ExportPhotosForm;
import com.kp.cms.forms.employee.EmployeeExportPhotoForm;
import com.kp.cms.transactionsimpl.admission.ExportPhotosImpl;

public class ExportImagesAction extends DownloadAction {
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	private static final Log log = LogFactory.getLog(ExportImagesAction.class);

	protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Entered DownloadImageAction");
		ExportPhotosForm objForm = (ExportPhotosForm) form;
		ExportPhotosImpl impl = new ExportPhotosImpl();
		errors.clear();
		messages.clear();
		errors = objForm.validate(mapping, request);

		int year = (objForm.getAdmittedYear() != null
				&& objForm.getAdmittedYear().trim().length() > 0 ? Integer
				.parseInt(objForm.getAdmittedYear()) : 0);
		int programId = (objForm.getProgramId() != null
				&& objForm.getProgramId().trim().length() > 0 ? Integer
				.parseInt(objForm.getProgramId()) : 0);
		String admitOrall=objForm.getAdmitOrAll();
		String fileName = "Images" + "-" + programId + "-" + year;
		String filePath=request.getRealPath("");
    	filePath = filePath + "//TempFiles//";
		File tempFile = new File(filePath+fileName + ".zip");
		if (tempFile.exists()) {
			tempFile.delete();
		}
		FileOutputStream tempZipFile = new FileOutputStream(tempFile);
		ZipOutputStream zout = new ZipOutputStream(tempZipFile);

		String contentType = fileName + ".zip";
		response.setHeader("Content-disposition", "attachment; filename ="
				+ fileName + ".zip");
		response.setContentType("application/zip");

		saveErrors(request, errors);

		if (errors.isEmpty()) {
			int totalImages = 0;

			totalImages = impl.getImages(year, programId,admitOrall);
			if (totalImages > 0) {
				int PAGESIZE = 5;
				int page = 1;
				int totalPages = (totalImages / PAGESIZE) + 1;

				while (page <= totalPages) {

					List<ApplnDoc> images = impl.getImages(year, programId,
							page, PAGESIZE,admitOrall);

					InputStream in = null;
					int size = images.size();
					if (size != 0) {
						for (int i = 0; i < size; i++) {

							ApplnDoc applnDoc = images.get(i);
							Set<Student> student = applnDoc.getAdmAppln().getStudents();
							Iterator< Student> iterator = student.iterator();
							String regNo ="";
							while (iterator.hasNext()) {
								Student student2 = (Student) iterator.next();
								regNo = student2.getRegisterNo(); 
							}
							if (applnDoc.getIsPhoto() != null)
								if (applnDoc.getIsPhoto()
										&& applnDoc.getDocument() != null) {
									byte[] myFileBytes = applnDoc.getDocument();
									String imageName="";
									if(regNo != null && !regNo.isEmpty()){
										imageName = regNo+ ".jpg";
									}else{
										imageName = applnDoc.getAdmAppln().getApplnNo()+".jpg";
									}
									

									try {
										in = new ByteArrayInputStream(
												myFileBytes);
										byte buffer[] = new byte[2048];

										zout.putNextEntry(new ZipEntry(
												imageName));
										int len;
										while ((len = in.read(buffer)) > 0) {
											zout.write(buffer, 0, len);
										}
									} catch (IOException e) {
										e.printStackTrace();
										log
												.info("DownloadImageAction IOException, "
														+ e);
									} catch (Exception e) {
										e.printStackTrace();
										log
												.info("DownloadImageAction Exception, "
														+ e);
									}
									
								}
							zout.closeEntry();
						}
					}
					page = page + 1;
				}
				zout.close();
			}
			
			objForm.setProgramId(null);
		}
		return new FileStreamInfo(contentType, tempFile);
		

	}
	
	
	protected StreamInfo getEmployeeStreamInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Entered DownloadImageAction");
		EmployeeExportPhotoForm objForm = (EmployeeExportPhotoForm) form;
		ExportPhotosImpl impl = new ExportPhotosImpl();
		errors.clear();
		messages.clear();
		errors = objForm.validate(mapping, request);

	//	List<Employee>  = impl.getEmployeeImages;
		String fileName = "EmployeeImages";
		String filePath=request.getRealPath("");
    	filePath = filePath + "//TempFiles//";
		File tempFile = new File(filePath+fileName + ".zip");
		if (tempFile.exists()) {
			tempFile.delete();
		}
		FileOutputStream tempZipFile = new FileOutputStream(tempFile);
		ZipOutputStream zout = new ZipOutputStream(tempZipFile);

		String contentType = fileName + ".zip";
		response.setHeader("Content-disposition", "attachment; filename ="
				+ fileName + ".zip");
		response.setContentType("application/zip");

		saveErrors(request, errors);
		if (errors.isEmpty()) {
			int totalImages = 0;

			totalImages = impl.getEmployeeImages();
			if (totalImages > 0) {
				int PAGESIZE = 5;
				int page = 1;
				int totalPages = (totalImages / PAGESIZE) + 1;

				while (page <= totalPages) {

					List<EmpImages> images = impl.getEmployeeImages(page, PAGESIZE);

					InputStream in = null;
					int size = images.size();
					if (size != 0) {
						for (int i = 0; i < size; i++) {
							EmpImages empPhoto = images.get(i);
													
						String fingerprintId ="";
						fingerprintId = empPhoto.getEmployee().getFingerPrintId(); 
							if (fingerprintId != null && !fingerprintId.isEmpty() && empPhoto.getEmpPhoto() != null && empPhoto.getEmpPhoto().length>0)
							{
									byte[] myFileBytes = empPhoto.getEmpPhoto();
									String imageName="";
									imageName = fingerprintId+ ".jpg";
									
									try {
										in = new ByteArrayInputStream(
												myFileBytes);
										byte buffer[] = new byte[2048];

										zout.putNextEntry(new ZipEntry(
												imageName));
										int len;
										while ((len = in.read(buffer)) > 0) {
											zout.write(buffer, 0, len);
										}
									} catch (IOException e) {
										e.printStackTrace();
										log
												.info("DownloadImageAction IOException, "
														+ e);
									} catch (Exception e) {
										e.printStackTrace();
										log
												.info("DownloadImageAction Exception, "
														+ e);
									}
									
								}
							zout.closeEntry();
						}
					}
					page = page + 1;
				}
				zout.close();
			}
			
		//	objForm.setProgramId(null);
		}
		return new FileStreamInfo(contentType, tempFile);
		

	}

	
	

	protected class ByteArrayStreamInfo implements StreamInfo {

		protected String contentType;
		protected byte[] bytes;

		public ByteArrayStreamInfo(String contentType, byte[] myXLSBytes) {
			this.contentType = contentType;
			this.bytes = myXLSBytes;
		}

		public String getContentType() {
			return contentType;
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(bytes);
		}
	}
}
