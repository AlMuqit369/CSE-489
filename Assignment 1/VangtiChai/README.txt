VangtiChai - CSE 489 Assignment 1 (Kotlin, minSdk 21)

Layouts:
  app/src/main/res/layout/activity_main.xml       - portrait (notes table left, keypad right)
  app/src/main/res/layout-land/activity_main.xml  - landscape (amount + table left, full-height keypad right)
  res/values/sizes.xml                            - phone sizes
  res/values-sw600dp/sizes.xml                    - tablet sizes (larger text/padding/margins)
Shared pieces: view_notes.xml (change table), view_keypad.xml (keypad of Buttons).
State (entered amount) is saved in onSaveInstanceState; the change list is recomputed on restore.
Amount is capped at 9 digits.

Devices tested: Pixel 10 emulator (portrait). Landscape and Nexus 10 not yet tested.
