import java.util.ArrayList;
import java.util.List;

public class Receptionist extends User {
    // attributes
    private int age;
    private String gender;


    public Receptionist(String username, String password, String firstName, String lastName, String email, String mobileNumber, int age, String gender) {
        super(username, password, firstName, lastName, email, mobileNumber);
        this.age = age;
        this.gender = gender;
        //DentalClinic.Receptionists.add(this);
    }

    // Getters and Setters

    public static void setReceptionists(List<Receptionist> receptionists) {
        DentalClinic.Receptionists = receptionists;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public static List<Receptionist> getReceptionists() {
        return  DentalClinic.Receptionists;
    }

// login and signup

    public static Receptionist login(String email, String password) {
        return User.login(email, password, DentalClinic.Receptionists);
    }


    @Override
    public boolean signup() {
        boolean exists =  DentalClinic.Receptionists.stream()
                .anyMatch(receptionist -> receptionist.getEmail().equalsIgnoreCase(this.email));
        if (exists) {
            System.out.println("A receptionist with this email already exists.");
            return false;
        }
        return true;
    }


    // Update data method
    public void updateEmail(String newEmail) {

        setEmail(newEmail);
}
    public void updateMobileNumber( String newMobileNumber) {

        setMobileNumber(newMobileNumber);
}


    // Function to reserve an appointment
    public void ReserveAppointmentForPatient (String PatientName,int appointmentId) {
        Appointment appointment = Appointment.findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setPatientName(PatientName);
            appointment.updateStatus(Appointment.STATUS_BOOKED);
            System.out.println("Appointment reserved successfully for " + appointment.getPatientName());
            System.out.println(appointment.getDetails());
        }
        else
        {
            System.out.println("Error cannot reserve appointment ");
        }
    }

    // Function to cancel a reservation
    public void cancelPatientReservation(int appointmentId) {
        Appointment appointment = Appointment.findAppointmentById(appointmentId);
        if (appointment != null) {
            System.out.println("Reservation canceled for " + appointment.getPatientName());
            appointment.setPatientName(null);
            appointment.setStatus(Appointment.STATUS_AVAILABLE);
            System.out.println(appointment.getDetails());
        }
        else
        {
            System.out.println("No matching requested appointment found.");
        }
    }

    public String getDetails(Receptionist receptionist) {
        return "Receptionist's Name: " + receptionist.getFirstName() + " " + receptionist.getLastName() + "\n" +
                "Mobile: " + receptionist.getMobileNumber() + "\n" +
                "Email: " + receptionist.getEmail() + "\n" +
                "Age: " + receptionist.getAge() + "\n" +
                "Gender: " + receptionist.getGender();
    }

    @Override
    public String toString() {
        return username + "|" + password + "|" + firstName + "|" + lastName + "|" + email + "|" + mobileNumber + "|" + age + "|" + gender;
    }

    public static void loadFromString(String data) {
        try {

            String[] receptionistData = data.split("\\|");
            if (receptionistData.length != 8) { // Expecting 8 fields
                throw new IllegalArgumentException("Invalid Receptionist data format: " + data);
            }

            // Create a new Receptionist object
            Receptionist receptionist = new Receptionist(
                    receptionistData[0], // username
                    receptionistData[1], // password
                    receptionistData[2], // first name
                    receptionistData[3], // last name
                    receptionistData[4], // email
                    receptionistData[5], // mobile
                    Integer.parseInt(receptionistData[6]), // age
                    receptionistData[7]  // gender
            );

            DentalClinic.Receptionists.add(receptionist);

        } catch (Exception e) {
            System.err.println("Error loading Receptionist data: " + e.getMessage());
            throw e; // Rethrow the exception for further handling if needed
        }
    }





}





