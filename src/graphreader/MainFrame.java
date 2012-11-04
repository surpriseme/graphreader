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
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import graphreader.res.Resources;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * <p>Title: Graph Reader</p>
 *
 * <p>Description: Utility for retrieving graph data from image files</p>
 *
 * @author Oleg Kalashev
 * @version 1.2
 */
public class MainFrame extends JFrame implements IGraphObserver{
	private static final long serialVersionUID = -2690275308445636370L;
	JPanel fContentPane;
    BorderLayout fBorderLayout1 = new BorderLayout();
    JMenuBar fMenuBar1 = new JMenuBar();
    JMenu fMenuFile = new JMenu();
    JMenuItem fMenuFileExit = new JMenuItem();
    JMenu fMenuHelp = new JMenu();
    JMenuItem fMenuHelpAbout = new JMenuItem();
    JToolBar fToolBar = new JToolBar();
    JButton fButtonOpen = new JButton();
    JButton fButtonSave = new JButton();
    JButton fButtonHelp = new JButton();
    ImageIcon fImageOpen = new ImageIcon(Resources.getResource("openFile.png"));
    ImageIcon fImageHelp = new ImageIcon(Resources.getResource("help.png"));
    ImageIcon fImageSave = new ImageIcon(Resources.getResource("saveFile.gif"));
    ImageIcon fImageStop = new ImageIcon(Resources.getResource("stop.png"));
    JLabel fStatusBar = new JLabel();
    JPanel fPanelSettings = new JPanel();
    GridBagLayout fGridBagLayout1 = new GridBagLayout();
    JLabel fLabelXmin = new JLabel();
    JTextField fTextFieldXmin = new JTextField();
    Component fHbox1 = Box.createHorizontalStrut(8);
    JLabel fLabelXmax = new JLabel();
    JTextField fTextFieldXmax = new JTextField();
    Component fVbox1 = Box.createVerticalStrut(8);
    Component fHbox2 = Box.createHorizontalStrut(8);
    Component fHbox3 = Box.createHorizontalStrut(8);
    Component fHbox4 = Box.createHorizontalStrut(8);
    Component fHbox5 = Box.createHorizontalStrut(8);
    JLabel fLableYmin = new JLabel();
    JTextField fTextFieldYmin = new JTextField();
    JLabel fLabelYmax = new JLabel();
    JTextField fTextFieldYmax = new JTextField();
    JCheckBox fCheckBoxLogscaleX = new JCheckBox();
    Component fHbox6 = Box.createHorizontalStrut(8);
    Component fHbox7 = Box.createHorizontalStrut(8);
    Component fHbox8 = Box.createHorizontalStrut(8);
    JToolBar fToolBarGraph = new JToolBar();
    JToggleButton fToggleButtonXmin = new JToggleButton();
    JToggleButton fToggleButtonXmax = new JToggleButton();
    JToggleButton fToggleButtonYmin = new JToggleButton();
    JToggleButton fToggleButtonYmax = new JToggleButton();
    JToggleButton fToggleButtonPlot = new JToggleButton();
    Component fVbox2 = Box.createVerticalStrut(8);
    JButton fButtonUndo = new JButton();
    ButtonGroup fInputTypeButtonGroup = new ButtonGroup();
    Component fVbox3 = Box.createVerticalStrut(8);
    public MainFrame() {
    	getContentPane().setBackground(new Color(192, 192, 192));
        try {
            init(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public MainFrame(String aImageFile) {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            //customInit(aImageFile);
            init(aImageFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void init(String aImageFile) throws Exception {
        fContentPane = (JPanel) getContentPane();
        fContentPane.setLayout(fBorderLayout1);
        setSize(new Dimension(400, 300));
        setTitle("Graph Reader");
        fStatusBar.setText("  ");
        fMenuFile.setText("File");
        fMenuFileExit.setText("Exit");
        fMenuFileExit.addActionListener(new
                                        MainFrame_fMenuFileExit_ActionAdapter(this));
        fMenuHelp.setText("Help");
        fMenuHelpAbout.setText("About");
        fMenuHelpAbout.addActionListener(new
                                         MainFrame_fMenuHelpAbout_ActionAdapter(this));
        fButtonOpen.setMaximumSize(new Dimension(23, 23));
        fButtonOpen.setMinimumSize(new Dimension(23, 23));
        fButtonOpen.setPreferredSize(new Dimension(23, 23));
        fButtonOpen.setActionCommand("open");
        fButtonOpen.addActionListener(new MainFrame_fButtonOpen_actionAdapter(this));
        fButtonSave.setMaximumSize(new Dimension(23, 23));
        fButtonSave.setMinimumSize(new Dimension(23, 23));
        fButtonSave.setPreferredSize(new Dimension(23, 23));
        fButtonSave.setActionCommand("save");
        fButtonSave.addActionListener(new MainFrame_fButtonSave_actionAdapter(this));
        fButtonHelp.setMaximumSize(new Dimension(23, 23));
        fButtonHelp.setMinimumSize(new Dimension(23, 23));
        fButtonHelp.setPreferredSize(new Dimension(23, 23));
        fButtonHelp.setActionCommand("help");
        fButtonHelp.addActionListener(new MainFrame_fButtonHelp_actionAdapter(this));
        fMenuFileOpen.setActionCommand("open");
        fMenuFileOpen.setText("From file");
        fMenuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.
                awt.event.KeyEvent.VK_O, java.awt.event.KeyEvent.CTRL_MASK, false));
        fMenuFileOpen.addActionListener(new
                                        MainFrame_fMenuFileOpen_actionAdapter(this));
        fMenuFileSave.setActionCommand("save");
        fMenuFileSave.setText("Save");
        fMenuFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.
                awt.event.KeyEvent.VK_S, java.awt.event.KeyEvent.CTRL_MASK, false));
        fMenuFileSave.addActionListener(new
                                        MainFrame_fMenuFileSave_actionAdapter(this));
        fMenuLoad.setText("Load image..");
        fMenuItemClipboard.setActionCommand("loadClipboard");
        fMenuItemClipboard.setText("From clipboard");
        fMenuItemClipboard.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.CTRL_MASK, false));
        fMenuItemClipboard.addActionListener(new
                MainFrame_fMenuItemClipboard_actionAdapter(this));
        fMenuItemUndo.setEnabled(false);
        fMenuItemUndo.setToolTipText("Undo last action");
        fMenuItemUndo.setText("Undo");
        fMenuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.
                event.KeyEvent.VK_Z, java.awt.event.KeyEvent.CTRL_MASK, false));
        fMenuItemUndo.addActionListener(new
                                        MainFrame_fMenuItemUndo_actionAdapter(this));
        fMenuEdit.setText("Edit");
        fMenuItemClear.setEnabled(false);
        fMenuItemClear.setToolTipText("Clear graph points");
        fMenuItemClear.setActionCommand("Clear");
        fMenuItemClear.setText("Clear");
        fMenuItemClear.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.
                awt.event.KeyEvent.VK_DELETE, java.awt.event.KeyEvent.CTRL_MASK, false));
        fMenuItemClear.addActionListener(new
                                         MainFrame_fMenuItemClear_actionAdapter(this));
        fMenuBar1.add(fMenuFile);
        fMenuFile.add(fMenuLoad);
        fMenuFile.add(fMenuFileSave);
        fMenuFile.add(fMenuFileExit);
        fMenuBar1.add(fMenuEdit);
        fMenuBar1.add(fMenuHelp);
        fMenuHelp.add(fMenuHelpAbout);
        setJMenuBar(fMenuBar1);
        fButtonOpen.setIcon(fImageOpen);
        fButtonOpen.setToolTipText("Open Image");
        fButtonSave.setIcon(fImageSave);
        fButtonSave.setToolTipText("Save");
        fButtonHelp.setIcon(fImageHelp);
        fButtonHelp.setToolTipText("Help");
        fToolBar.add(fButtonOpen);
        fToolBar.add(fButtonSave);
        fToolBar.add(fButtonHelp);
        fContentPane.add(fToolBar, BorderLayout.NORTH);
        fContentPane.add(fStatusBar, BorderLayout.SOUTH);
        fMenuLoad.add(fMenuFileOpen);
        fMenuLoad.add(fMenuItemClipboard);
        fMenuEdit.add(fMenuItemUndo);
        fMenuEdit.add(fMenuItemClear);
        
        setSize(new Dimension(800, 600));
        fCentralPanel.setBackground(new Color(192, 192, 192));
        
        getContentPane().add(fCentralPanel, BorderLayout.CENTER);
        fCentralPanel.setLayout(new CardLayout(0, 0));
        
        fCentralPanel.add(fPanelPlot);
        fToggleButtonXmin.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonXmin.setMaximumSize(new Dimension(47, 25));
        fToggleButtonXmin.setMinimumSize(new Dimension(47, 25));
        fToggleButtonXmin.setPreferredSize(new Dimension(47, 25));
        fToggleButtonXmin.setToolTipText("Set Xmin");
        fToggleButtonXmin.setActionCommand("SetXmin");
        fToggleButtonXmin.setText("Xmin");
        fToggleButtonXmin.addActionListener(new
                MainFrame_fToggleButtonXmin_actionAdapter(this));
        fPanelPlot.setLayout(new BorderLayout(0, 0));
        fPanelPlot.add(fToolBarGraph, BorderLayout.EAST);
        fToolBarGraph.setOrientation(JToolBar.VERTICAL);
        fToggleButtonXmax.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonXmax.setMaximumSize(new Dimension(47, 25));
        fToggleButtonXmax.setMinimumSize(new Dimension(47, 25));
        fToggleButtonXmax.setPreferredSize(new Dimension(47, 25));
        fToggleButtonXmax.setToolTipText("Set Xmax");
        fToggleButtonXmax.setActionCommand("SetXmax");
        fToggleButtonXmax.setText("Xmax");
        fToggleButtonXmax.addActionListener(new
                MainFrame_fToggleButtonXmax_actionAdapter(this));
        fToggleButtonYmin.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonYmin.setMaximumSize(new Dimension(47, 25));
        fToggleButtonYmin.setMinimumSize(new Dimension(47, 25));
        fToggleButtonYmin.setPreferredSize(new Dimension(47, 25));
        fToggleButtonYmin.setToolTipText("Set Ymin");
        fToggleButtonYmin.setActionCommand("SetYmin");
        fToggleButtonYmin.setText("Ymin");
        fToggleButtonYmin.addActionListener(new
                MainFrame_fToggleButtonYmin_actionAdapter(this));
        fToggleButtonYmax.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonYmax.setMaximumSize(new Dimension(47, 25));
        fToggleButtonYmax.setMinimumSize(new Dimension(47, 25));
        fToggleButtonYmax.setPreferredSize(new Dimension(47, 25));
        fToggleButtonYmax.setToolTipText("Set Ymax");
        fToggleButtonYmax.setActionCommand("SetYmax");
        fToggleButtonYmax.setText("Ymax");
        fToggleButtonYmax.addActionListener(new
                MainFrame_fToggleButtonYmax_actionAdapter(this));
        fToggleButtonScale.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent arg0) {
        		fRangesPanel.setVisible(fToggleButtonScale.isSelected());
        	}
        });
        fToggleButtonScale.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonScale.setMaximumSize(new Dimension(47, 25));
        fToggleButtonScale.setMinimumSize(new Dimension(47, 25));
        fToggleButtonScale.setPreferredSize(new Dimension(47, 25));
        fToggleButtonScale.setToolTipText("Set Scale");
        fToggleButtonScale.setActionCommand("SetScale");
        fToggleButtonScale.setText("Scale");
        fToggleButtonScale.addActionListener(new
                MainFrame_fToggleButtonScale_actionAdapter(this));
        fToggleButtonPlot.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fToggleButtonPlot.setMaximumSize(new Dimension(47, 25));
        fToggleButtonPlot.setMinimumSize(new Dimension(47, 25));
        fToggleButtonPlot.setPreferredSize(new Dimension(47, 25));
        fToggleButtonPlot.setToolTipText("Add points");
        fToggleButtonPlot.setText("Graph");
        fToggleButtonPlot.addActionListener(new
                MainFrame_fToggleButtonPlot_actionAdapter(this));
        fButtonUndo.setEnabled(false);
        fButtonUndo.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fButtonUndo.setMaximumSize(new Dimension(47, 25));
        fButtonUndo.setMinimumSize(new Dimension(47, 25));
        fButtonUndo.setPreferredSize(new Dimension(47, 25));
        fButtonUndo.setToolTipText("Delete last point");
        fButtonUndo.setMnemonic('0');
        fButtonUndo.setText("Undo");
        fButtonUndo.addActionListener(new MainFrame_fButtonUndo_actionAdapter(this));
        fButtonClear.setEnabled(false);
        fButtonClear.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
        fButtonClear.setMaximumSize(new Dimension(47, 25));
        fButtonClear.setMinimumSize(new Dimension(47, 25));
        fButtonClear.setPreferredSize(new Dimension(47, 25));
        fButtonClear.setToolTipText("Clear points");
        fButtonClear.setText("Clear");
        fButtonClear.addActionListener(new MainFrame_fButtonClear_actionAdapter(this));
        fToolBarGraph.add(fToggleButtonXmin);
        fToolBarGraph.add(fToggleButtonXmax);
        fToolBarGraph.add(fToggleButtonYmin);
        fToolBarGraph.add(fToggleButtonYmax);
        fToolBarGraph.add(fToggleButtonScale);
        fToolBarGraph.add(fToggleButtonPlot);
        fToolBarGraph.add(fVbox2);
        fToolBarGraph.add(fButtonUndo);
        fToolBarGraph.add(fButtonClear);
        fInputTypeButtonGroup.add(fToggleButtonXmin);
        fInputTypeButtonGroup.add(fToggleButtonXmax);
        fInputTypeButtonGroup.add(fToggleButtonYmin);
        fInputTypeButtonGroup.add(fToggleButtonYmax);
        fInputTypeButtonGroup.add(fToggleButtonScale);
        fInputTypeButtonGroup.add(fToggleButtonPlot);
        fPlotCanvas = new GraphCanvas(aImageFile);
        fPanelPlot.add(fPlotCanvas, BorderLayout.CENTER);
                                
        fPanelPlot.add(fRangesPanel, BorderLayout.NORTH);
        fRangesPanel.add(fPanelSettings);
        fPanelSettings.setLayout(fGridBagLayout1);
        fLabelXmin.setText("Xmin");
        fTextFieldXmin.setMinimumSize(new Dimension(50, 20));
        fTextFieldXmin.setPreferredSize(new Dimension(70, 20));
        fTextFieldXmin.setText("0.1");
        fTextFieldXmin.addFocusListener(new
                                        MainFrame_fTextFieldXmin_focusAdapter(this));
        fLabelXmax.setText("Xmax");
        fTextFieldXmax.setMinimumSize(new Dimension(50, 20));
        fTextFieldXmax.setPreferredSize(new Dimension(70, 20));
        fTextFieldXmax.setText("1");
        fTextFieldXmax.addFocusListener(new
                                        MainFrame_fTextFieldXmax_focusAdapter(this));
        //component6.setMinimumSize(new Dimension(20, 0));
        //component6.setMaximumSize(new Dimension(80, 32767));
        fLableYmin.setText("Ymin");
        fTextFieldYmin.setMinimumSize(new Dimension(50, 20));
        fTextFieldYmin.setPreferredSize(new Dimension(70, 20));
        fTextFieldYmin.setText("0.1");
        fTextFieldYmin.addFocusListener(new
                                        MainFrame_fTextFieldYmin_focusAdapter(this));
        fLabelYmax.setText("Ymax");
        fTextFieldYmax.setMinimumSize(new Dimension(50, 20));
        fTextFieldYmax.setPreferredSize(new Dimension(70, 20));
        fTextFieldYmax.setText("1");
        fTextFieldYmax.addFocusListener(new
                                        MainFrame_fTextFieldYmax_focusAdapter(this));
        fCheckBoxLogscaleX.setText("Logscale");
        jCheckBoxLogscaleY.setText("Logscale");
        fPanelSettings.add(fLabelXmin,
                           new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fTextFieldXmin,
                           new GridBagConstraints(2, 0, 1, 4, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox1,
                           new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fLabelXmax,
                           new GridBagConstraints(6, 0, 1, 4, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fTextFieldXmax,
                           new GridBagConstraints(8, 0, 1, 4, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fVbox1,
                           new GridBagConstraints(3, 4, 3, 2, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox2,
                           new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox3,
                           new GridBagConstraints(1, 1, 1, 3, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox4,
                           new GridBagConstraints(7, 1, 1, 3, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox5,
                           new GridBagConstraints(5, 2, 1, 2, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fLableYmin,
                           new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fTextFieldYmin,
                           new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fLabelYmax,
                           new GridBagConstraints(6, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fTextFieldYmax,
                           new GridBagConstraints(8, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox6,
                           new GridBagConstraints(9, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox7,
                           new GridBagConstraints(10, 4, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fHbox8,
                           new GridBagConstraints(11, 2, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(fCheckBoxLogscaleX,
                           new GridBagConstraints(12, 3, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
        fPanelSettings.add(jCheckBoxLogscaleY,
                           new GridBagConstraints(12, 6, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
                                
        fTextFieldXmin.setText(Double.toString(fXrange.min));
        fTextFieldXmax.setText(Double.toString(fXrange.max));
        fTextFieldYmin.setText(Double.toString(fYrange.min));
        fTextFieldYmax.setText(Double.toString(fYrange.max));
        
        fRangesPanel.add(fHbox9);
        fBtnDone.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		onBtnDone();
        	}
        });
        
        fRangesPanel.add(fBtnDone);
        fWelcomeTextPane.setBackground(new Color(192, 192, 192));
        
        fWelcomeTextPane.setEditable(false);
        fWelcomeTextPane.setContentType("text/html");
        try {
        	fWelcomeTextPane.setPage(MainFrame.class.getResource("/graphreader/res/hello.html"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        fCentralPanel.add(fWelcomeTextPane);      
        fPlotCanvas.setObserver(this);
        
        fInputTypeButtons.add(fToggleButtonXmin.getModel());
        fInputTypeButtons.add(fToggleButtonXmax.getModel());
        fInputTypeButtons.add(fToggleButtonYmin.getModel());
        fInputTypeButtons.add(fToggleButtonYmax.getModel());
        fInputTypeButtons.add(fToggleButtonScale.getModel());
        fInputTypeButtons.add(fToggleButtonPlot.getModel());
        fInputTypeButtonGroup.setSelected(fToggleButtonXmin.getModel(), true);
        fRangesPanel.setVisible(false);
        EnableWelcomeView(aImageFile == null);
    }
    
    void EnableWelcomeView(boolean aEnable)
    {
    	fPanelPlot.setVisible(!aEnable);
    	fWelcomeTextPane.setVisible(aEnable);
    	fPlotCanvas.setVisible(!aEnable);
    }

    /**
     * File | Exit action performed.
     *
     * @param actionEvent ActionEvent
     */
    void fMenuFileExit_actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Help | About action performed.
     *
     * @param actionEvent ActionEvent
     */
    void fMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
        AboutDialog.ShowDlg(this);
    }

    //Manually added controls and data below
    GraphCanvas fPlotCanvas;
    Vector<ButtonModel>      fInputTypeButtons = new Vector<ButtonModel>();
    Range       fXrange = new Range(0.1,1.);
    Range       fYrange = new Range(0.1,1.);
    JCheckBox jCheckBoxLogscaleY = new JCheckBox();

    double graph2real(Range aGraphRange, Range aRealRange, double aPoint, boolean aLogscale) throws Exception{
        if(aGraphRange.max == aGraphRange.min)
            throw new Exception("Unable to calculate (x,y) : min=max");

        if(aLogscale)
            return aRealRange.min*Math.pow(aRealRange.max/aRealRange.min,
                                           (aPoint - aGraphRange.min)/(aGraphRange.max - aGraphRange.min));
        else
            return aRealRange.min +
                (aRealRange.max - aRealRange.min) / (aGraphRange.max - aGraphRange.min) * (aPoint - aGraphRange.min);
    }

    public void mouseMove(GraphPoint aPoint) {
        try{
            double x = graph2real(fPlotCanvas.getXRange(), fXrange, aPoint.x, fCheckBoxLogscaleX.isSelected());
            double y = graph2real(fPlotCanvas.getYRange(), fYrange, aPoint.y, jCheckBoxLogscaleY.isSelected());
            fStatusBar.setText("x="+x+" y="+y);
        }catch(Exception e){
            fStatusBar.setText(e.getMessage());
        }
    }

    public void onNewPoint(GraphPoint aPoint, int aInputType){
        if(aInputType == GraphCanvas.INPUT_PLOT){
            fButtonUndo.setEnabled(true);
            fMenuItemClear.setEnabled(true);
            fMenuItemUndo.setEnabled(true);
            fButtonClear.setEnabled(true);
        }else
        {
        	if(aInputType==GraphCanvas.INPUT_RANGES)
            {
        		if(!ShowConfirmationDialog("Confirm", "Done with editing scale?", fPlotCanvas))
        			return;
            }
        	aInputType++;
            fPlotCanvas.setInputType(aInputType);
            fInputTypeButtonGroup.setSelected((ButtonModel)fInputTypeButtons.elementAt(fPlotCanvas.getInputType()), true);
        }
    }

    public void fToggleButtonXmin_actionPerformed(ActionEvent e) {
        fPlotCanvas.setInputType(GraphCanvas.XMIN);
    }

    public void fToggleButtonXmax_actionPerformed(ActionEvent e) {
        fPlotCanvas.setInputType(GraphCanvas.XMAX);
    }
    
    public void fToggleButtonScale_actionPerformed(ActionEvent e) {
    	fPlotCanvas.setInputType(GraphCanvas.INPUT_RANGES);
    	//panelRanges.setVisible(true);
    }

    public void fToggleButtonYmin_actionPerformed(ActionEvent e) {
        fPlotCanvas.setInputType(GraphCanvas.YMIN);
    }

    public void fToggleButtonYmax_actionPerformed(ActionEvent e) {
        fPlotCanvas.setInputType(GraphCanvas.YMAX);
    }
    
    void onBtnDone()
    {
    	//panelRanges.setVisible(false);
    	fPlotCanvas.setInputType(GraphCanvas.INPUT_PLOT);
    	fInputTypeButtonGroup.setSelected((ButtonModel)fInputTypeButtons.elementAt(GraphCanvas.INPUT_PLOT), true);
    }

    public void fToggleButtonPlot_actionPerformed(ActionEvent e) {
        fPlotCanvas.setInputType(GraphCanvas.INPUT_PLOT);
    }

    public void fButtonUndo_actionPerformed(ActionEvent e) {
        boolean canUndo = fPlotCanvas.undo();
        fButtonUndo.setEnabled(canUndo);
        fMenuItemUndo.setEnabled(canUndo);
        fMenuItemClear.setEnabled(canUndo);
        fButtonClear.setEnabled(canUndo);
    }

    void cancelLoading(){
        fLoadingInProgress = false;
        fPlotCanvas.cancelImageLoading();
        fButtonOpen.setIcon(fImageOpen);
        fButtonOpen.setToolTipText("Open image");
    }

    synchronized public void fButtonOpen_actionPerformed(ActionEvent e) {
        if(fLoadingInProgress){
            cancelLoading();
            return;
        }
        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("jpg");
        filter.addExtension("jpeg");
        filter.addExtension("gif");
        filter.addExtension("png");
        filter.setDescription("JPG, GIF & PNG Files");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try{
                fMenuItemClear_actionPerformed(null);//clear previous graph points
                File imgFile = chooser.getSelectedFile();
                fStatusBar.setText("Loading image from " + imgFile.getName() + "... Press STOP to cancel");
                fLoadingInProgress = true;
                fButtonOpen.setIcon(fImageStop);
                fButtonOpen.setToolTipText("Cancel loading");
                fPlotCanvas.loadImage(imgFile);
            }catch(Exception ex){
                fStatusBar.setText(ex.getMessage());
                cancelLoading();
            }
        }
    }
    
    static public Boolean ShowConfirmationDialog(String title, String text, Component parent)
    {
  	  return (JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION);
    }

    public void fButtonSave_actionPerformed(ActionEvent e) {
        if(fPlotCanvas.getGraph().size()==0){
            fStatusBar.setText("Nothing to save (no data points)");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File dataFile = chooser.getSelectedFile();
            if(dataFile.exists()){
                if(!ShowConfirmationDialog("Replace", "Replace existing "+dataFile.getName()+"?",this).booleanValue())
                    return;
            }
            try{
                saveData(dataFile);
                fStatusBar.setText("Data saved to "+dataFile.getAbsolutePath());
            }catch(Exception ex){
                fStatusBar.setText(ex.getMessage());
            }
        }
    }

    private void saveData(File aFile) throws Exception{

        PrintStream str = new  PrintStream(new FileOutputStream(aFile));//new PrintStream(aFile);
        Vector<?> data = fPlotCanvas.getGraph();
        for(int i = 0; i < data.size(); i++){
            GraphPoint p = (GraphPoint)data.elementAt(i);
            double x = graph2real(fPlotCanvas.getXRange(), fXrange, p.x, fCheckBoxLogscaleX.isSelected());
            double y = graph2real(fPlotCanvas.getYRange(), fYrange, p.y, jCheckBoxLogscaleY.isSelected());
            str.println(x+" "+y);
        }
        str.close();
    }

    public void fButtonHelp_actionPerformed(ActionEvent e) {
        fMenuHelpAbout_actionPerformed(e);
    }

    private JTextField fInvalidRangeTextField;
    JMenuItem fMenuFileOpen = new JMenuItem();
    JMenuItem fMenuFileSave = new JMenuItem();
    JMenu fMenuLoad = new JMenu();
    JMenuItem fMenuItemClipboard = new JMenuItem();
    JMenu fMenuEdit = new JMenu();
    JMenuItem fMenuItemUndo = new JMenuItem();
    JMenuItem fMenuItemClear = new JMenuItem();
    JButton fButtonClear = new JButton();
    boolean fLoadingInProgress = false;
    private final JPanel fCentralPanel = new JPanel();
    private final JPanel fPanelPlot = new JPanel();
    private final JTextPane fWelcomeTextPane = new JTextPane();
    private final JToggleButton fToggleButtonScale = new JToggleButton("Scale");
    private final JPanel fRangesPanel = new JPanel();
    private final Component fHbox9 = Box.createHorizontalStrut(20);
    private final JButton fBtnDone = new JButton("Done");

    private double handleRangeTextField_focusLost(JTextField aTextField, JCheckBox aLogscaleChkbox, double aPreviousValue, double aOtherLimit){
        if(fInvalidRangeTextField!=null && aTextField!=fInvalidRangeTextField)
            return aPreviousValue;//it is important to avoid the below GUI changes when focus is lost because other textfield has requested focus
        double result = aPreviousValue;
        try{
            result = Double.parseDouble(aTextField.getText());
            fInvalidRangeTextField = null;
            fButtonSave.setToolTipText("Save");
            fButtonSave.setEnabled(true);
            fMenuFileSave.setEnabled(true);
            fStatusBar.setForeground(Color.BLACK);
            fStatusBar.setText(" ");
            if (result <= 0) {
                aLogscaleChkbox.setSelected(false);
                aLogscaleChkbox.setEnabled(false);
            } else {
                if (aOtherLimit > 0)
                    aLogscaleChkbox.setEnabled(true);
            }

        }catch(NumberFormatException e){
            fInvalidRangeTextField = aTextField;
            aTextField.requestFocus();
            fStatusBar.setForeground(Color.RED);
            fStatusBar.setText("Number format error! Please change the value!");
            fButtonSave.setToolTipText("Fix settings first!");
            fButtonSave.setEnabled(false);
            fMenuFileSave.setEnabled(false);
            //aTextField.setText(Double.toString(aDefaultValue));
        }
        return result;
    }

    public void fTextFieldXmin_focusLost(FocusEvent e) {
        fXrange.min = handleRangeTextField_focusLost(fTextFieldXmin, fCheckBoxLogscaleX, fXrange.min, fXrange.max);
    }

    public void fTextFieldYmin_focusLost(FocusEvent e) {
        fYrange.min = handleRangeTextField_focusLost(fTextFieldYmin, jCheckBoxLogscaleY, fYrange.min, fYrange.max);
    }

    public void fTextFieldXmax_focusLost(FocusEvent e) {
        fXrange.max = handleRangeTextField_focusLost(fTextFieldXmax, fCheckBoxLogscaleX, fXrange.max, fXrange.min);
    }

    public void fTextFieldYmax_focusLost(FocusEvent e) {
        fYrange.max = handleRangeTextField_focusLost(fTextFieldYmax, jCheckBoxLogscaleY, fYrange.max, fYrange.min);
    }

    synchronized public void imageLoaded(Exception aException) {
        if(aException!=null){
            fStatusBar.setText("failed to load image");
        }else{
            fStatusBar.setText("image loaded");
        }
        fLoadingInProgress = false;
        fButtonOpen.setIcon(fImageOpen);
        fButtonOpen.setToolTipText("Open image");
        EnableWelcomeView(false);
    }

    public void fMenuItemClipboard_actionPerformed(ActionEvent e) {
        try{
            fMenuItemClear_actionPerformed(null);//clear previous graph points
            fStatusBar.setText("Loading image from clipboard... Press STOP to cancel");
            fLoadingInProgress = true;
            fButtonOpen.setIcon(fImageStop);
            fButtonOpen.setToolTipText("Cancel loading");
            fPlotCanvas.readImageFromClipboard();
        }catch(Exception ex){
            fStatusBar.setText(ex.getMessage());
            cancelLoading();
        }
    }

    public void fMenuItemUndo_actionPerformed(ActionEvent e) {
        fButtonUndo_actionPerformed(e);
    }

    public void fMenuItemClear_actionPerformed(ActionEvent e) {
        fPlotCanvas.clearGraph();
        fButtonUndo.setEnabled(false);
        fMenuItemUndo.setEnabled(false);
        fMenuItemClear.setEnabled(false);
        fButtonClear.setEnabled(false);
    }

    public void fButtonClear_actionPerformed(ActionEvent e) {
        fMenuItemClear_actionPerformed(e);
    }
}





class MainFrame_fButtonClear_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fButtonClear_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonClear_actionPerformed(e);
    }
}


class MainFrame_fMenuItemClear_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fMenuItemClear_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fMenuItemClear_actionPerformed(e);
    }
}


class MainFrame_fMenuItemUndo_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fMenuItemUndo_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fMenuItemUndo_actionPerformed(e);
    }
}


class MainFrame_fMenuItemClipboard_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fMenuItemClipboard_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fMenuItemClipboard_actionPerformed(e);
    }
}


class MainFrame_fMenuFileSave_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fMenuFileSave_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonSave_actionPerformed(e);
    }
}


class MainFrame_fMenuFileOpen_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fMenuFileOpen_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {

        adaptee.fButtonOpen_actionPerformed(e);
    }
}


class MainFrame_fTextFieldYmax_focusAdapter extends FocusAdapter {
    private MainFrame adaptee;
    MainFrame_fTextFieldYmax_focusAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void focusLost(FocusEvent e) {
        adaptee.fTextFieldYmax_focusLost(e);
    }
}


class MainFrame_fTextFieldXmax_focusAdapter extends FocusAdapter {
    private MainFrame adaptee;
    MainFrame_fTextFieldXmax_focusAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void focusLost(FocusEvent e) {
        adaptee.fTextFieldXmax_focusLost(e);
    }
}


class MainFrame_fTextFieldXmin_focusAdapter extends FocusAdapter {
    private MainFrame adaptee;
    MainFrame_fTextFieldXmin_focusAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void focusLost(FocusEvent e) {
        adaptee.fTextFieldXmin_focusLost(e);
    }
}


class MainFrame_fTextFieldYmin_focusAdapter extends FocusAdapter {
    private MainFrame adaptee;
    MainFrame_fTextFieldYmin_focusAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void focusLost(FocusEvent e) {
        adaptee.fTextFieldYmin_focusLost(e);
    }
}


class MainFrame_fButtonHelp_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fButtonHelp_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonHelp_actionPerformed(e);
    }
}


