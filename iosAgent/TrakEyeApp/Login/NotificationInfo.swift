//
//  NotificationInfo.swift
//  TrakEyeApp
//
//  Created by Mitansh on 22/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class NotificationInfo: NSObject, NSCoding {

    var userid = NSNumber()
    var descrip = String()
    var alertType = String()
    var status = String()
    var subject = String()
    var fromUserName = String()
    var createdDate = NSNumber()
    var toUserName = String()
    var caseID = NSNumber()
    
    static let sharedInstance = UserInfo()
    
    override init() {
        super.init()
    }
    
    func encode(with aCoder: NSCoder) {
        
        aCoder.encode(userid, forKey: "tokenid")
        aCoder.encode(descrip, forKey: "fname")
        aCoder.encode(alertType, forKey: "lname")
        aCoder.encode(status, forKey: "stat")
        aCoder.encode(subject, forKey: "sub")
        aCoder.encode(fromUserName, forKey: "funame")
        aCoder.encode(createdDate, forKey: "create")
        aCoder.encode(toUserName, forKey: "tuname")
        aCoder.encode(caseID, forKey: "case")
    }
    
    required  init?(coder aDecoder: NSCoder) {
        
        
        if let blogName = aDecoder.decodeObject(forKey: "tokenid") as? NSNumber {
            self.userid = blogName
        }
        if let descrip = aDecoder.decodeObject(forKey: "fname") as? String {
            self.descrip = descrip
        }
        if let alertType = aDecoder.decodeObject(forKey: "lname") as? String {
            self.alertType = alertType
        }
        if let status = aDecoder.decodeObject(forKey: "stat") as? String {
            self.status = status
        }
        if let subject = aDecoder.decodeObject(forKey: "sub") as? String {
            self.subject = subject
        }
        if let fromUser = aDecoder.decodeObject(forKey: "funame") as? String {
            self.fromUserName = fromUser
        }
        if let created = aDecoder.decodeObject(forKey: "create") as? NSNumber {
            self.createdDate = created
        }
        if let toUser = aDecoder.decodeObject(forKey: "tuname") as? String {
            self.toUserName = toUser
        }
        if let caseID = aDecoder.decodeObject(forKey: "case") as? NSNumber {
            self.caseID = caseID
        }
    }
    
    
    
    func updateUserwithValues(responsedata:NSDictionary)
    {
        userid = responsedata.value(forKey: "id") as! NSNumber
        descrip = responsedata.object(forKey: "description") as! String
        alertType = responsedata.object(forKey: "alertType") as! String
        status = responsedata.object(forKey: "status") as! String
        subject = responsedata.value(forKey: "subject") as! String
        fromUserName = responsedata.object(forKey: "fromUserName") as! String
        createdDate = responsedata.object(forKey: "createdDate") as! NSNumber
        toUserName = responsedata.object(forKey: "toUserName") as! String
        if responsedata.object(forKey: "trCaseId") as? NSNumber != nil{
             self.caseID = responsedata.object(forKey: "trCaseId") as! NSNumber
        }else
        {
            self.caseID = 0
        }
        
    }
}
