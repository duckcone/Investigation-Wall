/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painting;
import investigationwall.RightClickListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import investigationwall.RightClickListener;
/**
 *
 * @author tony
 */
public class Shape extends JComponent{
           PaintSpace parent;
           public ControlPoint E, W, S, N, NE, NW, SE, SW, RP;
           public State state;
           Point lp;
           boolean isOutlineVisible=false;
           public ControlLine line;
           public int w ,h;
           public ConnectLine cline;
           public Vector<ConnectLine> clineVector = new Vector<ConnectLine>();
           Point st;
           Point end;
          
           public boolean isStart=true;
           
           public RotatedTextArea textField;
           private Point mousePoint;
           public double angleRad = 0;
           
           boolean isResizeVisible=false;
           
           public Color shapeOutLineColor=Color.BLACK;
           public Color shapeBgColor=Color.WHITE;
           public Color shapeTextColor=Color.BLACK;
           public int shapeBoder=1;
           
           public String appearance; 
           
           public int clineGroup=-1;
                  
           public Shape(PaintSpace p, int x, int y, int w, int h) {
                     super();
                     parent = p;
                     
                     if(parent.state==State.drawRect || parent.state==State.drawOval || parent.state==State.drawTri){
                         this.setSize(w+100, h+100);
                         this.setLocation(x, y);
                     }
                     else if(parent.state==State.drawPushpin || parent.state==State.drawText){
                         this.setSize(w, h);
                         this.setLocation(x, y);
                     }
                     this.w=w;
                     this.h=h;
                     
                      if (textField == null) {               
                        this.textField = new RotatedTextArea();
                        this.textField.setForeground(shapeTextColor);
                        this.textField.setBackground(shapeBgColor);
                        this.textField.setBorder(null);
                        this.textField.setLocation(x+52, y+52);
                        this.textField.setSize(w,h);
                        if(parent.state==State.drawRect){
                            parent.add(textField);
                        }  
                    }
                    
                    //System.out.println(state);
                     SE = new ControlPoint();
                     SE.setSize(5, 5);
                     SE.setBackground(Color.red);
                     SE.setVisible(false);
                     parent.add(SE);
                     SE.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));

                     E = new ControlPoint();
                     E.setSize(5, 5);
                     E.setBackground(Color.red);
                     E.setVisible(false);
                     parent.add(E);
                     E.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));

                     NE = new ControlPoint();
                     NE.setSize(5, 5);
                     NE.setBackground(Color.red);
                     NE.setVisible(false);
                     parent.add(NE);
                     NE.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                     
                     W = new ControlPoint();
                     W.setSize(5, 5);
                     W.setBackground(Color.red);
                     W.setVisible(false);
                     parent.add(W);
                     W.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                     
                     NW = new ControlPoint();
                     NW.setSize(5, 5);
                     NW.setBackground(Color.red);
                     NW.setVisible(false);
                     parent.add(NW);
                     NW.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                     
                     SW = new ControlPoint();
                     SW.setSize(5, 5);
                     SW.setBackground(Color.red);
                     SW.setVisible(false);
                     parent.add(SW);
                     SW.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                     
                     S = new ControlPoint();
                     S.setSize(5, 5);
                     S.setBackground(Color.red);
                     S.setVisible(false);
                     parent.add(S);
                     S.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                     
                     N = new ControlPoint();
                     N.setSize(5, 5);
                     N.setBackground(Color.red);
                     N.setVisible(false);
                     parent.add(N);
                     N.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                     
                     RP = new ControlPoint();
                     RP.setSize(5, 5);
                     RP.setBackground(Color.green);
                     RP.setVisible(false);
                     parent.add(RP);
                     BufferedImage image;
                     BufferedImage i = null;
                     try {
                        i=ImageIO.read(new File("src/investigationwall/Images/FunctionImages/Redo.png"));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     image=i;
                     Toolkit toolkit = Toolkit.getDefaultToolkit();
                     Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "Cursor");
                     RP.setCursor(cursor);
                     
                     line = new ControlLine();
                     line.setSize(this.getWidth(), this.getHeight());
                     //this.w=301;
                     //this.h=301;
                     line.setBackground(Color.red);
                     line.setVisible(false);
                     parent.add(line);
                     
                     this.addMouseListener(new RightClickListener(parent));
                        
                     SE.addMouseListener(new MouseAdapter()
                    {
                        public void mousePressed(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                            if(state==State.actived)
                            {
                                state=State.resizing;
                                lp=e.getLocationOnScreen();
                                hideControlPoints();
                            }
                            
                        }
                        public void mouseReleased(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if(isResizeVisible){
                                setMaxShapeSize(Shape.this);
                                isResizeVisible=false;
                            }
                            if(state==State.resizing)
                            {
                                state=State.actived;
                                showControlLine();
                            }
                            Point op=Shape.this.getLocation();    
                            Dimension s = Shape.this.getSize();
                            int nX,nY;
                            nX = op.x+(s.width-Shape.this.w)/2;
                            nY = op.y+(s.height-Shape.this.h)/2;
                            Shape.this.textField.setLocation(nX+2,nY+2);
                        }
                    });

                    SE.addMouseMotionListener(new MouseAdapter()
                    {
                        public void mouseDragged(MouseEvent e)
                        {
                            if(state==State.resizing)
                            {
                                Point cp;
                                cp=e.getLocationOnScreen();

                                Dimension s = Shape.this.getSize();

                                Shape.this.w+=(cp.x-lp.x);
                                Shape.this.h+=(cp.y-lp.y);
                                Shape.this.setSize(s.width+(cp.x-lp.x), s.height+(cp.y-lp.y));
                                
                                updataAtt();
                                
                                Dimension texts = Shape.this.textField.getSize();
                                //Shape.this.textField.w=s.width+(cp.x-lp.x)-s.width/4-15;
                                //Shape.this.textField.h=s.height+(cp.y-lp.y)-s.height/4-15;
                                //Shape.this.textField.setLocation(Shape.this.textField.getX()+(Shape.this.w-(Shape.this.w-100))/2,Shape.this.textField.getY()+(Shape.this.h-(Shape.this.h-100))/2);
                                
                                //Shape.this.textField.setSize(s.width+(cp.x-lp.x)-s.width/4-15, s.height+(cp.y-lp.y)-s.height/4-15);
                                Shape.this.textField.setSize(Shape.this.w+(cp.x-lp.x), Shape.this.h+(cp.y-lp.y));
//                                System.out.print("Shape.this.w ");
//                                System.out.println(s.width+(cp.x-lp.x)-100);
//                                System.out.print("Shape.this.h ");
//                                System.out.println(s.height+(cp.y-lp.y)-100);
                                //Shape.this.textField.w=s.width+(cp.x-lp.x)-s.width/4-15;
                                //Shape.this.textField.h=s.height+(cp.y-lp.y)-s.height/4-15;
                                //Shape.this.textField.setSize(s.width+(cp.x-lp.x), s.height+(cp.y-lp.y));
                                
                                lp.x=cp.x;
                                lp.y=cp.y;
                                
                                isResizeVisible=true;
                            }
                        }
                        
                        
                    });
                    
                    S.addMouseListener(new MouseAdapter()
                    {
                        public void mousePressed(MouseEvent e)
                        {
                            S.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                            if(state==State.actived)
                            {
                                state=State.resizing;
                                lp=e.getLocationOnScreen();
                                hideControlPoints();
                            }
                            
                        }
                        public void mouseReleased(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if(isResizeVisible){
                                setMaxShapeSize(Shape.this);
                                isResizeVisible=false;
                            }
                            if(state==State.resizing)
                            {
                                state=State.actived;
                                showControlLine();
                            }
                            Point op=Shape.this.getLocation();    
                            Dimension s = Shape.this.getSize();
                            int nX,nY;
                            nX = op.x+(s.width-Shape.this.w)/2;
                            nY = op.y+(s.height-Shape.this.h)/2;
                            Shape.this.textField.setLocation(nX+2,nY+2);
                        }
                    });

                    S.addMouseMotionListener(new MouseAdapter()
                    {
                        public void mouseDragged(MouseEvent e)
                        {
                            if(state==State.resizing)
                            {
                                Point cp;
                                cp=e.getLocationOnScreen();

                                Dimension s = Shape.this.getSize();

                                Shape.this.h+=(cp.y-lp.y);
                                Shape.this.setSize(s.width, s.height+(cp.y-lp.y));
                                
                                updataAtt();
                                
                                Dimension texts = Shape.this.textField.getSize();
                                //Shape.this.textField.setSize((s.width+(cp.x-lp.x)-s.width/4)-15, (s.height+(cp.y-lp.y)-s.height/4)-15);
                                Shape.this.textField.setSize(Shape.this.w, Shape.this.h+(cp.y-lp.y)); 
                                //System.err.println(Shape.this.parent.parent.shapeSize.width);
                                lp.x=cp.x;
                                lp.y=cp.y;
                                
                                isResizeVisible=true;
                            }
                        }
                    });
                    
                    E.addMouseListener(new MouseAdapter()
                    {
                        public void mousePressed(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                            if(state==State.actived)
                            {
                                state=State.resizing;
                                lp=e.getLocationOnScreen();
                                hideControlPoints();
                            }
                            
                        }
                        public void mouseReleased(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if(isResizeVisible){
                                setMaxShapeSize(Shape.this);
                                isResizeVisible=false;
                            }
                            if(state==State.resizing)
                            {
                                state=State.actived;
                                showControlLine();
                            }
                            Point op=Shape.this.getLocation();    
                            Dimension s = Shape.this.getSize();
                            int nX,nY;
                            nX = op.x+(s.width-Shape.this.w)/2;
                            nY = op.y+(s.height-Shape.this.h)/2;
                            Shape.this.textField.setLocation(nX+2,nY+2);
                        }
                    });

                    E.addMouseMotionListener(new MouseAdapter()
                    {
                        public void mouseDragged(MouseEvent e)
                        {
                            if(state==State.resizing)
                            {
                                Point cp;
                                cp=e.getLocationOnScreen();

                                Dimension s = Shape.this.getSize();

                                Shape.this.w+=(cp.x-lp.x);
                                Shape.this.setSize(s.width+(cp.x-lp.x), s.height);
                                
                                updataAtt();
                                
                                //Shape.this.textField.w=s.width+(cp.x-lp.x)-s.width/4-15;
                                //Shape.this.textField.h=s.height+(cp.y-lp.y)-s.height/4-15;
                                //Shape.this.textField.setSize(s.width+(cp.x-lp.x)-s.width/4-15, s.height+(cp.y-lp.y)-s.height/4-15);
                                Shape.this.textField.setSize(Shape.this.w+(cp.x-lp.x), Shape.this.h); 
                                //System.err.println(Shape.this.parent.parent.shapeSize.width);
                                lp.x=cp.x;
                                lp.y=cp.y;
                                
                                isResizeVisible=true;
                            }
                        }
                    });

                    RP.addMouseMotionListener(new MouseAdapter()
                    {
                        public void mouseDragged(MouseEvent e)
                        {
                            mousePoint = e.getLocationOnScreen();
                            mousePoint.x-=parent.getFramePoint().x;
                            mousePoint.y-=parent.getFramePoint().y;
                            updataAtt();
                            repaint();
                        }
                    });
                    
                     RP.addMouseListener(new MouseAdapter()
                    {
                        public void mousePressed(MouseEvent e)
                        {
                            parent.setCursor(cursor);
                            System.out.println(parent.state);
                            if(parent.state==State.mouse)
                            {
                                state=State.rotate;
                                hideControlPoints();
                                if(isResizeVisible){
                                    setMaxShapeSize(Shape.this);
                                    isResizeVisible=false;
                                }
                            }
                            
                        }
                        public void mouseReleased(MouseEvent e)
                        {
                            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if(state==State.rotate)
                            {
                                state=State.actived;
                                showControlLine();
                            } 
                        }
                    });
                     
                     if(parent.state==State.drawPushpin){
                         System.out.println("YES!");
                         
                         parent.add(this,100);
                         parent.setLayer(this,100,0);
                     }else{
                         parent.add(this);
                     }

