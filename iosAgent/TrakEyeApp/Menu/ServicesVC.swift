//
//  ServicesVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 23/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire

class ServicesVC: BaseviewController,UITableViewDelegate,UITableViewDataSource {

    var tableView = UITableView()
    var arrayServices = NSMutableArray()
    var caseinfo = MycaseInfo()
    var caseDict = NSDictionary()
    var filterdServices = NSArray()
    var nextAPICallIndex : Int = 1
    var searchIndex : Int = 0

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        
        geoFenceBtn.isHidden = true
        searchtext.placeholder = "Search by Description or Id"
        
        APP_DELEGATE.window?.bringSubview(toFront: noResultlbl)
        
        let headerview = UIView()
        headerview.frame = CGRect(x: 0, y: searechbackgrndview.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: 30)
        headerview.backgroundColor=UIColor.cyan
        headerview.alpha = 0.8
        self.view.addSubview(headerview)
        
        let wid = headerview.width/3
        
        let idLabel = UILabel()
        idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
        idLabel.text = "ID"
        idLabel.textColor = UIColor.black
        idLabel.font = UIFont(name: fontBold, size: 13)
        idLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(idLabel)
        
        let descriptionLabel = UILabel()
        descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
        descriptionLabel.text = "Description"
        descriptionLabel.textColor = UIColor.black
        descriptionLabel.font = UIFont(name: fontBold, size: 13)
        descriptionLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(descriptionLabel)
        
        let dateLabel = UILabel()
        dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
        dateLabel.text = "Date"
        dateLabel.textColor = UIColor.black
        dateLabel.font = UIFont(name: fontBold, size: 13)
        dateLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(dateLabel)
        
