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

public void simulation_dropList_click(GDropList source, GEvent event) { //_CODE_:simulation_dropList:340319:
  if(source.getSelectedIndex() == 0) {window_distance_vector.setVisible(false);window_dijkstra.setVisible(true);}
  else if(source.getSelectedIndex()==1){window_distance_vector.setVisible(true);window_dijkstra.setVisible(false);}
} //_CODE_:simulation_dropList:340319:

synchronized public void window_distance_vector_draw(PApplet appc, GWinData data) { //_CODE_:window_distance_vector:583608:
  appc.background(230);
} //_CODE_:window_distance_vector:583608:

public void dv_sim_button_click(GButton source, GEvent event) { //_CODE_:dv_sim_button:778539:
/****************************************************************************************************************/
/******************************DISTANCE VECTOR SIMULATION BUTTON*************************************************/
/****************************************************************************************************************/
/****************************************************************************************************************/
/****************************************************************************************************************/
  

    
    if(textfield_dv_router_choosen.getText().length() == 0 && (choosen_dv_router == null)){ 
        println("ERROR: User failed to provide enough inputs");
        return;  
    }

    
    if(textfield_dv_router_choosen.getText().length() != 0)
    {
      
       if (!network.router_exists(textfield_dv_router_choosen.getText())){
          println("ERROR: User provided router names do not exist");
          return;
        }
    
        choosen_dv_router = textfield_dv_router_choosen.getText();
    }
    
    label_dv_counter.setText(Integer.toString(dv_counter));

    if(dv_counter == 0){//initialize
        for(int i=0; i < network.get_routers_count(); i++){
          network.get_router(i).Distance_Vector_initialize(network.get_routers(), network.get_links());
        }
        
        dv_counter++;
    }
    else if(dv_counter > 0 && (dv_counter%2 == 1)){
        dv_counter++;
        
        for(int i=0; i < network.get_routers_count(); i++){
          network.get_router(i).Distance_Vector_update_neighbors(network.get_routers());
        }

    }
    else if(dv_counter > 0 && (dv_counter%2 == 0)){
        dv_counter++;
        
        for(int i=0; i < network.get_routers_count(); i++){
          network.get_router(i).Distance_Vector_update_self(network.get_routers(), network.get_links());
        }      
    }
    
 
    update_distance_vector_display();
   
   
   
   
   
   

} //_CODE_:dv_sim_button:778539:

public void button_click_change_router_dv(GButton source, GEvent event) { //_CODE_:button_change_router_dv:819476:
  println("button1 - GButton >> GEvent." + event + " @ " + millis());
  
     if(textfield_dv_router_choosen.getText().length() == 0 && (choosen_dv_router == null)){ 
        println("ERROR: User failed to provide enough inputs");
        return;  
    }

    
    if(textfield_dv_router_choosen.getText().length() != 0)
    {
      
       if (!network.router_exists(textfield_dv_router_choosen.getText())){
          println("ERROR: User provided router names do not exist");
          return;
        }
    
        choosen_dv_router = textfield_dv_router_choosen.getText();
    }
    
    update_distance_vector_display();
    
} //_CODE_:button_change_router_dv:819476:

public void button_click_cost_dv(GButton source, GEvent event) { //_CODE_:button_cost_dv:258793:
  println("button1 - GButton >> GEvent." + event + " @ " + millis());
  /*****************************************************************************************/
  /*******************CHANGE LINK COST DURING DIST. VECT. SIMULATION************************/
  /*****************************************************************************************/


  if ((textfield_dv_cost.getText().length()==0) ||  (textfield_dv_cost_r1.getText().length()==0 || textfield_dv_cost_r2.getText().length() == 0)){
    println("ERROR: User failed to provide enough inputs");
  }
  else if (!network.router_exists(textfield_dv_cost_r1.getText()) || !network.router_exists(textfield_dv_cost_r2.getText())){
    println("ERROR: User provided router names do not exist");
  }
  else if(network.change_link_cost(textfield_dv_cost_r1.getText(), textfield_dv_cost_r2.getText(),Integer.parseInt(textfield_dv_cost.getText()))){// changes link cost
    println("Changing link cost was successful: ");
    println("  New cost is: " + textfield_dv_cost.getText());
  }else{
    println("ERROR: Link cost change failed");
  }

  
} //_CODE_:button_cost_dv:258793:

synchronized public void win_draw_dijkstra(PApplet appc, GWinData data) { //_CODE_:window_dijkstra:895846:
  appc.background(230);
  
  
  
} //_CODE_:window_dijkstra:895846:

