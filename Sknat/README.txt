Sknat

Stripped down real time strategy (RTS) game.

The goal of the game is to destroy the enemy headquarters and to defend your
own. To do this you have control over your units (which look like circles).
Units can move around the arena and are equipped with a gun which when fired
causes damage to and eventually destroys other units.

The headquarters is a special building owned by each player which during the
game will produce more units. The headquarters will continuously produce units
at a fixed rate which cannot be altered. There are no resources which must be
harvested in order for this to happen.

All units have identical behaviour but differ by their characteristics:
* Agility (ability to change direction and accelerate)
* Attack Power (ability to inflict damage on other units)
* Defensive Strength (ability to resist damage)
* Speed (top speed)

Units are drawn using simple graphics and are essentially just circles.
Defensive strength is indicated by the thickness of the line around the
circumfrence. Attack power is indicated by the thickness of the gun. A triangle
which is opposite the gun depicts the engine at the rear of the unit. The length
of the triangle (how far it reaches towards the front of the unit) depicts
speed. The width of the triangle depicts agility.

The characteristics of the units being produced by the headquarters are
controlled by the player. Sliders allow points (up to a total of 100) to be
split between the different characteristics. The headquarters will continuously
produce units (at the fixed rate) and which have the characteristics set by the
sliders.

By this method the player can "emulate" common unit classes found from popular
RTS games. Defensive turrets would have very high defensive strength and attack
power only (a very small speed would also be required in order to maneuver them
into the correct position). Tanks would have high power and strength, with
medium speed and low agility. Buggies would have high agility and top speed, and
lower strength and power. Bomber planes would have high top speed and attack
power, with low agility and defensive strength.

By this description it should be easy to produce the basic game which has most
of the elements of a basic RTS.

