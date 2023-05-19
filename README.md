# CS430Project

## Problem Description
The problem involves m machines and n (where n >= m) jobs, each defined by a start time and a finish time. Each job can be assigned to any machine, but each machine can serve only one job at a time. The objective is to schedule the maximum number of jobs onto the m machines. This problem requires the development of an O(n log n)-time algorithm, along with its implementation.

## Solution
To solve the given problem, a Greedy algorithm is employed. The algorithm follows these steps:

1. Sort all the jobs in ascending order based on their finish time.
2. Assign the first job to the first machine.
3. Iterate through the list of remaining jobs and check for the availability of the next machine.
4. If the machine is idle, assign the job to it.
5. If the machine is already running a job, compare the start time of the job to be assigned with the finish time of the already assigned job. If the start time is greater, assign the job to the machine. Otherwise, skip and move to the next machine.
6. Print the scheduled jobs.

## Pseudocode
The algorithm can be summarized in the following pseudocode:

```
1. Sort all the jobs j = [j1, j2, j3, ..., jn] in ascending order of finish time.
2. Assign j[0] to machine m[0].
3. For each job j[i] where i = 1 and i < (n-1):
    a. If machine is idle:
        - Assign job to the machine.
    b. Else:
        - If the start time of the job to be assigned is greater than the finish time of the already assigned job:
            - Assign the job to the machine.
        - Else:
            - Move to the next machine and repeat from step 4 to step 8.
            - Break the loop.
```

The algorithm runs in O(n log n) time complexity due to the initial sorting step.

## Program Implementation
Please refer to the source code files for the implementation of the algorithm.