        tableView = UITableView(frame:CGRect(x: 0, y: headerview.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-194) , style: UITableViewStyle.plain)
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
        self.getAllServiceswithparameter(parameters: parameters)
        
    }

    
    
    
    
    override func textFieldTyping(textField:UITextField)
    {
        if (textField.text?.characters.count)!>3 {
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            print(parameters)
            self.getFilteredServices(parameters: parameters)
            curruntpagelbl.text = "1"
        }else
        {
            nextBtn.isUserInteractionEnabled = true
            let count = "\(START_PAGINATION_AT_INDEX)"
            curruntpagelbl.text = "\(Int(count)! + 1)"
            
            let parameters:[String: Any] = ["page" : "\(count)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            showprogress(view: self.view)
            
            self.getAllServiceswithparameter(parameters: parameters)
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
            
            self.getFilteredServices(parameters: parameters)
            
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
            
            self.getAllServiceswithparameter(parameters: parameters)
        }
    }
    
    override func nextBtnClicked(){
        
        if (searchtext.text?.characters.count)!>2{
            let count = searchIndex + 1
            
            if count == TOTAL_PAGES {
                nextBtn.isUserInteractionEnabled = false
            }else{
                searchIndex += 1
                curruntpagelbl.text = "\(searchIndex + 1)"
                
                let parameters:[String: Any] = ["page" : "\(searchIndex)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                self.getFilteredServices(parameters: parameters)
            }
            
        }else{
            if self.arrayServices.count>0{
                let count = START_PAGINATION_AT_INDEX + 1
                
                if count == TOTAL_PAGES {
                    nextBtn.isUserInteractionEnabled = false
                }else{
                    
                    START_PAGINATION_AT_INDEX += 1
                    curruntpagelbl.text = "\(START_PAGINATION_AT_INDEX + 1)"
                    
                    let parameters:[String: Any] = ["page" : "\(START_PAGINATION_AT_INDEX)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                    
                    self.getAllServiceswithparameter(parameters: parameters)
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
            
            self.getFilteredServices(parameters: parameters)
            
        }else{
            
            if self.arrayServices.count>0 {
                START_PAGINATION_AT_INDEX = TOTAL_PAGES - 1
                
                curruntpagelbl.text = "\(TOTAL_PAGES)"
                
                let parameters:[String: Any] = ["page" : "\(TOTAL_PAGES - 1)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                self.getAllServiceswithparameter(parameters: parameters)
                
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
            
            self.getFilteredServices(parameters: parameters)
            
        }else{
             nextBtn.isUserInteractionEnabled = true
            START_PAGINATION_AT_INDEX = 0
            
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getAllServiceswithparameter(parameters: parameters)
        }
    }
    
    //TODO: GET DATA FROM API
    
    func getAllServiceswithparameter(parameters : [String : Any])
    {
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: kGetServices,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                if response.count>0{
                    self.tableView.isHidden = false
                    self.arrayServices.removeAllObjects()
                    self.arrayServices.addObjects(from: response as! [Any])
                    self.tableView.reloadData()
                }else
                {
                    self.tableView.isHidden = true
                    self.noResultlbl.isHidden = false
                }
                
            }
            else
            {
                self.tableView.isHidden = true
                self.noResultlbl.isHidden = false
                print(result)
            }
        }
    }
    
    
    func getFilteredServices(parameters : [String : Any]){
        
        let escapedString = searchtext.text!.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
        print(escapedString!)
        
        let urlinput = ksearchService + escapedString!
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: urlinput,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.filterdServices  = response
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
        
        if (searchtext.text?.characters.count)!>3 {
            return self.filterdServices.count
        }

        return self.arrayServices.count
    }

    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 30.0		
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
        
        cell.layoutMargins = UIEdgeInsets.zero
        cell.separatorInset = UIEdgeInsets.zero
        cell.preservesSuperviewLayoutMargins = false
        let wid = ScreenSize.SCREEN_WIDTH/3
        
        if (searchtext.text?.characters.count)!>3 {
            
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let searchDict = self.filterdServices[indexPath.row] as! NSDictionary
            
            
            idLabel.text = "\(searchDict.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            idLabel.font = UIFont(name: fontName, size: 13)
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = searchDict.value(forKey: "description") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(descriptionLabel)
            
            let milisecond = searchDict.value(forKey: "createdDate") as? Double
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = milisecond?.toDay
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
        }else
        {
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let serviceDict = self.arrayServices[indexPath.row] as! NSDictionary
            idLabel.text = "\(serviceDict.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            idLabel.font = UIFont(name: fontName, size: 13)
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = serviceDict.value(forKey: "description") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(descriptionLabel)
            
            let milisecond = serviceDict.value(forKey: "createdDate") as? Double
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = milisecond?.toDay
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
        }
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        //caseDict = self.arrayCases[indexPath.row] as! NSDictionary
        
        //self.caseinfo.updateUserwithValues(responsedata: caseDict)
        
        //saveMycasedata(user: self.caseinfo)
        
        
        //let notif = MycaseDetailsVC()
        //self.present(notif, animated: true, completion: nil)
        
        if (searchtext.text?.characters.count)!>3 {
            
            let k = self.filterdServices[indexPath.row] as! NSDictionary
            let idVal = k.value(forKey: "id") as! NSInteger
            let serviceDet = ServiceDetailsVC()
            serviceDet.idValue = idVal
            self.navigationController?.pushViewController(serviceDet, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Service-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }else
        {
            let k = self.arrayServices[indexPath.row] as! NSDictionary
            let idVal = k.value(forKey: "id") as! NSInteger
            let serviceDet = ServiceDetailsVC()
            serviceDet.idValue = idVal
            self.navigationController?.pushViewController(serviceDet, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Service-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func pagebasedData()
    {
        let utliti = Utilities()
        
        let parameters:[String: Any] = ["page" : nextAPICallIndex, "size" : "20", "sort" : "id,asc"]
        
        showprogress(view: self.view)
        utliti.getdatafromserverwithstring(input: KGetmycases,parameters: parameters) { (true,result) in
            if (true){
                let newData = result as! NSArray
                let array_OldCount : Int =  self.arrayServices.count
                
                for i in 0 ..< newData.count {
                    
                    if(!self.arrayServices.contains(newData.object(at: i)))
                    {
                        self.arrayServices.adding(newData.object(at: i))
                    }
                }
                if (array_OldCount != self.arrayServices.count){
                    let indexPath = IndexPath(row: array_OldCount, section: 0)
                    print(indexPath)
                }
                hideprogress()
            }
        }
        nextAPICallIndex += 1
        self.tableView.reloadData()
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
    

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return textField.resignFirstResponder()
    }
}
