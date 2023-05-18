package de.gamingfelin.simpletimer.commands;

import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import de.gamingfelin.simpletimer.SimpleTimer;
import de.gamingfelin.simpletimer.utilitys.TimerFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TimerCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("pause")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    simpleTimer.isRunning = false;
                    return true;
                } else if (strings[0].equalsIgnoreCase("resume")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    simpleTimer.isRunning = true;
                    return true;
                } else if (strings[0].equalsIgnoreCase("reset")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    simpleTimer.isRunning = false;
                    simpleTimer.setTime(0);
                    return true;
                } else if (strings[0].equalsIgnoreCase("list")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    FileConfiguration config = simpleTimer.getConfig();
                    Set<String> timers = config.getKeys(false);
                    String timersString = "Alle Timer: ";
                    boolean firstEntry = true;
                    for (String timer : timers) {
                        if (firstEntry) {
                            timersString = timersString + timer;
                            firstEntry = false;
                        } else {
                            timersString = timersString + ", " + timer;
                        }
                    }
                    commandSender.sendMessage(timersString);
                    return true;
                }
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("set")) {
                    try {
                        int newTime = Integer.parseInt(strings[1]);
                        SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                        simpleTimer.isRunning = false;
                        simpleTimer.setTime(newTime);
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Please enter a valid number!");
                    }
                    return true;
                } else if (strings[0].equalsIgnoreCase("save")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    FileConfiguration config = simpleTimer.getConfig();
                    try {
                        String name = strings[1];
                        int time = simpleTimer.getTime();
                        config.set(name, time);
                        simpleTimer.saveConfig();
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Please enter a valid name!");
                    }
                    return true;
                } else if (strings[0].equalsIgnoreCase("load")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    FileConfiguration config = simpleTimer.getConfig();
                    try {
                        String name = strings[1];
                        int newTime = config.getInt(name);
                        simpleTimer.setTime(newTime);
                        simpleTimer.isRunning = false;
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Pleas enter a valid name!");
                    }
                    return true;
                } else if (strings[0].equalsIgnoreCase("show")) {
                    SimpleTimer simpleTimer = SimpleTimer.getPlugin();
                    FileConfiguration config = simpleTimer.getConfig();
                    try {
                        String name = strings[1];
                        int time = config.getInt(name);
                        String formattedTime = TimerFormatter.formatTime(time);
                        commandSender.sendMessage("Der Timer " + name + " steht auf: " + formattedTime);
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Please enter a valid name!");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> arguments1 = new ArrayList<>();
        List<String> result = new ArrayList<>();
        arguments1.add("pause");
        arguments1.add("resume");
        arguments1.add("set");
        arguments1.add("reset");
        arguments1.add("save");
        arguments1.add("load");

        arguments1.add("list");
        arguments1.add("show");

        if (strings.length == 1) {
            for (String string : arguments1){
                if (string.toLowerCase().startsWith(strings[0].toLowerCase())) {
                    result.add(s);
                }
            }
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("load") || strings[0].equalsIgnoreCase("show")) {
                FileConfiguration config = SimpleTimer.getPlugin().getConfig();
                for (String timerName : config.getKeys(false)) {
                    if (timerName.toLowerCase().startsWith(strings[1].toLowerCase())) {
                        result.add(timerName);
                    }
                }
            }
        } else {
            result.add("");
        }
        return result;
    }
}
