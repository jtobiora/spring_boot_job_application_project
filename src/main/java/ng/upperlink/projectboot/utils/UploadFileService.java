package ng.upperlink.projectboot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ng.upperlink.projectboot.exceptions.InvalidFileException;


@Component
public class UploadFileService {
	@Autowired
    private FileUtil fileUtils;
	
	    Logger log = LoggerFactory.getLogger(this.getClass().getName());
		private final Path rootLocation = Paths.get("upload-dir");
		
		public boolean store(MultipartFile file,MultipartFile file2,String uploadRootPath) {
		    
			try {
				
				if(verifyPassportPhotograph(file) && verifyResume(file2)) {
					// Get the file and save it somewhere
		            byte[] bytes = file.getBytes();
		            Path path = Paths.get(uploadRootPath + file.getOriginalFilename());
		            Files.write(path, bytes);
		            
		         // Get the file and save it somewhere
		            byte[] bytes2 = file2.getBytes();
		            Path path2 = Paths.get(uploadRootPath + file2.getOriginalFilename());
		            Files.write(path2, bytes2);
		            
		            return true;
				}
				 
	            
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
			return false;
		}
		
		public boolean verifyPassportPhotograph(MultipartFile file) throws InvalidFileException{
			boolean flag = true;
			String fileName = file.getOriginalFilename();
			String cleanFileName = fileName.replaceAll("[^A-Za-z0-9.()]", "");
	                //check for valid extensions
			     if(!fileUtils.isValidExtension(cleanFileName)) {
			    	 flag = false;
			         throw new InvalidFileException("Invalid File Extension!");
			      };
			      
			     if(file.getSize() > 100000) {
			    	 flag = false;
			    	 throw new InvalidFileException("File Size limit exceeded!");
			     }
			 return flag;
		}
		
		public boolean verifyResume(MultipartFile file) throws InvalidFileException{
			boolean flag = true;
			String fileName = file.getOriginalFilename();
			String cleanFileName = fileName.replaceAll("[^A-Za-z0-9.()]", "");
	        //check for valid extensions
			     if(!fileUtils.isValidResumeExtension(cleanFileName)) {
			    	 flag = false;
			         throw new InvalidFileException("Invalid File Extension!");
			      };
			      
			     if(file.getSize() > 2000000) {
			    	 flag = false;
			    	 throw new InvalidFileException("File Size limit exceeded!");
			     }
			 return flag;
		}
}
