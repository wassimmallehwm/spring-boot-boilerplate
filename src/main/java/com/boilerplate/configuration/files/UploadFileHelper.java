package com.boilerplate.configuration.files;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Calendar;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.boilerplate.utils.UtilsService;

@Component
public class UploadFileHelper {

	@Autowired
	private UtilsService utilsService;

	public static String handleFileUpload(MultipartFile file, String path) throws FileUploadException {
		try {
			String fileName = file.getOriginalFilename();
			file.transferTo( new File(path + fileName));
			return fileName;
		} catch (Exception e) {
			throw new FileUploadException("File upload failed");
		}
	}
	public File simpleUpload(MultipartFile file, boolean encrypt_file_name, String upload_folder){
		System.out.println(file.getOriginalFilename());
		String filename=null;
		File serverfile=null;
		try{
			if(!file.isEmpty()){
				if(encrypt_file_name){
					String curentFileName=file.getOriginalFilename();
					String extension=curentFileName.substring(curentFileName.lastIndexOf("."));
					Long nameRadom=Calendar.getInstance().getTimeInMillis();
					filename= nameRadom + extension;
				}else 
					filename=file.getOriginalFilename();
				byte[] bytes=file.getBytes();
				String tempDir = utilsService.getTempDir(upload_folder);
				File dir= new File(tempDir);
				System.out.println("DIR---->"+dir.getAbsolutePath());
				if(!dir.exists()){
					dir.mkdirs();
				}
				serverfile=new File(dir.getAbsolutePath()+File.separator+filename);
					
				BufferedOutputStream stream= new BufferedOutputStream(Files.newOutputStream(serverfile.toPath()));
				stream.write(bytes);
				stream.close();
				return serverfile;
			}
		}catch (Exception e)
		{
			
			
		}
		return serverfile;
	}
	public static String readFromInputStream(InputStream inputStream)
			  throws IOException {
			    StringBuilder resultStringBuilder = new StringBuilder();
			    try (BufferedReader br
			      = new BufferedReader(new InputStreamReader(inputStream))) {
			        String line;
			        while ((line = br.readLine()) != null) {
			            resultStringBuilder.append(line).append("\n");
			        }
			    }
			  return resultStringBuilder.toString();
			}

}
