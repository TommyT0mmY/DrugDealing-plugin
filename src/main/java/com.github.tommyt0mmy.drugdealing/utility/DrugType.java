package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;

public enum DrugType
{
    WEED_PLANT(0, false, true, "weed_plant_name", null, "weedProductionPrice", "WEED_PRODUCT"),
    COKE_PLANT(1, false, true, "coke_plant_name", null, "cokeProductionPrice", "COKE_PRODUCT"),
    WEED_PRODUCT(2, true, false, "weed_drug_name", "weedDrugSellingPrice", null, "WEED_PLANT"),
    COKE_PRODUCT(3, true, false, "coke_drug_name", "cokeDrugSellingPrice", null, "COKE_PLANT");

    private final int id;
    private final boolean isAcceptedByDealer;
    private final boolean isPlant;
    private final String keywordPrettyName;
    private final String keywordSellingPrice;
    private final String keywordProductionPrice;
    private final String opposite;

    DrugType(final int id, final boolean isAcceptedByDealer, final boolean isPlant, final String keywordPrettyName, final String keywordSellingPrice, final String keywordProductionPrice, final String opposite)
    {
        this.id = id;
        this.isAcceptedByDealer = isAcceptedByDealer;
        this.isPlant = isPlant;
        this.keywordPrettyName = keywordPrettyName;
        this.keywordSellingPrice = keywordSellingPrice;
        this.keywordProductionPrice = keywordProductionPrice;
        this.opposite = opposite;
    }

    public int getId() { return id; }

    public boolean isAcceptedByDealer() { return isAcceptedByDealer; }

    public boolean isPlant() { return isPlant; }

    public String getPrettyName() { return DrugDealing.getInstance().language.getKeyword(keywordPrettyName); }

    public String getKeywordSellingPrice()
    {
        return keywordSellingPrice;
    }

    public String getKeywordProductionPrice()
    {
        return keywordProductionPrice;
    }

    public DrugType getOpposite()
    {
        return DrugType.valueOf(opposite);
    }
}
