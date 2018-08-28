package ng.upperlink.projectboot.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import ng.upperlink.projectboot.model.Applicant;
import ng.upperlink.projectboot.service.ApplicantService;
import ng.upperlink.projectboot.utils.AdminResponse;
import ng.upperlink.projectboot.utils.UploadFileService;


@Controller
@CrossOrigin(origins="http://localhost:4200")
public class MainController {
	@Autowired
	private UploadFileService fileService;
	
	@Autowired
	private ApplicantService applicantService;
	

	@RequestMapping(value = "/api/registerApplicants",method = RequestMethod.POST,
	    consumes = "multipart/form-data")
	@ResponseBody
	public ResponseEntity handleApplicantRegistration(@RequestParam("file") MultipartFile file,
			@RequestParam("file2") MultipartFile file2,
			@RequestPart("applicant") Applicant applicant,
			HttpServletRequest request) {
		
		String uploadRootPath = request.getServletContext().getRealPath("upload");
		
		//Check the number of applicants registered so far
		List<Applicant> registeredApplicants = applicantService.findAllApplicants();
		if(registeredApplicants.size() >= 5) {
			//display application closed
			return new ResponseEntity<Object>("Application closed!", HttpStatus.BAD_REQUEST);
		} else {
			//continue to process registration
			//check if the email exists
			Applicant person = applicantService.findApplicantByEmail(applicant.getEmail());
			if(person != null) {
				return new ResponseEntity<Object>("Email already exists!", HttpStatus.BAD_REQUEST);
			}
			String message = "";
			try {
				boolean checkUploads = fileService.store(file,file2,uploadRootPath);
				if(checkUploads) {
					//save the applicant data to database
					Path passport = Paths.get(uploadRootPath + file.getOriginalFilename());	            
			        Path resume = Paths.get(uploadRootPath + file2.getOriginalFilename());
			        
			        applicant.setPassport(passport.toString());
			        applicant.setResume(resume.toString());
			        applicant.setRole("ADMIN");
			    
			        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			        applicant.setPassword(encoder.encode(applicant.getPassword())); 
			        
			        //save the applicant
			        applicantService.saveApplicant(applicant);
					
			        message = "File Upload was successful";
				    return ResponseEntity.status(HttpStatus.OK).body(message);
				} else {
					message = "File upload was not successful! ";
					return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
				}
				
			} catch(Exception ex) {
				//message = "Failed To upload " + file.getOriginalFilename();
				message = "Error in uploading file:  " + ex.getMessage();
				return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
			}		
		}
	}
	
	
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginAsUser(@RequestBody Applicant applicant) {
        try {
        	
        	Applicant user = applicantService.findApplicantByEmail(applicant.getEmail());
        	if(user != null) {
        		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();          	
            	String inputPassword = applicant.getPassword();
            	String dbPassword = user.getPassword();
            	if(bCryptPasswordEncoder.matches(inputPassword, dbPassword)) {
            		return ResponseEntity.status(HttpStatus.OK).body(user.getFirstName() + " " + user.getSurname());
            	}else {
            		throw new RuntimeException("Username or password is incorrect");
            	}
        	}else {
        		throw new RuntimeException("Incorrect username or password");
        	}
        	
        } catch (Exception ex) {
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	
	@RequestMapping(value = "/api/admin/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginAsAdmin(@RequestBody Applicant applicant) {
        try {   	
        	Applicant user = applicantService.findApplicantByEmail(applicant.getEmail());
        	if(user != null) {
        		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();          	
            	String inputPassword = applicant.getPassword();
            	String dbPassword = user.getPassword();
            	if(bCryptPasswordEncoder.matches(inputPassword, dbPassword)) {
            		AdminResponse admin = new AdminResponse();
            		admin.setName(user.getFirstName() + " " + user.getSurname());
            		admin.setRole(user.getRole());
            		return ResponseEntity.status(HttpStatus.OK).body(admin);
            	}else {
            		throw new RuntimeException("Username or password is incorrect");
            	}
        	}else {
        		throw new RuntimeException("Incorrect username or password");
        	}
        	
        } catch (Exception ex) {
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	
	@RequestMapping(value = "/api/getAllApplicants", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
		List<Applicant> allApplicants = new ArrayList<>();
        try {   	
        	List<Applicant> users = applicantService.findAllApplicants();
        	if(users != null) {
        		 //find and remove the user with admin role
        		for(Applicant applicant : users) {
        			if(!applicant.getRole().equalsIgnoreCase("ADMIN")) {
        				allApplicants.add(applicant);
        			}
        		}
        	
        		return new ResponseEntity<Object>(allApplicants, HttpStatus.OK);
        	}  	
        } catch (Exception ex) {
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
    }

}
