//
//  myAssertsVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 12/12/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import Foundation
import UIKit
import Alamofire


class myAssertVC: UIViewController ,UITableViewDelegate,UITableViewDataSource, UISearchResultsUpdating, UISearchBarDelegate,UITextFieldDelegate {
    
    var tableView = UITableView()
    var arrayServices = NSMutableArray()
    var caseinfo = MycaseInfo()
    var caseDict = NSDictionary()
    var refreshControl: UIRefreshControl!
    var searchController = UISearchController()
    var filterdServices = NSArray()
    var searchActive : Bool = false
    var searchBar:UISearchBar = UISearchBar()
    var nextAPICallIndex : Int = 1
    var searchtext = UITextField()

    override func viewDidLoad() {
        super.viewDidLoad()
        let searechbackgrndview = UIView()
        searechbackgrndview.frame = CGRect(x: 0, y: 64, width: ScreenSize.SCREEN_WIDTH, height: 50)
        searechbackgrndview.backgroundColor = UIColor(rgb: 0x03a9f5)
        searechbackgrndview.alpha = 0.8
        self.view.addSubview(searechbackgrndview)
        
        let searechview = UIView()
        searechview.frame = CGRect(x: 10, y: 5, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        searechview.backgroundColor = UIColor.white
        searechbackgrndview.addSubview(searechview)
        
        
        searchtext.frame = CGRect(x: 10, y: 5, width: ScreenSize.SCREEN_WIDTH-20, height: 30)
        searchtext.delegate = self
        searchtext.placeholder = "Search by description"
        searchtext.addTarget(self, action: #selector(textFieldTyping), for: .editingChanged)
        searechview.addSubview(searchtext)
        
        
        tableView = UITableView(frame:CGRect(x: 0, y: 114, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-114) , style: UITableViewStyle.plain)
        tableView.delegate      =   self
        tableView.dataSource    =   self
        tableView.tableFooterView = UIView()
        tableView.layoutMargins = UIEdgeInsets.zero
        tableView.separatorInset = UIEdgeInsets.zero
        tableView.showsVerticalScrollIndicator = false
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        self.view.addSubview(self.tableView)
        
        
        
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl.addTarget(self, action:  #selector(refresh), for: UIControlEvents.valueChanged)
        tableView.refreshControl = refreshControl
        
        
        //self.getservices()
        
        let utliti = Utilities()
        
        let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
        showprogress(view: self.view)
        
        
        utliti.getdatafromserverwithstring(input: kGetAssets,parameters: parameters) { (data,result) in
            if let response = result as? NSArray{
                self.arrayServices.addObjects(from: response as! [Any])
                self.tableView.reloadData()
                hideprogress()
            }else
            {
                print(result)
                hideprogress()
            }
            
        }
        
    }
    
    
    func refresh() {
        let utliti = Utilities()
        let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
        showprogress(view: self.view)
        utliti.getdatafromserverwithstring(input: kGetServices,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                self.arrayServices.removeAllObjects()
                self.arrayServices.addObjects(from: response as! [Any])
                self.tableView.reloadData()
                hideprogress()
            }else
            {
                print(result)
                hideprogress()
            }
        }
        refreshControl.endRefreshing()
    }
    
    
    func textFieldTyping(textField:UITextField)
    {
        if (textField.text?.characters.count)!>3 {
            let utliti = Utilities()
            let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
            print(parameters)
            
            let escapedString = textField.text!.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
            print(escapedString!)
            
            let urlinput = ksearchService + escapedString!
            showprogress(view: self.view)
            utliti.getdatafromserverwithstring(input: urlinput,parameters: parameters) { (true,result) in
                if let response = result as? NSArray{
                    self.filterdServices  = response
                    self.tableView.reloadData()
                    hideprogress()
                }else
                {
                    print(result)
                    hideprogress()
                }
                
            }
            
        }else
        {
            self.tableView.reloadData()
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
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 30.0
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 30.0
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let headerview = UIView()
        headerview.frame = CGRect(x: 0, y: 0, width: ScreenSize.SCREEN_WIDTH, height: 30)
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
        
        
        let escalatedLabel = UILabel()
        escalatedLabel.frame = CGRect(x:3*wid, y: 0, width: wid, height: 30)
        escalatedLabel.text = "Escalated"
        escalatedLabel.textColor = UIColor.black
        escalatedLabel.font = UIFont(name: fontBold, size: 13)
        escalatedLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(escalatedLabel)
        
        return headerview
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
        
        cell.layoutMargins = UIEdgeInsets.zero
        
        let wid = ScreenSize.SCREEN_WIDTH/4
        
        if (searchtext.text?.characters.count)!>3 {
            
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let k = self.filterdServices[indexPath.row] as! NSDictionary
            
            
            idLabel.text = "\(k.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            idLabel.font = UIFont(name: fontName, size: 13)
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = k.value(forKey: "description") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(descriptionLabel)
            
            let milisecond = k.value(forKey: "createdDate") as? NSInteger
            
            let date = NSDate(timeIntervalSince1970: TimeInterval(milisecond!)/1000)
            let formatter = DateFormatter()
            formatter.dateFormat = "dd/MM/yyyy"
            //formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
            print(formatter.string(from: date as Date))
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = "\(formatter.string(from: date as Date))"
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            
            if let escDict = k.value(forKey: "trCase") as? NSDictionary{
                let esc = escDict.value(forKey: "escalated") as? Bool
                if !esc! {
                    escalatedLabel.text = "NO"
                }else{
                    escalatedLabel.text = "YES"
                }
            }
            else{
                escalatedLabel.text = "NO"
                
            }
            
            escalatedLabel.textColor = UIColor.black
            escalatedLabel.font = UIFont(name: fontName, size: 13)
            escalatedLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(escalatedLabel)
            
        }else
        {
            let idLabel = UILabel()
            idLabel.frame = CGRect(x: 0, y: 0, width: wid, height: 30)
            let k = self.arrayServices[indexPath.row] as! NSDictionary
            
            
            idLabel.text = "\(k.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            idLabel.font = UIFont(name: fontName, size: 13)
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = k.value(forKey: "description") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(descriptionLabel)
            
            let milisecond = k.value(forKey: "createdDate") as? NSInteger
            
            let date = NSDate(timeIntervalSince1970: TimeInterval(milisecond!)/1000)
            let formatter = DateFormatter()
            formatter.dateFormat = "dd/MM/yyyy"
            //formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
            print(formatter.string(from: date as Date))
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = "\(formatter.string(from: date as Date))"
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            
            if let escDict = k.value(forKey: "trCase") as? NSDictionary{
                let esc = escDict.value(forKey: "escalated") as? Bool
                if !esc! {
                    escalatedLabel.text = "NO"
                }else{
                    escalatedLabel.text = "YES"
                }
            }
            else{
                escalatedLabel.text = "NO"
                
            }
            
            escalatedLabel.textColor = UIColor.black
            escalatedLabel.font = UIFont(name: fontName, size: 13)
            escalatedLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(escalatedLabel)
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
    
    //TODO: Button Action
    
    func backClicked() {
        self.dismiss(animated: true, completion: nil)
    }
    
    //TODO: SearchResultUpdating Delegate
    
    func updateSearchResults(for searchController: UISearchController) {
        
        let searchText = searchController.searchBar.text
        if searchController.isActive == true {
            let searchPredicate = NSPredicate(format: "SELF.description contains[c] %@", searchText!)
            let array = self.arrayServices.filtered(using: searchPredicate)
            filterdServices = array as NSArray
        }
        
        
        
        self.tableView.reloadData()
    }
    
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchActive = true
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchActive = false
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchActive = false
        searchBar .resignFirstResponder()
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchActive = false
        searchBar .resignFirstResponder()
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        
        let searchPredicate = NSPredicate(format: "SELF.description contains[c] %@", searchText)
        let array = self.arrayServices.filtered(using: searchPredicate)
        filterdServices = array as NSArray
        
        if(filterdServices.count == 0){
            searchActive = false;
        } else {
            searchActive = true;
        }
        self.tableView.reloadData()
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
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        // UITableView only moves in one direction, y axis
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        
        // Change 10.0 to adjust the distance from bottom
        if maximumOffset - currentOffset <= 10.0 {
            self.pagebasedData()
            
        }
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return textField.resignFirstResponder()
    }
}
