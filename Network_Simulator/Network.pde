public class Network{
  private ArrayList<Router> routers;
  private ArrayList<Link> links;
  private String mode;//"Dijkstra" or "Distance Vector"
  
  public Network(int option){//generates a default network (int option = 1 --> see Optional Project spec figure)
    if (option == 1){   
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
    else{
      routers = new ArrayList<Router>();
      routers.add((new Router("Dijkstra"))); routers.get(0).set_router_name("x");
      routers.add((new Router("Dijkstra"))); routers.get(1).set_router_name("y");
      routers.add((new Router("Dijkstra"))); routers.get(2).set_router_name("z");
      
      links = new ArrayList<Link>();
      links.add(new Link("x","y",2));
      links.add(new Link("x","z",7));
      links.add(new Link("y","z",1));
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
  
  public final void add_router(String new_router_name){
    routers.add(new Router(mode));
    routers.get(routers.size()-1).set_router_name(new_router_name);
  }
  public final boolean remove_router(String router_name){//true success; false failure
  
    for (int i = 0; i < routers.size(); i++){
       if(routers.get(i).get_router_name().equals(router_name) == true){
         for(int j= 0; j < links.size(); j++){
           if(links.get(j).get_router_a_name().equals(router_name) || links.get(j).get_router_b_name().equals(router_name)){
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
  
  public final void add_link(Link new_link){
    links.add(new_link);
  }
  public final boolean remove_link(){//true success; false failure
    return false;
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
  public final String get_links_list(){
    String links_list = "";
    for (int i = 0; i < links.size(); i++){
       links_list += "[" + links.get(i).get_router_a_name() + "<-->" + 
                     links.get(i).get_router_b_name() + ", costs " + 
                     Integer.toString(links.get(i).get_link_cost()) + "]; ";
    }

    return links_list;
  }  
  public final String[] get_link_routers_names(int index){//provides the two routers on a link
    String[] link_routers_names = {links.get(index).get_router_a_name(),links.get(index).get_router_b_name()};
    return link_routers_names;
  }
  
}//end of Network class