public void button_click_dijkstra_sim(GButton source, GEvent event) { //_CODE_:button_dijkstra_simulation:508840:
/****************************************************************************************************************/
/******************************DIJKSTRA SIMULATION BUTTON********************************************************/
/****************************************************************************************************************/
/****************************************************************************************************************/
/****************************************************************************************************************/
  
  if (dij_choosen_router_textfield.getText().length()==0){
    println("ERROR: User failed to provide enough inputs");
  }
  else if (!network.router_exists(dij_choosen_router_textfield.getText())){
    println("ERROR: User provided router names do not exist");
  }
  else {
      label_dij_router_shown.setText(dij_choosen_router_textfield.getText());
      int router_index = network.get_router_index(dij_choosen_router_textfield.getText());
      network.get_router(router_index).Dijkstra_compute(network.get_routers(), network.get_links());
      network.get_router(router_index).dijkstra_compute_forwarding_table();
      
      
      //PRINT FORWARDING TABLE TO GUI
      HashMap<String,String> fwd_tbl = network.get_router(network.get_router_index(dij_choosen_router_textfield.getText())).get_dijkstra_forwarding_table();
      
      String addresses = new String();
      String forward_ports = new String();
      
      Iterator it = fwd_tbl.entrySet().iterator();
      while (it.hasNext()){
    
        Map.Entry<String,String> pair = (Map.Entry)it.next();
        addresses += pair.getKey() + "\r\n";
        forward_ports += pair.getValue() + "\r\n";
        println("getKey : " + pair.getKey() + "getValue: " + pair.getValue());
      }
      
      fwd_link_dij_label.setText(forward_ports);
      address_dijkstra_fwd_tbl.setText(addresses);
      
      
      //PRINT OPTIMAL DISTANCES AND PREVIOUS NODE INFORMATION
      String dij_path_info = new String();
      
      dij_path_info += "Least cost distances to destinations: \r\n"; 
      dij_path_info += "    " + network.get_router(network.get_router_index(dij_choosen_router_textfield.getText())).get_distance_table().toString();
      dij_path_info += "\n\r \n\r \n\r \n\r";
      dij_path_info += "Previous router traversal to destination: \r\n"; 
      dij_path_info += "    " + network.get_router(network.get_router_index(dij_choosen_router_textfield.getText())).get_previous_router_table().toString();;
      
      label_dij_path_information.setText(dij_path_info);
  }


  
} //_CODE_:button_dijkstra_simulation:508840:

public void button_click_cost_dij(GButton source, GEvent event) { //_CODE_:button_change_cost_dij:453746:
  println("button_change_cost_dij - GButton >> GEvent." + event + " @ " + millis());
  /*****************************************************************************************/
  /*******************CHANGE LINK COST DURING DIJKSTRA SIMULATION***************************/
  /*****************************************************************************************/
  
  
  if ((textfield_dij_cost.getText().length()==0) ||  (textfield_dij_cost_r1.getText().length()==0 || textfield_dij_cost_r2.getText().length() == 0)){
    println("ERROR: User failed to provide enough inputs");
  }
  else if (!network.router_exists(textfield_dij_cost_r1.getText()) || !network.router_exists(textfield_dij_cost_r2.getText())){
    println("ERROR: User provided router names do not exist");
  }
  else if(network.change_link_cost(textfield_dij_cost_r1.getText(), textfield_dij_cost_r2.getText(),Integer.parseInt(textfield_dij_cost.getText()))){// changes link cost
    println("Changing link cost was successful: ");
    println("  New cost is: " + textfield_dij_cost.getText());
  }else{
    println("ERROR: Link cost change failed");
  }


  
} //_CODE_:button_change_cost_dij:453746:

