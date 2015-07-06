/*
 * DataViewerUtilities.java  2/6/13 1:04 PM
 *
 * Copyright (C) 2012-2013 Nick Ma
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.dv.util;


import com.dv.prop.DVPropMain;
import com.dv.ui.component.DVUpgradeInformation;
import com.dv.ui.component.ExceptionErrorDialog;
import com.dv.ui.plaf.DataViewerTheme;
import com.dv.upgrade.DVUpgradeUtil;

import java.lang.reflect.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @author Nick Ma
 */
public class DataViewerUtilities {

    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    private static final boolean isOS2 = System.getProperty("os.name").toLowerCase().indexOf("os/2") != -1;
    private static final boolean isMac = System.getProperty("os.name").toLowerCase().indexOf("mac") != -1;
    private static final boolean isLinux = System.getProperty("os.name").toLowerCase().indexOf("linux") != -1;
    private static final boolean isSunOS = System.getProperty("os.name").toLowerCase().indexOf("sunos") != -1;
    private static final boolean isAIX = System.getProperty("os.name").toLowerCase().indexOf("aix") != -1;
    private static final boolean isHPUX = System.getProperty("os.name").toLowerCase().indexOf("hpux") != -1;
    private static final boolean isFreeBSD = System.getProperty("os.name").toLowerCase().indexOf("freebsd") != -1;
    private static final boolean isHiresScreen = Toolkit.getDefaultToolkit().getScreenSize().width > 1280;
    private static Double javaVersion = null;
    private static final String ELLIPSIS = "...";
    private static final Properties props = getSystemProperty();
    private static final boolean isWindows7 = System.getProperty("os.name").toLowerCase().indexOf("windows 7") != -1;
    public static JFrame frame;

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Checks if the specified value is
     * <code>null</code>. This will also return
     * <code>true</code> if the length of the specified value is zero.
     *
     * @param value the value to check for <code>null</code>
     * @return <code>true</code> | <code>false</code>
     */
    public static boolean isNull(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    public static void checekUpdateFromFtp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (DVUpgradeUtil.init()) {
                    if (DVUpgradeUtil.needToUpgrade()) {
                        new DVUpgradeInformation(DVPropMain.DV_FRAME.get("MAIN"));

                    }
                }
            }
        });
    }

    public static double getJavaVersion() {
        if (javaVersion == null) {
            try {
                String ver = System.getProperties().getProperty("java.version");
                String version = "";
                boolean firstPoint = true;
                for (int i = 0; i < ver.length(); i++) {
                    if (ver.charAt(i) == '.') {
                        if (firstPoint) {
                            version += ver.charAt(i);
                        }
                        firstPoint = false;
                    } else if (Character.isDigit(ver.charAt(i))) {
                        version += ver.charAt(i);
                    }
                }
                javaVersion = new Double(version);
            } catch (Exception ex) {
                javaVersion = new Double(1.3);
            }
        }
        return javaVersion.doubleValue();
    }

    public static boolean isWindows() {
        return isWindows;
    }

    public static boolean isWindows7() {
        return isWindows7;
    }

    public static boolean isOS2() {
        return isOS2;
    }

    public static boolean isMac() {
        return isMac;
    }

    public static boolean isLinux() {
        return isLinux;
    }

    public static boolean isSunOS() {
        return isSunOS;
    }

    public static boolean isAIX() {
        return isAIX;
    }

    public static boolean isHPUX() {
        return isHPUX;
    }

    public static boolean isFreeBSD() {
        return isFreeBSD;
    }

    public static boolean isHiresScreen() {
        return isHiresScreen;
    }

    public static boolean isLeftToRight(Component c) {
        return c.getComponentOrientation().isLeftToRight();
    }

    public static boolean isActive(JComponent c) {
        if (c == null) {
            return false;
        }

        boolean active = true;
        if (c instanceof JInternalFrame) {
            active = ((JInternalFrame) c).isSelected();
        }
        if (active) {
            Container parent = c.getParent();
            while (parent != null) {
                if (parent instanceof JInternalFrame) {
                    active = ((JInternalFrame) parent).isSelected();
                    break;
                }
                parent = parent.getParent();
            }
        }
        if (active) {
            active = isFrameActive(c);
        }
        return active;
    }

    public static boolean isFrameActive(JComponent c) {
        if (c == null) {
            return false;
        }

        if (c.getTopLevelAncestor() instanceof Window) {
            return isWindowActive((Window) c.getTopLevelAncestor());
        }

        return true;
    }

    public static boolean isWindowActive(Window window) {
        if (getJavaVersion() >= 1.4) {
            try {
                Class paramTypes[] = null;
                Object args[] = null;
                Method m = window.getClass().getMethod("isActive", paramTypes);
                Boolean b = (Boolean) m.invoke(window, args);
                return b.booleanValue();
            } catch (Exception ex) {
            }
        }
        return true;
    }

    public static Container getRootContainer(Component c) {
        if (c != null) {
            Container parent = c.getParent();
            while ((parent != null) && !(parent instanceof JPopupMenu) && !(parent instanceof JInternalFrame) && !(parent instanceof Window) && (parent.getParent() != null)) {
                parent = parent.getParent();
            }
            return parent;
        }
        return null;
    }

    public static Dimension getFrameSize(Component c) {
        Container parent = getRootContainer(c);
        if (parent != null) {
            return parent.getSize();
        }
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Point getRelLocation(Component c) {
        if (c == null || !c.isShowing()) {
            return new Point(0, 0);
        }

        Container parent = getRootContainer(c);
        if ((parent != null) && parent.isShowing()) {
            Point p1 = c.getLocationOnScreen();
            Point p2 = parent.getLocationOnScreen();
            return new Point(p1.x - p2.x, p1.y - p2.y);
        }

        return new Point(0, 0);
    }

    public static String getClippedText(String text, FontMetrics fm, int maxWidth) {
        if ((text == null) || (text.length() == 0)) {
            return "";
        }
        int width = SwingUtilities.computeStringWidth(fm, text);
        if (width > maxWidth) {
            int totalWidth = SwingUtilities.computeStringWidth(fm, ELLIPSIS);
            for (int i = 0; i < text.length(); i++) {
                totalWidth += fm.charWidth(text.charAt(i));
                if (totalWidth > maxWidth) {
                    return text.substring(0, i) + ELLIPSIS;
                }
            }
        }
        return text;
    }

    public static int findDisplayedMnemonicIndex(String text, int mnemonic) {
        if (text == null || mnemonic == '\0') {
            return -1;
        }

        char uc = Character.toUpperCase((char) mnemonic);
        char lc = Character.toLowerCase((char) mnemonic);

        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);

        if (uci == -1) {
            return lci;
        } else if (lci == -1) {
            return uci;
        } else {
            return (lci < uci) ? lci : uci;
        }
    }

    public static void fillHorGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        int steps = colors.length;
        double dy = (double) h / (double) (steps);
        if (dy <= 3.001) {
            int y1 = y;
            for (int i = 0; i < steps; i++) {
                int y2 = y + (int) Math.round((double) i * dy);
                g.setColor(colors[i]);
                if (i == (steps - 1)) {
                    g.fillRect(x, y1, w, y + h - y1);
                } else {
                    g.fillRect(x, y1, w, y2 - y1);
                }
                y1 = y2;
            }
        } else {
            smoothFillHorGradient(g, colors, x, y, w, h);
        }
    }

    public static void smoothFillHorGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        Graphics2D g2D = (Graphics2D) g;
        int steps = colors.length;
        double dy = (double) h / (double) (steps - 1);
        int y1 = y;
        for (int i = 0; i < steps; i++) {
            int y2 = y + (int) Math.round((double) i * dy);
            if (i == (steps - 1)) {
                g2D.setPaint(null);
                g2D.setColor(colors[i]);
                g.fillRect(x, y1, w, y + h - y1);
            } else {
                g2D.setPaint(new GradientPaint(0, y1, colors[i], 0, y2, colors[i + 1]));
                g.fillRect(x, y1, w, y2 - y1);
            }
            y1 = y2;
        }
    }

    public static void fillInverseHorGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        int steps = colors.length;
        double dy = (double) h / (double) steps;
        if (dy <= 3.001) {
            int y1 = y;
            for (int i = 0; i < steps; i++) {
                int y2 = y + (int) Math.round((double) i * dy);
                g.setColor(colors[colors.length - i - 1]);
                if (i == (steps - 1)) {
                    g.fillRect(x, y1, w, y + h - y1);
                } else {
                    g.fillRect(x, y1, w, y2 - y1);
                }
                y1 = y2;
            }
        } else {
            smoothFillInverseHorGradient(g, colors, x, y, w, h);
        }

    }

    public static void smoothFillInverseHorGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        Graphics2D g2D = (Graphics2D) g;
        int steps = colors.length;
        double dy = (double) h / (double) steps;
        int y1 = y;
        for (int i = 0; i < steps; i++) {
            int y2 = y + (int) Math.round((double) i * dy);
            g.setColor(colors[colors.length - i - 1]);
            if (i == (steps - 1)) {
                g2D.setPaint(null);
                g2D.setColor(colors[colors.length - i - 1]);
                g.fillRect(x, y1, w, y + h - y1);
            } else {
                g2D.setPaint(new GradientPaint(0, y1, colors[colors.length - i - 1], 0, y2, colors[colors.length - i - 2]));
                g.fillRect(x, y1, w, y2 - y1);
            }
            y1 = y2;
        }
    }

    public static void fillVerGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        int steps = colors.length;
        double dx = (double) w / (double) steps;
        int x1 = x;
        for (int i = 0; i < steps; i++) {
            int x2 = x + (int) Math.round((double) i * dx);
            g.setColor(colors[i]);
            if (i == (steps - 1)) {
                g.fillRect(x1, y, x + w - x1, h);
            } else {
                g.fillRect(x1, y, x2 - x1, h);
            }
            x1 = x2;
        }
    }

    public static void fillInverseVerGradient(Graphics g, Color[] colors, int x, int y, int w, int h) {
        int steps = colors.length;
        double dx = (double) w / (double) steps;
        int x1 = x;
        for (int i = 0; i < steps; i++) {
            int x2 = x + (int) Math.round((double) i * dx);
            g.setColor(colors[colors.length - i - 1]);
            if (i == (steps - 1)) {
                g.fillRect(x1, y, x + w - x1, h);
            } else {
                g.fillRect(x1, y, x2 - x1, h);
            }
            x1 = x2;
        }
    }
    //-------------------------------------------------------------------------------------------

    public static void drawBorder(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        g.drawRect(x, y, w - 1, h - 1);
    }

    public static void draw3DBorder(Graphics g, Color c1, Color c2, int x, int y, int w, int h) {
        int x2 = x + w - 1;
        int y2 = y + h - 1;
        g.setColor(c1);
        g.drawLine(x, y, x2 - 1, y);
        g.drawLine(x, y + 1, x, y2);
        g.setColor(c2);
        g.drawLine(x + 1, y2, x2 - 1, y2);
        g.drawLine(x2, y, x2, y2);
    }

    public static void drawRoundBorder(Graphics g, Color c, int x, int y, int w, int h, int r) {
        Graphics2D g2D = (Graphics2D) g;
        Object savedRederingHint = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(c);
        g2D.drawRoundRect(x, y, w - 1, h - 1, r, r);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedRederingHint);
    }

    public static void drawRound3DBorder(Graphics g, Color c1, Color c2, int x, int y, int w, int h) {
        Graphics2D g2D = (Graphics2D) g;
        int x2 = x + w;
        int y2 = y + h;
        int d = h;
        int r = h / 2;
        Color cm = ColorHelper.median(c1, c2);
        Color c1m = ColorHelper.median(c1, cm);
        Color c2m = ColorHelper.median(c2, cm);

        Object savedRederingHint = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // oben
        g2D.setColor(c1);
        g2D.drawLine(x + r, y, x2 - r, y);
        // rechts
        g2D.drawLine(x, y + r, x, y2 - r);
        // unten
        g2D.setColor(c2);
        g2D.drawLine(x + r, y2, x2 - r, y2);
        // links
        g2D.drawLine(x2, y + r, x2, y2 - r);

        // links
        g2D.setColor(c1);
        g2D.drawArc(x, y, d, d, 90, 45);
        g2D.setColor(c1m);
        g2D.drawArc(x, y, d, d, 135, 45);
        g2D.setColor(cm);
        g2D.drawArc(x, y, d, d, 180, 45);
        g2D.setColor(c2m);
        g2D.drawArc(x, y, d, d, 225, 45);
        // rechts
        g2D.setColor(c1m);
        g2D.drawArc(x2 - d, y, d, d, 45, 45);
        g2D.setColor(cm);
        g2D.drawArc(x2 - d, y, d, d, 0, 45);
        g2D.setColor(c2m);
        g2D.drawArc(x2 - d, y, d, d, -45, 45);
        g2D.setColor(c2);
        g2D.drawArc(x2 - d, y, d, d, -90, 45);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedRederingHint);
    }

    public static void scheduleGC() {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                System.gc();
//            }
//        });
        System.gc();
    }

    public static String getDVName() {


        return props.getProperty("dataviewer_name");

    }

    public static String getDVVersion() {


        return props.getProperty("dataviewer_version");

    }

    public static String getDVPublishDate() {


        return props.getProperty("dataviewer_publish_date");

    }

    public static Properties getSystemProperty() {

        Properties props = new Properties();

        try {
            props = loadPropertiesResource("com/dv/ui/resources/DataViewerVersion.properties");
        } catch (Exception e) {
        }
        return props;
    }

    public static Properties loadPropertiesResource(String path) throws IOException {
        InputStream input = null;

        try {
            ClassLoader cl = DataViewerUtilities.class.getClassLoader();

            if (cl != null) {
                input = cl.getResourceAsStream(path);
            } else {
                input = ClassLoader.getSystemResourceAsStream(path);
            }

            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static String getLastOpenFilePath() {

        String path = new String();
        if (DVPropMain.DV_FILE_RECENT_PATH == null) {
            DVPropMain.DV_FILE_RECENT_PATH = DVPropMain.DV_SQL_FOLDER;
        }
        path = DVPropMain.DV_FILE_RECENT_PATH;

        return path;
    }

    public static String getExportFilePath() {

        return DVPropMain.DV_EXPORT_FOLDER;
    }

    /**
     * Convenience method for consistent border colour. Actually aims to return
     * the value from
     * <code>
     * UIManager.getColor("controlShadow")</code>.
     *
     * @return the system default border colour
     */
    public static Color getDefaultBorderColour() {
        return UIManager.getColor("controlShadow");
    }

    public static final void displayWarningMessage(String message) {
        displayWarningMessage((Component) DVPropMain.DV_FRAME.get("MAIN"), message);
    }

    public static final void displayWarningMessage(Component parent, Object message) {
        displayDialog(parent,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                false,
                "OptionPane.warningIcon",
                "Warning",
                message);
    }

    /**
     * The dialog return value - where applicable
     */
    private static Object dialogReturnValue;

    private static Object displayDialog(final Component parent,
                                        final int optionType,
                                        final int messageType,
                                        final boolean wantsInput,
                                        final String icon,
                                        final String title,
                                        final Object message) {

        dialogReturnValue = null;

        Runnable runnable = new Runnable() {
            public void run() {
                showNormalCursor(parent);
                JOptionPane pane = new JOptionPane(message, messageType,
                        optionType, UIManager.getIcon(icon));
                pane.setWantsInput(wantsInput);

                JDialog dialog = pane.createDialog(parent, title);

                if (message instanceof DialogMessageContent) {

                    ((DialogMessageContent) message).setDialog(dialog);
                }
                dialog.setLocation(getLocationForDialog(parent, dialog.getSize()));
                dialog.setVisible(true);
                dialog.dispose();

                if (wantsInput) {
                    dialogReturnValue = pane.getInputValue();
                } else {
                    dialogReturnValue = pane.getValue();
                }

            }
        };
        invokeAndWait(runnable);

        return dialogReturnValue;
    }

    /**
     * Sets the application cursor to the system normal cursor the specified
     * component.
     *
     * @param component - the component to set the cursor onto
     */
    public static void showNormalCursor(Component component) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), component);
    }

    /**
     * Sets the specified cursor on the primary frame.
     *
     * @param cursor to set
     */
    private static void setCursor(Cursor cursor, Component component) {
        if (component != null) {
            component.setCursor(cursor);
        }
    }

    public static final void displayErrorMessage(String message) {
        displayErrorMessage((Component) DVPropMain.DV_FRAME.get("MAIN"), message);
    }

    public static final void displayErrorMessage(Component parent, Object message) {
        displayDialog(parent,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                false,
                "OptionPane.errorIcon",
                "Error Message",
                message);
    }

    public static final int displayConfirmCancelDialog(String message) {
        return displayConfirmCancelDialog((Component) DVPropMain.DV_FRAME.get("MAIN"), message);
    }

    public static final int displayConfirmCancelDialog(Component parent, Object message) {
        return formatDialogReturnValue(displayDialog(parent,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                false,
                "OptionPane.questionIcon",
                "Confirmation",
                message));
    }

    private static int formatDialogReturnValue(Object returnValue) {

        if (returnValue instanceof Integer) {

            return ((Integer) returnValue).intValue();
        }

        return -1;
    }

    /**
     * Executes requestFocusInWindow on the specified component using
     * invokeLater.
     *
     * @param c - the component
     */
    public static void requestFocusInWindow(final Component c) {
        invokeAndWait(new Runnable() {
            public void run() {
                c.requestFocusInWindow();
            }
        });
    }

    /**
     * Runs the specified runnable in the EDT using
     * <code>SwingUtilities.invokeAndWait(...)</code>. Note: This method
     * 'supresses' the method's thrown exceptions - InvocationTargetException
     * and InterruptedException.
     *
     * @param runnable - the runnable to be executed
     */
    public static void invokeAndWait(Runnable runnable) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            runnable.run();
        }
    }

    /**
     * Returns the specified component's visible bounds within the screen.
     *
     * @return the component's visible bounds as a <code>Rectangle</code>
     */
    public static Rectangle getVisibleBoundsOnScreen(JComponent component) {
        Rectangle visibleRect = component.getVisibleRect();
        Point onScreen = visibleRect.getLocation();
        SwingUtilities.convertPointToScreen(onScreen, component);
        visibleRect.setLocation(onScreen);
        return visibleRect;
    }

    /**
     * Returns the system font names within a collection.
     *
     * @return the system fonts names within a <code>Vector</code> object
     */
    public static Vector<String> getSystemFonts() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] tempFonts = gEnv.getAllFonts();

        char dot = '.';
        int dotIndex = 0;

        char[] fontNameChars = null;
        String fontName = null;
        Vector<String> fontNames = new Vector<String>();

        for (int i = 0; i < tempFonts.length; i++) {

            fontName = tempFonts[i].getFontName();
            dotIndex = fontName.indexOf(dot);

            if (dotIndex == -1) {
                fontNames.add(fontName);
            } else {
                fontNameChars = fontName.substring(0, dotIndex).toCharArray();
                fontNameChars[0] = Character.toUpperCase(fontNameChars[0]);

                fontName = new String(fontNameChars);

                if (!fontNames.contains(fontName)) {
                    fontNames.add(fontName);
                }

            }

        }

        Collections.sort(fontNames);
        return fontNames;
    }

    /**
     * Calculates and returns the centered position of a dialog with the
     * specified size to be added to the desktop area.
     *
     * @param component to center to
     * @param dialogDim of the dialog to be added as a <code>Dimension</code>
     *                  object
     * @return the <code>Point</code> at which to add the dialog
     */
    public static Point getLocationForDialog(Component component, Dimension dialogDim) {
        if (component == null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (dialogDim.height > screenSize.height) {
                dialogDim.height = screenSize.height;
            }
            if (dialogDim.width > screenSize.width) {
                dialogDim.width = screenSize.width;
            }
            return new Point((screenSize.width - dialogDim.width) / 2,
                    (screenSize.height - dialogDim.height) / 2);
        }

        //Rectangle dRec = getVisibleBoundsOnScreen(desktop.getDesktopPane());
        Dimension frameDim = component.getSize();
        Rectangle dRec = new Rectangle(component.getX(),
                component.getY(),
                (int) frameDim.getWidth(),
                (int) frameDim.getHeight());

        int dialogX = dRec.x + ((dRec.width - dialogDim.width) / 2);
        int dialogY = dRec.y + ((dRec.height - dialogDim.height) / 2);

        if (dialogX < 0 || dialogY < 0) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            if (dialogDim.height > screenSize.height) {
                dialogDim.height = screenSize.height;
            }

            if (dialogDim.width > screenSize.width) {
                dialogDim.width = screenSize.width;
            }

            dialogX = (screenSize.width - dialogDim.width) / 2;
            dialogY = (screenSize.height - dialogDim.height) / 2;
        }

        return new Point(dialogX, dialogY);
    }

    /**
     * <p>Calculates and returns the centered position of a dialog with the
     * specified size to be added to the desktop area - ie. taking into account
     * the size and location of all docked panels.
     *
     * @param dialogDim size of the dialog to be added as a <code>Dimension</code>
     *                  object
     * @return the <code>Point</code> at which to add the dialog
     */
    public static Point getLocationForDialog(Dimension dialogDim) {
        return getLocationForDialog((Component) frame, dialogDim);
    }

    static URLClassLoader urlLoader;

    public static Class launchLib(String fullPath, String classFullpath) {

        try {
            URL url = new URL(fullPath);
            URL[] urlA = new URL[]{url};
            urlLoader = new URLClassLoader(urlA);
            return urlLoader.loadClass(classFullpath);
        } catch (Exception e) {
            return null;
        }
    }

    public void FitTableColumns(JTable myTable) {

        JTableHeader header = myTable.getTableHeader();

        int rowCount = myTable.getRowCount();

        Enumeration columns = myTable.getColumnModel().getColumns();

        while (columns.hasMoreElements()) {

            TableColumn column = (TableColumn) columns.nextElement();

            int col = header.getColumnModel().getColumnIndex(
                    column.getIdentifier());

            int width = (int) myTable.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(myTable,
                    column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();

            for (int row = 0; row < rowCount; row++) {

                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false,
                        row, col).getPreferredSize().getWidth();

                width = Math.max(width, preferedWidth);

            }
            header.setResizingColumn(column);
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }

    public static void launchPluginLookAndFeel(String jarName, String className) {
        final String jar = jarName;
        final String classN = className;
        try {
            String full = DataViewerUtilities.class.getProtectionDomain().getCodeSource().getLocation().toString();
            full = full.substring(0, full.lastIndexOf("/") + 1);
            URL url = new URL(full + jar);
            URL[] urlA = new URL[]{url};
            DynamicLibraryLoader urlLoader = new DynamicLibraryLoader(urlA);
            Class c = urlLoader.loadClass(classN);
            LookAndFeel laf = (LookAndFeel) c.newInstance();
            UIManager.LookAndFeelInfo info = new UIManager.LookAndFeelInfo(laf.getName(), c.getName());
            if (classN != null && !classN.equals("")) {
                UIManager.installLookAndFeel(info);
                UIManager.setLookAndFeel(laf);
                UIManager.getLookAndFeelDefaults().put("ClassLoader", urlLoader);
            } else {
                setDefaultLookAndFeel(className);
            }
        } catch (Exception e) {
            setDefaultLookAndFeel(className);
        }
    }

    private static void setDefaultLookAndFeel(String className) {
        try {
            if (!className.trim().toUpperCase().equals("METAL")) {
                MetalLookAndFeel.setCurrentTheme(new DataViewerTheme());

                UIManager.setLookAndFeel(new MetalLookAndFeel());
                
            } else {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            }

        } catch (Exception eee) {
            eee.printStackTrace();
            DVLOG.setErrorLog(DataViewerUtilities.class.getName(), eee);
        }
    }

    public static java.sql.Driver launchPluginDataBaseDrivers(String dbType) {
        try {
            String full = DataViewerUtilities.class.getProtectionDomain().getCodeSource().getLocation().toString();
            full = full.substring(0, full.lastIndexOf("/") + 1);
            URL[] urlA;
            URL url = new URL(full + DVPropMain.getDBDriverJar(dbType));
            urlA = new URL[]{url};
            DynamicLibraryLoader urlLoader = new DynamicLibraryLoader(urlA);
            Class c = urlLoader.loadClass(DVPropMain.getDBDriver(dbType));
            return (java.sql.Driver) c.newInstance();
        } catch (Exception e) {
            DVLOG.setErrorLog(DataViewerUtilities.class.getName(), e);
        }
        return null;
    }

    public static void displayErrorMessageForConnection() {
        displayErrorMessageForException(DVPropMain.DATAVIEWER_EXCEPTION.get("ERROR"));
    }

    public static void displayErrorMessageForException(Throwable e) {
        ExceptionErrorDialog errorDialog = new ExceptionErrorDialog("Exception Cause By \n" + e.getMessage(), null, e);

    }
}
