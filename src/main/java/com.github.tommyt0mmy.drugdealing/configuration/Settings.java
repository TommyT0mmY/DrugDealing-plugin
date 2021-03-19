package com.github.tommyt0mmy.drugdealing.configuration;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.configuration.Configurable;

public class Settings extends Configurable
{
    public Settings(DrugDealing plugin)
    {
        super(plugin, "configs.yml");
    }
}
