from collections import deque

def bfs(capacity_a, capacity_b, goal):
    initial_state = (0, 0)
    queue = deque()
    queue.append((initial_state, []))
    visited = set()

    while queue:
        (a, b), path = queue.popleft()

        # Goal check
        if a == goal or b == goal:
            return path + [(a, b)]

        if (a, b) in visited:
            continue

        visited.add((a, b))

        next_states = []

        # Fill jug A
        next_states.append((capacity_a, b))

        # Fill jug B
        next_states.append((a, capacity_b))

        # Empty jug A
        next_states.append((0, b))

        # Empty jug B
        next_states.append((a, 0))

        # Pour A -> B
        pour = min(a, capacity_b - b)
        next_states.append((a - pour, b + pour))

        # Pour B -> A
        pour = min(b, capacity_a - a)
        next_states.append((a + pour, b - pour))

        for state in next_states:
            if state not in visited:
                queue.append((state, path + [(a, b)]))

    return None


# ----------- USER INPUT -----------
capacity_a = int(input("Enter capacity of Jug A: "))
capacity_b = int(input("Enter capacity of Jug B: "))
goal = int(input("Enter the goal amount: "))

solution = bfs(capacity_a, capacity_b, goal)

# ----------- OUTPUT -----------
if solution:
    print("\nSequence of steps to reach the goal:\n")
    for step in solution:
        print(step)
else:
    print("\nNo solution found.")