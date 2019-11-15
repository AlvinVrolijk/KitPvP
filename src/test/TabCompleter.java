package nl.alvinvrolijk.kitpvp.commands;

import nl.unnamedmc.apocalypse.player.ApocalypsePlayer;
import nl.unnamedmc.apocalypse.player.CooldownManager;
import nl.unnamedmc.apocalypse.storage.TeamManager;
import nl.unnamedmc.apocalypse.utils.CustomMob;
import nl.unnamedmc.apocalypse.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> offlinePlayers = new ArrayList<>();
        ArrayList<String> teamMembers = new ArrayList<>();
        ArrayList<String> teams = new ArrayList<>();

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            offlinePlayers.add(Bukkit.getOfflinePlayer(offlinePlayer.getUniqueId()).getName());
        }

        for (TeamManager teamManager : TeamManager.getTeams()) {
            teams.add(teamManager.getTeam());
        }

        if (command.getName().equalsIgnoreCase("getitem")) {
            if (sender.hasPermission("apocalypse.command.getitem")) {
                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], Items.getItemList(), completions);
                Collections.sort(completions);
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("spawnmob")) {
            if (sender.hasPermission("apocalypse.command.spawnmob")) {
                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], CustomMob.getZombieList(), completions);
                Collections.sort(completions);
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("flyspeed")) {
            if (sender.hasPermission("apocalypse.command.flyspeed")) {
                ArrayList<String> examples = new ArrayList<>();
                examples.add("reset");
                examples.add("1");
                examples.add("0.9");
                examples.add("0.8");
                examples.add("0.7");
                examples.add("0.6");
                examples.add("0.5");
                examples.add("0.4");
                examples.add("0.3");
                examples.add("0.2");
                examples.add("0.1");
                examples.add("0");

                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], examples, completions);
                Collections.sort(completions);
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("gamemode")) {
            if (sender.hasPermission("apocalypse.command.gamemode")) {
                ArrayList<String> examples = new ArrayList<>();
                examples.add("survival");
                examples.add("creative");
                examples.add("adventure");
                examples.add("spectator");

                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], examples, completions);
                Collections.sort(completions);
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("removedeathban")) {
            if (sender.hasPermission("apocalypse.command.removedeathban")) {
                ArrayList<String> deathbans = new ArrayList<>();

                for (Map.Entry<UUID, Long> entry : CooldownManager.deathbans.entrySet()) {
                    deathbans.add(Bukkit.getOfflinePlayer(entry.getKey()).getName());
                }

                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], deathbans, completions);
                Collections.sort(completions);
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("language") || command.getName().equalsIgnoreCase("lang") || command.getName().equalsIgnoreCase("taal")) {
            if (args.length == 1) {
                ArrayList<String> languages = new ArrayList<>();
                languages.add("English");
                languages.add("Dutch");

                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], languages, completions);
                Collections.sort(completions);
                return completions;
            }
        }

