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
 * FileDumper.java
 *
 */

package bluetoothgateway;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;
import java.util.Vector;

/**
 *
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
class FileDumper implements Observer {

   PrintStream m_file;
   
   public FileDumper() {
		/*
		 * print stream for file output
		 * generate a file 'datastream_yyyyMMdd_HHmmss.txt'
		 */
		Date startTime;
		startTime = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String logFilename = "datalog_" + dateFormat.format(startTime) + ".txt";
      try {

         m_file = new PrintStream(new FileOutputStream(logFilename));
      } catch (FileNotFoundException ex) {
         System.out.println("ERROR: FileDumper: Could not create file");
         ex.printStackTrace();
      }
   }

   public void update(Observable obs, Object arg) {
         if ( arg instanceof DataPacket ) {
            DataPacket dp = (DataPacket)arg;
            m_file.print(dp);
            // write newline manually to see whether socket is open
            try {
               byte [] newline = {'\r','\n'};
               m_file.write(newline);
            } catch(IOException e) {
               m_file.close();
               System.out.println("Error: FileDumper IOException, closing file.");
               e.printStackTrace();
            }

         } else {
            System.out.println("TCPServer.Connection: Unknown update received");
         }
   }
   
   /**
    * Test code: creates a file and drops one packet content into it.
    * @param args
    */
   public static void main(String [] args) {
      FileDumper fd = new FileDumper();
      
      Vector v = new Vector();
      v.add(new Integer(  1));
      v.add(new Short(  (short)2));
      v.add(new Character(  'w'));
      v.add(new Integer(  4));
      
      Date now = new Date();
      String [] channels = {"1","2","3"};
      DataPacket dp = new DataPacket(new Date(now.getTime() +  200), v, "a",channels);

      fd.update(null, dp);
   }
}
