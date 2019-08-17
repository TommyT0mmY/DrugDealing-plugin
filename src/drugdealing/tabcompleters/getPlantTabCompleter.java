package drugdealing.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import drugdealing.Main;

public class getPlantTabCompleter implements TabCompleter {
	@SuppressWarnings("unused")
	private Main mainClass;
	public getPlantTabCompleter(Main mainClass) {
		this.mainClass = mainClass;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) {
			String uncompleteArg = args[0];
			if ("coke".startsWith(uncompleteArg)) {
				suggestions.add("coke");
			}
			if ("weed".startsWith(uncompleteArg)) {
				suggestions.add("weed");
			}
		}
		
		return suggestions;
	}
	
	
	
}
