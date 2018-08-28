package ng.upperlink.projectboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ng.upperlink.projectboot.model.Applicant;
import ng.upperlink.projectboot.repository.ApplicantRepository;

@Service
public class ApplicantServiceImpl implements ApplicantService{

	@Autowired
    private ApplicantRepository applicantRepository;
	
	@Override
	public Applicant saveApplicant(Applicant applicant) {
		Applicant employee = applicantRepository.save(applicant);
		return employee;
	}

	@Override
	public Applicant findApplicantById(int id) {
		return applicantRepository.findOne(id);
	}

	@Override
	public Applicant findApplicantByEmail(String email) {
		return applicantRepository.findByEmail(email);
	}

	@Override
	public List<Applicant> findAllApplicants() {
		// TODO Auto-generated method stub
		return (List<Applicant>) applicantRepository.findAll();
	}
   
}
