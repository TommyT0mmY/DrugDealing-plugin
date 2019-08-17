package drugdealing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import drugdealing.utility.XMaterial;

public class Drugs {
	
	private ItemStack coke;
	private ItemStack weed; 
	
	private Main mainClass;
	public Drugs(Main mainClass) {
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
	
	public ItemStack getCokeItemStack() {
		return coke;
	}
	
	public ItemStack getWeedItemStack() {
		return weed;
	}
	
	public boolean isCokeItemStack(ItemStack toCheckIS) {
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
	
	public boolean isWeedItemStack(ItemStack toCheckIS) {
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
	
	public boolean isPlantedOnFarmland(Block plant) {
		Block belowBlock = plant.getLocation().subtract(0, 1, 0).getBlock();
		if (belowBlock.getType().equals(XMaterial.FARMLAND.parseItem().getType())) {
			return true;
		}
		
		return false;
	}
	
}
