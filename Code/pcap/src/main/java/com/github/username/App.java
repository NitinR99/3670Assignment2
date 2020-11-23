// App.java
package com.github.username;

import java.io.IOException;
import org.pcap4j.core.*;
import org.pcap4j.util.*;
import org.pcap4j.packet.*;
import java.util.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
public class App
{
 int packet_count = 0;
  private static final String READ_TIMEOUT_KEY = PacketListener.class.getName() + ".readTimeout";
  private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]
  private static final String SNAPLEN_KEY = PacketListener.class.getName() + ".snaplen";
  private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

  private static final String TIMESTAMP_PRECISION_NANO_KEY = PacketListener.class.getName() + ".timestampPrecision.nano";
  private static final boolean TIMESTAMP_PRECISION_NANO = Boolean.getBoolean(TIMESTAMP_PRECISION_NANO_KEY);

public int sniff() throws PcapNativeException, NotOpenException
{

    PcapNetworkInterface nif = null;
    try {
      nif = new NifSelector().selectNetworkInterface();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (nif == null) {
      return -1;
    }

    System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

    PcapHandle handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 10);
	handle.setFilter("tcp port 4445 or udp port 4445", BpfCompileMode.OPTIMIZE);
    while (true)
    {
      Packet packet = handle.getNextPacket();
      if (packet != null)
      {
        if (packet.contains(IpV4Packet.class) && (packet.contains(TcpPacket.class) || packet.contains(UdpPacket.class)))
	{
          IpV4Packet pkt = packet.get(IpV4Packet.class);
          if (packet.contains (TcpPacket.class))
	   {
             TcpPacket tcpPkt = packet.get(TcpPacket.class);
	System.out.println(tcpPkt.getHeader().getDstPort().value());

            	 System.out.println("TCP -->"+pkt.getHeader().getSrcAddr() + ":" + tcpPkt.getHeader().getDstPort());
	    }

          else
	  {
             UdpPacket udpPkt = packet.get(UdpPacket.class);
	System.out.println(udpPkt.getHeader().getDstPort().value());

             System.out.println("UDP -->"+pkt.getHeader().getSrcAddr() + ":" + udpPkt.getHeader().getDstPort());
	  }


        }
	else
	{
		System.out.println("Unknown packet ---> "+ packet.getPayload());
	}
       packet_count++;
      }
    }
  }
    public static void main(String[] args) throws PcapNativeException, NotOpenException
    {
	App o1=new App();
        o1.sniff();
}
}
