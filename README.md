**Discord bot for playing table-top RPG online.**

Supports various dice-rolling, Savage Worlds initiative cards and other RPG stuff.


**Installation:**
Click on the following link and authorize bot on your server: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0

**How to use it:**

Start you message with `!` then type any command from the following list. Some commands may require additional parameters.


__**CARDS category**__

**deal** or **dl**    [<card_count>] [<user_name>]    Secretly deals several (1 by default) cards to user (to self by default)

**show** or **sh**    Shows your cards, previously dealt to you by 'deal' command to current channel

**put**         Deals card and "puts it on table" (current channel). 

**shuffle**        Shuffles current deck, resets secret cards dealt to all users in this channel

__**CHARACTERS category**__

**list**        Lists characters with tokens and states

**remove**      <charName1> [<charName2> ...] removes character(s)

__**DICE category**__

**r**	or **roll**	rolls dice

You can totally omit command and roll dice with `!3d10`

You can use multiple rolls in one command separated by space: `!r d6 d4! d10+d12`

You can use comments in roll: `!r shooting at vampire s8 damage 2d6+1`

Currently supported dice codes are:

`!3d6` - roll 3 6-sided dice, show sum

`!2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`!4d6k3` - roll 4 6-sided dice keep 3 highest (DnD attributes roll-up)

`!2d10kl1` - roll 2 10-sided dice keep 1 lowest

`!s8` - Savage Worlds roll with trait die d8 and wild die d6

`!3s8` - Savage Worlds roll with three trait die d8 (bursts)

`!s8w10` - Savage Worlds roll with trait die d8 and wild die d10

`!3s8w8` - Savage Worlds roll with three trait die d8 and wild die d8 (bursts with non-default wild die)

`!Nw[+1/+2]` - West End Games D6 System roll. Rolls N d6's one of which is 'wild': it explodes on 6 and subtracts highest number on 1.

`!6d6s4` - WoD, Lady Blackbird roll - rolls 6 d6 and returns how many dice rolled 4 or more

`!12d10s7` - roll 12 d10s, 7+ is a success, count successes

`!12d10f1s7` - roll 12 d10s, 7+ is a success, 1- is a failure, subtract failures from successes

`!12d10s7f1` -  same as above

`!28d6!f1s10` - roll 28 open-ended d6s, 10+ is a success, 1- is a failure, subtract failures from successes

`!6x4d6k3` - roll 4 dice keeping 3 highest 6 times

`!d66` - rolls two d6 and returns values from 11 to 66 to support random choice from 6x6 table

`!4dF` - rolls four Fudge/FATE dice

`!d20adv+2` - advantage roll in DnD5. You can roll any dice with advantage: `!3d6adv`

`!d20dis+2` - disadvantage roll in DnD5e. You can roll any dice with disadvantage: `!3d8dis`

Bot supports limits to roll: 

`!r (3d8!+d6!)[6:24+6]` - this roll will give value at least 6 and no more than 30 (24+6). This used in our house-rule for damage rolls for Savage Worlds.

`!dC` - Carcosa roll. First d20 is rolled to determine size of dice - then this dice is rolled.

**rh**    <expression_1> ... <expressionN>    rolls multiple dice and prints a distribution of results.
Example: `!rh 1000x2d6`
  
**rs**    [<heading_1>] <expression_1> ... [<heading_N>] <expression_N>    rolls multiple dice and print them out sorted.
This is mostly useful for rolling initiative as a single command.
!rs Huey d20 Dewey d20 Louie d20 => 
`Dewey 14
Huey 10
Louie 5`

__**TOKENS category**__

**give** character [token count]		Gives token(s) to <character>

**take** character [token count]		Takes token(s) from <character>

**clear** character/all Clears token(s) for sprecified character or for all characters in channel

__**STATES category**__

**clstate** or **cs** <character_name>/all clears all states of character/all characters in channel

**remstate** or **rst** <character_name> <state1> [...] removes states from character

**state** or **st** <character_name> <state1> [...] sets states to character
  
  States are Savage Worlds states: Shaken, Stunned, Entangled, Bound, Distracted and Vulnerable. You can write them in any case even like sTuNnEd. You can use abbreviations: sha, stn, ent, bnd, dis, vul.

__**BENNIES category**__

**pocket**		<characterName>	Shows character's bennies

**use**			Uses one of character's benny

**benny**		<character>	Get benny from hat and adds it to characker's pocket

**hat**		[fill]	Puts all required bennies into the hat

__**INITIATIVE category**__

**fight** or **f**			starts new fight: shuffles deck, resets intiative tracker

**init**			Shows initiative tracker

**round** or **rd**	[+] [-<char_name1> .. -<char_nameN>]		Starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round. If `+` provided initiative cards will be dealt again to characters. If character names preceded by `-` were provided - characters will be removed from initiative tracker.

**drop** <char_name1> ... <char_nameN>  Removes characters from initiative tracker.

**di**      <character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]    Deal initiative card. Modifiers should start with - and be either 'q', 'l', 'i' or 'h': Quick, Levelheaded, Improved Levelheaded and Hesitant respectively.

**card** <character_name> gives character <character_name> new intiative card. Used as 'Card for benny' rule.

__**INFO category**__

**help**			Lists the description and syntax for every registered command.

**invite**			Creates invite link for this bot

__**ADMIN category**__

**ping**			Checks SavageBot readiness

**prefix**		[character]	Sets <character> as custom user-defined command prefix or shows current prefix
