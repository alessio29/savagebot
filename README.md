**Discord bot for playing table-top RPG online.**

Supports various dice-rolling, Savage Worlds initiative cards and other RPG stuff.


**Installation:**
Click on the following link and authorize bot on your server: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0

**How to use it:**

Start you message with `!` then type any command from the following list. Some commands may require additional parameters.


__**CARDS category**__

**deal** or **dl**    [<card_count>] Secretly deals several (1 by default) cards to user's private channel

**show** or **sh**    Shows your cards, previously dealt to you by 'deal' command to current channel

**put**         Deals card and "puts it on table" (shows in current channel). 

**shuffle**        Shuffles current deck, resets secret cards dealt to all users in this channel

__**CHARACTERS category**__

**list**        Lists characters with tokens and states

**remove**      <charName1> [<charName2> ...] Removes character(s) totally. This command could be used to erase defeated extras.

__**DICE category**__

**r**	or **roll**	die_code1 [die_code2] [...] rolls dice

You can totally omit command **roll** itself and roll dice with simple `!3d10`

You can make multiple rolls in one command separated by space: `!r d6 d4! d10+d12`

You can use comments in roll: `!r shooting at vampire s8 damage 2d6+1`

Currently supported dice codes are:

`!3d6` - roll 3 6-sided dice, show sum

`!2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`!4d6k3` - roll 4 6-sided dice keep 3 highest (DnD attributes roll-up)

`!2d10kl1` - roll 2 10-sided dice keep 1 lowest

`!s8` - Savage Worlds roll with trait die d8 and wild die d6

`!3s8` - Savage Worlds roll with three trait die d8 (Can be used for bursts)

`!s8w10` - Savage Worlds roll with trait die d8 and wild die d10

`!3s8w8` - Savage Worlds roll with three trait die d8 and wild die d8 (bursts with non-default wild die)

`!Nw[+1/+2]` - West End Games D6 System roll. Rolls N d6's one of which is 'wild': it explodes on 6 and subtracts highest number on 1.

`!6d6s4` - WoD, Lady Blackbird roll - rolls 6 d6 and returns how many dice rolled 4 or more

`!12d10s7` - roll 12 d10s, each 7+ result is a success, shows count of successes

`!12d10f1s7` - roll 12 d10s, each 7+ result is a success, each 1- result is a failure, shows (successes-failures)

`!12d10s7f1` -  same as above (you can provide success or failure condition in any order)

`!28d6!f1s10` - roll 28 open-ended d6s, each 10+ is a success, each 1- is a failure, show (successes-failures)

`!6x4d6k3` - roll 4 dice keeping 3 highest 6 times

`!d66` - rolls two d6 and returns values from 11 to 66 to support random choice from 6x6 table (for example in Coriolis)

`!4dF` - rolls four Fudge/FATE dice

`!d20adv+2` - advantage roll in DnD5. You can roll any dice with advantage: `!3d6adv`

`!d20dis+2` - disadvantage roll in DnD5e. You can roll any dice with disadvantage: `!3d8dis`

Bot supports limits to roll: 

`!r (3d8!+d6!)[6:24+6]` - this roll will give value at least 6 and no more than 30 (24+6). This used in our house-rule for damage rolls for Savage Worlds.

`!dC` - Carcosa roll. First d20 is rolled to determine size of dice - then this dice is rolled.

**rh**    <expression_1> ... <expressionN>    creates data for histogram: rolls dice multiple times and shows resulting distribution. 
Example: `!rh 1000x2d6` - rolls 2d6 1000 times and shows results table.
  
**rs**    [<heading_1>] <expression_1> ... [<heading_N>] <expression_N>    rolls multiple dice and print them out sorted.
This is mostly useful for rolling initiative as a single command.
!rs Huey d20 Dewey d20 Louie d20 => 
`Dewey 14
Huey 10
Louie 5`

__**TOKENS category**__

**give** character1 [token count1] [character2 [token count2]] [...]		Gives token(s) to character(s). Example: `!give Huey 2 Louie Dewey 3` - gives 2 tokens to Huey, 1 to Louie and 3 to Dewey. 

**take** character [token count] [character2 [token count2]] [...]    Takes token(s) from character(s).  Example: `!give Huey Louie Dewey 2` - takes 1 tokens from Huey, 1 from Louie and 2 from Dewey.

**clear** character1 [character2] [...] /all    Clears token(s) for sprecified character or for all characters in channel

__**STATES category**__

**clstate** or **cs** <character_name>/all clears all states of character/all characters in channel

**remstate** or **rst** <character_name1> <state1> [<state2>] [...]   Removes states from character. Example: `!remstate Huey stunned vul Dewey dis`

**state** or **st** <character_name1> <state1> [<state2>] [...]   Sets states to character. Example: `!state Huey stunned vul dis Dewey dis ent`
  
  States are Savage Worlds states: Shaken, Stunned, Entangled, Bound, Distracted and Vulnerable. You can write them in any case even like sTuNnEd. You can use abbreviations: sha, stn, ent, bnd, dis, vul.

__**BENNIES category**__

**pocket**		<characterName>	Shows character's bennies

**use**			Uses one of character's benny

**benny**		<character>	Get benny from hat and adds it to characker's pocket

**hat**		[fill]	Puts all required bennies into the hat

__**INITIATIVE category**__

**fight** or **f**			starts new fight: shuffles deck, resets intiative tracker

**init**			Shows current initiative tracker

**round** or **rd**	[+] [-<char_name1> .. -<char_nameN>]		Starts new round: resets resets intiative tracker, shuffles deck, if joker was dealt on previous round. If `+` provided initiative cards will be dealt again to characters. If character names preceded by `-` were provided - characters will be removed from initiative tracker.
Examples:
`!rd` - starts new round
`!rd +` - starts new round and deals initiative cards to all characters in initiative tracker. 
`!rd + -Dewey -Scrooge` - starts new round, removes characters Dewey and Scrooge from initiative tracker and deals initiative cards to all remaing characters.

**drop** <char_name1> ... <char_nameN>  Removes characters from initiative tracker.

**di**      <character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]    Deal initiative card. Modifiers should start with `-` and be either 'q', 'l', 'i' or 'h': Quick, Levelheaded, Improved Levelheaded and Hesitant respectively.
Example: `!di Huey -q Dewey Louie -l Scrooge -h Ma_Beagle -iq`

**card** <character_name>   Simply gives character <character_name> new intiative card. Used as 'Card for benny' rule or 'Tactician' edge .

__**INFO category**__

**help**			Lists the description and syntax for every registered command.

**invite**			Creates invite link for this bot

__**ADMIN category**__

**ping**			Checks SavageBot readiness

**prefix**		[symbol]	Sets <symbol> as custom user-defined command prefix or shows current prefix if no parameters provided.
