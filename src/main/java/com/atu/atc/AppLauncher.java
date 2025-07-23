/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc;

import com.atu.atc.gui.MainFrame;

import com.atu.atc.service.AuthService;
import com.atu.atc.service.AdminService;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.service.TutorService;
import com.atu.atc.service.StudentService;

import com.atu.atc.data.AdminRepository;
import com.atu.atc.data.ReceptionistRepository;
import com.atu.atc.data.TutorRepository;
import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.ClassesRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.PaymentRepository;
import com.atu.atc.data.RequestRepository;
import com.atu.atc.data.DataLoader;
import com.atu.atc.data.DataSaver;

import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;
import javax.swing.SwingUtilities;

/**
 *
 * @author Xuan
 */

public class AppLauncher {
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> {
            System.out.println("Application starting ...");
            
            // Initialise Repositories
            AdminRepository adminRepository = new AdminRepository();
            ReceptionistRepository receptionistRepository = new ReceptionistRepository();
            TutorRepository tutorRepository = new TutorRepository();
            StudentRepository studentRepository = new StudentRepository();
            ClassesRepository classesRepository = new ClassesRepository();
            SubjectRepository subjectRepository = new SubjectRepository();
            EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
            PaymentRepository paymentRepository = new PaymentRepository();
            RequestRepository requestRepository = new RequestRepository();
            
            // Initialise Utility Classes
            IDGenerator idGenerator = new IDGenerator();
            Validator validator = new Validator();
            
            // Initialise DataLoader and DataSaver, passing all repositories
            DataLoader dataLoader = new DataLoader(studentRepository, receptionistRepository, tutorRepository,
                    classesRepository, enrollmentRepository, paymentRepository,
                    requestRepository, adminRepository, subjectRepository);
            DataSaver dataSaver = new DataSaver(studentRepository, receptionistRepository, tutorRepository,
                    classesRepository, enrollmentRepository, paymentRepository,
                    requestRepository, adminRepository, subjectRepository);
            
            // Load all data at application startup
            dataLoader.loadAllData();
            
            // Initialise Services
            AuthService authService = new AuthService(adminRepository, receptionistRepository, tutorRepository, studentRepository);

            AdminService adminService = new AdminService(adminRepository, receptionistRepository, tutorRepository,
                                                         studentRepository, classesRepository, subjectRepository,
                                                         enrollmentRepository, paymentRepository, requestRepository,
                                                         idGenerator, validator);

            ReceptionistService receptionistService = new ReceptionistService(studentRepository, receptionistRepository,enrollmentRepository,
                                                            paymentRepository,classesRepository, subjectRepository,requestRepository,
                                                            tutorRepository);

            TutorService tutorService = new TutorService(tutorRepository,subjectRepository,studentRepository,enrollmentRepository, classesRepository, idGenerator);

            StudentService studentService = new StudentService(studentRepository, enrollmentRepository, classesRepository,
                                            requestRepository, paymentRepository, subjectRepository, tutorRepository);
            
            MainFrame mainFrame = new MainFrame(authService, adminService, receptionistService, tutorService,
                    studentService, requestRepository);
            
            // Make the main application window visible
            mainFrame.setVisible(true);          
        });
    }
}
