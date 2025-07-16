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
            
            // Initialise Utitily Classess
            IDGenerator idGenerator = new IDGenerator();
            Validator validator = new Validator();
            
            // Initialise Services
            AuthService authService = new AuthService(adminRepository, receptionistRepository, tutorRepository, studentRepository);
            AdminService adminService = new AdminService(adminRepository, receptionistRepository, tutorRepository,
                                                         studentRepository, classesRepository, subjectRepository,
                                                         enrollmentRepository, paymentRepository, requestRepository,
                                                         idGenerator, validator);
            ReceptionistService receptionistService = new ReceptionistService(receptionistRepository, studentRepository,classesRepository, 
                                                         enrollmentRepository,paymentRepository, requestRepository,idGenerator, validator);
            TutorService tutorService = new TutorService(tutorRepository, classesRepository, studentRepository,
                                                         enrollmentRepository, requestRepository);
            StudentService studentService = new StudentService(studentRepository, classesRepository, enrollmentRepository,
                                                            paymentRepository, requestRepository);
            
            MainFrame mainFrame = new MainFrame(authService, adminService, receptionistService, tutorService, studentService);
            
            // Make the main application window visible
            mainFrame.setVisible(true);          
        });
    }
}
