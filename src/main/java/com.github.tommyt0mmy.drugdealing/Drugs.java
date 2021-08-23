//Handles ingame drugs and itemstacks

package com.github.tommyt0mmy.drugdealing;

import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Helper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Drugs
{
    private ItemStack cokePlant;
    private ItemStack weedPlant;
    private ItemStack cokeDrug;
    private ItemStack weedDrug;

    private DrugDealing instance = DrugDealing.getInstance();

    public Drugs()
    {
        initItemStacks();
    }

    private void initItemStacks()
    {
        //TODO USE ENUMS
        //coke plant ItemStack

        cokePlant = new ItemStack(Material.POPPY);
        ItemMeta cokePlantMeta = cokePlant.getItemMeta();
        assert cokePlantMeta != null;
        cokePlantMeta.setDisplayName(instance.language.getKeyword("coke_plant_name"));
        NamespacedKey key = new NamespacedKey(instance, "id");
        cokePlantMeta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) DrugType.COKE_PLANT.getId());
        cokePlant.setItemMeta(cokePlantMeta);

        //weed plant ItemStack

        weedPlant = new ItemStack(Material.JUNGLE_SAPLING);
        ItemMeta weedPlantMeta = weedPlant.getItemMeta();
        assert weedPlantMeta != null;
        weedPlantMeta.setDisplayName(instance.language.getKeyword("weed_plant_name"));
        key = new NamespacedKey(instance, "id");
        weedPlantMeta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) DrugType.WEED_PLANT.getId());
        weedPlant.setItemMeta(weedPlantMeta);

        //coke drug ItemStack

        cokeDrug = new ItemStack(Material.SUGAR);
        ItemMeta cokeDrugItemMeta = cokeDrug.getItemMeta();
        assert cokeDrugItemMeta != null;
        cokeDrugItemMeta.setDisplayName(instance.language.getKeyword("coke_drug_name"));
        key = new NamespacedKey(instance, "id");
        cokeDrugItemMeta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) DrugType.COKE_PRODUCT.getId());
        cokeDrug.setItemMeta(cokeDrugItemMeta);

        //weed drug ItemStack

        weedDrug = new ItemStack(Material.GREEN_DYE);
        ItemMeta weedDrugItemMeta = cokeDrug.getItemMeta();
        assert weedDrugItemMeta != null;
        weedDrugItemMeta.setDisplayName(instance.language.getKeyword("weed_drug_name"));
        key = new NamespacedKey(instance, "id");
        weedDrugItemMeta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) DrugType.WEED_PRODUCT.getId());
        weedDrug.setItemMeta(weedDrugItemMeta);
    }

    //TODO STOP USING THESE METHODS
    @Deprecated
    public ItemStack getCokePlantItemStack()
    {
        return cokePlant;
    }

    @Deprecated
    public ItemStack getWeedPlantItemStack()
    {
        return weedPlant;
    }

    @Deprecated
    public ItemStack getCokeDrugItemStack()
    {
        return cokeDrug;
    }

    @Deprecated
    public ItemStack getWeedDrugItemStack()
    {
        return weedDrug;
    }

    public ItemStack getItemStack(DrugType drugType)
    {
        switch (drugType)
        {
            case WEED_PLANT:
                return weedPlant;
            case COKE_PLANT:
                return cokePlant;
            case WEED_PRODUCT:
                return weedDrug;
            case COKE_PRODUCT:
                return cokeDrug;
        }
        return null;
    }


    //TODO START USING PERSISTENT DATA CONTAINERS INSTEAD
    @Deprecated
    public boolean isCokePlantItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a coke plant item
        String coke_plant_name = instance.language.getKeyword("coke_plant_name");
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

    //TODO START USING PERSISTENT DATA CONTAINERS INSTEAD
    @Deprecated
    public boolean isWeedPlantItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a weed plant item
        String weed_plant_name = instance.language.getKeyword("weed_plant_name");
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

    //TODO START USING PERSISTENT DATA CONTAINERS INSTEAD
    @Deprecated
    public boolean isCokeDrugItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a coke item
        String coke_drug_name = instance.language.getKeyword("coke_drug_name");
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

    //TODO START USING PERSISTENT DATA CONTAINERS INSTEAD
    @Deprecated
    public boolean isWeedDrugItemStack(ItemStack toCheckIS)
    { //given an ItemStack returns true if it's a weed item
        String weed_drug_name = instance.language.getKeyword("weed_drug_name");
        if (toCheckIS.getType().equals(Material.GREEN_DYE))
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

    public Optional<DrugType> getDrugType(@NotNull ItemStack itemStack)
    {
        ItemMeta itemStackMeta = itemStack.getItemMeta();
        if (itemStackMeta == null)
            return Optional.empty();
        NamespacedKey key = new NamespacedKey(instance, "id");
        Byte id = itemStackMeta.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        if (id == null)
            return Optional.empty();

        return Optional.of(Helper.getDrugType(id));
    }

    public boolean isPlantedOnFarmland(Block plant)
    { //checks if the plant is planted on farmland
        Block belowBlock = plant.getRelative(0, -1, 0);
        return belowBlock.getType().equals(Material.FARMLAND);
    }

    //similar to org.bukkit.block.Block.breakNaturally()
    public void destroyPlant(Block plant, boolean dropItems)
    {
        Location plantLocation = plant.getLocation();

        //if the plant isn't registered
        if (!instance.plantsRegister.isDrugPlant(plantLocation))
            return;

        DrugType plantType = instance.plantsRegister.getDrugType(plantLocation);

        plant.setType(Material.AIR); //removing the plant's block
        plantLocation.getWorld().playSound(plantLocation, Sound.BLOCK_LAVA_POP, 5, 5); //sound feedback

        //Plant drops
        if (dropItems)
        {
            //if the plant is fully grown there will be two drops, otherwise only one
            int amount = (instance.plantsRegister.isPhysicallyGrown(plantLocation)) ? 2 : 1;

            ItemStack droppedItemStack = this.getItemStack(plantType);
            droppedItemStack.setAmount(amount);

            plant.getWorld().dropItemNaturally(plantLocation, droppedItemStack);
        }

        instance.plantsRegister.removePlant(plantLocation);
    }

    public void growPlant(Location loc)
    { //given a location of a plant it makes the plant grow to it's final stage, ready to be harvested
        DrugType plantType = instance.plantsRegister.getDrugType(loc);
        Block plantBlock = loc.getBlock();

        //if the plant is already grown
        if (instance.plantsRegister.isPhysicallyGrown(loc))
        {
            return;
        }

        Block top = loc.add(0, 1, 0).getBlock();
        if (plantType.equals(DrugType.WEED_PLANT))
        {
            setFlower(plantBlock, Material.LARGE_FERN, Bisected.Half.BOTTOM);
            setFlower(top, Material.LARGE_FERN, Bisected.Half.TOP);

        } else if (plantType.equals(DrugType.COKE_PLANT))
        {
            setFlower(plantBlock, Material.ROSE_BUSH, Bisected.Half.BOTTOM);
            setFlower(top, Material.ROSE_BUSH, Bisected.Half.TOP);
        }

        instance.plantsRegister.setPhysicallyGrown(loc, true);
    }

    private void setFlower(Block block, Material type, Bisected.Half half)
    {
        block.setType(type, false); //applyPhysics set to false
        Bisected data = (Bisected) block.getBlockData();
        data.setHalf(half);
        block.setBlockData(data);
    }
}
