// Need G4P library
import g4p_controls.*;

//creates and initializes default network (includes routers and links)
Network network = new Network(2);
//creates new jframe for a visible graph window
GraphDisplay g = new GraphDisplay();

public void setup(){
  size(480, 320, JAVA2D);
  createGUI();
  customGUI();
  // Place your setup code here
  
  println("routers in the network: " + network.get_routers_list());
  println("number of routers in the network: " + network.get_routers_count());
  println("links in the network: " + network.get_links_list());
  println("number of links in the network: " + network.get_links_count());
  
  //place network into visual graph window
  for(int i=0; i < network.get_routers_count(); i++){
    g.addVertex(network.get_router_name(i));
  }
  
  for(int i=0; i < network.get_links_count(); i++){
    g.addEdge(network.get_link_routers_names(i)[0],network.get_link_routers_names(i)[1]);
  }
  g.display();
  
  //***********************************************
  //workspace for Distance Vector prototyping******
  //***********************************************
  /*
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_initialize(network.get_routers(), network.get_links());
  }
  println("***************************************INITIALIZED**********************************");  
  for(int i=0; i < network.get_routers_count(); i++){//after all routers initialized print their dv_tables
    network.get_router(i). Distance_Vector_print_dv_table();
  }


  

  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_update_neighbors(network.get_routers());
  }
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_update_self(network.get_routers(), network.get_links());
  }
  println("***************************************FIRST UPDATE**********************************");
  for(int i=0; i < network.get_routers_count(); i++){//after all routers first update print their dv_tables
    network.get_router(i). Distance_Vector_print_dv_table();
  }  
  
  
  
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_update_neighbors(network.get_routers());
  }
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_update_self(network.get_routers(), network.get_links());
  }
  println("***************************************SECOND UPDATE**********************************");
  for(int i=0; i < network.get_routers_count(); i++){//after all routers first update print their dv_tables
    network.get_router(i). Distance_Vector_print_dv_table();
  }    
  */
  
  
  //***********************************************
  //workspace for Dijkstra prototyping*************
  //***********************************************
  network.get_router(0).Dijkstra_compute(network.get_routers(), network.get_links());
  
}

public void draw(){
  background(230);
  
}

// Use this method to add additional statements
// to customise the GUI controls
public void customGUI(){

}