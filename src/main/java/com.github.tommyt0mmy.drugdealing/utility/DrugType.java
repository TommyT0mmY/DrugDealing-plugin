package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;

public enum DrugType
{
    WEED_PLANT(0,false, true, DrugDealing.getInstance().language.getKeyword("weed_plant_name")),
    COKE_PLANT(1,false, true, DrugDealing.getInstance().language.getKeyword("coke_plant_name")),
    WEED_PRODUCT(2,true, false, DrugDealing.getInstance().language.getKeyword("weed_drug_name")),
    COKE_PRODUCT(3,true, false, DrugDealing.getInstance().language.getKeyword("coke_drug_name"));

    private final int id;
    private final boolean isAcceptedByDealer;
    private final boolean isPlant;
    private final String prettyName;

    DrugType(final int id, final boolean isAcceptedByDealer, final boolean isPlant, final String prettyName)
    {
        this.id = id;
        this.isAcceptedByDealer = isAcceptedByDealer;
        this.isPlant = isPlant;
        this.prettyName = prettyName;
    }

    public int getId() { return id; }

    public boolean isAcceptedByDealer() { return isAcceptedByDealer; }

    public boolean isPlant() { return isPlant; }

    public String getPrettyName() { return prettyName; }
}
