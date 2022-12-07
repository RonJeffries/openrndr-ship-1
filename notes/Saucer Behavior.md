# Saucer Behavior

## Large Saucer

Large saucer appears seven seconds after it last disappeared. It flies for seven seconds. It changes direction every 1.5 seconds. (Possible new story: every 0.5-1.5 seconds randomly.) It chooses randomly between straight, up 45 degrees, or down 45 degrees. Its speed is U.SAUCER_SPEED = 1500, (Ship is capped to SPEED_OF_LIGHT, which is 5000, which is bloody fast.

Saucer starts randomly in vertical on the x = left =right edge. It travels left to right on its first traversal, and alternates right to left and left to right thereafter.

It fires a missile every 0.5 seconds, in a random direction. Missile 
intrinsic velocity is the same as the Ship's, 1/3 light speed.

Large saucer scores 200 if you hit it with a ship missile.

Its missiles destroy asteroids and the ship, to no score for you. If it 
collides with ship, no score. If it collides with anything else, no score.

## Small Saucer

Details to follow. Score is 1000 if you kill it.