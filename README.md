# `lagger`

`lagger` originally started off as a tool simply to lag the
server's main thread for a certain amount of time to test
out watchdog threads and the effects of server lag. I've
added a bunch of other stuff to it now, and it'll probably
continue to grow in the future. It's more or less a
debugging tool for inspecting the internal workings of the
server.

Not really something production quality because it's just a
tool. Feel free to look around, the code is high quality and
I've done some cool module and dependency injection stuff
I suppose.

# Building

``` shell
git clone https://github.com/AgentTroll/lagger.git
cd lagger
mvn clean install
```

Requires Maven and Spigot API and server 1.8.8 installed
locally.

# Usage

See `plugin.yml` for commands.

# Features

- `/pause` - pauses the server for a given number of seconds
- `/psniff` - packet sniffer, allows one to look at the
packets sent to a given player and see where in the source
the packet was sent from and inspect the contents of the
packet. Packet filtering is opt-out; all packets are
sniffed unless you filter them out
- `/esniff` - event sniffer, allows one to look at the
events being dispatched and who calls the event and inspect
the contents of the event. Unlike `/psniff`, event sniffers
are opt-in, which means that no events are sniffed unless
you add them to the filter
- `/chunk` - displays chunk stats, entities and tiles
loaded, as well as player location
- `/ci` - clears your inventory
- `/runas` - run a command as a different online player
- `/copyplugins` - run a copy.sh file in the server's world
container to copy plugins to the plugin folder and reloads
the entire server, optionally removing files as specified
- `/runterm` - runs a command as the server terminal/
console
- `/hurt` - hurts you or a given player (negative numbers
allowed to heal), or the same for your hunger bar
- `/debugmode` - enters debug mode, which freezes the
server time, disables damage to you, prevents mob
targeting, etc. to make debugging easier
- `/getitem` - Get an item of the given type with an optional amount

Optional commands (enabled through configuration):

- `/ohi` - opens an inventory containing the skull of a
given player
- `setslot` - fakes an item packet for the currently held
slot
- `/lca` - loads a chunk somewhere asynchronously (if
supported)
- `/sas` - spawns an armor stand with velocity that passes
through blocks

# Things I'm Considering

- Since this plugin was meant to be standalone, it would be
nice to have a `/heal` like EssentialsX

# Credits

Built with [IntelliJ IDEA](https://www.jetbrains.com/idea/)

Uses [Dagger](https://github.com/google/dagger).
