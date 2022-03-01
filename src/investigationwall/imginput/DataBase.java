/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investigationwall.imginput;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.TextUI;

import org.apache.commons.lang3.StringUtils;

import investigationwall.*;
import painting.*;

import java.util.Vector;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Container;

import java.awt.Color;
import javax.swing.UIManager;


public class DataBase {
    File LoadFile = null;//OpenFile
    public int Img_i = 0, row = 0, Imgcol = 6;
    public String[] ImgCode = null;
    public int Target = -1;
    public int LoadData_i;
    
    public ImgBox[] SelectBox;//預備
    public ImgBox[] EntireBox;
    public Point[] BufferedBox;
    int ActiveAmount = 0;
    String Tag;
    Img img;
    
    int Nail_i = 0, Nail_row = 0, Nail_col = 2;
    int Nail_Target = -1;
    
    ImgBox[] NailBox;
    
    Application parent;
    PaintSpace space;

    public Vector<Component> jcomponents = new Vector<Component>();

    public DataBase(Application parent, PaintSpace space){
        this.parent = parent;
        this.space = space;
    }
    public void ClearData()
    {
        LoadFile = null;//OpenFile
        Img_i = 0; row = 0;
        ImgCode = null;
        Target = -1;
        LoadData_i = 0;
        
        SelectBox = null;
        EntireBox = null;
    }
    public void IncreaseArraySize(State Operate)
    {
        if(Operate == State.ImgOperate)
        {
            if(row == 0)
            {
                row++;
                System.out.println("i: "+Img_i+", row: "+row);

                EntireBox = new ImgBox[row];
                ImgCode = new String[row];
            }
            if(row != 0 && Img_i == row)
            {
                System.out.println("row++");
                row ++;
                System.out.println("i: "+Img_i+", row: "+row);

                ImgBox[] temp = new ImgBox[row];
                for(int i = 0;i < row - 1;i++)
                    temp[i] = EntireBox[i];

                EntireBox = null;
                EntireBox = new ImgBox[row];
                for(int i = 0;i < row;i++)
                    EntireBox[i] = temp[i];
                /*------------------------*/
                String[] temp2 = new String[row];
                for(int i = 0;i < row - 1;i++)
                    temp2[i] = ImgCode[i];

                ImgCode = null;
                ImgCode = new String[row];
                for(int i = 0;i < row;i++)
                    ImgCode[i] = temp2[i];
            }
        }
        else if(Operate == State.NailOperate)
        {
            if(Nail_row == 0)
            {
                Nail_row++;
                NailBox = new ImgBox[Nail_row];
            }
            if(Nail_row != 0 && Nail_i == Nail_row)
            {
                Nail_row++;
                ImgBox[] temp = new ImgBox[Nail_row];
                for(int i = 0;i < Nail_row - 1;i++)
                    temp[i] = NailBox[i];
                
                NailBox = null;
                NailBox = new ImgBox[Nail_row];
                for(int i = 0;i < Nail_row;i++)
                    NailBox[i] = temp[i];
            }
        }
    }
    public void ReduceArraySize(State Operate)
    {
        if(Operate == State.ImgOperate)
        {
            row --;
            Img_i--;

            ImgBox[] temp = new ImgBox[row];
            for(int i = 0;i < row;i++)
                temp[i] = EntireBox[i];

            EntireBox = null;
            EntireBox = new ImgBox[row];
            for(int i = 0;i < row;i++)
                EntireBox[i] = temp[i];
            /*------------------------*/
            String[] temp2 = new String[row];
            for(int i = 0;i < row;i++)
                temp2[i] = ImgCode[i];

            ImgCode = null;
            ImgCode = new String[row];
            for(int i = 0;i < row;i++)
                ImgCode[i] = temp2[i];
            /*------------------------*/

            for(int i=0;i<row;i++)//show
                System.out.println("<Exist>data["+i+"]x: " + EntireBox[i].x +", y: "+EntireBox[i].y+
                                        ", ImgWidth: "+EntireBox[i].ImgWidth+", ImgHeight: "+EntireBox[i].ImgHeight/*+
                                        ", ImgCode: "+ImgCode[i]*/);
        }
        else if(Operate == State.NailOperate)
        {
            
        }
    }
    public void CoverData(State Operate)
    {
        if(Operate == State.ImgOperate)
        {
            if(Target == row + 1)//If you delete last data
            {
                ReduceArraySize(State.ImgOperate);
            }
            else
            {
                WallPanel wPnl = parent.wallTabController.wallVector.get(parent.selectedIndex);
                for(int i = Target + 1;i < wPnl.database.row;i++)
                {
                    EntireBox[i-1] = EntireBox[i];
                    ImgCode[i-1] = ImgCode[i];
                }
                for(int i=0;i<row;i++)//show
                    System.out.println("<cover>data["+i+"]x: " + EntireBox[i].x+", y: "+EntireBox[i].y+
                                        ", ImgWidth: "+EntireBox[i].ImgWidth+", ImgHeight: "+EntireBox[i].ImgHeight+
                                        ", ImgCode: "+ImgCode[i]);
                ReduceArraySize(State.ImgOperate);
            }
        }
        else if(Operate == State.NailOperate)
        {
            
        }
    }
    public void removeImg(){
        WallPanel wPnl = parent.wallTabController.wallVector.get(parent.selectedIndex);
        for(int i =0;i < wPnl.database.row;i++){
            if(wPnl.database.EntireBox[i].ImgOperate == State.Active){
                wPnl.database.EntireBox[i].hideControlLine();
                space.remove(wPnl.database.EntireBox[i]);
                wPnl.database.Target = i;
                wPnl.database.CoverData(State.ImgOperate);
                i--;
            }
        }
    }
    public void SaveData(State ImgOperate)
    {
        if(ImgOperate == State.Add)
            ImgCode[Img_i] = parent.ImgFile.toString();
        if(ImgOperate == State.Pasting)
            ImgCode[Img_i] = ImgCode[Target];
        
        System.out.println("Now i: "+Img_i+", row: "+row);
        Img_i++;
    }
    public void ImageToBase()
    {
        System.out.println("ImageToBase");
        System.out.println("ImgCodeSize: "+ImgCode.length);
        
        for(int i = 0;i < row;i++)
        {
            File file = new File(ImgCode[i]);
            System.out.println("file: "+ file);
            try{
                BufferedImage imgx = ImageIO.read(file);
                Image Scale = imgx.getScaledInstance(EntireBox[i].ImgWidth, EntireBox[i].ImgHeight, Image.SCALE_SMOOTH);
                BufferedImage img = new BufferedImage(EntireBox[i].ImgWidth, EntireBox[i].ImgHeight, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g2d = img.createGraphics();
                g2d.drawImage(Scale, 0, 0, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();//圖片轉位元組
                ImageIO.write(img, "jpg", baos);
                byte[] bytes = baos.toByteArray();
                
                Base64.Encoder Encoder = Base64.getEncoder();
                String str = Encoder.encodeToString(bytes);
                System.out.println("ImgCode["+i+"]= "+str);
                ImgCode[i] = str;
            }catch(IOException ex){}
        }
    }
    public void SaveFile()
    {
        System.out.println("Save Image");
        Tag = new String("Img");
        for(int i = 0;i < row;i++)//show
        {
            System.out.println("data["+i+"] x: "+ EntireBox[i].x + ", y: " + EntireBox[i].y + 
                    ", ImgWidth: " + EntireBox[i].ImgWidth + ", ImgHeight: " + EntireBox[i].ImgHeight + 
                    ", ImgCode: " + ImgCode[i]);
        }
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setDialogTitle("另存新檔");
        FileChooser.setCurrentDirectory(new File( "" +"*.itw"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("ITW (*.itw)", "itw"));
        int result = FileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("SaveFile:");
            try{
                FileWriter fw = new FileWriter(new File(FileChooser.getSelectedFile().getAbsolutePath()+".itw"));
                if(EntireBox!=null){
                    fw.write(Tag + "\n");
                }
                for(int i = 0;i < row;i++)
                {
                    fw.write(Integer.toString(EntireBox[i].x) + ",");
                    fw.write(Integer.toString(EntireBox[i].y) + ",");
                    fw.write(Integer.toString(EntireBox[i].ImgWidth) + ",");
                    fw.write(Integer.toString(EntireBox[i].ImgHeight) + ",");
                    fw.write(ImgCode[i] + ",");
                    int layerIndex=0;
                    layerIndex = space.getLayer(EntireBox[i]);
                    fw.write(Integer.toString(layerIndex) + "\n");
                }
                for (Component s : space.getComponents())
                {
                    if (s instanceof Shape)
                    {
                        jcomponents.addElement(s);
                    }
                }
                String [] nShape={"Rectangle", "Oval", "Triangle", "PushPin", "TextBox"};
                boolean flag=true;
                for(int j=0;j<5;j++){
                    for(int i=0;i<jcomponents.size();i++){
                        Shape s = ((Shape)jcomponents.get(i));
                            if(s.appearance.equals(nShape[j])){
                                if(flag){
                                    fw.write(nShape[j] + "\n");
                                    flag=false;
                                }
                                //System.out.println(s.S.getLocation());
                                fw.write(Integer.toString(s.getX()) + ",");
                                fw.write(Integer.toString(s.getY()) + ",");
                                fw.write(Integer.toString(s.w) + ",");
                                fw.write(Integer.toString(s.h) + ",");
                                
                                fw.write(Double.toString(s.angleRad) + ",");
                                fw.write(Integer.toString(s.shapeBoder) + ",");
                                fw.write(Integer.toString(s.shapeOutLineColor.getRGB()) + ",");
                                fw.write(Integer.toString(s.shapeBgColor.getRGB()) + ",");
                                fw.write(Integer.toString(s.shapeTextColor.getRGB()) + ",");
                                
                                if(s.isStart){
                                    fw.write(Integer.toString(1) + ",");
                                }
                                else{
                                    fw.write(Integer.toString(0) + ",");
                                }
                                
                                fw.write(Integer.toString(s.clineVector.size()) + ",");

                                for(int k=0;k<s.clineVector.size();k++){
                                    fw.write(Integer.toString(s.clineVector.get(k).abStartX) + ",");
                                    fw.write(Integer.toString(s.clineVector.get(k).abStartY) + ",");
                                    fw.write(Integer.toString(s.clineVector.get(k).abEndX) + ",");
                                    fw.write(Integer.toString(s.clineVector.get(k).abEndY) + ",");
                                }
                                
                                fw.write(Integer.toString(space.getLayer(s)) + ",");
                                
                                if(j==4){
                                    fw.write(((iwLabel)s).str + ",");
                                }
                                fw.write( s.textField.getText().replace("\n", "<EOF>")+ "\n");
                            }   
                        }
                      flag=true;  
                }
                

                fw.flush();
                fw.close();
                jcomponents.clear();
            }catch(IOException ex){}
            System.out.println("done");
        }
        
    }
    public void OpenFile()
    {
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setDialogTitle("開啟舊案");
        FileChooser.setCurrentDirectory(new File("F:\\test\\"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("ITW (*.itw)", "itw"));
        int result = FileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION)
        {
            LoadFile = new File(FileChooser.getSelectedFile().getAbsolutePath());
            System.out.println("OpenFile:");
        }else if(result == JFileChooser.CANCEL_OPTION){
            parent.wallTabController.wallVector.remove(parent.selectedIndex);
            parent.howManyWall--;
            try{
                parent.mainTabbedPane.removeTabAt(parent.selectedIndex);
            }catch(Exception ex){
                System.err.println(ex);
            }
        }
        String [] nShape={"Rectangle", "Oval", "Triangle", "PushPin", "TextBox"};
        try{
            FileReader fr = new FileReader(LoadFile);
            BufferedReader reader = new BufferedReader(fr);
            String str;
            boolean flag=false;
            Vector<ConnectLine> clineVector = new Vector<ConnectLine>();
            Vector<PushPin> allPushPins = new Vector<PushPin>();
            while((str = reader.readLine())!=null)
            {
                if(str.equals("Img") || str.equals(nShape[0]) || str.equals(nShape[1]) || str.equals(nShape[2]) || str.equals(nShape[3]) || str.equals(nShape[4]))
                {
                    Tag = str;
                    continue;
                }
                
                if(Tag.equals("Img"))
                {
                    IncreaseArraySize(State.ImgOperate);
                    String[] data = new String[Imgcol];
                    int i = 0;
                    for(String st: str.split(","))
                    {
                        data[i] = st;
                        i++;
                    }
                    ImgCode[Img_i] = data[4];
                    for(i = 0;i < Imgcol - 1;i++)
                        System.out.print(data[i] + ",");
                    System.out.println(ImgCode[Img_i]);
                    
                    img = new Img(parent, space, Integer.valueOf(data[0]),
                                                 Integer.valueOf(data[1]),
                                                 Integer.valueOf(data[2]),
                                                 Integer.valueOf(data[3]),State.Open);
                    space.setLayer(img,  Integer.valueOf(data[5]) );
                    EntireBox[Img_i] = img;
                    this.Img_i++;
                    this.LoadData_i++; //in img.java
                    img.index=Img_i-1;
//                    data = null;
                }
                for(int j=0;j<5;j++){
                     if(Tag.equals(nShape[j])){
                        int[] data = new int[100];
                        int i = 0;
                        Double angle=0.0;
                        String text="";
                        for(String st: str.split(","))
                        {
                            if(i==4){
                                angle = Double.valueOf(st);
                                i++;
                                continue;
                            }
                            else if(i==12+data[10]*4){
                                text=st;
                                text=text.replace("<EOF>", "\n");
                                break;
                            }
                            data[i] =Integer.valueOf(st);
                            i++;
                        }
                        switch(j){
                            case 0:
                            {space.state=State.drawRect;
                            Rectangle r = new Rectangle(space, data[0], data[1], data[2], data[3]);
                            space.state=State.idle;
                            setRectAttribute(r, data, angle);
                            r.textField.setText(text);}
                            break;
                            case 1:
                            {Oval r = new Oval(space, data[0], data[1], data[2], data[3]);
                            setAttribute(r, data, angle);}
                            break;
                            case 2:
                            {Triangle r = new Triangle(space, data[0], data[1], data[2], data[3]);
                            setAttribute(r, data, angle);}
                            break;
                            case 3:
                                PushPin r = new PushPin(space, data[0], data[1], data[2], data[3]);    
                                setAttribute(r, data, angle); 

                                if(data[10]==0){
                                    continue;
                                }
                                allPushPins.addElement(r);

                                if(data[9]==1){
                                    r.isStart=true; 
                                }
                                else if(data[9]==0){
                                    r.isStart=false;
                                }
                            break;
                            case 4:
                            {space.state=State.drawText;
                            iwLabel s = new iwLabel(space, data[0], data[1], data[2], data[3]);
                            space.state=State.idle;
                            setAttribute(s, data, angle);
                            s.str=text;}
                            break;
                        }
                        
                    } 
                }
                    
                    
//
//                    if(flag==false){
//                        flag=true;
//                    }
//                    else {
//                        r.clineVector=clineVector;
//                        r.cline=clineVector.get(0);
//                        flag=false;
//                        continue;
//                    }
//
//                    for(i=0;i<Integer.valueOf(data[9]);i++){
//                        Point lineStart = new Point(Integer.valueOf(data[10+i]),Integer.valueOf(data[11+i]));
//                        Point lineEnd = new Point(Integer.valueOf(data[12+i]),Integer.valueOf(data[13+i]));
//                        ConnectLine c = new ConnectLine(space, lineStart, lineEnd);
//                        r.clineVector.add(c);
//                    }
//                    clineVector.addAll(r.clineVector);
//                    r.cline=r.clineVector.get(0);
                }
                
                
            reader = new BufferedReader(new FileReader(LoadFile));
            while((str = reader.readLine())!=null)
            {
                if(str.equals("Img") || str.equals(nShape[0]) || str.equals(nShape[1]) || str.equals(nShape[2]) || str.equals(nShape[3]) || str.equals(nShape[4])){
                    Tag = str;
                    continue;
                }
                if(Tag.equals("PushPin")){
                    int[] data = new int[100];
                    int i = 0;
                    int numline=0;
                    for(String st: str.split(","))
                    {
                        if(i==4){
                            i++;
                            continue;
                        }
                        data[i] =Integer.valueOf(st);
                        i++;
                    }
                    numline=data[10];
                    for(int k=0;k<numline;k++){
                        //System.out.println(data.length);
                        Point start = new Point(data[11+k*4], data[12+k*4]);
                        Point end = new Point(data[13+k*4], data[14+k*4]);

                        ConnectLine c=null;
                        boolean firstCline=true;
                        for(i=0;i<allPushPins.size();i++){
                            PushPin pin = allPushPins.get(i);
                            pin.showControlLine();
                            pin.hideControlPoints();
//                            System.err.println("s"+pin.S.getLocation());
//                            System.err.println("line"+start);
                            
                            if(pin.S.getLocation().equals(start) || pin.S.getLocation().equals(end)){
                                if(firstCline){
                                    for(int j=0;j<pin.clineVector.size();j++){
                                        Point startcline = new Point(pin.clineVector.get(j).abStartX, pin.clineVector.get(j).abStartY);
                                        Point endcline = new Point(pin.clineVector.get(j).abEndX, pin.clineVector.get(j).abEndY);
                                        if(start.equals(startcline) || end.equals(endcline)){
                                            //pin.clineVector.addElement(c);
                                            c=pin.clineVector.get(j);
                                            firstCline=false;
                                            continue;
                                        }
                                    }
                                        c=new ConnectLine(space, start, end);
                                        pin.clineVector.addElement(c);
                                        firstCline=false;
                                    }
                                    else{
                                        pin.clineVector.addElement(c);
                                    }
                            }
                        }
                    }
                }
            }
            /*int dot = 0;
            char c;
            for(int i = 0; i < str.length();i++)
            {
                c = str.charAt(i);
                if(c == ',')
                    dot++;
            }
            row = (dot + 1) / (col + 1);
            /*-------
            String[][] Data = new String[row][col];
            this.i = row;
            ImgData = new int[row][col];
            ImgCode = new String[row];
            EntireBox = new ImgBox[row];
            System.out.println("i: "+this.i+", row: "+row);
            /*-------
            int i = 0, j = 0;
            for(String st: str.split(","))
            {
                Data[i][j] = st;
                j++;
                if(j == col + 1)
                {
                    j = 0;
                    i++;
                }
            }
            
            /*for(i = 0;i < row;i++)
                for(j = 0;j < col + 1;j++)
                    System.out.println("Data["+i+"]["+j+"]: "+Data[i][j]);
            
            for(i = 0;i < row;i++)
                for(j = 0;j < col;j++)
                    ImgData[i][j] = Integer.parseInt(Data[i][j]);
            for(i = 0; i < row;i++)
                ImgCode[i] = Data[i][4];
            
            for(i = 0;i < row;i++)
                for(j = 0;j < col;j++)
                    System.out.println("ImgData["+i+"]["+j+"]: "+ImgData[i][j]);*/
            
        }catch(IOException ex){}
        /*for(int i = 0; i < row;i++)
        {
            LoadData_i = i;
            img = new Img(parent, space, ImgData[i][0], ImgData[i][1],
                                         ImgData[i][2], ImgData[i][3], State.Open);
            EntireBox[i] = img;
        }*/
        System.out.println("done");
    }

          void setAttribute(Shape r, int []data, Double angle){
                    r.angleRad=angle;
                    r.shapeBoder=data[5];
                    r.shapeOutLineColor=new Color(data[6]);
                    r.shapeBgColor=new Color(data[7]);
                    r.shapeTextColor=new Color(data[8]);
                    space.setLayer(r, data[11+data[10]*4]);
                    r.hideControlPoints();
                    r.state=State.idle;
           }
           void setRectAttribute(Shape r, int []data, Double angle){ 
                    r.angleRad=angle;
                    r.shapeBoder=data[5];
                    r.shapeOutLineColor=new Color(data[6]);
                    r.shapeBgColor=new Color(data[7]);
                    r.shapeTextColor=new Color(data[8]);
                    space.setLayer(r, data[11+data[10]*4]);                   
                    r.textField.setForeground(r.shapeTextColor);
                    r.textField.setBackground(r.shapeBgColor);
                    space.setLayer(r.textField, data[11+data[10]*4]+1);
                    r.hideControlPoints();
                    r.state=State.idle;
           }
}
