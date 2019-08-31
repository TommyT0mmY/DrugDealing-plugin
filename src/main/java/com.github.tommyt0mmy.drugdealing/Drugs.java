//Handles ingame drug tiles

package com.github.tommyt0mmy.drugdealing;

import com.github.tommyt0mmy.drugdealing.utility.XMaterial;
import com.github.tommyt0mmy.drugdealing.utility.XSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Drugs {
	
	private ItemStack coke;
	private ItemStack weed; 
	
	private DrugDealing mainClass;
	public Drugs(DrugDealing mainClass) {
		this.mainClass = mainClass;
		initItemStacks();
	}
	
	private void initItemStacks() {
		
		//coke ItemStack
		
		coke = XMaterial.POPPY.parseItem();
		ItemMeta cokeMeta = coke.getItemMeta();
		List<String> cokeLore = new ArrayList<>();
		cokeLore.add(mainClass.messages.getMessage("coke_name"));
		cokeMeta.setLore(cokeLore);
		cokeMeta.setDisplayName(mainClass.messages.getMessage("coke_name"));
		coke.setItemMeta(cokeMeta);
		
		//weed ItemStack
		
		weed = XMaterial.JUNGLE_SAPLING.parseItem();
		ItemMeta weedMeta = weed.getItemMeta();
		List<String> weedLore = new ArrayList<>();
		weedLore.add(mainClass.messages.getMessage("weed_name"));
		weedMeta.setLore(weedLore);
		weedMeta.setDisplayName(mainClass.messages.getMessage("weed_name"));
		weed.setItemMeta(weedMeta);
	}
	
	public ItemStack getCokeItemStack() { //returns a coke itemstack
		return coke;
	}
	
	public ItemStack getWeedItemStack() { //returns a weed itemstack
		return weed;
	}
	
	public boolean isCokeItemStack(ItemStack toCheckIS) { //given an ItemStack returns true if it's a coke item
		String coke_name = mainClass.messages.getMessage("coke_name");
		if (toCheckIS.getType().equals(XMaterial.POPPY.parseMaterial())) { //checking type
			if (toCheckIS.hasItemMeta()) { //checking name & lore
				ItemMeta placedMeta = toCheckIS.getItemMeta();
				if (placedMeta.hasDisplayName() && placedMeta.hasLore()) {
					if (placedMeta.getLore().get(0).equals(coke_name) && placedMeta.getDisplayName().equals(coke_name)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isWeedItemStack(ItemStack toCheckIS) { //given an ItemStack returns true if it's a weed item
		String weed_name = mainClass.messages.getMessage("weed_name");
		if (toCheckIS.getType().equals(XMaterial.JUNGLE_SAPLING.parseItem().getType())) {
			if (toCheckIS.hasItemMeta()) { //checking name & lore
				ItemMeta placedMeta = toCheckIS.getItemMeta();
				if (placedMeta.hasDisplayName() && placedMeta.hasLore()) {
					if (placedMeta.getLore().get(0).equals(weed_name) && placedMeta.getDisplayName().equals(weed_name)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isPlantedOnFarmland(Block plant) { //checks if the plant is planted on farmland
		Block belowBlock = plant.getLocation().subtract(0, 1, 0).getBlock();
		if (belowBlock.getType().equals(XMaterial.FARMLAND.parseItem().getType())) {
			return true;
		}
		
		return false;
	}
	
	public void destroyPlant(Block plant, boolean dropItems) { //similar to org.bukkit.block.Block.breakNaturally() but with some changes
		Location plantLocation = plant.getLocation();
		if (mainClass.plantsreg.isDrugPlant(plantLocation)) { //if the plant is registered in plants.yml
			
			plant.setType(XMaterial.AIR.parseItem().getType()); //removing the plant's block
			plantLocation.getWorld().playSound(plantLocation, XSound.BLOCK_LAVA_POP.parseSound(), 5, 5); //sound feedback
			ConfigurationSection plantCS = mainClass.plantsreg.getPlant(plantLocation); //the plant's section in plants.yml
			
			//Plant drops
			if (dropItems) {
				if (mainClass.plantsreg.getType(plantLocation).equals("coke")) {
					plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getCokeItemStack()); //first drop
					if (plantCS.getBoolean("grown")) { //If the plant is grown a second drop will appear
						plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getCokeItemStack());
					}
					
				}else if (mainClass.plantsreg.getType(plantLocation).equals("weed")) {
					plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getWeedItemStack()); //first drop
					if (plantCS.getBoolean("grown")) { //If the plant is grown a second drop will appear
						plant.getWorld().dropItemNaturally(plantLocation, mainClass.drugs.getWeedItemStack());
					}
				}
			}
			mainClass.plantsreg.removePlant(plantLocation);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void growPlant(Location loc) { //given a location of a plant it makes the plant grow to it's final stage, ready to be harvested
		String plantType = mainClass.plantsreg.getType(loc); 
		Block plantBlock = loc.getBlock();
		ConfigurationSection plant = mainClass.plantsreg.getPlant(loc);
		if (plant.getBoolean("grown")) {
			return;
		}

		Block top = loc.add(0, 1, 0).getBlock();
		Block bottom = plantBlock;
		if (plantType.equals("weed")) {
			//TOP DATA: 10
			//BOTTOM DATA: 3

			setFlower(bottom, Material.LARGE_FERN, Bisected.Half.BOTTOM);
			setFlower(top, Material.LARGE_FERN, Bisected.Half.TOP);

		}else if (plantType.equals("coke")) {
			//TOP DATA: 10
			//BOTTOM DATA: 4

			setFlower(bottom, Material.ROSE_BUSH, Bisected.Half.BOTTOM);
			setFlower(top, Material.ROSE_BUSH, Bisected.Half.TOP);
		}
		plant.set("grown", true);
	}

	private void setFlower(Block block, Material type, Bisected.Half half) {
		block.setType(type, false);
		Bisected data = (Bisected) block.getBlockData();
		data.setHalf(half);
		block.setBlockData(data); // Again, may need to pass "false"
	}
}
