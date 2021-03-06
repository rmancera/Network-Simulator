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