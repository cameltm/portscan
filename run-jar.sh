#!/bin/bash
java -cp ./target/port-scan-1.0-SNAPSHOT.jar org.camel.portscan.PortScan 192.168.3.60 0 65535 5000
