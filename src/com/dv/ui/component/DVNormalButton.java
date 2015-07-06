package com.dv.ui.component;

import com.jidesoft.swing.JideButton;

/**
 * Created with IntelliJ IDEA.
 * User: Cypress
 * Date: 11/15/2014
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class DVNormalButton extends JideButton {

    public DVNormalButton(){
        super();
        this.setButtonStyle(JideButton.TOOLBAR_STYLE);

    }

    public DVNormalButton(String text){
        super(text);
        this.setButtonStyle(JideButton.TOOLBAR_STYLE);
    }


}