//                     this.setLocation(x-50, y-50);
//                     this.setSize(w+100, h+100);
                    
                     /*this.addMouseListener(new MouseAdapter(){
                               public void mousePressed(MouseEvent e){
                                         if(parent.state==State.mouse){
                                                   if((parent.activeShape!=null)&&(parent.activeShape!=Shape.this)){
                                                             parent.activeShape.hideControlPoints();
                                                             parent.activeShape=Shape.this;
                                                             //showControlLine();
                                                             System.err.println("123");
                                                   }
                                                   
                                                   
                                         }
                                   
                               }
                         
                     });*/
         
        this.addMouseMotionListener(new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                //System.out.println("dragged!");
                if(state==State.moving)
                {
                    moveShape(e);
                    if(!clineVector.isEmpty()){
                            for(int i=0;i<clineVector.size();i++){
                                if(clineVector.get(i).isStar){
                                    clineVector.get(i).setLineStart(S.getLocation());
                                }
                                else{
                                    clineVector.get(i).setLineEnd(S.getLocation());
                                }
                            }
                    }
                    
                        
                        
                        
//                        if(!clineVector.isEmpty()){
//                            for(int i=0;i<clineVector.size();i++){
//                                if(isStart==true){
//                                    clineVector.get(i).setLineStart(S.getLocation());
//                                    //System.err.println("123");
//                                    //System.err.println(cline.lineStart);
//                                }
//                                else if(isStart==false){
//                                    clineVector.get(i).setLineEnd(S.getLocation());
//                                    //System.err.println("456");
//                                }
//                            }
//                        }
                        
                        
                        //Shape.this.parent.parent.shapeLocation=Shape.this.getShapeLocation();
                        //System.err.println(Shape.this.parent.parent.shapeLocation.x);
                    
                        updataAtt();
                        
                    
                    lp.x=e.getLocationOnScreen().x;
                    lp.y=e.getLocationOnScreen().y;
                }
                
                
            }
            
        });
                
        this.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent e)
            {
                if(state==State.ready2Move)
                {
                    state=State.actived;
                    showControlLine();
                }
                else if(state==State.moving)
                {
                    state=State.actived;
                    showControlLine();
                }
                
                
            }
            
            public void mousePressed(MouseEvent e)
            {
                updataAtt();
                if(!clineVector.isEmpty()){
                            for(int i=0;i<clineVector.size();i++){
                                if(clineVector.get(i).abStartX==S.getLocation().x && clineVector.get(i).abStartY==S.getLocation().y){
                                    clineVector.get(i).isStar=true;
                                }
                                else if(clineVector.get(i).abEndX==S.getLocation().x && clineVector.get(i).abEndY==S.getLocation().y){
                                    clineVector.get(i).isStar=false;
                                }
                            }
                        }
                //System.out.println(Shape.this.appearance);
                //System.out.println(Shape.this.S.getLocation());
                //Shape.this.parent.parent.shapeSize=Shape.this.getShapeSize();
                //Shape.this.parent.parent.shapeLocation=Shape.this.getShapeLocation();

                /*System.err.println("x:"+Shape.this.getShapeLocation().x);
                System.err.println("y:"+Shape.this.getShapeLocation().y);
                System.err.println("w:"+Shape.this.getShapeSize().width);
                System.err.println("h:"+Shape.this.getShapeSize().height);
                System.err.println("Angle:"+Shape.this.angleRad);
                System.err.println("Boder:"+Shape.this.shapeBoder);
                System.err.println("LineColor:"+Shape.this.shapeOutLineColor);
                System.err.println("BgColor:"+Shape.this.shapeBgColor);
                System.err.println("TextColor:"+Shape.this.shapeTextColor);*/
               
                if((parent.activeShape!=null)&&(parent.activeShape!=Shape.this))
                {
                    parent.activeShape.hideControlPoints();
                    parent.activeShape.state=State.idle;
                    parent.activeShape=Shape.this;
                    
                }
                
                if(state==State.idle)
                {
                    state=State.actived;
                    isOutlineVisible=false;
                    showControlLine();
                    Shape.this.parent.activeShape=Shape.this;
                }
                 if(parent.state==State.mouse)
                {
                    hideControlPoints();
                    state=State.moving;
                    lp=e.getLocationOnScreen();
                }
                 
                //System.out.println(isStart);
            }
            
            public void mouseClicked(MouseEvent e){
                    //System.err.println(Shape.this.getLocation());
                    //System.out.println(isStart);
                    
                    if(parent.state==State.ready2DrawALine){
                              parent.state=State.drawingALine;
                              parent.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                              if(isStart){
                                isStart=true;
                                parent.lineStart=S.getLocation();
                                System.err.println(parent.lineStart);
                              }
                              else{
                                  isStart=false;
                                  parent.lineEnd=S.getLocation();
                                  System.err.println(parent.lineEnd);
                              }
                              parent.preShape=Shape.this;
                              if(clineGroup==-1){
                                  clineGroup=++parent.clineGroupMax;
                              }
                              else{
                                  parent.clinGroup=clineGroup;
                              }
                              //System.err.println(parent.lineStart);
                              
                              //System.out.println(isStart);
                    }
                    else if(parent.state==State.drawingALine && parent.preShape!=Shape.this){
                              parent.state=State.ready2DrawALine;
                              parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                              if(parent.preShape.isStart==false){
                                  isStart=true;
                                  parent.lineStart=S.getLocation();
                                  System.err.println(parent.lineStart);
                              }
                              else{
                                  isStart=false;
                                  parent.lineEnd=S.getLocation();
                                  System.err.println(parent.lineEnd);
                              }
                              //System.err.println(parent.lineEnd);
                              cline = parent.preShape.cline = new ConnectLine(parent, parent.lineStart, parent.lineEnd);
                              clineVector.addElement(cline);
                              parent.preShape.clineVector.addElement(parent.preShape.cline);
                              
                              //System.out.println(isStart);
                    }
            }
        });
                     state=State.actived;
                     if((parent.activeShape!=null)){
                               parent.activeShape.hideControlPoints();
                     }
                     showControlLine();
                     
                        
           }
           
     void showControlPoints()
    {
        Point op=this.getLocation();
        Dimension s=this.getSize();
//        SE.setLocation(op.x+s.width+30, op.y+s.height+30);
        SE.setLocation(op.x+s.width-3, op.y+s.height-3);
        E.setLocation(op.x+s.width-3, op.y+s.height/2-3);
        NE.setLocation(op.x+s.width-3, op.y-2);
        
        SW.setLocation(op.x-2, op.y+s.height-3);
        W.setLocation(op.x-2, op.y+s.height/2-3);
        NW.setLocation(op.x-2, op.y-2);
        
        S.setLocation(op.x+s.width/2-5, op.y+s.height-3);
        N.setLocation(op.x+s.width/2-5, op.y-2);
        
        RP.setLocation(op.x+s.width/2-5, op.y-20);

        SE.setVisible(true);
        E.setVisible(true);
        NE.setVisible(true);
        
        
        SW.setVisible(true);
        W.setVisible(true);
        NW.setVisible(true);
        
        N.setVisible(true);
        S.setVisible(true);
        
        RP.setVisible(true);
        
    }
    
    public void hideControlPoints()
    {
        SE.setVisible(false);
        E.setVisible(false);
        NE.setVisible(false);
        
        SW.setVisible(false);
        W.setVisible(false);
        NW.setVisible(false);
        
        N.setVisible(false);
        S.setVisible(false);
        
        RP.setVisible(false);
        
        line.setVisible(false);
    }
    
    public void showControlLine(){
            showControlPoints();
            int x, y;
            x=this.getLocation().x;
            y=this.getLocation().y;
            
            
            //System.out.println(x);
            //System.out.println(y);
            
           line.setSize(this.getWidth(), this.getHeight());
           line.setLocation(x, y);
           line.setVisible(true);
           
           int c_index = parent.getLayer(this);
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeLayerIndex.setText(Integer.toString(c_index));

           
    }

           public void setMaxShapeSize(Shape a){
                     Dimension shpaeSize = new Dimension(w, h);
                     int maxSize;
                     shpaeSize.width=(int)Math.pow(w, 2);
                     shpaeSize.height=(int)Math.pow(h, 2);
                     maxSize=(int)Math.sqrt((shpaeSize.width+shpaeSize.height));
                     //System.out.println(maxSize);
                     //System.out.println(a.getWidth());
                     //System.out.println(a.getHeight());
                     
                         a.setSize(maxSize+20, maxSize+20);
                     
//                     if(a.getHeight()<maxSize){
//                         a.setSize(a.getHeight(), maxSize);
//                     }
                     //System.err.println(a.getWidth());
                    // System.err.println(a.getHeight());
           }
    
           
     @Override
    protected void paintComponent(Graphics gr) 
    {
        super.paintComponent(gr);
        
                Graphics2D g = (Graphics2D)gr;
                g.setRenderingHint(
                    RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY);
           if(state==State.rotate){
                if(mousePoint!=null){
                        int x = this.getX()+this.getWidth()/ 2;
                        int y = this.getY()+this.getHeight()/ 2;
                        
                        int cx = mousePoint.x-x;
                        int cy = mousePoint.y-y;
                        
                        angleRad = -Math.atan2(cx, cy);

                        angleRad = Math.toDegrees(angleRad) + 180;

                        //System.err.println(angleRad);
                }
        }
                 this.textField.angle=angleRad;
                 g.rotate(Math.toRadians(angleRad), this.getWidth()/2, this.getHeight()/2);
        
       // }
//        x = width / 2;
//        y = height / 2;
//        g.setStroke(new BasicStroke(3));
//        g.setColor(Color.RED);
//        g.drawLine(x, y, x, y - height );
//        //g.dispose();
            
            
    }
     
    public Dimension getShapeSize(){
           Dimension shpaeSize = new Dimension(w, h);
           shpaeSize.width-=100;
           shpaeSize.height-=100;
          return shpaeSize;
    }
     
