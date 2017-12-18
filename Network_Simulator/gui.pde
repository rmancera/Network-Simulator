/* =========================================================
 * ====                   WARNING                        ===
 * =========================================================
 * The code in this tab has been generated from the GUI form
 * designer and care should be taken when editing this file.
 * Only add/edit code inside the event handlers i.e. only
 * use lines between the matching comment tags. e.g.

 void myBtnEvents(GButton button) { //_CODE_:button1:12356:
     // It is safe to enter your event code here  
 } //_CODE_:button1:12356:
 
 * Do not rename this tab!
 * =========================================================
 */

public void add_router_button_click(GButton source, GEvent event) { //_CODE_:add_router_button:497740:
  if ((new_router_name_textfield.getText().length()==0) ||  (adjacent_router_name_textfield.getText().length()==0 || add_router_cost_textfield.getText().length() == 0)){
    println("ERROR: User failed to provide new router name or an existing router to link to the new router");
  }
  else if (network.router_exists(new_router_name_textfield.getText())){
    println("ERROR: User provided new router name already exists");
  }
  else if (!network.router_exists(adjacent_router_name_textfield.getText())){
    println("ERROR: User provided adjacent router does not exist");
  }
  else{
    println("adding the following router to the network: " + new_router_name_textfield.getText());
    network.add_router(new_router_name_textfield.getText(),adjacent_router_name_textfield.getText(),Integer.parseInt(add_router_cost_textfield.getText()));
    //g.addVertex(new_router_name_textfield.getText());
    //g.addEdge(new_router_name_textfield.getText(),adjacent_router_name_textfield.getText());
    //g.display();
    displayGraph();
    println("routers in the network: " + network.get_routers_list());
    println("number of routers in the network: " + network.get_routers_count());
  }
} //_CODE_:add_router_button:497740:

public void remove_router_button_click(GButton source, GEvent event) { //_CODE_:remove_router_button:449664:
  println("removing the following router from the network: " + remove_router_textfield.getText());
  if(network.remove_router(remove_router_textfield.getText()) == false)
    println("ALERT! Failed to remove: " + remove_router_textfield.getText());
  else {
    displayGraph();
    println("routers in the network: " + network.get_routers_list());
    println("number of routers in the network: " + network.get_routers_count());
  }
} //_CODE_:remove_router_button:449664:

public void remove_link_button_click(GButton source, GEvent event) { //_CODE_:remove_link_button:685768:

  if ((remove_link_textfield1.getText().length()==0) ||  (remove_link_textfield2.getText().length()==0)){
    println("ERROR: User failed to provide enough inputs");
  }
  else if (!network.router_exists(remove_link_textfield1.getText()) || !network.router_exists(remove_link_textfield2.getText())){
    println("ERROR: User provided router names do not exist");
  }
  else if(network.remove_link(remove_link_textfield1.getText(), remove_link_textfield2.getText())){// removes router
      displayGraph();
  }
  
} //_CODE_:remove_link_button:685768:

public void add_link_button_click(GButton source, GEvent event) { //_CODE_:add_link_button:596038:

  if ((add_link_textfield1.getText().length()==0) ||  (add_link_textfield2.getText().length()==0 || add_link_cost_textfield.getText().length() == 0)){
    println("ERROR: User failed to provide enough inputs");
  }
  else if (!network.router_exists(add_link_textfield1.getText()) || !network.router_exists(add_link_textfield2.getText())){
    println("ERROR: User provided router names do not exist");
  }
  else if(network.add_link(add_link_textfield1.getText(), add_link_textfield2.getText(),Integer.parseInt(add_link_cost_textfield.getText()))){// adds a link
      displayGraph();
  }

} //_CODE_:add_link_button:596038:

synchronized public void window_distance_vector_draw(PApplet appc, GWinData data) { //_CODE_:window_distance_vector:583608:
  appc.background(230);
  //textSize(32);
  //for(int i=0; i < network.get_routers_count(); i++){
    //network.get_router(i).Distance_Vector_initialize(network.get_routers(), network.get_links());
  //}
  //println("***************************************INITIALIZED**********************************");  
  //for(int i=0; i < network.get_routers_count(); i++){//after all routers initialized print their dv_tables
    //network.get_router(i).Distance_Vector_print_dv_table();
    //String fwtbl = new String();
    //fwtbl = network.get_router(0).Distance_Vector_get_forwarding_table(network.links);

  //}
  
} //_CODE_:window_distance_vector:583608:

public void dv_sim_button_click(GButton source, GEvent event) { //_CODE_:dv_sim_button:778539:
  for(int i=0; i < network.get_routers_count(); i++){
    network.get_router(i).Distance_Vector_initialize(network.get_routers(), network.get_links());
  }
  dv_sim_textarea.setText(network.get_router(0).Distance_Vector_get_forwarding_table(network.links));
} //_CODE_:dv_sim_button:778539:



