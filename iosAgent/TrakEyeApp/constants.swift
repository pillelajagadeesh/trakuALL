//
//  constants.swift
//  TrakEyeApp
//
//  Created by Mitansh on 17/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import Foundation
import UIKit
import CoreLocation

struct ScreenSize
{
    static let SCREEN_WIDTH = UIScreen.main.bounds.size.width
    static let SCREEN_HEIGHT = UIScreen.main.bounds.size.height
    static let SCREEN_MAX_LENGTH = max(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
    static let SCREEN_MIN_LENGTH = min(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
}

struct DeviceType
{
    static let IS_IPHONE_5 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 568.0
    static let IS_IPHONE_6 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 667.0
    static let IS_IPHONE_6P = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 736.0
    static let IS_IPAD = UIDevice.current.userInterfaceIdiom == .pad && ScreenSize.SCREEN_MAX_LENGTH == 1024
}

//struct API
//{
//    static let BaseUrl : String = "http://dev.trakeye.com/api/"
//    static let kUserLogin : String = "authenticate"
//    static let kLocationlogs:String = "location-logs"
//    static let KGetmycases : String = "tr-cases"
//    static let kGetprofileInfo :String = "account"
//    static let kGetallNotifications : String = "tr-notifications"
//    static let kGetServices : String = "tr-services"
//    static let kGetLatestLogs : String = "location-logs/latest"
//    static let kGetCasetypes : String = "case-types"
//    static let kUpdateusergps : String = "updateusergpsstatus"
//    static let ksearchMycase : String = "tr-cases/searchvalue/"
//    static let ksearchService : String = "tr-services/searchvalue/"
//    static let ksearchNotification : String = "tr-notifications/searchvalue/"
//    static let kGetAssets : String = "assets"
//    static let kAssetTypes : String = "asset-types"
//    static let kAssetSearch : String = "assets/searchvalue/"
//}




let APP_DELEGATE  = UIApplication.shared.delegate as! AppDelegate

let BaseUrl : String = "http://trakeye.com/api/"


let kUserLogin : String = "authenticate"
let kLocationlogs:String = "location-logs"
let KGetmycases : String = "tr-cases"
let kGetprofileInfo :String = "account"
let kGetallNotifications : String = "tr-notifications"
let kGetServices : String = "tr-services"
let kGetLatestLogs : String = "dashboard/agentdashboard"//"location-logs/latest"
let kGetCasetypes : String = "case-types"
let kUpdateusergps : String = "updateusergpsstatus"
let ksearchMycase : String = "tr-cases/searchvalue/"
let ksearchService : String = "tr-services/searchvalue/"
let ksearchNotification : String = "tr-notifications/searchvalue/"
let kGetAssets : String = "assets"
let kAssetTypes : String = "asset-types"
let kAssetSearch : String = "assets/searchvalue/"
let kLocationBulklogs:String = "location-bulklogs"
var globalLat : CLLocationDegrees!
var globalLong : CLLocationDegrees!
var globalAddress : String = ""
let kReachabilityChangedNotification : String = "kNetworkReachabilityChangedNotification"

let Title : String = project_getAppName() as String


var START_PAGINATION_AT_INDEX : Int = 0
var TOTAL_COUNT : Int = 0
var TOTAL_PAGES : Int = 0
var PAGE_COUNT : Int = 10
//MARK: Response Keys

let kInterenetAlert : String = "No Internet Connection"
let kInterenetAlertMessage:String = "Make sure your device is connected to the internet."

let Statuscode : String = "status_code"
let messageKey : String = "message"
let hud = JGProgressHUD()

func isValidEmail(testStr:String) -> Bool {
    
    let emailRegEx = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    
    let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
    return emailTest.evaluate(with: testStr)
}

func saveuserdata(user : UserInfo) ->Void
{
    let encoder = NSKeyedArchiver.archivedData(withRootObject: user)
    UserDefaults.standard.set(encoder, forKey: "userinfo");
    UserDefaults.standard.synchronize()
}

func fetchuserdata() ->UserInfo
{
    let decode = UserDefaults.standard.object(forKey: "userinfo") as! NSData
    return NSKeyedUnarchiver.unarchiveObject(with: decode as Data) as! UserInfo
}

func saveNotificationdata(user : NotificationInfo) ->Void
{
    let encoder = NSKeyedArchiver.archivedData(withRootObject: user)
    UserDefaults.standard.set(encoder, forKey: "notificationInfo");
    UserDefaults.standard.synchronize()
}

func fetchNotificationdata() ->NotificationInfo
{
    let decode = UserDefaults.standard.object(forKey: "notificationInfo") as! NSData
    return NSKeyedUnarchiver.unarchiveObject(with: decode as Data) as! NotificationInfo
}

func saveMycasedata(user : MycaseInfo) ->Void
{
    let encoder = NSKeyedArchiver.archivedData(withRootObject: user)
    UserDefaults.standard.set(encoder, forKey: "MycaseInfo");
    UserDefaults.standard.synchronize()
}

func fetchMycasedata() ->MycaseInfo
{
    let decode = UserDefaults.standard.object(forKey: "MycaseInfo") as! NSData
    return NSKeyedUnarchiver.unarchiveObject(with: decode as Data) as! MycaseInfo
}


func showAlertMessage(titleStr:String, messageStr:String) -> Void {
    let alert = UIAlertController(title: titleStr, message: messageStr, preferredStyle: UIAlertControllerStyle.alert);
    let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
        print("OK")
    }
    alert.addAction(okAction)
    if let viewController = UIApplication.shared.windows.first?.rootViewController as UIViewController? {
        viewController.present(alert, animated: true, completion: nil)
    }
}


func showprogress(view:UIView)->Void{
    
    hud.progress=Float(JGProgressHUDStyle.dark.rawValue)
    hud.textLabel.text="Loading..."
    hud.show(in: view)

}

func hideprogress(){
    hud.dismiss()
}

func showerrorprogress(title:String, view:UIView)->Void{
    let huD = JGProgressHUD()
    huD.textLabel.text = title
    huD.indicatorView = JGProgressHUDErrorIndicatorView() //JGProgressHUDSuccessIndicatorView is also available
    huD.show(in: view)
    huD.dismiss(afterDelay: 2.0)
}

func showSuccessprogress(title:String, view:UIView)->Void{
    let huD = JGProgressHUD()
    huD.textLabel.text = title
    huD.indicatorView = JGProgressHUDSuccessIndicatorView() //JGProgressHUDSuccessIndicatorView is also available
    huD.show(in: view)
    huD.dismiss(afterDelay: 2.0)
}



func rememberme(rememberme : Bool)->Void
{
    UserDefaults.standard.set(rememberme, forKey: "Rememberme")
    UserDefaults.standard.synchronize()
}

func getRememberme() -> Bool
{
    return UserDefaults.standard.bool(forKey: "Rememberme")
}

func project_getAppName()->NSString {
    let infoDictionary: NSDictionary = Bundle.main.infoDictionary as NSDictionary!
    let appName: NSString = infoDictionary.object(forKey: "CFBundleName") as! NSString
    
    return appName
}

func queryStringFromParameters(parameters: Dictionary<String,String>) -> String? {
    if (parameters.count == 0)
    {
        return nil
    }
    var queryString : String? = nil
    
    for (key, value) in parameters {
        if let encodedKey = key.URLEncodedString() {
            if let encodedValue = value.URLEncodedString() {
                if queryString == nil
                {
                    queryString = "?"
                }
                else
                {
                    queryString! += "&"
                }
                queryString! += encodedKey + "=" + encodedValue
            }
        }
    }
    return queryString
}


func designLabels123(firstLab:UILabel,secondLab:UILabel){
    firstLab.textColor = LOGIN_BG_COLOR
    firstLab.font = UIFont(name: fontName, size: 14)
    secondLab.textColor = UIColor.black
    secondLab.font = UIFont(name: fontName, size: 10)
    let bord = UILabel()
    bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
    bord.backgroundColor = UIColor.lightGray
    secondLab.superview!.addSubview(bord)
}
func designtextV123(firstLab:UILabel,secondLab:UITextView){
    firstLab.textColor = LOGIN_BG_COLOR
    firstLab.font = UIFont(name: fontName, size: 14)
    secondLab.textColor = UIColor.black
    secondLab.font = UIFont(name: fontName, size: 14)
    let bord = UILabel()
    bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
    bord.backgroundColor = UIColor.lightGray
    secondLab.superview!.addSubview(bord)
    secondLab.isEditable = false
    secondLab.isScrollEnabled = true
}
func designtext123(firstLab:UILabel,secondLab:UITextField){
    firstLab.textColor = LOGIN_BG_COLOR
    firstLab.font = UIFont(name: fontName, size: 14)
    secondLab.textColor = UIColor.black
    secondLab.font = UIFont(name: fontName, size: 14)
    let bord = UILabel()
    bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
    bord.backgroundColor = UIColor.lightGray
    secondLab.superview!.addSubview(bord)
    secondLab.isUserInteractionEnabled = false
}

func rectForText(text: String, font: UIFont, maxSize: CGSize) -> CGSize {
    let attrString = NSAttributedString.init(string: text, attributes: [NSFontAttributeName:font])
    let rect = attrString.boundingRect(with: maxSize, options: NSStringDrawingOptions.usesLineFragmentOrigin, context: nil)
    let size = CGSize(width: rect.size.width, height: rect.size.height)
    return size
}

func resizeImage(image: UIImage, newWidth: CGFloat) -> UIImage {
    
    let scale = newWidth / image.size.width
    let newHeight = image.size.height * scale
    UIGraphicsBeginImageContext(CGSize(width: newWidth, height: newHeight))
    image.draw(in: CGRect(x: 0, y: 0, width: newWidth, height: newHeight))
    let newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    
    return newImage!
}


//func getlistofapps(){
//    
//    var LSApplicationWorkspace_class: AnyClass = objc_getClass("LSApplicationWorkspace") as! AnyClass
//    var selector: Selector = NSSelectorFromString("defaultWorkspace")
//    var workspace: NSObject? = LSApplicationWorkspace_class.perform(selector)
//    var selectorALL: Selector = NSSelectorFromString("allApplications")
//    print("apps: \(workspace?.perform(selectorALL))")
//}
//


func mach_task_self() -> task_t {
    return mach_task_self_
}

func getMegabytesUsed() -> Float? {
    var info = mach_task_basic_info()
    var count = mach_msg_type_number_t(MemoryLayout.size(ofValue: info) / MemoryLayout<integer_t>.size)
    let kerr = withUnsafeMutablePointer(to: &info) { infoPtr in
        return infoPtr.withMemoryRebound(to: integer_t.self, capacity: Int(count)) { (machPtr: UnsafeMutablePointer<integer_t>) in
            return task_info(
                mach_task_self(),
                task_flavor_t(MACH_TASK_BASIC_INFO),
                machPtr,
                &count
            )
        }
    }
    guard kerr == KERN_SUCCESS else {
        return nil
    }
    return Float(info.resident_size / 1024 / 1024)
}



func report_memory() {
    var taskInfo = mach_task_basic_info()
    var count = mach_msg_type_number_t(MemoryLayout<mach_task_basic_info>.size)/4
    let kerr: kern_return_t = withUnsafeMutablePointer(to: &taskInfo) {
        $0.withMemoryRebound(to: integer_t.self, capacity: 1) {
            task_info(mach_task_self_, task_flavor_t(MACH_TASK_BASIC_INFO), $0, &count)
        }
    }
    if kerr == KERN_SUCCESS {
        print("Memory used in MB: \(taskInfo.resident_size/1024/1024)")
        print("Total memory in GB : \(ProcessInfo.processInfo.physicalMemory)")
    }
    else {
        print("Error with task_info(): " +
            (String(cString: mach_error_string(kerr), encoding: String.Encoding.ascii) ?? "unknown error"))
    }
}


func __getMemoryUsedPer1() -> Float
{
    var taskInfo = mach_task_basic_info()
    let MACH_TASK_BASIC_INFO_COUNT = (MemoryLayout<mach_task_basic_info_data_t>.size / MemoryLayout<natural_t>.size)
    var name = mach_task_self_
    var flavor = task_flavor_t(MACH_TASK_BASIC_INFO)
    var size = mach_msg_type_number_t(MACH_TASK_BASIC_INFO_COUNT)
    let infoPointer = UnsafeMutablePointer<mach_task_basic_info>.allocate(capacity: 1)
    //let kerr = task_info(name, flavor, UnsafeMutablePointer(infoPointer), &size)
    let kerr: kern_return_t = withUnsafeMutablePointer(to: &taskInfo) {
        $0.withMemoryRebound(to: integer_t.self, capacity: 1) {
            task_info(name, flavor, $0, &size)
        }
    }
    let info = infoPointer.move()
    infoPointer.deallocate(capacity: 1)
    if kerr == KERN_SUCCESS
    {
        let used_bytes: Float = Float(info.resident_size)
        let total_bytes: Float = Float(ProcessInfo.processInfo.physicalMemory)
        print("Used: \(used_bytes / 1024.0 / 1024.0) MB out of \(total_bytes / 1024.0 / 1024.0) MB (\(used_bytes * 100.0 / total_bytes)%%)")
        return used_bytes / total_bytes
    }
    return 1
}



//func print_free_memory()
//{
//    var host_port = mach_port_t()
//    let HOST_VM_INFO_COUNT: mach_msg_type_number_t = mach_msg_type_number_t(MemoryLayout<vm_statistics_data_t>.size / MemoryLayout<integer_t>.size)
//    var pagesize = vm_size_t()
//    host_port = mach_host_self()
//    
//    host_page_size(host_port, &pagesize)
//    let vm_stat = vm_statistics_data_t()
//    var count: mach_msg_type_number_t = HOST_VM_INFO_COUNT
////    if host_statistics(host_port, HOST_VM_INFO, (vm_stat as? host_info_t), host_size) != KERN_SUCCESS {
////        print("Failed to fetch vm statistics")
////    }
////    let hostInfo = host_basic_info_t.allocate(capacity: 1)
////    let kernStatus: kern_return_t = hostInfo.withMemoryRebound(to: integer_t.self, capacity: Int(HOST_VM_INFO_COUNT)) {
////        host_info(mach_host_self(), HOST_BASIC_INFO, $0, &count)
////    }
//    
//    /* Stats in bytes */
//    let mem_used: natural_t = (vm_stat.active_count + vm_stat.inactive_count + vm_stat.wire_count) * pagesize
//    let mem_free: natural_t = vm_stat.free_count * pagesize
//    let mem_total: natural_t = mem_used + mem_free
//    print("used: \(mem_used) free: \(mem_free) total: \(mem_total)")
//}


func systemFreeMemorySize() -> UInt?
{
    let HOST_VM_INFO_COUNT: mach_msg_type_number_t = mach_msg_type_number_t(MemoryLayout<vm_statistics_data_t>.size / MemoryLayout<integer_t>.size)
    
    let host: host_t = mach_host_self()
    
    var pageSize: vm_size_t = vm_size_t()
    let hostPageSizeKernStatus: kern_return_t = host_page_size(host, &pageSize)
    guard hostPageSizeKernStatus == KERN_SUCCESS else {
        NSLog("Error with host_page_size(): " + (String.init(describing: mach_error_string(hostPageSizeKernStatus)) ))
        return nil
    }
    
    var stats: vm_statistics_data_t = vm_statistics_data_t()
    var count: mach_msg_type_number_t = HOST_VM_INFO_COUNT
    
    
//    let kernStatus: kern_return_t = withUnsafeMutablePointer(to: &stats) {_ in 
//        return host_statistics(host, HOST_VM_INFO, stats, &count)
//    }
    let hostInfo = host_basic_info_t.allocate(capacity: 1)
    let kernStatus: kern_return_t = hostInfo.withMemoryRebound(to: integer_t.self, capacity: Int(HOST_VM_INFO_COUNT)) {
        host_info(mach_host_self(), HOST_BASIC_INFO, $0, &count)
    }

    
    guard kernStatus == KERN_SUCCESS else {
        NSLog("Error with host_statistics(): " + (String.init(describing: mach_error_string(kernStatus)) ))
        return nil
    }
    
    return UInt(stats.free_count) * UInt(pageSize)
}

extension String {
    func URLEncodedString() -> String? {
        let escapedString = self.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
        return escapedString
    }
    static func queryStringFromParameters(parameters: Dictionary<String,String>) -> String? {
        if (parameters.count == 0)
        {
            return nil
        }
        var queryString : String? = nil
        for (key, value) in parameters {
            if let encodedKey = key.URLEncodedString() {
                if let encodedValue = value.URLEncodedString() {
                    if queryString == nil
                    {
                        queryString = "?"
                    }
                    else
                    {
                        queryString! += "&"
                    }
                    queryString! += encodedKey + "=" + encodedValue
                }
            }
        }
        return queryString
    }
}

class LatestLogs{
    var statusText = UILabel()
    var nameText = UILabel()
    var locationText = UILabel()
    var batteryLevel = UILabel()
    var startTime = UILabel()
    
}



