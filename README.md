Discord bot for playing Savage Worlds RPG online. 
Supports dice-rolling, initiative cards and other stuff.

Use prefix ~ as first symbol before command 
List of commands: 
ping - checks SavageBot readiness 
help - shows this info
deal [n] [user] - secretly deals n (1 by default) cards to user (to self by default) 
open [n] - openly deals n (1 by default) cards to current channel
show - show your secret cards to current channel
shuffle - shuffles deck, resets secret cards
=====================================================
roll or r = rolls dice
Currently supported dice codes are:
ndm - roll n m-sided dice, show sum
ndm! - roll n 'exploding' m-sided dice, show sum
ndmkp - roll n m-sided dice keep p highest
ndmklp - roll n m-sided dice keep p lowest\nsm - where m is one of (4,6,8,10,12) - Savage Worlds roll with wild die
nsm - as previous but instead of d6 wild die is dn
lxdice - roll dice l times - i.e. 6x4d6k3
You can use multiple rolls in one command separeted by space:
~r d6 d4! d10+d12
You can use comments in roll:
~r shooting at vampire s8 damage 2d6+1
=====================================================
fight - starts new fight: shuffles deck, resets intiative tracker
round - starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round
show - shows initiative tracker
draw character [ilq] - adds to initiative tracker character and draws card to him
add q - if character has edge Quick, l - if character has edge Levelheaded and 
il - if character has edge Impreved Levelheaded
