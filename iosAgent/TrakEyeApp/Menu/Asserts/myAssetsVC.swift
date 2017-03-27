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
import CoreTelephony
import TPKeyboardAvoiding
import GoogleMaps
import GooglePlaces

class myAssetsVC: BaseviewController ,UITableViewDelegate,UITableViewDataSource, GMSMapViewDelegate, CLLocationManagerDelegate {
    
    var tableView = UITableView()
    var arrayServices = NSMutableArray()
    var caseinfo = MycaseInfo()
    var caseDict = NSDictionary()
    var filterdServices = NSArray()
    var nextAPICallIndex : Int = 1
    var rightButton = UIBarButtonItem()
    var searchIndex : Int = 0
    var map = UIView()
    var assetview = UIView()
    var mapView = GMSMapView()
    var responseArray = NSMutableArray()
    var resultArray = [NSDictionary]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        geoFenceBtn.isHidden = true
        
        searchtext.placeholder = "Search by Name or Id"
        
        self.createAssetsView()
        
        let rightButton0 = UIButton()
        rightButton0.frame = CGRect(x: 20, y: 20, width: 30, height: 30)
        let initImage0 = UIImage(named: "list_button")
        let rightButton0image = UIImage(named: "list_button_uncheck")
        rightButton0.setImage(initImage0, for: UIControlState.normal)
        rightButton0.setImage(rightButton0image, for: UIControlState.selected)
        rightButton0.layer.cornerRadius = 5.0
        rightButton0.addTarget(self, action: #selector(self.rightMenuClicked), for: UIControlEvents.touchDown)
        
        rightButton = UIBarButtonItem(customView: rightButton0)
        
        let rightButton2 = UIButton()
        rightButton2.frame = CGRect(x: 20, y: 20, width: 30, height: 30)
        let initImage = UIImage(named: "map_button")
        let rightButton2image = UIImage(named: "map_button-uncheck")
        rightButton2.setImage(initImage, for: UIControlState.normal)
        rightButton2.setImage(rightButton2image, for: UIControlState.selected)
        rightButton2.layer.cornerRadius = 5.0
        rightButton2.addTarget(self, action: #selector(self.rightButton2clicked), for: UIControlEvents.touchDown)
        
        let rightButton1 = UIBarButtonItem(customView: rightButton2)
        self.navigationItem.rightBarButtonItems = [rightButton1,rightButton]
        
        rightButton.isEnabled = false
        

    }

    func createAssetsView()
    {
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
        descriptionLabel.text = "Name"
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
        escalatedLabel.text = "Layout"
        escalatedLabel.textColor = UIColor.black
        escalatedLabel.font = UIFont(name: fontBold, size: 13)
        escalatedLabel.textAlignment = NSTextAlignment.center
        headerview.addSubview(escalatedLabel)
        
        
        assetview.frame = CGRect(x: 0, y: headerview.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-194)
        self.view.addSubview(assetview)
        
        
        tableView = UITableView(frame:CGRect(x: 0, y: 0, width: assetview.width, height: assetview.height) , style: UITableViewStyle.plain)
        tableView.delegate      =   self
        tableView.dataSource    =   self
        tableView.tableFooterView = UIView()
        tableView.layoutMargins = UIEdgeInsets.zero
        tableView.separatorInset = UIEdgeInsets.zero
        tableView.showsVerticalScrollIndicator = false
        tableView.cellLayoutMarginsFollowReadableWidth = false
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        assetview.addSubview(self.tableView)
        
        
        //self.getservices()
        
        
        let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
        showprogress(view: self.view)
        
        self.getAllAssetswithparameter(parameters: parameters)
        
    }
    
    
    
    
    func rightMenuClicked(){
        map.removeFromSuperview()
        searechbackgrndview.isHidden = false
        self.createAssetsView()
    }
    func rightButton2clicked(){
        
//        assetview.removeFromSuperview()
//        
//        searechbackgrndview.isHidden = true
//        
//        map.frame = CGRect(x: 0, y: 64, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-64)
//        self.view.addSubview(map)
//        
//        self.mapView = GMSMapView()
//        let camera = GMSCameraPosition.camera(withLatitude: 20.5937, longitude: 78.9629, zoom: 10.0)
//        self.mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
//        self.mapView.delegate = self
//        self.mapView.frame = CGRect(x: 0, y: 0, width: map.width, height: map.height)
//        //self.view.addSubview(self.mapView)
//        map.addSubview(self.mapView)
//        
//        if(self.responseArray.count > 0){
//            //let deadlineTime = DispatchTime.now() + .seconds(1)
//            self.getData()
//        }
        
        
        let vc = allAssetsMapViewVC()
        if (searchtext.text?.characters.count)!>2{
            if(self.filterdServices.count > 0){
                vc.responseArray = self.filterdServices as! NSMutableArray
            }
            else{
                return
            }
        }
        else{
            vc.responseArray = self.arrayServices
        }
        
        let backItem = UIBarButtonItem()
        backItem.title = "All-Assets"
        navigationItem.backBarButtonItem = backItem
        self.navigationController?.pushViewController(vc, animated: false)
    }
    
    //TODO: Create assetmapview
    
    func getData(){
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.03) {
            let _ = self.responseArray.map { (self.createMapView(responsedata: $0 as! NSDictionary)) }
        }
    }
    func createMapView(responsedata:NSDictionary){
        let assetType = responsedata.value(forKey: "assetType") as! NSDictionary
        let layout = assetType.value(forKey: "layout") as! String
        let name = assetType.value(forKey: "name") as! String
        let desc = assetType.value(forKey: "description") as? String
        var plotValues = [NSDictionary]()
        if let assetCoordinates = responsedata.value(forKey: "assetCoordinates") as? NSArray{
            for value in assetCoordinates{
                if let dictValue = value as? NSDictionary{
                    let dictLatLongValue:NSDictionary = ["latitude":dictValue.value(forKey: "latitude") as! Double, "longitude":dictValue.value(forKey: "longitude") as! Double]
                    plotValues.append(dictLatLongValue)
                }
            }
        }
        var bounds = GMSCoordinateBounds()
        if(layout.lowercased() == "fixed"){
            let img = assetType.value(forKey: "image") as! String
            let imageData = NSData(base64Encoded: img, options: [])
            do{
                let myImage =  UIImage(data: imageData as! Data)
                for i in plotValues{
                    let mark = GMSMarker(position: CLLocationCoordinate2D(latitude: i.value(forKey: "latitude") as! CLLocationDegrees, longitude: i.value(forKey: "longitude") as! CLLocationDegrees))
                    mark.map = mapView
                    mark.icon = resizeImage(image: myImage!, newWidth: 20)
                    mark.title = name
                    mark.snippet = desc
                    bounds = bounds.includingCoordinate(mark.position)
                }
            }
            
        }
        else{
            let color = strToHexa(colorString: assetType.value(forKey: "colorcode") as! String)//
            let path = GMSMutablePath()
            for i in plotValues{
                path.add(CLLocationCoordinate2D(latitude: i.value(forKey: "latitude") as! CLLocationDegrees, longitude: i.value(forKey: "longitude") as! CLLocationDegrees))
                let mark = GMSMarker(position: CLLocationCoordinate2D(latitude: i.value(forKey: "latitude") as! CLLocationDegrees, longitude: i.value(forKey: "longitude") as! CLLocationDegrees))
                bounds = bounds.includingCoordinate(mark.position)
            }
            let rectangle = GMSPolyline(path: path)
            rectangle.strokeWidth = 4
            rectangle.map = mapView
            rectangle.strokeColor = UIColor(rgb:UInt(color))
            rectangle.title = name
            rectangle.isTappable = true
        }
        self.mapView.reloadInputViews()
        self.mapView.animate(with: GMSCameraUpdate.fit(bounds, with: UIEdgeInsetsMake(50, 0, 50, 0)))
        self.mapView.setMinZoom(3, maxZoom: 25)
        //self.mapView.animate(with: GMSCameraUpdate.fit(bounds))
    }
    
