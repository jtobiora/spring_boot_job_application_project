package ng.upperlink.projectboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ng.upperlink.projectboot.model.Applicant;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant, Integer>{
   Applicant findByEmail(String email);
}
