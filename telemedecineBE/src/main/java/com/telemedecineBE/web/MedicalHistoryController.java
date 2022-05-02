package com.telemedecineBE.web;

import com.telemedecineBE.dao.MedicalHistoryRepository;
import com.telemedecineBE.dao.PatientRepository;
import com.telemedecineBE.entities.MedicalHistory;
import com.telemedecineBE.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MedicalHistoryController {

    private MedicalHistoryRepository medicalHistoryRepository;
    private PatientRepository patientRepository;

    @Autowired
    MedicalHistoryController(MedicalHistoryRepository medicalHistoryRepository, PatientRepository patientRepository){
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.patientRepository = patientRepository;
    }

    //get all medical history (conditions)
    @GetMapping("/medical_histories")
    List<MedicalHistory> getAllMedicalHistory(){
        System.out.println("getAllMedicalHistory");
        return medicalHistoryRepository.findAll();
    }

    //get medical history by name
    @GetMapping("/medical_history/name={name}")
    MedicalHistory getMedicalHistoryByName(@PathVariable(value="name")String name){
        Boolean exists = medicalHistoryRepository.existsByName(name);
        if(!exists){
            throw new IllegalStateException("Medical history with the name " + name + " does not exist.");
        }
        System.out.println("getMedicalHistoryByName");
        MedicalHistory medication = medicalHistoryRepository.findByName(name);
        return medication;
    }

    //get medical history by id
    @GetMapping("/medical_history/id={id}")
    MedicalHistory getMedicalHistoryById(@PathVariable(value="id")Integer id){
        Boolean exists = medicalHistoryRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("Medical history with id " + id + " does not exist");
        }
        System.out.println("getMedicalHistoryById");
        MedicalHistory medication = medicalHistoryRepository.findById(id);
        return medication;
    }

    //add new medical history
    @PostMapping("/medical_history")
    MedicalHistory newMedicalHistory(@RequestBody MedicalHistory medicalHistory){
        Boolean exists = medicalHistoryRepository.existsByName(medicalHistory.getName());
        if(exists){
            throw new IllegalStateException("Medical history with name " + medicalHistory.getName() + " already exists.");
        }
        medicalHistoryRepository.save(medicalHistory);
        System.out.println("newMedicalHistory");
        return medicalHistory;
    }

    @PostMapping("/medical_histories")
    List<MedicalHistory> newMedicalHistories(@RequestBody List<MedicalHistory> medicalHistories){
        for (MedicalHistory mH:
                medicalHistories) {
            if(medicalHistoryRepository.existsByName(mH.getName())){
                throw new IllegalStateException("Medical History with  " + mH.getName() + " already exists.");
            }
        }
        System.out.println("newMedicalHistories");
        medicalHistoryRepository.saveAll(medicalHistories);
        return medicalHistories;
    }

    //delete medical history by name
    @DeleteMapping("/medical_history/name={name}")
    void deleteMedicalHistoryByName(@PathVariable(value = "name") String name){
        Boolean exists = medicalHistoryRepository.existsByName(name);
        if(!exists){
            throw new IllegalStateException("Medical history with name " + name + " does not exist.");
        }
        System.out.println("deleteMedicalHistoryByName");
        medicalHistoryRepository.deleteByName(name);
    }

    //delete medical history by id
    @DeleteMapping("/medical_history/id={id}")
    void deleteMedicalHistoryById(@PathVariable(value = "id") Integer id){
        Boolean exists = medicalHistoryRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("Medical History with id " + id + " does not exist.");
        }
        System.out.println("deleteMedicalHistoryById");
        MedicalHistory mH = medicalHistoryRepository.findById(id);
        Patient pat = mH.getPatient();
        List<MedicalHistory> mHs = pat.getMedicalHistory();
        mHs.removeIf(m -> m.getId() == mH.getId());
        pat.setMedicalHistory(mHs);
        patientRepository.save(pat);
        medicalHistoryRepository.deleteById(id);
    }

    //update medical history by name
    @PutMapping("/medical_history/name={name}")
    MedicalHistory updateMedicalHistoryByName(
            @PathVariable(value = "name") String name,
            @RequestParam(required = false)String newName,
            @RequestParam(required = false)String doctorDiagnosed,
            @RequestParam(required = false) LocalDate dateDiagnosed,
            @RequestParam(required = false)String description,
            @RequestParam(required = false)Integer state

    ){
        Boolean exists = medicalHistoryRepository.existsByName(name);
        if(!exists){
            throw new IllegalStateException("Medical history with name " + name + " does not exist.");
        }
        MedicalHistory medicalHistory = medicalHistoryRepository.findByName(name);

        if(newName != null && newName.length() > 0 && medicalHistory.getName() != newName){
            medicalHistory.setName(newName);
        }

        if(doctorDiagnosed != null && doctorDiagnosed.length() > 0 && medicalHistory.getDoctorDiagnosed() != doctorDiagnosed){
            medicalHistory.setDoctorDiagnosed(doctorDiagnosed);
        }

        if(dateDiagnosed != null){
            medicalHistory.setDateDiagnosed(dateDiagnosed);
        }

        if(description != null && description.length() > 0 && medicalHistory.getDescription() != description){
            medicalHistory.setDescription(description);
        }

        if(state != null && state > 0 && state != medicalHistory.getState()){
            medicalHistory.setState(state);
        }

        System.out.println("updateMedicalHistoryByName\n" + medicalHistory);
        medicalHistoryRepository.save(medicalHistory);
        return medicalHistory;
    }

    //update medical history by id
    @PutMapping("/medical_history/id={id}")
    MedicalHistory updateMedicalHistoryById(
            @PathVariable(value = "id") Integer id,
            @RequestBody MedicalHistory medicalHistory
    ){
        Boolean exists = medicalHistoryRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("Medical History with id " + id + " does not exist.");
        }
        MedicalHistory currentMedicalHistory = medicalHistoryRepository.findById(id);

        if(medicalHistory.getName() != null && medicalHistory.getName().length() > 0 && currentMedicalHistory.getName() != medicalHistory.getName()){
            currentMedicalHistory.setName(medicalHistory.getName());
        }

        if(medicalHistory.getDoctorDiagnosed() != null && medicalHistory.getDoctorDiagnosed().length() > 0 && currentMedicalHistory.getDoctorDiagnosed() != medicalHistory.getDoctorDiagnosed()){
            currentMedicalHistory.setDoctorDiagnosed(medicalHistory.getDoctorDiagnosed());
        }

        if(medicalHistory.getDateDiagnosed() != null && currentMedicalHistory.getDateDiagnosed() != medicalHistory.getDateDiagnosed()){
            currentMedicalHistory.setDateDiagnosed(medicalHistory.getDateDiagnosed());
        }

        if(medicalHistory.getDescription() != null && medicalHistory.getDescription().length() > 0 && currentMedicalHistory.getDescription() != medicalHistory.getDescription()){
            currentMedicalHistory.setDescription(medicalHistory.getDescription());
        }

        if(medicalHistory.getState() != null && medicalHistory.getState() > 0 && medicalHistory.getState() != currentMedicalHistory.getState()){
            currentMedicalHistory.setState(medicalHistory.getState());
        }

        System.out.println("updateMedicalHistoryById\n" + currentMedicalHistory);
        medicalHistoryRepository.save(currentMedicalHistory);
        return currentMedicalHistory;
    }
}
