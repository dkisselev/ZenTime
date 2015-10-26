# ZenTime

An Xposed module to customize the available time options in the Android 5.0+ priority mode notification panel.

## Usage

1. Install & enable the module
2. Open the app and choose choose your time options. 'More' adds 5 minute intervals in the lower settings and 30 minute intervals in the higher ones
3. Reboot your device (a soft reset works as well)

## Custom time settings

When using the custom option, enter the minute options you would like to see separated by commas. Make sure to include '60' as an option (otherwise the systemui crashes), it's the first option that you will see when you open the panel.
