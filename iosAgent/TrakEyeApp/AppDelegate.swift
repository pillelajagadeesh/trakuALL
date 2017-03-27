//
//  AppDelegate.swift
//  TrakEyeApp
//
//  Created by Deepu on 09/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import GoogleMaps
import GooglePlaces
import CoreLocation
import SystemConfiguration
import Firebase
import FirebaseInstanceID
import FirebaseMessaging
import UserNotifications

let fontBold = "HelveticaNeue-Bold"
let fontName = "HelveticaNeue-Thin"

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate , UINavigationBarDelegate, UINavigationControllerDelegate{
    /// The callback to handle data message received via FCM for devices running iOS 10 or above.
   

    let gcmMessageIDKey = "gcm.message_id"
    var window: UIWindow?
    var locationManager = CLLocationManager()
    var internetReach = Reachability()
    var navigationContoller = UINavigationController()
    var fcm_Token = String()
    
   
    
//    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
//        <#code#>
//    }
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        GMSServices.provideAPIKey("AIzaSyBRYO1RNv-eUSH-5osZUdaNBrSWWFP80hs")
        GMSPlacesClient.provideAPIKey("AAIzaSyBRYO1RNv-eUSH-5osZUdaNBrSWWFP80hs")
        //FIRApp.configure()
        UINavigationBar.appearance().barTintColor = UIColor(rgb: 0x03a9f5)//UIColor.blue
        UINavigationBar.appearance().tintColor = UIColor.white
        UINavigationBar.appearance().titleTextAttributes = [NSForegroundColorAttributeName : UIColor.white]
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.reachabilityChanged),name: NSNotification.Name(rawValue: kReachabilityChangedNotification),object: nil)
        
        internetReach = Reachability.forInternetConnection()
        
        internetReach.startNotifier()
        
        self.updatereachability(curReach: internetReach)
        
        application.setMinimumBackgroundFetchInterval(UIApplicationBackgroundFetchIntervalMinimum)
        
        self.loginActioncall()
        
        print(report_memory())
        print(Float(__getMemoryUsedPer1()))
        
        self.window?.backgroundColor = UIColor.white
        self.window?.makeKeyAndVisible()
        // Override point for customization after application launch.
        
        if #available(iOS 10.0, *) {
            let center = UNUserNotificationCenter.current()
            center.delegate = self
            center.requestAuthorization(options: [.sound, .alert, .badge]) { (granted, error) in
                if error == nil{
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
            // For iOS 10 data message (sent via FCM)
            FIRMessaging.messaging().remoteMessageDelegate = self
            
        } else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
            application.registerForRemoteNotifications()
        }
        
        
        // [END register_for_notifications]
        FIRApp.configure()
        
        // Add observer for InstanceID token refresh callback.
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.tokenRefreshNotification),
                                               name: .firInstanceIDTokenRefresh,
                                               object: nil)
        
        
        
        
        
        
        //application.registerForRemoteNotifications()
        
        return true
    }
