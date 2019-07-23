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

# Credits

Built with [IntelliJ IDEA](https://www.jetbrains.com/idea/)

Uses [Dagger](https://github.com/google/dagger) and the
[Checker Framework](https://checkerframework.org/)
