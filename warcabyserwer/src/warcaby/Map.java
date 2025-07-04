package warcaby;
import java.io.Serializable;

public class Map implements Serializable {
    public Field[][] fields = null;  // [kolumna y ][wiersz  x ]
    public Map() {
        fields = new Field[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fields[i][j] = new Field();
            }
        }
    }
    void initMap() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    if (i < 3) {
                        fields[i][j].is_pawn_there = true;
                        fields[i][j].who = 'b';
                    } else if (i > 4) {
                        fields[i][j].is_pawn_there = true;
                        fields[i][j].who = 'w';
                    } else {
                        fields[i][j].who = 0;
                        fields[i][j].is_pawn_there = false;
                    }
                } else {
                    fields[i][j].is_pawn_there = false;
                    fields[i][j].who = 0;
                }
            }
        }
    }
    void printMap() {
        if(fields == null) {
            System.out.printf("null map\n");
            return;
        }
        System.out.println(" x 0 1 2 3 4 5 6 7");
        System.out.println("y +----------------");
        for (int row = 0; row < 8; row++) {
            System.out.print((row) + " |");
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print("  "); // jasne pole
                } else {
                    if (!fields[row][col].is_pawn_there) {
                        System.out.print(". ");
                    } else {
                        System.out.print(fields[row][col].who + " ");
                    }
                }
            }
            System.out.println("| " + (row));
        }
        System.out.println("  +----------------");
        System.out.println(" x 0 1 2 3 4 5 6 7");
    }
}
