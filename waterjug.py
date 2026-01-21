from collections import deque

CAPACITY_A = 4
CAPACITY_B = 3
GOAL = 2

def bfs():
    initial_state = (0, 0)
    queue = deque()
    queue.append((initial_state, []))
    visited = set()

    while queue:
        (a, b), path = queue.popleft()

        if a == GOAL:
            return path + [(a, b)]

        if (a, b) in visited:
            continue

        visited.add((a, b))

        next_states = []

        next_states.append((CAPACITY_A, b))
        next_states.append((a, CAPACITY_B))
        next_states.append((0, b))
        next_states.append((a, 0))

        pour = min(a, CAPACITY_B - b)
        next_states.append((a - pour, b + pour))

        pour = min(b, CAPACITY_A - a)
        next_states.append((a + pour, b - pour))

        for state in next_states:
            if state not in visited:
                queue.append((state, path + [(a, b)]))

    return None

solution = bfs()

print("Sequence of steps to reach the goal:\n")
for step in solution:
    print(step)
