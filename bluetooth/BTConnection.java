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

package bluetoothgateway.bluetooth;

// Main is used for notification of shutdown
import bluetoothgateway.Main;

import bluetoothgateway.StreamDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Handles communication directly with a Bluetooth device.
 * 
 * @author Tonio Gsell <tgsell@ee.ethz.ch>
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class BTConnection implements Runnable {
   
   private String m_btConnectString;
   private String m_address;
   
   private StreamConnection m_StrmConn;
   private OutputStream m_Output;
   private InputStream m_Input;
   private boolean m_IsConnected;
   private boolean m_bStop;
   private StreamDecoder m_sd;
   
   private int BUFFER_SIZE = 1;
   
   /**
    * Initializes the connection. Use start() to start receiving data
    * @param address MAC address of the Bluetooth device to connect to
    */
   public BTConnection(String address, StreamDecoder sd) {
      m_address = address;
      m_btConnectString = "btspp://"+address+":1;authenticate=true;encrypt=true;master=false";
      m_sd = sd;
   }
   
   /**
    * Replaces the StreamDecoder used
    * @param sd The new stream decoder. It receives the next bytes read
    */
   public void changeDecoder( StreamDecoder sd ) {
      m_sd = sd;
   }
   
   /**
    * Returns the remote bluetooth address of the BTConnection
    * @return
    */
   public String getAddress() {
      return m_address;
   }
   
   /**
    * Stops receiving data and shuts down the connection gracefully
    */
   public void stop() {
      m_bStop = true;
   }
   
   /**
    * Returns whether the connection is still alive
    * @return true if connection is still alive
    */
   public boolean IsConnected() {
      return m_IsConnected;
   }

   /**
    * Worker thread - reads bytes from the connection and forwards them to the 
    * FrameDecoder defined. This function will automatically shut down the 
    * connection if errors are detected.
    */
   public void run() {
      m_IsConnected = false; 
      m_bStop = false;
      
      // set up the streams
      try {
         m_StrmConn = (StreamConnection) Connector.open(m_btConnectString);
         m_Output = m_StrmConn.openOutputStream();
         m_Input = m_StrmConn.openInputStream();
      } catch (IOException ex) {
         System.out.println("BTConnection: could not connect to: '"+m_btConnectString+"': " + ex.getLocalizedMessage());
         return;
      }

      m_IsConnected = true;
      System.out.println("BTConnection: successfully connected to '" + m_btConnectString+"'");
      
      try {
         while(m_bStop==false) {
            
            //TODO: choose a good buffer size
            byte [] bytes = new byte[BUFFER_SIZE];
            if ( m_Input.read(bytes) == BUFFER_SIZE )  {
               m_sd.read(bytes);
            } else {
               System.out.println("BTConnection to "+ m_address +": could not read full buffer from input stream, closing.");
               break;
            }
            
         }
      } catch (IOException ex) {
         System.out.println("BTConnection: could not read from input stream, closing: "+ex.getLocalizedMessage());
      }
      
      m_IsConnected = false;
      try {
         m_Output.close();
         m_Input.close();
         m_StrmConn.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      // notify framework that BTConnection is gone
      Main.removeBTConnection(this);
   }
   
}
