# Velocity Vortex Live Scoring

The goal of this project is to create a livescoring android app that can be used by game officials to keep track of the score as the game progresses.

## User Manual

The main menu allows you to select the scoring type you will be tracking for the match. Note that referees may not toggle between scoring types in the middle of a match (it resets the score!), therefore a total of **three** referees is required to score live play: two vortex scorers and one fixed scorer.

<p align="center">
  <img src="https://raw.githubusercontent.com/jhryniw/VVLiveScore/master/docs/images/vortex_setup.png"/>
  <img src="https://raw.githubusercontent.com/jhryniw/VVLiveScore/master/docs/images/fixed_setup.png"/>
</p>

Click on the *Scoring Type* and *Alliance* buttons to toggle between the different scoring modes. If more than one referee is using the same scoring mode, their scores will overwrite eachother!

### Vortex Scoring
One Vortex Scorer is required for each alliance. As a vortex scorer, you can update scores for both the centre and corner vortexes for both the Autonomous and Teleop control periods. **Important:** you must switch between the autonomous and teleop periods by **long-clicking** on the OpMode button at the bottom of the screen to specify the opmode you are tracking. 

<p align="center">
  <img src="https://raw.githubusercontent.com/jhryniw/VVLiveScore/master/docs/images/vortex_activity.png"/>
</p>

Clicking on the *{Vortex type} +/-* buttons increments and decrements their respective counts for the selected opmode. These counts are tracked just below the scoring buttons, and are internally converted into the appropriate score to be published to the website.

To reset the score, long-click on any of the decrement buttons and accept the resulting prompt.

### Fixed Scoring
Fixed Scoring includes the Beacons, Cap Ball and Robot Parking. 

<p align="center">
  <img src="https://raw.githubusercontent.com/jhryniw/VVLiveScore/master/docs/images/fixed_activity.png"/>
</p>

In Autonomous, the top four buttons score the robot parking scores for the match. They will toggle between the states of *Not Parked*, *Partially Parked* and *Fully Parked*. Assign one of these buttons per robot. The next two buttons track the cap ball score, essentially if it has stayed (*Not on Floor*) or has been knocked off (*Touching Floor*) the center. Finally, the bottom four buttons represent the four beacons. They start off as unclaimed (grey) but once toggled their ownership will switch between the alliances. Long-clicking on the beacon buttons will unclaim them in case you make a mistake.

Once the Autonomous Period scoring is complete, long-click on the OpMode button in the middle to toggle to the TeleOp period. **Once this is done you may not go back to Autonomous without resetting the score.**

<p align="center">
  <img src="https://raw.githubusercontent.com/jhryniw/VVLiveScore/master/docs/images/fixed_activity_teleop.png"/>
</p>

You can remark that the screen is not all that different from the Autonomous period. Beacon states are automatically carried over from  the Autonomous period and applied onto the teleop period (now at only 10 points per claimed beacon). There are no parking scores in TeleOp so those buttons do not exist. However, for the end-game, the cap ball buttons have additional states: *On floor*, *Off floor*, *Above 30"* and *Capped*.

**Returning from TeleOp to Autonomous mode will reset the score!** This will show up on the website immediately, so please follow the guidelines detailed in the "Resetting" section.  

### Resetting for the next match
Each referee is responsible for resetting the score for his/her scoring mode. To allow the audience to get maximum enjoyment out of the livescoring app and analyze the final game score, it is recommended that referees coordinate their restes and do not reset their scores until just before starting the next match. Any reset from a referee will be immediately reflected on the website, so if only one or two referees reset it will not be obvious who won and may cause confusion among the audience and teams!

Depending on the scoring type there are different methods of resetting:
If Vortex Scoring, long-click on any of the decrement buttons and accept the resulting prompt.
If Fixed Scoring, returning from TeleOp to Autonomous will reset the score.

In all cases, touching the back button and returning into your scoring mode from the main menu also resets the score.
