//
//  NotificationsVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 21/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire

class NotificationsVC: BaseviewController,UITableViewDelegate,UITableViewDataSource {

    var tableView = UITableView()
    var arrayNotifications = NSMutableArray()
    var notificationdata = NotificationInfo()
    var notDict = NSDictionary()
    var filterdNotification = NSArray()
    var lastindex : Bool = false
    var nextAPICallIndex : Int = 1

    var searchIndex : Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
   
        geoFenceBtn.isHidden = true
        
        searchtext.placeholder = "Search by Title"
        
        tableView = UITableView(frame:CGRect(x: 0, y: searechbackgrndview.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-164) , style: UITableViewStyle.plain)
        tableView.delegate      =   self
        tableView.dataSource    =   self
        tableView.tableFooterView = UIView()
        tableView.layoutMargins = UIEdgeInsets.zero
        tableView.separatorInset = UIEdgeInsets.zero
        tableView.showsVerticalScrollIndicator = false
        tableView.cellLayoutMarginsFollowReadableWidth = false
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        self.view.addSubview(self.tableView)
        
        let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
        
        self.getnotificationswithparameter(parameters: parameters)
        
    }

    
    override func viewDidAppear(_ animated: Bool) {
        let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
        
        self.getnotificationswithparameter(parameters: parameters)
    }
    
   //TODO: Baseviewcontroller button Actions
    
    override func textFieldTyping(textField:UITextField)
    {
        if (textField.text?.characters.count)!>2 {
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            print(parameters)
            
            self.getfilternotifications(parameters: parameters)
            curruntpagelbl.text = "1"
            
        }else
        {
            
            nextBtn.isUserInteractionEnabled = true
            let count = "\(START_PAGINATION_AT_INDEX)"
            curruntpagelbl.text = "\(Int(count)! + 1)"
            
            let parameters:[String: Any] = ["page" : "\(count)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            showprogress(view: self.view)
            
            self.getnotificationswithparameter(parameters: parameters)
            
        }
    }
    
    
    override func previousBtnClicked(){
        
        if (searchtext.text?.characters.count)!>2{
             nextBtn.isUserInteractionEnabled = true
            var parameters = [String: Any]()
            
            if searchIndex >= 1 {
                searchIndex -= 1
                let count = "\(searchIndex)"
                curruntpagelbl.text = "\(Int(count)! + 1)"
                parameters = ["page" : "\(searchIndex)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            }
            else{
                curruntpagelbl.text = "1"
                parameters = ["page" : "\(searchIndex)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            }
            
            self.getfilternotifications(parameters: parameters)
            
        }else
        {
             nextBtn.isUserInteractionEnabled = true
            var parameters = [String: Any]()
            
            if START_PAGINATION_AT_INDEX >= 1 {
                START_PAGINATION_AT_INDEX -= 1
                let count = "\(START_PAGINATION_AT_INDEX)"
                curruntpagelbl.text = "\(Int(count)! + 1)"
                parameters = ["page" : "\(START_PAGINATION_AT_INDEX)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            }
            else{
                curruntpagelbl.text = "1"
                parameters = ["page" : "\(START_PAGINATION_AT_INDEX)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            }
            
            self.getnotificationswithparameter(parameters: parameters)
        }
        
    }
    
    override func nextBtnClicked(){
        
        
         if (searchtext.text?.characters.count)!>2{
            let count = searchIndex + 1
            
            if count == TOTAL_PAGES {
                nextBtn.isUserInteractionEnabled = false
            }else
            {
                searchIndex += 1
                curruntpagelbl.text = "\(searchIndex + 1)"
                
                let parameters:[String: Any] = ["page" : "\(searchIndex)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                self.getfilternotifications(parameters: parameters)
            }
            
            
         }else{
            if self.arrayNotifications.count>0{
                let count = START_PAGINATION_AT_INDEX + 1
                
                if count == TOTAL_PAGES {
                    nextBtn.isUserInteractionEnabled = false
                }else{
                    
                    START_PAGINATION_AT_INDEX += 1
                    curruntpagelbl.text = "\(START_PAGINATION_AT_INDEX + 1)"
                    
                    let parameters:[String: Any] = ["page" : "\(START_PAGINATION_AT_INDEX)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                    
                    self.getnotificationswithparameter(parameters: parameters)
                }
            }
            
            
        }
        
    }
    
    override func lastpagebtnclicked()
    {
        
         if (searchtext.text?.characters.count)!>2{
            
            searchIndex = TOTAL_PAGES - 1
            
            curruntpagelbl.text = "\(TOTAL_PAGES)"
            
            let parameters:[String: Any] = ["page" : "\(TOTAL_PAGES - 1)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getfilternotifications(parameters: parameters)
            
         }else{
            
            if self.arrayNotifications.count>0 {
                START_PAGINATION_AT_INDEX = TOTAL_PAGES - 1
                
                curruntpagelbl.text = "\(TOTAL_PAGES)"
                
                let parameters:[String: Any] = ["page" : "\(TOTAL_PAGES - 1)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                 self.getnotificationswithparameter(parameters: parameters)
            }else{
                curruntpagelbl.text = "1"
            }
            
           
        }
        
    }
    
    override func initialpageBtnclicked(){
        
         if (searchtext.text?.characters.count)!>2{
             nextBtn.isUserInteractionEnabled = true
            searchIndex = 0
            
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getfilternotifications(parameters: parameters)
            
         }else{
             nextBtn.isUserInteractionEnabled = true
            START_PAGINATION_AT_INDEX = 0
            
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getnotificationswithparameter(parameters: parameters)
        }
       
    }
    
    //TODO: GET DATA FROM API
    
    func getnotificationswithparameter(parameters : [String : Any])
    {
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: kGetallNotifications,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.arrayNotifications.removeAllObjects()
                self.arrayNotifications.addObjects(from: response as! [Any])
                self.tableView.reloadData()
            }
            else
            {
                print(result)
            }
        }
    }
    
    
    func getfilternotifications(parameters : [String : Any]){
        
//        if([searchtext.text?.caseInsensitiveCompare("My Case sensitiVE")] == NSOrderedSame)  {
//            // strings are equal except for possibly case
//        }
        
        let escapedString = searchtext.text!.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
        print(escapedString!)
        
        let urlinput = ksearchNotification + escapedString!
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: urlinput,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.filterdNotification  = response
                self.tableView.reloadData()
            }
            else
            {
                print(result)
            }
            
        }
    }
    
    //TODO: Tableview Delegate Methodes
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (searchtext.text?.characters.count)!>2 {
            return filterdNotification.count
        }
        return arrayNotifications.count
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        
//        cell.layer.transform = CATransform3DMakeScale(0.1,0.1,1)
//        UIView.animate(withDuration: 0.25, animations: {
//            cell.layer.transform = CATransform3DMakeScale(1,1,1)
//        })
//        let cellContentView: UIView? = cell.contentView
//        let rotationAngleDegrees: CGFloat = -30
//        let rotationAngleRadians: CGFloat = rotationAngleDegrees * (.pi / 180)
//        let offsetPositioning = CGPoint(x: CGFloat(500), y: CGFloat(-20.0))
//        var transform: CATransform3D = CATransform3DIdentity
//        transform = CATransform3DRotate(transform, rotationAngleRadians, -50.0, 0.0, 1.0)
//        transform = CATransform3DTranslate(transform, offsetPositioning.x, offsetPositioning.y, -50.0)
//        cellContentView?.layer.transform = transform
//        cellContentView?.layer.opacity = 0.8
//        UIView.animate(withDuration: 0.65, delay: 0.0, usingSpringWithDamping: 0.85, initialSpringVelocity: 0.8, options: [], animations: {() -> Void in
//            cellContentView?.layer.transform = CATransform3DIdentity
//            cellContentView?.layer.opacity = 1
//        }, completion: {(_ finished: Bool) -> Void in
//        })
        
        
        let cellContentView: UIView? = cell.contentView
        let rotationAngleDegrees: CGFloat = -30
        let rotationAngleRadians: CGFloat = rotationAngleDegrees * (.pi / 180)
        let offsetPositioning = CGPoint(x: CGFloat(0), y: CGFloat(cell.contentView.frame.size.height * 4))
        var transform: CATransform3D = CATransform3DIdentity
        transform = CATransform3DRotate(transform, rotationAngleRadians, -50.0, 0.0, 1.0)
        transform = CATransform3DTranslate(transform, offsetPositioning.x, offsetPositioning.y, -50.0)
        cellContentView?.layer.transform = transform
        cellContentView?.layer.opacity = 0.8
        UIView.animate(withDuration: 0.65, delay: 0o0, usingSpringWithDamping: 0.85, initialSpringVelocity: 0.8, options: [], animations: {() -> Void in
            cellContentView?.layer.transform = CATransform3DIdentity
            cellContentView?.layer.opacity = 1
        }, completion: {(_ finished: Bool) -> Void in
        })
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
        
        cell.layoutMargins = UIEdgeInsets.zero
        cell.separatorInset = UIEdgeInsets.zero
        cell.preservesSuperviewLayoutMargins = false
        
        if (searchtext.text?.characters.count)!>2 {
            if self.filterdNotification.count>0{
                let searchDict = self.filterdNotification[indexPath.row] as! NSDictionary
                let status = searchDict.value(forKey: "status") as? String
                let idLabel = UILabel()
                idLabel.frame = CGRect(x: 10, y: 5, width: 50, height: 50)
                idLabel.text = searchDict.value(forKey: "notificationType") as? String
                idLabel.textColor = UIColor.black
                idLabel.layer.cornerRadius = 25
                idLabel.layer.borderWidth = 1.0
                idLabel.layer.borderColor = UIColor(rgb: 0x03a9f5).cgColor
                idLabel.clipsToBounds = true
                if DeviceType.IS_IPAD{
                    idLabel.font = UIFont(name: fontBold, size: 25)
                }else{
                    idLabel.font = UIFont(name: fontBold, size: 20)
                }
                idLabel.textAlignment = NSTextAlignment.center
                cell.addSubview(idLabel)
                
                let subject = searchDict.value(forKey: "subject") as? String
                
                let labelSize = rectForText(text: subject!, font: UIFont(name: fontName, size: 10)!, maxSize: CGSize(width: ScreenSize.SCREEN_WIDTH-135, height: 999))
                let labelHeight = labelSize.height + 30.0
                
                let subjectLabel = UILabel()
                subjectLabel.frame = CGRect(x: 70, y: 5, width: ScreenSize.SCREEN_WIDTH-135, height: labelHeight)
                subjectLabel.text = searchDict.value(forKey: "subject") as? String
                subjectLabel.textColor = UIColor(rgb: 0x03a9f5)
                subjectLabel.numberOfLines = 0
                subjectLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
                if DeviceType.IS_IPAD{
                    subjectLabel.font = UIFont(name: fontBold, size: 15)
                }else{
                    subjectLabel.font = UIFont(name: fontBold, size: 10)
                }
                subjectLabel.textAlignment = NSTextAlignment.left
                cell.addSubview(subjectLabel)
                
                
                let dateLabel = UILabel()
                dateLabel.frame = CGRect(x:subjectLabel.maxXOrigin, y: 5, width: 60, height: 20)
                let createdate = searchDict.value(forKey: "createdDate") as! Double
                dateLabel.text = createdate.toDay//commonFunctions().millsecondsToDate(milli: searchDict.value(forKey: "createdDate") as! Int)
                dateLabel.textColor = UIColor.black
                dateLabel.numberOfLines = 3
                dateLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
                if DeviceType.IS_IPAD{
                    dateLabel.font = UIFont(name: fontName, size: 15)
                }else{
                    dateLabel.font = UIFont(name: fontName, size: 10)
                }
                dateLabel.textAlignment = NSTextAlignment.left
                cell.addSubview(dateLabel)
                
                let descriptionLabel = UILabel()
                descriptionLabel.frame = CGRect(x:70, y: 18, width: ScreenSize.SCREEN_WIDTH-75, height: 50)
                descriptionLabel.text = searchDict.value(forKey: "description") as? String
                descriptionLabel.textColor = UIColor.black
                descriptionLabel.numberOfLines = 3
                descriptionLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
                if DeviceType.IS_IPAD{
                    descriptionLabel.font = UIFont(name: fontName, size: 15)
                }else{
                    if status == "SENT"{
                        descriptionLabel.font = UIFont(name: "Helvetica-Bold", size: 10.0)
                    }else{
                        descriptionLabel.font = UIFont(name: fontName, size: 10)
                    }                }
                descriptionLabel.textAlignment = NSTextAlignment.left
                cell.addSubview(descriptionLabel)
            }else{
                
            }
            
        }else{
            
            let notifDict = self.arrayNotifications[indexPath.row] as! NSDictionary
            
            let status = notifDict.value(forKey: "status") as? String
            
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 10, y: 5, width: 50, height: 50)
            idLabel.text = notifDict.value(forKey: "notificationType") as? String
            idLabel.textColor = UIColor.black
            idLabel.layer.cornerRadius = 25
            idLabel.layer.borderWidth = 1.0
            idLabel.layer.borderColor = UIColor(rgb: 0x03a9f5).cgColor
            idLabel.clipsToBounds = true
            if DeviceType.IS_IPAD{
                 idLabel.font = UIFont(name: fontBold, size: 25)
            }else{
                 idLabel.font = UIFont(name: fontBold, size: 20)
            }
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            
            let subject = notifDict.value(forKey: "subject") as? String
            
            let labelSize = rectForText(text: subject!, font: UIFont(name: fontName, size: 10)!, maxSize: CGSize(width: ScreenSize.SCREEN_WIDTH-135, height: 999))
            let labelHeight = labelSize.height + 10.0
            
            let subjectLabel = UILabel()
            subjectLabel.frame = CGRect(x: 70, y: 5, width: ScreenSize.SCREEN_WIDTH-135, height: labelHeight)
            subjectLabel.text = notifDict.value(forKey: "subject") as? String
            subjectLabel.textColor = UIColor(rgb: 0x03a9f5)
            subjectLabel.numberOfLines = 0
            subjectLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
            if DeviceType.IS_IPAD{
                subjectLabel.font = UIFont(name: fontBold, size: 15)
            }else{
                subjectLabel.font = UIFont(name: fontBold, size: 10)
            }
            subjectLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(subjectLabel)
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:subjectLabel.maxXOrigin, y: 5, width: 60, height: 20)
            let createdate = notifDict.value(forKey: "createdDate") as! Double
            dateLabel.text = createdate.toDay
            dateLabel.textColor = UIColor.black
            dateLabel.numberOfLines = 3
            dateLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
            if DeviceType.IS_IPAD{
                dateLabel.font = UIFont(name: fontName, size: 15)
            }else{
                dateLabel.font = UIFont(name: fontName, size: 10)
            }
            
            dateLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(dateLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x:70, y: 18, width: ScreenSize.SCREEN_WIDTH-75, height: 50)
            descriptionLabel.text = notifDict.value(forKey: "description") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.numberOfLines = 3
            descriptionLabel.lineBreakMode = NSLineBreakMode.byWordWrapping
            if DeviceType.IS_IPAD{
                descriptionLabel.font = UIFont(name: fontName, size: 15)
            }else{
                if status == "SENT"{
                    descriptionLabel.font = UIFont(name: "Helvetica-Bold", size: 10.0)
                }else{
                    descriptionLabel.font = UIFont(name: fontName, size: 10)
                }
            }
            descriptionLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(descriptionLabel)
        }
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if (searchtext.text?.characters.count)!>2 {
            notDict = self.filterdNotification[indexPath.row] as! NSDictionary
            self.notificationdata.updateUserwithValues(responsedata: notDict)
            saveNotificationdata(user: self.notificationdata)
            let idVal = notDict.value(forKey: "id") as! NSInteger
            
            let notif = NotificationDetailsVC()
            notif.idValue = idVal
            self.navigationController?.pushViewController(notif, animated: false)
            let backItem = UIBarButtonItem()
            
            backItem.title = "Notification-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }
        else{
            notDict = self.arrayNotifications[indexPath.row] as! NSDictionary
            self.notificationdata.updateUserwithValues(responsedata: notDict)
            saveNotificationdata(user: self.notificationdata)
            let idVal = notDict.value(forKey: "id") as! NSInteger
            
            let notif = NotificationDetailsVC()
            notif.idValue = idVal
            self.navigationController?.pushViewController(notif, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Notification-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }
    }
    
    
    
    
    func pagebasedData()
    {
        let utliti = Utilities()
        
        let parameters:[String: Any] = ["page" : nextAPICallIndex, "size" : "20", "sort" : "id,asc"]
        print(parameters)
        showprogress(view: self.view)
        utliti.getdatafromserverwithstring(input: kGetallNotifications,parameters: parameters) { (true,result) in
            if (true){
                
                let newData = result as! NSArray
                let array_OldCount : Int =  newData.count
                
                
                for i in 0 ..< newData.count {
                    
                    if(!(self.arrayNotifications.contains(newData.object(at: i))))
                    {
                        self.arrayNotifications.add(newData.object(at: i))
                    }
                }
                if (array_OldCount != self.arrayNotifications.count){
                    
                     let indexPath = IndexPath(row: array_OldCount, section: 0)
                    
                    print(indexPath)
                }

                hideprogress()
            }
            
        }
        nextAPICallIndex += 1
        self.tableView.reloadData()
    }

    //TODO: Button Action
    
    func backClicked() {
        self.dismiss(animated: true, completion: nil)
    }
    
    
//    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
//        
//        // UITableView only moves in one direction, y axis
//        let currentOffset = scrollView.contentOffset.y
//        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
//        
//        // Change 10.0 to adjust the distance from bottom
//        if maximumOffset - currentOffset <= 10.0 {
//            self.pagebasedData()
//
//        }
//    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(true)
        START_PAGINATION_AT_INDEX = 0
    }
   

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return textField.resignFirstResponder()
    }
}
