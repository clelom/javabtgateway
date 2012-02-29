/*
   JavaBluetoothGateway
   Copyright (C) 2009:
         Clemens Lombriser and Daniel Roggen, Wearable Computing Laboratory, ETH Zurich

	All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

   1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
   2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDERS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluetoothgateway;

import bluetoothgateway.bluetooth.BTConnection;
import bluetoothgateway.gui.MainWindow;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * The Main class reads in command line arguments and binds together the 
 * different components to run the BluetoothGateway
 *
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class Main {

   private static HashMap m_BTConnections = new HashMap();
   private static MainWindow m_mainWindow;
   private static ConfigFileReader m_config;
   
   /**
    * Initializes a Bluetooth connection to the given address and starts a 
    * server.
    * @param address the Bluetooth MAC address of the device
    */
   public static void addBTConnection(String address, String sensorname, String format) {
      
      FrameParser fp = new FrameParser(sensorname,format,((m_config!=null)?m_config.getDescription(format):null) );
      TCPServer tcpserver = new TCPServer();
      fp.addObserver(tcpserver);
      fp.addObserver(new FileDumper());
      BTConnection btc = new BTConnection(address,fp );
      
      new Thread(tcpserver).start();
      new Thread(btc).start();
      
      m_BTConnections.put(btc.getAddress(), btc);
   }

   /**
    * Notifies the control that a bluetooth connection has been terminated
    * @param btc the bluetooth connection that has terminated itself
    */
   public static void removeBTConnection(BTConnection btc) {
      m_BTConnections.remove(btc.getAddress());
      m_mainWindow.closedBTConnection(btc.getAddress());
   }

   /**
    * Starts up the main window.
    * @param args
    */
   public static void main(String [] args) {

      try {
         System.out.println("Opening config file");
         m_config = new ConfigFileReader("btg_config.txt");
      } catch (FileNotFoundException ex) {
         System.out.println("Main: could not find config file, using default values");
         if (m_config == null) m_config = new ConfigFileReader();
         m_config.addBTDevice("1000E85CDC3F (NT motion sensor 119)");
         m_config.addBTDevice("1000E89D082E (NT motion sensor 120)");
         m_config.addFilter("DX4;cs-s-s-s-s-s-s-s-s-s-s-s");
      } catch (IOException ex) {
         System.out.println("Main: read error in config file: " + ex.getLocalizedMessage());
      }
      
      // only open GUI if it is not turned off
      if (m_config.isOpenGUI()) {
         m_mainWindow = new MainWindow();
         m_config.configureMainWindow(m_mainWindow);
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { m_mainWindow.setVisible(true); }
         });
      }
   }

}
