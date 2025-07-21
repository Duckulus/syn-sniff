
# `syn-sniff`
![Support Discord](https://img.shields.io/discord/850134191232647229?style=flat&label=Support%20Discord&color=%237289da)

A Paper plugin that passively sniffs TCP/IP SYN packets to give deeper insight into player connections. 
This can be used to reveal information such as the Operating System or VPN.

## Requirements
- Java 21
- pcap native library (libpcap, WinPcap, Npcap)
- Administrator/privileges or [Capabilities (Linux)](./docs/capabilities.md)

## Usage
todo

## Developer API
todo

## Todo

This project is under active development and there is plenty of room to contribute.

Below is a list of stuff that still needs to be done:

- [ ] More sophisticated OS prediction using more sample Records
- [ ] Support for IPv6
- [ ] API for plugin devs
- [ ] More modular code structure possibly supporting different kinds of servers
- [ ] Optional Persistence of Fingerprints