    func callFinal(){
        hideprogress()
        print("assests Completed Dude")
    }
    
    // end asset mapview
    
    override func textFieldTyping(textField:UITextField)
    {
        if (textField.text?.characters.count)!>2 {
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            self.getFilteredAsset(parameters: parameters)
            curruntpagelbl.text = "1"
        }else
        {
            nextBtn.isUserInteractionEnabled = true
            let count = "\(START_PAGINATION_AT_INDEX)"
            curruntpagelbl.text = "\(Int(count)! + 1)"
            
            let parameters:[String: Any] = ["page" : "\(count)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            showprogress(view: self.view)
            
            self.getAllAssetswithparameter(parameters: parameters)
            
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
            
            self.getFilteredAsset(parameters: parameters)
            
        }else
        {
            var parameters = [String: Any]()
            nextBtn.isUserInteractionEnabled = true
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
            
            self.getAllAssetswithparameter(parameters: parameters)
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
                
                self.getFilteredAsset(parameters: parameters)
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
                    
                    self.getAllAssetswithparameter(parameters: parameters)
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
            
            self.getFilteredAsset(parameters: parameters)
            
        }else{
            
            if self.arrayServices.count>0 {
                START_PAGINATION_AT_INDEX = TOTAL_PAGES - 1
                
                curruntpagelbl.text = "\(TOTAL_PAGES)"
                
                let parameters:[String: Any] = ["page" : "\(TOTAL_PAGES - 1)", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
                
                self.getAllAssetswithparameter(parameters: parameters)
                
            }else{
                curruntpagelbl.text = "1"
            }
        }
        
    }
    override func initialpageBtnclicked(){
        if (searchtext.text?.characters.count)!>2{
            searchIndex = 0
            nextBtn.isUserInteractionEnabled = true
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getFilteredAsset(parameters: parameters)
            
        }else{
            START_PAGINATION_AT_INDEX = 0
            nextBtn.isUserInteractionEnabled = true
            curruntpagelbl.text = "1"
            
            let parameters:[String: Any] = ["page" : "0", "size" : "\(PAGE_COUNT)", "sort" : "id,desc"]
            
            self.getAllAssetswithparameter(parameters: parameters)
        }
    }
    
    func getAllAssetswithparameter(parameters : [String : Any])
    {
        let utliti = Utilities()
        utliti.getdatafromserverwithstring(input: kGetAssets,parameters: parameters) { (true,result) in
            if let response = result as? NSArray{
                print(response)
                self.arrayServices.removeAllObjects()
                self.arrayServices.addObjects(from: response as! [Any])
                self.tableView.reloadData()
            }
            else
            {
                print(result)
            }
        }
    }
    
    
    func getFilteredAsset(parameters : [String : Any]){
        
        let escapedString = searchtext.text!.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
        print(escapedString!)
        
        let urlinput = kAssetSearch + escapedString!
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
        
        if (searchtext.text?.characters.count)!>2 {
            return self.filterdServices.count
        }
        
        return self.arrayServices.count
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
//        
//        let wid = headerview.width/4
//        
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
//        descriptionLabel.text = "Name"
//        descriptionLabel.textColor = UIColor.black
//        descriptionLabel.font = UIFont(name: fontBold, size: 13)
//        descriptionLabel.textAlignment = NSTextAlignment.center
//        headerview.addSubview(descriptionLabel)
//        
//        let dateLabel = UILabel()
//        dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
//        dateLabel.text = "Date"
//        dateLabel.textColor = UIColor.black
//        dateLabel.font = UIFont(name: fontBold, size: 13)
//        dateLabel.textAlignment = NSTextAlignment.center
//        headerview.addSubview(dateLabel)
//        
//        
//        let escalatedLabel = UILabel()
//        escalatedLabel.frame = CGRect(x:3*wid, y: 0, width: wid, height: 30)
//        escalatedLabel.text = "Layout"
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
            let k = self.filterdServices[indexPath.row] as! NSDictionary
            
            
            idLabel.text = "\(k.value(forKey: "id") as! NSInteger)"
            idLabel.textColor = UIColor.black
            idLabel.font = UIFont(name: fontName, size: 13)
            idLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(idLabel)
            
            let descriptionLabel = UILabel()
            descriptionLabel.frame = CGRect(x: wid, y: 0, width: wid, height: 30)
            descriptionLabel.text = k.value(forKey: "name") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(descriptionLabel)
            
            let milisecond = k.value(forKey: "createDate") as? Double
            
           
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = milisecond?.toDay
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            let layDict = k.value(forKey: "assetType") as! NSDictionary
            escalatedLabel.text = layDict.value(forKey: "layout") as? String
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
            descriptionLabel.text = k.value(forKey: "name") as? String
            descriptionLabel.textColor = UIColor.black
            descriptionLabel.font = UIFont(name: fontName, size: 13)
            descriptionLabel.textAlignment = NSTextAlignment.left
            cell.addSubview(descriptionLabel)
            
            let milisecond = k.value(forKey: "createDate") as? Double
            
            
            let dateLabel = UILabel()
            dateLabel.frame = CGRect(x:2*wid, y: 0, width: wid, height: 30)
            dateLabel.text = milisecond?.toDay
            dateLabel.textColor = UIColor.black
            dateLabel.font = UIFont(name: fontName, size: 13)
            dateLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(dateLabel)
            
            
            let escalatedLabel = UILabel()
            escalatedLabel.frame = CGRect(x: 3*wid, y: 0, width: wid, height: 30)
            let layDict = k.value(forKey: "assetType") as! NSDictionary
            escalatedLabel.text = layDict.value(forKey: "layout") as? String
            escalatedLabel.textColor = UIColor.black
            escalatedLabel.font = UIFont(name: fontName, size: 13)
            escalatedLabel.textAlignment = NSTextAlignment.center
            cell.addSubview(escalatedLabel)
        }
        rightButton.isEnabled = true
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        
        if (searchtext.text?.characters.count)!>2 {
            
            let k = self.filterdServices[indexPath.row] as! NSDictionary
            let idVal = k.value(forKey: "id") as! NSInteger
            let serviceDet = assetDetailsVC()
            serviceDet.idValue = idVal
            self.navigationController?.pushViewController(serviceDet, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Asset Type-\(idVal)"
            navigationItem.backBarButtonItem = backItem
        }else
        {
            let k = self.arrayServices[indexPath.row] as! NSDictionary
            let idVal = k.value(forKey: "id") as! NSInteger
            let serviceDet = assetDetailsVC()
            serviceDet.idValue = idVal
            self.navigationController?.pushViewController(serviceDet, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Asset Type-\(idVal)"
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
        
        utliti.getdatafromserverwithstring(input: kGetAssets,parameters: parameters) { (true,result) in
            if (true){
                
                let newData = result as! NSArray
                let array_OldCount : Int =  self.arrayServices.count
                
                for i in 0 ..< newData.count {
                    
                    if(!self.arrayServices.contains(newData.object(at: i)))
                    {
                        self.arrayServices.add(newData.object(at: i))
                    }
                }
                if (array_OldCount != self.arrayServices.count){
                    let indexPath = IndexPath(row: array_OldCount, section: 0)
                    print(indexPath)
                }
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
