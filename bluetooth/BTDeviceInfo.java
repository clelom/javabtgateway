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
 * BTDeviceInfo.java
 */

package bluetoothgateway.bluetooth;

/**
 * This class stores information about the devices found.
 * 
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class BTDeviceInfo {
   public String name;
   public String address;
   
   public String getConnectionString() {
      return "btspp://"  + address + ":1;authenticate=true;encrypt=true;master=false";      
   }
}
