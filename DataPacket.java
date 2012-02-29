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
 * DataPacket.java
 *
 */

package bluetoothgateway;

import java.io.InvalidClassException;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class DataPacket {

   private Date   m_timestamp;
   private Date   m_original_timestamp;
   private Vector m_data;
   private String m_sensorname;
   private String[]m_channels;
   
   public DataPacket(Date timestamp, Vector data, String sensorname, String [] channels) {
      m_original_timestamp = m_timestamp = timestamp;
      m_data = data;
      m_sensorname = sensorname;
      m_channels = channels;
   }
   
   public String getSensorname() {
      return m_sensorname;
   }
   
   public String [] getChannels() {
      return m_channels;
   }
   
   public Vector getValues() {
      return m_data;
   }
   
   public long getTimestamp() {
      return m_timestamp.getTime();
   }
   
   public void setTimestamp(Date timestamp) {
      m_timestamp = timestamp;
   }
   
   /**
    * Returns the timestamp that was given initially to the DataPacket. This 
    * timestamp may have changed 
    * @return
    */
   public Date getOriginalTimestamp() {
      return m_original_timestamp;
   }
   
   /**
    * Prints a timestamp and the data into a string to be returned.
    * @return data contained within the packet
    */
   public String toString() {
      String strMessage = printDigits(m_timestamp.getTime(),12,true);
      
      // print individual values - special handling for Characters
      for(int i=0; i < m_data.size();i++) {
         if ( m_data.get(i) instanceof Character) {
            strMessage += " " + Character.getNumericValue(((Character)m_data.get(i)).charValue());
         } else {
            strMessage += " " + m_data.get(i);
         }
      }
      
      return strMessage;
   }
   
   /**
    * Returns the content of the data element index as integer value
    * @param index index of the data element
    * @return integer value of the data element
    * @throws java.lang.IndexOutOfBoundsException thrown when index is too high
    * @throws java.io.InvalidClassException thrown if data does not contain any known data type
    */
   public int getIntValue(int index) throws IndexOutOfBoundsException, InvalidClassException {
      if ( index >= m_data.size()) throw new IndexOutOfBoundsException();
      
      Object obj = m_data.get(index);
      
      // change value to integer - throw error if unknown type is found
      int value;
      if (obj instanceof Character) {
         value = (int) ((Character)obj).charValue();
      } else if (obj instanceof Short) {
         value = (int) ((Short)obj).shortValue();
      } if (obj instanceof Integer) {
         value = (int) ((Integer)obj).intValue();
      } else {
         throw new InvalidClassException("Unkown data type within DataPacket value");
      }
      
      return value;
   }
   
   /**
    * Retrieves the maximal value for the element at position index.
    * @param index
    * @return
    * @throws java.lang.IndexOutOfBoundsException thrown when index is too high
    * @throws java.io.InvalidClassException thrown if data does not contain any known data type
    */
   public long getMaxValue(int index) throws IndexOutOfBoundsException, InvalidClassException {
      if ( index >= m_data.size()) throw new IndexOutOfBoundsException();
      
      Object obj = m_data.get(index);
      
      // get maximum value - throw error if unknown type is found
      long value;
      if (obj instanceof Character) {
         value = Character.MAX_VALUE;
      } else if (obj instanceof Short) {
         value = Short.MAX_VALUE;
      } if (obj instanceof Integer) {
         value = Integer.MAX_VALUE;
      } else {
         throw new InvalidClassException("Unkown data type within DataPacket value");
      }
      
      return value;
   }

    /**
     * Returns a string with iDigits bytes representing the number given. This relates to printf("%03f",iNumber) with zeroPadding=true and iDigits=3.
     * @param iNumber  Number to convert
     * @param iDigits  Digits to represent
     * @param zeroPadding fill in leading zeros
     * @return string with converted number
     */
    private String printDigits( long iNumber, int iDigits, boolean zeroPadding) {
        byte [] converted = new byte[iDigits];
        long curNumber = iNumber;
        
        // step from LSB to MSB
        for (int i=0; i< converted.length; i++) {
            if ( curNumber > 0 ) {
                converted[converted.length-1-i] = (byte) ('0' + curNumber % 10);
                curNumber /= 10;
            } else {
                converted[converted.length-1-i] = (byte) (zeroPadding ? '0' : ' ');
            }
        }
        return new String(converted);
    }

}
