# big-project-dahl

# What is our project?
This project is a music composition creator. When running the program, a list of instruments are listed on the left side, and the composition panel is on the right. You can add, select, move, and delete notes in the composition panel, group and ungroup notes, and choose your instruments in the instrument panel. When you're ready to listen to what you've created, you can play and pause your piece. Each space between lines represents a pitch, and the differences between adjacent pitches is a half step. 

# How does our project work?
## To change instruments:
Use the instruments panel located on the left side of the window to select which instrument you want to write for. You can write for all of the available instruments at once.
## To add a note:
Click anywhere in the composition panel to create a new note. The note will look like a colored rectangle with a bold black outline. The color of the note corresponds to the color of each instrument's button. The bold black outline indicates that the note is currently selected. If an instrument was not previously selected, the note will be played with the piano.
## To select a single note:
Simply click on any individual note to de-select any other notes and select the current note. 
## To select multiple notes:
There are three ways to select multiple notes.
1. To create a new selected note without de-selecting any other notes, hold the control key while creating a new note. This will select the new note, and will keep all prior notes selected as well.
2. Hold the control key while selecting notes to retain prior selections. 
3. Click and drag in the composition panel (do not start the click on a note). A selection box will appear that will automatically select any notes that intersect or are within the selection box.
## To select all notes:
In the menu bar, navigate to Edit > Select All. Alternatively, use the keyboard shortcut Ctrl+A.
## To delete selected notes:
Press the backspace key on your computer, or navigate to Edit > Delete.
## To create a group of notes:
Select all of the notes that you wish to group, then navigate to Edit > Group. Groups can be stacked. Selecting any note in a group will cause the entire group to be selected. Selected groups will have a black dashed outline; unselected groups will have a gray dashed outline.
## To ungroup a group:
Select the group that you wish to ungroup, then navigate to Edit > Ungroup. This will only ungroup the most recent group made.
## To copy a selection:
Either use the command "Ctrl-C" or navigate to Edit > Copy.
## To paste a selection: 
Either use the command "Ctrl-P" or navigate to Edit > Paste.
## Edit note length:
**This will modify the note lengths of all selected notes, by the amount that you drag it.*
Click the far right side of any selected note, and drag to the right or left to make the note longer or shorter. There is a minimum note size; you can't get rid of any notes doing this.
## Move selected notes:
Click in the middle of any selected note and drag them to where you want them to be. The notes will snap into place when you release the mouse. Notes can move in any direction.
## Play the composition:
In the menu bar, navigate to Actions > Play, or use the keyboard command Ctrl+P.
## Stop the composition:
There are multiple ways to stop the composition.
1. In the menu bar, navigate to Actions > Stop.
2. Use the keyboard command Ctrl+S.
3. Click anywhere in the composition panel (clicking and dragging does nothing).
## Change Snapping
To change how your notes will land in the composition panel, navigate to Options > Change Settings. Then you can enter a number between 1 and 500, and this will set the number of pixels between each line that the notes will snap to. This will impact both where a note starts and where a note ends.
## Save your composition
Navigate to File > Save, or use the keyboard command "Ctrl-S". To save the current composition as a new file, navigate to File > Save As... and type in a name for your file. 
## Create a new composition
Navigate to File > New, or press Ctrl-N. If you are currently working on an unsaved composition, it will ask if you want to save your composition, or cancel the interaction. If your composition is already saved, this pop-up will not appear. 
## Open a pre-existing composition
Navigate to File > Open... and choose the correct file.

## UML Diagram:

Link to our diagram: https://www.figma.com/file/Vq13xgEe96Ok88kqp0LHJD/DAHL-UML?type=design&node-id=0%3A1&t=I4F7zaED3XC90tqm-1

# Who uses our project?
Our project is designed for people interested in composition, but maybe don't know how to read standard Western sheet music.

# What is our project's goal?
Our project's goal is to create an accessible way to create music for people without any formal music training.

# Contributors:
Andrew Kuhlken, Diego Quispe Vilcahuaman, Henry Young, LJ Friedman
