import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import com.mxgraph.layout.mxCircleLayout; 
import com.mxgraph.model.mxGraphModel; 
import com.mxgraph.swing.mxGraphComponent; 
import com.mxgraph.util.mxConstants; 
import com.mxgraph.util.mxUtils; 
import java.util.Collection; 
import javax.swing.JFrame; 
import org.jgrapht.UndirectedGraph; 
import org.jgrapht.graph.DefaultWeightedEdge; 
import org.jgrapht.graph.SimpleGraph; 
import org.jgrapht.graph.AbstractGraph; 
import org.jgrapht.ext.JGraphModelAdapter; 
import org.jgrapht.ext.JGraphXAdapter; 
import org.jgrapht.graph.ListenableUndirectedWeightedGraph; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.Iterator; 
import java.util.Set; 
import java.util.PriorityQueue; 
import java.util.Comparator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Network_Simulator extends PApplet {

// Need G4P library


//creates and initializes default network (includes routers and links)
Network network = new Network(0);
//creates new jframe for a visible graph window
GraphDisplay g = null;
int dv_counter = 0;
String choosen_dv_router = null;

public void setup(){
  
  createGUI();
  customGUI();
  // Place your setup code here
  window_distance_vector.setVisible(false);
  window_dijkstra.setVisible(false);
  
  
  println("routers in the network: " + network.get_routers_list());
  println("number of routers in the network: " + network.get_routers_count());
  println("links in the network: " + network.get_links_list());
  println("number of links in the network: " + network.get_links_count());
  
  displayGraph();
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
    
    //DISPLAY FORWARDING TABLE
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
    
    
    //DISPLAY DISTANCE VECTOR TABLE
    String dv_path_info = new String();
    dv_path_info = "Distance Vector Table: \n\r";
    dv_path_info += network.get_router(network.get_router_index(choosen_dv_router)).Distance_Vector_get_dv_table();
    label_dv_path_information.setText(dv_path_info);
}
















public class GraphDisplay extends JFrame {
  
  private  ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge> graph = new ListenableUndirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
  private JGraphXAdapter<String, DefaultWeightedEdge> jgxAdapter;
  private mxGraphComponent graphComponent;
  
  public GraphDisplay(){
    jgxAdapter = new JGraphXAdapter <String,DefaultWeightedEdge>(graph);
    //display();
  }
  
  public void display(){
    println(graph.toString());
    graphComponent = new mxGraphComponent(jgxAdapter);
    mxGraphModel graphModel = (mxGraphModel)graphComponent.getGraph().getModel(); 
    Collection<Object> cells =  graphModel.getCells().values();
    mxUtils.setCellStyles(graphComponent.getGraph().getModel(), 
    cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
    getContentPane().add(graphComponent);

    mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
    layout.execute(jgxAdapter.getDefaultParent());
    
    setTitle("Network Topology");
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    
     
    pack();
    setVisible(true);
    this.setExtendedState(this.getExtendedState()); //also works with this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
  }
  
  public void addVertex(String vertex){
    graph.addVertex(vertex);
    //println("[Graph][addVertex] adding vertex: " + vertex);
  }
  public DefaultWeightedEdge addEdge(String vertex1, String vertex2){
    return graph.addEdge(vertex1,vertex2);
    //println("[Graph][addEdge] adding edge: " + vertex1 + ", " + vertex2);
  }
  public void removeVertex(String vertex){
    graph.removeVertex(vertex);
    //println("[Graph][removeVertex] removing: " + vertex);
    //println(graph.toString());
  }
  public void setEdgeWeight(DefaultWeightedEdge e, int weight){
    graph.setEdgeWeight(e, weight);
  }
  
}
public class Link{
  private int link_cost;
  private String router_a_name;
  private String router_b_name;
  
  
  public Link(String user_router_a_name, String user_router_b_name, int input_cost){
    if (user_router_a_name.equals(user_router_b_name)){//ERROR:router names are the same
    }
    else{//Create link between router_a and router_b
      router_a_name = user_router_a_name;
      router_b_name = user_router_b_name;
      link_cost = input_cost;
    }
  }
  
  public final void set_link_cost(int new_cost){
    link_cost = new_cost; 
  }
  public final int get_link_cost(){
    return link_cost; 
  }

  public String get_router_a_name(){
    return router_a_name;
  }
  public String get_router_b_name(){
    return router_b_name;
  }
  
  public String link_to_string(){
    String link_string = "[" + router_a_name + "<-->" + router_b_name + ", costs " + Integer.toString(link_cost) + "]; ";
    return link_string;
  }
  
  public String get_neighboring_router_name(String instant_router_name){
    if(instant_router_name.equals(router_a_name))
      return new String(router_b_name);
    else if(instant_router_name.equals(router_b_name))
      return new String(router_a_name);
    else
      return "ERROR! [Link][get_neighboring_router] " + instant_router_name + " not available in link: " + link_to_string();
  }
  
  public boolean has_router_name(String instant_router_name){
    if(router_a_name.equals(instant_router_name) || router_b_name.equals(instant_router_name))
      return true;
    else
      return false;
  }
}
public class Network{
  private ArrayList<Router> routers;
  private ArrayList<Link> links;
  private String mode;//"Dijkstra" or "Distance Vector"
  
  public Network(int option){//generates a default network (int option = 1 --> see Optional Project spec figure)
    if (option == 0){   
      routers = new ArrayList<Router>();   
      for(int i = 116; i <= 122; i++){//"ascii 't'-->116 through 'z'-->122
        Router curr_router = new Router("Dijkstra");
        routers.add(curr_router);
        routers.get(routers.size()-1).set_router_name(Character.toString ((char) i));
      }
      
      links = new ArrayList<Link>();
      links.add(new Link("t","u",10));links.add(new Link("t","v",10));links.add(new Link("t","y",10));
      links.add(new Link("u","v",10));links.add(new Link("u","w",10));
      links.add(new Link("v","w",10));links.add(new Link("v","x",10));links.add(new Link("v","y",10));
      links.add(new Link("w","x",10));
      links.add(new Link("x","y",10));links.add(new Link("x","z",10));
      links.add(new Link("y","z",10));
    }
    else if (option == 1){
      routers = new ArrayList<Router>();
      routers.add((new Router("Dijkstra"))); routers.get(0).set_router_name("x");
      routers.add((new Router("Dijkstra"))); routers.get(1).set_router_name("y");
      routers.add((new Router("Dijkstra"))); routers.get(2).set_router_name("z");
      
      links = new ArrayList<Link>();
      links.add(new Link("x","y",2));
      links.add(new Link("x","z",7));
      links.add(new Link("y","z",1));
    }
    else if (option == 2){
      routers = new ArrayList<Router>();   
      for(int i = 117; i <= 122; i++){//"ascii 'u'-->117 through 'z'-->122
        Router curr_router = new Router("Dijkstra");
        routers.add(curr_router);
        routers.get(routers.size()-1).set_router_name(Character.toString ((char) i));
      }
      
      links = new ArrayList<Link>();
      links.add(new Link("u","v",2));links.add(new Link("u","w",5));links.add(new Link("u","x",1));
      links.add(new Link("v","w",3));links.add(new Link("v","x",2));
      links.add(new Link("w","x",3));links.add(new Link("w","y",1));links.add(new Link("w","z",5));
      links.add(new Link("x","y",1));
      links.add(new Link("y","z",2));      
    }
  }
  
  public Network(String first_router_name, String second_router_name, String user_input_mode, int first_link_cost){    
    mode = user_input_mode; 
    routers = new ArrayList<Router>();
    
    Router first_router = new Router(mode);
    routers.add(first_router);
    routers.get(routers.size()-1).set_router_name(first_router_name);

    Router second_router = new Router(mode);
    routers.add(second_router);
    routers.get(routers.size()-1).set_router_name(second_router_name);
    
    links = new ArrayList<Link>();
    Link first_link = new Link(first_router_name,second_router_name,first_link_cost);
    links.add(first_link);
  }
  
  public final void add_router(String new_router_name, String linked_router_name, int cost){
    if(!router_exists(linked_router_name)) return;
    if(router_exists(new_router_name)) return;
    routers.add(new Router(mode));
    routers.get(routers.size()-1).set_router_name(new_router_name);
    add_link(new_router_name, linked_router_name,cost);//link between both routers added to network
  }
  
  public final boolean remove_router(String router_name){//true success; false failure
    if(router_links_count(router_name) > 1) return false;
    
    for (int i = 0; i < routers.size(); i++){
       if(routers.get(i).get_router_name().equals(router_name) == true){
         for(int j= 0; j < links.size(); j++){
           if(links.get(j).has_router_name(router_name)){
             println("[Network][remove_router] deleting link:" + links.get(j).link_to_string());
             links.remove(j);
           }
         }
         routers.remove(i);
         return true;
       }
    }
    
    return false;
  }
  
  public int get_router_index(String router_name){
    int index = 0;
    for (int i = 0; i < routers.size(); i++){
      if (router_name.equals(routers.get(i).get_router_name()))
        index = i;
    }
    
    return index;      
  }
  
  public final void add_link(Link new_link){
    links.add(new_link);
  }
  
  public final boolean add_link(String router1_name, String router2_name, int cost){
 
    if(router1_name.equals(router2_name)) return false;
    for(int i = 0; i < links.size(); i++){
      if(links.get(i).has_router_name(router1_name) && links.get(i).has_router_name(router2_name))
        return false;
    }
    
    links.add(new Link(router1_name,router2_name,cost)); 
    return true;
  }
  
  public final boolean remove_link(String router1_name, String router2_name){//true success; false failure
  
    if((router_links_count(router1_name) <= 1) || (router_links_count(router2_name) <= 1)) return false;
    if(router1_name.equals(router2_name)) return false;
    
    for(int i = 0; i < links.size(); i++){
      if(links.get(i).has_router_name(router1_name) && links.get(i).has_router_name(router2_name))
        links.remove(i);
    }
    
    return true;
  }
  public final boolean change_link_cost(String router1_name, String router2_name, int new_cost){
    for(int i = 0; i < links.size(); i++){
      if(links.get(i).has_router_name(router1_name) && links.get(i).has_router_name(router2_name)){
        links.get(i).set_link_cost(new_cost);
        return true;
      }
    }
    return false;
  }
  public final int router_links_count(String router_name){//# of links corresponding to router specified
    int count = 0;
    for(int i = 0; i < links.size(); i++){
      if(links.get(i).has_router_name(router_name))
        count++;
    }
    
    return count;
  }
  
  
  public final int get_routers_count(){//number of routers in the network
    return routers.size();
  }
  
  public final String get_routers_list(){
    String routers_list = "";
    for (int i = 0; i < routers.size(); i++){
       routers_list += routers.get(i).get_router_name() + "; ";
    }

    return routers_list;
  }
  public final Router get_router(int i){
    return routers.get(i);
  }
  public final ArrayList<Router> get_routers(){
    return routers;
  }
  public final String get_router_name(int index){
    return routers.get(index).get_router_name(); 
  }
  public final boolean router_exists(String router_name){
    for (int i = 0; i < routers.size(); i++){
      if (router_name.equals(routers.get(i).get_router_name()))
        return true;
    }
    
    return false;      
  }

  public final ArrayList<Link> get_links(){
    return links;
  }
  public final int get_links_count(){//number of routers in the network
    return links.size();
  }
  public final String get_links_list(){//string listing the cost for each link
    String links_list = "Current costs of each link in the network are as follows:\n\r";
    for (int i = 0; i < links.size(); i++){
       links_list += "    Link(" + links.get(i).get_router_a_name() + "," + 
                     links.get(i).get_router_b_name() + ") has a cost of " + 
                     Integer.toString(links.get(i).get_link_cost()) + "\n\r";
    }

    return links_list;
  }  
  public final String[] get_link_routers_names(int index){//provides the two routers on a link
    String[] link_routers_names = {links.get(index).get_router_a_name(),links.get(index).get_router_b_name()};
    return link_routers_names;
  }
  
}//end of Network class
/*
** To Do:
** [] remove mode variable from class and constructor (it was not necessary in the final impementation)
** [] if time allows clean up Distance_Vector_initialize(ArrayList<Router> routers, ArrayList<Link> links)
** ---see  Distance_Vector_update_self(...) which uses new methods in Link class that should clean up the code
** [] Distance Vector forwarding table returned is defectively including own router in a row
** ---work around performed GUI side, try to correct return here if there is time
*/









public class Router {
  private String router_name;
  private String mode;//"Dijkstra" or "Distance Vector"
 
  public Router(String user_choosen_mode){
    mode = user_choosen_mode;
  }
  
  public final void set_router_name (String user_choosen_router_name){
    router_name = user_choosen_router_name;
  }
  public final String get_router_name (){
    return router_name;
  }
  
  public final void set_router_dijkstra(){
    mode = "Dijkstra";
  }
  public final void set_router_distance_vector(){
    mode = "Distance Vector";
  }
  public final String get_router_mode(){
    return mode;
  }
  

  
  
  
  
  
  /***************************************************************************************************************
  ****************************************************************************************************************
  *                                     Distance Vector                                                          *
  ****************************************************************************************************************
  ****************************************************************************************************************/
  
  private HashMap<String,HashMap<String,String>> dv_table;//<neghbors, (destination, cost)>
  
  public final void Distance_Vector_initialize(ArrayList<Router> routers, ArrayList<Link> links){
    println("[Router][Distanc_Vector_initialize] initializing to all INFs router: " + router_name);
    dv_table = new HashMap<String,HashMap<String,String>>();
    
    //initialize row of instant router
    HashMap<String,String> yc_row_curr = new HashMap<String,String>();//<destination, cost>
    for(int j=0; j < routers.size(); j++){
      if(routers.get(j).get_router_name().equals(router_name))
        yc_row_curr.put(routers.get(j).get_router_name(),Integer.toString(0));
      else
        yc_row_curr.put(routers.get(j).get_router_name(),"INF");
    }
    dv_table.put(router_name,yc_row_curr);
    
    //initialize rows of neighbors to INF and change instant routers destination costs
    for(int i=0; i < links.size(); i++){
      //make a row
      if(links.get(i).router_a_name.equals(router_name) || links.get(i).router_b_name.equals(router_name)){
        HashMap<String,String> yc_table = new HashMap<String,String>();//<destination, cost>
        for(int j=0; j < routers.size(); j++){
          yc_table.put(routers.get(j).get_router_name(),"INF");
        }
        
        //assign a row and put it in the table
        if(links.get(i).router_a_name.equals(router_name)){
          //change instant routers destination costs
          dv_table.get(router_name).put(links.get(i).get_router_b_name(), Integer.toString(links.get(i).get_link_cost()));
          //load adjacent router row to table
          dv_table.put(links.get(i).get_router_b_name(),yc_table);
          println("  Row " + links.get(i).get_router_b_name() + ": " + dv_table.get(links.get(i).get_router_b_name()).toString());
        }
        else if(links.get(i).router_b_name.equals(router_name)){
          //change instant routers destination costs
          dv_table.get(router_name).put(links.get(i).get_router_a_name(), Integer.toString(links.get(i).get_link_cost()));
          //load adjacent router row to table
          dv_table.put(links.get(i).get_router_a_name(),yc_table);
          println("  Row " + links.get(i).get_router_a_name() + ": " + dv_table.get(links.get(i).get_router_a_name()).toString());
        }
        
      }
    }//end of for(int i=0; i < links.size(); i++)
    
    //print row of instant router
    println("  Row " + router_name + ": " + dv_table.get(router_name).toString());
  }
  
  public void Distance_Vector_update_neighbors(ArrayList<Router> routers){
    //retrieve row of instant router dv_table.get(router_name) 
    for(int j=0; j < routers.size(); j++){
      if(dv_table.containsKey(routers.get(j).get_router_name()) && !routers.get(j).get_router_name().equals(router_name)){//check if iteration router is neighbor
        println("[Router][Distance_Vector_update_neighbors] router: " + router_name + "; sending update to router: " + routers.get(j).get_router_name());
        routers.get(j).update_dv_table(router_name,dv_table.get(router_name));//send to neighbor row of instant router
      }
    }
  }
  
  //helper function called by Distance_Vector_update_neighbors(ArrayList<Router> routers)
  public final void update_dv_table(String updating_router_name, HashMap<String,String> yc_row){
     if(dv_table.containsKey(updating_router_name) && !updating_router_name.equals(router_name)){//check if updating router is neighbor       
        Iterator it = yc_row.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> pair = (Map.Entry)it.next();
            dv_table.get(updating_router_name).put(new String(pair.getKey()),new String(pair.getValue()));
        }
        println("  [Router][update_dv_table] updated router: " + router_name + "; updated secion = " + dv_table.get(updating_router_name).toString());
     }
  }
  
  //-check costs of each link
  //-recompute distance vectors
  public void Distance_Vector_update_self(ArrayList<Router> routers,ArrayList<Link> links){
     //get current link costs for instant router and map neighbors to costs
     HashMap<String,Integer> cost_x_v = new HashMap<String,Integer>();//<neighbor, cost>
     cost_x_v.put(new String(router_name),0); //neighboring with itself is always cost of 0
     for(int i=0; i < links.size(); i++){
       if(links.get(i).has_router_name(router_name))
         cost_x_v.put(links.get(i).get_neighboring_router_name(router_name),new Integer(links.get(i).get_link_cost()));
     }
     
     //iterate through all destination routers "y" where Dx(y) is to be updated
     for(int j=0; j < routers.size(); j++){
            
       int d_x_y_min = 0;
       boolean d_x_y_min_exists = false;
       //iterate through 'v's at at y where Dv(y) is found
       Iterator it = cost_x_v.entrySet().iterator();
       while (it.hasNext()) {
          Map.Entry<String,Integer> pair = (Map.Entry)it.next();
          println("[Router][Distance_Vector_update_self] current router: " + router_name + " neighbor router: " + pair.getKey());  
          if(!dv_table.get(pair.getKey()).get(routers.get(j).get_router_name()).equals("INF")){
             int d_x_y_curr_min = cost_x_v.get(pair.getKey()) + Integer.parseInt(  dv_table.get( pair.getKey() ).get( routers.get(j).get_router_name() )  );
             if(!d_x_y_min_exists){
               d_x_y_min = d_x_y_curr_min;
               d_x_y_min_exists = true;
             }
             else if(d_x_y_curr_min < d_x_y_min)
                 d_x_y_min = d_x_y_curr_min;
          }
       }
       //load minimum {cost(x,v) + Dv(y)} into the dv_table at the instant routers row and the column belonging to destination y           
       if(d_x_y_min_exists)
         dv_table.get(router_name).put(routers.get(j).get_router_name(), Integer.toString(d_x_y_min));
    }
  }//end of Distance_Vector_update_self(ArrayList<Router> routers,ArrayList<Link> links)
  
  public void Distance_Vector_print_dv_table(){
    println("[Router][Distance_Vector_print_dv_table] printing table of router "  + router_name);
    
    Iterator it = dv_table.entrySet().iterator();
    while (it.hasNext()){
      Map.Entry<String,HashMap<String,String>> pair = (Map.Entry)it.next();
      println("  Row: " + pair.getKey() + " = " + dv_table.get(pair.getKey()).toString());
    }//end of while loop
    
  }//end of Distance_Vector_print_dv_table()
  
  
  
  public String Distance_Vector_get_dv_table(){      
      String dv_table_string = new String();
      Iterator it = dv_table.entrySet().iterator();
      while (it.hasNext()){
        Map.Entry<String,HashMap<String,String>> pair = (Map.Entry)it.next();
        
        dv_table_string += pair.getKey() + ":  " + dv_table.get(pair.getKey()).toString();
        dv_table_string += "\n\r";
      }
      
      return dv_table_string;
    
  }
  
  
  
  
  
  private HashMap<String,String> dv_forwarding_table; //<destination,next hop>
 
  public HashMap<String,String> Distance_Vector_get_forwarding_table(ArrayList<Link> links){
    HashMap<String,Integer> neighbors = new HashMap<String,Integer>();//<neighbor name, direct cost to neighbor>
    dv_forwarding_table = new HashMap<String,String>();//<destinationY, next hop>
    
    //initialize forwarding table and neghbor cost map
    for(int i = 0; i < links.size(); i++){
      if(links.get(i).has_router_name(router_name)){
        dv_forwarding_table.put(links.get(i).get_neighboring_router_name(router_name),links.get(i).get_neighboring_router_name(router_name));
        neighbors.put(links.get(i).get_neighboring_router_name(router_name), links.get(i).get_link_cost());  
      }
    }
       
    Iterator it_col = dv_table.get(router_name).entrySet().iterator();
    while(it_col.hasNext()){
      Map.Entry<String,String> pair_col = (Map.Entry)it_col.next();
      Iterator it_row = dv_table.entrySet().iterator();
      String min_neighbor = null;
      String min_cost_dvY = "INF"; //min{c(x,v) + Dv(y)
      while (it_row.hasNext()){
        Map.Entry<String,HashMap<String,String>> pair_row = (Map.Entry)it_row.next();
        
        if(!pair_row.getKey().equals(router_name)){
          if(!dv_table.get(pair_row.getKey()).get(pair_col.getKey()).equals("INF")){
            if(!min_cost_dvY.equals("INF")){
              if(Integer.parseInt(dv_table.get(pair_row.getKey()).get(pair_col.getKey())) + neighbors.get(pair_row.getKey())  < Integer.parseInt(min_cost_dvY)){
                min_neighbor = new String(pair_row.getKey());
                min_cost_dvY = Integer.toString(Integer.parseInt(dv_table.get(pair_row.getKey()).get(pair_col.getKey())) + neighbors.get(pair_row.getKey()));                
              }
            }
            else{
                min_neighbor = new String(pair_row.getKey());
                println("[Router][Distance_Vector_print_forwarding_table] min_neighbor: " + min_neighbor);
                println("[Router][Distance_Vector_print_forwarding_table] pair_row.getKey(): " + pair_row.getKey());
                println("[Router][Distance_Vector_print_forwarding_table] pair_col.getKey(): " + pair_col.getKey());
                println("[Router][Distance_Vector_print_forwarding_table] neighbors.get(pair_col.getKey()): " + neighbors.get(pair_col.getKey()));
                min_cost_dvY = Integer.toString(Integer.parseInt(dv_table.get(pair_row.getKey()).get(pair_col.getKey())) + neighbors.get(pair_row.getKey()));  
            }
          }
        }
        
        
      }//end of while loop
      
      if(min_neighbor != null){
          dv_forwarding_table.put(pair_col.getKey(),min_neighbor);
          println("[Router][Distance_Vector_print_forwarding_table] forwarding_table: " + dv_forwarding_table.toString());
      }
  }
      println("[Router][Distance_Vector_print_forwarding_table] forwarding_table: " + dv_forwarding_table.toString());
      
      return dv_forwarding_table;
  }
  
  
  
  /*****************************************************************************************************************
  ******************************************************************************************************************
  *                                           Dijstra's                                                            *
  ******************************************************************************************************************
  ******************************************************************************************************************/
  
  
  private HashMap<String,String> previous_router_table;
  private HashMap<String,String> distance_table;
  
  
  public void Dijkstra_compute(ArrayList<Router> routers, ArrayList<Link> links){
    distance_table = new HashMap<String,String>();//<router name, cost>
    previous_router_table = new HashMap<String,String>();//<router name, previous router>
    
    //initialize priority queue
    class weighted_router{String weighted_router_name; String weight;}
    Comparator<weighted_router> comparator = new Comparator<weighted_router>() {
      @Override
      public int compare(weighted_router left, weighted_router right) {
          if(!left.weight.equals("INF") && right.weight.equals("INF")) return -1;
          if(left.weight.equals("INF") && !right.weight.equals("INF")) return 1;
          else if (left.weight.equals("INF") && right.weight.equals("INF")) return 0;
          else if((Integer.parseInt(left.weight) < Integer.parseInt(right.weight)))
            return -1;
          else if((Integer.parseInt(left.weight) > Integer.parseInt(right.weight)))
            return 1;
          else 
            return 0;
      } 
    };
    PriorityQueue<weighted_router> queue = new PriorityQueue<weighted_router>(routers.size(), comparator);
    
     
    //initialization
    distance_table.put(router_name, Integer.toString(0));
    
    for (int i = 0; i < routers.size(); i++){
      if(!routers.get(i).get_router_name().equals(router_name)){
        distance_table.put(routers.get(i).get_router_name(), "INF");
        previous_router_table.put(routers.get(i).get_router_name(),null);
      }
      weighted_router wr = new weighted_router();
      wr.weighted_router_name = routers.get(i).get_router_name();
      wr.weight = distance_table.get(wr.weighted_router_name);
      queue.add(wr);
    }
    
    //main loop
    while(!queue.isEmpty()){
      weighted_router min_router = queue.poll();
      
      for(int i = 0; i <links.size(); i++){
        if(links.get(i).has_router_name(min_router.weighted_router_name)){//found a link corresponding to min_router 
            ArrayList<weighted_router> min_router_neighbors_removed = new ArrayList<weighted_router>();
            Iterator<weighted_router> it = queue.iterator();
            while (it.hasNext()){
              weighted_router min_router_neighbor = it.next();
              if(links.get(i).get_neighboring_router_name(min_router.weighted_router_name).equals(min_router_neighbor.weighted_router_name)){//found a neighbor of min_router
                if(!distance_table.get(min_router.weighted_router_name).equals("INF")){
                  int alternative_distance = Integer.parseInt(distance_table.get(min_router.weighted_router_name)) + links.get(i).get_link_cost();
                  println("[Router][Dijkstra_compute] min_router: " + min_router.weighted_router_name + "; min_router_neighbor: " + min_router_neighbor.weighted_router_name);
                  println("[Router][Dijkstra_compute] distance_table.get(min_router_neighbor.weighted_router_name): " + distance_table.get(min_router_neighbor.weighted_router_name));
                  println("[Router][Dijkstra_compute] distance_table.get(min_router_neighbor.weight): " + distance_table.get(min_router_neighbor.weighted_router_name));
                  println("[Router][Dijkstra_compute] alternative distance: " + alternative_distance);
                  if(distance_table.get(min_router_neighbor.weighted_router_name).equals("INF")){
                    distance_table.put(min_router_neighbor.weighted_router_name, Integer.toString(alternative_distance));
                    previous_router_table.put(min_router_neighbor.weighted_router_name,min_router.weighted_router_name);
                    min_router_neighbor.weight = Integer.toString(alternative_distance);
                    min_router_neighbors_removed.add(min_router_neighbor);
                    it.remove();
                  } 
                  else if(alternative_distance < Integer.parseInt(distance_table.get(min_router_neighbor.weighted_router_name))){
                    distance_table.put(min_router_neighbor.weighted_router_name, Integer.toString(alternative_distance));
                    previous_router_table.put(min_router_neighbor.weighted_router_name,min_router.weighted_router_name);
                    min_router_neighbor.weight = Integer.toString(alternative_distance);
                    min_router_neighbors_removed.add(min_router_neighbor);
                    it.remove();
                  }
                }
              }
            }//end of while(...)
             for(int k = 0; k < min_router_neighbors_removed.size();k++){//place removed and edited neighbots back in queue 
               queue.add(min_router_neighbors_removed.get(k));
               println("[Router][Dijkstra_compute] restoring neighbor router: " + min_router_neighbors_removed.get(k).weighted_router_name);
             }
        }
        
      }//end of for(...)
      
    }//end of while(...)
    
  println("[Router][Dijkstra_compute] for router: " + router_name);
  println("    Distance table: " + distance_table.toString());
  println("    Previous router table: " + previous_router_table.toString());
    
  }//end of Dijkstra_compute(ArrayList<Router> routers, ArrayList<Link> links)
  
  public HashMap<String,String> get_previous_router_table(){return previous_router_table;}
  public HashMap<String,String> get_distance_table(){return distance_table;}
  
  HashMap<String,String> forwarding_table_dij;
  
  public HashMap<String,String> get_dijkstra_forwarding_table(){
      dijkstra_compute_forwarding_table();
      return forwarding_table_dij;
  }
  
  public void dijkstra_compute_forwarding_table(){
    forwarding_table_dij = new HashMap<String,String>();
    ArrayList<String> port_routers = new ArrayList<String>();
    
    //collect neighbors of this router
    Iterator it = previous_router_table.entrySet().iterator();
    while (it.hasNext()){
      Map.Entry<String,String> pair = (Map.Entry)it.next();
      
      if(pair.getValue().equals(router_name))
        port_routers.add(pair.getKey());
    }
    
    while(forwarding_table_dij.size() < previous_router_table.size())
    {
        
        println("[Router][dijkstra_compute_forwarding_table()] forwarding table: " + forwarding_table_dij.toString());
        Iterator it2 = previous_router_table.entrySet().iterator();
        while (it2.hasNext()){
          Map.Entry<String,String> pair2 = (Map.Entry)it2.next();
          println("[Router][dijkstra_compute_forwarding_table()] key, value: " + pair2.getKey() + ", " + pair2.getValue());

          if(port_routers.contains(pair2.getKey()))
              forwarding_table_dij.put(pair2.getKey(), pair2.getKey());
          else if(forwarding_table_dij.containsKey(pair2.getValue()))
              forwarding_table_dij.put(pair2.getKey(),forwarding_table_dij.get(pair2.getValue()));          
        }
    }
    
    println("[Router][dijkstra_compute_forwarding_table()] forwarding table: " + forwarding_table_dij.toString());
  }
  
  
  
  
}//end of Router class
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
  if(source.getSelectedIndex() == 0) {window_distance_vector.setVisible(false);window_dijkstra.setVisible(true);label_dij_path_information.setText(network.get_links_list());}
  else if(source.getSelectedIndex()==1){window_distance_vector.setVisible(true);window_dijkstra.setVisible(false);label_dv_path_information.setText(network.get_links_list());}
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
  
  
  label_dv_path_information.setText(network.get_links_list());

  
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
      dij_path_info += "    " + network.get_router(network.get_router_index(dij_choosen_router_textfield.getText())).get_previous_router_table().toString();
      
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

  //Display link costs to user
  label_dij_path_information.setText(network.get_links_list());
  
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
  simulation_dropList = new GDropList(this, 10, 240, 160, 64, 3);
  simulation_dropList.setItems(loadStrings("list_340319"), 3);
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
  textfield_dv_router_choosen = new GTextField(window_distance_vector, 140, 30, 347, 27, G4P.SCROLLBARS_NONE);
  textfield_dv_router_choosen.setPromptText("Enter name of router.");
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
  button_change_router_dv = new GButton(window_distance_vector, 140, 10, 135, 20);
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
  label_dij_path_information.setTextAlign(GAlign.LEFT, GAlign.TOP);
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
  dij_choosen_router_textfield = new GTextField(window_dijkstra, 190, 10, 210, 30, G4P.SCROLLBARS_NONE);
  dij_choosen_router_textfield.setPromptText("Enter name of router.");
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

  public void settings() {  size(480, 320, JAVA2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Network_Simulator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
