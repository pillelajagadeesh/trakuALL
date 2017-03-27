//
//  MapViewController.swift
//  TrakEyeApp
//
//  Created by Deepu on 09/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import GoogleMaps
import GooglePlaces
import Alamofire
import CoreTelephony
//import SQLite

extension UIView {
    func addShadowView(width:CGFloat=5, height:CGFloat=5, Opacidade:Float=0.7, maskToBounds:Bool=false, radius:CGFloat=2){
        self.layer.shadowColor = UIColor.black.withAlphaComponent(0.5).cgColor
        self.layer.shadowOffset = CGSize(width: width, height: height)
        self.layer.shadowRadius = radius
        self.layer.shadowOpacity = Opacidade
        self.layer.masksToBounds = maskToBounds
    }
}
typealias ServiceResponse = (NSDictionary?, NSError?) -> Void
class MapViewController: UIViewController, GMSMapViewDelegate, CLLocationManagerDelegate {
    var locationManager = CLLocationManager()
    var mapView: GMSMapView!
    var fromMarker = GMSMarker()
    var toMarker = GMSMarker()
    
    var myLocation1 = CLLocation()
    var lastCameraPosition = GMSCameraPosition()
    let displayDetailsView : UIView = UIView()
    var marker = GMSMarker()
    var placesClient = GMSPlacesClient()
    var locMark = UIButton()
    let boolValue = 0
    var latitude : CLLocationDegrees!
    var longitude : CLLocationDegrees!
    var oldLatitude : CLLocationDegrees!
    var oldLongitude : CLLocationDegrees!
    var oflinePostArray = NSMutableArray()
    var oldOfflineLatitude : CLLocationDegrees!
    var oldOfflineLongitude : CLLocationDegrees!

    var address : String!
    var tokenStr  :String!
    var startLocation:CLLocation!
    var lastLocation: CLLocation!
    var traveledDistance:Double = 0
    var locationlogsDict = NSDictionary()
    var locationlogsPutDict = NSDictionary()
    var logSourceStr : String!
    var isFirstTime = true
    var istableCreated = false
    var idStr : Int!
    var userIdStr : NSInteger!
    var createdTimeStr : Int!
    var updatedTimeStr : Int!
    var batteryLevel: Float = 0.0
    var latestLogsArray  = NSDictionary()
    var latestLogs = LatestLogs()
    private var didPerformGeocode = false
    var idleTimer = Timer()
    var isBackgroundMode = false
    var _deferringUpdates = false
//    var db = try! Connection()
//    let storeData = Table("storeData")
//    let newTable = Table("newTable")
//    let id = Expression<Int64>("id")
//    let offlineLatitude = Expression<String?>("latitude")
//    let offlineLongitude = Expression<String>("longitude")
    
    var gpsStatus : Bool!
    var updateLocTimer :  Timer!
    var updateTimer :  Timer!
    var locationUpdate :  Timer!
    var checkBool : Bool = true
    var checkOfflineBool : Bool = true
    var bulkBool  :Bool = true
    var notBtn = MIBadgeButton()
    var casesBtn = MIBadgeButton()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        self.updateLocationStatus()
        self.edgesForExtendedLayout = []
//        let path = NSSearchPathForDirectoriesInDomains(
//            .documentDirectory, .userDomainMask, true
//            ).first!
//        db = try! Connection("\(path)/db.sqlite3")
//        
//        try! db.run(storeData.create(temporary: false, ifNotExists: true, block: { t in
//            t.column(id, primaryKey: true)
//            t.column(offlineLatitude)
//            t.column(offlineLongitude)
//        }))
//        db = try! Connection("\(path)/db.sqlite3")
//        
//        try! db.run(storeData.create(temporary: false, ifNotExists: true, block: { t in
//            t.column(id, primaryKey: true)
//            t.column(offlineLatitude)
//            t.column(offlineLongitude)
//        }))
  //      db = try! Connection("\(path)/db.sqlite3")
  //      db = try! Connection("\(path)/db.sqlite3")
//        db = try! Connection("\(path)/db.sqlite3")
//        
//        try! db.run(storeData.create(temporary: false, ifNotExists: true, block: { t in
//            t.column(id, primaryKey: true)
//            t.column(offlineLatitude)
//            t.column(offlineLongitude)
//        }))
//        db = try! Connection("\(path)/db.sqlite3")
//        
//        try! db.run(storeData.create(temporary: false, ifNotExists: true, block: { t in
//            t.column(id, primaryKey: true)
//            t.column(offlineLatitude)
//            t.column(offlineLongitude)
//        }))