class MainFrame_fButtonSave_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fButtonSave_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonSave_actionPerformed(e);
    }
}


class MainFrame_fButtonOpen_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fButtonOpen_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonOpen_actionPerformed(e);
    }
}


class MainFrame_fButtonUndo_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fButtonUndo_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fButtonUndo_actionPerformed(e);
    }
}


class MainFrame_fToggleButtonPlot_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonPlot_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonPlot_actionPerformed(e);
    }
}


class MainFrame_fToggleButtonYmax_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonYmax_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonYmax_actionPerformed(e);
    }
}


class MainFrame_fToggleButtonYmin_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonYmin_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonYmin_actionPerformed(e);
    }
}

class MainFrame_fToggleButtonScale_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonScale_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonScale_actionPerformed(e);
    }
}


class MainFrame_fToggleButtonXmax_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonXmax_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonXmax_actionPerformed(e);
    }
}


class MainFrame_fToggleButtonXmin_actionAdapter implements ActionListener {
    private MainFrame adaptee;
    MainFrame_fToggleButtonXmin_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.fToggleButtonXmin_actionPerformed(e);
    }
}


class MainFrame_fMenuFileExit_ActionAdapter implements ActionListener {
    MainFrame adaptee;

    MainFrame_fMenuFileExit_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.fMenuFileExit_actionPerformed(actionEvent);
    }
}


class MainFrame_fMenuHelpAbout_ActionAdapter implements ActionListener {
    MainFrame adaptee;

    MainFrame_fMenuHelpAbout_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.fMenuHelpAbout_actionPerformed(actionEvent);
    }
}