// Create all the GUI controls. 
// autogenerated do not edit
public void createGUI(){
  G4P.messagesEnabled(false);
  G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
  G4P.setCursor(ARROW);
  surface.setTitle("Roberto Mancera Jr's Network Simulator");
  add_router_button = new GButton(this, 10, 10, 96, 16);
  add_router_button.setText("Add Router");
  add_router_button.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  add_router_button.addEventHandler(this, "add_router_button_click");
  new_router_name_textfield = new GTextField(this, 110, 10, 360, 16, G4P.SCROLLBARS_NONE);
  new_router_name_textfield.setPromptText("Enter a unique router name here (new router).");
  new_router_name_textfield.setOpaque(true);
  remove_router_button = new GButton(this, 10, 200, 96, 16);
  remove_router_button.setText("Remove Router");
  remove_router_button.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  remove_router_button.addEventHandler(this, "remove_router_button_click");
  remove_router_textfield = new GTextField(this, 110, 200, 360, 30, G4P.SCROLLBARS_NONE);
  remove_router_textfield.setPromptText("Enter name of router (with only 1 link) to remove here.");
  remove_router_textfield.setOpaque(true);
  remove_link_button = new GButton(this, 10, 150, 96, 16);
  remove_link_button.setText("Remove Link");
  remove_link_button.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  remove_link_button.addEventHandler(this, "remove_link_button_click");
  remove_link_textfield1 = new GTextField(this, 110, 150, 360, 16, G4P.SCROLLBARS_NONE);
  remove_link_textfield1.setPromptText("Enter name of one of the routers on the link here.");
  remove_link_textfield1.setOpaque(true);
  remove_link_textfield2 = new GTextField(this, 110, 170, 360, 16, G4P.SCROLLBARS_NONE);
  remove_link_textfield2.setPromptText("Enter name of the other router on the link here.");
  remove_link_textfield2.setOpaque(true);
  adjacent_router_name_textfield = new GTextField(this, 110, 30, 360, 16, G4P.SCROLLBARS_NONE);
  adjacent_router_name_textfield.setPromptText("Enter an existing router name here (routers will be linked).");
  adjacent_router_name_textfield.setOpaque(true);
  add_router_cost_textfield = new GTextField(this, 110, 50, 360, 16, G4P.SCROLLBARS_NONE);
  add_router_cost_textfield.setPromptText("Enter initial link cost between routers here.");
  add_router_cost_textfield.setOpaque(true);
  add_link_button = new GButton(this, 10, 80, 96, 16);
  add_link_button.setText("Add Link");
  add_link_button.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  add_link_button.addEventHandler(this, "add_link_button_click");
  add_link_textfield1 = new GTextField(this, 110, 80, 360, 16, G4P.SCROLLBARS_NONE);
  add_link_textfield1.setPromptText("Enter a first existing router to be linked here.");
  add_link_textfield1.setOpaque(true);
  add_link_textfield2 = new GTextField(this, 110, 100, 360, 16, G4P.SCROLLBARS_NONE);
  add_link_textfield2.setPromptText("Enter a second existing router to be linked here.");
  add_link_textfield2.setOpaque(true);
  add_link_cost_textfield = new GTextField(this, 110, 120, 360, 16, G4P.SCROLLBARS_NONE);
  add_link_cost_textfield.setPromptText("Enter the cost of the new link here.");
  add_link_cost_textfield.setOpaque(true);
  window_distance_vector = GWindow.getWindow(this, "Distance Vector Simulation", 0, 0, 800, 600, JAVA2D);
  window_distance_vector.noLoop();
  window_distance_vector.addDrawHandler(this, "window_distance_vector_draw");
  dv_sim_textarea = new GTextArea(window_distance_vector, 10, 70, 650, 280, G4P.SCROLLBARS_NONE);
  dv_sim_textarea.setOpaque(true);
  dv_sim_button = new GButton(window_distance_vector, 10, 10, 80, 50);
  dv_sim_button.setText("Step Through Simulation");
  dv_sim_button.setLocalColorScheme(GCScheme.GOLD_SCHEME);
  dv_sim_button.addEventHandler(this, "dv_sim_button_click");
  window_distance_vector.loop();
}

// Variable declarations 
// autogenerated do not edit
GButton add_router_button; 
GTextField new_router_name_textfield; 
GButton remove_router_button; 
GTextField remove_router_textfield; 
GButton remove_link_button; 
GTextField remove_link_textfield1; 
GTextField remove_link_textfield2; 
GTextField adjacent_router_name_textfield; 
GTextField add_router_cost_textfield; 
GButton add_link_button; 
GTextField add_link_textfield1; 
GTextField add_link_textfield2; 
GTextField add_link_cost_textfield; 
GWindow window_distance_vector;
GTextArea dv_sim_textarea; 
GButton dv_sim_button; 