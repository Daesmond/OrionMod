Updated as of June 10, 2017

 

I'm just new in minecraft and it's modding and I'm having problem finding working mods that I need thus I'll create one :D

 

Now I finally made a successful GuiScreen *YEY!*
Also implemented SimpleNetworkWrapper for triggering Logon Screen
And implemented command for setting password
Fix implementation on proxy thus config file creations don't occur on client side.
I'm also looking for a way to prevent fire on protected blocks.  So far.... NO LUCK!


So far my mod functions are.

on chat:
setglobalspawn  - Set a global spawn (for new and dead-respawning players).  It will set on the position of player.
setmyspawn       - This will set the spawn for current player thus ignoring global spawn
protect coord x1,y1,z1 x2,y2,z2 [player]    -  Protect coordinates. Only ops can use this.
unprotect coord x1,y1,z1 x2,y2,z2 [player]  - Unprotect coordinates Only ops can use this.

 

Command:
/setpass password [user] - Set password for use with LAN authentication. Only op and console can use
                                               [user] parameter.

 

Added Option:
config/Orion/OrionConfig.json
  "CONFIG":{"CreepersBlows":"false"}  - By default sets creepers not to explode.  Just make it true so
                                        creepers can explode again.
config/Orion/users.json               - Authentication file use by /setpass and login GUI
config/Orion/Locked.json              - Lock chests, doors, and buttons save file
 

Deprecated:
protection on      - Enable block protection right clicks .   RightClick with sword will disable block protection. RightClick with PickAxe will enable block protection.
protection off     - Disable block protection  right clicks.
protection save  - Save config file (in case it won't save for some reason)



GUI:

Logon Screen  -  Use for LAN authentication.  When a player/user use setpass it will show this gui.
                              Currently issue/limitation
                              1) Mouse cursor doesn't show but enter key will submit authentication

Items:

stonewand      - unprotect block (right-click or lshift-right-click).  Only ops can unprotect other player(s)
                 protected blocks.
ironwand       - protect block (right-click or lshift-right-click)
goldwand       - no use yet
diamondwand    - no use yet (gui password testing)
Pearl Orb      - For levitation or flying.  AllowFlight should be set true on server.properties to
                 work.  Should be equipped on offhand (shield slot) then press space twice to levitate.
                 LShift to descend.  Side effects,  Immune to fall and drowning
orionkey       - This will lock chest, doors and buttons.  Even if the player put his own button as long as the
                 door itself is locked then it won't open.
 

Thanks for Igrek for the wand textures

 

Fix Limitation

1) Config file for protection is save upon server stop.  I need to hook it on a watchdog, create a timer, or add save command.
   * Protection save file will auto save within 1200 ticks (If I am right)
   * typing protection save will force save the file
2) Explosions canceled when a protected block is included.  Need to create an algorithm to exclude protected blocks and behind them
3) No password retried implemented for Authentication Screen
4) No ESC handler for Authentication Screen thus it will shutdown the client for not authenticating


Limitation:
1) Does not lock doors when locking only button thus you need the lock (even the iron doors)

Known Issue:

1) Race condition on client side that crashes it (GuiScreen.drawDefaultBackground()).  Just run MC Client and try again.

Fixed Known Issue:
1) Bad coding for password input (TSK lazy me)
 

Todo updated:

1) Players can set their own spawn point by typing setmyspawn
2) New players will use default spawn set by op/admin user
3) Enable a per player permissions on block protection thus only owners can remove protection.
     This is now implemented.  Also ops can unprotect other players protected blocks.
4) Key to protect chests, doors and buttons that players protected. Owners doesn't need keys to open once it is locked.

Todo:

1) Need to implement nbtags so a person who locked them can give keys to other players 
2) Implement commands instead of chat
3) Put protection file on a sqlite database somehow so not to hog memory
4) Highlight blocks that are protected (I'm now sure how to implement yet)


Abandoned Todo:

1) Auto-protect function when placing blocks from main hand (I may have another idea for this)
2) Create algorithm to only exclude protected blocks and what's behind those blocks




Update as of June 7, 2017


I created a github repository for the mod.
The tree/Daesmond would be my working tag/branch and master would be the last release tag
I already fix the lousy password input on the Logon Screen on my last update to the branch.
I also include an ESC handler for the Logon Screen thus it won't close when ESC is pressed.

 
I am currently researching about the use of nbt tag so I can customize keys for locking/unlocking doors, chests and buttons.

