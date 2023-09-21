package Trip;

import Port.AdminPort;
import Vehicle.AdminVehicle;

import java.time.LocalDateTime;

public class AdminTrips {
    private AdminVehicle vehicleInfo;
    private LocalDateTime DepartureDate;
    private LocalDateTime ArrivalDate;
    private AdminPort portDepart;
    private AdminPort portArrived;
    private String status;

    public AdminTrips(AdminVehicle vehicleInfo, LocalDateTime DepartureDate, LocalDateTime ArrivalDate, AdminPort portDepart, AdminPort portArrived, String status) {
        this.vehicleInfo = vehicleInfo;
        this.DepartureDate = DepartureDate;
        this.ArrivalDate = ArrivalDate;
        this.portDepart = portDepart;
        this.portArrived = portArrived;
        this.status = status;
    }
    // Constructor
    public AdminTrips() {
    }

    public AdminVehicle getVehicleInfo() {
        return vehicleInfo;
    }
    public void setVehicleInfo() {
        this.vehicleInfo = vehicleInfo;
    }
    public LocalDateTime getDepartureDate() {
        return DepartureDate;
    }
    public LocalDateTime getArrivalDate() {
        return ArrivalDate;
    }
    public AdminPort getPortArrived() {
        return portArrived;
    }
    public AdminPort getPortDepart() {
        return portDepart;
    }
    public String getStatus() {
        return status;
    }
}
