package com.github.tommyt0mmy.drugdealing.utility;

public class Helper
{
    private static DrugType[] drugTypes;
    private static CriminalRole[] criminalRoles;

    static {
        drugTypes = new DrugType[DrugType.values().length];
        for (DrugType drugType : DrugType.values())
            drugTypes[drugType.getId()] = drugType;

        criminalRoles = new CriminalRole[CriminalRole.values().length];
        for (CriminalRole criminalRole : CriminalRole.values())
            criminalRoles[criminalRole.getId()] = criminalRole;
    }

    public static CriminalRole getCriminalRole(int id)
    {
        return criminalRoles[id];
    }

    public static DrugType getDrugType(int id)
    {
        return drugTypes[id];
    }
}
