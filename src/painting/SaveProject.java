/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painting;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author Dylan
 */
public class SaveProject {
    PaintSpace parent;
    public ArrayList jcomponents;
    public SaveProject(PaintSpace p) {
        parent=p;
        jcomponents = new ArrayList();
        for (Component c : parent.getComponents())
        {
              if (c instanceof JComponent)
              {
                    jcomponents.add(c);
                    //System.out.println(c);
              }
         }    
    }
    
}
