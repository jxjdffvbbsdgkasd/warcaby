package warcaby;
import java.util.ArrayList;
import java.util.List;
public class utils {
    public static void normalMove(Move m, Field[][] f) {
        f[m.y_to][m.x_to].who = f[m.y_from][m.x_from].who;
        f[m.y_from][m.x_from].is_pawn_there = false;
        f[m.y_to][m.x_to].is_pawn_there = true;
        f[m.y_from][m.x_from].who = 0;
        promote(m.x_to, m.y_to, f);
    }

    public static void takeMove(Move m, Field[][] f) {
        boolean pawn = f[m.y_from][m.x_from].who == 'b' || f[m.y_from][m.x_from].who == 'w';
        if (pawn) {
            f[(m.y_from + m.y_to) / 2][(m.x_from + m.x_to) / 2].is_pawn_there = false;
            f[(m.y_from + m.y_to) / 2][(m.x_from + m.x_to) / 2].who = 0;
            normalMove(m, f);
        } else {
            int fx = m.x_from;
            int fy = m.y_from;
            int dx = (int) Math.signum(m.x_to - m.x_from);
            int dy = (int) Math.signum(m.y_to - m.y_from);
            fx += dx;
            fy += dy;
            while (fx != m.x_to || fy != m.y_to) {
                if (f[fy][fx].is_pawn_there) {
                    f[fy][fx].is_pawn_there = false;
                    f[fy][fx].who = 0;
                }
                fx += dx;
                fy += dy;
            }

        normalMove(m, f);

        }
    }

    public static void promote(int x, int y, Field[][] f) {
        if (f[y][x].who == 'b' && y == 7) {
            f[y][x].who = 'B';
        } else if (f[y][x].who == 'w' && y == 0) {
            f[y][x].who = 'W';
        }
    }

    public static boolean is_ok(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    public static boolean nieBijemyKolegow(int x, int y, int fx, int fy, Field[][] f) {
        return Character.toLowerCase(f[y][x].who) != Character.toLowerCase(f[y + fy][x + fx].who);// jak rozne kolory to zwraca 👍
    }
    public static List<Move> findPossibleMoves(int x, int y, Field[][] f) {
        if (!f[y][x].is_pawn_there) return null;

        List<Move> moves = new ArrayList<Move>();
        int[][] dir;
        boolean pawn = f[y][x].who == 'b' || f[y][x].who == 'w';

        if (!pawn) {
            dir = new int[][]{{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        } else {
            dir = (f[y][x].who == 'b')
                    ? new int[][]{{-1, 1}, {1, 1}}
                    : new int[][]{{-1, -1}, {1, -1}};
        }

        for (int[] d : dir) {
            int fx = d[0];
            int fy = d[1];

            if (pawn) {
                if (is_ok(x + fx, y + fy)) {
                    if (f[y + fy][x + fx].is_pawn_there && nieBijemyKolegow(x, y, fx, fy, f)) {
                        fx += d[0];
                        fy += d[1];
                        if (is_ok(x + fx, y + fy)) {
                            if (!f[y + fy][x + fx].is_pawn_there) {
                                moves.add(new Move(x, y, x + fx, y + fy, true));
                            }
                        }
                    } else {

                        if (!f[y + fy][x + fx].is_pawn_there) {
                            moves.add(new Move(x, y, x + fx, y + fy, false));
                        }
                    }
                }
            } else {
                while (is_ok(x + fx, y + fy) && !f[y + fy][x + fx].is_pawn_there) {
                    moves.add(new Move(x, y, x + fx, y + fy, false));
                    fx += d[0];
                    fy += d[1];
                }
                if (is_ok(x + fx, y + fy) && nieBijemyKolegow(x, y, fx, fy, f)) {
                    fx += d[0];
                    fy += d[1];
                    if (is_ok(x + fx, y + fy)) {
                        if(!f[y + fy][x + fx].is_pawn_there){
                            moves.add(new Move(x, y, x + fx, y + fy, true));
                        }
                    }
                }
            }
        }

        return moves;
    }

    public static List<Move> filterMoves(List<Move> moves) {
        if(moves==null) return null;
        List<Move> filtered = new ArrayList<Move>();
        for (Move move : moves) {
            if (move.take) {
                filtered.add(move);
            }
        }
        if(filtered.isEmpty()) return moves;
        return filtered;
    }
    public static List<Move> filterOnlyTakes(List<Move> moves) {
        if(moves==null) return null;
        List<Move> filtered = new ArrayList<Move>();
        for (Move move : moves) {
            if (move.take) {
                filtered.add(move);
            }
        }
        return filtered;
    }
}