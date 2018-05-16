package fr.corenting.edcompanion.models;

import java.util.List;

public class CommodityFinderResults extends BaseModel{

    public List<CommodityFinderResult> Results;

    public CommodityFinderResults(boolean success, List<CommodityFinderResult> results)
    {
        this.Success = success;
        this.Results = results;
    }
}
