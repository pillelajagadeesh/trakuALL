//
//  commonFunctions.swift
//  TrakEyeApp
//
//  Created by Apple on 29/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import Foundation

class commonFunctions {
    
    func millsecToDate(milli:NSInteger) -> String{
    let milisecond: TimeInterval = TimeInterval(milli)
    let date = Date(timeIntervalSince1970: milisecond)
    let formatter = DateFormatter()
    formatter.dateFormat = "dd-MM-yyyy HH:mm:ss a"
    formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
    formatter.timeZone = NSTimeZone.system
    print(formatter.string(from: date as Date))
    return formatter.string(from: date as Date)
    }
    
    func millsecondsToDate(milli:NSInteger) -> String{
        //let milisecond = milli
        let milisecond: TimeInterval = TimeInterval(milli)
        
        let date = Date(timeIntervalSince1970: milisecond)
        let formatter = DateFormatter()
        formatter.dateFormat = "dd-MM-yyyy"
        formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
        formatter.timeZone = NSTimeZone.system
        print(formatter.string(from: date as Date))
        return formatter.string(from: date as Date)
    }
    
    
    func ResizeImage(image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        
        let widthRatio  = targetSize.width  / image.size.width
        let heightRatio = targetSize.height / image.size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio,  height: size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    
}
public func showalertforuserAthentication() -> Void {
    UserDefaults.standard.removeObject(forKey: "YES")
    
    let alert = UIAlertController(title: Title, message: "User is DeActivated", preferredStyle: UIAlertControllerStyle.alert);
    let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
        APP_DELEGATE.loginActioncall()
    }
    alert.addAction(okAction)
    if let viewController = UIApplication.shared.windows.first?.rootViewController as UIViewController? {
        viewController.present(alert, animated: true, completion: nil)
    }
}
/**
 *
 * Convert unix time to human readable time. Return empty string if unixtime
 * argument is 0. Note that EMPTY_STRING = ""
 *
 * @param unixdate the time in unix format, e.g. 1482505225
 * @param timezone the user's time zone, e.g. EST, PST
 * @return the date and time converted into human readable String format
 *
 **/

public func getDate(unixdate: Int, timezone: String) -> String {
    let date = NSDate(timeIntervalSince1970: TimeInterval(unixdate))
    let dayTimePeriodFormatter = DateFormatter()
    dayTimePeriodFormatter.locale = NSLocale.current
    dayTimePeriodFormatter.dateFormat = "MM/dd/YYYY hh:mm a"
    dayTimePeriodFormatter.timeZone = TimeZone.current
    let dateString = dayTimePeriodFormatter.string(from: date as Date)
    return "\(dateString)"
}
