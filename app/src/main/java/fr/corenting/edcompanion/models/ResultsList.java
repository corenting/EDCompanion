package fr.corenting.edcompanion.models;

import java.util.List;

public class ResultsList<TDataType> extends BaseModel {
    public List<TDataType> Results;

    public ResultsList(boolean success, List<TDataType> results)
    {
        this.Success = success;
        this.Results = results;
    }
}
