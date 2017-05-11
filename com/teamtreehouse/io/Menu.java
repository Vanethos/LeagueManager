package com.teamtreehouse.io;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;

import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.IndexOutOfBoundsException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.HashMap;
import com.teamtreehouse.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;

public class Menu {
  private BufferedReader mReader;
  private Map<String, String> mMainOptions;
  private Map<String, String> mTeamOptions;
  private Map<String, String> mReportOptions;
  private String bold = "\033[0;1m";
  private String unBold = "\033[1;0m";
  
  private String initFormat = " -=> "+bold+"%s%s"+unBold+"- %s%n";
  private String menuFormat = "   "+bold+"%d)"+unBold+" %s%n";
  
  private Set<Team> teams;
  private Player[] players;
  private Map<Player, Team> playerAssignment;
  private int numberOfPlayers;
  private int maxLen;
  
  public Menu() {
    mReader = new BufferedReader(new InputStreamReader(System.in));
    teams = new TreeSet<>();
    players = Players.load();
    numberOfPlayers = players.length;
    mMainOptions = new LinkedHashMap<>();
    mTeamOptions = new LinkedHashMap<>();
    mReportOptions = new LinkedHashMap<>();
    // create a map with the players and the teams
    playerAssignment = new HashMap<>();
    for (Player player : Arrays.asList(players)){
      playerAssignment.put(player, null); 
    }
    //System.out.println(playerAssignment);
    maxLen = maxStringLen(listOfPlayerNames());
    
    // = MENU - MAIN = //
    mMainOptions.put("create", "Create a new team");  
    mMainOptions.put("manage", "Manage teams - add new players or remove players from them");
    mMainOptions.put("reports", "Generate player's height and experience report or the teams roster");
    mMainOptions.put("quit", "The coach is now crying... Poor guy, he does not deserve this ya know?"); 
    
     // = MENU - TEAM MANAGEMENT = //
    mTeamOptions.put("add", "Adds a player to a team");
    mTeamOptions.put("remove", "Remove a player from a team");
    mTeamOptions.put("back", "Return to the main menu");
   
    
    // = MENU - REPORTS = //
    mReportOptions.put("stats", "Display the stats of a single team");
    mReportOptions.put("height", "Prints the players' height report.");
    mReportOptions.put("balance", "League Balance Report");  
    mReportOptions.put("back", "Return to the main menu");    
  }
  
  /*** ===== MENU ===== ***/
  public void run() {
    String choice = "";
    do {
      System.out.printf(bold+"%n= | = Welcome to the new Futbol program V2.0 = | ="+unBold+"%n%n%n");
      try {
       choice = chooseChoice(mMainOptions, initFormat);
       int option;
        switch(choice) {
         case "create":
            if (teams.size() + 1 <= numberOfPlayers) { 
              clearScreen();
              //System.out.printf("There are %d players in this season", numberOfPlayers);
              Team team = createTeam();
              if(!teams.add(team)) {
                System.out.printf("%n"+bold+"     ###Team was already added###"+unBold+"%n%n");
                waitForEnter();
              } else {
                System.out.printf("%n     == "+bold+"Team successfully created" + unBold + " ==%n%n");
                waitForEnter();
              }
              System.out.printf("There are now %d teams", teams.size());
            } else {
              clearScreen();
              System.out.printf(bold+"%n     ### You cannot have more teams than players ### %n%n"+unBold);
              waitForEnter();
            }
            break;
          case "manage":
            if (teams.size() == 0) { 
              clearScreen();
              System.out.printf(bold+"%n     ### There are no teams created. Please create a team first ### %n%n"+unBold);
              waitForEnter();
            } else {
              manage();
            }
            break;
          case "reports":
            if (teams.size() == 0) { 
              clearScreen();
              System.out.printf(bold+"%n     ### There are no teams created. Please create a team first ### %n%n"+unBold);
              waitForEnter();
            } else {
              reports();
            }
            break;
          
          case "quit":
            System.out.println("Goodbye. Goodbye my lover, *sniffs*");
          break;
         default:
            System.out.println("");
            System.out.println("Sorry we did not understand your query");
            waitForEnter();
            
            break;
        }
      } catch (IOException ioe) {
        System.out.println("There was an error in your query");
        ioe.printStackTrace();
      }
    } while(!choice.equals("quit")); 
  }
  
