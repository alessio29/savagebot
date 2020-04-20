# Introduction
**Discord bot for playing table-top RPG online.**
Supports various dice-rolling, Savage Worlds initiative cards, bennies, tokens, and other RPG stuff.

## Patreon
In case you like this bot so much - you can support development via Patreon: https://www.patreon.com/savagebot

## Installation
Click on the following link and authorize bot on your server: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0

To know whether the bot is alive:
```javascript
!ping
```

If that exclamation mark is an issue (conflict with another bot?), you can change it with `prefix` command followed by a symbol to use as prefix from now on:
```javascript
!prefix #
```

`invite` command let you get this bot invite link, to share it with friends for their server.
```javascript
!invite
```

# Help
All commands starts with`!`.
Most commands can have their name shortened to speed things up.

To get help from a discord channel, just type `help` command:
```javascript
!help
```

## Dice Rolls
Let's first see the simpliest rolls for Savage Worlds Extra and Wild Cards. But, in the [Avanced](#advanced) section below, we will see more complex rolls as well as rolls for other systems, including D20 initiative.

### Simple die
To roll a single die (no Acing), you can use the `roll` (or `r` for short) command, or even simply use the die as command `d4`, `d6`, `d8`, `d10` or `d12`.

For example, you need to know in which direction a grenade deviates?
```javascript
!r d12
```
**Tips:** 
* In fact, you can just roll any die `!d20`, `!d100`, even very weird ones `!d73` if you feel like it!
* You can also directly include modifiers (after or before the roll), for example, here is a human running roll `!6+d6`.

### Trait rolls
Traits roll do Ace (if the die hits its max, you roll another one and add it, until no more die aces).
Follow the die with an `!`.

For example, lets roll a Fighting d6 for a bandit extra:
```javascript
!d6!
```
The result will directly display the sum of the dices. You won't see each individual ones. 
Here, on a d4, we got 4, then 2:
```
> d4!: 6 = 6
```

**Tips** You can directly add modifiers to the roll, like a -2 (dim light) penalty to Shooting `!d6!-2`, or Combat Reflexes adding +2 to recover from Shaken `!d8!+2`. Untrained would be `!d4!-2`.

### Wildcard Trait rolls
Wild Cards do roll a "Wild Die" (usually a d6) next to their Trait and keep the highest of both. Use `s4`

For example, let's have Player Character Huey rolls for Persuasion:
```javascript
!s8
```

This will not only display the highest total to keep, but also individual rolls, so you know whether you got Snake Eyes, or if you hit Innocent Bystanders.
```
> s8: [5, w4] = 5(1)
```

The number in parenthesis in the end, if the number of successes against a Target Number of 4 (which is usually the case for Trait rolls). 0 would be a failure, 1 a simple success, 2 a success and a raise, and so on.
Here we have a nice roll with 2 raises:
```
> s8: [13; w2] = 13 (3)
```

Oops, Snake Eyes!
```
> s12: [1; w1] = 1
```

**Tips:** You can put descriptive text before the roll (it will return to next line after each roll).
```javascript
Huey's Persuasion is !s8
```

### Parry
For rolls where the Target Number is not the standard 4, you can add a `t` parameter after the `s` savage roll.
For example, Huery attacks in melee a Parry 6 bandit:
```javascript
Huey Fighting vs Bandit: !s10t6
```

The number of success and raises is calculated accordingly:
```
> Huey Fighting vs Bandit: s10t6: [9; w3] = 9 (1; TN: 6)
```

### Custom Wild Die
If a character has an Edge such as Master, they get to roll a Wild Die higher than d6. You can add `w` to the Savage roll to tell which Wild Die to use.
Here is Master d12+2, using a d10 for Wild Die.
```javascript
!s12w10+2
```

**Tips:** You can combine with specific target number, but if you add modifiers, you must also add parenthesis.
This rolls d12+2, with a d10 for Wild Die, against Parry 8.
```javascript
!(s12w10t8)+2
```

### Damage rolls
Pretty simple, back to standard `d` syntax. Damage dice ace.

You can roll multiple dice and sum them up.
Here is a knife (Str+d4) wielding bandit with d6 strength:
```javascript
!d6!+d4!
```
Here is a bow (2d6) wielding assassin:
```javascript
!2d6!
```

**Tips:** You roll multiple separate rolls on the same line, and put texts around as you like.
```javascript
The assassin shoots at Huey !s8 Damage: !2d6!+1 Bonus damage (if raise): !d6!
```
Will display:
```
The assassin shoots at Huey s8: [1; w3] = 3
Damage: 2d6!+1: 5 + 1 + 1 = 7
Bonus damage (if raise): d6!: 1 = 1
```

**Not yet:** You can't compare damage to toughness and read the number of wounds. `!2d6!t8` isn't available yet.

## Savage Worlds Initiative

### Start a fight
To start a fight use the `fight` or `f` command:
```javascript
!f
```
This shuffles deck and resets (clears) initiative tracker.

### Deal cards
To deal cards to one or more characters, use `di` followed by the characters' names. Those are not the discord names of the players, but really the character names or nicknames, and NPC names.
```javascript
!di Huey Dewey Bandits Wolves
```
**Tips**: Remove spaces from their name (or they will be considered multiple characters). Keep the character names short, like one word, you might have to type them more than once.

