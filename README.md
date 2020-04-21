# Introduction
**Discord bot for playing table-top RPG online.**
Supports various dice-rolling, Savage Worlds initiative cards, bennies, tokens, and other RPG stuff.

## Patreon
In case you like this bot so much - you can support development via Patreon: https://www.patreon.com/savagebot

## Installation
Click on this link and [authorize bot on your server](https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0)

To know whether the bot is alive:
```
!ping
```

If that exclamation mark is an issue (conflict with another bot?), you can change it with `prefix` command followed by a symbol to use as prefix from now on:
```
!prefix #
```

`invite` command let you get this bot invite link, to share it with friends for their server.
```
!invite
```

## Help & Syntax
All commands starts with`!`.
Most commands can have their name shortened to speed things up.

To get help from a discord channel, just type `help` command:
```
!help
```

---
# Savage Worlds Dice Rolls
Let's first see the simpliest rolls for Savage Worlds Extra and Wild Cards. But, in the [Avanced](#advanced) section below, we will see more complex rolls as well as rolls for other systems, including D20 initiative.

## Simple rolls
To roll a single die (no Acing), you can use the `roll` (or `r` for short) command, or even simply use the die as command `d4`, `d6`, `d8`, `d10` or `d12`.

For example, you need to know in which direction a grenade deviates?
```
!r d12
```
or for short:
```
!d12
```

**Tips:** 
* In fact, you can just roll any die `!d20`, `!d100`, even very weird ones `!d73` if you feel like it!
* You can also directly include modifiers (after or before the roll), for example, here is a human running roll `!6+d6`.

## Trait rolls
Traits roll do Ace (if the die hits its max, you roll another one and add it, until no more die aces).
Follow the die with an `!`.

For example, lets roll a Fighting d6 for a bandit extra:
```
!d6!
```
The result will directly display the sum of the dices. You won't see each individual ones. 
Here, on a d4, we got 4, then 2:
```
> d4!: 6 = 6
```

**Tips** You can directly add modifiers to the roll, like a -2 (dim light) penalty to Shooting `!d6!-2`, or Combat Reflexes adding +2 to recover from Shaken `!d8!+2`. Untrained would be `!d4!-2`.

## Wildcard Trait rolls
Wild Cards do roll a "Wild Die" (usually a d6) next to their Trait and keep the highest of both. Use `s4`

