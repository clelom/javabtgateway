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
 * ConfigFileReader.java
 *
 */

package bluetoothgateway;

import bluetoothgateway.gui.MainWindow;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * The ConfigFileReader reads in values for different other objects from a 
 * configuration file. The structure of the configuration file is:
 * 
 * key = value
 * 
 * Where key identifies what is described and the value identifies the parameter 
 * that will be processed according to the key type. The following keys have  
 * been defined:
 * 
 *  - btdevice: Expected value format: BLUETOOTH_MAC_ID (SENSOR_NAME)
 *              This is used by the bluetooth.gui.MainWindow object to describe 
 *              sensors that are known before a bluetooth scan has to be 
 *              performed.
 *  - filter:   Expected value format: D??;XXXXXXXXX...
 *              Defines a bluetooth.FrameParser format string that can be used 
 *              for selection by the user or auto-detection. Details on the 
 *              format are given in bluetooth.FrameParser
 *  - description: Expected value format: D??;STRING;STRING;STRING;...
 *                 Describes the names of the channels of the associated filter. 
 *                 Filters are matched by their D?? value. The filter MUST be 
 *                 defined before its description.
 * - opengui:   Expected value: true/false (default:true)
 * 
 * 
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class ConfigFileReader {

   private Vector m_btdevices; // bluetooth device descriptions for MainWindow
   private boolean m_bOpenGUI = true; // whether to open GUI

   public class Filter {
      public String filter;
      public Vector description;
      public Filter(String _filter) { filter = _filter; }
   }
   private Map m_filters;
   
   public ConfigFileReader() {
      m_btdevices = new Vector();
   }
   
   /**
    * Reads in the configuration file strFilename
    * @param strFilename Filename of the configuration file
    * @throws java.io.FileNotFoundException
    * @throws java.io.IOException
    */
   public ConfigFileReader(String strFilename) throws FileNotFoundException, IOException {
      m_btdevices = new Vector();
      
      BufferedReader cfgFile;
              
      // read in configuration file
      String line;
      cfgFile = new BufferedReader( new FileReader(strFilename));
      while((line = cfgFile.readLine()) != null) {
         int separator = line.indexOf("=");
         
         if (separator > 0 ) {
            String key = line.substring(0,separator).trim().toLowerCase();
            String value = line.substring(separator+1,line.length()).trim();
         
            // go through all defined values
            if (     key.compareTo("btdevice")    == 0 ) { addBTDevice(value); }
            else if( key.compareTo("filter")      == 0 ) { addFilter(value); }
            else if( key.compareTo("description") == 0 ) { addDescription(value); }
            else if( key.compareTo("opengui")     == 0 ) { m_bOpenGUI = (value.toLowerCase().compareTo("false") != 0); }
            else if( key.compareTo("connect")     == 0 ) { connectTo(value); }
            else {System.out.print("Unknown key found in configuration file: "+key); }
            
         }
      }
      
      cfgFile.close();
   }

   /**
    * Adds a bluetooth devide to the list of the bluetooth devices. 
    * Expected format: BLUETOOTH_MAC (SENSOR_NAME)
    * @param address
    */
   public void addBTDevice(String address) {
      m_btdevices.add(address);
   }
   
   /**
    * Returns whether the GUI should be opened
    * @return true if the GUI should be opened
    */
   public boolean isOpenGUI() {
      return m_bOpenGUI;
   }
   
   /**
    * Connects to the device given
    * @param Device address
    */
   private void connectTo(String value) {
      int iPos = value.indexOf(";");
      String address = value.substring(0,iPos);
      value = value.substring(iPos+1);
      
      iPos = value.indexOf(";");
      String sensorname = value.substring(0,iPos);
      String strFormat = value.substring(iPos+1);

      System.out.println("Connecting to \""+address+"\" with name \""+sensorname+"\" and format \""+strFormat+"\"");
      
      Main.addBTConnection(address, sensorname, strFormat);
   }

   /**
    * Passes all configuration file info to the main window
    * @param m_mainWindow
    */
   public void configureMainWindow(MainWindow mainWindow) {
      for(int i=0; i<m_btdevices.size(); i++) {
         mainWindow.addBTDevice((String)m_btdevices.get(i));
      }
      
      String [] formats = new String[0];
      if (m_filters!=null) {
         formats = new String[m_filters.size()];
         Iterator it = m_filters.entrySet().iterator();
         int i=0;
         while(it.hasNext()) {
            formats[i++] = ((Filter)((Map.Entry)it.next()).getValue()).filter;
         }
      }
      mainWindow.addFormatStrings( formats );
   }

   private void addDescription(String value) {
      if (m_filters == null) {
         System.out.println("ConfigFileReader: ERROR: description added before definition");
         return;
      }
      
      // determine format key
      int keysep = value.indexOf(";");
      if ( keysep < 0 ) {
         System.out.println("ConfigFileReader: ERROR: no description key found within: '"+value+"'");
         return;
      }
      String key = value.substring(0,value.indexOf(";"));
      
      Filter filter = (Filter)m_filters.get(key);
      filter.description = new Vector();
      
      String curdesc = value;
      while (curdesc.indexOf(";") > 0) {
         filter.description.add(curdesc.substring(0,value.indexOf(";")));
         curdesc = curdesc.substring(curdesc.indexOf(";")+1);
      }
      
   }
   
   public String[] getDescription(String format) {
      if (m_filters == null) {
         System.out.println("ConfigFileReader: ERROR: description added before definition");
         return null;
      }

      int keysep = format.indexOf(";");
      if ( keysep < 0 ) {
         System.out.println("ConfigFileReader: ERROR: no description key found within: '"+format+"'");
         return null;
      }
      String key = format.substring(0,format.indexOf(";"));
      Filter filter = (Filter)m_filters.get(key);

      if(filter == null) return null;
      if(filter.description == null) return null;

      String [] description = new String[filter.description.size()];
      for (int i=0; i<description.length; i++) {
         description[i] = (String)filter.description.get(i);
      }
      
      return description;
   }


   public void addFilter(String value) {
      if (m_filters == null) {
         m_filters = new HashMap();
      }
      
      // determine format key
      int keysep = value.indexOf(";");
      if ( keysep < 0 ) {
         System.out.println("ConfigFileReader: ERROR: no filter key found within: '"+value+"'");
         return;
      }
      String key = value.substring(0,value.indexOf(";"));
      
      m_filters.put(key, new Filter(value));
      
   }
   
   /**
    * Test code
    * @param args No arguments expected
    */
   public static void main(String [] args) {
      try {
         ConfigFileReader cfg = new ConfigFileReader("btg_config.txt");
      } catch (FileNotFoundException ex) {
         System.out.println("ERROR: file not found");
      } catch (IOException ex) {
         System.out.println("ERROR: IO Error");
         ex.printStackTrace();
      }
   }
   
}
