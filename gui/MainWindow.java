/*
   JavaBluetoothGateway
   Copyright (C) 2009:
         Clemens Lombriser and Daniel Roggen, Wearable Computing Laboratory, ETH Zurich

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

/*
 * MainWindow.java
 *
 * Created on 6. August 2009, 09:44
 */

package bluetoothgateway.gui;

import bluetoothgateway.bluetooth.BTDeviceInfo;
import bluetoothgateway.bluetooth.DeviceDiscoveryThread;
import bluetoothgateway.gui.SensorConfigFrame;
import java.util.Observable;
import java.util.Observer;

/**
 * The main window shows the devices that are currently connected and performs 
 * device queries. It is started from class bluetoothgateway.Main
 * 
 * @author  Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class MainWindow extends java.awt.Frame implements Observer {

   String [] m_formats;
   
    /** Creates new form MainWindow */
    public MainWindow() {
        super("BluetoothGateway");
        initComponents();
        pack(); //cl: this makes the window actually appear
    }

   public void addFormatStrings(String[] formats) {
      m_formats = formats;
   }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      lblRegistered = new java.awt.Label();
      lstConnectedDevices = new java.awt.List();
      btnDiscoverDevices = new java.awt.Button();
      lstDiscoveredDevices = new java.awt.List();
      btnConnect = new java.awt.Button();
      btnExit = new java.awt.Button();

      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowOpened(java.awt.event.WindowEvent evt) {
            formWindowOpened(evt);
         }
      });

      lblRegistered.setName("lblRegistered"); // NOI18N
      lblRegistered.setText("Connected devices");

      btnDiscoverDevices.setLabel("Discover");
      btnDiscoverDevices.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnDiscoverDevicesActionPerformed(evt);
         }
      });

      btnConnect.setLabel("Connect");
      btnConnect.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnConnectActionPerformed(evt);
         }
      });

      btnExit.setLabel("Exit");
      btnExit.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnExitActionPerformed(evt);
         }
      });

      org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
               .add(org.jdesktop.layout.GroupLayout.LEADING, lstConnectedDevices, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
               .add(org.jdesktop.layout.GroupLayout.LEADING, lstDiscoveredDevices, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
               .add(org.jdesktop.layout.GroupLayout.LEADING, lblRegistered, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
               .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                  .add(btnDiscoverDevices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                  .add(btnConnect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 60, Short.MAX_VALUE)
                  .add(btnExit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(lblRegistered, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(1, 1, 1)
            .add(lstConnectedDevices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
               .add(btnDiscoverDevices, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
               .add(btnConnect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
               .add(btnExit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(lstDiscoveredDevices, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
            .addContainerGap())
      );

      java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      setBounds((screenSize.width-267)/2, (screenSize.height-380)/2, 267, 380);
   }// </editor-fold>//GEN-END:initComponents

   /**
    * Connects to the device currently selected in the discovery panel
    * @param evt
    */
private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
   String [] selItems = lstDiscoveredDevices.getSelectedItems();
   
   for (int i=0; i<selItems.length; i++) {
      String address    = selItems[i].substring(0, selItems[i].indexOf(" ("));
      String sensorname = selItems[i].substring(selItems[i].indexOf("(")+1);
      sensorname = sensorname.substring(0,sensorname.indexOf(")"));
      
      SensorConfigFrame scf = new SensorConfigFrame(address, sensorname, m_formats);

      scf.setVisible(true);
      
      lstDiscoveredDevices.remove(selItems[i]);
      lstConnectedDevices.add(selItems[i]);
   }
   
   
}//GEN-LAST:event_btnConnectActionPerformed

/**
 * Notification that a BTConnection has been closed.
 * @param address
 */
public void closedBTConnection(String address) {
   for(int i=0; i<lstConnectedDevices.getItemCount(); i++ ) {
      if ( lstConnectedDevices.getItem(i).indexOf(address) != -1 ) {
         String devicename = lstConnectedDevices.getItem(i);
         addBTDevice(devicename);
         lstConnectedDevices.remove(i);
      }
   }
}


private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
   System.exit(0);
}//GEN-LAST:event_btnExitActionPerformed

private void btnDiscoverDevicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscoverDevicesActionPerformed

   Thread ddt = new Thread(new DeviceDiscoveryThread(this));
   ddt.start();
}//GEN-LAST:event_btnDiscoverDevicesActionPerformed

/**
 * Invoked the first time a window is made visible - fills the Bluetooth cached 
 * devices into the list.
 * @param evt
 */
private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
   DeviceDiscoveryThread ddt = new DeviceDiscoveryThread(this);
   ddt.reportKnownDevices();
}//GEN-LAST:event_formWindowOpened

/**
 * Receives notifications from the other components
 * @param obs  The sending object
 * @param arg  the (optional) argument
 */
public void update(Observable obs, Object arg) {
   if (obs instanceof DeviceDiscoveryThread) {
      if (arg instanceof BTDeviceInfo) {
         BTDeviceInfo btdi = (BTDeviceInfo)arg;
         addBTDevice(btdi.address +" (" + btdi.name + ")");
      }
   } else {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}

public void addBTDevice(String devicename) {
   
   devicename = devicename.trim();
   int separator = devicename.indexOf(" ");
   String address = (separator>0)? devicename.substring(0, separator) : devicename;
   
   // check whether it is available already
   for ( int i=0; i<lstDiscoveredDevices.getItemCount();i++) {
      if ( lstDiscoveredDevices.getItem(i).indexOf(address) == 0 ) {
         lstDiscoveredDevices.remove(i);
         lstDiscoveredDevices.addItem(devicename,i);
         return;
      }
   }
   
   // not found - add item at the end
   lstDiscoveredDevices.add(devicename);
}




   // Variables declaration - do not modify//GEN-BEGIN:variables
   private java.awt.Button btnConnect;
   private java.awt.Button btnDiscoverDevices;
   private java.awt.Button btnExit;
   private java.awt.Label lblRegistered;
   private java.awt.List lstConnectedDevices;
   private java.awt.List lstDiscoveredDevices;
   // End of variables declaration//GEN-END:variables

   /**
    * This function opens up this window to test the GUI.
    * 
    * @param args Program arguments
    */
   public static void main(String [] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
   }

}
