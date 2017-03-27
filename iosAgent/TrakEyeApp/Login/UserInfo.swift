//
//  UserInfo.swift
//  TrakEyeApp
//
//  Created by Mitansh on 17/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class UserInfo: NSObject,NSCoding {
    
   
    
    var userid = String()
    var firstname = String()
    var lastname = String()
    var email = String()
    var userName = String()
    static let sharedInstance = UserInfo()
    
    override init() {
        super.init()
    }
    
    func encode(with aCoder: NSCoder) {
        aCoder.encode(userid, forKey: "tokenid")
        aCoder.encode(firstname, forKey: "fname")
        aCoder.encode(lastname, forKey: "lname")
        aCoder.encode(email, forKey: "email")
        aCoder.encode(userName, forKey: "user")
    }

    required  init?(coder aDecoder: NSCoder) {
        if let blogName = aDecoder.decodeObject(forKey: "tokenid") as? String {
            self.userid = blogName
        }
        if let firstname = aDecoder.decodeObject(forKey: "fname") as? String {
            self.firstname = firstname
        }
        if let lastname = aDecoder.decodeObject(forKey: "lname") as? String {
            self.lastname = lastname
        }
        if let email = aDecoder.decodeObject(forKey: "email") as? String {
            self.email = email
        }
        if let username = aDecoder.decodeObject(forKey: "user") as? String {
            self.userName = username
        }
    }
    
    func updatewithValues(responsedata:NSDictionary)
    {
        userid = responsedata.value(forKey: "id_token") as! String
        
    }
    
    func updateUserwithValues(responsedata:NSDictionary)
    {
        firstname = responsedata.object(forKey: "firstName") as! String
        lastname = responsedata.object(forKey: "lastName") as! String
        email = responsedata.object(forKey: "email") as! String
        userName = responsedata.object(forKey: "login") as! String
    }
    
}
