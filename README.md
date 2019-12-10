# Helios

**Authors:**

- Marco Judt (ic18b039@technikum-wien.at)
- Florian Dienesch (ic18b509@technikum-wien.at)
- Daniel Melichar (ic18b503@technikum-wien.at)


## Description
The International Space Station (ISS) circles the earth in roughly 92 minutes and
completes 15.5 orbits per day. The ISS orbits are fixed, and only a few 
geographical locations are flown over. A precise live map of the station's current
position, and the weather conditions around that point on earth, are required.

The Helios web-service aims to solve this problem. By collection data from 
various sources and APIs, such as from NASA and ZAMG, processing the information,
and then displaying a live map representation.

Our main goal is to answer the simple question of: where do I need to stand to 
have a clear view onto the ISS with my telescope?

## Functionalities

- Map shown on Web page
- NASA API for ISS location integrated into Map
- ZAMG API for weather integrated into Map
- (optional) Forecast
- (optional) ISS Pass Times: A list of upcoming ISS passes for a particular location
- (optional) How many people are in space right now?