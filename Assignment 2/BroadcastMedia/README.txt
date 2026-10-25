BroadcastMedia - CSE 489 Assignment 2 (Kotlin, minSdk 21)

Single-activity app with a navigation drawer (Jetpack Navigation component, fragments, ViewBinding).

Drawer menu:
  A. Broadcast Receiver
       1. BroadcastSelectFragment : spinner (Custom broadcast receiver / System battery notification receiver) + Next button
       2. custom  -> CustomInputFragment : plain-text input, passes the text on (Next)
          battery -> BatteryFragment     : receives ACTION_BATTERY_CHANGED, shows % / status / power source
       3. CustomReceiverFragment : registers the custom BroadcastReceiver, broadcasts the text from step 2
          and shows "Received: <text>". (Nothing happens in this step for the battery option.)
  B. Image scale : ImageFragment  - image loaded from the internet, pinch (ScaleGestureDetector + Matrix) to zoom
  C. Video       : VideoFragment  - one video from the internet (VideoView + MediaController)
  D. Audio       : AudioFragment  - one audio file from the internet (MediaPlayer: play / pause / stop / seek)

The app needs an internet connection for B, C and D (INTERNET permission).
Sizes/text are in values/sizes.xml (+ values-sw600dp for tablets) and strings.xml.

Tested: Pixel 10 emulator, portrait - custom broadcast, battery, image load, video load, audio play.
NOT tested: pinch gesture (needs a multi-touch device/emulator), landscape, tablet.
