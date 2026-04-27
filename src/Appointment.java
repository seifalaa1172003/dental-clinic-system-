import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Appointment {

    // Status constants
    public static final String STATUS_AVAILABLE = "available";
    public static final String STATUS_BOOKED = "booked";


    private final int appointmentId;
    protected String patientName;
    protected String doctorName;
    protected LocalDate appointmentDate;
    protected String status;
    private String time;

    // Constructors
    public Appointment(int appointmentId, String patientName, String doctorName, LocalDate appointmentDate, String time) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.time = time;
        this.status = STATUS_AVAILABLE; // Default status
    }
    public Appointment(String patientName, String doctorName, LocalDate appointmentDate, String time) {
        this.appointmentId = generateAppointmentId(); // Ensure ID is unique
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.time = time;
        this.status = STATUS_AVAILABLE; // Default status
    }

    // Getter and Setter methods
    public int getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Method to update appointment status
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    protected static int generateAppointmentId() {
        int id;
        do {
            id = (int) (Math.random() * 1000000); // Generate a random ID
        } while (isIdInUse(id)); // Check if the ID already exists in the allAppointments list
        return id;
    }

    // Helper method to check if an ID is already in use
    private static boolean isIdInUse(int id) {
        return DentalClinic.allAppointments.stream()
                .anyMatch(appointment -> appointment.getAppointmentId() == id);
    }

    // Method to display appointment details
    public String getDetails() {
        return "Appointment ID: " + appointmentId + "\nPatient Name: " + patientName +
                "\nDoctor Name: " + doctorName + "\nAppointment Date: " + appointmentDate +
                "\nTime: " + time + "\nStatus: " + status;
    }



    // Display appointments by status
    public static void displayAppointmentsByStatus(String status) {
        try {
            List<Appointment> appointments = getAppointmentsByStatus(status);
            if (appointments.isEmpty()) {
                System.out.println("No appointments found with status: " + status);
            } else {
                for (Appointment appointment : appointments) {
                    System.out.println(appointment.getDetails());
                    System.out.println("----------------------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Error displaying appointments: " + e.getMessage());
        }
    }

    // Helper method to get appointments by status
    public static List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> matchingAppointments = new ArrayList<>();

        try {
            if (status == null || status.isBlank()) {
                throw new IllegalArgumentException("Status cannot be null or empty.");
            }

            matchingAppointments = DentalClinic.allAppointments.stream()
                    .filter(appointment -> appointment.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }

        return matchingAppointments;
    }

    public static void DisplayAppointmentsByPatient(Patient patient, String status) {
        try {
            // Filter appointments by patient and status
            List<Appointment> appointments = DentalClinic.allAppointments.stream()
                    .filter(appointment -> appointment.getPatientName().equalsIgnoreCase(patient.getFullName()) && appointment.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());

            // Display the filtered appointments
            if (appointments.isEmpty()) {
                System.out.println("No appointments found for patient: " + patient.getFullName() + " with status: " + status);
            } else {
                System.out.println("Appointments for patient: " + patient.getFullName() + " with status: " + status);
                appointments.forEach(appointment -> {
                    System.out.println("Appointment ID: " + appointment.getAppointmentId());
                    System.out.println("Doctor Name: " + appointment.getDoctorName());
                    System.out.println("Date: " + appointment.getAppointmentDate());
                    System.out.println("Time: " + appointment.getTime());
                    System.out.println("Status: " + appointment.getStatus());
                    System.out.println("-----------");
                });
            }
        } catch (Exception e) {
            System.err.println("Error retrieving or displaying appointments: " + e.getMessage());
        }
    }

    public static Appointment findAppointmentById(int appointmentId) {
        try {
            if (appointmentId <= 0) {
                throw new IllegalArgumentException("Appointment ID must be positive.");
            }
            return DentalClinic.allAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentId() == appointmentId)
                    .findFirst()
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        return null;
    }




    // File handling: Save appointments to string

    public String toString() {
        return appointmentId + "|" + patientName + "|" + doctorName + "|" + appointmentDate + "|" + time + "|" + status;
    }


    // File handling: Load appointments from string
    public static void loadFromString(String data) {
        String[] lines = data.split("\n");

        DentalClinic.allAppointments.clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    throw new IllegalArgumentException("Invalid data format: " + line);
                }

                int appointmentId = Integer.parseInt(parts[0]);
                String patientName = parts[1];
                String doctorName = parts[2];
                LocalDate appointmentDate = LocalDate.parse(parts[3], formatter);
                String status = parts[4];
                String time = parts[5];

                Appointment appointment = new Appointment(appointmentId, patientName, doctorName, appointmentDate, time);
                appointment.setStatus(status);
                DentalClinic.allAppointments.add(appointment);
            } catch (Exception e) {
                System.err.println("Error processing line: " + line + " - " + e.getMessage());
            }
        }
    }
}