  private void manage() throws IOException {
     clearScreen();
     System.out.printf("---- "+bold+"TEAM MANAGEMENT"+unBold+" ----%n%n");
     String choice;
     Boolean permited = false;  
    Map<String, Player> playersMap;
    Map<String, Team> teamRoster = createTeamList();
    Set<String> addPlayer;
    Set<String> removePlayer;
    do {
        choice = chooseChoice(mTeamOptions, initFormat);  
        switch(choice) {
          case "add":  
            clearScreen();
            playersMap = createPlayerRoster("add");
            //addPlayer = playersMap.keySet();
            if (playersMap.size() > 0) {
              addOrRemovePlayer("add", playersMap, teamRoster);
            } else {
              System.out.printf("---- "+bold+"### ERROR ###"+unBold+" ----%n%n");
              System.out.println("All players are allocated to a team");
              waitForEnter();
            }
            permited = true;
            break;
          case "remove":
            clearScreen();
            playersMap = createPlayerRoster("remove");
            if (playersMap.size() > 0) { 
                addOrRemovePlayer("remove", playersMap, teamRoster);
            } else {
              System.out.printf("---- "+bold+"### ERROR ###"+unBold+" ----%n%n");
              System.out.println("Currently, there are no teams with players");
              waitForEnter();
            }
            permited = true;
            break;
          case "back":
            permited = true;
            clearScreen();
            break;
          default:
            System.out.printf("Sorry we did not understand your query%n%n%n"); 
            break;
          }
    } while(!permited);
  }
  
  private void reports() throws IOException {
   clearScreen();
   System.out.printf("---- "+bold+"REPORTS"+unBold+" ----%n%n");
   String choice;
   Boolean permited = false;  
    Map<String, Player> playersMap;
    Map<String, Team> teamRoster = createTeamList();
    do {
        choice = chooseChoice(mReportOptions, initFormat);  
        switch(choice) {
          case "stats":
            clearScreen();
            System.out.printf("---- "+bold+"TEAM REPORTS"+unBold+" ----%n%n");
            System.out.println("This will print out a detailed report for a single team");
            System.out.println("Start by selecting a team from the list");
            System.out.println("");
            waitForEnter();
            if (teams.size() == 0) { 
              clearScreen();
              System.out.printf(bold+"%n     ### There are no teams created. Please create a team first ### %n%n"+unBold);
              waitForEnter();
            } else {    
              String teamOption = menuChoice(teamRoster.keySet(), menuFormat);
              Team chosenTeam = teamRoster.get(teamOption);
              printTeamRoster(chosenTeam);
            }
            break;
          case "height":
            String teamOption = menuChoice(teamRoster.keySet(), menuFormat);
            Team chosenTeam = teamRoster.get(teamOption);
            clearScreen();
            printHeightReport(chosenTeam); 
            break;
          case "balance":
            clearScreen();
            System.out.printf("---- "+bold+"| LEAGUE BALANCE REPORT |"+unBold+" ----%n%n");
            for (Team team: teamRoster.values()) {
              System.out.println(team.leagueReport());
            }
            waitForEnter();
          case "back":
            permited = true;
            clearScreen();
            break;
          default:
            System.out.printf("Sorry we did not understand your query%n%n%n"); 
            break;
          }
    } while(!permited);
  }
  
  
  private String chooseChoice(Map<String, String> map, String format) throws IOException {
    System.out.println("Possible choices: ");
    List<String> keyList = new ArrayList<String>();
    keyList.addAll(map.keySet());
    int maxLen = maxStringLen(keyList);
    for (Map.Entry<String, String> key : map.entrySet()) {
      System.out.printf(format,
                         key.getKey(),
                         normalizeSpace(maxLen, key.getKey()),
                         key.getValue()
                        );
    }
    return mReader.readLine().trim().toLowerCase(); 
  }
  
