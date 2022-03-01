package com.telemedecineBE.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telemedecineBE.entities.Patient;

import javax.transaction.Transactional;

@Transactional
public interface PatientRepository 
	
				extends JpaRepository<Patient, Serializable>{
	
	public Patient findByPhone(String phone);
	
	public Patient findByEmail(String email);

	public Patient findById(Integer id);
	
	public Boolean existsByPhone(String phone);

	public Boolean existsByEmail(String email);

	public Boolean existsById(Integer id);

	public void deleteByPhone(String tel);

	public void deleteByEmail(String email);

	public void deleteById(Integer id);


}
