# Lia's Better Offhand
A client-side Fabric mod for Minecraft 26.1.2 that prevents accidental offhand item activation while using your main hand.
## What it does
Ever been eating food and then suddenly half a stack of fireworks has hit the cieling? Or suddenly zoomed in with your spyglass? Or perhaps you were throwing snowballs at a friend and didn't appreciate throwing your shield up when you ran out?

In vanilla, if you hold right click to use an item in your main hand, like eating food, the offhand item will activate as soon as the main hand action completes, even if you're still holding the button down. This mod blocks the offhand from activating until you release and re-press right click.

This applies to:
- **Hold right click items:** food, potions, crossbows, shields, spyglass, trident, spear, goat horn, and any other item with a use animation
- **Instant use items:** snowballs, eggs, ender pearls, splash potions, fireworks, and any other item that consumes from a stack on use
- **Any items that break while being used**

Known limitations:
- Instant use items detection (e.g. throwing the last snowball in a stack) relies on the stack size changing. If a mod adds an instant-use item that doesn't reduce stack size, it may not be caught.

## About
This is the first Minecraft mod I've ever made, which was initially just for personal use on the [Immortal MC server](https://www.immortalmc.net/) where we have a fully generated [voxy](https://modrinth.com/mod/voxy) file that I love using the spyglass with (in my offhand). If you're looking for a small, tight-knit, vanilla survival (with a couple pluigns) come check us out!

For full disclosure, a self-hosted AI model was used to assist in developing this, since I've never created a mod before and just wanted a simple fix for a tiny issue I was having which I couldn't find any maintained mods for. I tried to do as much coding as I could myself but most of my coding knowledge is in Python, and Java is a big jump lol. A lot of the tasks I set it were debugging and optimisation. Since this AI model was self-hosted on my own GPU no lakes were drained 🙂, it just made my PC go brr which drew about as much wattage as Cities: Skylines II normally does, although maybe it was less lol.

## Contributing
Contributions are more than welcome! Please make this better! If you find anything that could be improved, whether that be a bug or a new feature, feel free to open an issue or submit a pull request. I'm sure I've probably missed some edge cases. If I'm unable to maintain this mod for newer versions of Minecraft, I'm happy for anyone to fork and continue it, just make sure to follow the GPL-3.0 licence 🏳️‍🌈🏳️‍⚧️
