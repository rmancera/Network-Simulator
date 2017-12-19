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