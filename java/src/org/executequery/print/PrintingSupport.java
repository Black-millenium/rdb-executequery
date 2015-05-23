/*
 * PrintingSupport.java
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

package org.executequery.print;

import org.executequery.GUIUtilities;
import org.executequery.log.Log;
import org.executequery.util.UserSettingsProperties;
import org.underworldlabs.util.FileUtils;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.*;
import javax.swing.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;

/**
 * Utility class aiding printing.
 *
 * @author Takis Diakoumis
 * @version $Revision: 160 $
 * @date $Date: 2013-02-08 17:15:04 +0400 (Пт, 08 фев 2013) $
 */
public class PrintingSupport {

  public PageFormat pageSetup() {

    PrinterJob job = getPrintJob("PageSetupOnly");

    PrintRequestAttributeSet attributeSet = loadAttributeSet();

    PageFormat pageFormat = job.pageDialog(attributeSet);

    if (pageFormat != null) {

      saveAttributeSet(attributeSet);
    }

    GUIUtilities.scheduleGC();

    return pageFormat;
  }

  public String print(Printable printable, String jobName) {

    return print(printable, jobName, false);
  }

  public String print(Printable printable, String jobName, boolean pageable) {

    PrinterJob job = getPrintJob(jobName);

    if (pageable) {

      PrintRequestAttributeSet attributeSet = loadAttributeSet();

      Book book = new Book();
      book.append(printable, pageFormatFromAttributeSet(attributeSet));

      job.setPageable(book);

    } else {

      job.setPrintable(printable);
    }

    return print(job);
  }

  public PageFormat getPageFormat() {

    return pageFormatFromAttributeSet(loadAttributeSet());
  }

  private String print(PrinterJob job) {

    PrintRequestAttributeSet attributeSet = loadAttributeSet();

    if (!job.printDialog(attributeSet)) {

      return "cancelled";
    }

    try {

      job.print(attributeSet);

      saveAttributeSet(attributeSet);

      return "Done";

    } catch (PrinterException exception) {

      String message = exception.getMessage();
      Log.error("Printing error: " + message);

      int option = GUIUtilities.displayConfirmCancelErrorMessage(
          "Print error:\n" + message);

      if (option == JOptionPane.CANCEL_OPTION) {

        return "Failed";

      } else {

        return print(job);
      }

    } finally {

      GUIUtilities.scheduleGC();
    }

  }

  private PrinterJob getPrintJob(String jobName) {

    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName(jobName);

    return job;
  }

  private PageFormat pageFormatFromAttributeSet(PrintRequestAttributeSet attributeSet) {

    // convert from PrintRequestAttributeSet to the pageFormat

    PrinterJob printJob = getPrintJob(" ");
    PageFormat pageFormat = printJob.defaultPage();
    Paper paper = pageFormat.getPaper();

    MediaSizeName mediaSizeName = (MediaSizeName) attributeSet.get(Media.class);
    MediaSize mediaSize = MediaSize.getMediaSizeForName(mediaSizeName);

    MediaPrintableArea mediaArea = (MediaPrintableArea) attributeSet.get(MediaPrintableArea.class);

    if (mediaArea != null) {

      paper.setImageableArea(
          (double) (mediaArea.getX(MediaPrintableArea.INCH) * 72),
          (double) (mediaArea.getY(MediaPrintableArea.INCH) * 72),
          (double) (mediaArea.getWidth(MediaPrintableArea.INCH) * 72),
          (double) (mediaArea.getHeight(MediaPrintableArea.INCH) * 72));
    }

    if (mediaSize != null) {

      paper.setSize(
          (double) (mediaSize.getX(MediaSize.INCH) * 72),
          (double) (mediaSize.getY(MediaSize.INCH) * 72));
    }

    pageFormat.setPaper(paper);

    OrientationRequested orientation = (OrientationRequested) attributeSet.get(
        OrientationRequested.class);

    if (orientation != null) {

      if (orientation.getValue() == OrientationRequested.LANDSCAPE.getValue()) {

        pageFormat.setOrientation(PageFormat.LANDSCAPE);

      } else if (orientation.getValue() == OrientationRequested.REVERSE_LANDSCAPE.getValue()) {

        pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);

      } else if (orientation.getValue() == OrientationRequested.PORTRAIT.getValue()) {

        pageFormat.setOrientation(PageFormat.PORTRAIT);

      } else if (orientation.getValue() == OrientationRequested.REVERSE_PORTRAIT.getValue()) {

        //doesnt exist??
        //pf.setOrientation(PageFormat.REVERSE_PORTRAIT);
        //then just do the next best thing

        pageFormat.setOrientation(PageFormat.PORTRAIT);
      }

    }

    return pageFormat;
  }

  private String printSetupFile() {

    UserSettingsProperties settings = new UserSettingsProperties();

    return settings.getUserSettingsDirectory() + "print.setup";
  }

  private PrintRequestAttributeSet loadAttributeSet() {

    try {

      File file = new File(printSetupFile());

      if (file.exists()) {

        PrintRequestAttributeSet attributeSet =
            (PrintRequestAttributeSet) FileUtils.readObject(file);

        return setDefaultAttributes(attributeSet);
      }

    } catch (IOException e) {

      Log.error("Error loading saved printer setup: " + e.getMessage());

    } finally {

      GUIUtilities.scheduleGC();
    }

    return setDefaultAttributes(new HashPrintRequestAttributeSet());
  }

  private PrintRequestAttributeSet setDefaultAttributes(PrintRequestAttributeSet attributeSet) {

    attributeSet.add(Chromaticity.MONOCHROME);
    attributeSet.add(new PrinterResolution(600, 600, ResolutionSyntax.DPI));

    return attributeSet;
  }

  private void saveAttributeSet(PrintRequestAttributeSet attributeSet) {

    try {

      FileUtils.writeObject(attributeSet, printSetupFile());

    } catch (IOException e) {

      e.printStackTrace();

    } finally {

      GUIUtilities.scheduleGC();
    }

  }

}









