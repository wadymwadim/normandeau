# Remarks

In these instructions, terminology discribed in the paper is used liberally.

# Loading and Removing Covers 

To load a cover, select the desired angles you would like to see the cover of. Note: To display the 112.2-112.3 cover and 'All' cover, you must have a machine with at least 10GB FREE RAM.

To remove a cover and clear any sections, press the 'Clear' button.

# Navigation

You can pan around the viewer by clicking and dragging in the map. 

In the menu, there are 4 radio buttons that can be chosen. 'Select' will give information about whatever you clicked on. See the next section of instruction for this. 'Magnify' will zoom to the point you selected. The point you clicked will now be the center of the screen. 'Demagnify' is then of course the same, but zooming out. 'Center' will center the screen on wherever you clicked. The 'Zoom Scale' box is the amount by which to magnify and demagnify by. 

To undo or redo navigation actions, the 'Backward' and 'Forward' buttons.

# Getting Information about Code Sequences

Click on a cover square or the grey infinite pattern region while having the 'Select' radio button chosen. This will cause an info box to come up on the left side, underneath the controls. Here you will  find the code sequence that covers that square or section of the infinite pattern. If the square is covered by a triple, there will of course be three code sequences listed. When clicking on the infinite pattern, there will again be three code sequences listed. These are the stable element of the infinite pattern which you have clicked on, and the two unstable elements which surround it.

You will also see a checkbox with the lable 'Draw' next to each code sequence. Clicking this will cause the "exact" region corresponding to the code sequence to be drawn on screen. Clicking it again will cause the bounding polygon to be drawn. You can click it a final time to reset and have no image drawn. Reminder: this region is only as exact as a computer can draw on screen, and is for visual demonstration  purposes only. The proof is not dependant on this image. Also note that this  drawing process will make the program lag a bit.

To see more detailed info about a code sequence, you can press the button  labeled 'Info'. After entering the code sequence at the top, you will see the bounding polygon and equations of exact region. If the code is unstable, there will also be the equation of the line of triangles which work.

# Viewing the Structure of the Cover

It may be of intrest to you to see how which squares are covered by each code sequence. For this reason, you may select a color after clicking on a square. This will change that square and all other squares that are covered by the same code sequence to that color. Additionally, you can turn the 'Cycle' check box on. This will cause the colors of the cover squares to change as you click on them, with different colors every time. This will allow you to see how the cover is composed. 

So far, it is not known whether every periodic path will define a connected region. However, so far every region we have encountered has been star-shaped. You might notice that some sets of cover squares with the same code sequence are disjoint, but this is not because the sequence's region is disjoint. Bacause there is some overlap in out set of codes, a because a different code sequence simply filled in some cover squares that would have been filled by another first, causeing it to appear disjoint.


# Checking the Cover

'Check Cover' will run the prover on the cover that is currently loaded. The way this prover works is detalied in the paper. Due to the size of the proof, in order to get a full grasp of what is being done by the prover, we recommend that you read the code in (relevant file names).

Checking the cover can take a long time, depending on your machine. The first cover, 100-105, will run in less than 5 minutes. It is not recommended that you run any of the other ones unless you are prepared to let it run for quite awhile. The 105-110 cover will take several hours on a standard computer. The rest would take days.







