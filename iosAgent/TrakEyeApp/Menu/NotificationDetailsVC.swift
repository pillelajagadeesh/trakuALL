//
//  NotificationDetailsVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 22/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire

class NotificationDetailsVC: UIViewController {
    
    var createDateField = UILabel()
    var DescriptionText = UILabel()
    var satusField = UILabel()
    var subjectField = UILabel()
    var alertTypeField = UILabel()
    var fromUserField = UILabel()
    var toUserField = UILabel()
    var caseField = UILabel()
    var idValue = NSInteger()
    var arrayNotification =  NSDictionary()
    var caseTitle = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        let notificationdata = fetchNotificationdata()
        
        let WIDTH = ScreenSize.SCREEN_WIDTH-20
        
        let createDateTitle = UILabel()
        createDateTitle.frame = CGRect(x: 10, y: 74, width: WIDTH, height: 25)
        createDateTitle.text = "Created Date"
        createDateTitle.textColor = UIColor(rgb: 0x03a9f5)
        self.view.addSubview(createDateTitle)
        
        createDateField.frame = CGRect(x: 10, y: createDateTitle.maxYOrigin, width: WIDTH, height: 30)
        self.view.addSubview(createDateField)
        designLabels123(firstLab: createDateTitle, secondLab: createDateField)
        
        let DescriptionTitle = UILabel()
        DescriptionTitle.frame = CGRect(x: 10, y: (createDateField.frame.origin.y + createDateField.frame.size.height) + 10, width: WIDTH, height: 25)
        DescriptionTitle.text = "Description"
        DescriptionTitle.textColor = UIColor(rgb: 0x03a9f5)
        self.view.addSubview(DescriptionTitle)
        
        
        let labelSize = rectForText(text: notificationdata.descrip, font: UIFont(name: fontName, size: 10)!, maxSize: CGSize(width: WIDTH, height: 999))
        let labelHeight = labelSize.height + 30.0
        
        DescriptionText.frame = CGRect(x: 10, y: DescriptionTitle.maxYOrigin, width: WIDTH, height: labelHeight)
        self.view.addSubview(DescriptionText)
        DescriptionText.numberOfLines = 0
        designLabels123(firstLab: DescriptionTitle, secondLab: DescriptionText)
        
        //        let satusTitle = UILabel()
        //        satusTitle.frame = CGRect(x: 10, y: DescriptionText.maxYOrigin, width: WIDTH, height: 25)
        //        satusTitle.text = "Status"
        //        satusTitle.textColor = LOGIN_BG_COLOR
        //        self.view.addSubview(satusTitle)
        //
        //        satusField.frame = CGRect(x: 10, y: satusTitle.maxYOrigin, width: WIDTH, height: 30)
        //        self.view.addSubview(satusField)
        //        designLabels123(firstLab: satusTitle, secondLab: satusField)
        
        
        let subjectTitle = UILabel()
        subjectTitle.frame = CGRect(x: 10, y: DescriptionText.maxYOrigin, width: WIDTH, height: 25)
        subjectTitle.text = "Subject"
        subjectTitle.textColor = UIColor(rgb: 0x03a9f5)
        self.view.addSubview(subjectTitle)
        
        subjectField.frame = CGRect(x: 10, y: subjectTitle.maxYOrigin, width: WIDTH, height: 30)
        self.view.addSubview(subjectField)
        designLabels123(firstLab: subjectTitle, secondLab: subjectField)
        
        let alertTitle = UILabel()
        alertTitle.frame = CGRect(x: 10, y: subjectField.maxYOrigin , width: WIDTH, height: 25)
        alertTitle.text = "Alert"
        alertTitle.textColor = UIColor(rgb: 0x03a9f5)
        self.view.addSubview(alertTitle)
        
        alertTypeField.frame = CGRect(x: 10, y: alertTitle.maxYOrigin, width: WIDTH, height: 30)
        self.view.addSubview(alertTypeField)
        designLabels123(firstLab: alertTitle, secondLab: alertTypeField)
        
        let fromUserTitle = UILabel()
        fromUserTitle.frame = CGRect(x: 10, y: alertTypeField.maxYOrigin , width: WIDTH, height: 25)
        fromUserTitle.text = "From User"
        fromUserTitle.textColor = UIColor(rgb: 0x03a9f5)
        self.view.addSubview(fromUserTitle)
        
        fromUserField.frame = CGRect(x: 10, y: fromUserTitle.maxYOrigin, width: WIDTH, height: 30)
        self.view.addSubview(fromUserField)
        designLabels123(firstLab: fromUserTitle, secondLab: fromUserField)
        
