# Wolf Command - Serious Taming #
## Latest: 1.6 ##

This plugin allows users to command their wolves better than basic Minecraft. It gives players the ability to tell all their wolves to sit/stand as well as attack monsters at range by swinging a stick at them. 
When left-click with a stick at a monster within 40 blocks, your wolves will target the selected mob and start attacking. Right clicking will cancel the targeting.   

#### <font color='red'>  _VERSION 1.4+ NOTE_ </font> ####
Beginning from version 1.4, WolfCommand is using the native setTarget() for setting the wolves attack target. The current Bukkit version's function **DOES NOT WORK**.  In order to use the current WolfCommand plugin, you must modify your Bukkit version with [2 lines of patches](https://github.com/Bukkit/CraftBukkit/pull/1293): [Patch 1](https://github.com/InfernoGames/CraftBukkit/commit/7a49d3d87331e264920b84a39056f1b2b6cdd59e) [Patch 2](https://github.com/InfernoGames/CraftBukkit/commit/a12d2bef2744df4bfeafbe8f9e6b42586c6149fd) 

-----  

### Features ###
- Full stand / sit control over your tamed wolves
- Long range attacking

### To Do ###
- Add configuration file to allow/disallow all options of this plugin
- Disable damaging wolves with trigger stick
- Change default wolf collar color w/ dye
- Command to untame wolves

### Commands ###
- **/wolf stand**: Sets wolves to standing
- **/wolf sit**: Sets wolves to sitting

### Long Range Attacking ###
- Left clicking a mob within 40 blocks, with a stick, will set that mob as the active target for all standing tamed wolves!
- Right clicking will cancel active targeting for all wolves

### External Links ###
- [Bukkit Project](http://dev.bukkit.org/bukkit-plugins/wolfcommand/ 'Bukkit Project Page')
- [GitHub Repo](https://github.com/puppyize/WolfCommand 'GitHub Repository')
- [Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=VBMY8UXSFDX5E 'Fund the Plugin')

-----

_**Developer Note**_: I _am_ accepting "Enhancements" for the project. If you have a suggestion to better this plugin, or feel it's missing something, please fill out a ticket with the 'enhancement' tag and I'll see what I can do about it. **PLEASE REPORT ALL BUGS!**