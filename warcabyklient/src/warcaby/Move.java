package warcaby;
import java.io.Serializable;

public class Move implements Serializable {
    int x_from;
    int y_from;
    int x_to;
    int y_to;
    boolean take;

    Move(int xf, int yf, int xt, int yt, boolean t) {
        x_from = xf;
        y_from = yf;
        x_to = xt;
        y_to = yt;
        take = t;
    }
}