public void textfield4_change1(GTextField source, GEvent event) { //_CODE_:dij_choosen_router_textfield:434707:
  println("textfield4 - GTextField >> GEvent." + event + " @ " + millis());
} //_CODE_:dij_choosen_router_textfield:434707:



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
  remove_router_textfield = new GTextField(this, 110, 200, 360, 16, G4P.SCROLLBARS_NONE);
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
  simulation_dropList = new GDropList(this, 10, 240, 160, 60, 2);
  simulation_dropList.setItems(loadStrings("list_340319"), 0);
  simulation_dropList.addEventHandler(this, "simulation_dropList_click");
  window_distance_vector = GWindow.getWindow(this, "Distance Vector Simulation", 0, 0, 680, 600, JAVA2D);
  window_distance_vector.noLoop();
  window_distance_vector.addDrawHandler(this, "window_distance_vector_draw");
  dv_sim_button = new GButton(window_distance_vector, 20, 10, 120, 50);
  dv_sim_button.setText("Step Through Simulation");
  dv_sim_button.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  dv_sim_button.addEventHandler(this, "dv_sim_button_click");
  address_dv_fwd_tbl = new GLabel(window_distance_vector, 10, 150, 80, 430);
  address_dv_fwd_tbl.setTextAlign(GAlign.LEFT, GAlign.TOP);
  address_dv_fwd_tbl.setOpaque(true);
  fwd_link_dv_label = new GLabel(window_distance_vector, 100, 150, 80, 430);
  fwd_link_dv_label.setTextAlign(GAlign.LEFT, GAlign.TOP);
  fwd_link_dv_label.setOpaque(true);
  label9 = new GLabel(window_distance_vector, 100, 130, 80, 20);
  label9.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label9.setText("Link");
  label9.setOpaque(true);
  label10 = new GLabel(window_distance_vector, 10, 130, 80, 20);
  label10.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label10.setText("Address");
  label10.setOpaque(true);
  label11 = new GLabel(window_distance_vector, 10, 90, 170, 40);
  label11.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label11.setText("Forwarding Table");
  label11.setTextBold();
  label11.setOpaque(true);
  textfield_dv_router_choosen = new GTextField(window_distance_vector, 160, 31, 347, 27, G4P.SCROLLBARS_NONE);
  textfield_dv_router_choosen.setPromptText("Enter the name of another existing router.");
  textfield_dv_router_choosen.setOpaque(true);
  label_dv_counter = new GLabel(window_distance_vector, 42, 61, 80, 18);
  label_dv_counter.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label_dv_counter.setText("step");
  label_dv_counter.setLocalColorScheme(GCScheme.RED_SCHEME);
  label_dv_counter.setOpaque(true);
  label7 = new GLabel(window_distance_vector, 546, 10, 105, 63);
  label7.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label7.setText("Currently Showing Information for Router");
  label7.setLocalColorScheme(GCScheme.RED_SCHEME);
  label7.setOpaque(true);
  label_dv_router_shown = new GLabel(window_distance_vector, 547, 75, 103, 18);
  label_dv_router_shown.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label_dv_router_shown.setText("--");
  label_dv_router_shown.setTextBold();
  label_dv_router_shown.setLocalColorScheme(GCScheme.RED_SCHEME);
  label_dv_router_shown.setOpaque(true);
  button_change_router_dv = new GButton(window_distance_vector, 172, 11, 135, 20);
  button_change_router_dv.setText("Change Router View");
  button_change_router_dv.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  button_change_router_dv.addEventHandler(this, "button_click_change_router_dv");
  label_dv_path_information = new GLabel(window_distance_vector, 200, 190, 460, 390);
  label_dv_path_information.setTextAlign(GAlign.LEFT, GAlign.TOP);
  label_dv_path_information.setOpaque(true);
  label8 = new GLabel(window_distance_vector, 200, 170, 460, 20);
  label8.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label8.setText("Path Information");
  label8.setTextBold();
  label8.setOpaque(false);
  button_cost_dv = new GButton(window_distance_vector, 200, 90, 120, 20);
  button_cost_dv.setText("Change Link Cost");
  button_cost_dv.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  button_cost_dv.addEventHandler(this, "button_click_cost_dv");
  textfield_dv_cost = new GTextField(window_distance_vector, 320, 90, 220, 20, G4P.SCROLLBARS_NONE);
  textfield_dv_cost.setPromptText("Enter a new cost.");
  textfield_dv_cost.setOpaque(true);
  textfield_dv_cost_r1 = new GTextField(window_distance_vector, 320, 110, 220, 20, G4P.SCROLLBARS_NONE);
  textfield_dv_cost_r1.setPromptText("Enter a router on the link.");
  textfield_dv_cost_r1.setOpaque(true);
  textfield_dv_cost_r2 = new GTextField(window_distance_vector, 320, 130, 220, 20, G4P.SCROLLBARS_NONE);
  textfield_dv_cost_r2.setPromptText("Enter the other router on the link.");
  textfield_dv_cost_r2.setOpaque(true);
  window_dijkstra = GWindow.getWindow(this, "Dijkstra Simulation", 0, 0, 680, 600, JAVA2D);
  window_dijkstra.noLoop();
  window_dijkstra.addDrawHandler(this, "win_draw_dijkstra");
  label1 = new GLabel(window_dijkstra, 20, 90, 80, 20);
  label1.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label1.setText("Address");
  label1.setOpaque(true);
  label2 = new GLabel(window_dijkstra, 110, 90, 80, 20);
  label2.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label2.setText("Link");
  label2.setOpaque(true);
  label3 = new GLabel(window_dijkstra, 20, 50, 170, 40);
  label3.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label3.setText("Forwarding Table");
  label3.setTextBold();
  label3.setOpaque(true);
  address_dijkstra_fwd_tbl = new GLabel(window_dijkstra, 20, 110, 80, 460);
  address_dijkstra_fwd_tbl.setTextAlign(GAlign.LEFT, GAlign.TOP);
  address_dijkstra_fwd_tbl.setOpaque(true);
  fwd_link_dij_label = new GLabel(window_dijkstra, 110, 110, 80, 460);
  fwd_link_dij_label.setTextAlign(GAlign.LEFT, GAlign.TOP);
  fwd_link_dij_label.setOpaque(true);
  label_dij_path_information = new GLabel(window_dijkstra, 220, 220, 430, 350);
  label_dij_path_information.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label_dij_path_information.setOpaque(true);
  label6 = new GLabel(window_dijkstra, 220, 180, 430, 40);
  label6.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label6.setText("Path Information");
  label6.setTextBold();
  label6.setOpaque(false);
  button_dijkstra_simulation = new GButton(window_dijkstra, 20, 10, 170, 30);
  button_dijkstra_simulation.setText("Run Dijkstra Simulation");
  button_dijkstra_simulation.setTextBold();
  button_dijkstra_simulation.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  button_dijkstra_simulation.addEventHandler(this, "button_click_dijkstra_sim");
  textfield_dij_cost_r1 = new GTextField(window_dijkstra, 330, 110, 210, 20, G4P.SCROLLBARS_NONE);
  textfield_dij_cost_r1.setPromptText("Enter a router on the link.");
  textfield_dij_cost_r1.setOpaque(true);
  button_change_cost_dij = new GButton(window_dijkstra, 210, 90, 110, 20);
  button_change_cost_dij.setText("Change Link Costs");
  button_change_cost_dij.setLocalColorScheme(GCScheme.GREEN_SCHEME);
  button_change_cost_dij.addEventHandler(this, "button_click_cost_dij");
  textfield_dij_cost_r2 = new GTextField(window_dijkstra, 330, 130, 210, 20, G4P.SCROLLBARS_NONE);
  textfield_dij_cost_r2.setPromptText("Enter the other router on the link.");
  textfield_dij_cost_r2.setOpaque(true);
  textfield_dij_cost = new GTextField(window_dijkstra, 330, 90, 210, 20, G4P.SCROLLBARS_NONE);
  textfield_dij_cost.setPromptText("Enter the new cost.");
  textfield_dij_cost.setOpaque(true);
  dij_choosen_router_textfield = new GTextField(window_dijkstra, 200, 10, 210, 30, G4P.SCROLLBARS_NONE);
  dij_choosen_router_textfield.setPromptText("Enter a different router to view.");
  dij_choosen_router_textfield.setOpaque(true);
  dij_choosen_router_textfield.addEventHandler(this, "textfield4_change1");
  label5 = new GLabel(window_dijkstra, 550, 10, 120, 60);
  label5.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label5.setText("Currently Showing Information For Router");
  label5.setTextBold();
  label5.setLocalColorScheme(GCScheme.RED_SCHEME);
  label5.setOpaque(true);
  label_dij_router_shown = new GLabel(window_dijkstra, 550, 70, 120, 27);
  label_dij_router_shown.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  label_dij_router_shown.setText("--");
  label_dij_router_shown.setTextBold();
  label_dij_router_shown.setLocalColorScheme(GCScheme.RED_SCHEME);
  label_dij_router_shown.setOpaque(true);
  window_distance_vector.loop();
  window_dijkstra.loop();
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
GDropList simulation_dropList; 
GWindow window_distance_vector;
GButton dv_sim_button; 
GLabel address_dv_fwd_tbl; 
GLabel fwd_link_dv_label; 
GLabel label9; 
GLabel label10; 
GLabel label11; 
GTextField textfield_dv_router_choosen; 
GLabel label_dv_counter; 
GLabel label7; 
GLabel label_dv_router_shown; 
GButton button_change_router_dv; 
GLabel label_dv_path_information; 
GLabel label8; 
GButton button_cost_dv; 
GTextField textfield_dv_cost; 
GTextField textfield_dv_cost_r1; 
GTextField textfield_dv_cost_r2; 
GWindow window_dijkstra;
GLabel label1; 
GLabel label2; 
GLabel label3; 
GLabel address_dijkstra_fwd_tbl; 
GLabel fwd_link_dij_label; 
GLabel label_dij_path_information; 
GLabel label6; 
GButton button_dijkstra_simulation; 
GTextField textfield_dij_cost_r1; 
GButton button_change_cost_dij; 
GTextField textfield_dij_cost_r2; 
GTextField textfield_dij_cost; 
GTextField dij_choosen_router_textfield; 
GLabel label5; 
GLabel label_dij_router_shown; 