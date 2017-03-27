//
//  MycaseInfo.swift
//  TrakEyeApp
//
//  Created by Mitansh on 23/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class MycaseInfo: NSObject, NSCoding {

    var updatedDate = NSNumber()
    var createdDate = NSNumber()
    var descrip = String()
    var address = String()
    var status = String()
    var escalated = String()
    var reportedby = String()
    var assignedto = String()
    var caseID = NSNumber()
    var updatedby = String()
    var imagesArray = NSArray()
    
    static let sharedInstance = UserInfo()
    
    override init() {
        super.init()
    }
    
    func encode(with aCoder: NSCoder) {
        
        aCoder.encode(updatedDate, forKey: "update")
        aCoder.encode(descrip, forKey: "fname")
        aCoder.encode(address, forKey: "lname")
        aCoder.encode(status, forKey: "stat")
        aCoder.encode(escalated, forKey: "sub")
        aCoder.encode(reportedby, forKey: "funame")
        aCoder.encode(createdDate, forKey: "create")
        aCoder.encode(assignedto, forKey: "tuname")
        aCoder.encode(caseID, forKey: "case")
        aCoder.encode(updatedby, forKey: "updatedby")
        aCoder.encode(imagesArray, forKey: "Images")
    }
    
    required  init?(coder aDecoder: NSCoder) {
        
        
        if let blogName = aDecoder.decodeObject(forKey: "update") as? NSNumber {
            self.updatedDate = blogName
        }
        if let descrip = aDecoder.decodeObject(forKey: "fname") as? String {
            self.descrip = descrip
        }
        if let alertType = aDecoder.decodeObject(forKey: "lname") as? String {
            self.address = alertType
        }
        if let status = aDecoder.decodeObject(forKey: "stat") as? String {
            self.status = status
        }
        if let subject = aDecoder.decodeObject(forKey: "sub") as? String {
            self.escalated = subject
        }
        if let fromUser = aDecoder.decodeObject(forKey: "funame") as? String {
            self.reportedby = fromUser
        }
        if let created = aDecoder.decodeObject(forKey: "create") as? NSNumber {
            self.createdDate = created
        }
        if let toUser = aDecoder.decodeObject(forKey: "tuname") as? String {
            self.assignedto = toUser
        }
        if let caseID = aDecoder.decodeObject(forKey: "case") as? NSNumber {
            self.caseID = caseID
        }
        if let updated = aDecoder.decodeObject(forKey: "updatedby") as? String {
            self.updatedby = updated
        }
        
        if let arrImages = aDecoder.decodeObject(forKey: "Images") as? NSArray {
            self.imagesArray = arrImages
        }
    }
    
    
    
    func updateUserwithValues(responsedata:NSDictionary)
    {
        
        if let update = responsedata.value(forKeyPath: "caseType.updateDate") as? NSNumber {
            updatedDate = update
        }
        
        descrip = responsedata.object(forKey: "description") as! String
        
        if let add = responsedata.object(forKey: "address") as? String{
            address = add//responsedata.object(forKey: "address") as! String
        }
        
        imagesArray = responsedata.value(forKey: "caseImages") as! NSArray
        
        status = responsedata.object(forKey: "status") as! String
        if (responsedata.object(forKey: "escalated") as? Bool)!{
            self.escalated = "YES"//responsedata.object(forKey: "escalated") as! Bool
        }else
        {
            self.escalated = "NO"
        }
        reportedby = responsedata.object(forKey: "reportedByUser") as! String
        if let create = responsedata.value(forKeyPath: "caseType.createdDate") as? NSNumber {
            createdDate = create
        }
        
        assignedto = responsedata.object(forKey: "assignedToUser") as! String
        if responsedata.object(forKey: "id") as? NSNumber != nil{
            self.caseID = responsedata.object(forKey: "id") as! NSNumber
        }else
        {
            self.caseID = 0
        }
        updatedby = responsedata.object(forKey: "updatedByUser") as! String
    }
    
}
