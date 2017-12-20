# Network-Simulator
Created by: Roberto Mancera Jr.

https://github.com/rmancera/Network-Simulator

This project is a graphical user interface that simulates routing network topologies.

 1. (Initially a user is presented with two windows (one where the
    network topology is displayed and another where the user can make
    changes to the network topology by filling in text boxes and
    pressing corresponding buttons).
 2. Once the user has defined a desired topology, the user must select a
    routing algorithm (Dijkstra or Distance Vector) from the drop down
    menu titled "CHOOSE SIMULATION" in order to simulate the network.
 3. After the user has chosen a routing algorithm a third window will
    appear that provides the user with the current link costs of the
    topology.  The user may change the link cost of any link at any time
    within this window.
 4. The user must provide a router to view prior to running the chosen
    simulation (the user may select a different router to view at any
    time during the simulation).
 5. Once the user provides the router and starts the simulation a
    forwarding table is populated and network path information is
    provided for the specified router.

This project contains functioning executables available for download for both Windows (.exe files) and Linux. 
The locations of these executables is:

 - Network_Simulator\application.linux-arm64\
 - Network_Simulator\application.linux-armv6hf\
 - Network_Simulator\application.linux32\
 - Network_Simulator\application.linux64\
 - Network_Simulator\application.windows32\
 - Network_Simulator\application.windows64\

This project is developed using both the programming languages Processing and Java.
This project is originally set up for the Processing 3.3.6 integrated development environment (IDE).
To download this IDE visit https://processing.org/download/.

To set up a local programming environment simply place all of the contents of this repo in the "Processing" folder installed by the Processing IDE installer (for example, in Microsoft Windows 10 the path where the contents of this repo should go is C:\Users\User Name\Documents\Processing).

The main source code files for this project are located in the Network_Simulator folder, see:

 - Network_Simulator\GraphDisplay.pde
 - Network_Simulator\Link.pde
 - Network_Simulator\Network.pde
 - Network_Simulator\Network_Simulator.pde
 - Network_Simulator\Router.pde
 - Network_Simulator\gui.pde
