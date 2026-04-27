import java.io.File;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        FileHandler.readAllData();
        Scanner scanner = new Scanner(System.in);
        System.out.println(" \t\t\t\t\t\t\t\t " + "Welcome to  *" + DentalClinic.getName() + "*");

        boolean isRunning = true;

        while (isRunning) {
            // Login or Signup process
            System.out.println("\n1. Login");
            System.out.println("2. Signup");
            System.out.print("Choose an option: ");
            int option = getValidatedInteger(scanner);

            switch (option) {
                case 1: // Login
                    boolean loginSuccessful = false;
                    while (!loginSuccessful) {
                        System.out.print("Email: ");
                        String loginEmail = getValidEmail(scanner);
                        System.out.print("Password: ");
                        String loginPassword = scanner.nextLine();
                        System.out.print("Role (Doctor, Patient, Receptionist): ");
                        String loginRole = scanner.nextLine();

                        switch (loginRole.toLowerCase()) {
                            case "doctor":
                                Doctor loggedInDoctor = Doctor.login(loginEmail, loginPassword);
                                if (loggedInDoctor != null) {
                                    System.out.println("Login successful! Welcome, Dr. " + loggedInDoctor.getFirstName());
                                    doctorMenu(loggedInDoctor, scanner);
                                    loginSuccessful = true; // Exit the loop
                                } else
                                {
                                    System.out.println("Invalid email or password. Please try again.");
                                }
                                break;

                            case "patient":
                                Patient loggedInPatient = Patient.login(loginEmail, loginPassword);
                                if (loggedInPatient != null) {
                                    System.out.println("Login successful! Welcome, " + loggedInPatient.getFirstName());
                                    patientMenu(loggedInPatient, scanner);
                                    loginSuccessful = true; // Exit the loop
                                }
                                else
                                {
                                    System.out.println("Invalid email or password. Please try again.");
                                }
                                break;

                            case "receptionist":
                                Receptionist loggedInReceptionist = Receptionist.login(loginEmail, loginPassword);
                                if (loggedInReceptionist != null) {
                                    System.out.println("Login successful! Welcome, " + loggedInReceptionist.getFirstName());
                                    receptionistMenu(loggedInReceptionist, scanner);
                                    loginSuccessful = true; // Exit the loop
                                } else {
                                    System.out.println("Invalid email or password. Please try again.");
                                }
                                break;

                            default:
                                System.out.println("Invalid role. Please enter Doctor, Patient, or Receptionist.");
                                break;
                        }

                        // Option to exit login if login fails
                        if (!loginSuccessful) {
                            System.out.println("\nDo you want to try logging in again? (yes/no)");
                            String retryChoice = scanner.nextLine();
                            if (!retryChoice.equalsIgnoreCase("yes")) {
                                break; // Exit the login loop
                            }
                        }
                    }
                    break;

                case 2: // Signup
                    System.out.print("Enter First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String signupEmail = getValidEmail(scanner);
                    System.out.print("Enter Password: ");
                    String signupPassword = scanner.nextLine();
                    System.out.print("Enter Mobile Number: ");
                    String mobileNumber = getValidEgyptianMobileNumber(scanner);
                    System.out.print("Enter Username: ");
                    String signupUsername = scanner.nextLine();

                    System.out.print("Role (Doctor, Patient, Receptionist): ");
                    String signupRole = scanner.nextLine();

                    switch (signupRole.toLowerCase()) {
                        case "doctor":
                            System.out.print("Enter Specialization: ");
                            String specialization = scanner.nextLine();

                            // Validate available days
                            List<String> availableDays;
                            while (true) {
                                System.out.print("Enter Available Days (comma-separated, e.g., sunday,monday): ");
                                String days = scanner.nextLine();
                                try {
                                    availableDays = validateDays(days); // Call validateDays for validation
                                    break;
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid format for days: " + e.getMessage());
                                }
                            }

                            // Validate available hours
                            List<String> availableHours;
                            while (true) {
                                System.out.print("Enter Available Hours (comma-separated time ranges HH:mm-HH:mm, e.g., 09:00-11:00): ");
                                String hours = scanner.nextLine();
                                try {
                                    availableHours = validateHours(hours); // Call validateHours for validation
                                    break;
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid format for hours: " + e.getMessage());
                                }
                            }

                            // Create the doctor object
                            Doctor newDoctor = new Doctor(
                                    signupUsername, signupPassword, firstName, lastName,
                                    signupEmail, mobileNumber, specialization, availableDays, availableHours
                            );

                            // Check for signup and add the doctor
                            if (newDoctor.signup()) {
                                DentalClinic.addDoctor(newDoctor); // Add doctor to clinic list
                                System.out.println("Doctor account created successfully!");

                                // Create and add appointments for the doctor
                                List<Appointment> appointments = newDoctor.createAvailableAppointments();
                                DentalClinic.allAppointments.addAll(appointments); // Add to global list
                                System.out.println("Appointments created successfully for the doctor.");

                            } else {
                                System.out.println("Error in creating Doctor account.");
                            }
                            break;


                        case "patient":
                            try {
                                System.out.print("Enter Age: ");
                                int age = getValidatedInteger(scanner);
                                System.out.print("Enter Gender: ");
                                String gender = getValidGender();
                                System.out.print("Enter Height: ");
                                double height = Double.parseDouble(scanner.nextLine());
                                System.out.print("Enter Weight: ");
                                double weight = Double.parseDouble(scanner.nextLine());
                                System.out.print("Enter Blood Type: ");
                                String bloodType = getValidBloodType();


                                Patient newPatient = new Patient(signupUsername, signupPassword, firstName, lastName, signupEmail, mobileNumber, "", age, gender, bloodType, weight, height);
                                if (newPatient.signup()) {
                                    DentalClinic.addPatient(newPatient);
                                    System.out.println("Patient account created successfully!");
                                } else {
                                    System.out.println("Error in creating Patient account.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter numeric values for Age, Height, and Weight.");
                            }
                            break;

                        case "receptionist":
                            try {
                                System.out.print("Enter Age: ");
                                int age = getValidatedInteger(scanner);
                                System.out.print("Enter Gender: ");
                                String gender = getValidGender();

                                Receptionist newReceptionist = new Receptionist(signupUsername, signupPassword, firstName, lastName, signupEmail, mobileNumber, age, gender);
                                if (newReceptionist.signup()) {
                                    DentalClinic.addReceptionist(newReceptionist);
                                    System.out.println("Receptionist account created successfully!");
                                } else {
                                    System.out.println("Error in creating Receptionist account.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter numeric values for Age.");
                            }
                            break;

                        default:
                            System.out.println("Invalid role. Please try again.");
                    }
            }
            // Exit or continue
            System.out.println("\nDo you want to continue? (yes/no)");
            String choice = scanner.nextLine();
            isRunning = choice.equalsIgnoreCase("yes");

        }

        // Save data before exiting
        FileHandler.writeAllData();
        System.out.println("Goodbye! Data saved successfully.");
    }



    private static void doctorMenu(Doctor doctor, Scanner scanner) {
        boolean isDoctorMenuRunning = true;
        while (isDoctorMenuRunning) {

            System.out.println("\nDoctor Menu:");
            System.out.println("1. Create Prescription for Patients");
            System.out.println("2. Show All Appointments for a Specific Day");
            System.out.println("3. Get Contact Information of Receptionist");
            System.out.println("4. Get Patient Information");
            System.out.println("5. Change Availability");
            System.out.println("6. Logout");

            int choice = getValidatedInteger(scanner);
            switch (choice) {
                case 1:
                    createPrescription(doctor, scanner);
                    break;
                case 2:
                    showAppointments(doctor, scanner);
                    break;
                case 3:
                    getReceptionistContact(doctor, scanner);
                    break;
                case 4:
                    getPatientInfo(doctor, scanner);
                    break;
                case 5:
                    changeAvailability(doctor, scanner);
                    break;
                case 6:
                    isDoctorMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createPrescription(Doctor doctor, Scanner scanner) {
        System.out.print("Enter Patient Full Name: ");
        String patientName = scanner.nextLine().trim(); // Trim spaces for better matching

        System.out.print("Enter Medication: ");
        String medication = scanner.nextLine().trim();

        System.out.print("Enter Instruction: ");
        String instruction = scanner.nextLine().trim();

        // Find patient in the clinic's list
        Patient patient = DentalClinic.Patients.stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(patientName))
                .findFirst()
                .orElse(null);

        if (patient != null) {
            doctor.createPrescription(patient, medication, instruction);

        } else {
            System.out.println("Patient not found. Please check the name or try again.");
        }
    }

    private static void showAppointments(Doctor doctor, Scanner scanner) {
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine().trim();
        // Validate date format
        LocalDate date;
        try {
            date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch ( DateTimeException e) {
            System.out.println("Invalid date format or non-existent date. Please enter the date in YYYY-MM-DD format.");
            return;
        }
        // Call the doctor's method to display appointments
        doctor.displayAppointmentsByDate(date);
    }

    private static void getReceptionistContact(Doctor doctor, Scanner scanner) {
        System.out.print("Enter Receptionist Full Name: ");
        String receptionistName = scanner.nextLine();
        String contactInfo = doctor.getReceptionistContact(receptionistName);
        if (contactInfo != null) {
            System.out.println(contactInfo);
        }
    }

    private static void getPatientInfo(Doctor doctor, Scanner scanner) {
        System.out.print("Enter Patient Full Name: ");
        String patientName = scanner.nextLine();
        Patient patient =  DentalClinic.Patients.stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(patientName))
                .findFirst().orElse(null);

        if (patient != null) {
            System.out.println(doctor.getPatientInfo(patient));
        } else {
            System.out.println("Patient not found.");
        }
    }
    public static void changeAvailability(Doctor doctor, Scanner scanner) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nCurrent weekly Availability:");
            doctor.displayAvailability();

            System.out.println("\nOptions:");
            System.out.println("1. Add Day");
            System.out.println("2. Delete Day");
            System.out.println("3. Change All Days and Hours");
            System.out.println("4. Edit Hours for Specific Day");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice;

            try {
                choice = getValidatedInteger(scanner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    addDayAndHours(doctor, scanner);
                    break;
                case 2:
                    deleteDay(doctor, scanner);
                    break;
                case 3:
                    changeAll(doctor, scanner);
                    break;
                case 4:
                    editHoursForDay(doctor, scanner);
                    break;
                case 5:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // Update available appointments after any change
        updateDoctorAppointments(doctor);
    }
    private static void updateDoctorAppointments(Doctor doctor) {
        // Delete old appointments
        DentalClinic.allAppointments.removeIf(appointment -> appointment.getDoctorName().equals(doctor.getFullName()));

        // Create new appointments
        List<Appointment> newAppointments = doctor.createAvailableAppointments();
        DentalClinic.allAppointments.addAll(newAppointments);
    }
    private static void changeAll(Doctor doctor, Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter New Available Days (comma separated): ");
                String days = scanner.nextLine();
                List<String> newDays = validateDays(days);

                System.out.print("Enter New Available Hours (comma separated): ");
                String hours = scanner.nextLine();
                List<String> newHours = validateHours(hours);

                doctor.changeAll(newDays, newHours);
                System.out.println("Availability updated successfully.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private static void addDayAndHours(Doctor doctor, Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter new day to add: ");
                String newDay = scanner.nextLine();
                List<String> validatedDays = validateDays(newDay);

                if (!doctor.getAvailableDays().contains(validatedDays.get(0))) {
                    System.out.print("Enter hours for " + newDay + " (comma separated, e.g., 09:00-12:00): ");
                    String newHours = scanner.nextLine();
                    List<String> validatedHours = validateHours(newHours);

                    doctor.addAvailability(validatedDays.get(0), String.join(",", validatedHours));
                    System.out.println(newDay + " and its hours added successfully.");
                } else {
                    System.out.println(newDay + " is already in the available days.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void deleteDay(Doctor doctor, Scanner scanner) {
        System.out.print("Enter day to delete: ");
        String deleteDay = scanner.nextLine();
        if (doctor.getAvailableDays().contains(deleteDay.toLowerCase())) {
            doctor.removeAvailabileDay(deleteDay.toLowerCase());
            System.out.println(deleteDay + " removed successfully.");
        } else {
            System.out.println(deleteDay + " not found in available days.");
        }
    }

    private static void editHoursForDay(Doctor doctor, Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter day to edit hours: ");
                String editDay = scanner.nextLine();

                if (doctor.getAvailableDays().contains(editDay.toLowerCase())) {
                    System.out.print("Enter new hours (comma separated, e.g., 09:00-12:00): ");
                    String newHours = scanner.nextLine();
                    List<String> validatedHours = validateHours(newHours);

                    int editIndex = doctor.getAvailableDays().indexOf(editDay.toLowerCase());
                    doctor.getAvailableHours().set(editIndex, String.join(",", validatedHours));
                    System.out.println(editDay + " hours updated successfully.");
                    break;
                } else {
                    System.out.println(editDay + " not found in available days.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }



    private static void patientMenu(Patient patient, Scanner scanner) {
        boolean isPatientMenuRunning = true;
        while (isPatientMenuRunning) {
            System.out.println("\nPatient Menu:");
            System.out.println("1. Change Email, Mobile Number, Weight, Height");
            System.out.println("2. Reserve an Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Check Prices for Appointments");
            System.out.println("5. Search for Doctor");
            System.out.println("6. Display Available Appointments");
            System.out.println("7. Logout");

            int choice = getValidatedInteger(scanner);
            switch (choice) {
                case 1:
                    updatePatientDetails(patient, scanner);
                    break;
                case 2:
                    reserveAppointment(patient, scanner);
                    break;
                case 3:
                    cancelAppointment(patient, scanner);
                    break;
             case 4:
                   patient.displayServices();
                    break;
                case 5:
                    searchDoctor(patient, scanner);
                    break;
                case 6:
                    patient.displayAvailableAppointments();
                    break;
                case 7:
                    isPatientMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void updatePatientDetails(Patient patient, Scanner scanner) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Update Patient Details:");
            System.out.println("1. Update Email");
            System.out.println("2. Update Mobile Number");
            System.out.println("3. Update Height");
            System.out.println("4. Update Weight");
            System.out.println("5. Exit ");

            System.out.print("Choose an option: ");
            int update;

            try {
                update = getValidatedInteger(scanner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (update) {
                case 1:
                    System.out.print("Enter New Email: ");
                    String newEmail = getValidEmail(scanner);
                    patient.UpdateEmail(newEmail);
                    break;
                case 2:
                    System.out.print("Enter New Mobile Number: ");
                    String newMobileNumber = getValidEgyptianMobileNumber(scanner);
                    patient.UpdateMobileNumber(newMobileNumber);
                    break;
                case 3:
                    System.out.print("Enter New Height: ");
                    double newHeight = Double.parseDouble(scanner.nextLine());
                    patient.UpdateHeight(newHeight);
                    break;
                case 4:
                    System.out.print("Enter New Weight: ");
                    double newWeight = Double.parseDouble(scanner.nextLine());
                    patient.UpdateWeight(newWeight);
                    break;
                case 5:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void reserveAppointment(Patient patient, Scanner scanner) {
        Appointment.displayAppointmentsByStatus(Appointment.STATUS_AVAILABLE);
        System.out.print("Enter Doctor Name: ");
        String doctorName = scanner.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Time: ");
        String time = scanner.nextLine();
        System.out.print("Enter appointment ID: ");
        int Id = getValidatedInteger(scanner);
        patient.reserveAppointment( Id ,doctorName, date, time);
    }

    private static void cancelAppointment(Patient patient, Scanner scanner) {
        Appointment.DisplayAppointmentsByPatient(patient,Appointment.STATUS_BOOKED);
        System.out.print("Enter appointment ID: ");
        int Id = getValidatedInteger(scanner);
        patient.cancelRequestedAppointment(Id);
    }

    private static void searchDoctor(Patient patient, Scanner scanner) {
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("Choose the search option:");
            System.out.println("1. Search by  Name");
            System.out.println("2. Search by Mobile Number");
            System.out.print("Enter your choice: ");

            int choice = getValidatedInteger(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter Doctor's First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Doctor's Last Name: ");
                    String lastName = scanner.nextLine();
                    patient.searchDoctorByName(firstName, lastName, DentalClinic.Doctors);
                    validChoice = true;
                    break;
                case 2:
                    System.out.print("Enter Doctor's Mobile Number: ");
                    String mobileNumber = getValidEgyptianMobileNumber(scanner);
                    patient.searchDoctorByMobileNumber( mobileNumber,  DentalClinic.Doctors);
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option (1, 2, or 3).");
                    break;
            }
        }
    }

    private static void receptionistMenu(Receptionist receptionist, Scanner scanner) {
        boolean isReceptionistMenuRunning = true;
        while (isReceptionistMenuRunning) {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. Change Email, Mobile Number");
            System.out.println("2. Reserve an Appointment for Patient");
            System.out.println("3. Cancel Reservation for Patient");
            System.out.println("4. Logout");

            int choice = getValidatedInteger(scanner);
            switch (choice) {
                case 1:
                    updateReceptionistDetails(receptionist, scanner);
                    break;
                case 2:
                    reserveAppointmentForPatient(receptionist, scanner);
                    break;
                case 3:
                    cancelReservationForPatient(receptionist, scanner);
                    break;
                case 4:
                    isReceptionistMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void updateReceptionistDetails(Receptionist receptionist, Scanner scanner) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nUpdate Receptionist Details:");
            System.out.println("1. Update Email");
            System.out.println("2. Update Mobile Number");
            System.out.println("3. Exit to Main Menu");

            System.out.print("Choose an option: ");
            int update;

            try {
                update = getValidatedInteger(scanner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                continue;
            }

            switch (update) {
                case 1:
                    System.out.print("Enter New Email: ");
                    String newEmail = getValidEmail(scanner);
                    receptionist.updateEmail(newEmail);
                    break;
                case 2:
                    System.out.print("Enter New Mobile Number: ");
                    String newMobileNumber = getValidEgyptianMobileNumber(scanner);
                    receptionist.updateMobileNumber(newMobileNumber);
                    break;
                case 3:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }


    private static void reserveAppointmentForPatient(Receptionist receptionist, Scanner scanner)
    {
        Appointment.displayAppointmentsByStatus(Appointment.STATUS_AVAILABLE);
        System.out.print("Enter appointment ID: ");
        int appointmentId = getValidatedInteger(scanner);
        System.out.print("Enter Patient Name: ");
        String PatientName = scanner.nextLine();
        receptionist.ReserveAppointmentForPatient(PatientName ,appointmentId);
    }

    private static void cancelReservationForPatient(Receptionist receptionist, Scanner scanner) {
        Appointment.displayAppointmentsByStatus(Appointment.STATUS_BOOKED);
        System.out.print("Enter appointment ID: ");
        int appointmentId = getValidatedInteger(scanner);
        receptionist.cancelPatientReservation(appointmentId);
    }

// validate email mobile number
private static String getValidEmail(Scanner scanner) {
    String email;
    while (true) {
        email = scanner.nextLine();
        if (email.contains("@") && email.contains(".com")) {
            break;
        } else {
            System.out.print("Invalid email format. Please enter a valid email: ");
        }
    }
    return email;
}

private static String getValidEgyptianMobileNumber(Scanner scanner) {
        String mobileNumber;
        while (true) {
            mobileNumber = scanner.nextLine();
            if (Pattern.matches("^01[0-9]{9}$", mobileNumber)) { // Egyptian mobile number pattern
                break;
            } else {
                System.out.print("Invalid Egyptian mobile number. Please enter a valid number (e.g., 01123796563): ");
            }
        }
        return mobileNumber;
    }
//available days and hours format validation
    private static List<String> validateDays(String days) {
        String[] validDays = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        List<String> validDaysList = List.of(validDays);

        String[] inputDays = days.toLowerCase().split(",");
        List<String> validatedDays = new ArrayList<>();

        for (String day : inputDays) {
            day = day.trim();
            if (!validDaysList.contains(day)) {
                throw new IllegalArgumentException("Invalid day: " + day + ". Valid days are: " + String.join(", ", validDays));
            }
            validatedDays.add(day);
        }

        return validatedDays;
    }
    private static List<String> validateHours(String hours) {
        String[] inputHours = hours.split(",");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<String> validatedHours = new ArrayList<>();

        for (String hourRange : inputHours) {
            hourRange = hourRange.trim();
            String[] times = hourRange.split("-");

            if (times.length != 2) {
                throw new IllegalArgumentException("Invalid time range format: " + hourRange + ". Use the format HH:mm-HH:mm.");
            }

            try {
                LocalTime startTime = LocalTime.parse(times[0].trim(), timeFormatter);
                LocalTime endTime = LocalTime.parse(times[1].trim(), timeFormatter);

                if (startTime.isAfter(endTime)) {
                    throw new IllegalArgumentException("Start time must be earlier than end time in range: " + hourRange);
                }

                validatedHours.add(hourRange); // Add valid hour range
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid time format in range: " + hourRange + ". Use the format HH:mm.");
            }
        }

        return validatedHours;
    }
// blood type validation
    private static String getValidBloodType() {
    String[] validBloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    String bloodType = "";

    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.print("Enter Blood Type (e.g., A+, O-, AB+): ");
        String input = scanner.nextLine().toUpperCase();
        if (isValidBloodType(input, validBloodTypes)) {
            bloodType = input;
            break;
        } else {
            System.out.println("Invalid blood type. Please try again.");
        }
    }

    return bloodType;
}
    private static boolean isValidBloodType(String input, String[] validBloodTypes) {
        for (String type : validBloodTypes) {
            if (type.equals(input)) {
                return true;
            }
        }
        return false;
    }

    //Gender validation
    private static String getValidGender() {
    String[] validGenders = {"Male", "Female"};
    String gender = "";

    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.print("Enter Gender (Male/Female): ");
        String input = scanner.nextLine().trim();
        if (isValidGender(input, validGenders)) {
            gender = input;
            break; // Valid gender entered
        } else {
            System.out.println("Invalid gender. Please try again.");
        }
    }

    return gender;
}
    private static boolean isValidGender(String input, String[] validGenders) {
        for (String g : validGenders) {
            if (g.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    // Function to get a validated integer input
    public static int getValidatedInteger(Scanner scanner) {
        int validInteger = -1;
        boolean valid = false;

        while (!valid) {

            String input = scanner.nextLine();

            try {
                validInteger = Integer.parseInt(input);
                valid = true;
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        return validInteger;
    }

}
