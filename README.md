**Discord bot for playing table-top RPG online.**

Supports various dice-rolling, Savage Worlds initiative cards and other RPG stuff.


**Installation:**
Click on the following link and authorize bot on your sever: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0

**How to use it:**

Start you message with `~` then type any command from the following list. Some commands may require additional parameters.

`~ping` - checks SavageBot readiness 

`~help` - shows this info

`~deal [n] [user]` - secretly deals n (1 by default) cards to user (to self by default) 

`~open [n]` - openly deals n (1 by default) cards to current channel

`~show` - show your secret cards to current channel

`~shuffle` - shuffles deck, resets secret cards

`~roll` or `~r` - rolls dice

Currently supported dice codes are:

`~r 3d6` - roll 3 6-sided dice, show sum

`~r 2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`~r 4d6k3` - roll 4 6-sided dice keep 3 highest (DnD attributes roll-up)

`~r 2d10kl1` - roll 2 10-sided dice keep 1 lowest

`~r s8` - Savage Worlds roll with trait die d8 and wild die d6

`~r 10s8` - Savage Worlds roll with trait die d8 and wild die d10

`~r 6d6s4` - Lady Blackbird roll - rolls 6 d6 and returns how many dice rolled 4 or more

`~r 6x4d6k3` - roll 4 dice keeping 3 highest 6 times

You can use multiple rolls in one command separated by space: `~r d6 d4! d10+d12`

You can use comments in roll: `~r shooting at vampire s8 damage 2d6+1`

**Savage Worlds initiative cards support**

`~fight` - starts new fight: shuffles deck, resets intiative tracker.

`~round` - starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round.

`~show` - shows initiative tracker.

`~draw character [ilq]` - adds to initiative tracker character and draws card to him. Add q - if character has edge Quick, l - if character has edge Levelheaded and il - if character has edge Improved Levelheaded

**Savage Worlds bennies support**

`~hat` - prepares hat with 20 white, 10 red and 5 blue bennies (for Deadlands). 

`~benny Rory Scorcetti` - pulls benny out of hat and gives it to character 'Rory Scorcetti'.

`~pocket Rory Scorcetti` - shows bennies available to character 'Rory Scorcetti'.
  
 `~use white Rory Scorcetti` - pulls out of Rory Scorcetti's pocket white benny and expends it.
