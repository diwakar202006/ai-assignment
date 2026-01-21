import java.util.*;

class Node {
    int[][] state;
    int x, y;
    int g, h;
    Node parent;

    Node(int[][] state, int x, int y, int g, Node parent) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.g = g;
        this.parent = parent;
        this.h = calculateHeuristic();
    }

    int calculateHeuristic() {
        int h = 0;
        int[][] goal = {
            {1,2,3},
            {4,5,6},
            {7,8,0}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = state[i][j];
                if (val != 0) {
                    for (int r = 0; r < 3; r++) {
                        for (int c = 0; c < 3; c++) {
                            if (goal[r][c] == val) {
                                h += Math.abs(i - r) + Math.abs(j - c);
                            }
                        }
                    }
                }
            }
        }
        return h;
    }

    int f() {
        return g + h;
    }
}

public class EightPuzzleAStar {

    static boolean isGoal(int[][] state) {
        int[][] goal = {
            {1,2,3},
            {4,5,6},
            {7,8,0}
        };
        return Arrays.deepEquals(state, goal);
    }

    static List<int[][]> getPath(Node node) {
        List<int[][]> path = new ArrayList<>();
        while (node != null) {
            path.add(node.state);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    static void printState(int[][] state) {
        for (int[] row : state) {
            for (int v : row) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {

        int[][] start = {
            {1,2,3},
            {4,0,6},
            {7,5,8}
        };

        PriorityQueue<Node> open =
            new PriorityQueue<>(Comparator.comparingInt(Node::f));
        Set<String> closed = new HashSet<>();

        Node startNode = new Node(start, 1, 1, 0, null);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            System.out.println("Current Node (f=" + current.f() +
                               ", g=" + current.g +
                               ", h=" + current.h + ")");
            printState(current.state);

            if (isGoal(current.state)) {
                System.out.println("Goal Reached!\n");
                List<int[][]> solution = getPath(current);
                System.out.println("Solution Path:");
                for (int[][] s : solution) {
                    printState(s);
                }
                return;
            }

            closed.add(Arrays.deepToString(current.state));

            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                    int[][] newState = new int[3][3];
                    for (int r = 0; r < 3; r++)
                        newState[r] = current.state[r].clone();

                    newState[current.x][current.y] = newState[nx][ny];
                    newState[nx][ny] = 0;

                    if (!closed.contains(Arrays.deepToString(newState))) {
                        open.add(new Node(newState, nx, ny,
                                current.g + 1, current));
                    }
                }
            }
        }
    }
}
