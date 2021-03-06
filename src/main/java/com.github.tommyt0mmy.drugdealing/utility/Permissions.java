package com.github.tommyt0mmy.drugdealing.utility;

import java.util.HashMap;
import java.util.Map;

public class Permissions
{

    @SuppressWarnings("serial")
    private static final Map<String, String> permissionsMap = new HashMap<String, String>()
    {
        {
            put("plant_coke", "drugdealing.plant.coke");
            put("plant_weed", "drugdealing.plant.weed");
            put("harvest_coke", "drugdealing.harvest.coke");
            put("harvest_weed", "drugdealing.harvest.weed");
            put("getplant_coke", "drugdealing.getplant.coke");
            put("getplant_weed", "drugdealing.getplant.weed");
            put("getdrug_coke", "drugdealing.getdrug.coke");
            put("getdrug_weed", "drugdealing.getdrug.weed");
            put("help_menu", "drugdealing.help");
            put("spawn_dealer", "drugdealing.setnpc.dealer");
            put("spawn_producer", "drugdealing.setnpc.producer");
            put("remove_dealer", "drugdealing.removenpc.dealer");
            put("remove_producer", "drugdealing.removenpc.producer");
            put("use_dealer", "drugdealing.usenpc.dealer");
            put("use_producer", "drugdealing.usenpc.producer");
            put("consume_coke", "drugdealing.consume.coke");
        }
    };

    public static String getPermission(String permissionName)
    {
        String perm = "";
        perm = permissionsMap.get(permissionName);
        return perm;
    }

}
