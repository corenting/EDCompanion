package fr.corenting.edcompanion.models;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;

public class CommodityFinderResults extends BaseModel{

    public List<CommodityFinderResult> Results;
    public CommodityResponse Commodity;

    public CommodityFinderResults(boolean success, List<CommodityFinderResult> results,
                                  CommodityResponse commodity)
    {
        this.Success = success;
        this.Results = results;
        this.Commodity = commodity;
    }
}