//    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
//        let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
//        print("APNs device token: \(deviceTokenString)")
//    }
    
   
    // Push notification received
    
    
    func loginActioncall(){
        if let roomCount = UserDefaults.standard.object(forKey: "YES") {
            print(roomCount)
            let initialViewController = MapViewController()
            navigationContoller = UINavigationController(rootViewController: initialViewController)
            self.window?.rootViewController = navigationContoller
        }
        else
        {
            let initialViewController = LoginViewController()
            //self.window?.rootViewController = initialViewController
            let navigationCont = UINavigationController(rootViewController: initialViewController)
            self.window?.rootViewController = navigationCont
        }
    }
    
    func reachabilityChanged(note: NSNotification) {
        
        let reachability = note.object as! Reachability
        
        self.updatereachability(curReach: reachability)
        
    }
    
    
    func reachability(curReach : Reachability)
    {
        let netStatus : NetworkStatus = curReach.currentReachabilityStatus()
        
        if netStatus == NotReachable {
          UserDefaults.standard.set(false, forKey: "networkStatus")
        }
        else{
               UserDefaults.standard.set(true, forKey: "networkStatus")
        }
        
        var connectionRequired = curReach.connectionRequired() 
        
        var statusString = ""
        
        switch (netStatus)
        {
        case NotReachable:
            
            if(curReach == internetReach){
                 showerrorprogress(title: kInterenetAlertMessage, view: APP_DELEGATE.window!)
            }
            
                connectionRequired = false
                break;
            
        case ReachableViaWWAN:
            
                break;
            
        case ReachableViaWiFi:
            
                break;
        default:
            return
        }
        
        if(connectionRequired)
        {
            statusString = "\(statusString), Connection Required"
        }

    }
    
    func updatereachability(curReach : Reachability)
    {
        if(curReach == internetReach)
        {
            self.reachability(curReach: curReach)
        }

    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
    }
    
    
    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void){
        application.setMinimumBackgroundFetchInterval(UIApplicationBackgroundFetchIntervalMinimum)
        locationManager.startUpdatingLocation()
        locationManager.startMonitoringSignificantLocationChanges()
        completionHandler(UIBackgroundFetchResult.newData)
    }

    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
        self.locationManager.stopUpdatingLocation()
        self.locationManager.desiredAccuracy=kCLLocationAccuracyBest
        self.locationManager.distanceFilter=10
        self.locationManager.activityType = .automotiveNavigation
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.allowsBackgroundLocationUpdates = true
        locationManager.startUpdatingLocation()
    }
    
    func getLoc(){
        print("Got Loc")
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
 
        application.setMinimumBackgroundFetchInterval(UIApplicationBackgroundFetchIntervalMinimum)
        locationManager.startUpdatingLocation()
        locationManager.startMonitoringSignificantLocationChanges()// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     //FIRMessaging.messaging().disconnect()
      //  print("Disconnected from FCM.")

    }

    func applicationWillEnterForeground(_ application: UIApplication) {
       // locationManager.startUpdatingLocation()
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

   

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    
    private func application(_ application: UIApplication, didRegister notificationSettings: UNNotificationSettings)
    {
        
        /**
         Allow device to register for remote notification.
         */
        UIApplication.shared.registerForRemoteNotifications()
    }
    
    // [START receive_message]
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        // Print full message.
        print(userInfo)
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        // Print full message.
        print(userInfo)
        
        completionHandler(UIBackgroundFetchResult.newData)
    }
    // [END receive_message]
    // [START refresh_token]
    func tokenRefreshNotification(_ notification: Notification) {
        if let refreshedToken = FIRInstanceID.instanceID().token() {
            fcm_Token = refreshedToken
            print("InstanceID token: \(refreshedToken)")
        }
        
        // Connect to FCM since connection may have failed when attempted before having a token.
        connectToFcm()
    }
    // [END refresh_token]
    // [START connect_to_fcm]
    func connectToFcm() {
        // Won't connect since there is no token
        guard FIRInstanceID.instanceID().token() != nil else {
            return;
        }
        
        // Disconnect previous FCM connection if it exists.
        FIRMessaging.messaging().disconnect()
        
        FIRMessaging.messaging().connect { (error) in
            if error != nil {
                print("Unable to connect with FCM. \(error)")
            } else {
                print("Connected to FCM.")
            }
        }
    }
    // [END connect_to_fcm]
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Unable to register for remote notifications: \(error.localizedDescription)")
    }
    
    // This function is added here only for debugging purposes, and can be removed if swizzling is enabled.
    // If swizzling is disabled then this function must be implemented so that the APNs token can be paired to
    // the InstanceID token.
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("APNs token retrieved: \(deviceToken.description)")
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        print(token)
        // With swizzling disabled you must set the APNs token here.
        // FIRInstanceID.instanceID().setAPNSToken(deviceToken, type: FIRInstanceIDAPNSTokenType.sandbox)
    }
    
    // [START connect_on_active]
    func applicationDidBecomeActive(_ application: UIApplication) {
        connectToFcm()
    }
    // [END connect_on_active]
    // [START disconnect_from_fcm]
    
    // [END disconnect_from_fcm]
}

// [START ios_10_message_handling]
@available(iOS 10, *)
extension AppDelegate : UNUserNotificationCenterDelegate {
    
    // Receive displayed notifications for iOS 10 devices.
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        // Print full message.
        print(userInfo)
        
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "RefreshNotification"), object: nil)
        
        // Change this to your preferred presentation option
        completionHandler([UNNotificationPresentationOptions.alert,
                            UNNotificationPresentationOptions.sound,
                            UNNotificationPresentationOptions.badge])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        // Print full message.
        print(userInfo)
        
        completionHandler()
    }
}
// [END ios_10_message_handling]
// [START ios_10_data_message_handling]
extension AppDelegate : FIRMessagingDelegate {
    // Receive data message on iOS 10 devices while app is in the foreground.
    func applicationReceivedRemoteMessage(_ remoteMessage: FIRMessagingRemoteMessage) {
        print(remoteMessage.appData)
    }
}
// [END ios_10_data_message_handling]
