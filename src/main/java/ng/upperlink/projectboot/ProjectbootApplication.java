package ng.upperlink.projectboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ng.upperlink.projectboot.model.Applicant;
import ng.upperlink.projectboot.service.ApplicantService;

@SpringBootApplication
public class ProjectbootApplication implements CommandLineRunner{
	@Autowired
    private ApplicantService applicantService;
	
	public static void main(String[] args) {
		SpringApplication.run(ProjectbootApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		String email = "admin@adiogroup.com";
		Applicant user = applicantService.findApplicantByEmail(email);
		if(user == null) {
			Applicant admin = new Applicant();
			admin.setPassport(null);
			admin.setResume(null);
			admin.setRole("ADMIN");
	        admin.setFirstName("John");
	        admin.setSurname("Peters");
	        admin.setPhoneNumber("08053425262");
	        admin.setCoverLetter(null);
	        admin.setEmail(email);
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        admin.setPassword(encoder.encode("password"));
	        
	        //save the admin
	        applicantService.saveApplicant(admin);
		}
	}
}
