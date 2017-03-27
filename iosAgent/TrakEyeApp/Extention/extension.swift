//
//  extension.swift
//  TrakEyeApp
//
//  Created by Apple on 29/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import Foundation
import UIKit
var viewWidth:CGFloat = 0
var viewHeight:CGFloat = 0
/*---------------- getting width, position,height -----------------*/
extension UIView {
    
    var xOrigin: CGFloat {
        get {
            return frame.origin.x
        }
        set {
            var frame = self.frame
            frame.origin.x = newValue
            self.frame = frame
        }
    }
    
    var yOrigin: CGFloat {
        get {
            return frame.origin.y
        }
        set {
            var frame = self.frame
            frame.origin.y = newValue
            self.frame = frame
        }
    }
    
    var maxXOrigin: CGFloat {
        return frame.origin.x + frame.size.width
    }
    
    var maxYOrigin: CGFloat {
        return frame.origin.y  + frame.size.height
    }
    
    var midXOrigin: CGFloat {
        return center.x
    }
    
    var midYOrigin: CGFloat {
        return center.y
    }
    
    var width: CGFloat {
        get {
            return frame.size.width
        }
        set {
            var frame = self.frame
            frame.size.width = newValue
            self.frame = frame
        }
    }
    
    var height: CGFloat {
        get {
            return frame.size.height
        }
        set {
            var frame = self.frame
            frame.size.height = newValue
            self.frame = frame
        }
    }
    
    var size: CGSize {
        get {
            return frame.size
        }
        set {
            var frame = self.frame
            frame.size = newValue
            self.frame = frame
        }
    }
    
    var origin: CGPoint {
        get {
            return frame.origin
        }
        set {
            var frame = self.frame
            frame.origin = newValue
            self.frame = frame
        }
    }
    
}

extension UIImage {
    enum JPEGQuality: CGFloat {
        case lowest  = 0
        case low     = 0.25
        case medium  = 0.5
        case high    = 0.75
        case highest = 1
    }
    
    /// Returns the data for the specified image in PNG format
    /// If the image object’s underlying image data has been purged, calling this function forces that data to be reloaded into memory.
    /// - returns: A data object containing the PNG data, or nil if there was a problem generating the data. This function may return nil if the image has no data or if the underlying CGImageRef contains data in an unsupported bitmap format.
    var png: Data? { return UIImagePNGRepresentation(self) }
    
    /// Returns the data for the specified image in JPEG format.
    /// If the image object’s underlying image data has been purged, calling this function forces that data to be reloaded into memory.
    /// - returns: A data object containing the JPEG data, or nil if there was a problem generating the data. This function may return nil if the image has no data or if the underlying CGImageRef contains data in an unsupported bitmap format.
    func jpeg(_ quality: JPEGQuality) -> Data? {
        return UIImageJPEGRepresentation(self, quality.rawValue)
    }
    
    func resizeWith(percentage: CGFloat) -> UIImage? {
        let imageView = UIImageView(frame: CGRect(origin: .zero, size: CGSize(width: size.width * percentage, height: size.height * percentage)))
        imageView.contentMode = .scaleAspectFit
        imageView.image = self
        UIGraphicsBeginImageContextWithOptions(imageView.bounds.size, false, scale)
        guard let context = UIGraphicsGetCurrentContext() else { return nil }
        imageView.layer.render(in: context)
        guard let result = UIGraphicsGetImageFromCurrentImageContext() else { return nil }
        UIGraphicsEndImageContext()
        return result
    }
    func resizeWith(width: CGFloat) -> UIImage? {
        let imageView = UIImageView(frame: CGRect(origin: .zero, size: CGSize(width: width, height: CGFloat(ceil(width/size.width * size.height)))))
        imageView.contentMode = .scaleAspectFit
        imageView.image = self
        UIGraphicsBeginImageContextWithOptions(imageView.bounds.size, false, scale)
        guard let context = UIGraphicsGetCurrentContext() else { return nil }
        imageView.layer.render(in: context)
        guard let result = UIGraphicsGetImageFromCurrentImageContext() else { return nil }
        UIGraphicsEndImageContext()
        return result
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
}
extension Bool {
    init<T : Integer>(_ integer: T) {
        if integer == 0 {
            self.init(false)
        } else {
            self.init(true)
        }
    }
}

extension UINavigationController {
    func pop(animated: Bool) {
        _ = self.popViewController(animated: animated)
    }
    
