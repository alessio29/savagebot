**Discord bot for playing table-top RPG online.**

Supports various dice-rolling, Savage Worlds initiative cards and other RPG stuff.


**Installation:**
Click on the following link and authorize bot on your sever: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0

**How to use it:**

Start you message with `!` then type any command from the following list. Some commands may require additional parameters.


__**CARDS category**__

**draw**		character [ilq]	draws card to character

add q if character has edge Quick

add l if character has edge Levelheaded

add il if character has edge Improved Levelheaded

**open**			openly deals several (1 by default) cards to current channel

**show**			Shows your cards, previously dealt to you by 'deal' command to current channel

**shuffle**			Shuffles current deck, resets secret cards dealt to all users in this channel

**deal**		CardCount User	secretly deals n (1 by default) cards to user (to self by default)

__**DICE category**__

**r**	or **roll**	rolls dice

You can use multiple rolls in one command separated by space: `!r d6 d4! d10+d12`

You can use comments in roll: `!r shooting at vampire s8 damage 2d6+1`

Currently supported dice codes are:

`!r 3d6` - roll 3 6-sided dice, show sum

`!r 2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`!r 4d6k3` - roll 4 6-sided dice keep 3 highest (DnD attributes roll-up)

`!r 2d10kl1` - roll 2 10-sided dice keep 1 lowest

`!r s8` - Savage Worlds roll with trait die d8 and wild die d6

`!r 3s8` - Savage Worlds roll with three trait die d8 (bursts)

`!r s8w10` - Savage Worlds roll with trait die d8 and wild die d10

`!r 3s8w8` - Savage Worlds roll with three trait die d8 and wild die d8 (bursts with non-default wild die)

`!r 6d6s4` - WoD, Lady Blackbird roll - rolls 6 d6 and returns how many dice rolled 4 or more

`12d10s7` - roll 12 d10s, 7+ is a success, count successes

`12d10f1s7` - roll 12 d10s, 7+ is a success, 1- is a failure, substract failures from successes

`12d10s7f1` -  same as above

`28d6!f1s10` - roll 28 open-ended d6s, 10+ is a success, 1- is a failure, substract failures from successes

`!r 6x4d6k3` - roll 4 dice keeping 3 highest 6 times

`!r d66` - rolls two d6 and returns values from 11 to 66 to support random choice from 6x6 table

`!r 4dF` - rolls Fudge/FATE dice

`!r d20adv+2` - advantage roll in DnD5e

`!r d20dis+2` - disadvantage roll in DnD5e

Bot supports limits to roll: 

`!r (3d8!+d6!)[6:24+6]` - this roll will give value at least 6 and no more than 30 (24+6). This used in our house-rule for damage rolls for Savage Worlds.

__**BENNIES category**__

**pocket**		<characterName>	Shows character's bennies

**use**			Uses one of character's benny

**benny**		<character>	Get benny from hat and adds it to characker's pocket

**hat**		[fill]	Puts all required bennies into the hat

__**INITIATIVE category**__

**fight**			starts new fight: shuffles deck, resets intiative tracker

**init**			Shows initiative tracker

**round**			Starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round

__**INFO category**__

**help**			Lists the description and syntax for every registered command.

**invite**			Creates invite link for this bot

__**TOKENS category**__

**tokens**			Shows characters and their tokens

**give** <character> [token count]		Gives token(s) to <character>

**take** <character> [token count]		Takes token(s) from <character>

**clear** <character>/all Clears token(s) for sprecified <character> or for all characters in channel


__**ADMIN category**__

**ping**			Checks SavageBot readiness

**prefix**		[character]	Sets <character> as custom user-defined command prefix or shows current prefix
