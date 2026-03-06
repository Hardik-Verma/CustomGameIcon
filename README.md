**CustomGameIcon**

CustomGameIcon is a lightweight Fabric client mod that allows you to change the Minecraft taskbar icon using your own custom images.

Instead of the default Minecraft icon, you can place any PNG image in the config folder and the game will automatically use it as the window icon.

Supports any resolution — the mod automatically resizes images so they work perfectly with the taskbar.


**Features**

• Custom taskbar icons using PNG files  
• Automatic image resizing (any resolution supported)  
• Random icon mode  
• Set icon using Minecraft player heads  
• Reload icons without restarting the game  
• In-game commands to control the mod  
• Lightweight and fully client-side  
• Works with Fabric


**Installation**

1. Install Fabric Loader for your Minecraft version.
2. Download the mod from Modrinth.
3. Place the mod JAR in your `mods` folder.
4. Launch Minecraft.


**Usage**

After launching the game, a folder will be created automatically:

config/CustomGameIcon

Place any `.png` image inside this folder.

Example:

config/CustomGameIcon/myicon.png

Restart the game or run the command:

/customgameicon reload

The new icon will be applied to the Minecraft window and taskbar.


**Commands**

/customgameicon help  
Shows all available commands.

/customgameicon reload  
Reloads icons from the config folder without restarting the game.

/customgameicon random on  
Randomly selects an icon from the config folder.

/customgameicon random off  
Disables random icon mode.

/customgameicon seticon <player>  
Uses a Minecraft player's head as the game icon.

/customgameicon clear  
Restores the default Minecraft icon (requires restart).


**Links**

Modrinth  
https://modrinth.com/mod/customgameicon

GitHub  
https://github.com/Hardik-Verma/CustomGameIcon


**Compatibility**

Minecraft versions:  
1.21+ (Fabric)

Works with most Fabric client setups and modpacks.


**Author**

_Pheonix
