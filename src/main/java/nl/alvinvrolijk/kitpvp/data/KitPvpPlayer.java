package nl.alvinvrolijk.kitpvp.data;

import nl.alvinvrolijk.kitpvp.KitPvP;
import nl.alvinvrolijk.kitpvp.utils.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class KitPvpPlayer {

    private static HashMap<UUID, KitPvpPlayer> registry = new HashMap<>();

    private Player player;
    private int kills;
    private int deaths;

    public KitPvpPlayer(Player player, int kills, int deaths) {
        this.player = player;
        this.kills = kills;
        this.deaths = deaths;
    }

    public Player getPlayer() { return this.player; }

    public int getKills() { return this.kills; }

    public int getDeaths() { return this.deaths; }

    public void setKills(int kills) {
        Scoreboard.updateScoreboard(Objects.requireNonNull(KitPvpPlayer.getPlayer(player.getUniqueId())));
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        Scoreboard.updateScoreboard(Objects.requireNonNull(KitPvpPlayer.getPlayer(player.getUniqueId())));
        this.deaths = deaths;
    }

    public static void registerPlayer(KitPvpPlayer kitPvpPlayer) {
        Scoreboard.updateScoreboard(kitPvpPlayer);

        registry.put(kitPvpPlayer.player.getUniqueId(), kitPvpPlayer);
    }

    public static void unregisterPlayer(KitPvpPlayer kitPvpPlayer) {
        KitPvpPlayer.savePlayerData(kitPvpPlayer);

        registry.remove(kitPvpPlayer.player.getUniqueId(), kitPvpPlayer);
    }

    // Update player data in MySQL
    public static void savePlayerData(KitPvpPlayer kitPvpPlayer) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(KitPvP.kitPvP, () -> {
            String playerSql = "UPDATE `stats` SET `kills`='" + kitPvpPlayer.getKills() + "',`deaths`='" + kitPvpPlayer.getDeaths() + "' WHERE uuid='" + kitPvpPlayer.getPlayer().getUniqueId() + "';";
            PreparedStatement playerStmt = null;
            try {
                playerStmt = MySQL.connection.prepareStatement(playerSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (playerStmt != null) {
                    // Execute query
                    playerStmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Retrieve player data from MySQL
    public static void initializePlayerData(Player player) {
        Scoreboard.createScoreboard(player); // Create scoreboard

        Bukkit.getServer().getScheduler().runTaskAsynchronously(KitPvP.kitPvP, () -> {
            String blockSql = "SELECT * FROM stats WHERE uuid='" + player.getUniqueId()  + "'";
            PreparedStatement blocksStmt = null;
            try {
                blocksStmt = MySQL.connection.prepareStatement(blockSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ResultSet blockResults = null;
            try {
                if (blocksStmt != null) {
                    blockResults = blocksStmt.executeQuery();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (blockResults != null) {
                    if (blockResults.next()) {
                        // Register a new KitPvpPlayer object
                        KitPvpPlayer.registerPlayer(new KitPvpPlayer(player, blockResults.getInt("kills"), blockResults.getInt("deaths")));
                    } else {
                        // Insert a new player in MySQL if none exists
                        String playerSql = "INSERT INTO stats(uuid, kills, deaths) VALUES (?, ?, ?);";
                        PreparedStatement playerStmt = null;
                        try {
                            playerStmt = MySQL.connection.prepareStatement(playerSql);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (playerStmt != null) {
                                playerStmt.setString(1, String.valueOf(player.getUniqueId())); // UUID
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (playerStmt != null) {
                                playerStmt.setInt(2, 0); // Kills
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (playerStmt != null) {
                                playerStmt.setInt(3, 0); // Deaths
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (playerStmt != null) {
                                playerStmt.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // Register a new KitPvpPlayer object
                        KitPvpPlayer.registerPlayer(new KitPvpPlayer(player, 0, 0));
                    }
                }
            } catch (SQLException e) {
                // Error
                e.printStackTrace();
            }
        });
    }

    public static KitPvpPlayer getPlayer(UUID uuid) {
        if (registry.containsKey(uuid)) return registry.get(uuid);
        return null;
    }

    public static Boolean containsPlayer(UUID uuid) {
        return registry.containsKey(uuid);
    }
}
