package org.camel.portscan;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.Socket;
import java.net.*;

/**
 * PortScanner
 *
 */
public class PortScan {

	public static void main( String[] args ) {

		System.out.println( "Java Port scanner" );
		System.out.println( "PortScan [ip] [startPort] [stopPort] [timeOut]" );

		String ip = "127.0.0.1";	// or localhost
		int startPortRange = 0, stopPortRange = 65535;
//		int poolSize = 1000;
		int timeOut = 200;		//	5000

		try {
			ip = args[0];
	        	startPortRange = Integer.parseInt(args[1]);
		        stopPortRange = Integer.parseInt(args[2]);
			timeOut = Integer.parseInt(args[3]);
		} catch (Exception e) {
			System.err.println( "PortScan [ip] [startPort] [stopPort] [timeOut]" );
		}
		System.out.println( "Scan IP " + ip + " opened ports from " + startPortRange + " to " + stopPortRange + " (timeout " + timeOut +")" );


		ConcurrentLinkedQueue openPorts = new ConcurrentLinkedQueue<>();
//		ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
		final ExecutorService executorService = Executors.newCachedThreadPool();

		AtomicInteger port = new AtomicInteger(startPortRange);

		while (port.get() < stopPortRange) {

			final int currentPort = port.getAndIncrement();
			final String ip2 = ip;
			final int timeOut2 = timeOut;

			executorService.submit(() -> {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip2, currentPort), timeOut2);
					socket.close();
					openPorts.add(currentPort);
//					System.out.println(ip2 + " ,port open: " + currentPort);
				} catch (IOException e) {
//					System.err.println(e);
				}
			});
		}

		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		List openPortList = new ArrayList<>();
		System.out.println("Total opened ports: " + openPorts.size());

		while (!openPorts.isEmpty()) {
			openPortList.add(openPorts.poll());
		}
		openPortList.forEach(p -> System.out.println("port " + p + " is open"));
		System.out.println("Done.");
	}
}
