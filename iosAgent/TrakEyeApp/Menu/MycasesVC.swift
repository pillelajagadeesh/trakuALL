//
//  MycasesVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 18/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class MycasesVC: BaseviewController,UITableViewDelegate,UITableViewDataSource {

    var tableView = UITableView()
    var arrayCases = NSMutableArray()
    var caseinfo = MycaseInfo()
    var caseDict = NSDictionary()
    var filterdMycases = NSArray()
    var nextAPICallIndex : Int = 1
    var searchIndex : Int = 0
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        geoFenceBtn.isHidden = true
        
        self.navigationController?.extendedLayoutIncludesOpaqueBars = true

        searchtext.placeholder = "Search by Description or Id"
        
        let headerview = UIView()
        headerview.frame = CGRect(x: 0, y: searechbackgrndview.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: 30)
        headerview.backgroundColor=UIColor.cyan
        headerview.alpha = 0.8
        self.view.addSubview(headerview)
        let wid = headerview.width/4
        
        let idLabel = UILabel()
        idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
        idLabel.text = "ID"
        idLabel.textColor = UIColor.black
        idLabel.font = UIFont(name: fontBold, size: 13)
        idLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(idLabel)
        
        let descriptionLabel = UILabel()
        descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
        descriptionLabel.text = "Casetype"
        descriptionLabel.textColor = UIColor.black
        descriptionLabel.font = UIFont(name: fontBold, size: 13)
        descriptionLabel.textAlignment = NSTextAlignment.left
        headerview.addSubview(descriptionLabel)
        
        let dateLabel = UILabel()
        dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
        dateLabel.text = "Date"
        dateLabel.textColor = UIColor.black
        dateLabel.font = UIFont(name: fontBold, size: 13)
        if DeviceType.IS_IPAD {
            dateLabel.textAlignment = NSTextAlignment.left
        }else
        {
            dateLabel.textAlignment = NSTextAlignment.center
        }
        headerview.addSubview(dateLabel)
        
        let escalatedLabel = UILabel()
        escalatedLabel.frame = CGRect(x:3*wid, y: 0, width: wid, height: 30)
        escalatedLabel.text = "Escalated"
        escalatedLabel.textColor = UIColor.black
        escalatedLabel.font = UIFont(name: fontBold, size: 13)
        escalatedLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(escalatedLabel)
        
        
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
        self.getAllMYcaseswithparameter(parameters: parameters)
        
       // let image1 = UIImage(named: "back_img")
       // let leftButton = UIBarButtonItem(image: image1, style: UIBarButtonItemStyle.plain, target: self, action: #selector(backClicked))
       // navigationItem.leftBarButtonItem = leftButton
    }
    func backClicked(){
        self.navigationController?.pop(animated: true)
    }
    
    
    override func textFieldTyping(textField:UITextField)
    {
        if (textField.text?.characters.count)!>2 {
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            print(parameters)
            
            self.getFilteredMycase(parameters: parameters)

            curruntpagelbl.text = "1"
        }else
        {
            nextBtn.isUserInteractionEnabled = true
            let count = "\(START_PAGINATION_AT_INDEX)"
            curruntpagelbl.text = "\(Int(count)! + 1)"
            
            let parameters:[String: Any] = ["page" : "\(count)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            showprogress(view: self.view)
            
            self.getAllMYcaseswithparameter(parameters: parameters)
        }
    }
   
     //TODO: Baseviewcontroller button Actions
    
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
            
            self.getFilteredMycase(parameters: parameters)
            
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
            
            self.getAllMYcaseswithparameter(parameters: parameters)
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
                
                self.getFilteredMycase(parameters: parameters)
            }
            
            
        }else{
            if self.arrayCases.count>0 {
                let count = START_PAGINATION_AT_INDEX + 1
                
                if count == TOTAL_PAGES {
                    nextBtn.isUserInteractionEnabled = false
                }else{
                    
                    START_PAGINATION_AT_INDEX += 1
                    curruntpagelbl.text = "\(START_PAGINATION_AT_INDEX + 1)"
                    
                    let parameters:[String: Any] = ["page" : "\(START_PAGINATION_AT_INDEX)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                    
                    self.getAllMYcaseswithparameter(parameters: parameters)
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
            
            self.getFilteredMycase(parameters: parameters)
            
        }else{
            
            if self.arrayCases.count>0 {
                START_PAGINATION_AT_INDEX = TOTAL_PAGES - 1
                
                curruntpagelbl.text = "\(TOTAL_PAGES)"
                
                let parameters:[String: Any] = ["page" : "\(TOTAL_PAGES - 1)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                self.getAllMYcaseswithparameter(parameters: parameters)
                
               
                
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
            
            self.getFilteredMycase(parameters: parameters)
            
        }else{
            nextBtn.isUserInteractionEnabled = true
            START_PAGINATION_AT_INDEX = 0
            
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getAllMYcaseswithparameter(parameters: parameters)
        }
    }
    
    //TODO: GET DATA FROM API
    
    func getAllMYcaseswithparameter(parameters : [String : Any])
    {
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: KGetmycases,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.arrayCases.removeAllObjects()
                self.arrayCases.addObjects(from: response as! [Any])
                self.tableView.reloadData()
            }
            else
            {
                print(result)
            }
        }
    }
    
    
    func getFilteredMycase(parameters : [String : Any]){
        
        let escapedString = searchtext.text!.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
        print(escapedString!)
        
        let urlinput = ksearchMycase + escapedString!
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: urlinput,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.filterdMycases  = response
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
            return filterdMycases.count
        }
        return self.arrayCases.count
    }
//    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
//        return 30.0
//    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 30.0
    }

    
