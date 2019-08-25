package drugdealing.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CreateNPCTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) {
			String uncompleteArg = args[0];
			if ("producer".startsWith(uncompleteArg)) {
				suggestions.add("producer");
			}
			if ("dealer".startsWith(uncompleteArg)) {
				suggestions.add("dealer");
			}
		}
		
		return suggestions;
	}
	
}
