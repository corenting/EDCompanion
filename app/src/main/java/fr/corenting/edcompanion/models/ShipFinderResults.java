package fr.corenting.edcompanion.models;


import java.util.List;

public class ShipFinderResults extends BaseModel {
    public List<ShipFinderResult> Results;

    public ShipFinderResults(boolean success, List<ShipFinderResult> results)
    {
        this.Success = success;
        this.Results = results;
    }
}