  //      db = try! Connection("\(path)/db.sqlite3")
        
//        try! db.run(storeData.create(temporary: false, ifNotExists: true, block: { t in
//            t.column(id, primaryKey: true)
//            t.column(offlineLatitude)
//            t.column(offlineLongitude)
//        }))

        UIDevice.current.isBatteryMonitoringEnabled = true
        batteryLevel = UIDevice.current.batteryLevel
        viewWidth = view.width
        viewHeight = view.height
        print("battery  percentage \(batteryLevel)")
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            logSourceStr = "GPS"
        } else {
            logSourceStr = "NP"
        }
        
        self.mapView = GMSMapView()
        self.mapView.delegate = self
        
        self.title = "My Location"
        self.navigationController?.navigationBar.tintColor = UIColor.white
        self.navigationController?.navigationBar.backgroundColor = LOGIN_BG_COLOR
        let image = UIImage(named: "icon_menu")
        let leftButton = UIBarButtonItem(image: image, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.menuClicked))
        self.navigationItem.leftBarButtonItem = leftButton
        
        displayDetailsView.frame = CGRect(x: 25, y: ScreenSize.SCREEN_HEIGHT-240, width: ScreenSize.SCREEN_WIDTH-50, height: 150)
        displayDetailsView.backgroundColor = UIColor.init(rgb: 0xFFFFFF)
        
        let nameLabel = UILabel()
        nameLabel.frame = CGRect(x: 15, y: 5, width: (displayDetailsView.width/2) - 20, height: 20)
        nameLabel.text = "NAME"
        displayDetailsView.addSubview(nameLabel)
        
        let userDefaults = UserDefaults.standard
        
        latestLogs.nameText = UILabel()
        latestLogs.nameText.frame = CGRect(x: 15, y: nameLabel.height + nameLabel.frame.origin.y + 2, width: displayDetailsView.width/2 - 20, height: 20)
        let name = userDefaults.value(forKey: "name") as! String?
        latestLogs.nameText.text = name?.uppercased()
        designLabels(firstValue: nameLabel, secondValue: latestLogs.nameText)
        displayDetailsView.addSubview(latestLogs.nameText)
        
        let statusLabel = UILabel()
        statusLabel.frame = CGRect(x: 15, y: latestLogs.nameText.height + latestLogs.nameText.frame.origin.y + 5, width: displayDetailsView.width/2 - 20, height: 20)
        statusLabel.text = "STATUS"
        
        displayDetailsView.addSubview(statusLabel)
        
        latestLogs.statusText = UILabel()
        latestLogs.statusText.frame = CGRect(x: 15, y: statusLabel.height + statusLabel.frame.origin.y + 2, width: displayDetailsView.width/2 - 20, height: 20)
        latestLogs.statusText.text = ""//userDefaults.value(forKey: "name") as! String?
        designLabels(firstValue: statusLabel, secondValue: latestLogs.statusText)
        displayDetailsView.addSubview(latestLogs.statusText)
        
        let locationLabel = UILabel()
        locationLabel.frame = CGRect(x: 15, y: latestLogs.statusText.maxYOrigin, width: displayDetailsView.width/2 - 20, height: 20)
        locationLabel.text = "LOCATION"
        displayDetailsView.addSubview(locationLabel)
        
        latestLogs.locationText = UILabel()
        latestLogs.locationText.frame = CGRect(x: 15, y: locationLabel.maxYOrigin, width: displayDetailsView.width - 30, height: 20)
        latestLogs.locationText.numberOfLines = 2
        latestLogs.locationText.text = ""//userDefaults.value(forKey: "name") as! String?
        designLabels(firstValue: locationLabel, secondValue: latestLogs.locationText)
        displayDetailsView.addSubview(latestLogs.locationText)
        
        let batteryLabel = UILabel()
        batteryLabel.frame = CGRect(x: displayDetailsView.width/2, y: nameLabel.frame.origin.y, width: displayDetailsView.width/2 - 20, height: 20)
        batteryLabel.text = "BATTERY LEVEL"
        
        displayDetailsView.addSubview(batteryLabel)
        
        latestLogs.batteryLevel = UILabel()
        latestLogs.batteryLevel.frame = CGRect(x: batteryLabel.frame.origin.x, y: batteryLabel.height + batteryLabel.frame.origin.y + 2, width: displayDetailsView.width/2 - 20, height: 20)
        designLabels(firstValue: batteryLabel, secondValue: latestLogs.batteryLevel)
        displayDetailsView.addSubview(latestLogs.batteryLevel)
        
        let startTimeLabel = UILabel()
        startTimeLabel.frame = CGRect(x: batteryLabel.frame.origin.x, y: latestLogs.batteryLevel.height + latestLogs.batteryLevel.frame.origin.y + 5, width: displayDetailsView.width/2 - 20, height: 20)
        startTimeLabel.text = "VERSION"
        displayDetailsView.addSubview(startTimeLabel)
        
        latestLogs.startTime = UILabel()
        latestLogs.startTime.frame = CGRect(x: batteryLabel.frame.origin.x, y: startTimeLabel.height + startTimeLabel.frame.origin.y + 2, width: displayDetailsView.width/2 - 20, height: 20)
        designLabels(firstValue: startTimeLabel, secondValue: latestLogs.startTime)
        displayDetailsView.addSubview(latestLogs.startTime)
        
        let camera = GMSCameraPosition.camera(withLatitude: -33.86, longitude: 151.20, zoom: 6.0)
        self.mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        self.mapView.delegate = self
        self.mapView.isMyLocationEnabled = true
        //self.mapView.settings.myLocationButton = true
        self.mapView.settings.scrollGestures = true
        self.mapView.settings.zoomGestures = true
        //let mapInsets = UIEdgeInsetsMake(100.0, 0.0, 0.0, 300.0);
       
        //self.mapView.padding = mapInsets//UIEdgeInsetsMake(0, 0, ScreenSize.SCREEN_HEIGHT/1.2, 0);
        self.locationManager.delegate = self
        self.locationManager.desiredAccuracy=kCLLocationAccuracyBest
        self.locationManager.distanceFilter=10
        self.locationManager.activityType = .automotiveNavigation
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.allowsBackgroundLocationUpdates = true
        //self.locationManager.startMonitoringSignificantLocationChanges()
        self.mapView.frame = CGRect(x: 0.0, y: 0.0, width: viewWidth, height: viewHeight)
        view.addSubview(self.mapView)
        
        for object in self.mapView.subviews{
            for obj in object.subviews{
                if let button = obj as? UIButton{
                    let name = button.accessibilityIdentifier
                    if(name == "my_location"){
                        //config a position
                        button.center = self.mapView.center
                    }
                }
            }
        }
        
        if CLLocationManager.locationServicesEnabled(){
        self.locationManager.startUpdatingLocation()
        }
        //self.displayRightIcons()
        // Do any additional setup after loading the view.
        
        let notImage = UIImage(named: "Notifications")
        let cassesImage = UIImage(named: "MYCASES")
        //let subMenuImage = UIImage(named: "MYCASES")
        
        
        self.designBarButtons(buttn: notBtn)
        notBtn.setImage(notImage, for: .normal)
        notBtn.addTarget(self, action: #selector(self.notIconClick), for: .touchUpInside)
        
        self.designBarButtons(buttn: casesBtn)
        casesBtn.setImage(cassesImage, for: .normal)
        casesBtn.addTarget(self, action: #selector(self.myCassesIconClick), for: .touchUpInside)
        
        let notificationIcon = UIBarButtonItem(customView: notBtn)
        let mycasesIcon = UIBarButtonItem(customView: casesBtn)//UIBarButtonItem(image: cassesImage, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.menuClicked))
        self.navigationItem.rightBarButtonItems = [notificationIcon,mycasesIcon]
    }
    
    
    func designBarButtons(buttn:MIBadgeButton){
        buttn.frame = CGRect(x: 0, y: 0, width: 35, height: 35)
        buttn.badgeTextColor = UIColor.red
        buttn.badgeBackgroundColor = UIColor.white
        buttn.badgeEdgeInsets = UIEdgeInsetsMake(10, 0, 0, 10)
    }
    
    
    func notIconClick(){
        let notification = NotificationsVC()
        self.navigationController?.pushViewController(notification, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Notifications"
        navigationItem.backBarButtonItem = backItem
    }
    
    func myCassesIconClick(){
        let mycase = MycasesVC()
        self.navigationController?.pushViewController(mycase, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "My Cases"
        navigationItem.backBarButtonItem = backItem
    }
    
    
    override func viewWillAppear(_ animated: Bool) {

        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.allowsBackgroundLocationUpdates = true
        self.locationManager.pausesLocationUpdatesAutomatically = false
         updateTimer = Timer.scheduledTimer(timeInterval: 32.0, target: self, selector: #selector(self.getLatestLogs), userInfo: nil, repeats: true)
        print(updateTimer)
         locationUpdate = Timer.scheduledTimer(timeInterval: 10.0, target: self, selector: #selector(self.checkLocation), userInfo: nil, repeats: true)
        print(locationUpdate)
        
//        do{
//        
////            let count = try db.scalar(storeData.select(offlineLatitude.distinct.count))
////            print(count)
////            for user in try db.prepare(storeData) {
////                print("id: \(user[id]), latitude: \(user[offlineLatitude]), longitude: \(user[offlineLongitude])")
////            }
//        }
//        catch{
//            print("error")
//        }

        
//        if UserDefaults.standard.bool(forKey: "networkStatus"){
//            if(oflinePostArray.count > 0){
//                DispatchQueue.main.asyncAfter(deadline: .now() + 0.03) {
//                    self.postBulkLogs()
//                }
//            }
//        }

    }
    
    func getLoc(){
        
        let state: UIApplicationState = UIApplication.shared.applicationState
        
        if state == .background {
           updateLocTimer.invalidate()
        updateLocTimer = Timer.scheduledTimer(timeInterval: 25.0, target: self, selector: #selector(self.getLoc), userInfo: nil, repeats: true)
            self.locationManager.startMonitoringSignificantLocationChanges()
        }else{
        locationManager.startUpdatingLocation()
        }
    }
    
    func designLabels(firstValue:UILabel, secondValue:UILabel){
        //firstValue.font = UIFont(name: "Futura-Medium", size: 12)
        firstValue.font = UIFont.systemFont(ofSize: 12)
        firstValue.textColor = LOGIN_BG_COLOR
        firstValue.textAlignment = NSTextAlignment.left
        
        //secondValue.font = UIFont(name: "Futura-Medium", size: 14)
        secondValue.font = UIFont(name: "Helvetica-Bold", size: 10)
        secondValue.textColor = UIColor.black
        secondValue.textAlignment = NSTextAlignment.left
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
        
    }
    
    //TODO: LocationManager Delegate
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        self.locationManager.allowsBackgroundLocationUpdates = true
        
        let locValue:CLLocationCoordinate2D = manager.location!.coordinate
        
        print("locations = \(locValue.latitude) \(locValue.longitude)")
        print("Loc 1st = \(locations.first)")
        print("Loc 2nd = \(locations.last)")
        print(manager.location!.speed)
        latitude = (locValue.latitude).roundTo(places: 8)
        longitude = (locValue.longitude).roundTo(places: 8)
        globalLat = latitude.roundTo(places: 8)
        globalLong = longitude.roundTo(places: 8)
        if(checkBool == true){
          oldLatitude = latitude
          oldLongitude = longitude
          checkBool = false
        }
       
        if (isBackgroundMode && !_deferringUpdates)
        {
            _deferringUpdates = true;
            self.locationManager.allowDeferredLocationUpdates(untilTraveled: CLLocationDistanceMax, timeout: 60.0)
        }
        
        if UserDefaults.standard.bool(forKey: "networkStatus"){
            let userDefaults = UserDefaults.standard
            
            if(bulkBool == true){
                bulkBool = false
                if userDefaults.object(forKey: "bulkArray") != nil  {
                    self.oflinePostArray = NSMutableArray(array: userDefaults.object(forKey: "bulkArray") as! NSArray)
                    
                    if(oflinePostArray.count >= 10){
                        
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.03) {
                            
                            self.postBulkLogs()
                        }
                    }
                }
            }
        }
        else{
            if self.isFirstTime {
                address = ""
                self.postLogs()
                self.isFirstTime = false
            }
            else{
                if(self.oldLatitude == self.latitude && self.oldLongitude == self.longitude){
//                    address = ""
//                    self.postLogs()
                }else{
                    address = ""
                    self.postLogs()
                    self.oldLatitude = self.latitude
                    self.oldLongitude = self.longitude
                }
            }
            
        }

      
//        if UserDefaults.standard.bool(forKey: "networkStatus"){
//            
//        }else{
//            if(checkOfflineBool == true){
//            oldOfflineLatitude = latitude
//            oldOfflineLongitude = longitude
//            checkOfflineBool = false
//            }
//            print(oldOfflineLatitude, latitude, oldOfflineLongitude, longitude)
//            if(oldOfflineLatitude == latitude && oldOfflineLongitude == longitude){
//            }else{
//                let insert = storeData.insert(offlineLatitude <- String(locValue.latitude), offlineLongitude <- String(locValue.longitude))
//                let rowid = try! db.run(insert)
//                print(rowid)
//            }
//        }
        
        
        
        //start timer
        //idleTimer = Timer.scheduledTimer(timeInterval: 60.0, target: self, selector: #selector(self.putLogs), userInfo: nil, repeats: true)
        
        UserDefaults.standard.set(latitude, forKey: "lat")
        UserDefaults.standard.set(longitude, forKey: "long")
        
        let camera = GMSCameraPosition.camera(withLatitude: (locValue.latitude), longitude: (locValue.longitude), zoom: 12.0)
        self.mapView.animate(to: camera)
        
        
        print(distance(lat1: latitude, lon1: longitude, lat2: oldLatitude, lon2: oldLongitude, unit: "M"),"Miles")
        
        UIApplication.shared.beginBackgroundTask(expirationHandler: {})
        guard let location = locations.first, location.horizontalAccuracy >= 0 else { return }
        
        // or if we have already searched, return
        
        guard !didPerformGeocode else { return }
        
        // otherwise, update state variable, stop location services and start geocode
        
        didPerformGeocode = true
       // locationManager.stopUpdatingLocation()
        
        //--- CLGeocode to get address of current location ---//
        CLGeocoder().reverseGeocodeLocation(manager.location!, completionHandler: {(placemarks, error)->Void in
          //  self.locationManager.stopUpdatingLocation()
            if (error != nil)
            {
                print("Reverse geocoder failed with error" + (error?.localizedDescription)!)
                return
            }
            
            if (placemarks?.count)! > 0
            {
                let pm = (placemarks?[0])! as CLPlacemark
                self.displayLocationInfo(placemark: pm)
                
                if self.startLocation == nil {
                    self.startLocation = locations.first
                } else {
                    let lastLocation = locations.last
                    let distance = self.startLocation.distance(from: lastLocation!)
                    self.startLocation = lastLocation
                    self.traveledDistance = distance
                }
                
                if self.isFirstTime {
                    self.postLogs()
                    self.isFirstTime = false
                    print("travel distance \(self.traveledDistance)")
                }
                else{
                    print(self.oldLatitude, self.latitude, self.oldLongitude, self.longitude)
                    
                    if(self.oldLatitude == self.latitude && self.oldLongitude == self.longitude || manager.location!.speed <= 0.0){
                        self.putLogs()
                        print("travel distance \(self.traveledDistance)")
                    }else{
                        
                        self.postLogs()
                        self.oldLatitude = self.latitude
                        self.oldLongitude = self.longitude
                        print("travel distance \(self.traveledDistance)")
                    }
                }
            }
            else
            {
                print("Problem with the data received from geocoder")
            }
        })
        
        marker.position = CLLocationCoordinate2DMake(locValue.latitude, locValue.longitude)
        marker.map = mapView
        displayDetailsView.addShadowView()
        displayDetailsView.layer.cornerRadius = 10.0
        
        view.addSubview(displayDetailsView)
     //   self.locationManager.stopUpdatingLocation()
        
        
        //TODO: Saving Logs in local store
        
        var paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory = paths[0]
        let fileName = "\(Date()).log"
        let logFilePath = (documentsDirectory as NSString).appendingPathComponent(fileName)
        freopen(logFilePath.cString(using: String.Encoding.ascii)!, "a+", stderr)
        
    }
    
    //TODO: GMSMAPVIEW DELEGATE METHODE
    
    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        
    }
    
    func menuClicked(){
        let menuView = MenuViewController()
        self.navigationController?.pushViewController(menuView, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Menu"
        navigationItem.backBarButtonItem = backItem
    }
    
    
    //TODO: POST LOGS
    func postLogs(){
        var secondsFromGMT: Int { return NSTimeZone.local.secondsFromGMT() }
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        let currentTime = currentTimeMillis()
        
        let parameters: [String : Any] = [
            "latitude" : latitude,
            "longitude" : longitude,
            "createdDateTime" : currentTime,
            "address" : address,
            "logSource" : logSourceStr,
            "logTimeAndZone" : secondsFromGMT,
            "batteryPercentage" : UIDevice.current.batteryLevel*100
        ]
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            
            Alamofire.request("\(BaseUrl)\(kLocationlogs)", method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenStr!)"]).responseJSON { response in
                if(response.result.isFailure){
                    print("no data!");
                }else{
                    if(response.response?.statusCode==200)
                    {
                        print(response.result.value!)
                        self.locationlogsDict = response.result.value! as! NSDictionary
                        self.didPerformGeocode = false
                    }else
                    {
                        self.locationlogsDict = response.result.value! as! NSDictionary
                        self.idStr = self.locationlogsDict.object(forKey: "id") as! Int
                        if let userDict = self.locationlogsDict.object(forKey: "user") as? NSDictionary{
                            self.userIdStr = userDict.object(forKey: "id") as! NSInteger
                        }
                        else{
                            self.userIdStr = 0
                        }
                        
                        self.createdTimeStr = self.locationlogsDict.object(forKey: "createdDateTime") as! Int
                        self.updatedTimeStr = self.locationlogsDict.object(forKey: "updatedDateTime") as! Int
                        self.didPerformGeocode = false
                        self.getLatestLogs()
                    }
                }
            }
        } else {
            let parametersNEW = NSMutableDictionary()
            
            parametersNEW["latitude"] = latitude
            parametersNEW["longitude"] = longitude
            parametersNEW["createdDateTime"] = currentTime
            parametersNEW["address"] = ""
            parametersNEW["logSource"] = "NP"
            parametersNEW["logTimeAndZone"] = secondsFromGMT
            parametersNEW["batteryPercentage"] = UIDevice.current.batteryLevel*100
            oflinePostArray.add(parametersNEW)
            
            let userDefaults = UserDefaults.standard
            userDefaults.set(oflinePostArray, forKey: "bulkArray")
            userDefaults.synchronize()
            //oflinePostArray.add(parameters)
            print(oflinePostArray)
        }
    }
    
    func displayLocationInfo(placemark: CLPlacemark?)
    {
        if let containsPlacemark = placemark
        {
            //stop updating location to save battery life
           // locationManager.stopUpdatingLocation()
            let subThoroughfare = (containsPlacemark.subThoroughfare != nil) ? containsPlacemark.subThoroughfare : ""
            let subLocality = (containsPlacemark.subLocality != nil) ? containsPlacemark.subLocality : ""
            let locality = (containsPlacemark.locality != nil) ? containsPlacemark.locality : ""
            let administrativeArea = (containsPlacemark.administrativeArea != nil) ? containsPlacemark.administrativeArea : ""
            let country = (containsPlacemark.country != nil) ? containsPlacemark.country : ""
            address  = "\(subThoroughfare!), \(subLocality!), \(locality!) , \(administrativeArea!), \(country!) "
            UserDefaults.standard.set(address, forKey: "Address")
            print(address)
            
        }
    }
    
    //TODO: UPDATE Location logs
    func putLogs(){
        //showAlertMessage(titleStr: "Alert", messageStr: "put logs")
        var secondsFromGMT: Int { return NSTimeZone.local.secondsFromGMT() }
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        
        let parameters: [String: Any] = [
            "latitude" : latitude,
            "longitude" : longitude,
            "address" : address,
            "logSource" : logSourceStr,
            "logTimeAndZone" : secondsFromGMT,
            "id" : idStr,
            "userId" : userIdStr,
            "createdDateTime" : createdTimeStr,
            "updatedDateTime" : updatedTimeStr,
            "batteryPercentage" : UIDevice.current.batteryLevel*100
        ]
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            
            Alamofire.request("\(BaseUrl)\(kLocationlogs)", method: .put, parameters: parameters, encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenStr!)"]).responseJSON { response in
                if(response.result.isFailure){
                    print("no data!");
                }else{
                    if(response.response?.statusCode==200)
                    {
                        self.locationlogsPutDict = response.result.value! as! NSDictionary
                        
                        self.didPerformGeocode = false
                        
                    }else
                    {
                        print(response.result.value!)
                        self.locationlogsDict = response.result.value! as! NSDictionary
                        self.didPerformGeocode = false
                    }
                }
            }
        } else {
            print("Internet connection FAILED")
            showerrorprogress(title: kInterenetAlertMessage, view: self.view)
        }
    }
    
    
    //TODO: Get Latest Location logs
    
    func getLatestLogs(){
        
        let userDefaults = UserDefaults.standard
        print(userDefaults.value(forKey: "name")!)
        let name = userDefaults.value(forKey: "name") as! String?
        latestLogs.nameText.text = name?.uppercased()
        latestLogs.batteryLevel.text = String(format: UIDevice.current.batteryLevel * 100 == floor(UIDevice.current.batteryLevel * 100) ? "%.0f" : "%.1f", UIDevice.current.batteryLevel * 100) + "%"
        latestLogs.startTime.text = (Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String)
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            let urlString = "\(BaseUrl)\(kGetLatestLogs)"
            print(urlString)
            let userdata = fetchuserdata()
            tokenStr =  userdata.userid
            
            Alamofire.request(urlString, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenStr!)","Content-Type" : "application/json"]).responseJSON { response in
                if(response.result.isFailure){
                    print("no data!");
                }else{
                    if(response.response?.statusCode==200)
                    {
                        print(response.result.value!)
                        self.latestLogsArray = response.result.value! as! NSDictionary
                        
                        self.populateUserStatus()
                        
                    }else
                    {
                        print(response.result.value!)
                     }
                }
            }
        } else {
           
            showerrorprogress(title: kInterenetAlertMessage, view: self.view)
        }
    }
    
    func populateUserStatus()
    {
        let userDefaults = UserDefaults.standard
        print(userDefaults.value(forKey: "name")!)
        let name = userDefaults.value(forKey: "name") as! String?
        latestLogs.nameText.text = name?.uppercased()
        latestLogs.batteryLevel.text = String(format: UIDevice.current.batteryLevel * 100 == floor(UIDevice.current.batteryLevel * 100) ? "%.0f" : "%.1f", UIDevice.current.batteryLevel * 100) + "%"
        latestLogs.startTime.text = (Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String)
        if(self.latestLogsArray.count > 0){
            
            var k = NSArray()
            let dict = self.latestLogsArray.value(forKey: "caseCounts") as! NSDictionary
            self.casesBtn.badgeString = dict.value(forKey: "NEW") as? String
            let notDict = self.latestLogsArray.value(forKey: "notificationCounts") as! NSDictionary
            self.notBtn.badgeString = notDict.value(forKey: "SENT") as? String
            
            if let newArr : NSArray = self.latestLogsArray.value(forKey: "liveLogs") as? NSArray {
                k = newArr
            }
            
            let  jj = k[0] as! NSDictionary
            
            if let newArr  = jj.value(forKey: "status") as? String {
                latestLogs.statusText.text = newArr.uppercased()
            }
            if let array  = jj.value(forKey: "address") as? String {
                latestLogs.locationText.text = array.uppercased()
                globalAddress = array
            }
            
            //value(forKey: "notificationCounts.SENT")
        }
        
        if latestLogs.statusText.text == "IDLE"{
            marker.icon = GMSMarker.markerImage(with: UIColor.yellow)
        }else if latestLogs.statusText.text == "ACTIVE"{
            marker.icon = GMSMarker.markerImage(with: UIColor.green)
        }else
        {
            marker.icon = GMSMarker.markerImage(with: UIColor.red)
        }
        
    }
    
    
    //TODO: UPdate User Location Status
    func updateLocationStatus(){
        if CLLocationManager.locationServicesEnabled() {
            switch(CLLocationManager.authorizationStatus()) {
            case .notDetermined, .restricted, .denied:
                print("No access")
                gpsStatus = false
            case .authorizedAlways, .authorizedWhenInUse:
                print("Access")
                gpsStatus = true
            }
        } else {
            gpsStatus = false
            print("Location services are not enabled")
        }
        let userDefaults = UserDefaults.standard
        let parameters: [String: Any] = [
            "login" : userDefaults.value(forKey: "name") as! String,
            "gpsStatus": gpsStatus
        ]
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        print(parameters)
        if UserDefaults.standard.bool(forKey: "networkStatus") {
           // showprogress(view: self.view)
            self.doRequestPost1234(url: "\(BaseUrl)\(kUpdateusergps)", data: parameters)
        } else {
            print("Internet connection FAILED")
            showerrorprogress(title: kInterenetAlertMessage, view: self.view)
        }
    }
    
    func doRequestPost1234(url:String,data:[String: Any]){
        
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        let theJSONData = try? JSONSerialization.data(
            withJSONObject: data ,
            options: JSONSerialization.WritingOptions(rawValue: 0))
        let jsonString = NSString(data: theJSONData!,encoding: String.Encoding.ascii.rawValue)
        print("Request Object:\(data)")
        print("Request string = \(jsonString!)")
        let session = URLSession.shared
        let urlPath = NSURL(string: url)
        let request = NSMutableURLRequest(url: urlPath! as URL)
        request.addValue("Bearer \(tokenStr!)", forHTTPHeaderField: "Authorization")
        request.addValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        let postLength = NSString(format:"%lu", jsonString!.length) as String
        request.setValue(postLength, forHTTPHeaderField:"Content-Length")
        request.httpBody = jsonString!.data(using: String.Encoding.utf8.rawValue, allowLossyConversion:true)
        
        let dataTask = session.dataTask(with: request as URLRequest) { (data, response, error) -> Void in
            hideprogress()
            if((error) != nil) {
                print(error!.localizedDescription)
                //[self.delegate .APIResponseArrived([])]
            }else {
                print("Succes:")
                _ = NSString(data: data!, encoding:String.Encoding.utf8.rawValue)
            }
        }
        dataTask.resume()
    }
    
    var jj = 0
    var kk = 1
    
    func checkLocation(){
        
        if CLLocationManager.locationServicesEnabled(){
            jj = 0
            if(kk == 1){
                kk += 1
                // service call
                let userDefaults = UserDefaults.standard
                
                let parameters: [String: Any] = [
                    "login" : userDefaults.value(forKey: "name") as! String,
                    "gpsStatus": true
                ]
                let userdata = fetchuserdata()
                tokenStr =  userdata.userid
                print(parameters)
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                self.doRequestPost1234(url: "\(BaseUrl)\(kUpdateusergps)", data: parameters)
                } else {
                    print("Internet connection FAILED")
                    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                }
            }
        }
        else{
            kk = 1
            if(jj == 0){
                jj += 1
                //service call
                let userDefaults = UserDefaults.standard
                let parameters: [String: Any] = [
                    "login" : userDefaults.value(forKey: "name") as! String,
                    "gpsStatus": false
                ]
                let userdata = fetchuserdata()
                tokenStr =  userdata.userid
                print(parameters)
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    self.doRequestPost1234(url: "\(BaseUrl)\(kUpdateusergps)", data: parameters)
                } else {
                    print("Internet connection FAILED")
                    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                }
            }
        }
    }
    
    //TODO: POST BULKLOGS
    
    func postBulkLogs(){
        
        var secondsFromGMT: Int { return NSTimeZone.local.secondsFromGMT() }
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        
        
       // self.oflinePostArray = NSMutableArray(array: userDefaults.object(forKey: "bulkArray") as! NSArray)
        _ = self.oflinePostArray.count/10
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.03) {
                var arr = [NSDictionary]()
                for i in 0..<10{
                   // arr.addEntries(from: self.oflinePostArray[i] as! [AnyHashable : Any])
                    arr.append(self.oflinePostArray[i] as! NSDictionary)
                }
                
               // var dataParam = self.oflinePostArray[0..<10]
                
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    let url = "\(BaseUrl)\(kLocationBulklogs)"
                    self.offlineServiceCall(url:url, data:arr, method:"post") { (responseObject:NSDictionary?, error:NSError?) in
                        if ((error) != nil) {
                            print("Error logging you in!")
                        } else {
                            print("Do something in the view controller in response to successful login!")
                           // for i in 0..<10{
                                 self.oflinePostArray.removeObjects(in: arr)
                              // self.oflinePostArray.removeObject(at: i)
                          //  }
                            print(self.oflinePostArray.count)
                            print(self.oflinePostArray)
                            if(self.oflinePostArray.count >= 10){
                                self.postBulkLogs()
                            }else{
                                let userDefaults = UserDefaults.standard
                                userDefaults.removeObject(forKey: "bulkArray")
                                self.bulkBool = true
                            }
                        }
                    }
                } else {
                    print("Internet connection FAILED")
                    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                }
        }
    }

    func offlineServiceCall(url:String,data:Any,method:String,onCompletion: @escaping ServiceResponse) -> Void{
        
        let userdata = fetchuserdata()
        tokenStr =  userdata.userid
        do{
            let theJSONData = try? JSONSerialization.data(withJSONObject: data ,options: JSONSerialization.WritingOptions.init(rawValue: 0))
            
            
            
            let jsonString = NSString(data: theJSONData!,encoding: String.Encoding.ascii.rawValue)
          //  print("Request Object:\(data)")
          //  print("Request string = \(jsonString!)")
            let session = URLSession.shared
            let urlPath = NSURL(string: url)
            let request = NSMutableURLRequest(url: urlPath! as URL)
            request.addValue("Bearer \(tokenStr!)", forHTTPHeaderField: "Authorization")
            request.addValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpMethod = method.uppercased()
            let postLength = NSString(format:"%lu", jsonString!.length) as String
            request.setValue(postLength, forHTTPHeaderField:"Content-Length")
            request.httpBody = jsonString!.data(using: String.Encoding.utf8.rawValue, allowLossyConversion:true)
            
            let dataTask = session.dataTask(with: request as URLRequest) { (data, response, error) -> Void in
                hideprogress()
                if((error) != nil) {
                    print(error!.localizedDescription)
                    onCompletion(nil, error as NSError?)
                    //[self.delegate .APIResponseArrived([])]
                }else {
                    print("Succes:")
                    let str = NSString(data: data!, encoding:String.Encoding.utf8.rawValue)
                    onCompletion(self.convertToDictionary(text: str as! String) as NSDictionary?, nil)
                }
            }
            dataTask.resume()
        }
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    func currentTimeMillis() -> Int64{
        let nowDouble = NSDate().timeIntervalSince1970
        return Int64(nowDouble*1000)
    }
}
extension MapViewController{
    
    func deg2rad(deg:Double) -> Double {
        return deg * M_PI / 180
    }
    

    func rad2deg(rad:Double) -> Double {
        return rad * 180.0 / M_PI
    }
    
    func distance(lat1:Double, lon1:Double, lat2:Double, lon2:Double, unit:String) -> Double {
        let theta = lon1 - lon2
        var dist = sin(deg2rad(deg: lat1)) * sin(deg2rad(deg: lat2)) + cos(deg2rad(deg: lat1)) * cos(deg2rad(deg: lat2)) * cos(deg2rad(deg: theta))
        dist = acos(dist)
        dist = rad2deg(rad: dist)
        dist = dist * 60 * 1.1515
        if (unit == "K") {
            dist = dist * 1.609344
        }
        else if (unit == "N") {
            dist = dist * 0.8684
        }
        return dist
    }
}


