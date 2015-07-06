package com.dv.ui.frequent;

import com.dv.dbinstance.DVServerInstance;
import com.dv.prop.DVPropMain;
import com.dv.ui.DVFrame;
import com.dv.ui.DVSearchFieldView;
import com.dv.ui.DataViewerRegister;
import com.dv.ui.action.DVRecentRecordAction;
import com.jidesoft.swing.JideBorderLayout;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Cypress
 * Date: 11/15/2014
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataViewerNewFrequentView extends JPanel implements MouseListener, ActionListener {

    DVFrame parent;

    ResourceBundle msg = ResourceBundle.getBundle("com.dv.about.AboutDialog");

    JLabel titleLabel = new JLabel(new ImageIcon(DataViewerNewFrequentView.class.getResource(msg.getString("mainIcon"))));

    String serverName = null;
    String serverHost = null;
    String serverSID = null;
    String serverSchema = null;
    String serverLastTime = null;
    String countTimes = null;
    DVServerInstance dvsi;
    DVRecentRecordAction aviaction;
    HashMap<Integer, JButton> RECENT_BUTTON = new HashMap<Integer, JButton>();

    public DVSearchFieldView getViewSearch() {
        return viewSearch;
    }

    DVSearchFieldView viewSearch;

    int selectInstanceIndex = 0;

    JPopupMenu instancePopupMenu;

    JMenuItem loadInstanceItem, removeInstanceItem, editDataBaseInstance;



    public DataViewerNewFrequentView(DVFrame parent){
        this.parent=parent;
        initComponents();

        this.add(frequentPanel);
        buildButtonGroup();
        buildPopupMenu();
        buildMouseActions();
        this.setOpaque(true);
        this.setBackground(Color.white);
    }

    public void buildMouseActions() {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.black, 1));
            RECENT_BUTTON.get(i + 1).addMouseListener(this);
        }
    }

    public void buildButtonGroup() {
        RECENT_BUTTON.put(1, display1);
        RECENT_BUTTON.put(2, display2);
        RECENT_BUTTON.put(3, display3);
        RECENT_BUTTON.put(4, display4);
        RECENT_BUTTON.put(5, display5);
        RECENT_BUTTON.put(6, display6);
        RECENT_BUTTON.put(7, display7);
        RECENT_BUTTON.put(8, display8);
    }

    public HashMap<Integer, JButton> getRECENT_BUTTON() {
        return RECENT_BUTTON;
    }

    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                setDisplayActions(i + 1, e);
                break;
            }
        }
    }

    public String getDvrecentInstanceName(int index, HashMap<String, String> detail) {
        String propvValue;
        try {
            propvValue = detail.get(String.valueOf(index));
        } catch (Exception e) {
            return null;
        }
        return propvValue;
    }

    public void setDisplayActions(int index, MouseEvent e) {
        if (e.getClickCount() == 2) {
            loadInstance(index);
        }
    }

    public void loadInstance(int index) {
        if (getDvrecentInstanceName(index, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
            aviaction = new DVRecentRecordAction((DVFrame) DVPropMain.DV_FRAME.get("MAIN"), DVPropMain.DV_RECENT_INSTANCE_NAME.get(String.valueOf(index)), 1);
            aviaction.setRecentDataBaseAcess();
        }
    }

    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                break;
            }
        }
    }

    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.black, 1));
                break;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (int i = 0; i < RECENT_BUTTON.size(); i++) {
                if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                    showPopup(i + 1, e);
                    break;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void showPopup(int index, MouseEvent e) {
        if (!instancePopupMenu.isVisible()) {
            if (getDvrecentInstanceName(index, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
                selectInstanceIndex = index;
                instancePopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }


    public void buildPopupMenu() {

        instancePopupMenu = new JPopupMenu();
        loadInstanceItem = new JMenuItem("Load Database");
        removeInstanceItem = new JMenuItem("Remove Database ");
        editDataBaseInstance = new JMenuItem("Change Database Setting");

        editDataBaseInstance.addActionListener(this);

        loadInstanceItem.addActionListener(this);
        removeInstanceItem.addActionListener(this);

        instancePopupMenu.add(loadInstanceItem);
        instancePopupMenu.add(removeInstanceItem);
        instancePopupMenu.addSeparator();
        instancePopupMenu.add(editDataBaseInstance);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeInstanceItem) {
            if (getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
                DVRecentRecordProcess.removeRecentServerRecord(getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME));
                this.setRecentRecords();
            }
        }

        if (e.getSource() == loadInstanceItem) {
            loadInstance(selectInstanceIndex);
        }

        if (e.getSource() == editDataBaseInstance) {
            invokeDataViewerRegister();
        }
    }

    public void invokeDataViewerRegister() {
        String instanceNme = getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME);
        if (instanceNme!= null) {
            DataViewerRegister register=new DataViewerRegister((DVFrame) (DVPropMain.DV_FRAME.get("MAIN")), true,instanceNme);
        }
    }

    public void setRecentRecords() {
        removeAllDispalys();
        int size = 0;
        if (DVPropMain.DV_RECENT_INSTANCE_NAME.size() > 8) {
            size = 8;
        } else {
            size = DVPropMain.DV_RECENT_INSTANCE_NAME.size();
        }
        if (size > 0) {
            for (int i = 1; i < size + 1; i++) {
                setDisplay(i);
            }
        }
    }

    public void removeAllDispalys() {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            RECENT_BUTTON.get((i + 1)).setText("");
        }
    }

    public void setDisplay(int index) {
        setDisplayDetail(index);
    }

    public void setDisplayDetail(int index) {
        serverName = DVPropMain.DV_RECENT_INSTANCE_NAME.get(String.valueOf(index));
        dvsi = DVPropMain.DV_SERVER_INSTANCE.get(serverName);
        serverHost = dvsi.getHost();
        serverSID = dvsi.getSid();
        serverSchema = dvsi.getSchama();
        serverLastTime = DVPropMain.DV_RECENT_INSTANCE_LASTTIME.get(serverName);
        countTimes = DVPropMain.DV_RECENT_INSTANCE_COUNT.get(serverName);
        String infor = "<html><p>Server Name : <font color=red face=\"DialogInput\" >" + serverName + "</font></p>"
                + "<p>Host  : <font color=blue face=\"DialogInput\" >" + serverHost + "</font></p>"
                + "<p>SID  : <font color=blue face=\"DialogInput\" >" + serverSID + "</font></p>"
                + "<p>Schema : <font color=blue face=\"DialogInput\" >" + serverSchema + "</font></p>"
                + "<p>Total Time(s) : <font color=red face=\"DialogInput\" >" + countTimes + "</font></p>"
                + "<p>Last : <font color=blue face=\"DialogInput\" >" + serverLastTime + "</font></p></html>";
        RECENT_BUTTON.get(index).setText(infor);
    }


    public void initComponents(){

        frequentPanel=new JPanel();

        JideBoxLayout layoutMain=new JideBoxLayout(frequentPanel,1,40);

        frequentPanel.setLayout(layoutMain);


        JPanel buttonPanels=new JPanel();

        JideBoxLayout layout=new JideBoxLayout(buttonPanels,1,20);
        buttonPanels.setLayout(layout);

        display1 = new JButton();
        display2 = new JButton();
        display3 = new JButton();
        display4 = new JButton();
        display5 = new JButton();
        display6 = new JButton();
        display7 = new JButton();
        display8 = new JButton();

        JPanel lineOne=new JPanel();

        JideBoxLayout layout1=new JideBoxLayout(lineOne,0,16);
        lineOne.setLayout(layout1);

        lineOne.setOpaque(false);

        display1.setPreferredSize(new Dimension(239, 181));
        display1.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineOne.add(display1, JideBoxLayout.FIX);

        display2.setPreferredSize(new Dimension(239, 181));
        display2.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineOne.add(display2, JideBoxLayout.FIX);

        display3.setPreferredSize(new Dimension(239, 181));
        display3.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineOne.add(display3, JideBoxLayout.FIX);

        display4.setPreferredSize(new Dimension(239, 181));
        display4.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineOne.add(display4, JideBoxLayout.FIX);

        JPanel lineTwo=new JPanel();

        lineTwo.setOpaque(false);

        JideBoxLayout layout2=new JideBoxLayout(lineTwo,0,16);
        lineTwo.setLayout(layout2);

        display5.setPreferredSize(new Dimension(239, 181));
        display5.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineTwo.add(display5, JideBoxLayout.FIX);

        display6.setPreferredSize(new Dimension(239, 181));
        display6.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineTwo.add(display6, JideBoxLayout.FIX);

        display7.setPreferredSize(new Dimension(239, 181));
        display7.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineTwo.add(display7, JideBoxLayout.FIX);

        display8.setPreferredSize(new Dimension(239, 181));
        display8.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        lineTwo.add(display8, JideBoxLayout.FIX);


        lineOne.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        buttonPanels.add(lineOne, JideBoxLayout.FIX);

        lineTwo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        buttonPanels.add(lineTwo, JideBoxLayout.FIX);

        buttonPanels.setOpaque(false);


        JPanel searchmainPanel=new JPanel();



        JideBoxLayout layout6=new JideBoxLayout(searchmainPanel,1,26);

        searchmainPanel.setLayout(layout6);

        JPanel searchPanel=new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setOpaque(false);
//
//        JideBorderLayout blayout=new JideBorderLayout(30,10);
//
//        searchPanel.setLayout(blayout);

        viewSearch=new DVSearchFieldView(parent);

        viewSearch.setPreferredSize(new Dimension(650,30));

//        searchPanel.add(viewSearch,JideBorderLayout.BEFORE_FIRST_LINE);

        searchPanel.add(viewSearch);

        searchmainPanel.add(titleLabel);

        searchmainPanel.add(searchPanel);

        searchmainPanel.setOpaque(false);

        JLabel blankLabel=new JLabel(" ");
//        JLabel blankLabel1=new JLabel(" ");

        frequentPanel.add(blankLabel);
//        frequentPanel.add(blankLabel1);
        frequentPanel.add(searchmainPanel);
        frequentPanel.add(buttonPanels);
        frequentPanel.setOpaque(false);

    }

    private JPanel frequentPanel;
    private JButton display1;
    private JButton display2;
    private JButton display3;
    private JButton display4;
    private JButton display5;
    private JButton display6;
    private JButton display7;
    private JButton display8;

}
