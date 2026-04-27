import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileHandler {

    protected static final String FILE_NAME = "clinic_data.txt";

    // Function to read all data
    public static synchronized void readAllData() {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing whitespace
                if (line.isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(":");
                if (parts.length != 2) {
                   // System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String className = parts[0];
                String data = parts[1];

                try {
                    switch (className) {
                        case "Doctor":
                            Doctor.loadFromString(data);
                            break;
                        case "Patient":
                            Patient.loadFromString(data);
                            break;
                        case "Receptionist":
                            Receptionist.loadFromString(data);
                            break;
                        case "Appointment":
                            Appointment.loadFromString(data);
                            break;
                        case "Prescription":
                            Prescription.loadFromString(data);
                            break;

                        default:
                            System.err.println("Unknown class name: " + className);
                    }


                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }

            reader.close();
        }
        catch (IOException e)
        {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }

    // Function to write all data
    public static void writeAllData() {
        try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)) ; // Append to file if exists
            for (Doctor doctor : DentalClinic.Doctors) {
                writer.write("Doctor:" + doctor.toString());
                writer.newLine();
            }
            for (Patient patient : DentalClinic.Patients) {
                writer.write("Patient:" + patient.toString());
                writer.newLine();
            }
            for (Receptionist receptionist : DentalClinic.Receptionists) {
                writer.write("Receptionist:" + receptionist.toString());
                writer.newLine();
            }
            for (Appointment appointment : DentalClinic.allAppointments) {
                writer.write("Appointment:" + appointment.toString());
                writer.newLine();
            }
            for (Prescription prescription : DentalClinic.Prescriptions) {
                writer.write("Prescription:" + prescription.toString());
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.err.println("Error writing data: " + e.getMessage());
        }

    }

}
