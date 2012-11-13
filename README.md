Mars exploration
=============
OBJECTIVE:

Implementing a Multi-Agent System for simulating a scenario extraction
minerals on Mars.

DESCRIPTION:

A set of agents has the task to explore the environment for ores, and
carry as much as possible to the base. 

There are three types of agents:
  - The agent spotter's role is to look for sources of minerals and inspect them to determine whether can be explored. 
  - An agent is called the producer a source for a spotter to extract as much ore as possible this spring. 
  - When finished, the producer seeks to allocate one or more agents conveyors to carry the ore obtained from the base. 

In order to facilitate the search, all agents can locate sources of minerals and submit your location for the spotter.
The choice of the producer by the spotter should follow a protocol of negotiation to define. 
The allocation of carriers to a particular source must also follow a protocol negotiation, initiated by producer. 
This allocation must take into account the amount of ore transporting, in order to determine more accurately the number of carriers
necessary.

Agent-based model running:

![](https://github.com/fsschmitt/AIAD_Mars/raw/master/running_example.jpg) 
