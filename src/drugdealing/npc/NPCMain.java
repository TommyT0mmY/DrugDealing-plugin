package drugdealing.npc;

import org.bukkit.Bukkit;

import drugdealing.Main;
import drugdealing.npc.versions.NPC_1_12_R1;
import drugdealing.npc.versions.NPC_1_13_R1;
import drugdealing.npc.versions.NPC_1_13_R2;

public class NPCMain {
	private Main mainClass = Main.getInstance();
	
	private NPC npc;
	
	public NPCMain() {
		load();
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	private void load() {
        if (!setupNPCs()) {
        	mainClass.console.severe("Failed to setup NPC System!");
        	mainClass.console.severe("Your server version is not compatible with this plugin!");
        	
        }
	}
	
	 private boolean setupNPCs() {
	        String version;
	        try {
	        	
	            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	            
	        } catch (ArrayIndexOutOfBoundsException notCompatibleVersion) {return false;}

	        mainClass.console.info("Your server is running version " + version);

	        switch (version) {
	        case "v1_12_R1":
	        	npc = new NPC_1_12_R1(); //1.12.*
	        	break;
	        case "v1_13_R1":
	        	npc = new NPC_1_13_R1(); //1.13
	        	break;
	        case "v1_13_R2":
	        	npc = new NPC_1_13_R2(); //1.13.1, 1.13.2
	        	break;
	        }
	        return npc != null;
	    }
	
	//when the server reloads the npcs will disappear so this function will respawn them
	public void spawnNPCs() {
		
	}
}
