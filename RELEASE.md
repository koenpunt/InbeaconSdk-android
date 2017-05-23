#### 2.1.0 (23-May-2017)
- support for custom events
- don't restart BLE periodically if the library confrims device can detect duplicate advertisements in a single scan, leading to more reliable detections with short scan cycles
- Fix bug causing brief scan dropouts after starting a scan after a long period of inactivity (i.e. startup and background-foreground transitions) due to Android N scan limits
- Ensure thread safety for singleton creation of BeaconManager, DetectionTracker, MonitoringStatus, and Stats
