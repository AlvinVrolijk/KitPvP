package nl.alvinvrolijk.kitpvp.utils;

import nl.alvinvrolijk.kitpvp.data.KitPvpPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Scoreboard {

    public static void createScoreboard(Player player) {
        Scoreboard helper = Scoreboard.createScore(player); // Create new score
        helper.setTitle("&6&lKitPvP");
        helper.setSlot(10, "&6Kills:");
        helper.setSlot(9, "&eLoading..."); // Set placeholder
        helper.setSlot(8, "  ");
        helper.setSlot(7, "&6Deaths:");
        helper.setSlot(6, "&eLoading..."); // Set placeholder
        helper.setSlot(5, "   ");
        helper.setSlot(4, "&6K/D Ratio:");
        helper.setSlot(3, "&eLoading..."); // Set placeholder
        helper.setSlot(2, "    ");
        helper.setSlot(1, "&cplay.dusdavidgames.nl");
    }

    public static void updateScoreboard(KitPvpPlayer kitPvpPlayer) {
        if (Scoreboard.hasScore(kitPvpPlayer.getPlayer())) { // Check if player has a score
            Scoreboard helper = Scoreboard.getByPlayer(kitPvpPlayer.getPlayer()); // Get scoreboard helper

            // Get data
            int kills = kitPvpPlayer.getKills(); // Get kills
            int deaths = kitPvpPlayer.getDeaths(); // Get deaths


            // Calculate K/D ratio (WARNING: this can output unlimited digits)
            String killDeathRatio;

            try {
                // In case a integer is 0 (it is not possible to divide by 0, remember?)
                if (kills == 0 && deaths == 0) {
                    killDeathRatio = String.valueOf(0.00);
                } else if (kills != 0 && deaths == 0) {
                    killDeathRatio = String.valueOf(kills);
                } else if (kills == 0 && deaths != 0) {
                    killDeathRatio = String.valueOf(0);
                } else {
                    killDeathRatio = String.format(Locale.US, "%.2f", (double) kills / deaths);
                }
            } catch (NullPointerException exception) {
                killDeathRatio = "&cError";
            }

            // Set data
            helper.setSlot(9, "&e" + kills); // Update kills
            helper.setSlot(6, "&e" + deaths); // Update deaths
            helper.setSlot(3, "&e" + killDeathRatio); // Update kill-death ratio
        }
    }

    public static void removeScoreboard(Player player) {
        if (Scoreboard.hasScore(player)) {
            Scoreboard.removeScore(player);
        }
    }

    private static HashMap<UUID, Scoreboard> players = new HashMap<>();

    public static boolean hasScore(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public static Scoreboard createScore(Player player) {
        return new Scoreboard(player);
    }

    public static Scoreboard getByPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static Scoreboard removeScore(Player player) {
        return players.remove(player.getUniqueId());
    }

    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective sidebar;

    private Scoreboard(Player player) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy", "sidebar");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Create Teams
        for(int i=1; i<=15; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }

        player.setScoreboard(scoreboard);
        players.put(player.getUniqueId(), this);
    }

    public void setTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        sidebar.setDisplayName(title.length()>32 ? title.substring(0, 32) : title);
    }

    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        text = ChatColor.translateAlternateColorCodes('&', text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    public void setSlotsFromList(List<String> list) {
        while(list.size()>15) {
            list.remove(list.size()-1);
        }

        int slot = list.size();

        if(slot<15) {
            for(int i=(slot +1); i<=15; i++) {
                removeSlot(i);
            }
        }

        for(String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }
}