For example, let's have Player Character Huey rolls for Persuasion:
```
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

**Tips:** Modifiers are also supported. Let's take a Wild Card untrained Trait roll: `!s4-2`.

## Damage rolls
Pretty simple, back to standard `d` syntax. Damage dice ace.

You can roll multiple dice and sum them up.
Here is a knife (Str+d4) wielding bandit with d6 strength:
```
!d6!+d4!
```
Here is a bow (2d6) wielding assassin:
```
!2d6!
```

---
# Savage Worlds Initiative

## Start a fight
To start a fight use the `fight` or `f` command:
```
!f
```
This shuffles deck and resets (clears) initiative tracker.

## Deal cards
To deal cards to one or more characters, use `di` followed by the characters' names. Those are not the discord names of the players, but really the character names or nicknames, and NPC names.
```
!di Huey Dewey Bandits Wolves
```
**Tips**: Remove spaces from their name (or they will be considered multiple characters). Keep the character names short, like one word, you might have to type them more than once.

## Quick, Level Headed, and Hesitant
For characters with such Edges/Hindrances, when dealing cards to them, follow their name with -q (for Quick), -l (for Level Headed), -i (for Improved Level Headed), or -h (for Hesistant).
```
!di Huey -q Dewey Bandits -h Assassin -i
```

**Tips**: When a player has Level Headed and Improved Level Headed, you only add `-i`.

## Tactician, Card for a Benny
If you need to deal a new card to a character, because of an Edge, or they spend a benny, or whatever reason, use `card` command.
```
!card Dewey
```

## Show Initiative
When you need to pick again at the initiative tracker, run the `init` command (init for initiative, not for initialize!) :
```
!init
```

## New Round
`round` let's you move into next round of combat. This will remove cards from the initiative tracker, shuffle the deck if a Joker was dealt on previous round. By default, all characters are also removed from the tracker. However, if you add `+` after the command, you keep the characters and deal them new cards (applying their edges if they had).
```
!rd +
```

## Add new characters
A new contendent joins the frey? Well, simply deal them a card with `di`.
```
!di Scrooge
```

## Remove characters
Someone is dead or fled? You made a typo in their name? Whenever you need to remove someone from the initiative tracker, use `drop` followed by the character names.
```
!drop Huey Bandits
```

Oh, you can also do that when you start a new round, by adding the character names prefixed by `-` to the `rd` command.
```
!rd + -Bandits
```

---
# Bennies
## Grant a Benny
To give a Benny to a player (or character), use the `!give` command.
```
!give Huey
```

To grant multiple Bennies at once, add the number of Bennies after the character name.
```
!give Huey 3
```

You can give Bennies to multiple persons at once, for example when a Joker is dealt.
This gives 2 to Huey, one to Louie, and 3 to Dewey.
```
!give Huey 2 Louie Dewey 3
```

## Spend a Benny
When a player wants to spend a Benny, the Game Master `take` it from them (or they take it away from themselves).
```
!take Huey
```

You can take away from multiple players, multiple Bennies in a single line.
```
!take Huey Louie 2 Dewey 2
```

## Who has what?
The `list` command lets you check how many Bennies each character has. (it also shows states, see [States](#states) below).
```
!list
```

Here is what the result looks like:
```
> NAME                TOKENS    STATES                             
> Huey                2                                            
> Dewey               4                                            
> Louie               2              
```

## New session
When you start a new session and want to reset all Bennies to default 3 (or more with Edges), first use the `clear` command to remove all remaining Bennies, and then, `give` each character what they deserve.

Here, our group with Huey, Louie, and Dewey, and Louie has the Luck Edge:
```
!clear
!give Huey 3 Louie 4 Dewey 3
```

## Removing characters
Once you gave a Benny to a character, it remains in the `list`, even if they reach 0 Benny. It's ok when it's a player, at some point they will regain some Benny, but for Wild Card NPCs, you might want to `remove` them from the list.
```
!remove Assassin
```

**Tips:** You can remove multiple characters at one: 
```
!remove LordDenak GoblinWarlord EliteSentinel
```

---
# Characters

## Add characters
Users often ask *how do I add players to my game?*. Well, you don't. Players (and characters) are automatically added if you deal them an initiative card, a Benny (or a state see [States](#states) below).

## Who is playing?
Use `list` to have the list of players/characters currently in the game. It shows their name, Bennies (tokens), and states.
```
!list
```

## Remove characters
Since the `list` holds players and characters, and since some characters might just have to leave (defeated, passing by npc, ...), you can `remove` characters:
```
!remove BigBadEvilBoss BigBadLieutenant
```

## Character Sheets
At the moment, Savage Bot does not manage Character Sheets. You can't roll `!persuasion` or `!fighting`.

Players or GMs usually keep the character sheets on paper in front of them, or use online sites such as [Savaged.us](http://savaged.us), or Virtual TableTop softwares with Character Sheets abilities. Whatever works for you. The simpler the better. Fast! Fun! Furious!

---
# States
In Savage Worlds, states are effects that affects a character for some time, like being Shaken, Distracted, or Vulnerable.

Savage Bot allows you to track the following states: Shaken, Stunned, Entangled, Bound, Distracted and Vulnerable.

## Apply State to Character
When a character is Shaken, or Entangled, or whatnot, use the `state` command to track it.
```
!state Huey +Vulnerable
```

**Tips:** You can use the shortened version of each state: Shaken → sha, Stunned → stn, Entangled → ent, Bound → bnd, Distracted → dis and Vulnerable → vul. You can also apply a state to multiple characters at once.
```
!state Huey +stn Dewey +dis Louie +bnd
```

## Remove State from Character
Use `state` again, with a minus sign.
```
!state Huey -vul
```

To remove all states from a character, add `clear` after their name. This removes all states from Huey:
```
!state Huey clear
```

**Tips:** You can apply and remove states to a single character at once. You omit the `+` sign, it is still considered a "add/apply". This adds Stunned and Distracted to Huey while also removing Distracted, add Distracted to Dewey but also adds Entangled, and clears everything from Louie. Yeah, we know some nasty GMs!
```
!state Huey +stunned -vul dis Dewey dis -ent Louie clear
```

## How Are You Doing?
Want to see if a character is Shaken or Distracted?
```
!list
> NAME                TOKENS    STATES                             
> Huey                1         STUNNED                            
> Assassin            2         DISTRACTED                         
> Dewey               1         SHAKEN                             
> Louie               3                                            
> Bandits             0                                    
```

---
# Advanced Savage Worlds

## Advanced Rolls

### Emotes and Narration
You roll multiple separate rolls on the same line/command and put narrative text around as you like. Note that it returns to next line after each roll:
```
The assassin shoots at Huey !s8 Damage: !2d6!+1 Bonus damage (if raise): !d6!
> The assassin shoots at Huey s8: [1; w3] = 3
> Damage: 2d6!+1: 5 + 1 + 1 = 7
> Bonus damage (if raise): d6!: 1 = 1
```

### Variable Target Number (Fighting vs Parry)
For rolls where the Target Number is not the standard 4, you can add a `t` parameter after the `s` savage roll.
For example, Huery attacks in melee a Parry 6 bandit:
```
Huey Fighting vs Bandit: !s10t6
```

The number of success and raises is calculated accordingly:
```
> Huey Fighting vs Bandit: s10t6: [9; w3] = 9 (1; TN: 6)
```

**Not yet:** You can't compare damage to toughness and read the number of wounds (yet).

### Multiple Modifiers
You don't want to add up penalties and bonuses by yourself? Let's go lazy, Savage Bot does it for you.
Melee attack with Called Shot to Head, Dim Light, Trademark Weapon, and Wild attack?
```
!s10-4-2+1+2
> s10-4-2+1+2: [7; w4] - 4 - 2 + 1 + 2 = 4 (1)
```
That was close!

### Custom Wild Die
If a character has an Edge such as Master, they get to roll a Wild Die higher than d6. You can add `w` to the Savage roll to tell which Wild Die to use.
Here is Master d12+2, using a d10 for Wild Die.
```
!s12w10+2
```

**Tips:** You can combine with specific target number.
This rolls d12+2, with a d10 for Wild Die, against Parry 8.
```
!s12w10t8+2
```

### Multi-dice
Weapons with a Rate of Fire, Frenzy, Work the Room, there are situations where you need to roll multiple Trait dice for a single action (and a single Wild Die if you are a Wild Card). 

As an extra, prefix with the number of dice to roll and a x (for *times*)
```
!2xd6!
```

As a wild card, the syntax is simpler, prefix your savage die with the number of dice to roll:
```
!2s6
```

**Tips:** This also works with Custom Wild Die and Modifiers, e.g. `!3s12w10+2`. But not (yet) with Target Numbers (Parry): `!3s6t2` (TN 2 is ignored here).

### Group Rolls
Group Rolls are used in Savage Worlds to simulate an average result for a group of extra (e.g. when they all sneak on the party, all attempt to notice something, or evaluate how well they survive their journey through the desert). It is resolved using a single Trait roll plus a standard d6 Wild Die. So simply use the `s` Savage Die!
```
!s6
```

**Tips:** Groups rolls are not used in combat. You'll have to roll each extra action one by one. However, you can roll multiple dice at once like this: `!5d6!` (5 extra rolling a d6 Trait).

### Custom Rolls
For a specific setting or house-rule you need something even more specific? Here are a few tricks that can be useful.

**Raise step other than 4**
If you need to count number of success and raises in step of 3 or 6 (instead of the standard 4):
```
!s8r6
> s8r6: [10; w1] = 10 (2; raise step: 6)
```

With specific target number:
```
!s12t8r6
> s12t8r6: [16; w1] = 16 (2; TN: 8; raise step: 6)
```

When target number and raise steps are the same:
```
!s12tr6
> s12tr6: [9; w3] = 9 (1; TN: 6; raise step: 6)
```

And with custom Wild Die too:
```
!s12w8t5r3
> s12w8t5r3: [7; w2] = 7 (1; TN: 5; raise step: 3)
```

**Limit roll results**
You will roll and sum up multiple dice, but you want the result to be not too extreme? You can put a lower and higher limit on your rolls. Here, we want the result to be between 6 and 30 (24+6).
```
!r (3d8!+d6!)[6:24+6]
```
This used in our house-rule for damage rolls for Savage Worlds.

## Dramatic Tasks
When running a Dramatic Tasks, players must collect a given amount of tokens in a set number of rounds.
To track those tokens, we will use the Benny commands (`give`, `take`, and `list`) and a virtual character, we will call... Miss Task!

### Start a Dramatic Task
Well, in case you had a previous task running, to start out fresh, start by removing MsTask from characters.
```
!remove MsTask
!list
```

**Notes:** We don't want to use `clear`, it would remove all Bennies from all players. This makes players unhappy. Not good.

### Gain Task Tokens
When players gain Task Token, deal them to MsTask.
```
!give MsTask 2
```

Same if they Snake Eye, and lose a Task Token:
```
!take MsTask 1
```

## Mass Battles
Same as with Dramatic Tasks, use the bennies commands to deal and take from two virtual characters. Let's call them TheHeroes and TheVillains (we give them names which starts the same, so that they end up next to eachother in the characters list).

### Set up a Mass Battle
Decide which side has the maximum number of tokens (10), and how many tokens the other side gets. Then deal those amounts of tokens to our mass battle avatars. In case you had a previous Mass Battle running, don't forget to remove the avatars before giving them new tokens.

```
!remove TheHeroes TheVillains
!give TheHeroes 7 TheVillains 10
```

### Morale and Casualties
At the end of each round of Mass Battles, `give` or `take` tokens from each sides.

```
!take TheVillains 2
!give TheHeroes 1
```

## Social Conflicts
Same as with Mass Battles and Dramatic Tasks, deal bennies to virtual characters to track the Social Conflict progress. Here let's call it MsInfluence.

### Start a Social Conflict
In case you already had a Social Conflict running, remove MsInfluence from the character list.
```
!remove MsInfluence
!list
```

### Gain Influence
Simply `give` Bennies to MsInfluence each time your arguments have gained you some influence.
```
!give MsInfluence 1
```

At the end of the three rounds, check how many influence MsInfluence has gained:
```
!list
```

## Deadland Bennies
Deadlands uses a pool of colored Bennies. Each color has its own uses. Characters draw Bennies at random from the pool.
This will use complete different command than regular Bennies. We assume Bennies are drawn (`benny`) from a `hat` and placed in the character's `pocket` until they `use` them. Do NOT use `clear`, `give`, or `take` for Deadlands bennies.

**Trivia:** Savage Bot started out aimed for Deadlands players. That's why the `benny` command is for Deadlands Bennies and not for standard Savage Worlds bennies.

Those Deadlands Bennies commands might get a rework to put them more in line with standard Benny features.

### New Deadlands session
Fill your hat with 20 white Bennies, 10 red Bennies, 5 blue Bennies (and no Golden ones):
```
!hat fill
```

### Grant Deadlands Bennies
Use `benny` to draw a random Benny from the hand and deal it to a single character.
```
!benny Huey
```

**Note:** as of now, there is no way to draw multiple Deadlands Bennies at once, and no way to deal to multiple players at once.

### Spend Deadlands Bennies
The `use` command lets you spend a Benny of a given color from a character.
```
!use white Huey
```

### What's in your pocket?
To check a character's pocket for Deadlands Bennies, use `pocket`.
```
!pocket Huey
```

**Note:** at the moment, there is no way to check for all characters' pockets at once.

### Reward a Golden Benny
While the hat seems able to have golden bennies, there is no feature right now to add Golden Bennies to the pot, or give a Golden Benny to a player.

However, if you don't use the standard Bennies commands, you could use them to stand for Golden Bennies.

### What's left in the hat?
Using `hat` command without the `fill` argument, you simply check how many Bennies of each color are left in the hat.
```
!hat
```

---
# Other Systems

## West End Games D6 System
One d6 is Wild: it explodes on 6 and subtracts highest number on 1.

```
!3w
> 3w: w11 + 6 + 4 = 21
```

## Worlds of Darkness
In World of Darkness or Lady Blackbird, you roll dices and count the number of success, that is how many did hit a given target.

Roll 6d6, successes on 4+:
```
!6d6s4
> 6d6s4: [successes(3): 6, 6, 4; rest: 2, 1, 1] = 3
```

Failures removes from number of successes:
Roll 12d10, success on 7+, failure on 1.
```
!12d10f1s7
> 12d10f1s7: [successes(3): 10, 9, 7; failures(1): 1; rest: 6, 5, 5, 5, 5, 4, 3, 3] = 2
```

Same with exploding (open-ended, acing) dice.
Roll 28d6, success on 10+, failture on 1-.
```
!28d6!s10f1
> 28d6!s10f1: [successes(7): 15, 14, 11, 11, 10, 10, 10; failures(5): 1, 1, 1, 1, 1; rest: 9, 9, 7, 5, 5, 4, 4, 4, 4, 3, 3, 2, 2, 2, 2, 2] = 2
```

## Coriolis
Randomly pick in a 6x6 table, roll two d6, for cols and rows (displayed as 11, 25, 66...).
```
!d66
```

## Fate/Fudge
Rolls four Fudge/FATE dice.
Roll 4 Fate dice:
```
!4dF
```

## D&D
Roll with Advantage (roll 2, keep highest).
Roll a d20 with advantage and +2 modifier:
```
!d20adv+2
```

Roll with Disadvantage (roll 2, keep lowest).
```
!d20dis+2
```

To roll initiative, give character names and their initiative dice, this will sort them from highest to smallest.
```
!rs Huey d20 Dewey d20-2 Louie d20+4
> Louie: d20+4: 18 + 4 = 22
> Huey: d20: 15
> Dewey: d20-2: 7 - 2 = 5
```

## Carcosa
First d20 is rolled to determine size of dice - then roll that dice.
```
!dC
```

---
# Other tricks

## Keep Highest roll
Roll 4 d6 and keep the three best results:
```
!4d6k3
```

You can pretend to "roll with advantage" any dice. Yeah, this works with any die, not just d20. It will roll one more dice than asked and exclude the lowest from the total. 
```
!3d6adv
> 3d6adv: 4 + 6 + 6 + 6 = 18
```

## Keep Lowest
Roll five d10 and keep the lowest two.
```
!5d10kl2
> 5d10kl2: 5 + 7 + 8 + 9 + 9 = 12
```

You can also "roll with disadvantage" any dice. It will roll one more dice and exclude the highest result.
```
!3d8dis
> 3d8dis: 3 + 4 + 6 + 8 = 13
```

## Run multiple times the same roll
You can repeat any roll any number of times.
Here, roll 4d6 and keep the 3 highest, repeat 6 times.
```
!6x4d6k3
> 6x4d6k3: 
> 1: 4d6k3: 1 + 3 + 3 + 6 = 12
> 2: 4d6k3: 2 + 2 + 5 + 6 = 13
> 3: 4d6k3: 1 + 2 + 3 + 5 = 10
> 4: 4d6k3: 1 + 2 + 3 + 4 = 9
> 5: 4d6k3: 2 + 4 + 4 + 5 = 13
> 6: 4d6k3: 1 + 2 + 3 + 4 = 9
```

This works with pretty much any roll.
```
!20x4dF
!10xs8t7
```

## Roll Statistics
Want to check out the probabilities of a given roll?
Run it high number of times and have each possible result display how many times it went out.
Here we will run 1000 times 2d6 and `rh` will tell us how many ended up with a 2, a 7, a 12...
```
!rh 1000x2d6
> 2d6:
> 2: 2d6: 22;
> 3: 2d6: 61;
> 4: 2d6: 69;
> 5: 2d6: 119;
> 6: 2d6: 142;
> 7: 2d6: 168;
> 8: 2d6: 115;
> 9: 2d6: 121;
> 10: 2d6: 97;
> 11: 2d6: 53;
> 12: 2d6: 33;
```

**Tips:** Since the number of rolls we called is a factor of 100, it's easy to get percentages of. (22 → 2.2%, 119 → 11.9%)

## Cards
Here are a few commands to manipulate a standard deck of 54 cards.
This feature is pretty old and quite limited. It might get a rework later on.

### Deal Cards
`deal` or `dl` deals cards in secret to you. Each player must use this command themselves. You can't deal to another person. The cards dealt are sent by direct message to you.

This deals a hand of 5 cards:
```
!dl 5
```

### What's my Hand?
`show` or `sh` displays your current hand of cards.
```
!show
> 3 :spades: Q :hearts: 2 :hearts: 10 :spades: 4 :hearts:
```

### Draw to the Table
`put` does draw a card from the deck and put it on the table for everyone to see.

This draws three cards, and drop them on the table.
```
!put 3
```

### Shuffle
Grabs every cards back and shuffle into a new fresh deck.
```
!shuffle
```
