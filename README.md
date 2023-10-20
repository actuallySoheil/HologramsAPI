# WixiHologramsAPI
A simple and lightweight API for creating holograms for Minecraft plugins.

### How to add it to your project?
This code uses NMS (net.minecraft.server). If you don't have it, please get it on [this site](https://www.spigotmc.org/wiki/buildtools/).
<br>**More versions will be added soon, but for now, this API only works on 1.8.8 (v1_8_R3)**
```xml
<repositories>
        
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
     </repository>
        
</repositories>

<dependencies>
        
    <!-- WixiHologramsAPI -->
    <dependency>
        <groupId>com.github.WixiDev</groupId>
        <artifactId>HologramsAPI</artifactId>
        <version>LATEST_COMMIT_TAG</version>
    <dependency>

    <!-- NMS (1.8.8) -->
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot</artifactId>
        <version>1.8.8-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.28</version>
        <scope>provided</scope>
    </dependency>
            
</dependencies>
```

### How to use?
It's so simple. create an instance of HologramManager in your Main class for only once:
```java
public class YourPlugin extends JavaPlugin {

    @Getter
    private HologramManager hologramManager;
    
    @Override
    public void onEnable() {
        this.hologramManager = new HologramManager(this);
    }
    
}
```
Now, once you have access to it, use ``newHologram()`` method to create a hologram. here is an example:
```java
@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
    if (!(sender instanceof Player player)) return true;

    val hologram = this.hologramManager.newHologram("your-hologram-name", player.getLocation(),
            "&aLine 1",
            "&cLine 2!!!"
    );

    return true;
}
```
You can also create a variable to use more methods. For instance, let's create a page and show that page to the player:
```java
val hologram = this.hologramManager.newHologram(player.getLocation(),
    "&aLine 1",
    "&cLine 2!!!"
);
hologram.addPage(
    "&eLine 1 from page 2!"
);
hologram.showPageToPlayer(player, 2);
```

### How to use the event?
Use ``HologramClickEvent`` class to listen for the click events on hologram! Want an example? here you go:
```java
public class YourHologramListener implements Listener {

    @EventHandler
    private void onHologramClick(HologramClickEvent event) {

        val hologram = event.getHologram();
        val player = event.getClicker();
        
        if (hologram.getName().equals("your-hologram-name")) {
            player.sendMessage("Hi! You've successfully clicked on this hologram!");
            hologram.hideFromPlayer(player);
        }

    }

}
```
In this listener, we get the hologram name and check it if the hologram has the name "your-hologram-name", and after that, send a message to the player, and then hide it from the player.

### More Information
There are so many other methods to use! Here is a summary of all methods (also API has java docs to get more information about the every method):
<br>**NOTE: Don't use ``spawn()`` or ``create()`` methods (API handles itself) unless you know what you are doing.**
<br>**And also remove the hologram by using ``HologramManager`` class, to prevent any bugs or issues.**

#### WixiHologram Interface
- ``spawn(Location location, String... lines)`` - Create and spawn the hologram.
- ``remove()`` - Remove the hologram from all the players.
- ``addPage(String... lines)`` - Add page to the current hologram.
- ``removePage(int index)`` - Remove pagae from that hologram.
- ``showPage(int page)`` - Show this page to all online players.
- ``showPageToPlayer(Player player, int page)`` - Show this page to only this player.
- ``isSpawned()`` - Check if the hologram is spawned.
- ``getPageByNumber(int number)`` - Get the page by number.
- ``getPages()`` - Get all the pages from this hologram.
- ``getCurrentPage()`` - Get the current page that hologram is in.
- ``getPagesCount()`` - Get the amount of the pages.
- ``getName()`` - Get the hologram name.
- ``getLocation()`` - Get main hologram location that was spawned.

#### WixiHologramLine
This interface is about the every lines of the Hologram. (Every line has an interface to access directly to the line)
- ``create(Location location, String line)`` - Create a hologram line
- ``remove()`` - Remove the hologram line.
- ``show()`` - Show the hologram line.
- ``showToPlayer(Player player)`` - Show the hologram line only to this player.
- ``hideFromPlayer(Player player)`` - Hide the hologram line only from this player.
- ``setLine(String line)`` - Update and set the line of this hologram line for all the online players.
- ``setLineForPlayer(Player player, String line)`` - Update and set the line of this hologram line only for this player.
- ``getLine()`` - Get line of the hologram line.
- ``getLocation()`` - Get the location of the line.

#### WixiHologramPage
This interface is about pages of the hologram. So, every hologram has a page (even a single page).
- ``getPage()`` - Get the current page.
- ``getLines()`` - Get the lines of this page.
- ``getHologramLineByNumber(int lineNumber)`` - Get the hologramLine by line number.
- ``getHologramLines()`` - Get all the lines. (List of WixiHologramLine)


### Found it Useful?
Consider giving a star! <3
