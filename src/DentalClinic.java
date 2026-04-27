import java.util.ArrayList;
import java.util.List;

    public abstract class DentalClinic {
    // Attributes for DentalClinic
        public static String name= "Dentalcoo";
        public static String location="Ain shams";
        public static List<Patient> Patients = new ArrayList<>();
        public static List<Doctor> Doctors = new ArrayList<>();
        public static List<Receptionist> Receptionists = new ArrayList<>();
        public static List<Appointment> allAppointments = new ArrayList<>(); // Unified list of all appointments
        public static List<Prescription> Prescriptions = new ArrayList<>();


       public static String[][] servicesWithPrices = {
                {"Examination and Diagnosis", "250.0"},
                {"Teeth Cleaning and Whitening", "1600.0"},
                {"Dental Fillings", "1400.0"},
                {"Root Canal Treatment", "2500.0"},
                {"Tooth Extraction", "1000.0"},
                {"Orthodontics", "25000.0"},
                {"Dental Crowns and Bridges", "10000.0"},
                {"Dental Implants", "30000.0"},
                {"Children's Dental Services", "800.0"}
        };


        // Constructor
    public DentalClinic(String name, String location) {
       DentalClinic.name = name;
     DentalClinic.location = location;
   }


        public static List<Doctor> getDoctors() {
            return Doctors;
        }

        public static void setDoctors(List<Doctor> doctors) {
            Doctors = doctors;
        }

        public static List<Patient> getPatients() {
            return Patients;
        }

        public static void setPatients(List<Patient> patients) {
            Patients = patients;
        }

        public static List<Receptionist> getReceptionists() {
            return Receptionists;
        }

        public static void setReceptionists(List<Receptionist> receptionists) {
            Receptionists = receptionists;
        }

        // Getters and Setters
    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        DentalClinic.name = name;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        DentalClinic.location = location;
    }

    // Add Doctor
    public static void addDoctor(Doctor doctor) {
        DentalClinic.Doctors.add(doctor);
    }

    // Remove Doctor
    public static void removeDoctor(Doctor doctor) {
        DentalClinic.Doctors.remove(doctor);
    }

    // Add Patient
    public static void addPatient(Patient patient) {
        DentalClinic.Patients.add(patient);
    }

    // Remove Patient
    public static void removePatient(Patient patient) {
        DentalClinic.Patients.remove(patient);
    }

    // Add Receptionist
    public static void addReceptionist(Receptionist receptionist) {
        DentalClinic.Receptionists.add(receptionist);
    }

    // Remove Receptionist
    public static void removeReceptionist(Receptionist receptionist) {
        DentalClinic.Receptionists.remove(receptionist);
    }

    // Display Doctors
    public  void displayDoctors() {
        System.out.println("List of Doctors:");
        for (Doctor doctor : DentalClinic.Doctors) {
            System.out.println(doctor.getDetails(doctor));
        }
    }

    // Display Patients
    public  void displayPatients() {
        System.out.println("List of Patients:");
        for (Patient patient : DentalClinic.Patients) {
            System.out.println(patient.getFullName());
        }
    }

    // Display Receptionists
    public  void displayReceptionists() {
        System.out.println("List of Receptionists:");
        for (Receptionist receptionist : DentalClinic.Receptionists) {
            System.out.println(receptionist.getDetails(receptionist));
        }
    }


}
