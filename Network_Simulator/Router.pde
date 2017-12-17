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
  
  /************************************************************************************************
  *  Distance Vector                                                                              *
  ************************************************************************************************/
  
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

}//end of Router class