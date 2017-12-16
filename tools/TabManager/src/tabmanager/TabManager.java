/**
 * 
 * TabManager v4.6
 * 
 * PDE-Tabs are listed vertically in a separate window.
 * 
 * 
 * 
 *   (c) 2017    Thomas Diewald
 *               http://thomasdiewald.com
 *   
 *   last built: 03/15/2017
 *   
 *   download:   http://thomasdiewald.com/processing/tools/TabManager/
 *   source:     https://github.com/diwi/ 
 *   
 *   tested OS:  
 *   processing: 3.0
 *
 *
 *
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package tabmanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import processing.app.Base;
import processing.app.Platform;
import processing.app.Sketch;
import processing.app.SketchCode;
import processing.app.tools.Tool;
import processing.app.ui.Editor;



public class TabManager extends JPanel implements Tool, ActionListener {

  private static final String TOOL_AUTHOR_NAME = "Thomas Diewald";
  private static final String TOOL_AUTHOR_URL = "http://thomasdiewald.com";
  private static final String TOOL_NAME = "TabManager";
  private static final String TOOL_VERSION = "4.6 (9)";

  private static String CMD_ADD = "add";
  private static String CMD_NEW = "new";
  private static String CMD_RENAME = "rename";
  private static String CMD_DELETE = "delete";

  private SketchTree panel_tree;

  private Base base;
  private JFrame frame;
  
  static Iconloader ICONS;

  public TabManager() {
  }

  @Override
  public void init(Base base) {
    this.base = base;
  }

  @Override
  public void run() {
    System.out.println(TOOL_NAME + " " + TOOL_VERSION + " by " + TOOL_AUTHOR_NAME + ", " + TOOL_AUTHOR_URL);
    createAndShowGUI();
    panel_tree.rebuild();
  }

  @Override
  public String getMenuTitle() {
    return "TabManager";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (base == null) {
      return;
    }
    String cmd = e.getActionCommand();
    if (CMD_ADD.equals(cmd)) {
      base.getActiveEditor().getSketch().handleAddFile();
    } else if (CMD_NEW.equals(cmd)) {
      base.getActiveEditor().getSketch().handleNewCode();
    } else if (CMD_RENAME.equals(cmd)) {
      base.getActiveEditor().getSketch().handleRenameCode();
    } else if (CMD_DELETE.equals(cmd)) {
      base.getActiveEditor().getSketch().handleDeleteCode();
    }
    panel_tree.rebuild();
  }

  private void createAndShowGUI() {

    if (frame != null) {
      frame.setVisible(true);
      return;
    }

    if(ICONS == null){
      ICONS = new Iconloader();
    }
    
    JButton btn_add = new JButton(CMD_ADD);
    btn_add.setActionCommand(CMD_ADD);
    btn_add.addActionListener(this);

    JButton btn_new = new JButton(CMD_NEW);
    btn_new.setActionCommand(CMD_NEW);
    btn_new.addActionListener(this);

    JButton btn_rename = new JButton(CMD_RENAME);
    btn_rename.setActionCommand(CMD_RENAME);
    btn_rename.addActionListener(this);

    JButton btn_delete = new JButton(CMD_DELETE);
    btn_delete.setActionCommand(CMD_DELETE);
    btn_delete.addActionListener(this);

    JPanel panel_tabmenu = new JPanel(new GridLayout(2, 2));
    panel_tabmenu.add(btn_add);
    panel_tabmenu.add(btn_new);
    panel_tabmenu.add(btn_rename);
    panel_tabmenu.add(btn_delete);

    panel_tree = new SketchTree(base);
    panel_tree.setPreferredSize(new Dimension(250, 400));

    setLayout(new BorderLayout());
    setOpaque(true);

    add(panel_tabmenu, BorderLayout.NORTH);
    add(panel_tree, BorderLayout.CENTER);

    frame = new JFrame("TabManager");
    frame.setContentPane(this);
    frame.pack();
    frame.setVisible(true);
    requestFocusInWindow();


    frame.setIconImage(ICONS.icon_tool.getImage());

  }

  
  
  
  
  static class SketchTree extends JPanel implements MouseListener {

    protected Base base;
    protected DefaultMutableTreeNode root;
    protected DefaultTreeModel model;
    protected JTree tree;

    JPopupMenu ctx_menu_sketch;
    JPopupMenu ctx_menu_sketchcode;

    protected SketchTree(Base base) {
      super(new GridLayout(1, 0));
      this.base = base;
      root = new DefaultMutableTreeNode("Projects");
      model = new DefaultTreeModel(root);
      tree = new JTree(model);
      tree.setBorder(new EmptyBorder(5, 5, 5, 5));
      tree.setEditable(!true);
      tree.setShowsRootHandles(true);
      tree.setRootVisible(false);
      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
      tree.addMouseListener(this);
      add(new JScrollPane(tree));

      // tree.addTreeSelectionListener(new TreeSelectionListener() {
      // @Override
      // public void valueChanged(TreeSelectionEvent e) {
      // TreePath tree_path = e.getPath();
      // }
      // });

      JMenuItem open_sketch_folder = new JMenuItem("Open Sketch-Folder");
      open_sketch_folder.addActionListener((ActionEvent e) -> {
        Platform.openFolder(base.getActiveEditor().getSketch().getFolder());
      });

      JMenuItem open_data_folder = new JMenuItem("Open Data-Folder");
      open_data_folder.addActionListener((ActionEvent e) -> {
        File dir = base.getActiveEditor().getSketch().getDataFolder();
        if (dir != null) {
          dir.mkdir();
          if (dir.exists()) {
            Platform.openFolder(dir);
          }
        }
      });

      JMenuItem sketchcode_new = new JMenuItem("Add new File");
      sketchcode_new.addActionListener((ActionEvent e) -> {
        base.getActiveEditor().getSketch().handleNewCode();
        rebuild();
      });

      JMenuItem sketchcode_add = new JMenuItem("Add existing File");
      sketchcode_add.addActionListener((ActionEvent e) -> {
        base.getActiveEditor().getSketch().handleAddFile();
        rebuild();
      });

      JMenuItem sketchcode_rename = new JMenuItem("Rename File");
      sketchcode_rename.addActionListener((ActionEvent e) -> {
        base.getActiveEditor().getSketch().handleRenameCode();
        rebuild();
      });

      JMenuItem sketchcode_delete = new JMenuItem("Delete File");
      sketchcode_delete.addActionListener((ActionEvent e) -> {
        base.getActiveEditor().getSketch().handleDeleteCode();
        rebuild();
      });


      open_data_folder  .setIcon(ICONS.icon_open_folder);
      open_sketch_folder.setIcon(ICONS.icon_open_folder);
      sketchcode_new    .setIcon(ICONS.icon_file_new);
      sketchcode_add    .setIcon(ICONS.icon_file_add);
      sketchcode_rename .setIcon(ICONS.icon_file_rename);
      sketchcode_delete .setIcon(ICONS.icon_file_delete);
      
      ctx_menu_sketch = new JPopupMenu();
      ctx_menu_sketch.add(open_sketch_folder);
      ctx_menu_sketch.add(open_data_folder);
      ctx_menu_sketch.addSeparator();
      ctx_menu_sketch.add(sketchcode_new);
      ctx_menu_sketch.add(sketchcode_add);

      ctx_menu_sketchcode = new JPopupMenu();
      ctx_menu_sketchcode.add(sketchcode_rename);
      ctx_menu_sketchcode.add(sketchcode_delete);

      DefaultTreeCellRenderer renderer = new CustomDefaultTreeCellRenderer();
      // renderer.setLeafIcon(icon_file_p5);
      tree.setCellRenderer(renderer);

    }

    
    class CustomDefaultTreeCellRenderer extends DefaultTreeCellRenderer {

      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getUserObject() instanceof SketchCodeWrap) {
          SketchCodeWrap sketchcodewrap = (SketchCodeWrap) node.getUserObject();
          String filename = sketchcodewrap.sketch_code.getFileName().toLowerCase();
               if (filename.endsWith(".pde" )) setIcon(ICONS.icon_file_p5     );
          else if (filename.endsWith(".java")) setIcon(ICONS.icon_file_java   );
          else                                 setIcon(ICONS.icon_file_unknown);
        }
        return this;
      }
    }

    
    
    
    protected void rebuild() {
      if (base == null) {
        return;
      }

      // keep track which sketches are expanded
      HashMap<String, SketchWrap> expanded_sketches = new HashMap<String, SketchWrap>();
      int child_count = root.getChildCount();
      for (int i = 0; i < child_count; i++) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
        if (tree.isExpanded(new TreePath(child.getPath()))) {
          if (child.getUserObject() instanceof SketchWrap) {
            SketchWrap sketch = (SketchWrap) child.getUserObject();
            String path = sketch.path.getAbsolutePath();
            expanded_sketches.put(path, sketch);
          } else {
            System.out.println("ERROR");
          }
        }
      }

      // clear all
      root.removeAllChildren();
      model.reload();

      // rebuild from scratch
      List<Editor> editors = base.getEditors();
      for (int i = 0; i < editors.size(); i++) {
        Editor editor = editors.get(i);
        Sketch sketch = editor.getSketch();
        SketchWrap sketch_wrap = new SketchWrap(editor);

        // add tree node
        DefaultMutableTreeNode node_sketch = new DefaultMutableTreeNode(sketch_wrap);
        model.insertNodeInto(node_sketch, root, i);

        SketchCode[] sketch_codes = sketch.getCode();
        for (int j = 0; j < sketch_codes.length; j++) {
          SketchCode sketch_code = sketch_codes[j];
          SketchCodeWrap sketch_code_wrap = new SketchCodeWrap(sketch_wrap, sketch_code);

          // add tree node
          DefaultMutableTreeNode node_sketch_code = new DefaultMutableTreeNode(sketch_code_wrap, false);
          model.insertNodeInto(node_sketch_code, node_sketch, j);

          // select sketchcode if it is the currently acive one
          if (editor.isActive() && sketch_code == sketch.getCurrentCode()) {
            tree.setSelectionPath(new TreePath(node_sketch_code.getPath()));
          }
        }

        // expand sketch, if it was so before the rebuild
        if (expanded_sketches.get(sketch_wrap.path.getAbsolutePath()) != null) {
          tree.expandPath(new TreePath(node_sketch.getPath()));
        }

      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      TreePath tree_path = tree.getPathForLocation(e.getX(), e.getY());
      // TreePath tree_path = tree.getSelectionPath();
      if (tree_path != null) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree_path.getLastPathComponent();
        Object obj = node.getUserObject();

        boolean selected_sketch_code = obj instanceof SketchCodeWrap;
        boolean selected_sketch      = obj instanceof SketchWrap;

        boolean LMB = e.getButton() == MouseEvent.BUTTON1;
        boolean RMB = e.getButton() == MouseEvent.BUTTON3;

        if (LMB || RMB) {
          if (selected_sketch_code)  ((SketchCodeWrap) obj).makeCurrent();
          if (selected_sketch     )  ((SketchWrap    ) obj).makeCurrent();

          if (RMB) {
            if (selected_sketch_code)  ctx_menu_sketchcode.show(e.getComponent(), e.getX(), e.getY());
            if (selected_sketch     )  ctx_menu_sketch    .show(e.getComponent(), e.getX(), e.getY());
          }
          
          tree.setSelectionPath(tree_path);
        }
      }
    }
    
    

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      rebuild();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

  }

  static class SketchCodeWrap {
    SketchWrap parent;
    SketchCode sketch_code;
    String node_name;

    SketchCodeWrap(SketchWrap parent, SketchCode sketch_code) {
      this.parent = parent;
      this.sketch_code = sketch_code;
      this.node_name = sketch_code.getFileName();
    }

    public String toString() {
      String modified_marker = sketch_code.isModified() ? "> " : "";
      return modified_marker + node_name;
    }

    public void makeCurrent() {
      parent.editor.toFront();
      parent.editor.requestFocus();
      parent.sketch.setCurrentCode(sketch_code.getFileName());
    }
  }

  static class SketchWrap {
    Editor editor;
    Sketch sketch;
    String name;
    String node_name;
    File path;

    SketchWrap(Editor editor) {
      this.editor = editor;
      this.sketch = editor.getSketch();
      this.path = sketch.getFolder();
      this.name = sketch.getName();
      this.node_name = name;
    }

    public void makeCurrent() {
      editor.toFront();
      editor.requestFocus();
    }

    public String toString() {
      String modified_marker = sketch.isModified() ? "> " : "";
      return modified_marker + node_name;
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        TabManager tabmanger = new TabManager();
        tabmanger.createAndShowGUI();
        tabmanger.run();
      }
    });
  }
  
  
  
  
  static class Iconloader{
    
    ClassLoader loader = TabManager.class.getClassLoader();
    
    ImageIcon icon_tool         = new ImageIcon(loader.getResource("icon/Dw_TabManager.png"          ));
    ImageIcon icon_open_folder  = new ImageIcon(loader.getResource("icon/Dw_OpenFolder_20.png"       ));
    ImageIcon icon_file_delete  = new ImageIcon(loader.getResource("icon/Dw_Delete_File_20.png"      ));
    ImageIcon icon_file_rename  = new ImageIcon(loader.getResource("icon/Dw_Rename_File_20.png"      ));
    ImageIcon icon_file_new     = new ImageIcon(loader.getResource("icon/Dw_Add_empty_File_20.png"   ));
    ImageIcon icon_file_add     = new ImageIcon(loader.getResource("icon/Dw_Add_existing_File_20.png"));
    ImageIcon icon_file_p5      = new ImageIcon(loader.getResource("icon/Dw_FileType_PDE_16.png"     ));
    ImageIcon icon_file_java    = new ImageIcon(loader.getResource("icon/Dw_FileType_JAVA_16.png"    ));
    ImageIcon icon_file_unknown = new ImageIcon(loader.getResource("icon/Dw_FileType_UNKNOWN_16.png" ));
    
//    use processing icons
//    List<Image> iconImages = new ArrayList<Image>();
//    final int[] sizes = { 16, 32, 48, 64, 128, 256, 512 };
//    for (int sz : sizes) {
//    URL url = TabManager.class.getClassLoader().getResource("icon/pde-" + sz
//    + ".png");
//    Image icon = new ImageIcon(url).getImage();
//    iconImages.add(icon);
//    }
//    frame.setIconImages(iconImages);
    
  }

}