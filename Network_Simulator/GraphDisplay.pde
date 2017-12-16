import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.util.Collection;
import javax.swing.JFrame;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableUndirectedGraph;

public class GraphDisplay extends JFrame {
  
  private  ListenableUndirectedGraph<String, DefaultEdge> graph = new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
  private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
  private mxGraphComponent graphComponent;
  
  public GraphDisplay(){
    jgxAdapter = new JGraphXAdapter <String, DefaultEdge>(graph);
    display();
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
    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
  }
  
  public void addVertex(String vertex){
    graph.addVertex(vertex);
    println("[Graph][addVertex] adding vertex: " + vertex);
  }
  public void addEdge(String vertex1, String vertex2){
    graph.addEdge(vertex1,vertex2);
    println("[Graph][addEdge] adding edge: " + vertex1 + ", " + vertex2);
  }
  public void removeVertex(String vertex){
    graph.removeVertex(vertex);
    println("[Graph][removeVertex] removing: " + vertex);
    println(graph.toString());
  }
}