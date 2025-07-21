
# `syn-sniff`
![Support Discord](https://img.shields.io/discord/850134191232647229?style=flat&label=Support%20Discord&color=%237289da)

A Paper plugin that passively sniffs TCP/IP SYN packets to give deeper insight into player connections. 
This can be used to reveal information such as the Operating System or VPN.

## Requirements
- Java 21
- pcap native library (libpcap, WinPcap, Npcap)
- Administrator/privileges or [Capabilities (Linux)](./docs/capabilities.md)

## Usage

First make Sure you meet all the [Requirements](#requirements)

### Installation
1. Download the latest Jar from the [Releases](https://github.com/Duckulus/syn-sniff/releases) page.
2. Place the `.jar` into your servers `plugins/` directory
3. Start the server once to generate the default config
4. Open `plugins/SynSniff/config.yml` and configure the plugin
5. Restart the Server

### Commands

| Command                 | Description                         | Permission                |
|-------------------------|-------------------------------------|---------------------------|
| `/fingerprint <player>` | View raw TCP/IP fingerprint info    | `synsniff.command-fingerprint`    |
| `/predictos <player>`   | Show the predicted operating system | `synsniff.command-predictos`      |

### Demo
![Demo](./assets/demo.gif)


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
