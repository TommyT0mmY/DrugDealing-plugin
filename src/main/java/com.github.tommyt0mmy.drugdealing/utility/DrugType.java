package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;

public enum DrugType {
    WEED_PLANT(false, true, DrugDealing.getInstance().messages.getMessage("weed_plant_name")),
    COKE_PLANT(false, true, DrugDealing.getInstance().messages.getMessage("coke_plant_name")),
    WEED_PRODUCT(true, false, DrugDealing.getInstance().messages.getMessage("weed_drug_name")),
    COKE_PRODUCT(true, false, DrugDealing.getInstance().messages.getMessage("coke_drug_name"));

    private boolean isAcceptedByDealer;
    private boolean isPlant;
    private String prettyName;

    DrugType(final boolean isAcceptedByDealer, final boolean isPlant, final String prettyName) {
        this.isAcceptedByDealer = isAcceptedByDealer;
        this.isPlant = isPlant;
        this.prettyName = prettyName;
    }

    public boolean isAcceptedByDealer() { return isAcceptedByDealer; }

    public boolean isPlant() { return isPlant; }

    public String getPrettyName() { return prettyName; }
}
