/*
 * SimpleHtmlContentPane.java
 *
 * Copyright (C) 2002-2013 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery.components;

import org.executequery.util.SystemWebBrowserLauncher;
import org.underworldlabs.swing.DialogMessageContent;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;

public class SimpleHtmlContentPane extends JEditorPane implements DialogMessageContent {

  private Dialog dialog;

  public SimpleHtmlContentPane(String message) {

    super("text/html", message.replaceAll("\n", "<br>"));

    setEditable(false);
    setOpaque(false);

    setSelectionColor(UIManager.getColor("OptionPane.background"));

    Font font = UIManager.getFont("OptionPane.font");
    String bodyRule = "body { font-family: " + font.getFamily() + "; " +
        "font-size: " + (font.getSize()) + "pt; }";

    ((HTMLDocument) getDocument()).getStyleSheet().addRule(bodyRule);

    addHyperlinkListener(new OpensLinkInBrowserListener());
  }

  public void setDialog(Dialog dialog) {
    this.dialog = dialog;
  }

  class OpensLinkInBrowserListener implements HyperlinkListener {

    public void hyperlinkUpdate(HyperlinkEvent e) {

      if (isActiveEvent(e)) {

        try {

          new SystemWebBrowserLauncher().launch(e.getDescription());

        } finally {

          dialog.dispose();
        }

      }

    }

    private boolean isActiveEvent(HyperlinkEvent e) {

      return HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType());
    }

  }


}




