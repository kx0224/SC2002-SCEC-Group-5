class Administrator {
    private List<Staff> staffList;
    private List<Medication> inventory;
    private List<Appointment> appointments;
    private List<String> replenishmentRequests;

    public Administrator() {
        staffList = new ArrayList<>();
        inventory = new ArrayList<>();
        appointments = new ArrayList<>();
        replenishmentRequests = new ArrayList<>();
        loadInitialData();
    }

    // Load initial data from files
    private void loadInitialData() {
        loadStaffFromFile("staff.txt");
        loadInventoryFromFile("inventory.txt");
    }

    // Staff management methods
    private void loadStaffFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("\t");
                staffList.add(new Staff(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }
        } catch (IOException e) {
            System.out.println("Error loading staff data: " + e.getMessage());
        }
    }

    public void displayStaffList(String filterType, String filterValue) {
        System.out.println("\nStaff List:");
        System.out.println("ID\tName\tRole\tGender\tAge");
        for (Staff staff : staffList) {
            boolean display = true;
            switch (filterType.toLowerCase()) {
                case "role":
                    display = staff.getRole().equalsIgnoreCase(filterValue);
                    break;
                case "gender":
                    display = staff.getGender().equalsIgnoreCase(filterValue);
                    break;
                case "age":
                    display = staff.getAge() == Integer.parseInt(filterValue);
                    break;
            }
            if (display) {
                System.out.println(staff.getStaffId() + "\t" + staff.getName() + "\t" + 
                                 staff.getRole() + "\t" + staff.getGender() + "\t" + staff.getAge());
            }
        }
    }

    // Inventory management methods
    private void loadInventoryFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("\t");
                inventory.add(new Medication(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory data: " + e.getMessage());
        }
    }

    public void displayInventory() {
        System.out.println("\nMedication Inventory:");
        System.out.println("Name\tStock\tLow Stock Alert");
        for (Medication med : inventory) {
            System.out.println(med.getName() + "\t" + med.getStock() + "\t" + med.getLowStockAlert());
        }
    }

    public void updateMedicationStock(String medName, int newStock) {
        for (Medication med : inventory) {
            if (med.getName().equalsIgnoreCase(medName)) {
                med.setStock(newStock);
                System.out.println("Stock updated successfully.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void updateLowStockAlert(String medName, int newAlert) {
        for (Medication med : inventory) {
            if (med.getName().equalsIgnoreCase(medName)) {
                med.setLowStockAlert(newAlert);
                System.out.println("Low stock alert updated successfully.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    // Appointment management methods
    public void displayAppointments() {
        System.out.println("\nAppointment Details:");
        System.out.println("Patient ID\tDoctor ID\tStatus\tDate/Time");
        for (Appointment apt : appointments) {
            System.out.println(apt.getPatientId() + "\t" + apt.getDoctorId() + "\t" + 
                             apt.getStatus() + "\t" + apt.getDateTime());
        }
    }

    // Replenishment request management
    public void approveReplenishmentRequest(String medName, int amount) {
        for (Medication med : inventory) {
            if (med.getName().equalsIgnoreCase(medName)) {
                med.setStock(med.getStock() + amount);
                System.out.println("Replenishment request approved and stock updated.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }
}
