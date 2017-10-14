package fr.corenting.edcompanion.models;

public class ServerStatus extends BaseModel {
    public String Status;

    public ServerStatus(boolean success, String status) {
        Success = success;
        Status = status;
    }
}
