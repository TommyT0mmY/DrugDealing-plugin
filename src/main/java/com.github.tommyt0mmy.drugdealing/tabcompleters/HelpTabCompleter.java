package com.github.tommyt0mmy.drugdealing.tabcompleters;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class HelpTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            String uncompleteArg = args[0];
            if ("drugdealing".startsWith(uncompleteArg)) {
                suggestions.add("drugdealing");
            }
            if ("getplant".startsWith(uncompleteArg)) {
                suggestions.add("getplant");
            }
            if ("getdrug".startsWith(uncompleteArg)) {
                suggestions.add("getdrug");
            }
            if ("setnpc".startsWith(uncompleteArg)) {
                suggestions.add("setnpc");
            }
            if ("removenpc".startsWith(uncompleteArg)) {
                suggestions.add("removenpc");
            }
            if ("permissions".startsWith(uncompleteArg)) {
                suggestions.add("permissions");
            }
        } else if (args.length == 2) {
            String argBefore = args[0];
            String uncompleteArg = args[1];

            //aliases (not in use rn)
            //argBefore = argBefore.replaceAll("", "");

            if (argBefore.equals("permissions")) {
                if ("1".startsWith(uncompleteArg)) {
                    suggestions.add("1");
                }
                if ("2".startsWith(uncompleteArg)) {
                    suggestions.add("2");
                }
            } else {
                if ("1".startsWith(uncompleteArg)) {
                    suggestions.add("1");
                }
            }
        }

        return suggestions;
    }

}
