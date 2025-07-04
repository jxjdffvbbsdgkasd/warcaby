package warcaby;
import java.io.Serializable;
public class Field implements Serializable {
    boolean is_pawn_there;
    char who; // b - czarny pionek, B - damka, w - bialy pionek, W - damka, 0 - puste
}
