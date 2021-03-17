//Handles ingame drugs and itemstacks

package com.github.tommyt0mmy.drugdealing;

import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Drugs
{

    private ItemStack coke_plant;
    private ItemStack weed_plant;
    private ItemStack coke_drug;
    private ItemStack weed_drug;

    private DrugDealing mainClass = DrugDealing.getInstance();

    public Drugs()
    {
        initItemStacks();
    }

    private void initItemStacks()
    {

        //coke plant ItemStack

        coke_plant = new ItemStack(Material.POPPY);
        ItemMeta cokePlantMeta = coke_plant.getItemMeta();
        List<String> cokePlantLore = new ArrayList<>();
        cokePlantLore.add(mainClass.language.getKeyword("coke_plant_name"));
        cokePlantMeta.setLore(cokePlantLore);
        cokePlantMeta.setDisplayName(mainClass.language.getKeyword("coke_plant_name"));
        coke_plant.setItemMeta(cokePlantMeta);

        //weed plant ItemStack

        weed_plant = new ItemStack(Material.JUNGLE_SAPLING);
        ItemMeta weedPlantMeta = weed_plant.getItemMeta();
        List<String> weedPlantLore = new ArrayList<>();
        weedPlantLore.add(mainClass.language.getKeyword("weed_plant_name"));
        weedPlantMeta.setLore(weedPlantLore);
        weedPlantMeta.setDisplayName(mainClass.language.getKeyword("weed_plant_name"));
        weed_plant.setItemMeta(weedPlantMeta);

        //coke drug ItemStack

        coke_drug = new ItemStack(Material.SUGAR);
        ItemMeta cokeDrugItemMeta = coke_drug.getItemMeta();
        List<String> cokeDrugLore = new ArrayList<>();
        cokeDrugLore.add(mainClass.language.getKeyword("coke_drug_name"));
        cokeDrugItemMeta.setLore(cokeDrugLore);
        cokeDrugItemMeta.setDisplayName(mainClass.language.getKeyword("coke_drug_name"));
        coke_drug.setItemMeta(cokeDrugItemMeta);

        //weed drug ItemStack

        weed_drug = new ItemStack(Material.CACTUS_GREEN);
        ItemMeta weedDrugItemMeta = coke_drug.getItemMeta();
        List<String> weedDrugLore = new ArrayList<>();
        weedDrugLore.add(mainClass.language.getKeyword("weed_drug_name"));
        weedDrugItemMeta.setLore(weedDrugLore);
        weedDrugItemMeta.setDisplayName(mainClass.language.getKeyword("weed_drug_name"));
        weed_drug.setItemMeta(weedDrugItemMeta);
    }

    public ItemStack getCokePlantItemStack()
    { //returns a coke itemstack
        return coke_plant;
    }

    public ItemStack getWeedPlantItemStack()
    { //returns a weed itemstack
        return weed_plant;
    }

    public ItemStack getCokeDrugItemStack() { return coke_drug; }

    public ItemStack getWeedDrugItemStack() { return weed_drug; }

    public boolean isCokePlantItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a coke plant item
        String coke_plant_name = mainClass.language.getKeyword("coke_plant_name");
        if (toCheckIS.getType().equals(Material.POPPY))
        { //checking type
            if (toCheckIS.hasItemMeta())
            { //checking name & lore
                ItemMeta toCheckMeta = toCheckIS.getItemMeta();
                if (toCheckMeta.hasDisplayName() && toCheckMeta.hasLore())
                {
                    return toCheckMeta.getLore().get(0).equals(coke_plant_name) && toCheckMeta.getDisplayName().equals(coke_plant_name);
                }
            }
        }

        return false;
    }

    public boolean isWeedPlantItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a weed plant item
        String weed_plant_name = mainClass.language.getKeyword("weed_plant_name");
        if (toCheckIS.getType().equals(Material.JUNGLE_SAPLING))
        {
            if (toCheckIS.hasItemMeta())
            { //checking name & lore
                ItemMeta toCheckMeta = toCheckIS.getItemMeta();
                if (toCheckMeta.hasDisplayName() && toCheckMeta.hasLore())
                {
                    return toCheckMeta.getLore().get(0).equals(weed_plant_name) && toCheckMeta.getDisplayName().equals(weed_plant_name);
                }
            }
        }

        return false;
    }

    public boolean isCokeDrugItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a coke item
        String coke_drug_name = mainClass.language.getKeyword("coke_drug_name");
        if (toCheckIS.getType().equals(Material.SUGAR))
        { //checking type
            if (toCheckIS.hasItemMeta())
            { //checking name & lore
                ItemMeta toCheckMeta = toCheckIS.getItemMeta();
                if (toCheckMeta.hasDisplayName() && toCheckMeta.hasLore())
                {
                    return toCheckMeta.getLore().get(0).equals(coke_drug_name) && toCheckMeta.getDisplayName().equals(coke_drug_name);
                }
            }
        }

        return false;
    }

    public boolean isWeedDrugItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a weed item
        String weed_drug_name = mainClass.language.getKeyword("weed_drug_name");
        if (toCheckIS.getType().equals(Material.CACTUS_GREEN))
        {
            if (toCheckIS.hasItemMeta())
            { //checking name & lore
                ItemMeta toCheckMeta = toCheckIS.getItemMeta();
                if (toCheckMeta.hasDisplayName() && toCheckMeta.hasLore())
                {
                    return toCheckMeta.getLore().get(0).equals(weed_drug_name) && toCheckMeta.getDisplayName().equals(weed_drug_name);
                }
            }
        }

        return false;
    }

    //if the given ItemStack isn't a drug type return null
    public DrugType getDrugType(ItemStack toCheckIS)
    {
        if (isCokePlantItemStack(toCheckIS)) { return DrugType.COKE_PLANT; }
        if (isWeedPlantItemStack(toCheckIS)) { return DrugType.WEED_PLANT; }
        if (isCokeDrugItemStack(toCheckIS)) { return DrugType.COKE_PRODUCT; }
        if (isWeedDrugItemStack(toCheckIS)) { return DrugType.WEED_PRODUCT; }

        return null;
    }

    public boolean isPlantedOnFarmland(Block plant)
    { //checks if the plant is planted on farmland
        Block belowBlock = plant.getLocation().subtract(0, 1, 0).getBlock();
        return belowBlock.getType().equals(Material.FARMLAND);
    }

    public void destroyPlant(Block plant, boolean dropItems)
    { //similar to org.bukkit.block.Block.breakNaturally() but with some changes
        Location plantLocation = plant.getLocation();
        if (mainClass.plantsRegister.isDrugPlant(plantLocation))
        { //if the plant is registered in plants.yml

            plant.setType(Material.AIR); //removing the plant's block
            plantLocation.getWorld().playSound(plantLocation, Sound.BLOCK_LAVA_POP, 5, 5); //sound feedback
            ConfigurationSection plantCS = mainClass.plantsRegister.getPlant(plantLocation); //the plant's section in plants.yml

            //Plant drops
            if (dropItems)
            {
                if (mainClass.plantsRegister.getType(plantLocation).equals("coke"))
                {
                    plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getCokePlantItemStack()); //first drop
                    if (plantCS.getBoolean("grown"))
                    { //If the plant is grown a second drop will appear
                        plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getCokePlantItemStack());
                    }

                } else if (mainClass.plantsRegister.getType(plantLocation).equals("weed"))
                {
                    plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getWeedPlantItemStack()); //first drop
                    if (plantCS.getBoolean("grown"))
                    { //If the plant is grown a second drop will appear
                        plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getWeedPlantItemStack());
                    }
                }
            }
            mainClass.plantsRegister.removePlant(plantLocation);
        }
    }

    public void growPlant(Location loc)
    { //given a location of a plant it makes the plant grow to it's final stage, ready to be harvested
        String plantType = mainClass.plantsRegister.getType(loc);
        Block plantBlock = loc.getBlock();
        ConfigurationSection plant = mainClass.plantsRegister.getPlant(loc);
        if (plant.getBoolean("grown"))
        {
            return;
        }

        Block top = loc.add(0, 1, 0).getBlock();
        if (plantType.equals("weed"))
        {
            //TOP DATA: 10
            //BOTTOM DATA: 3

            setFlower(plantBlock, Material.LARGE_FERN, Bisected.Half.BOTTOM);
            setFlower(top, Material.LARGE_FERN, Bisected.Half.TOP);

        } else if (plantType.equals("coke"))
        {
            //TOP DATA: 10
            //BOTTOM DATA: 4

            setFlower(plantBlock, Material.ROSE_BUSH, Bisected.Half.BOTTOM);
            setFlower(top, Material.ROSE_BUSH, Bisected.Half.TOP);
        }
        plant.set("grown", true);
    }

    private void setFlower(Block block, Material type, Bisected.Half half)
    {
        block.setType(type, false);
        Bisected data = (Bisected) block.getBlockData();
        data.setHalf(half);
        block.setBlockData(data); // Again, may need to pass "false"
    }
}
