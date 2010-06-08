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
 * FrameDetector.java
 *
 */

package bluetoothgateway;

import java.util.Observable;
import java.util.Observer;

/**
 * This class detects the Frame type in the D?? format. Upon detection, it 
 * notifies its observers with the matching format string.
 * 
 * The detection waits until it finds one of the keys, or first three letters
 * and then issues the command.
 * 
 * Current assumptions:
 * The header of each format has just 3 bytes. This sequence uniquely identifies 
 * the packet. No checks are made whether the format is actually correct.
 * 
 * 
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class FrameDetector extends Observable implements StreamDecoder {

   private String [] m_knownFormats;
   private byte[][] m_keys;
   private byte[] m_buffer;
   
   public FrameDetector( String [] knownFormats ) {
      m_knownFormats = knownFormats;
      
      // extract key values for faster access
      m_keys = new byte[m_knownFormats.length][3];
      for (int i=0; i<m_knownFormats.length; i++) {
         byte [] format = m_knownFormats[i].getBytes();
         m_keys[i][0] = format[0];
         m_keys[i][1] = format[1];
         m_keys[i][2] = format[2];
      }
      
      m_buffer = new byte[3];
   }

   /** 
    * Upon an incoming data byte, the buffer is shifted and searched for one of 
    * the headers.
    * @param b
    */
   public void read(byte[] b) {

      for (int i=0; i<b.length; i++ ) {
         m_buffer[0] = m_buffer[1];
         m_buffer[1] = m_buffer[2];
         m_buffer[2] = b[i];

         // check for each key
         for(int j=0; j<m_keys.length; j++ ) {
            if ( m_keys[j][0] == m_buffer[0] && 
                 m_keys[j][1] == m_buffer[1] && 
                 m_keys[j][2] == m_buffer[2] ) {
               this.setChanged();
               this.notifyObservers(m_knownFormats[j]);
            }
         } // for j
      } // for i
      
   }

   
   /**
    * TEST CODE:
    * This tests the working of the FrameDetector class.
    */
   public static void main(String [] args) {
      
      String [] formats = {"DX3","DX4","DKB"};
      
      FrameDetector fd = new FrameDetector(formats);
      
      fd.addObserver(new Observer() {
         public void update(Observable o, Object arg) {
            System.out.println( ((String)arg));
         }
      });      
      
      byte [] input = { 2,3,4,'D','X', 0, 'D','X','3','D','D','K','B',5,6};
      
      fd.read(input);
      
   }

}
