/*
   Graph Reader - utility for retrieving graph data from image files
 
   Copyright 2012 Oleg Kalashev

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License
*/

package graphreader;

import java.awt.*;
import java.io.File;
import java.util.Vector;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


/**
 * <p>Title: Graph Reader</p>
 *
 * <p>Description: Utility for retrieving graph data from image files</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * @author Oleg Kalashev
 * @version 1.2
 */
public class GraphCanvas extends Canvas{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7032115983677147383L;
	Image fImage;
    int fImgWidth = 0;
    int fImgHeight = 0;
    static final int fDashSize=8;
    static final int fPointSize=6;
    Cursor fCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    Cursor fWaitCursor = new Cursor(Cursor.WAIT_CURSOR);
    //graph points and ranges are in internal representation (0.0-1.0)
    Vector<GraphPoint> fGraph = new Vector<GraphPoint>();// graph points
    GraphPoint fRanges[] = new GraphPoint[4];// Xmin, Xmax, Ymin, Ymax

    int fInputType = 0;
    static final int XMIN = 0;
    static final int XMAX = 1;
    static final int YMIN = 2;
    static final int YMAX = 3;
    static final int INPUT_RANGES = 4;
    static final int INPUT_PLOT = 5;
    IGraphObserver fObserver;
    private boolean fLoadingInProgress = false;
    Thread fLoadingThread;
    int fLastInputType=-1;

    /**
     * @wbp.parser.entryPoint
     */
    public GraphCanvas(String aImageFile) {
        try{
                if(aImageFile!=null)
                {
                	fImage = getToolkit().getImage(aImageFile);///GIF, JPEG, PNG
                	fImage.getHeight(this);
                }
            }catch(Exception e){
            	e.printStackTrace();
            }

        setBackground(Color.WHITE);
        setFocusable(true);
        setMinimumSize(new Dimension(300,300));
        setSize(500,500);
        setIgnoreRepaint(false);
        resetRanges();
        addMouseListener(new GraphCanvas_this_mouseAdapter(this));
        addMouseMotionListener(new GraphCanvas_this_mouseMotionAdapter(this));
        setCursor(fCursor);
        addKeyListener(new GraphCanvasKeyAdapter(this));
    }

    public Vector<GraphPoint> getGraph() {
        return fGraph;
    }

    public Range getXRange() {
        if(fRanges[XMIN]==null || fRanges[XMAX]==null)
            return null;
        return new Range(fRanges[XMIN].x,fRanges[XMAX].x);
    }

    public Range getYRange() {
    if(fRanges[YMIN]==null || fRanges[YMAX]==null)
        return null;
    return new Range(fRanges[YMIN].y,fRanges[YMAX].y);
    }

    void setInputType(int aType){
        fInputType = aType%6;
    }

    int getInputType(){
        return fInputType;
    }

    void setObserver(IGraphObserver aObserver){
        fObserver = aObserver;
    }

    void loadImage(File aImageFile) throws Exception{
        if(fLoadingInProgress)
            throw new Exception("Operation in progress, please, wait..");
        fLoadingInProgress = true;
        fLoadingThread = new Thread(new ImageFileLoader(aImageFile, this));
        fLoadingThread.start();
    }

    void readImageFromClipboard() throws Exception{
        if(fLoadingInProgress)
            throw new Exception("Operation in progress, please, wait..");
        fLoadingInProgress = true;
        fLoadingThread = new Thread(new ImageClipboardLoader());
        fLoadingThread.start();
    }

    public void setExternalImage(BufferedImage aImage){
        fImage = aImage;
        fImgWidth = aImage.getWidth();
        fImgHeight = aImage.getHeight();
        resetRanges();
        repaint();
    }

    void cancelImageLoading(){
        if(fLoadingInProgress&&fLoadingThread!=null){
            fLoadingInProgress = false;
            fLoadingThread.interrupt();
        }
        setCursor(fCursor);
    }

    void resetRanges(){
        fRanges[XMIN] = new GraphPoint(0,0);
        fRanges[XMAX] = new GraphPoint(1,0);
        fRanges[YMIN] = new GraphPoint(0,0);
        fRanges[YMAX] = new GraphPoint(0,1);
    }

