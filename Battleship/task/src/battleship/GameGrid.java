package battleship;

public class GameGrid {
    String[][] grid;
    String name;
    int ships;
    public GameGrid() {
        this.grid = new String[11][11];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (i == 0) {
                    this.grid[i][j] = Integer.toString(j);
                } else if (j == 0) {
                    this.grid[i][j] = Character.toString('A' + (i-1));
                } else {
                    this.grid[i][j] = "~";
                }
            }
        }
    }
    public void addName(String name){
        this.name = name;
    }
    public static int addShip(Ship ship, GameGrid player) {
        int x, y, a, b;
        //System.out.println(ship.coordinateType);
        switch (ship.coordinateType) {
            case 1 -> {
                x = ship.coordinates[0].charAt(0) - 64;
                y = Integer.parseInt(ship.coordinates[1]);
                a = ship.coordinates[2].charAt(0) - 64;
                b = Integer.parseInt(ship.coordinates[3]);
            }
            case 2 -> {
                x = ship.coordinates[0].charAt(0) - 64;
                y = Integer.parseInt(ship.coordinates[1] + ship.coordinates[2]);
                a = ship.coordinates[3].charAt(0) - 64;
                b = Integer.parseInt(ship.coordinates[4]);
            }
            case 3 -> {
                x = ship.coordinates[0].charAt(0) - 64;
                y = Integer.parseInt(ship.coordinates[1]);
                a = ship.coordinates[2].charAt(0) - 64;
                b = Integer.parseInt(ship.coordinates[3] + ship.coordinates[4]);
            }
            case 4 -> {
                x = ship.coordinates[0].charAt(0) - 64;
                y = Integer.parseInt(ship.coordinates[1] + ship.coordinates[2]);
                a = ship.coordinates[3].charAt(0) - 64;
                b = Integer.parseInt(ship.coordinates[4] + ship.coordinates[5]);
            }
            default -> {
                System.out.println("Error in coordinateType during AddShip method");
                return 0;
            }
        }
        //System.out.println("Coordinates: X=" + x + " Y=" + y + " A=" + a + " B=" + b);
        if (x == a && y == b) {
            return 11;
        }
        if (x != a && y != b) {
            return 13;
        }
        if (y != b) {
            if (Math.abs(y - b) == ship.length-1) {
                if (!player.isOccupied(x, Math.min(y, b), ship.length, 0)) {
                    for (int i = Math.min(y, b); i <= Math.max(y, b); i++) {
                        player.grid[x][i] = "O";
                    }
                } else {
                    return 12;
                }
            } else {
                return 10;
            }
        } else {
            if (Math.abs(x - a) == ship.length-1) {
                if (!player.isOccupied(Math.min(x, a), y, ship.length, 1)) {
                    for (int i = Math.min(x, a); i <= Math.max(x, a); i++) {
                        player.grid[i][y] = "O";
                        // i = 9; y = 2
                    }
                } else {
                    return 12;
                }
            } else {
                return 10;
            }
        }
        return 1;
    }

    public static int addShot(String[] shot, GameGrid player, GameGrid empty) {
        int x, y;
        switch (shot.length) {
            case 2 -> {
                x = shot[0].charAt(0) - 64;
                y = Integer.parseInt(shot[1]);
            }
            case 3 -> {
                x = shot[0].charAt(0) - 64;
                y = Integer.parseInt(shot[1] + shot[2]);
            }
            default -> {
                System.out.println("Error in addShot");
                return 0;
            }
        }
        switch (player.grid[x][y]) {
            case "O":
                empty.grid[x][y] = "X";
                player.grid[x][y] = "X";
                if (player.shipSunk(x, y)) {
                    return 4;
                } else {
                    return 1;
                }
            case "~":
                empty.grid[x][y] = "M";
                player.grid[x][y] = "M";
                return 2;
            case "X":
                return 3;
            case "M":
                return 2;
            default:
                return 0;
        }
    }
    public boolean shipSunk(int a, int b) {
        boolean sunk = true;
        //System.out.println((a + " " + b));
        if (!(a-1 < 0)) {
            if ("O".equals(this.grid[a-1][b])) {
                System.out.println((a-1) + " " + b);
                sunk = false;
            }
        }
        if (!(b-1 < 0)) {
            if ("O".equals(this.grid[a][b-1])) {
                System.out.println(a + " " + (b-1));
                sunk = false;
            }
        }
        if (!(b+1 >= this.grid.length)) {
            if ("O".equals(this.grid[a][b+1])) {
                System.out.println(a + " " + (b+1));
                sunk = false;
            }
        }
        if (!(a+1 >= this.grid.length)) {
            if ("O".equals(this.grid[a+1][b])) {
                System.out.println((a-1) + " " + b);
                sunk = false;
            }
        }
        return sunk;
    }
    public boolean isOccupied(int a, int b, int length, int direction) {
        switch (direction) {
            case 1 -> {
                if (a-1 > 0) {
                    if ("O".equals(this.grid[a - 1][b])) {
                        return true;
                    }
                }
                if ((a + length) < grid.length) {
                    if ("O".equals(this.grid[a + length][b])) {
                        return true;
                    }
                }
                for (int i = 0; i < length; i++) {
                    if ((a + i < grid.length) || (b - 1 > 0) || (b + 1 > grid.length)) {
                        if (("O".equals(this.grid[a + i][b - 1])) || ("O".equals(this.grid[a + i][b + 1]))) {
                            return true;
                        }
                    }
                }
                return false;
            }
            case 0 -> { //TODO change this case to be the same as case 1
                if (!this.grid[a][b - 1].isEmpty()) {
                    if ("O".equals(this.grid[a][b - 1])) {
                        return true;
                    }
                } else if (!this.grid[a][b + length].isEmpty()) {
                    if ("O".equals(this.grid[a][b + length])) {
                        return true;
                    }
                } else {
                    for (int i = 0; i < length; i++) {
                        if (!(this.grid[a - 1][b + i].isEmpty() || this.grid[a + 1][b + i].isEmpty())) {
                            if (("O".equals(this.grid[a - 1][b + i])) || ("O".equals(this.grid[a + 1][b + i]))) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            default -> {
                System.out.println("Error: isOccupied default statement!");
                return true;
            }
        }
    }
    public void printGrid(){
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (i == 0 && j==0) {
                    System.out.print("  ");
                } else {
                    System.out.print(this.grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

}
