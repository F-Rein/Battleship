package battleship;

public class Ship {
    int length;
    String name;
    String[] coordinates;


    int coordinateType;
    /*
    1 is common type e.g. A1B1
    2 is length 5 first type e.g. A10A7
    3 is length 5 second type e.g. A7A10
    4 is length 6 type e.g. A10B10
    probably a stupid way to do this
     */
    public Ship(int length, String name) {
        this.length = length;
        this.name = name;
        this.coordinates = new String[length];
    }
    public void addCoordinates(String[] str, int coordinateType){
        this.coordinates = str;
        this.coordinateType = coordinateType;
    }

}
