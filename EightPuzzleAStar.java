import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        this.h = heuristic();
    }

    int heuristic() {
        int h = 0;
        int[][] goal = {{1,2,3},{4,5,6},{7,8,0}};
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (state[i][j] != 0)
                    for (int r = 0; r < 3; r++)
                        for (int c = 0; c < 3; c++)
                            if (goal[r][c] == state[i][j])
                                h += Math.abs(i - r) + Math.abs(j - c);
        return h;
    }

    int f() {
        return g + h;
    }
}

public class EightPuzzleGUI extends JFrame {

    JTextField[][] cells = new JTextField[3][3];
    JButton solveBtn = new JButton("Solve");
    JLabel status = new JLabel("Enter puzzle (0 = blank)");

    EightPuzzleGUI() {
        setTitle("8 Puzzle Problem – A* Algorithm");
        setSize(400, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel grid = new JPanel(new GridLayout(3, 3, 5, 5));

        Font f = new Font("Arial", Font.BOLD, 24);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setFont(f);
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid.add(cells[i][j]);
            }

        add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(solveBtn, BorderLayout.CENTER);
        bottom.add(status, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        solveBtn.addActionListener(e -> solve());

        setVisible(true);
    }

    void solve() {
        int[][] start = new int[3][3];
        int bx = 0, by = 0;

        try {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++) {
                    start[i][j] = Integer.parseInt(cells[i][j].getText());
                    if (start[i][j] == 0) {
                        bx = i;
                        by = j;
                    }
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers!");
            return;
        }

        List<int[][]> solution = aStar(start, bx, by);

        if (solution == null) {
            status.setText("No solution found");
            return;
        }

        new Thread(() -> animate(solution)).start();
    }

    void animate(List<int[][]> path) {
        for (int[][] state : path) {
            SwingUtilities.invokeLater(() -> updateGrid(state));
            try {
                Thread.sleep(800);
            } catch (InterruptedException ignored) {}
        }
        status.setText("Goal Reached!");
    }

    void updateGrid(int[][] state) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                cells[i][j].setText(String.valueOf(state[i][j]));
    }

    List<int[][]> aStar(int[][] start, int x, int y) {
        PriorityQueue<Node> open =
                new PriorityQueue<>(Comparator.comparingInt(Node::f));
        Set<String> closed = new HashSet<>();

        open.add(new Node(start, x, y, 0, null));

        while (!open.isEmpty()) {
            Node cur = open.poll();

            if (isGoal(cur.state))
                return path(cur);

            closed.add(Arrays.deepToString(cur.state));

            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];

                if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                    int[][] newState = copy(cur.state);
                    newState[cur.x][cur.y] = newState[nx][ny];
                    newState[nx][ny] = 0;

                    if (!closed.contains(Arrays.deepToString(newState)))
                        open.add(new Node(newState, nx, ny, cur.g + 1, cur));
                }
            }
        }
        return null;
    }

    boolean isGoal(int[][] s) {
        int[][] goal = {{1,2,3},{4,5,6},{7,8,0}};
        return Arrays.deepEquals(s, goal);
    }

    List<int[][]> path(Node n) {
        List<int[][]> p = new ArrayList<>();
        while (n != null) {
            p.add(n.state);
            n = n.parent;
        }
        Collections.reverse(p);
        return p;
    }

    int[][] copy(int[][] a) {
        int[][] b = new int[3][3];
        for (int i = 0; i < 3; i++)
            b[i] = a[i].clone();
        return b;
    }

    public static void main(String[] args) {
        new EightPuzzleGUI();
    }
}