//    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
//        
//        let headerview = UIView()
//        headerview.frame = CGRect(x: 0, y: 0, width: ScreenSize.SCREEN_WIDTH, height: 30)
//        headerview.backgroundColor=UIColor.cyan
//        headerview.alpha = 0.8
//        self.view.addSubview(headerview)
//        let wid = headerview.width/4
//        
//        let idLabel = UILabel()
//        idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
//        idLabel.text = "ID"
//        idLabel.textColor = UIColor.black
//        idLabel.font = UIFont(name: fontBold, size: 13)
//        idLabel.textAlignment = NSTextAlignment.center
//        headerview.addSubview(idLabel)
//        
//        let descriptionLabel = UILabel()
//        descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
//        descriptionLabel.text = "Casetype"
//        descriptionLabel.textColor = UIColor.black
//        descriptionLabel.font = UIFont(name: fontBold, size: 13)
//        descriptionLabel.textAlignment = NSTextAlignment.left
//        headerview.addSubview(descriptionLabel)
//        
//        let dateLabel = UILabel()
//        dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
//        dateLabel.text = "Date"
//        dateLabel.textColor = UIColor.black
//        dateLabel.font = UIFont(name: fontBold, size: 13)
//        if DeviceType.IS_IPAD {
//            dateLabel.textAlignment = NSTextAlignment.left
//        }else
//        {
//            dateLabel.textAlignment = NSTextAlignment.center
//        }
//        headerview.addSubview(dateLabel)
//        
//        let escalatedLabel = UILabel()
//        escalatedLabel.frame = CGRect(x:3*wid, y: 0, width: wid, height: 30)
//        escalatedLabel.text = "Escalated"
//        escalatedLabel.textColor = UIColor.black
//        escalatedLabel.font = UIFont(name: fontBold, size: 13)
//        escalatedLabel.textAlignment = NSTextAlignment.center
//        headerview.addSubview(escalatedLabel)
//        
//        return headerview
//    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
        
        cell.layoutMargins = UIEdgeInsets.zero
        cell.separatorInset = UIEdgeInsets.zero
        cell.preservesSuperviewLayoutMargins = false
        let wid = ScreenSize.SCREEN_WIDTH/4
        
        if (searchtext.text?.characters.count)!>2 {
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let searchDict = self.filterdMycases[indexPath.row] as! NSDictionary
            idLabel.text = "\(searchDict.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                idLabel.font = UIFont(name: fontName, size: 16)
            }else{
                idLabel.font = UIFont(name: fontName, size: 13)
            }
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = searchDict.value(forKeyPath: "caseType.name") as? String
            descriptionLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                descriptionLabel.font = UIFont(name: fontName, size: 16)
            }else{
                descriptionLabel.font = UIFont(name: fontName, size: 13)
            }
            descriptionLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(descriptionLabel)
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x: 2*wid, y: 0, width: wid, height: 30)
            let createdate = searchDict.value(forKey: "createDate") as! Double
            dateLabel.text = createdate.toDay//commonFunctions().millsecondsToDate(milli: searchDict.value(forKey: "createDate") as! NSInteger)
            dateLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
               dateLabel.font = UIFont(name: fontName, size: 16)
            }else{
                dateLabel.font = UIFont(name: fontName, size: 13)
            }
            dateLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(dateLabel)
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            
            if let xz = searchDict.value(forKey: "escalated") as? NSNumber{
                let x = Bool(xz)
                if x == true{
                    escalatedLabel.text = "YES"
                }else
                {
                    escalatedLabel.text = "NO"
                }
            }else
            {
                escalatedLabel.text = "NO"
            }
            escalatedLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                escalatedLabel.font = UIFont(name: fontName, size: 16)
            }else{
                escalatedLabel.font = UIFont(name: fontName, size: 13)
            }
            escalatedLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(escalatedLabel)
        }else
        {
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let caseDict = self.arrayCases[indexPath.row] as! NSDictionary
            idLabel.text = "\(caseDict.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                idLabel.font = UIFont(name: fontName, size: 16)
            }else{
                idLabel.font = UIFont(name: fontName, size: 13)
            }
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = caseDict.value(forKeyPath: "caseType.name") as? String
            descriptionLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                descriptionLabel.font = UIFont(name: fontName, size: 16)
            }else{
                descriptionLabel.font = UIFont(name: fontName, size: 13)
            }
            descriptionLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(descriptionLabel)
            

            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x: 2*wid, y: 0, width: wid, height: 30)
            let createDate = caseDict.value(forKey: "createDate") as! Double
            dateLabel.text = createDate.toDay//commonFunctions().millsecondsToDate(milli: caseDict.value(forKey: "createDate") as! NSInteger)
            dateLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                dateLabel.font = UIFont(name: fontName, size: 16)
            }else{
                dateLabel.font = UIFont(name: fontName, size: 13)
            }
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            if let xz = caseDict.value(forKey: "escalated") as? NSNumber{
                let x = Bool(xz)
                if x == true{
                    escalatedLabel.text = "YES"
                }else
                {
                    escalatedLabel.text = "NO"
                }
            }else
            {
                escalatedLabel.text = "NO"
            }
            escalatedLabel.textColor = UIColor.black
            if DeviceType.IS_IPAD{
                escalatedLabel.font = UIFont(name: fontName, size: 16)
            }else{
                escalatedLabel.font = UIFont(name: fontName, size: 13)
            }
            escalatedLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(escalatedLabel)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        
        if (searchtext.text?.characters.count)!>2 {
            
            caseDict = self.filterdMycases[indexPath.row] as! NSDictionary
            
            let idVal = caseDict.value(forKey: "id") as! NSInteger
            
            
            let notif = MycaseDetailsVC()
            notif.idValue = idVal
            self.navigationController?.pushViewController(notif, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "My Case-\(idVal)"
            navigationItem.backBarButtonItem = backItem
            
        }else{
            caseDict = self.arrayCases[indexPath.row] as! NSDictionary
            
             let idVal = caseDict.value(forKey: "id") as! NSInteger
            
            let notif = MycaseDetailsVC()
            notif.idValue = idVal
            self.navigationController?.pushViewController(notif, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "My Case-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }
        
        
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
    
    //TODO: Button Action
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    func pagebasedData()
    {
        let utliti = Utilities()
        
        
        let parameters:[String: Any] = ["page" : nextAPICallIndex, "size" : "20", "sort" : "id,asc"]
        
        showprogress(view: self.view)
        utliti.getdatafromserverwithstring(input: KGetmycases,parameters: parameters) { (true,result) in
            if (true){
                
                let newData = result as! NSArray
                let array_OldCount : Int =  self.arrayCases.count
                
                
                for i in 0 ..< newData.count {
                    
                    if(!self.arrayCases.contains(newData.object(at: i)))
                    {
                        self.arrayCases.adding(newData.object(at: i))
                    }
                }
                if (array_OldCount != self.arrayCases.count){
                  
                   let indexPath = IndexPath(row: array_OldCount, section: 0)
                    print(indexPath)
                }
                hideprogress()
            }
            
        }
        nextAPICallIndex += 1
        self.tableView.reloadData()
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return textField.resignFirstResponder()
    }
    
    
}


