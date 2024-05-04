package me.lutto.shardsmp.commands.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LivesTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {


        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("give", "remove", "get", "set"), new ArrayList<>());
        } else if (args.length == 2) {

            List<String> playerNames = new ArrayList<>();
            for (Player playerName : Bukkit.getOnlinePlayers()) {
                playerNames.add(playerName.getName());
            }

            return StringUtil.copyPartialMatches(args[1], playerNames, new ArrayList<>());

        }

        return null;
    }

}
