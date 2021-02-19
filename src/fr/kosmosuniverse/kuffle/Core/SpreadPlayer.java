package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class SpreadPlayer {
	private static final Random random = new Random();

    public static boolean spreadPlayers(Player sender, double distance, double range, boolean teams, ArrayList<Player> players) {
        final double x = sender.getLocation().getX();
        final double z = sender.getLocation().getZ();

        if (distance < 0.0D) {
            sender.sendMessage(ChatColor.RED + "Distance is too small.");
            return false;
        }

        if (range < distance + 1.0D) {
            sender.sendMessage(ChatColor.RED + "Max range is too small.");
            return false;
        }

        World world = sender.getWorld();

        if (world == null) {
            return true;
        }

        final double xRangeMin = x - range;
        final double zRangeMin = z - range;
        final double xRangeMax = x + range;
        final double zRangeMax = z + range;

        final int spreadSize = teams ? getTeams(players) : players.size();

        final Location[] locations = getSpreadLocations(world, spreadSize, xRangeMin, zRangeMin, xRangeMax, zRangeMax);
        final int rangeSpread = range(world, distance, xRangeMin, zRangeMin, xRangeMax, zRangeMax, locations);

        if (rangeSpread == -1) {
            sender.sendMessage(String.format("Could not spread %d %s around %s,%s (too many players for space - try using spread of at most %s)", spreadSize, teams ? "teams" : "players", x, z));
            return false;
        }

        final double distanceSpread = spread(world, players, locations, teams);

        if (locations.length > 1) {
            sender.sendMessage(String.format("(Average distance between %s is %s blocks apart after %s iterations)", teams ? "teams" : "players",  String.format("%.2f", distanceSpread), rangeSpread));
        }
        return true;
    }

    private static int range(World world, double distance, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax, Location[] locations) {
        boolean flag = true;
        double max;

        int i;

        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            max = Float.MAX_VALUE;

            Location loc1;
            int j;

            for (int k = 0; k < locations.length; ++k) {
                Location loc2 = locations[k];

                j = 0;
                loc1 = new Location(world, 0, 0, 0);

                for (int l = 0; l < locations.length; ++l) {
                    if (k != l) {
                        Location loc3 = locations[l];
                        double dis = loc2.distanceSquared(loc3);

                        max = Math.min(dis, max);
                        if (dis < distance) {
                            ++j;
                            loc1.add(loc3.getX() - loc2.getX(), 0, 0);
                            loc1.add(loc3.getZ() - loc2.getZ(), 0, 0);
                        }
                    }
                }

                if (j > 0) {
                    loc2.setX(loc2.getX() / j);
                    loc2.setZ(loc2.getZ() / j);
                    double d7 = Math.sqrt(loc1.getX() * loc1.getX() + loc1.getZ() * loc1.getZ());

                    if (d7 > 0.0D) {
                        loc1.setX(loc1.getX() / d7);
                        loc2.add(-loc1.getX(), 0, -loc1.getZ());
                    } else {
                        double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
                        loc2.setX(x);
                        loc2.setZ(z);
                    }

                    flag = true;
                }

                boolean swap = false;

                if (loc2.getX() < xRangeMin) {
                    loc2.setX(xRangeMin);
                    swap = true;
                } else if (loc2.getX() > xRangeMax) {
                    loc2.setX(xRangeMax);
                    swap = true;
                }

                if (loc2.getZ() < zRangeMin) {
                    loc2.setZ(zRangeMin);
                    swap = true;
                } else if (loc2.getZ() > zRangeMax) {
                    loc2.setZ(zRangeMax);
                    swap = true;
                }
                if (swap) {
                    flag = true;
                }
            }

            if (!flag) {
                Location[] locs = locations;
                int i1 = locations.length;

                for (j = 0; j < i1; ++j) {
                    loc1 = locs[j];
                    if (world.getHighestBlockYAt(loc1) == 0) {
                        double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
                        locations[i] = (new Location(world, x, 0, z));
                        loc1.setX(x);
                        loc1.setZ(z);
                        flag = true;
                    }
                }
            }
        }

        if (i >= 10000) {
            return -1;
        } else {
            return i;
        }
    }

    private static double spread(World world, List<Player> list, Location[] locations, boolean teams) {
        double distance = 0.0D;
        int i = 0;
        HashMap<Team, Location> hashmap = new HashMap<Team, Location>();

        for (int j = 0; j < list.size(); ++j) {
            Player player = list.get(j);
            Location location;

            if (teams) {
                Team team = player.getScoreboard().getEntryTeam(player.getDisplayName());

                if (!hashmap.containsKey(team)) {
                    hashmap.put(team, locations[i++]);
                }

                location = hashmap.get(team);
            } else {
                location = locations[i++];
            }

            player.teleport(new Location(world, Math.floor(location.getX()) + 0.5D, world.getHighestBlockYAt((int) location.getX(), (int) location.getZ()), Math.floor(location.getZ()) + 0.5D));
            double value = Double.MAX_VALUE;

            for (int k = 0; k < locations.length; ++k) {
                if (location != locations[k]) {
                    double d = location.distanceSquared(locations[k]);
                    value = Math.min(d, value);
                }
            }

            distance += value;
        }

        distance /= list.size();
        return distance;
    }

    private static int getTeams(List<Player> players) {
        ArrayList<Team> teams = new ArrayList<Team>();

        for (Player player : players) {
        	if (!teams.contains(player.getScoreboard().getEntryTeam(player.getDisplayName()))) {
                teams.add(player.getScoreboard().getEntryTeam(player.getDisplayName()));	
        	}
        }

        return teams.size();
    }

    private static Location[] getSpreadLocations(World world, int size, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax) {
        Location[] locations = new Location[size];

        for (int i = 0; i < size; ++i) {
            double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
            double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
            locations[i] = (new Location(world, x, 0, z));
        }

        return locations;
    }
}