### Quick, Level Headed, and Hesitant
For characters with such Edges/Hindrances, when dealing cards to them, follow their name with -q (for Quick), -l (for Level Headed), -i (for Improved Level Headed), or -h (for Hesistant).
```javascript
!di Huey -q Dewey Bandits -h Assassin -i
```

**Tips**: When a player has Level Headed and Improved Level Headed, you only add `-i`.

### Tactician, Card for a Benny
If you need to deal a new card to a character, because of an Edge, or they spend a benny, or whatever reason, use `card` command.
```javascript
!card Dewey
```

### Show Initiative
When you need to pick again at the initiative tracker, run the `init` command (init for initiative, not for initialize!) :
```javascript
!init
```

### New Round
`round` let's you move into next round of combat. This will remove cards from the initiative tracker, shuffle the deck if a Joker was dealt on previous round. By default, all characters are also removed from the tracker. However, if you add `+` after the command, you keep the characters and deal them new cards (applying their edges if they had).
```javascript
!rd +
```

### Add new characters
A new contendent joins the frey? Well, simply deal them a card with `di`.
```javascript
!di Scrooge
```

### Remove characters
Someone is dead or fled? You made a typo in their name? Whenever you need to remove someone from the initiative tracker, use `drop` followed by the character names.
```javascript
!drop Huey Bandits
```

Oh, you can also do that when you start a new round, by adding the character names prefixed by `-` to the `rd` command.
```javascript
!rd + -Bandits
```


------
OLD 

__**CHARACTERS category**__

**list**        Lists characters with tokens and states

**remove**      <charName1> [<charName2> ...] Removes character(s) totally. This command could be used to erase defeated extras.


__**CARDS category**__

**deal** or **dl**    [<card_count>] Secretly deals several (1 by default) cards to user's private channel

**show** or **sh**    Shows your cards, previously dealt to you by 'deal' command to current channel

**put**         Deals card and "puts it on table" (shows in current channel). 

**shuffle**        Shuffles current deck, resets secret cards dealt to all users in this channel



__**TOKENS category**__

`!give character1 [token count1] [character2 [token count2]] [...]`		Gives token(s) to character(s). 

Example: `!give Huey 2 Louie Dewey 3` - gives 2 tokens to Huey, 1 to Louie and 3 to Dewey. 


`!take character [token count] [character2 [token count2]] [...]`    Takes token(s) from character(s).  

Example: `!give Huey Louie Dewey 2` - takes 1 tokens from Huey, 1 from Louie and 2 from Dewey.


`!clear character1 [character2] [...] /all`    Clears token(s) for sprecified character or for all characters in channel


__**STATES category**__

`!state <character_name1> [+/-]<state1> [+/-][<state2>] [...]`

`!st <character_name1> [+/-]<state1> [+/-][<state2>] [...]` Sets and removes states of character.
  
  Example: `!state Huey +stunned -vul dis Dewey dis -ent Louie clear`
  
  States are Savage Worlds states: Shaken, Stunned, Entangled, Bound, Distracted and Vulnerable. You can write them in any case even like sTuNnEd. You can use abbreviations: sha, stn, ent, bnd, dis, vul.


__**BENNIES category**__

**pocket**		<characterName>	Shows character's bennies

**use**			Uses one of character's benny

**benny**		<character>	Get benny from hat and adds it to characker's pocket

**hat**		[fill]	Puts all required bennies into the hat


__**DICE category**__


Currently supported dice codes are:

`!3d6` - roll 3 6-sided dice, show sum

`!2d8!` - roll 2 'exploding' 8-sided dice, show sum. 'Exploding' means that if die come up with maximum value - it will be rolled again and summed up 

`!4d6k3` - roll 4 6-sided dice, keep 3 highest

`!6x4d6k3` - repeat 6 times: roll 4 6-sided dice, keep 3 highest 

`!2d10kl1` - roll 2 10-sided dice keep 1 lowest

`!s8` - Savage Worlds roll with d8 trait die and (default) d6 wild die

`!3s8` - Savage Worlds roll with three d8 trait dice (e.g., bursts)

`!s8w10` - Savage Worlds roll with trait die d8 and wild die d10

`!3s8w8` - Savage Worlds roll with three d8 trait dice and d8 wild die (e.g., bursts with non-default wild die)

`!s8t6` - Savage Worlds roll with trait die d8 and target number 6 (for counting successes and raises)

`!s8tr6` - Savage Worlds roll with trait die d8, target number 6, and raise step 6

`!s8t10r6` - Savage Worlds roll with trait die d8, target number 10, and raise step 6

Also SavageBot supports custom TN and counting raises from it. To do so - use the following syntax: `s6t5`. Result will look like: 

  s6t5: [11; w8] = 11 (2; TN: 5) 

This means: roll result is 11, 2 - is success with one raise, TN is self-explanatory.

This may be combined with othe roll options, like custom wild die: `!s6w8t7`.

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

`!rh    <expression_1> ... <expressionN>`    creates data for histogram: rolls dice multiple times and shows resulting distribution. 
Example: `!rh 1000x2d6` - rolls 2d6 1000 times and shows results table.
  
`!rs    [<heading_1>] <expression_1> ... [<heading_N>] <expression_N>`    rolls multiple dice and print them out sorted.
This is mostly useful for rolling initiative as a single command.
```
> !rs Huey d20 Dewey d20 Louie d20 
Dewey 14
Huey 10
Louie 5
```
