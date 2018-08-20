package fr.corenting.edcompanion.models;

import org.threeten.bp.Instant;

public class CommodityFinderResult {

    public long BuyPrice;
    public String LandingPad;
    public String Station;
    public String System;
    public boolean PermitRequired;
    public boolean IsPlanetary;
    public long Stock;
    public int DistanceToStar;
    public float Distance;
    public Instant LastPriceUpdate;
    public int PriceDifferenceFromAverage;
}
