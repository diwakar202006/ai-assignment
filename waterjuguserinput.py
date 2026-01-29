from collections import deque
import tkinter as tk
import time

# ---------------- BFS LOGIC ----------------
def bfs(capacity_a, capacity_b, goal):
    initial_state = (0, 0)
    queue = deque([(initial_state, [])])
    visited = set()

    while queue:
        (a, b), path = queue.popleft()

        if a == goal or b == goal:
            return path + [(a, b)]

        if (a, b) in visited:
            continue

        visited.add((a, b))

        next_states = [
            (capacity_a, b),  # Fill A
            (a, capacity_b),  # Fill B
            (0, b),           # Empty A
            (a, 0),           # Empty B
            (a - min(a, capacity_b - b), b + min(a, capacity_b - b)),  # A → B
            (a + min(b, capacity_a - a), b - min(b, capacity_a - a))   # B → A
        ]

        for state in next_states:
            if state not in visited:
                queue.append((state, path + [(a, b)]))

    return None


# ---------------- GUI PART ----------------
class WaterJugGUI:
    def __init__(self, root, cap_a, cap_b, solution):
        self.root = root
        self.cap_a = cap_a
        self.cap_b = cap_b
        self.solution = solution
        self.index = 0

        self.canvas = tk.Canvas(root, width=500, height=400, bg="white")
        self.canvas.pack()

        # Jug outlines
        self.jugA = self.canvas.create_rectangle(100, 80, 170, 320, width=2)
        self.jugB = self.canvas.create_rectangle(300, 80, 370, 320, width=2)

        self.waterA = None
        self.waterB = None

        self.label = tk.Label(root, text="", font=("Arial", 12))
        self.label.pack()

        self.animate()

    def draw_water(self, a, b):
        self.canvas.delete("water")

        max_height = 240

        height_a = (a / self.cap_a) * max_height
        height_b = (b / self.cap_b) * max_height

        self.canvas.create_rectangle(
            100, 320 - height_a, 170, 320,
            fill="skyblue", tags="water"
        )

        self.canvas.create_rectangle(
            300, 320 - height_b, 370, 320,
            fill="skyblue", tags="water"
        )

        self.label.config(
            text=f"Jug A: {a}/{self.cap_a}     Jug B: {b}/{self.cap_b}"
        )

    def animate(self):
        if self.index < len(self.solution):
            a, b = self.solution[self.index]
            self.draw_water(a, b)
            self.index += 1
            self.root.after(1200, self.animate)


# ---------------- MAIN ----------------
capacity_a = int(input("Enter capacity of Jug A: "))
capacity_b = int(input("Enter capacity of Jug B: "))
goal = int(input("Enter the goal amount: "))

solution = bfs(capacity_a, capacity_b, goal)

if solution:
    root = tk.Tk()
    root.title("Water Jug Problem Visualization")
    WaterJugGUI(root, capacity_a, capacity_b, solution)
    root.mainloop()
else:
    print("No solution found")