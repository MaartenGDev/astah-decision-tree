# Astah ActivityDiagram parser
This Astah plugin can find the following types of paths:
- Initial -> Decision
- Decision -> Decision
- Decision -> Final
- Initial -> Final

Apart from finding paths the plugin can also find different flows(scenarios) that go from the initial to final node in the activity diagram.

## Installation
1. [Download](https://github.com/MaartenGDev/astah-decision-tree/raw/master/target/pathfinder-1.0-SNAPSHOT.jar) the plugin jar
2. Copy the "**pathfinder-1.0-SNAPSHOT.jar**" to the "plugins" folder in the Astah install folder.
3. Launch Astah and go to [Help] - [Plugin List].

Further help: [Astah help](http://astah.net/tutorial/plug-ins/plugin_tutorial_en/html/installplugin.html)

## Demo
![Astah Tree](./astah_tree.gif)

## License
MIT