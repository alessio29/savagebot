**Discord bot for playing Savage Worlds RPG online.**

Supports dice-rolling, initiative cards and other stuff.

**How to use it:**

Start you message with `~` then type any command from the following list. Some commands may require additional parameters.

`ping` - checks SavageBot readiness 

`help` - shows this info

`deal [n] [user]` - secretly deals n (1 by default) cards to user (to self by default) 

`open [n]` - openly deals n (1 by default) cards to current channel

`show` - show your secret cards to current channel

`shuffle` - shuffles deck, resets secret cards

`roll` or `r` = rolls dice

Currently supported dice codes are:

`~r 3d6` - roll 3 6-sided dice, show sum

`~r 2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`~r 4d6k3` - roll 4 6-sided dice keep 3 highest (DnD attributes roll-up)

`~r 2d10kl1` - roll 2 10-sided dice keep 1 lowest

`~r s8` - Savage Worlds roll with trait die d8 and wild die d6

`~r 10s8` - Savage Worlds roll with trait die d8 and wild die d10



`~r 6x4d6k3` - roll 4 dice keeping 3 highest 6 times

You can use multiple rolls in one command separated by space: `~r d6 d4! d10+d12`

You can use comments in roll: `~r shooting at vampire s8 damage 2d6+1`


`~fight` - starts new fight: shuffles deck, resets intiative tracker.

`~round` - starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round.

`~show` - shows initiative tracker.

`~draw character [ilq]` - adds to initiative tracker character and draws card to him. Add q - if character has edge Quick, l - if character has edge Levelheaded and il - if character has edge Impreved Levelheaded
