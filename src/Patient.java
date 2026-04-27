import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient extends User {

    private String Patient_History;
    private int Age;
    private String Gender;
    private String Blood_Type;
    private double Weight;
    private double Height;


    public Patient(String username, String password, String email, String firstname, String lastname, String mobileNumber, String Patient_History, int Age, String Gender, String Blood_Type, double Weight, double Height) {
        super(username, password, firstname, lastname, email, mobileNumber);
        this.Patient_History = Patient_History;
        this.Age = Age;
        this.Gender = Gender;
        this.Blood_Type = Blood_Type;
        this.Height = Height;
        this.Weight = Weight;
        //DentalClinic.Patients.add(this);
    }



    public String getBloodType() {
        return Blood_Type;
    }
    public void setBlood_Type(String blood_Type) {
        this.Blood_Type = blood_Type;
    }

    public int getAge() {
        return Age;
    }
    public void setAge(int age) {
        this.Age = age;
    }

    public String getFullName() {
        return firstName + lastName;
    }

    public static List<Patient> getPatients() {

        return DentalClinic.Patients;
    }

    public String getPatient_History() {
        return Patient_History;
    }
    public void setPatient_History(String patient_History) {

        this.Patient_History = patient_History;
    }


    public String getGender() {
        return Gender;
    }
    public void setGender(String gender) {
        this.Gender = gender;
    }




    public void setWeight(double weight) {
        this.Weight = weight;
    }
    public double getWeight() {
        return Weight;
    }



    public void setHeight(double height) {
        Height = height;
    }
    public double getHeight() {
        return Height;
    }



    // login and signup

    public static Patient login(String email, String password) {
        return User.login(email, password, DentalClinic.Patients);
    }
    @Override
    public boolean signup() {
        boolean exists = DentalClinic.Patients.stream().anyMatch(patient -> patient.getEmail().equalsIgnoreCase(this.email));
        if (exists) {
            System.out.println("A patient with this email already exists.");
            return false;
        }
        return true;
    }

    // Update data methods
    public void UpdateHeight(double newHeight) {

        setHeight(newHeight);
    }
    public void UpdateWeight(double newWeight) {

        setWeight(newWeight);
    }
    public void UpdateEmail(String newEmail) {

        setEmail(newEmail);
    }
    public void UpdateMobileNumber(String newMobileNumber) {

        setMobileNumber(newMobileNumber);
    }
    public String updatePatient_history(String newData)
    {
        Patient_History=getPatient_History()+"\n" +newData;
        return Patient_History;
    }


    public void displayAvailableAppointments() {
        System.out.println("Available Appointments:");
        List<Appointment> availableAppointments = Appointment.getAppointmentsByStatus(Appointment.STATUS_AVAILABLE);

        if (availableAppointments == null || availableAppointments.isEmpty()) {
            System.out.println("No available appointments.");
        } else {
            for (Appointment appointment : availableAppointments) {
                System.out.println(appointment.getDetails());
            }
        }
    }

    //request an appointment
    public void reserveAppointment(int appointmentID, String doctorName, LocalDate date, String time) {
        // Find the appointment by its ID
        Appointment appointment = Appointment.findAppointmentById(appointmentID);

        if (appointment != null) {
            // Update the appointment's data
            appointment.setPatientName(this.getFullName());
            appointment.updateStatus(Appointment.STATUS_BOOKED); // Set status to booked

            System.out.println("Appointment booked with Dr. " + doctorName + " on " + date + " at " + time);
        } else {
            System.out.println("Appointment with ID " + appointmentID + " not found.");
        }
    }

    public void cancelRequestedAppointment(int appointmentID) {
        // Find the appointment by its ID
        Appointment appointment = Appointment.findAppointmentById(appointmentID);

        // Validate if the appointment exists and has the requested status
        if (appointment == null || !appointment.getStatus().equals(Appointment.STATUS_BOOKED)) {
            System.out.println("No valid requested appointment to cancel.");
            return;
        }

        appointment.setPatientName(null);
        appointment.setStatus(Appointment.STATUS_AVAILABLE);

        System.out.println("Booked appointment on " + appointment.getAppointmentDate() + " at " + appointment.getTime() + " has been canceled and is now available again.");
    }


    // Method to search for a doctor by name or mobile number

    public void searchDoctorByName(String firstName, String lastName, List<Doctor> doctors) {
        try {
            boolean found = false;

            for (Doctor doctor : doctors) {
                if (doctor.getFirstName().equalsIgnoreCase(firstName) && doctor.getLastName().equalsIgnoreCase(lastName)) {
                    System.out.println("Doctor Found:");
                    System.out.println("Name: " + doctor.getFirstName() + " " + doctor.getLastName());
                    System.out.println("Specialization: " + doctor.getSpecialization());
                    System.out.println("Mobile: " + doctor.getMobileNumber());
                    System.out.println("---------------------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No doctor found with the name: " + firstName + " " + lastName);
            }
        } catch (Exception e) {
            System.err.println("Error searching for doctor by name: " + e.getMessage());
        }
    }


    public void searchDoctorByMobileNumber(String mobileNumber, List<Doctor> doctors) {
        try {
            boolean found = false;

            for (Doctor doctor : doctors) {
                if (doctor.getMobileNumber().equals(mobileNumber)) {
                    System.out.println("Doctor Found:");
                    System.out.println("Name: " + doctor.getFirstName() + " " + doctor.getLastName());
                    System.out.println("Specialization: " + doctor.getSpecialization());
                    System.out.println("Mobile: " + doctor.getMobileNumber());
                    System.out.println("---------------------");
                    found = true;
                    break; // Exit loop since mobile numbers are unique
                }
            }

            if (!found) {
                System.out.println("No doctor found with the mobile number: " + mobileNumber);
            }
        } catch (Exception e) {
            System.err.println("Error searching for doctor by mobile number: " + e.getMessage());
        }
    }
    //method to Check Prices for Appointments
    public  void displayServices() {
        for (int i = 0; i < DentalClinic.servicesWithPrices.length; i++) {
            System.out.printf("Service: %s, Price: %s%n", DentalClinic.servicesWithPrices[i][0], DentalClinic.servicesWithPrices[i][1]);
        }
    }

    // Save patients to file
    @Override
    public String toString() {
        return username + "|" + password + "|" + email + "|" + firstName + "|" + lastName + "|" + mobileNumber + "|" +
                Patient_History + "|" + Age + "|" + Gender + "|" + Blood_Type + "|" + Weight + "|" + Height;
    }

    public static void loadFromString(String data) {
        try {
            DentalClinic.Patients.clear();
            String[] patientData = data.split("\\|");
            if (patientData.length != 12) {
                throw new IllegalArgumentException("Invalid patients data format: " + data);
            }

            // Create a new Receptionist object
            Patient patient = new Patient(
                    patientData[0],//username
                    patientData[1],//password
                    patientData[2],//email
                    patientData[3],//first name
                    patientData[4],//last name
                    patientData[5], //mobile number
                    patientData[6],//Patient_History
                    Integer.parseInt(patientData[7]),//Age
                    patientData[8],//Gender
                    patientData[9], //Blood_Type,
                    Double.parseDouble(patientData[10]),//Weight
                    Double.parseDouble(patientData[11])//Height
            );

            // Add the receptionist to the clinic's list
            if (DentalClinic.Patients == null) {
                DentalClinic.Patients = new ArrayList<>();
            }
            DentalClinic.Patients.add(patient);

        } catch (Exception e) {
            System.err.println("Error loading Patient data: " + e.getMessage());
            throw e; // Rethrow the exception for further handling if needed
        }
    }


}