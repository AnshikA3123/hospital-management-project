import java.io.*;
import java.util.Scanner;

public class DischargePatient {
    private static final String PATIENT_FILE = "C:\\Users\\91812\\Desktop\\hospital management\\Data\\patient.txt";
    private static final String ROOM_FILE = "C:\\Users\\91812\\Desktop\\hospital management\\rooms.txt";

    public static void dischargePatient(Scanner scanner) {
        String[] patients = new String[100]; // Array to store patient records
        int patientCount = 0;
        String dischargedPatientRoom = null;

        System.out.println("\n===== Patient List =====");
        System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-12s %-12s %-12s%n",
                "ID", "Name", "Age", "Gender", "Disease", "Deposit", "Room No", "Date", "Time", "Total Bill", "Remaining Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

        // Read patient records and store them in an array
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null && patientCount < patients.length) {
                // Remove extra spaces from the line and skip if it’s empty
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Splitting the line by commas, but gotta be careful so Date & Time stay together as one field
                String[] details = line.split(",", -1); 

                // Makeing sure thtat there are exactly 11 fields before we process the record
                if (details.length == 11) {
                    System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-12s %-12s %-12s%n",
                            details[0].trim(), details[1].trim(), details[2].trim(), details[3].trim(),
                            details[4].trim(), details[5].trim(), details[6].trim(),
                            details[7].trim(), details[8].trim(), details[9].trim(), details[10].trim());
                    patients[patientCount++] = line; // Store valid records
                } else {
                    System.out.println(" Skipping invalid record (incorrect fields): " + line);
                }
            }

            if (patientCount == 0) {
                System.out.println(" No patients found.");
                return;
            }
        } catch (IOException e) {
            System.out.println(" Error reading patient data: " + e.getMessage());
            return;
        }

        // Ask user for Patient ID to discharge
        System.out.print("\nEnter Patient ID to discharge: ");
        String patientId = scanner.nextLine().trim();
        boolean found = false;
        String[] updatedPatients = new String[patientCount];
        int updatedCount = 0;

        // Loop through the patient array and remove the selected patient
        for (int i = 0; i < patientCount; i++) {
            String[] details = patients[i].split(",");
            if (details.length == 11 && details[0].trim().equals(patientId)) {
                dischargedPatientRoom = details[6].trim(); // Store room number
                found = true;
            } else {
                updatedPatients[updatedCount++] = patients[i]; // Keep other patients
            }
        }

        if (!found) {
            System.out.println(" No patient found with ID: " + patientId);
            return;
        }

        // Update patient file (Remove discharged patient)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENT_FILE))) {
            for (int i = 0; i < updatedCount; i++) {
                writer.write(updatedPatients[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Error updating patient records: " + e.getMessage());
        }

        // Mark the room as Available
        if (dischargedPatientRoom != null) {
            updateRoomStatus(dischargedPatientRoom);
        }

        System.out.println(" Patient with ID " + patientId + " has been discharged successfully!");
    }

    private static void updateRoomStatus(String roomNumber) {
        String[] rooms = new String[100]; // Array to store room data
        int roomCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null && roomCount < rooms.length) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    if (details[0].trim().equals(roomNumber)) {
                        details[1] = "Available"; // Update room status
                        line = String.join(",", details);
                    }
                }
                rooms[roomCount++] = line;
            }
        } catch (IOException e) {
            System.out.println(" Error reading room data: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOM_FILE))) {
            for (int i = 0; i < roomCount; i++) {
                writer.write(rooms[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating room records: " + e.getMessage());
        }
    }
}