  private String menuChoice(Set<String> set, String format) throws IOException {
    clearScreen();
    System.out.println("Possible choices: ");
    List<String> list = new ArrayList<String>();  
    list.addAll(set);
    String result = "";
    int count = 0;
    for (String item : list) {
      count++;
      if (count < 10) {
      System.out.printf(" "+format,
                         count,
                         item
                        );
      } else {
      System.out.printf(format,
                         count,
                         item
                        );        
      }
    }
    int choice;
    
    do {
      try {
        choice = Integer.parseInt(mReader.readLine().trim());
        result = list.get(choice-1);
      } catch (NumberFormatException nfe) {
        System.out.println(bold+"Error"+unBold+". Please choose a valid number:"); 
        choice = Integer.MAX_VALUE;
      } catch (IndexOutOfBoundsException ioe) {
        System.out.println(bold+"Error"+unBold+". Please choose a number within the range:"); 
        choice = Integer.MAX_VALUE;
      }
    } while(choice > list.size());
    return result; 
  }
  

  
  /*** ===== TEAM ===== ***/  
  private Team createTeam() throws IOException{
    String name = "";
    String coach = "";
    do {
      System.out.print("=> Please insert the name of the team: ");
      name = mReader.readLine().trim();
      if (name.length() == 0) {
        System.out.println("Please enter a valid name"); 
        System.out.println("");
      }
    } while(name.length() == 0);
    do {
      System.out.print("=> Please insert the name of the coach: ");
      coach = mReader.readLine().trim();
      if (name.length() == 0) {
        System.out.println("Please enter a valid name"); 
        System.out.println("");
      }
    } while(coach.length() == 0);
    return new Team(name, coach);
  }
  
  private Map<String, Team> createTeamList() {
    Map<String, Team> map = new TreeMap<>();
    for (Team team : teams) {
       map.put(team.toString(), team);
    }
    return map;
  }
  
  private void printTeamRoster(Team team) throws IOException{
      if (team.players.size() == 0) {
          System.out.printf("---- "+bold+"### ERROR ###"+unBold+" ----%n%n");
          System.out.println("This team has no players");
          waitForEnter();        
      } else {
        int count = 0;
        List<String> roster = new ArrayList<>();
        for (Player player : team.players) {    
        count++;
        String name = player.getFirstName()+" "+player.getLastName();
        String space = normalizeSpace(maxLen, name);
        String aux = String.format(bold+count+")"+unBold+"%s%s | Height: %d | Has experience? %s", 
                                  new Object[] {
                                     name,
                                     space,
                                     player.getHeightInInches(),
                                     (player.isPreviousExperience() ? "Yes" : "No "),
                                     ((playerAssignment.get(player) == null) ? "No current team" : playerAssignment.get(player))
                                   }
                                  );
         roster.add(aux);
       }
       clearScreen();
       //System.out.println(team.players);
       System.out.printf(bold + "--- | Team "+team.getName()+" | Experience level: %d in 100 points | ---%n%n"+unBold, team.experienceLevel());
       for (String s : roster) {
          System.out.println(s); 
       }
      System.out.println("");
      waitForEnter();
      } 
  }
 
  private void printHeightReport(Team team) throws IOException{
      if (team.players.size() == 0) {
          System.out.printf("---- "+bold+"### ERROR ###"+unBold+" ----%n%n");
          System.out.println("This team has no players");
          waitForEnter();        
      } else {
         System.out.printf(bold + "| ----- " + team.toString() + bold + " ----- |"+unBold +"%n%n");
         Map<String, List<Player>> report = team.heightReport();
         for (Map.Entry<String, List<Player>> height : report.entrySet()) {
           System.out.printf(" ===> " + bold + height.getKey() + unBold + ": %d players%n", height.getValue().size());
           if (height.getValue().size() > 0) {
              for (Player player : height.getValue()) {
                 System.out.printf(" ---- ---- %s%n", player.stats(maxLen));
              }
           }
           System.out.println("");
         }
         System.out.println("");
         System.out.println("");
         waitForEnter();
      }       
  }
  
  private Map<String, Team> filterTeamsNoPlayers(Map<String, Team> teamRoster) {
    Map <String, Team> result = new HashMap<>();  
    for (Map.Entry<String, Team> key : teamRoster.entrySet()) {
      if (key.getValue().players.size() > 0) {
        result.put(key.getKey(), key.getValue());
      }
    } 
    return result;
  }
  
  
  /*** ===== PLAYER ===== ***/  
  private Map<String, Player> createPlayerRoster(String type) {
    // TYPE
    // - add: list of all the players that are NOT in a team
    // - remove: list of all players that ARE in a team
    // - all: list all players
    Map<String, Player> result = new TreeMap<>();
    int maxLen = maxStringLen(listOfPlayerNames());
    for (Player player : players) {    
      String name = player.getFirstName()+" "+player.getLastName();
      String space = normalizeSpace(maxLen, name);
      String aux = String.format("%s%s | Height: %d | Has experience? %s | Current Team: %s ", 
                                new Object[] {
                                   name,
                                   space,
                                   player.getHeightInInches(),
                                   (player.isPreviousExperience() ? "Yes" : "No "),
                                   ((playerAssignment.get(player) == null) ? "No current team" : playerAssignment.get(player))
                                 }
                                );
        switch(type) {
          case "add":
            if (playerAssignment.get(player) == null) {
              result.put(aux, player);
            }
            break;
          case "remove":
            if (playerAssignment.get(player) != null) {
              result.put(aux, player);
            }
            break;
          case "all":
            result.put(aux, player);
            break;
          default:
            result.put(aux, player);
            break;
        }
      }
	return result;
  }
  