//        if (command.getName().equalsIgnoreCase("rank")) {
//            if (args.length == 1) {
//                ArrayList<String> rank = new ArrayList<>();
//                if (sender.hasPermission("apocalypse.rank.youtube")) {
//                    rank.add("fly");
//                }
//
//                final List<String> completions = new ArrayList<>();
//                StringUtil.copyPartialMatches(args[0], rank, completions);
//                Collections.sort(completions);
//                return completions;
//            }
//        }

        if (command.getName().equalsIgnoreCase("admin")) {
            ArrayList<String> admin = new ArrayList<>();
            if (sender.hasPermission("apocalypse.admin")) {
                admin.add("reload");
                admin.add("logs");
                admin.add("log");
                admin.add("onlinetime");
                admin.add("modonlinetime");
                admin.add("addmember");
                admin.add("removemember");
                admin.add("createbackpack");
                admin.add("backpack");
                admin.add("ptime");
                admin.add("getbackpack");
                admin.add("updateitem");
            }

            if (args.length == 1) {
                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], admin, completions);
                Collections.sort(completions);
                return completions;
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("createbackpack")) {
                    ArrayList<String> sizes = new ArrayList<>();
                    sizes.add("Large");
                    sizes.add("Medium");
                    sizes.add("Small");

                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], sizes, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("getbackpack")) {
                    ArrayList<String> sizes = new ArrayList<>();
                    sizes.add("Large");
                    sizes.add("Medium");
                    sizes.add("Small");

                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], sizes, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("get")) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], Items.getItemList(), completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("spawn")) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], CustomMob.getZombieList(), completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("onlinetime")) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], offlinePlayers, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("modonlinetime")) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], offlinePlayers, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("claimblock")) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], teams, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("gm")) {
                    ArrayList<String> gamemodes = new ArrayList<>();

                    gamemodes.add("0");
                    gamemodes.add("1");
                    gamemodes.add("2");
                    gamemodes.add("3");

                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], gamemodes, completions);
                    Collections.sort(completions);
                    return completions;
                }
            }

            if (args[0].equalsIgnoreCase("addmember")) {
                if (args.length == 3) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[2], teams, completions);
                    Collections.sort(completions);
                    return completions;
                }
            }

            if (args[0].equalsIgnoreCase("removemember")) {
                if (args.length == 3) {
                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[2], teams, completions);
                    Collections.sort(completions);
                    return completions;
                }
            }
        }

        if (command.getName().equalsIgnoreCase("mod")) {
            if (args.length == 1) {
                ArrayList<String> mod = new ArrayList<>();
                if (sender.hasPermission("apocalypse.mod")) {
                    mod.add("toggle");
                    if (Objects.requireNonNull(ApocalypsePlayer.getPlayer((Player) sender)).hasModMode()) {
                        mod.add("createteam");
                        mod.add("setteam");
                        mod.add("deleteteam");
                        mod.add("teamlist");
                        mod.add("teaminfo");
                        mod.add("invsee");
                        mod.add("tp");
                        mod.add("tphere");
                        mod.add("spawn");
                        mod.add("spawnpoint");
                        mod.add("claimchunk");
                        mod.add("unclaimchunk");
                        mod.add("zpoints");
                    }
                }

                final List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], mod, completions);
                Collections.sort(completions);
                return completions;
            }

            if (Objects.requireNonNull(ApocalypsePlayer.getPlayer((Player) sender)).hasModMode()) {
                if (args[0].equalsIgnoreCase("zpoints")) {
                    if (args.length == 3) {
                        ArrayList<String> zpoints = new ArrayList<>();

                        zpoints.add("get");
                        zpoints.add("add");
                        zpoints.add("remove");
                        zpoints.add("set");

                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[2], zpoints, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }

                if (args[0].equalsIgnoreCase("setteam")) {
                    if (args.length == 3) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[2], teams, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }

                if (args[0].equalsIgnoreCase("deleteteam")) {
                    if (args.length == 2) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[1], teams, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }

                if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("tphere") || args[0].equalsIgnoreCase("invsee") || args[0].equalsIgnoreCase("bpsee")) {
                    ArrayList<String> worlds = new ArrayList<>();

                    for (World world : Bukkit.getWorlds()) {
                        String one = world.getName().replace("CraftWorld{name=", "");
                        String two = one.replace("}", "");
                        worlds.add(two);
                    }

                    if (args.length == 4) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[3], worlds, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }

                if (args[0].equalsIgnoreCase("spawn")) {
                    ArrayList<String> worlds = new ArrayList<>();
                    worlds.add("main");
                    worlds.add("archive");
                    worlds.add("event");
                    worlds.add("test");

                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], worlds, completions);
                    Collections.sort(completions);
                    return completions;
                }

                if (args[0].equalsIgnoreCase("claimchunk")) {
                    if (args.length == 2) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[1], teams, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }

                if (args[0].equalsIgnoreCase("teaminfo")) {
                    if (args.length == 2) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[1], teams, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }
            }
        }

        if (command.getName().equalsIgnoreCase("team")) {
            if (args.length == 1) {
                ArrayList<String> team = new ArrayList<>();

                if (!TeamManager.getTeamByPlayer(((Player) sender).getUniqueId()).getTeam().equals("None")) {
                    team.add("info");
                    team.add("leave");
                    team.add("invite");
                    team.add("kick");

                    final List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[0], team, completions);
                    Collections.sort(completions);
                    return completions;
                }
            }

            if (TeamManager.getTeamByPlayer(((Player) sender).getUniqueId()).getOwner().equals(((Player) sender).getUniqueId())) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("kick")) {
                        final List<String> completions = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[1], teamMembers, completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }
            }
        }

        return null;
    }
}
