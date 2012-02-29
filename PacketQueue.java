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
 * PacketQueue.java
 *
 */

package bluetoothgateway;

/**
 * Implements the java.util.Queue as it is not available here.
 * 
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class PacketQueue {
   
   /**
    * Used to generate a linked list of elements
    */
   private class QueueElement {
      public Object element;
      public QueueElement next;
      public QueueElement(Object o) {
         element = o;
         next = null;
      }
   }
   private QueueElement m_firstElement;
   private QueueElement m_lastElement;
   
   /**
    *  Retrieves, but does not remove, the head of this queue.
    * @return the head of this queue, null if empty 
    */
   public Object element() {
      return peek(); // this should throw an exception, which was not implemented
   }

   /**
    * Inserts the specified element into this queue
    * @param o the element to insert. 
    * @return true if it was possible to add the element to this queue, else false
    */
   public boolean offer(Object o) {
      if (m_lastElement == null) {
         m_firstElement = m_lastElement = new QueueElement(o);
      } else {
         m_lastElement.next = new QueueElement(o);
         m_lastElement = m_lastElement.next;
      }
      return true;
   }
   
   /**
    * Retrieves, but does not remove, the head of this queue, returning null if this queue is empty. 
    * @return the head of this queue, or null if this queue is empty.
    */
   public Object peek() {
      return (m_firstElement==null)? null : m_firstElement.element;
   }
   
   /**
    * Retrieves, but does not remove, n items from the head of the queue. 
    * Always returns an array with n elements, but sets them null if the queue 
    * is too short.
    * @param n
    * @return
    */
   public Object[] peekN(int n) {
      Object[] obs = new Object[n];
      
      QueueElement curEl = m_firstElement;
      for (int i=0; i<n; i++) {
         if (curEl != null) {
            obs[i] = curEl.element;
            curEl = curEl.next;
         } else break;
      }
      return obs;
   }
   
   /**
    * Retrieves and removes the head of this queue, or null if this queue is empty. 
    * @return the head of this queue, or null if this queue is empty.
    */
   public Object poll() {
      if (m_firstElement == null) return null;
      
      Object o = m_firstElement.element;
      m_firstElement = m_firstElement.next;
      
      // check whether this was the only element in the queue
      if (m_firstElement == null) m_lastElement = null;
      
      return o;
   }
   
   /**
    * Retrieves and removes the head of this queue. This method differs from the poll method in that it throws an exception if this queue is empty. 
    * @return the head of this queue. 
    */
   public Object remove() {
      return poll(); // this should throw an exception, which was not implemented
   }

   public DataPacket [] getAllPackets() {
      
      // count packets
      int count = 0;
      QueueElement curElement = m_firstElement;
      while (curElement != null){
         count++;
         curElement = curElement.next;
      }
      
      DataPacket[] packets = new DataPacket[count];
      
      count=0;
      curElement = m_firstElement;
      while (curElement != null) {
         packets[count++] = (DataPacket)curElement.element;
         curElement = curElement.next;
      }
      
      return packets;
   }
}
