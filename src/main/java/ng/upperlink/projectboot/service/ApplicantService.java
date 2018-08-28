package ng.upperlink.projectboot.service;

import java.util.List;

import ng.upperlink.projectboot.model.Applicant;

public interface ApplicantService {
	   Applicant saveApplicant(Applicant applicant);
	   Applicant findApplicantById(int id);
	   Applicant findApplicantByEmail(String email);
	   List<Applicant> findAllApplicants();
}
