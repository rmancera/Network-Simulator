import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

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
  
  private class Dijstra_Forwarding_table{
    
  }
  
  private HashMap<String,HashMap<String,String>> dv_table;//<neghbors, (destination, cost)>
  
  public void Distance_Vector_initialize(ArrayList<Router> routers, ArrayList<Link> links){
    println("[Router][Distanc_Vector_initialize] initializing to all INFs router: " + router_name);
    dv_table = new HashMap<String,HashMap<String,String>>();
    
    //initialize row of instant router
    HashMap<String,String> yc_row_curr = new HashMap<String,String>();//<destination, cost>
    for(int j=0; j < routers.size(); j++){
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
  
  public void Distance_Vector_update_neighbors(ArrayList<Router> routers, ArrayList<Link> links){
    for(int i=0; i < links.size(); i++){
      if(links.get(i).router_a_name.equals(router_name)){
      }
      if(links.get(i).router_a_name.equals(router_name)){
      }
    }
  }

}//end of Router class