        //        let toUserTitle = UILabel()
        //        toUserTitle.frame = CGRect(x: 10, y: fromUserField.maxYOrigin, width: WIDTH, height: 25)
        //        toUserTitle.text = "To User"
        //        toUserTitle.textColor = LOGIN_BG_COLOR
        //        self.view.addSubview(toUserTitle)
        //
        //        toUserField.frame = CGRect(x: 10, y: toUserTitle.maxYOrigin, width: WIDTH, height: 30)
        //        self.view.addSubview(toUserField)
        //        designLabels123(firstLab: toUserTitle, secondLab: toUserField)
        
        
        caseTitle.frame = CGRect(x: 10, y: fromUserField.maxYOrigin, width: WIDTH, height: 25)
        caseTitle.text = "Case"
        caseTitle.textColor = UIColor(rgb: 0x03a9f5)
        caseTitle.isHidden = true
        self.view.addSubview(caseTitle)
        
        caseField.frame = CGRect(x: 10, y: caseTitle.maxYOrigin, width: WIDTH, height: 30)
        caseField.isHidden = true
        self.view.addSubview(caseField)
        
        
        
        self.callService()
       
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    //TODO: GET DATA FROM API
    
    func callService(){
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            let urlString = "\(kGetallNotifications)/\(idValue)"
            print(urlString)
            
            let utliti = Utilities()
            let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
            showprogress(view: self.view)
            self.navigationItem.rightBarButtonItem?.isEnabled = false
            utliti.getDICTIONARYfromserverwithstring(input: urlString,parameters: parameters) { (true,result) in
                
                self.arrayNotification  = result as! NSDictionary
                print(self.arrayNotification)
                
                let status = self.arrayNotification.value(forKey: "status") as? String
                if status == "SENT"{
                     self.callupdateNotification()
                }
               
                let milisecond = self.arrayNotification.value(forKey: "createdDate") as! Double
                
                self.DescriptionText.text = self.arrayNotification.value(forKey: "description") as? String
                self.createDateField.text = milisecond.toHour//commonFunctions().millsecToDate(milli: milisecond!)
                self.satusField.text = self.arrayNotification.value(forKey: "status") as? String
                self.subjectField.text = self.arrayNotification.value(forKey: "subject") as? String
                self.alertTypeField.text = self.arrayNotification.value(forKey: "alertType") as? String
                self.fromUserField.text = self.arrayNotification.value(forKey: "fromUserName") as? String
                self.toUserField.text = self.arrayNotification.value(forKey: "toUserName") as? String
                
                
                let notificationType = self.arrayNotification.value(forKey: "notificationType") as? String
                if(notificationType == "A"){
                    self.caseTitle.isHidden = true
                    self.caseField.isHidden = true
                }else
                {
                    self.caseTitle.isHidden = false
                    self.caseField.isHidden = false
                    designLabels123(firstLab: self.caseTitle, secondLab: self.caseField)
                    let caseID = self.arrayNotification.value(forKey: "trCaseId") as! NSInteger
                    self.caseField.text = "\(caseID)"
                }
                hideprogress()
                
            }
        }
    }
    
    func callupdateNotification(){
        
        let userdata = fetchuserdata()
        let tokenStr =  userdata.userid
        
        let parameters : [String : Any] = ["alertType" : self.arrayNotification.value(forKey: "alertType")  ,
                                           "createdDate" : self.arrayNotification.value(forKey: "createdDate") as! NSInteger,
                                           "description" : self.arrayNotification.value(forKey: "description") as! String,
                                           "downloadLink" : self.arrayNotification.value(forKey: "downloadLink") as? String,
                                           "fromUserId" : self.arrayNotification.value(forKey: "fromUserId") as! NSInteger,
                                           "fromUserName" : self.arrayNotification.value(forKey: "fromUserName") as! String,
                                           "id" : self.arrayNotification.value(forKey: "id") as! NSInteger,
                                           "notificationType" : self.arrayNotification.value(forKey: "notificationType") as! String,
                                           "status" : "RECIEVED",
                                           "subject" : self.arrayNotification.value(forKey: "subject") as! String,
                                           "toUserId" : self.arrayNotification.value(forKey: "toUserId") as! NSInteger,
                                           "toUserName" : self.arrayNotification.value(forKey: "toUserName") as! String,
                                           "trCaseId" : self.arrayNotification.value(forKey: "trCaseId") as? NSInteger
        ]
        
        print(parameters)
        
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            
            let headers: HTTPHeaders = [
                "Authorization": "Bearer \(tokenStr)",
                "Accept": "application/json"
            ]
            showprogress(view: self.view)
            
            Alamofire.request("\(BaseUrl)\(kGetallNotifications)", method: .put, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON { response in
                if(response.result.isFailure){
                    hideprogress()
                    print("no data!")
                }else{
                    if(response.response?.statusCode==200)
                    {
                        print(response.result.value!)
                        hideprogress()
                    }else
                    {
                        print(response.result.value!)
                        hideprogress()
                    }
                }
                
            }
            
        } else {
            print("Internet connection FAILED")
            showerrorprogress(title: kInterenetAlertMessage, view: self.view)
        }
        
    }
    
}
