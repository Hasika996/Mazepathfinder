package Mazepathfinder;
import java.util.*;

public class Maze {
    int rows;
    int cols;
    boolean[][] hasghost;

    public Maze(int rows, int cols) { 
        this.rows = rows;
        this.cols = cols;
        this.hasghost = new boolean[rows][cols];
    }

    public void addghost(int x, int y) {
        hasghost[x][y] = true;
    }

    public boolean isghost(int x, int y) {
        return hasghost[x][y];
    }

    public void printgrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) { 
                System.out.print(hasghost[i][j] ? 1 : 0);
            }
            System.out.println();
        }
    }

    public int getrows() {
        return rows;
    }

    public int getcols() {
        return cols;
    }

    static class PathNotFoundException extends Exception { 
        public PathNotFoundException(String message) {
            super(message);
        }
    }
}

class pathfinder {
    Maze maze;
    int[][] dircs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // up, down, left, right

    public pathfinder(Maze maze) {
        this.maze = maze;
    }

    public List<int[]> findpath(int[] start, int[] end) throws Maze.PathNotFoundException { 
        int sx = start[0];
        int sy = start[1];
        int ex = end[0];
        int ey = end[1];
        int n = maze.getrows();
        int m = maze.getcols();

        if (!inbounds(sx, sy, n, m) || !inbounds(ex, ey, n, m) || maze.isghost(sx, sy) || maze.isghost(ex, ey)) {
            throw new Maze.PathNotFoundException("Invalid start or end"); 
        }

        boolean[][] visited = new boolean[n][m];
        Stack<int[]> st = new Stack<>();
        st.push(new int[] { sx, sy });
        visited[sx][sy] = true;

        while (!st.isEmpty()) {
            int[] currpath = st.peek();
            int x = currpath[0];
            int y = currpath[1];

            if (x == ex && y == ey) {
                return new ArrayList<>(st);
            }

            boolean moved = false;
            for (int[] d : dircs) {
                int nx = x + d[0], ny = y + d[1];
                if (inbounds(nx, ny, n, m) && !visited[nx][ny] && !maze.isghost(nx, ny)) {
                    visited[nx][ny] = true;
                    st.push(new int[] { nx, ny });
                    moved = true;
                    break;
                }
            }

            if (!moved) { //backtrack because all your 4 sides are blocked
                st.pop();
            }
        }

        throw new Maze.PathNotFoundException(
                "No path found from [" + sx + "," + sy + "] to [" + ex + "," + ey + "]");
    }

    public boolean inbounds(int x, int y, int n, int m) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}

class Main {
    public static void main(String[] args) {
        try {
            Maze maze = new Maze(4, 4);
            maze.addghost(0, 0);
            maze.addghost(2, 2);
            maze.addghost(3, 1);

            pathfinder nav = new pathfinder(maze);
            int[] start = { 2, 0 };
            int[] end = { 1, 3 };

            List<int[]> path = nav.findpath(start, end);
            System.out.println("Path found:");
            for (int[] p : path) {
                System.out.printf("(%d,%d) ", p[0], p[1]);
            }
        } catch (Maze.PathNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}