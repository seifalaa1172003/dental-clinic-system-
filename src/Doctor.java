import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Doctor extends User {
    private String specialization;
    private List<String> availableDays;
    private List<String> availableHours;
// constructors
    public Doctor(String username, String password, String firstName, String lastName, String email, String mobileNumber,
                  String specialization, List<String> availableDays, List<String> availableHours) {
        super(username, password, firstName, lastName, email, mobileNumber);
        this.specialization = specialization;
        this.availableDays = (availableDays != null) ? new ArrayList<>(availableDays) : new ArrayList<>();
        this.availableHours = (availableHours != null) ? new ArrayList<>(availableHours) : new ArrayList<>();
        //DentalClinic.Doctors.add(this);
    }


// Setters and getters

    public String getSpecialization() {
        return specialization;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    public List<String> getAvailableDays() {

        return availableDays;
    }

    public List<String> getAvailableHours() {

        return availableHours;
    }
    public static Doctor getDoctorByName(String doctorName) {
        for (Doctor doctor : DentalClinic.Doctors) {
            if (doctor.getFullName().equalsIgnoreCase(doctorName)) {
                return doctor;
            }
        }
        return null;
    }

    public static List<Doctor> getDoctors() {

        return DentalClinic.Doctors;
    }

    public static void setDoctors(List<Doctor> doctors) {

        DentalClinic.Doctors = doctors;
    }

    // login and signup
    public static Doctor login(String email, String password) {
        return User.login(email, password, DentalClinic.Doctors);
    }


    @Override
    public boolean signup() {
        boolean exists = DentalClinic.Doctors.stream()
                .anyMatch(doctor -> doctor.getEmail().equalsIgnoreCase(this.email));
        if (exists) {
            System.out.println("A doctor with this email already exists.");
            return false;
        }

        return true;
    }

    //create prescription for patient
    public void createPrescription(Patient patient, String medication, String instruction) {
        if (patient == null || medication.trim().isEmpty() || instruction.trim().isEmpty()) {
            System.out.println("Invalid inputs. Please check the patient and prescription details.");
            return;
        }

        // Generate a unique prescription
        Prescription prescription = new Prescription(Prescription.generatePrescriptionId(),
                patient.getFullName(), this.getFullName(), new Date());

        prescription.addMedication(medication, instruction);
        Prescription.addPrescription(prescription);

        // Update the patient's history
        patient.setPatient_History(patient.updatePatient_history(prescription.getDetails()));

        System.out.println("Prescription created successfully for " + patient.getFullName());
        DentalClinic.Prescriptions.add(prescription);
    }

    // Display appointments for a specific date
    public String getPatientInfo(Patient patient) {
        try {
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found .");
            }

            return "Name: " + patient.getFirstName() + " " + patient.getLastName() + "\n" +
                    "Age: " + patient.getAge() + "\n" +
                    "Gender: " + patient.getGender() + "\n" +
                    "Blood Type: " + patient.getBloodType() + "\n" +
                    "Weight: " + patient.getWeight() + "\n" +
                    "Height: " + patient.getHeight()+"\n"+
                    "patient history:  " + patient.getPatient_History() ;

        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            return null;
        }
    }

    public String getReceptionistContact(String receptionistName) {
        if (receptionistName == null || receptionistName.isEmpty()) {
            throw new IllegalArgumentException("Invalid receptionist name.");
        }

        // Search the static list of receptionists
        for (Receptionist receptionist : Receptionist.getReceptionists()) {
            String fullName = receptionist.getFirstName() + " " + receptionist.getLastName();
            if (fullName.equalsIgnoreCase(receptionistName)) {
                return "Name: " + fullName +
                        "\nEmail: " + receptionist.getEmail() +
                        "\nMobile: " + receptionist.getMobileNumber();
            }
        }

        System.out.println("Receptionist not found.");
        return null; // Return null if no matching receptionist is found
    }


    public void changeAll(List<String> newDays, List<String> newHours) {
        try {
            if (newDays == null || newDays.isEmpty()) {
                throw new IllegalArgumentException("Available days list cannot be null or empty.");
            }
            if (newHours == null || newHours.isEmpty()) {
                throw new IllegalArgumentException("Available hours list cannot be null or empty.");
            }
            this.availableDays = new ArrayList<>(newDays);
            this.availableHours = new ArrayList<>(newHours);
            System.out.println("Availability updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    public void addAvailability(String newDay, String newHour) {
        if (newDay == null || newHour == null || newDay.isEmpty() || newHour.isEmpty()) {
            throw new IllegalArgumentException("Day and Hour cannot be null or empty.");
        }
        availableDays.add(newDay);
        availableHours.add(newHour);
        System.out.println("Availability added successfully!");
    }
    public void removeAvailabileDay(String cancelDay) {
        int index = availableDays.indexOf(cancelDay);
        if (index != -1) {
            availableDays.remove(cancelDay);
            availableHours.remove(index); // Remove corresponding hours
            System.out.println(cancelDay + " and its hours removed.");
        }
        else
        {
            System.out.println(cancelDay + " not found in the available days.");
        }

    }

    public String getDetails(Doctor doctor)
    {
     return "doctor's Name: " + doctor.getFirstName() + " " + doctor.getLastName() + "\n" +
             "doctor's Mobile: " + doctor.getMobileNumber() + "\n" +
             "doctor's specialization: " + doctor.getSpecialization() + "\n"+
             "doctor's availableDays: " + doctor.getAvailableDays() + "\n" +
             "doctor's availableHours: " + doctor.getAvailableHours() + "\n" ;
    }

    public static void displayAllDoctors() {
        if (DentalClinic.Doctors.isEmpty()) {
            System.out.println("No doctors available.");
        } else {
            System.out.println("List of Doctors:");
            for (Doctor doctor : DentalClinic.Doctors) {
                System.out.println(doctor.getDetails(doctor));
                System.out.println("----------------------");
            }
        }
    }


    public void displayAppointmentsByDate(LocalDate specificDate) {
        System.out.println("Appointments on " + specificDate + ":");

        boolean hasAppointments = false;

        for (Appointment appointment : DentalClinic.allAppointments) {
            if (appointment.getAppointmentDate().equals(specificDate)) {
                System.out.println(appointment.getDetails());
                System.out.println("-------------------------------------");
                hasAppointments = true;
            }
        }

        if (!hasAppointments) {
            System.out.println("No appointments found for this date.");
        }
    }

    public void displayAvailability() {
        List<LocalDate> dates = generateDatesForCurrentWeek();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < availableDays.size(); i++) {
            System.out.println(
                    "Date: " + dates.get(i).format(dateFormatter) +
                            " (" + availableDays.get(i) + "), Hours: " + availableHours.get(i)
            );
        }
    }


    // Doctor methods to store and load doctors data in the list that will  be passed to the reader and writer
    @Override
    public String toString() {
        return username + "|" + password + "|" + firstName + "|" + lastName + "|" + email + "|" + mobileNumber + "|" +
                specialization + "|" + String.join(",", availableDays) + "|" + String.join(",", availableHours);
    }


    public static void loadFromString(String data) {
        try {
            String[] doctorData = data.split("\\|");
            if (doctorData.length != 9) { // Expecting 9 fields
                System.out.println("ay 7aga");
                throw new IllegalArgumentException("Invalid Doctor data format: " + data);
            }
            // Create a new Doctor object
            Doctor doctor = new Doctor(
                    doctorData[0].trim(), // username
                    doctorData[1].trim(), // password
                    doctorData[2].trim(), // first name
                    doctorData[3].trim(), // last name
                    doctorData[4].trim(), // email
                    doctorData[5].trim(), // mobile
                    doctorData[6].trim(), // specialization
                    new ArrayList<>(Arrays.asList(doctorData[7].split(","))), // mutable available days
                    new ArrayList<>(Arrays.asList(doctorData[8].split(",")))  // mutable available hours
            );

            // Add the doctor to the clinic's list
            if (DentalClinic.Doctors == null) {
                DentalClinic.Doctors = new ArrayList<>();
            }
            DentalClinic.Doctors.add(doctor);

        } catch (Exception e) {
            System.err.println("Error loading Doctor data: " + e.getMessage());
            throw e; // Rethrow the exception for further handling if needed
        }
    }


    private static DayOfWeek getDayOfWeek(String dayName) {
        return switch (dayName.toLowerCase()) {
            case "monday" -> DayOfWeek.MONDAY;
            case "tuesday" -> DayOfWeek.TUESDAY;
            case "wednesday" -> DayOfWeek.WEDNESDAY;
            case "thursday" -> DayOfWeek.THURSDAY;
            case "friday" -> DayOfWeek.FRIDAY;
            case "saturday" -> DayOfWeek.SATURDAY;
            case "sunday" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Invalid day name: " + dayName);
        };
    }
    public List<LocalDate> generateDatesForCurrentWeek() {
        if (availableDays == null || availableDays.isEmpty()) {
            throw new IllegalArgumentException("No available days provided.");
        }

        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (String day : availableDays) {
            try {
                DayOfWeek dayOfWeek = getDayOfWeek(day);
                LocalDate date = today.with(dayOfWeek);
                if (date.isBefore(today)) {
                    date = date.plusWeeks(1); // Move to the next week's occurrence of the day
                }
                dates.add(date);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid day encountered: " + e.getMessage());
            }
        }

        if (dates.isEmpty()) {
            throw new IllegalArgumentException("No valid days provided for scheduling.");
        }

        return dates;
    }

    public List<Appointment> createAvailableAppointments() {
        if (availableHours == null || availableHours.isEmpty()) {
            throw new IllegalArgumentException("No available hours provided.");
        }

        List<Appointment> availableAppointments = new ArrayList<>();
        List<LocalDate> dates = generateDatesForCurrentWeek();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            String hours = availableHours.get(i); // Corresponding hours for the day

            try {
                String[] timeRange = hours.split("-");
                LocalTime startTime = LocalTime.parse(timeRange[0].trim(), timeFormatter);
                LocalTime endTime = LocalTime.parse(timeRange[1].trim(), timeFormatter);

                if (startTime.isAfter(endTime)) {
                    throw new IllegalArgumentException("Start time must be earlier than end time in range: " + hours);
                }

                // Create 30-minute slots
                while (startTime.isBefore(endTime)) {
                    LocalTime slotEndTime = startTime.plusMinutes(30);
                    if (!slotEndTime.isAfter(endTime)) {
                        Appointment appointment = new Appointment(
                                Appointment.generateAppointmentId(), // Unique ID
                                null, // Patient will be assigned later
                                this.getFullName(), // Doctor's name
                                date,
                                startTime + " - " + slotEndTime
                        );
                        availableAppointments.add(appointment);
                    }
                    startTime = slotEndTime;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format in range: " + hours + ". Use the format HH:mm.");
            }
        }

        //DentalClinic.allAppointments.addAll(availableAppointments); // Store globally
        return availableAppointments;
    }

//if we need to create available for 4 weeks
//    public List<LocalDate> generateDatesForNext4Weeks() {
//    if (availableDays == null || availableDays.isEmpty()) {
//        throw new IllegalArgumentException("No available days provided.");
//    }
//
//    List<LocalDate> dates = new ArrayList<>();
//    LocalDate today = LocalDate.now();
//
//    for (int weekOffset = 0; weekOffset < 4; weekOffset++) { // Repeat for 4 weeks
//        for (String day : availableDays) {
//            try {
//                DayOfWeek dayOfWeek = getDayOfWeek(day);
//                LocalDate date = today.with(dayOfWeek).plusWeeks(weekOffset);
//
//                // Ensure dates are in the future
//                if (date.isBefore(today)) {
//                    date = date.plusWeeks(1);
//                }
//                dates.add(date);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Invalid day encountered: " + e.getMessage());
//            }
//        }
//    }
//
//    if (dates.isEmpty()) {
//        throw new IllegalArgumentException("No valid days provided for scheduling.");
//    }
//
//    return dates;
//}
//
//
//    public List<Appointment> createAvailableAppointments() {
//        if (availableHours == null || availableHours.isEmpty()) {
//            throw new IllegalArgumentException("No available hours provided.");
//        }
//
//        List<Appointment> availableAppointments = new ArrayList<>();
//        List<LocalDate> dates = generateDatesForNext4Weeks(); // Updated to 4 weeks
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//
//        for (int i = 0; i < dates.size(); i++) {
//            LocalDate date = dates.get(i);
//            int hourIndex = i % availableHours.size(); // Cycle through available hours
//            String hours = availableHours.get(hourIndex); // Corresponding hours for the day
//
//            try {
//                String[] timeRange = hours.split("-");
//                LocalTime startTime = LocalTime.parse(timeRange[0].trim(), timeFormatter);
//                LocalTime endTime = LocalTime.parse(timeRange[1].trim(), timeFormatter);
//
//                if (startTime.isAfter(endTime)) {
//                    throw new IllegalArgumentException("Start time must be earlier than end time in range: " + hours);
//                }
//
//                // Create 30-minute slots
//                while (startTime.isBefore(endTime)) {
//                    LocalTime slotEndTime = startTime.plusMinutes(30);
//                    if (!slotEndTime.isAfter(endTime)) {
//                        Appointment appointment = new Appointment(
//                                Appointment.generateAppointmentId(), // Unique ID
//                                null, // Patient will be assigned later
//                                this.getFullName(), // Doctor's name
//                                date,
//                                startTime + " - " + slotEndTime
//                        );
//                        availableAppointments.add(appointment);
//                    }
//                    startTime = slotEndTime;
//                }
//            } catch (DateTimeParseException e) {
//                System.out.println("Invalid time format in range: " + hours + ". Use the format HH:mm.");
//            }
//        }
//
//        return availableAppointments;
//    }





}



