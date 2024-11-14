package hms.users;

import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User {
    private String specialization;
    private ArrayList<Appointment> patientRequests;
    private ArrayList<Appointment> upcomingAppointments;
    private ArrayList<Appointment> declinedAppointments;
    private String[][][] schedule = new String[12][31][10]; // [Month][Day][Timeslot]

    // Constructor
    public Doctor(String hospitalID, String password, String name, String gender, String role, int age, String specialization) {
        super(hospitalID, password, name, gender, role, age);
        this.specialization = specialization;
        this.patientRequests = new ArrayList<>();
        this.upcomingAppointments = new ArrayList<>();
        this.declinedAppointments = new ArrayList<>();
    }

    // Getter for specialization
    public String getSpecialization() {
        return this.specialization;
    }

    // Method to view personal schedule for a given date
    public void viewSchedule() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter date (MM/DD): ");
        String dateInput = scanner.nextLine();
        int[] date = parseDate(dateInput);
        if (date == null) {
            System.out.println("Invalid date format. Use MM/DD.");
            return;
        }
        System.out.printf("Schedule for %d/%d:\n", date[0], date[1]);
        for (int i = 0; i < 10; i++) {
            System.out.printf("Timeslot %d: %s\n", i + 1, schedule[date[0] - 1][date[1] - 1][i]);
        }
    }

    // Method to set availability for a specific date
    public void setAvailability() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter date (MM/DD) to set availability: ");
        String dateInput = scanner.nextLine();
        int[] date = parseDate(dateInput);
        if (date == null) {
            System.out.println("Invalid date format. Use MM/DD.");
            return;
        }

        System.out.print("Enter start timeslot (1-10): ");
        int startSlot = scanner.nextInt();
        System.out.print("Enter end timeslot (1-10): ");
        int endSlot = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (startSlot < 1 || endSlot > 10 || startSlot > endSlot) {
            System.out.println("Invalid timeslot range.");
            return;
        }

        for (int i = startSlot - 1; i < endSlot; i++) {
            schedule[date[0] - 1][date[1] - 1][i] = "Available";
        }
        System.out.printf("Availability set for %d/%d from timeslot %d to %d.\n", date[0], date[1], startSlot, endSlot);
    }

    // Accept or decline appointment requests
    public void manageAppointmentRequests() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Appointment Requests:");
        for (int i = 0; i < patientRequests.size(); i++) {
            System.out.printf("%d. ", i + 1);
            patientRequests.get(i).showDetails();
        }
        System.out.print("Select appointment to manage (enter index): ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index < 0 || index >= patientRequests.size()) {
            System.out.println("Invalid selection. Please choose a valid appointment request.");
            return;
        }

        System.out.print("Enter 'accept' or 'decline' for the appointment: ");
        String action = scanner.nextLine();
        if (action.equalsIgnoreCase("accept")) {
            acceptAppointment(patientRequests.get(index));
            patientRequests.remove(index);
        } else if (action.equalsIgnoreCase("decline")) {
            declineAppointment(patientRequests.get(index));
            patientRequests.remove(index);
        } else {
            System.out.println("Invalid action.");
        }
    }

    // Accept an appointment and add it to upcoming appointments
    private void acceptAppointment(Appointment appointment) {
        appointment.setAcceptedStatus(1);
        upcomingAppointments.add(appointment);
        schedule[appointment.getDateArray()[0] - 1][appointment.getDateArray()[1] - 1][appointment.getTimeslot() - 1] = appointment.getPatientName();
        System.out.println("Appointment accepted.");
    }

    // Decline an appointment and add it to declined appointments
    private void declineAppointment(Appointment appointment) {
        appointment.setAcceptedStatus(-1);
        declinedAppointments.add(appointment);
        System.out.println("Appointment declined.");
    }

    // View upcoming appointments
    public void viewUpcomingAppointments() {
        if (upcomingAppointments.isEmpty()) {
            System.out.println("No upcoming appointments.");
        } else {
            System.out.println("Upcoming Appointments:");
            for (Appointment appointment : upcomingAppointments) {
                appointment.showDetails();
            }
        }
    }

    // Record the outcome of a completed appointment
    public void recordAppointmentOutcome() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter timeslot of the completed appointment: ");
        int timeslot = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter date (MM/DD) of the appointment: ");
        String dateInput = scanner.nextLine();
        int[] date = parseDate(dateInput);
        if (date == null) {
            System.out.println("Invalid date format.");
            return;
        }

        Appointment completed = findAppointment(date, timeslot);
        if (completed == null) {
            System.out.println("No appointment found for the given date and timeslot.");
            return;
        }

        System.out.print("Enter consultation notes: ");
        String notes = scanner.nextLine();
        ArrayList<String> medications = new ArrayList<>();
        ArrayList<Float> quantities = new ArrayList<>();
        System.out.print("Enter number of medications prescribed: ");
        int medCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < medCount; i++) {
            System.out.print("Medication name: ");
            medications.add(scanner.nextLine());
            System.out.print("Quantity (grams): ");
            quantities.add(scanner.nextFloat());
            scanner.nextLine();
        }

        completed.setConsultationNotes(notes);
        for (int i = 0; i < medications.size(); i++) {
            completed.addMedication(medications.get(i), quantities.get(i));
        }
        completed.setAcceptedStatus(2); // Mark as completed
        System.out.println("Appointment outcome recorded.");
    }

    // Helper method to find an appointment by date and timeslot
    private Appointment findAppointment(int[] date, int timeslot) {
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getDateArray()[0] == date[0] && appointment.getDateArray()[1] == date[1] && appointment.getTimeslot() == timeslot) {
                return appointment;
            }
        }
        return null;
    }

    // Helper method to parse date in MM/DD format
    private int[] parseDate(String date) {
        try {
            String[] parts = date.split("/");
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            return new int[]{month, day};
        } catch (Exception e) {
            return null;
        }
    }
}