//    public Point getShapeLocation(){
//           Point shapeLocation = this.getLocation();
//           shapeLocation.x+=((this.getWidth()-(w-100))/2);
//           shapeLocation.y+=((this.getHeight()-(h-100))/2);
//           return shapeLocation;
//    }
    
    public void setShapeWidth(int w){
            this.w=w;
            this.setSize(this.w+100, h);
            setMaxShapeSize(Shape.this);
            this.showControlLine();
            //textField
            Shape.this.textField.setSize(this.w, this.h); 
            Point op=Shape.this.getLocation();          
            int nX,nY;
            nX = op.x+(this.getWidth()-this.w)/2;
            nY = op.y+(this.getHeight()-this.h)/2;
            Shape.this.textField.setLocation(nX+1,nY+1);

    }
    
    public void setShapeHeight(int h){
            this.h=h;
            this.setSize(h+100, this.h);
            setMaxShapeSize(Shape.this);
            this.showControlLine();
             //textField
            Shape.this.textField.setSize(this.w, this.h); 
            Point op=Shape.this.getLocation();          
            int nX,nY;
            nX = op.x+(this.getWidth()-this.w)/2;
            nY = op.y+(this.getHeight()-this.h)/2;
            Shape.this.textField.setLocation(nX+1,nY+1);
    }
    
    public void setShapeX(int x){
            this.setLocation(x, this.getY());
            this.showControlLine();
            //textField
            Point op=Shape.this.getLocation();          
            int nX,nY;
            nX = op.x+(this.getWidth()-this.w)/2;
            nY = op.y+(this.getHeight()-this.h)/2;
            Shape.this.textField.setLocation(nX+1,nY+1);
    }
    
    public void setShapeY(int y){
            this.setLocation(this.getX(), y);
            this.showControlLine();
            //textField
            Point op=Shape.this.getLocation();          
            int nX,nY;
            nX = op.x+(this.getWidth()-this.w)/2;
            nY = op.y+(this.getHeight()-this.h)/2;
            Shape.this.textField.setLocation(nX+1,nY+1);
    }
    
    public void setShapeAngle(int angle){
            this.angleRad=angle;
            this.showControlLine();
    }
    
    public void removeShape(){
        parent.remove(this);
        parent.remove(line);
        if(cline!=null){
            parent.remove(cline);
        }
        parent.remove(E);
        parent.remove(W);
        parent.remove(S);
        parent.remove(N);
        parent.remove(NE);
        parent.remove(NW);
        parent.remove(SE);
        parent.remove(SW);
        parent.remove(RP); 
        parent.remove(textField);
        this.clineVector.clear();
    }
    
    public void updataAtt(){
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeRowTF.setText(Integer.toString(Shape.this.w));
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeColTF.setText(Integer.toString(Shape.this.h));
           
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeXTF.setText(Integer.toString(Shape.this.getLocation().x));
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeYTF.setText(Integer.toString(Shape.this.getLocation().y));
           
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeAngleTF.setText(Double.toString(Math.round(Shape.this.angleRad*1.0) / 1.0));
           
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).attributeNoteBoderTF.setText(Integer.toString(shapeBoder));
           
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).LineColorLb.setBackground(shapeOutLineColor);
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).BgColorLb.setBackground(shapeBgColor);
           Shape.this.parent.parent.app.wallTabController.wallVector.get(Shape.this.parent.parent.app.selectedIndex).TextColorLb.setBackground(shapeTextColor);
    }
    
    void moveShape(MouseEvent e){
         int dx = e.getLocationOnScreen().x - lp.x;
         int dy = e.getLocationOnScreen().y - lp.y;

         Point op=Shape.this.getLocation();
         Point textop=textField.getLocation();
         Shape.this.textField.setLocation(textop.x+dx, textop.y+dy);

         Shape.this.setLocation(op.x+dx, op.y+dy);
         showControlLine();
         hideControlPoints();
    }
}
