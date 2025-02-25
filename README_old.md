# Cider Remote Android
This is an unofficial remote client created by me, utilizing the remote APIs of Cider.

> [!IMPORTANT]  
> This app is an independent project and is not affiliated with, associated with, authorized by, endorsed by, or in any way officially connected to Cider Collective LLC. All product names, trademarks, and registered trademarks are the property of their respective owners. The use of any names, logos, or brands is for identification purposes only and does not imply endorsement. The creators of this app are solely responsible for its content and functionality.

# Setup
It's practically a one-time setup, and settings will be saved for future use.

> [!TIP]  
> It is recommended to use this app in a LAN setup (same Wi-Fi network). WAN mode may cause issues and delays.

## Setup on Cider

### Settings for the remote to work
1. Open the Cider instance on your PC/Linux/MacOS
2. Click on the three dots button (usually at the top left side of Cider)
3. Go to "Settings," then to "Connectivity"
4. Scroll down and turn on the WebSockets API
5. Click the "Manage" button under "Manage External Applications"
6. Turn off the "Require API Tokens" option

### URL for the Client
1. Click on the three buttons
2. Go to "Help"
3. Click on "Connect a Remote App"
4. Go to the "Pair" option and copy the host address
5. Send this address to your phone

## Android Client Setup
> [!NOTE]  
> Keep the actual Cider instance running during this setup. It might even play a track

1. Install the app and open it (the app is linked under releases)
2. Go to the "Setup" option
3. Paste the host address copied earlier and add `:10767` after the host address
4. The URL should be in the format `http://HostAddress:10767`. For example: `http://192.164.2.5:10767`
5. Click "Submit"
6. If something is playing on your Cider app, it will reflect on the remote. Just go to "Controls" and check

## Screenshots
<p>
  <img src="/screenshots/Controls.png" width="200"/>  
  &nbsp;&nbsp;&nbsp;  
  <img src="/screenshots/Queue.png" width="200"/>
</p>

## Upcoming
Support for multiple instances/devices (already exists in the new version).

## Bugs and Crashes
Ping me on the Cider Discord server if you find any bugs or if the app crashes.