    public Dimension getPreferredSize(){
        if(fImgWidth>0 && fImgHeight>0)
            return new Dimension(fImgWidth,fImgHeight);
        return super.getPreferredSize();
    }

    public void paint(Graphics g) {
        Dimension size = getSize();

        if(fImage!=null&&fImgWidth>0&&fImgHeight>0){
            g.drawImage(fImage, 0, 0,
                                  size.width ,
                                  size.height,
                                  0,
                                  0,
                                  fImgWidth,
                                  fImgHeight,
                                  this);
            g.setColor(Color.RED);
            Point xmin = internal2pixel(fRanges[XMIN],size);
            g.drawLine(xmin.x, xmin.y - fDashSize/2, xmin.x, xmin.y + fDashSize/2);
            Point xmax = internal2pixel(fRanges[XMAX],size);
            g.drawLine(xmax.x, xmax.y - fDashSize/2, xmax.x, xmax.y + fDashSize/2);
            Point ymin = internal2pixel(fRanges[YMIN],size);
            g.drawLine(ymin.x - fDashSize/2, ymin.y, ymin.x + fDashSize/2, ymin.y);
            Point ymax = internal2pixel(fRanges[YMAX],size);
            g.drawLine(ymax.x - fDashSize/2, ymax.y, ymax.x + fDashSize/2, ymax.y);
            g.setColor(Color.GREEN);
            for(int i=0; i<fGraph.size(); i++){
                Point p = internal2pixel((GraphPoint)fGraph.elementAt(i),size);
                g.drawLine(p.x - fPointSize/2, p.y, p.x + fPointSize/2, p.y);
                g.drawLine(p.x, p.y - fPointSize/2, p.x, p.y + fPointSize/2);
            }
        }
    }
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height){
        if((infoflags&WIDTH)!=0 && (infoflags&HEIGHT)!=0 && (fImgWidth!=width || fImgHeight!=height)){
            fImgWidth = width; fImgHeight = height;
        }
        return super.imageUpdate(img, infoflags,x,y,width,height);
    }

    private Point internal2pixel(GraphPoint aPoint, Dimension aSize){
        int x,y;
        double val, lval;

        val = aSize.width * aPoint.x;
        lval = Math.floor(val);
        x = (int)((val - lval < 0.5)?lval:lval+1.);

        val = aSize.height * (1.-aPoint.y);
        lval = Math.floor(val);
        y = (int)((val - lval < 0.5)?lval:lval+1.);

        return new Point(x,y);
    }

    public void this_mouseClicked(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==1&&!fLoadingInProgress){
            Dimension size = getSize();
            GraphPoint point = new GraphPoint(e.getX()/size.getWidth(), 1. - e.getY()/size.getHeight());
            handleNewPoint(point);
            repaint(e.getX()-fDashSize/2,e.getY()-fDashSize/2,fDashSize,fDashSize);
        }
        requestFocus();
    }

    public void this_mouseMoved(MouseEvent e) {
          if(fObserver!=null&&!fLoadingInProgress){
            Dimension size = getSize();
            GraphPoint point = new GraphPoint(e.getX()/size.getWidth(), 1. - e.getY()/size.getHeight());
            fObserver.mouseMove(point);
          }
    }


    protected void handleNewPoint(GraphPoint aPoint){
        fLastInputType = fInputType;
        if(fInputType!=INPUT_RANGES)
        {
	        if(fInputType==INPUT_PLOT){
	            fGraph.add(aPoint);
	        }else{
	            Point oldPoint= internal2pixel(fRanges[fInputType], getSize());
	            fRanges[fInputType] = aPoint;
	            repaint(oldPoint.x-fDashSize,oldPoint.y-fDashSize,2*fDashSize,2*fDashSize);
	        }
        }
        if(fObserver!=null)
            fObserver.onNewPoint(aPoint,fInputType);
    }

    /*
    Removes last added point.
    @return true if farther undo available
     */
    public boolean undo(){
        if(!fGraph.isEmpty()&&!fLoadingInProgress) {
            Point oldPoint= internal2pixel((GraphPoint)fGraph.remove(fGraph.size()-1), getSize());
            repaint(oldPoint.x-fPointSize,oldPoint.y-fPointSize,2*fPointSize,2*fPointSize);
        }
        return !fGraph.isEmpty();
    }

    public void clearGraph(){
        fGraph.clear();
        repaint();
    }

    /*
    @return true if undo available
     */
    public boolean canUndo(){
        return !fGraph.isEmpty();
    }

    void moveLastPoint(int x, int y)
    {
        Dimension size = getSize();
        Point oldPoint;
        if(fLastInputType==INPUT_PLOT){
            if(fGraph.size()==0)
                return;
            oldPoint= internal2pixel((GraphPoint)fGraph.remove(fGraph.size()-1), getSize());
        }else{
            oldPoint= internal2pixel(fRanges[fLastInputType], getSize());
        }
        Point newPoint = new Point(oldPoint);
        int newX = oldPoint.x + x;
        if(newX >= 0 && newX < size.width)
            newPoint.x = newX;
        int newY = oldPoint.y - y;
        if(newY >= 0 && newY < size.height)
            newPoint.y = newY;
        GraphPoint newGraphPoint = new GraphPoint(newPoint.getX()/size.getWidth(), 1. - newPoint.getY()/size.getHeight());
        if(fLastInputType==INPUT_PLOT){
            fGraph.add(newGraphPoint);
        }else{
            fRanges[fLastInputType] = newGraphPoint;
        }
        repaint(oldPoint.x-fPointSize,oldPoint.y-fPointSize,2*fPointSize,2*fPointSize);
        repaint(newPoint.x-fPointSize,newPoint.y-fPointSize,2*fPointSize,2*fPointSize);
    }

    public void onKeyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
        case 37://left
            moveLastPoint(-1, 0);
            break;
        case 39://right
            moveLastPoint(1, 0);
            break;
        case 38://up
            moveLastPoint(0, 1);
            break;
        case 40://down
            moveLastPoint(0, -1);
            break;
        }
    }

    class ImageClipboardLoader implements Runnable{ //loads image from clipboard
        public void run() {
            try {
                setCursor(fWaitCursor);
                Clipboard clipboard = getToolkit().getSystemClipboard();
                BufferedImage img = (BufferedImage)clipboard.getContents(this).getTransferData(DataFlavor.imageFlavor);
                //Thread.sleep(3000);//test
                if(fLoadingInProgress){//loading was not canceled
                    setExternalImage(img);
                    fObserver.imageLoaded(null);
                }
            } catch (Exception e) {
                if(fObserver!=null)
                    fObserver.imageLoaded(e);
                else
                    e.printStackTrace();
            }
            setCursor(fCursor);
            fLoadingInProgress = false;
        }
    }

    class ImageFileLoader implements Runnable{ //loads image from clipboard
    File fImageFile;
    GraphCanvas fSelf;
    ImageFileLoader(File aImageFile, GraphCanvas aParent){
        fImageFile = aImageFile;
        fSelf = aParent;
    }
    public void run() {
        try {
            setCursor(fWaitCursor);
            //Thread.sleep(3000);//test
            Image img = getToolkit().getImage(fImageFile.getAbsolutePath());///GIF, JPEG, PNG
            if(fLoadingInProgress){//loading was not canceled
                fImage = img;
                fImage.getHeight(fSelf);
                resetRanges();
                repaint();
                fObserver.imageLoaded(null);
            }
        } catch (Exception e) {
            if(fObserver!=null)
                fObserver.imageLoaded(e);
            else
                e.printStackTrace();
        }
        setCursor(fCursor);
        fLoadingInProgress = false;
    }
}

}


class GraphCanvas_this_mouseAdapter extends MouseAdapter {
    private GraphCanvas adaptee;
    GraphCanvas_this_mouseAdapter(GraphCanvas adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.this_mouseClicked(e);
    }
}


class GraphCanvas_this_mouseMotionAdapter extends MouseMotionAdapter {
    private GraphCanvas adaptee;
    GraphCanvas_this_mouseMotionAdapter(GraphCanvas adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseMoved(MouseEvent e) {
        adaptee.this_mouseMoved(e);
    }
}

class GraphCanvasKeyAdapter extends KeyAdapter {
    private GraphCanvas adaptee;
    GraphCanvasKeyAdapter(GraphCanvas adaptee) {
        this.adaptee = adaptee;
    }

    public void keyPressed(KeyEvent e) {
        adaptee.onKeyPressed(e);
    }
}
