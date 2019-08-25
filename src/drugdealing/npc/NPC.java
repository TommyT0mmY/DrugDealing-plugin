package drugdealing.npc;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface NPC {
	
	/*	p => the player who sends the command
	 *  name => the name that the npc will have
	 *  role => the role that the npc will have (producer / dealer)
	 */
	public UUID createNPC(Player p, String name); //the returned String will be the UUID of the NPC
	
	public void removeNPC(Player p, UUID UUID); //removes an ncp from the world
	
}
