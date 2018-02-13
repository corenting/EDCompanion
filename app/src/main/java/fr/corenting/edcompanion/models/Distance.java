package fr.corenting.edcompanion.models;


public class Distance extends BaseModel {
    public float Distance;
    public String StartSystemName;
    public String EndSystemName;
    public boolean StartPermitRequired;
    public boolean EndPermitRequired;

    public Distance(boolean ok, float distance, String startSystem, String endSystem,
                    boolean startPermitRequired, boolean endPermitRequired) {
        StartSystemName = startSystem;
        EndSystemName = endSystem;
        Success = ok;
        Distance = distance;
        StartPermitRequired = startPermitRequired;
        EndPermitRequired = endPermitRequired;
    }
}