// Need G4P library
import g4p_controls.*;

//creates and initializes default network (includes routers and links)
Network network = new Network(0);
//creates new jframe for a visible graph window
GraphDisplay g = null;
int dv_counter = 0;
String choosen_dv_router = null;

public void setup(){
  size(480, 320, JAVA2D);
  createGUI();
  customGUI();
  // Place your setup code here
  window_distance_vector.setVisible(true);
  window_dijkstra.setVisible(true);
  
  
  println("routers in the network: " + network.get_routers_list());
  println("number of routers in the network: " + network.get_routers_count());
  println("links in the network: " + network.get_links_list());
  println("number of links in the network: " + network.get_links_count());
  
  displayGraph();
  
    
  //***********************************************
  //workspace for Dijkstra prototyping*************
  //***********************************************
  //network.get_router(0).Dijkstra_compute(network.get_routers(), network.get_links());
  //network.get_router(0).dijkstra_compute_forwarding_table();


  
  //***********************************************
  //workspace for Distance Vector prototyping******
  //***********************************************
  
  /*
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_initialize(network.get_routers(), network.get_links());
  }
  println("***************************************INITIALIZED**********************************");  
  for(int i=0; i < network.get_routers_count(); i++){//after all routers initialized print their dv_tables
    network.get_router(i).Distance_Vector_print_dv_table();
    network.get_router(i).Distance_Vector_print_forwarding_table(network.links);
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
    network.get_router(i).Distance_Vector_print_forwarding_table(network.links);
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
    network.get_router(i).Distance_Vector_print_forwarding_table(network.links);
  }    
  
  */
  

  
}

public void draw(){
  background(230);
  
}

// Use this method to add additional statements
// to customise the GUI controls
public void customGUI(){

}

public void displayGraph()
{
  if(g != null)
    g.dispose();
  
   //place network into visual graph window
  g = new GraphDisplay();
  for(int i=0; i < network.get_routers_count(); i++){
    g.addVertex(network.get_router_name(i));
  }
  
  for(int i=0; i < network.get_links_count(); i++){
    DefaultWeightedEdge e = g.addEdge(network.get_link_routers_names(i)[0],network.get_link_routers_names(i)[1]);
    g.setEdgeWeight(e,network.get_links().get(i).get_link_cost());
    
  }
  g.display(); 
}

public void update_distance_vector_display(){
     label_dv_router_shown.setText(choosen_dv_router);
    
    
    HashMap<String,String> fwd_tbl = network.get_router(network.get_router_index(choosen_dv_router)).Distance_Vector_get_forwarding_table(network.get_links());
    
    String addresses = new String();
    String forward_ports = new String();
    
    Iterator it = fwd_tbl.entrySet().iterator();
    while (it.hasNext()){
  
      Map.Entry<String,String> pair = (Map.Entry)it.next(); 
      if(!pair.getKey().equals(choosen_dv_router)){
        addresses += pair.getKey() + "\r\n";
        forward_ports += pair.getValue() + "\r\n";
        println("getKey : " + pair.getKey() + "getValue: " + pair.getValue());
      }
    }
    
    fwd_link_dv_label.setText(forward_ports);
    address_dv_fwd_tbl.setText(addresses); 
}