  private List<String> listOfPlayerNames() {
   List<String> names = new ArrayList<>();
   for(Player player : players) {
     names.add(player.getFirstName()+" "+player.getLastName());
   }
   return names;
  }
  
  private void addOrRemovePlayer(String com, Map<String, Player> playersMap, Map<String, Team> teamRoster) throws IOException {
    // com, as in command, can be:
    // --- add - adds the player
    // --- remove - removes the player from the team
    Team chosenTeam;
    Player chosenPlayer;
    String playerOption;
    String teamOption;
    System.out.printf("---- "+bold+"| "+com.toUpperCase()+" PLAYER |"+unBold+" ----%n%n");
    if(com.equals("add")) {
      System.out.println("You will be presented with a list of possible players.");
      System.out.println("Start by choosing a player from the list.");
      waitForEnter();
      clearScreen();
      playerOption = menuChoice(playersMap.keySet(), menuFormat);
      chosenPlayer = playersMap.get(playerOption);
      clearScreen();
      //System.out.println(playerAssignment);
      System.out.printf("Please choose a team to "+bold+com+unBold+" %s%n", chosenPlayer.toString());
      waitForEnter();
      teamOption = menuChoice(teamRoster.keySet(), menuFormat);
      chosenTeam = teamRoster.get(teamOption);
      clearScreen();
      Boolean added = chosenTeam.players.add(chosenPlayer);
      //System.out.printf("Player was %s added%n%n", added ? "" : "not");
      playerAssignment.put(chosenPlayer, chosenTeam);
      System.out.printf("Player "+bold+"%s"+unBold+" was added to "+bold+"%s"+unBold+"%n%n", chosenPlayer.toString(), chosenTeam.getName());
    } else {
      System.out.println("You will be presented with a list of teams.");
      System.out.println("Start by choosing a team from the list.");
      waitForEnter();
      clearScreen();
      Map<String, Team> teamsWithPlayers = filterTeamsNoPlayers(teamRoster);
      teamOption = menuChoice(teamsWithPlayers.keySet(), menuFormat);
      chosenTeam = teamRoster.get(teamOption);
      clearScreen();
      //System.out.println(playerAssignment);
      System.out.printf("Please choose a player to "+bold+"remove from"+unBold+" %s%n%n", chosenTeam.toString());
      Map<String, Player> removePlayer = new HashMap<>();
      for (Player player : chosenTeam.players) {
        removePlayer.put(player.stats(maxLen), player); 
      }
      playerOption = menuChoice(removePlayer.keySet(), menuFormat);
      chosenPlayer = removePlayer.get(playerOption);
      //System.out.println(chosenPlayer);
      chosenTeam.players.remove(chosenPlayer);
      playerAssignment.put(chosenPlayer, null);
      clearScreen();
      System.out.printf("Player "+bold+"%s"+unBold+" was removed from "+bold+"%s"+unBold+"%n%n", chosenPlayer.toString(), chosenTeam.toString());      
    }
    //System.out.println(playerAssignment);
    waitForEnter();
  }
  
  /*** ===== MISC ===== ***/  
  private int maxStringLen(List<String> list) {
    int result = 0;
    for (String s : list) {
      result = (s.length()+1 > result) ? s.length()+1 : result;
   }
    return result + 1;
  }
  
  private String normalizeSpace(int maxLen, String name) {
   String space = ""; 
    for (int i = 0; i < maxLen - name.length(); i++) {
      space += " "; 
    }
    return space;
  }
  
  private void clearScreen() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
   } 
  
  private void waitForEnter() throws IOException {
    System.out.println("Press Enter to Continue");
    mReader.readLine();
    clearScreen();
  }
  
}