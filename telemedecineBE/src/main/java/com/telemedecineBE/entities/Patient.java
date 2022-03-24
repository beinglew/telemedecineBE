package com.telemedecineBE.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.telemedecineBE.enumeration.UserType;
import lombok.*;

@Entity
@Table(name = "PATIENT",
		uniqueConstraints = @UniqueConstraint(columnNames={"EMAIL", "PHONE"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient extends User{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PATIENT_ID")
	private Integer id;
	/*
	@Column(name="FIRST_NAME")
	private String Fname;
	@Column(name="LAST_NAME")
    private String Lname;
	@Column(name="PHONE")
    private String phone;
	@Column(name="EMAIL")
    private String email;
	 */

	@Column(name="DOB")
    private String dob;
	@Column(name="IS_INSURED")
    private Boolean isInsured = false;
	@Column(name="STATE")
    private Integer state=1;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="medicalHistoryId")
	private List<MedicalHistory> medicalHistory = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "insuranceID")
    private List<Insurance> insurance;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "patient", fetch = FetchType.LAZY)
	@JsonManagedReference(value = "patient-address")
	private Address address;
	
	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference(value = "patient-appointments")
	private List<Appointment> appointments;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference(value = "patient-prescriptions")
	private List<Prescriptions> prescriptions;

	@ManyToMany
	@JoinColumn(name="doctorID")
	private List<Doctor> doctors;

	public Patient(String fname, String lname, String email, String cellphone, String userpassword){
		super(fname, lname, userpassword, UserType.PATIENT, email, cellphone);
	}

	public Doctor addDoctor(Doctor doctor){
		if(!this.doctors.contains(doctor)){
			this.doctors.add(doctor);
			return doctor;
		} else {
			return null;
		}
	}

	@Override
	public String toString (){
		return "Patient [id= " + id + ", fname= " + getFname() + ", lname= " + getLname() +
				", username= " + getUserName() + ", userType= " + getUserType().getType() +
				", email= " + getEmail() + ", cellphone= " + getCellphone() + ", dob= " + dob +
				", isInsured= " + isInsured + ", habilitations= " + getHabilitations() + ", medicalHistory= " +
				medicalHistory + ", insurance= " + insurance + ", address= " + address + ", appointments= " +
				appointments + ", prescriptions= " + prescriptions + ", state= " + state + "]";
	}
}