    func popToRoot(animated: Bool) {
        _ = self.popToRootViewController(animated: animated)
    }
}


class colorCode {
    //Color Codes
    let CYAN = "CYAN"
    let BLACK = "BLACK"
    let BLUE = "BLUE"
    let BLUEVIOLET = "BLUEVIOLET"
    let BROWN = "BROWN"
    let CHARTREUSE = "CHARTREUSE"
    let CRIMSON = "CRIMSON"
    let YELLOW = "YELLOW"
    let MAGENTA = "MAGENTA"
    let DEEPPINK = "DEEPPINK"
    let LIGHTCORAL = "LIGHTCORAL"
    
}
//Color Codes
let CYAN:uint = 0x00FFFF
let BLACK:uint = 0x000000
let BLUE:uint = 0x0000FF
let BLUEVIOLET:uint = 0x8A2BE2
let BROWN:uint = 0xA52A2A
let CHARTREUSE:uint = 0x7FFF00
let CRIMSON:uint = 0xDC143C
let YELLOW:uint = 0xFFFF00
let MAGENTA:uint = 0x8B008B
let DEEPPINK:uint = 0xFF1493
let LIGHTCORAL:uint = 0xF08080


public func strToHexa(colorString:String) ->uint{
   // var coloo = colorCode.colorString
    switch colorString {
    case colorCode().CYAN:
        return CYAN
    case colorCode().BLACK:
        return BLACK
    case colorCode().BLUE:
        return BLUE
    case colorCode().BLUEVIOLET:
        return BLUEVIOLET
    case colorCode().BROWN:
        return BROWN
    case colorCode().CHARTREUSE:
        return CHARTREUSE
    case colorCode().CRIMSON:
        return CRIMSON
    case colorCode().YELLOW:
        return YELLOW
    case colorCode().MAGENTA:
        return MAGENTA
    case colorCode().DEEPPINK:
        return DEEPPINK
    case colorCode().LIGHTCORAL:
        return LIGHTCORAL
    default:
        return BLACK
    }
}


typealias UnixTime = Int

extension UnixTime {
    private func formatType(form: String) -> DateFormatter {
        let dateFormatter = DateFormatter()
        //dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.timeZone = NSTimeZone(name: "UTC") as TimeZone!
        dateFormatter.dateFormat = form
        
        return dateFormatter
    }
    var dateFull: Date {
        return Date(timeIntervalSince1970: Double(self))
    }
    var toHour: String {
        return formatType(form: "MM/dd/yyyy HH:mm a").string(from: dateFull)
    }
    var toDay: String {
        return formatType(form: "MM/dd/yyyy").string(from: dateFull)
    }
}

extension Double {
    
    private func formatType(form: String) -> DateFormatter {
        let dateFormatter = DateFormatter()
        //dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.timeZone = NSTimeZone(name: "UTC") as TimeZone!
        dateFormatter.dateFormat = form
        
        return dateFormatter
    }
    var Fulldate: NSDate {
        return NSDate(timeIntervalSince1970: self/1000)
    }
    var toHour: String {
        return formatType(form: "dd/MM/yyyy HH:mm a").string(from: Fulldate as Date)
    }
    var toDay: String {
        return formatType(form: "dd/MM/yyyy").string(from: Fulldate as Date)
    }
  
    func roundTo(places: Int) -> Double {
        let divisor = pow(10.0, Double(places))
        return (self * divisor).rounded() / divisor
    }
    
}

    

