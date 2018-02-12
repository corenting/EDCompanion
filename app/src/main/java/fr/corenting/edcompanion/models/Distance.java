package fr.corenting.edcompanion.models;


public class Distance extends BaseModel {
    public float Distance;
    public boolean StartPermitRequired;
    public boolean EndPermitRequired;

    public Distance(boolean ok, float distance, boolean startPermitRequired, boolean endPermitRequired) {
        Success = ok;
        Distance = distance;
        StartPermitRequired = startPermitRequired;
        EndPermitRequired = endPermitRequired;
    }
}