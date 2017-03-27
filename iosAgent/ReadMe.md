
#Requirements

1. Mac OS (10.12) or above
2. Xcode 8.0 and above
3. Developer account with apple id credentials
4. iOS version 9.3 and above

#Development

1. Download the project from the SVN repository
2. From the list of files that you downloaded, double click on TrakEyeApp.xcworkspace. Xcode will launch the project.
3. On the left top corner, select your device from the list of devices available.
4. Build and run the application (cmd + B/ cmd + R) or Choose product -> build / product -> run
5. The app will get installed in the respective device.
6. If the device is not registered and does not have d sign in  certificates, visit the developer portal (developer.apple.com) and the UDID of your device to the list and download the updated provisioning profile and clean and run the project.


#Creating an ipa file

1. Open the project through xcode.
2. Select the device type to generic ios device
3. Product -> Archive -> Export 
4. Choose Save for ADHOC deployment
5. ipa will be generated and ready to be installed on registered iPhone


#Creating AppStore ipa file

1. Open the project through xcode.
2. Select the device type to generic ios device
3. Product -> Archive -> Export 
4. New window opened i.e, Organiser
5. On the right side select valodation: wait for complete validation
6. Submit To Appstore : wait until submission completed.
