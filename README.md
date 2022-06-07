# PokerBOT - Discord Poker Texas Hold'em bot

Play Texas Hold'em Poker on your discord server with your friends! 

# How to run bot?

1. Go to https://discord.com/developers/applications and new create application and then add a new bot in "Bot" tab.
2. In "Privileged Gateway Intents" check both "PRESENCE INTENT" and "SERVER MEMBERS INTENT" options.
3. Generate token which will be used to connect application with discord.
4. Clone this repository and create .env file in root directory of the project then paste your token:

```
TOKEN=<paste your token>
```

5. Add bot to a server and run application. **Well done!** You can start playing texas hold'em poker with your friends. Type **!help** to get command list.

# Command List
 
 ```
!help - get command list  
!init - create table (init game)  
!join - join table  
!start - start the game  
!bet - show current bet  
!rank - show your composition in privvate message
!fold - fold
!call - call
!raise <value> - raise current bet
!check - check
!rules - show rules
!hands - show poker composition hierarchy
```

# Authors

* Jakub Czarnecki
* Filip Grzechnik
* Michał Piotrowski
* Damian Wasilewski
* Radosław Zyzik
