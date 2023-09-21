@echo on
java -cp ./target/port-scan-1.0-SNAPSHOT.jar org.camel.portscan.PortScan 192.168.3.50 0 65535 200
@echo off
rem  localhost 0 1024 500