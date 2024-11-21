package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import models.Inventory;
import models.Medication;
import models.ReplenishmentRequest;
import models.ReplenishmentStatus;

public class ReplenishmentRequestController {
    private Map<String, ReplenishmentRequest> requestList;
    private Inventory inventory;

    public ReplenishmentRequestController(Inventory inventory) {
        this.requestList = new HashMap<>();
        this.inventory = inventory;
        initializeData();
    }
    private void initializeData() {
        File inventoryFile = new File("data/replenishmentrequest.csv");
        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                if (itemData.length >= 4) { // Ensure there are at least 4 fields
                    String reqID = itemData[0].trim();
                    String medID = itemData[1].trim();
                    int quantity = Integer.parseInt(itemData[2].trim());
                    ReplenishmentStatus status = ReplenishmentStatus.valueOf(itemData[3].trim().toUpperCase());

                    // Add new item to inventory
                    ReplenishmentRequest replenishmentRequest = new ReplenishmentRequest(reqID,medID,quantity);
                    replenishmentRequest.setStatus(status);
                    requestList.put(reqID,replenishmentRequest);
//                    System.out.println("Loaded Replenishment ID: "+ reqID);
                } else {
                    System.out.println("Invalid line format, skipping...");
                }
            }
            System.out.println("Replenishment Requests initialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Replenishment Requests data file not found!");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Replenishment Requests data. Please check the format of numeric values.");
        }
    }


    public void submitReplenishmentRequest(String medicationID, int quantity, String pharmacistID) {
        String replenishmentID = generateReqId();
        ReplenishmentRequest request = new ReplenishmentRequest(replenishmentID, medicationID, quantity);
        requestList.put(replenishmentID,request);
    }

    public List<ReplenishmentRequest> viewReplenishmentRequests(ReplenishmentStatus status) {
        List<ReplenishmentRequest> filteredRequests = new ArrayList<>();
        for (ReplenishmentRequest request : requestList.values()) {
            if (request.getStatus()== status) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }

    public void approveReplenishmentRequest(String requestID) {
        for (ReplenishmentRequest request : requestList.values()) {
            if (request.getReplenishmentID().equalsIgnoreCase(requestID) && request.getStatus() == ReplenishmentStatus.PENDING) {
                request.setStatus(ReplenishmentStatus.APPROVED);
                System.out.println("Request "+requestID+" approved...Updating inventory.");
                updateInventoryForApprovedRequest(requestID);
                return;
            }
        }
//        System.out.println("Request not found or not pending.");
    }

    public void rejectReplenishmentRequest(String requestID) {
        for (ReplenishmentRequest request : requestList.values()) {
            if (request.getReplenishmentID().equals(requestID) && request.getStatus() == ReplenishmentStatus.PENDING) {
                request.setStatus(ReplenishmentStatus.REJECTED);
                System.out.println("Request "+requestID+" been rejected.");
                return;
            }
        }
        System.out.println("Request not found or not pending.");
    }

    private void updateInventoryForApprovedRequest(String requestID) {
        ReplenishmentRequest request = requestList.get(requestID);
        if (request != null && request.getStatus() == ReplenishmentStatus.APPROVED) {
                Medication med = inventory.getMedicationByID(request.getMedicationID());
                if (med != null) {
                    boolean success = inventory.updateStock(med.getMed_id(), request.getQuantity());
                    if (success){
                    System.out.println("Inventory updated for medication: " + med.getName() + " with added quantity: " + request.getQuantity());
                    System.out.println("New Quantity: "+ med.getStock());
                    return;}

            }
            System.out.println("Medication not found in inventory for request ID: " + requestID);
        } else {
            System.out.println("Invalid request or request not approved.");
        }
    }


    private String generateReqId() {
        int count = requestList.size() + 1; // Increment by 1 to avoid zero-based index
        return String.format("R%03d", count);
    }
}
