package battleship;

import java.io.IOException;
import java.util.Scanner;

public class GameLogic {

    public static void start(){
        GameGrid player = new GameGrid();
        player.addName("Player 1");
        GameGrid player2 = new GameGrid();
        player.addName("Player 2");
        GameGrid[] playerArr = new GameGrid[4];
        playerArr[0] = player;
        playerArr[1] = player2;
        player.ships = 17;
        player2.ships = 17;

        GameGrid empty = new GameGrid();
        GameGrid empty2 = new GameGrid();

        empty.printGrid();
        Ship[] shipArr = new Ship[5];
        shipArr[0] = new Ship(5, "Aircraft Carrier");
        shipArr[1] = new Ship(4, "Battleship");
        shipArr[2] = new Ship(3, "Submarine");
        shipArr[3] = new Ship(3, "Cruiser");
        shipArr[4] = new Ship(2, "Destroyer");
        for (int i = 0; i < 5; i++) {
            placeShip(shipArr[i], player);
            player.printGrid();
        }
        promptEnterKey();
        for (int i = 0; i < 5; i++) {
            placeShip(shipArr[i], player2);
            player2.printGrid();
        }
        promptEnterKey();

        boolean player1 = true;
        do {
            System.out.println("Player 1 Ships: " + player2.ships);
            System.out.println("Player 2 Ships: " + player.ships);
            if (player1) {
                empty.printGrid();
                System.out.println("---------------------");
                playerArr[0].printGrid();
                String hitOrMiss = fireAShot(playerArr[1], empty);
                player1 = false;
                System.out.println(hitOrMiss);
            } else {
                empty2.printGrid();
                System.out.println("---------------------");
                playerArr[1].printGrid();
                String hitOrMiss = fireAShot(playerArr[0], empty2);
                player1 = true;
                System.out.println(hitOrMiss);
            }
            promptEnterKey();
        } while (player.ships > 0 && player2.ships > 0);
    }
    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String fireAShot(GameGrid player, GameGrid empty) {
        do {
            try (Scanner scanner = new Scanner(System.in)) {
                String input = scanner.nextLine();
                System.out.println("Input: " + input);
                if (input.isEmpty()) {
                    System.out.println("Error: Input is empty! Try again:%n");
                    continue;
                }
                int validShot = validShot(input);
                if (validShot == 0) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }
                String[] shot = input.split("");
                int result = GameGrid.addShot(shot, player, empty);
                return switch (result) {
                    case 1 -> {
                        player.ships--;
                        if (player.ships == 0) {
                            yield "You sank the last ship. You won. Congratulations!";
                        } else {
                            yield "You hit a ship!";
                        }
                    }
                    case 2 -> "You missed!";
                    case 3 -> "You already fired at this coordinate";
                    case 4 -> {
                        player.ships--;
                        if (player.ships == 0) {
                            yield "You sank the last ship. You won. Congratulations!";
                        } else {
                            yield "You sank a ship! Specify a new target:";
                        }
                    }
                    case 0 -> "Unknown error in addShot";
                    default -> "addShot did not return a valid int";
                };
            } catch (Exception e) {
                System.out.println(e);
            }
            break;
        } while (true);
        return "This shouldn't be reached";
    }
    private static void placeShip(Ship ship, GameGrid player) {
        System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.name, ship.length);
        boolean success = false;
        do {
            try (Scanner scanner = new Scanner(System.in)) {
                String input = scanner.nextLine();
                String coordinates = input.replaceAll("\\s", "");
                if (coordinates.isEmpty()) {
                    System.out.println("Error: Input is empty! Try again:%n");
                    continue;
                }
                int isValid = validInput(coordinates);
                if (isValid == 0) {
                    System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.name);
                    continue;
                }
                String[] splitCoordinates = coordinates.split("");
                ship.addCoordinates(splitCoordinates, isValid);
                int result = (GameGrid.addShip(ship, player));
                // result code 10 is wrong length
                // code 11 is same coordinates in both fields
                // 12 is already occupied space
                // 0 is generic error
                // 1 is successful add
                switch (result) {
                    case 0:
                        System.out.println("Error");
                        break;
                    case 1:
                        success = true;
                        break;
                    case 10:
                        System.out.printf("Error: Wrong length of the %s! Try again:%n", ship.name);
                        break;
                    case 11:
                        System.out.println("Error: Same coordinates in both fields! Try again:");
                        break;
                    case 12:
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        break;
                    case 13:
                        System.out.println("Error! Can't place ships diagonally! Try again:");
                        break;
                    default:
                        System.out.println("Error!!! Wtf? Try again maybe:");
                        break;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } while (!success);
    }

    private static int validShot(String str) {
        if (str.length() > 3 || str.length() < 2) {
            System.out.println("Error! Incorrect length of input string!");
            return 0;
        }
        switch (str.length()) {
            case 2:
                if (str.matches("[A-J]+[1-9]")) {
                    return 1;
                } else {
                    System.out.println("Error! Invalid coordinates [Err: 2]");
                    return 0;
                }
            case 3:
                if (str.matches("[A-J]+1+0")) {
                    return 1;
                } else {
                    System.out.println("Error! Invalid coordinates [Err: 3]");
                    return 0;
                }
            default:
                System.out.println("Error! Invalid coordinates [Err: validShot unknown err]");
                return 0;
        }
    }
    private static int validInput(String str) {

        //if (str.isEmpty()) return 0;

        if (str.length() > 6 || str.length() < 4) {
            System.out.println("Error! Incorrect length of input string!");
            return 0;
        }
        //System.out.println("String: " + str);
        switch (str.length()) {
            case 4:
                if (str.matches("[A-J]+[1-9]+[A-J]+[1-9]")) {
                    return 1;
                } else {
                    System.out.println("Error! Invalid coordinates [Err: 4]");
                    return 0;
                }
            case 5:
                if (str.matches("[A-J]+1+0+[A-J]+[1-9]")) {
                    return 2;
                } else {
                    if (str.matches("[A-J]+[1-9]+[A-J]+1+0")) {
                        return 3;
                    } else {
                        System.out.println("Error! Invalid coordinates [Err: 5]");
                        return 0;
                    }
                }
            case 6:
                if (str.matches("[A-J]+1+0+[A-J]+1+0")) {
                    return 4;
                } else {
                    System.out.println("Error! Invalid coordinates [Err: 6]");
                    return 0;
                }
            default:
                System.out.println("Error! Invalid coordinates [Err: validInput unknown err]");
                return 0;
        }
    }
}
