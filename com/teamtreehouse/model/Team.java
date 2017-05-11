package com.teamtreehouse.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Team implements Comparable{
  private String mName;
  private String mCoach;
  public Set<Player> players;
  private String bold = "\033[0;1m";
  private String unBold = "\033[1;0m";
  
  public Team(String name, String coach) {
    mName = name;
    mCoach = coach;
    players = new   HashSet<Player>();
  }
  
  public String getName() {
    return mName; 
  }
  
  @Override
  public int compareTo(Object obj) {
   Team other = (Team) obj; 
    if(mName.equals(other.mName) && mCoach.equals(other.mCoach)) {
      //System.out.println("Same object");
      return 0; 
    } 
    /* If we want the same team with different coaches, add this
     else if (mName.equals(other.mName) && !mCoach.equals( other.mCoach)) {
      System.out.println("Same name different coach");
      return mCoach.compareTo(other.mCoach); 
    } */
    else {
      //System.out.println("Nothing");
      return mName.compareTo(other.mName);
    }
  }
  
  /*** NOTE TO REVIEWER:
  * In the "How you will be graded section", it is stated that we needed another data type for the map "Report uses a map like solution to properly report experienced vs. inexperienced for each team"
  * One way to do this would be to have an additional data structure that, for each player, it displays the boolean value for experience. 
  * This method provides the same outcome, so if I may ask: what would be the more efficient way to tackle the problem? An additional data structure or the way I implemented it?
  * Thank you for your time and patience
  */
  public int numberOfExperiencedPlayers() {
    int count = 0;
    for (Player player : players) {
       count += player.isPreviousExperience() ? 1 : 0; 
    }
    return count;
  }
  
  public int experienceLevel() {
    int count = numberOfExperiencedPlayers() ;
    if (players.size() > 0) {
      return (100*count)/(players.size());
    } else {
      return 0; 
    }
  }
  
  public Map<String, List<Player>> heightReport() {
    Map<String, List<Player>> report = new HashMap<>();
    List<Player> s = new ArrayList<>();
    List<Player> m = new ArrayList<>();
    List<Player> l = new ArrayList<>();
    for (Player player: players) {
       if (player.getHeightInInches() < 41) {
          s.add(player);
       } else if (player.getHeightInInches() < 47) {
         m.add(player);
       } else {
         l.add(player); 
       }
    }
    report.put("[35 - 40]", s);
    report.put("[41 - 46]", m);
    report.put("[47 - 50]", l);
    return report;
  }
  
  public String leagueReport() {
    Map<String, List<Player>> heights = heightReport();
    int experiencedPlayers = numberOfExperiencedPlayers();
    return String.format(this.toString() + "%n"+
                         "-- Number of players: %1d%n" +
                         "-- Number of "+bold+" Experienced "+unBold+"Players: %2d | Number of "+bold+" Unexperienced "+unBold+"Players: %3d%n"+
                         "-- " + bold + "Height Report: %n" + 
                         "====> " + bold + "[35 - 40]: " + unBold + "%4d players%n" +
                         "====> " + bold + "[41 - 46]: " + unBold + "%5d players%n" +
                         "====> " + bold + "[47 - 50]: " + unBold + "%6d players%n",
                         this.players.size(),
                         experiencedPlayers,
                         (this.players.size() - experiencedPlayers),
                         heights.get("[35 - 40]").size(),
                         heights.get("[41 - 46]").size(),
                         heights.get("[47 - 50]").size()
                        );
  }
  
  @Override
  public String toString() {
    return bold + mName + unBold + ", trained by " + bold + mCoach + unBold + " with experience level " + bold + experienceLevel() + " out of 100"+ unBold; 
  }
}