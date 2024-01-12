## Crossroad Manager

Concurrent Java Application that imitates situation at an intersection using smart traffic lights.

## Description

Crossroad has 4 traffic lights for draivers and 8 traffic lights for pedesrtians as at the picture below.
Drivers can only go to the right or straight. Obviously, 3 traffic lights can be on (green) at the same time while the others are off (red).
So, the solution of the problem of pedestrians and cars not crossing is to turn on 1 traffic light for the drivers and 2 traffic lights for pedestrians left of the drivers.

<img width="636" alt="image" src="https://github.com/lilyrubich/crossroad-manager/assets/124808148/e1b3c3c9-4466-4541-946d-a9536af08fe2">


To increase the throughput of an intersection, the application has a task that calculates the optimal operating time of each traffic light.

The yellow light only comes on for traffic lights for drivers and only 1 second before the green light turns on.
