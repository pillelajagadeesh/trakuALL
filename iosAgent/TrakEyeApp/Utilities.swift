//
//  Utilities.swift
//  TrakEyeApp
//
//  Created by Mitansh on 23/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire

class Utilities: NSObject {

    
    
    func getdatafromserverwithstring(input: String, parameters : [String: Any],  completionHandler:@escaping (Bool, _ result:Any) -> ())->() {
        
        let urlstring = BaseUrl + input
        
        let userdata = fetchuserdata()
        
        let tokenstring:String! = userdata.userid
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            
            showprogress(view: APP_DELEGATE.window!)
            
            let headers: HTTPHeaders = [
                "Authorization": "Bearer \(tokenstring!)",
                "Accept": "application/json"
            ]
            print(headers)
            
            Alamofire.request(urlstring, method: .get, parameters: parameters, encoding: URLEncoding.default, headers: headers).responseJSON { (response:DataResponse<Any>) in
                
                if(response.result.isFailure){
                     hideprogress()
                    showalertforuserAthentication()
                    print("no data!")
                    completionHandler(false, response.result.isFailure)
                }else if((response.result.error) != nil){
                     hideprogress()
                    print(response.result.error?.localizedDescription ?? String())
                    showAlertMessage(titleStr: kInterenetAlert, messageStr: (response.result.error?.localizedDescription)!)
                }
                else{
                    if(response.response?.statusCode==200)
                    {
                        hideprogress()
                        let httpResponse = response.response! as HTTPURLResponse
                        let xDemAuth = httpResponse.allHeaderFields["X-Total-Count"] as? String
                        TOTAL_COUNT = Int(xDemAuth!)!
                        let value = Double(TOTAL_COUNT)/Double(PAGE_COUNT)
                        let count = ceil(value)
                        TOTAL_PAGES = Int(count)
                       print(TOTAL_COUNT,TOTAL_PAGES)
                       completionHandler(true, response.result.value as! NSArray)
                        
                    }else if(response.response?.statusCode==401)
                    {
                         hideprogress()
                        showalertforuserAthentication()
                        completionHandler(false, response.result.isFailure)
                    }
                    else if(response.response?.statusCode==403)
                    {
                         hideprogress()
                        completionHandler(false, response.result.value as! NSDictionary)
                    }
                    else
                    {
                         hideprogress()
                        completionHandler(false, response.result.value as! NSDictionary)
                    }
                }
            
            }
        }else{
            showAlertMessage(titleStr: kInterenetAlert, messageStr: kInterenetAlertMessage)
        }

    }
    
    func getDICTIONARYfromserverwithstring(input: String, parameters : [String: Any],  completionHandler:@escaping (Bool, _ result:Any) -> ())->() {
        
        let urlstring = BaseUrl + input
        
        let userdata = fetchuserdata()
        
        let tokenstring:String! = userdata.userid
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            
            let headers: HTTPHeaders = [
                "Authorization": "Bearer \(tokenstring!)",
                "Accept": "application/json"
            ]
            
            Alamofire.request(urlstring, method: .get, parameters: parameters, encoding: URLEncoding.default, headers: headers).responseJSON { (response:DataResponse<Any>) in
                
                if(response.result.isFailure){
                    showalertforuserAthentication()
                    print("no data!")
                    completionHandler(false, response.result.isFailure)
                }else if((response.result.error) != nil){
                    print(response.result.error?.localizedDescription ?? String())
                    showAlertMessage(titleStr: kInterenetAlert, messageStr: (response.result.error?.localizedDescription)!)
                }
                else{
                    if(response.response?.statusCode==200)
                    {
                        completionHandler(true, response.result.value as! NSDictionary)
                    }else if(response.response?.statusCode==401)
                    {
                        showalertforuserAthentication()
                        completionHandler(false, response.result.isFailure)
                    }
                    else if(response.response?.statusCode==403)
                    {
                        completionHandler(false, response.result.value as! NSDictionary)
                    }
                    else
                    {
                        completionHandler(false, response.result.value as! NSDictionary)
                    }
                }
            }
        }else{
            showAlertMessage(titleStr: kInterenetAlert, messageStr: kInterenetAlertMessage)
        }
        
    }
    
    
}
