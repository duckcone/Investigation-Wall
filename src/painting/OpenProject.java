/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painting;

import java.awt.Component;
import javax.swing.JComponent;

/**
 *
 * @author Dylan
 */
public class OpenProject {
    PaintSpace parent;
    public OpenProject(PaintSpace p){
        parent=p;
        for (Component c : parent.getComponents())
        {
              if (c instanceof JComponent)
              {
                    parent.add(c);
                    //System.out.println(c);
              }
         }
    }
}
