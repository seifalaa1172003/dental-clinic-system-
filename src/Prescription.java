import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Prescription {

    // Attributes
    private int prescriptionId;
    private String patientName;
    private String doctorName;
    private Date dateIssued;
    private List<String> medications; // List of prescribed medications
    private List<String> instructions; // Instructions for each medication


    // Constructor
    public Prescription(int prescriptionId, String patientName, String doctorName, Date dateIssued) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.dateIssued = dateIssued;
        this.medications = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    // Getters and Setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    // Add a medication with instructions
    public void addMedication(String medication, String instruction) {
        medications.add(medication);
        instructions.add(instruction);
    }

    // Remove a medication by name
    public boolean removeMedication(String medication) {
        int index = medications.indexOf(medication);
        if (index != -1) {
            medications.remove(index);
            instructions.remove(index);
            return true; // Medication removed successfully
        }
        return false; // Medication not found
    }

    protected static int generatePrescriptionId() {
        int id;
        do {
            id = (int) (Math.random() * 1000000); // Generate a random ID
        } while (isIdInUse(id)); // Check if the ID already exists in the allAppointments list
        return id;
    }

    // Helper method to check if an ID is already in use
    private static boolean isIdInUse(int id) {
        return DentalClinic.Prescriptions.stream()
                .anyMatch(prescription -> prescription.getPrescriptionId() == id);
    }

    // Display prescription details
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Prescription ID: ").append(prescriptionId)
                .append("\nPatient Name: ").append(patientName)
                .append("\nDoctor Name: ").append(doctorName)
                .append("\nDate Issued: ").append(dateIssued)
                .append("\nMedications:\n");

        for (int i = 0; i < medications.size(); i++) {
            details.append("  , ").append(medications.get(i)).append(": ").append(instructions.get(i)).append("\n");
        }

        return details.toString();
    }

    public static void addPrescription(Prescription prescription) {
        DentalClinic.Prescriptions.add(prescription);  // Add to the list
    }



    public String toString() {
        return prescriptionId + "|" + patientName + "|" + doctorName + "|" + dateIssued + "|" + medications + "|" + instructions;
    }

    public static void loadFromString(String data) {
        DentalClinic.Prescriptions.clear();
        String[] prescriptionData = data.split("\\|");
        Prescription prescription = new Prescription(
                Integer.parseInt(prescriptionData[0]), prescriptionData[1], prescriptionData[2],
                new Date(Long.parseLong(prescriptionData[3]))
        );
        for (int i = 4; i < prescriptionData.length; i++) {
            String[] medParts = prescriptionData[i].split(":");
            if (medParts.length == 2) {
                prescription.addMedication(medParts[0], medParts[1]);
            }
        }
    }


}
