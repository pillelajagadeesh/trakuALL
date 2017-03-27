//
//  CreateAssetsViewController.swift
//  TrakEyeApp
//
//  Created by Apple on 12/12/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import TPKeyboardAvoiding
import Alamofire

class CreateAssetsViewController: UIViewController, UITextFieldDelegate, UITableViewDelegate, UITableViewDataSource {

    var parentView = TPKeyboardAvoidingScrollView()
    var nameField = FloatLabelTextField()
    var descriptionField = FloatLabelTextField()
    var assetTypeBtn = UIButton()
    var tableView = UITableView()
    var assetTypeDropDown = UITableView()
    var selected :Bool = false
    var tokenStr  :String!
    var responseArray = NSArray()
    var footerView = UIView()
    var attributeView  = UIView()
    var assetTypeAttributes = NSDictionary()
    var latLongArray = NSMutableArray()
    var spreadLatLongArray = NSMutableArray()
    var updateTimer : Timer!
    var isStartClicked : Bool = false
    var globalStart = UIButton()
    var globalStop = UIButton()
    var loaderView = UIView()
    override func viewDidLoad() {
        super.viewDidLoad()

        let navigationBar = UINavigationBar(frame: CGRect(x: 0, y: 0, width: view.width, height: 64))
        navigationBar.backgroundColor = LOGIN_BG_COLOR
        let navigationItem = UINavigationItem()
        navigationBar.items = [navigationItem]
        
        let headingLabel = UILabel()
        headingLabel.frame = CGRect(x: view.width/2 - 42.5, y: 25, width: ScreenSize.SCREEN_WIDTH/2, height: 35)
        headingLabel.text = "Create Asset"
        headingLabel.textColor = UIColor.white
        headingLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 17)
        headingLabel.textAlignment = NSTextAlignment.left
        navigationBar.addSubview(headingLabel)
        navigationBar.tintColor = UIColor.white
        
        
        parentView = TPKeyboardAvoidingScrollView(frame: CGRect(x: 0, y: 0, width: view.width, height: view.height - 144))
        self.view.addSubview(parentView)
        self.view.addSubview(footerView)
        
        
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.parentView.subviews.forEach({ $0.removeFromSuperview() })
        self.footerView.subviews.forEach({ $0.removeFromSuperview() })
        self.designViews()
    }
    
    func designViews(){
        nameField = FloatLabelTextField()
        nameField.frame = CGRect(x: 10, y: 10, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        nameField.keyboardType = UIKeyboardType.alphabet
        nameField.delegate = self
        nameField.placeholder = "Name"
        nameField.textColor = UIColor.black
        nameField.font = UIFont(name: "Futura-Medium", size: 15)
        nameField.textAlignment = NSTextAlignment.left
        nameField.setSignInBottomBorder()
        parentView.addSubview(nameField)
        
        descriptionField = FloatLabelTextField()
        descriptionField.frame = CGRect(x: 10, y: nameField.maxYOrigin + 10, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        descriptionField.keyboardType = UIKeyboardType.alphabet
        descriptionField.delegate = self
        descriptionField.placeholder = "Description"
        descriptionField.textColor = UIColor.black
        descriptionField.font = UIFont(name: "Futura-Medium", size: 15)
        descriptionField.textAlignment = NSTextAlignment.left
        descriptionField.setSignInBottomBorder()
        parentView.addSubview(descriptionField)
        
        assetTypeBtn = UIButton()
        assetTypeBtn.frame = CGRect(x: 10, y: descriptionField.maxYOrigin + 20, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        assetTypeBtn.layer.borderWidth = 1.0
        assetTypeBtn.layer.cornerRadius = 5.0
        // assetTypeBtn.isSelected = false
        assetTypeBtn.setTitle("Asset Type", for: UIControlState.normal)
        assetTypeBtn.setTitleColor(UIColor.black, for: UIControlState.normal)
        assetTypeBtn.titleLabel?.textAlignment = NSTextAlignment.center
        assetTypeBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        assetTypeBtn.addTarget(self, action: #selector(CreateAssetsViewController.assetTypeClicked), for: UIControlEvents.touchDown)
        parentView.addSubview(assetTypeBtn)
        
        assetTypeDropDown = UITableView()
        assetTypeDropDown = UITableView(frame: CGRect(x:10, y:assetTypeBtn.maxYOrigin, width:ScreenSize.SCREEN_WIDTH-20, height:130), style: UITableViewStyle.plain)
        assetTypeDropDown.delegate = self
        assetTypeDropDown.dataSource = self
        assetTypeDropDown.register(UITableViewCell.self,forCellReuseIdentifier: "cell")
        assetTypeDropDown.separatorStyle = UITableViewCellSeparatorStyle.none
        assetTypeDropDown.isHidden = true
        parentView.addSubview(assetTypeDropDown)
        
        footerView.frame = CGRect(x: 0, y: parentView.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: 80)
        self.assetTypesData()
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    //TODO: Tableview delegate & datasource methods
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.responseArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
        if(tableView == assetTypeDropDown)
        {
            cell.layoutMargins = UIEdgeInsets.zero
            cell.backgroundColor = UIColor.groupTableViewBackground
            
            cell.textLabel?.textColor = UIColor.black
            
            
            if let dic = self.responseArray[indexPath.row] as? NSDictionary{
                let name = dic.value(forKey: "name") as! String
                cell.textLabel?.text = name
               
            }
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        assetTypeDropDown.isHidden = true
        self.footerView.subviews.forEach({$0.removeFromSuperview()})
        if let dic = self.responseArray[indexPath.row] as? NSDictionary{
            let layout = dic.value(forKey: "layout") as! String
            let assetId = dic.value(forKey: "id") as! NSInteger
            let name = dic.value(forKey: "name") as! String
            assetTypeBtn.tag = assetId
            assetTypeBtn.setTitle(name, for: UIControlState.normal)
            self.assetTypeAttributes = dic
            let attributes = dic.value(forKey: "assetTypeAttributes") as! NSArray
            var yPos : CGFloat = 0
            attributeView.subviews.forEach({$0.removeFromSuperview()})
            attributeView.frame = CGRect(x: 0, y: assetTypeBtn.maxYOrigin + 10, width: ScreenSize.SCREEN_WIDTH, height: 70)
            parentView.addSubview(attributeView)
            
            
            for i in 0 ..< attributes.count{
                
                let attLabel = UILabel()
                attLabel.frame = CGRect(x: 10, y: yPos , width: ScreenSize.SCREEN_WIDTH - 20, height: 20)
                attLabel.text = (attributes[i] as AnyObject).value(forKey: "name") as? String
                attLabel.textColor = LOGIN_BG_COLOR
                attLabel.font = UIFont(name: "Futura-Medium", size: 14)
                attributeView.addSubview(attLabel)
                
                let attField = UITextField()
                attField.frame = CGRect(x: 10, y: attLabel.maxYOrigin, width: ScreenSize.SCREEN_WIDTH - 20, height: 40)
                attField.textColor = UIColor.black
                attField.placeholder = "\("Please enter") \(attLabel.text!)"
                attField.tag = (attributes[i] as AnyObject).value(forKey: "id") as! NSInteger
                attField.font = UIFont(name: "Futura-Medium", size: 15)
                attField.textAlignment = NSTextAlignment.left
                attField.setSignInBottomBorder()
                attributeView.addSubview(attField)
                yPos = yPos + 70
                self.attributeView.frame.size.height = attField.maxYOrigin + 5
            }
            
            self.parentView.contentSize.height = self.attributeView.maxYOrigin + 10
            
            if(layout.lowercased() == "fixed"){
                let pinLocButton = UIButton()
                pinLocButton.frame = CGRect(x: ScreenSize.SCREEN_WIDTH/2 - 35 , y: 3, width: 70, height: 70)
                pinLocButton.setTitle("Start", for: .normal)
                pinLocButton.addTarget(self, action: #selector(CreateAssetsViewController.startFixedClicked), for: .touchDown)
                globalStart = pinLocButton
                pinLocButton.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                pinLocButton.setTitleColor(UIColor.white, for: .normal)
                pinLocButton.layer.cornerRadius = pinLocButton.width/2
                footerView.addSubview(pinLocButton)
            }else {
                let startBtn = UIButton()
                startBtn.frame = CGRect(x: (footerView.width/2)/2 - 35 , y: 3, width: 70, height: 70)
                startBtn.setTitle("Start", for: .normal)
                startBtn.layer.cornerRadius = startBtn.width/2
                globalStart = startBtn
                startBtn.addTarget(self, action: #selector(CreateAssetsViewController.startSpreadClicked), for: .touchDown)
                footerView.addSubview(startBtn)
                
                let stopBtn = UIButton()
                stopBtn.frame = CGRect(x: footerView.width/2 + (footerView.width/2)/2 - 35 , y: 3, width: 70, height: 70)
                stopBtn.setTitle("Save", for: .normal)
                self.globalStop = stopBtn
                stopBtn.layer.cornerRadius = stopBtn.width/2
                footerView.addSubview(stopBtn)
                startBtn.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                startBtn.setTitleColor(UIColor.white, for: .normal)
                stopBtn.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                stopBtn.addTarget(self, action: #selector(CreateAssetsViewController.stopSpreadClicked), for: .touchDown)
                stopBtn.setTitleColor(UIColor.white, for: .normal)
                self.globalStop.isUserInteractionEnabled = false
            }
        }
    }
    
    func assetTypeClicked(sender:UIButton){
        
        if(self.responseArray.count > 0){
            assetTypeDropDown.isHidden = false
        }else
        {
            showAlertMessage(titleStr: "Alert", messageStr: "No data found.")
        }

    }

    func assetTypesData(){
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            let urlString = "\(BaseUrl)\(kAssetTypes)"
            print(urlString)
            let userdata = fetchuserdata()
            tokenStr =  userdata.userid
         showprogress(view: self.view)
        Alamofire.request(urlString, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenStr!)","Content-Type" : "application/json"]).responseJSON { response in
            if(response.result.isFailure){
                hideprogress()
                print("no data!");
            }else{
                if(response.response?.statusCode==200)
                {
                    hideprogress()
                    print(response.result.value!)
                   self.responseArray  = response.result.value! as! NSArray
                    self.assetTypeDropDown.reloadData()
                }else
                {
                    hideprogress()
                    print(response.result.value!)
                    
                }
             }
          }
        }
     else {
    print("Internet connection FAILED")
    //showAlertMessage(titleStr: kInterenetAlert, messageStr: kInterenetAlertMessage)
    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
    }

    }

    //TODO: UITextfield Delegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return textField.resignFirstResponder()
    }

    func startFixedClicked(){
        
        let alert = UIAlertController(title: title, message: "create asset with your location \(globalAddress) \(globalLat,globalLong).", preferredStyle: UIAlertControllerStyle.alert);
        let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
            self.createfixedAsset()
        }
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.cancel, handler: nil))

        alert.addAction(okAction)
        self.present(alert, animated: true, completion: nil)
        
    }
    
    func createfixedAsset()
    {
        guard nameField.text != "" else {
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter the Name")
            return
        }
        
        /* LatLong Values */
        let latLongDict = NSMutableDictionary()
        latLongDict["latitude"] = globalLat
        latLongDict["longitude"] = globalLong
        latLongArray.add(latLongDict)
        
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributes") as! NSArray
        let valueArray = NSMutableArray()
        
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary()
                            
                            let subDic = NSMutableDictionary()
                            subDic["id"] = textFid.tag
                            subDic["name"] = customdict["name"] as! String
                            
                            dict["attributeValue"] = textFid.text!
                            dict["assetTypeAttribute"] = subDic
                            valueArray.add(dict)
                        }
                        else{
                            showAlertMessage(titleStr: "Alert", messageStr: textFid.placeholder!)
                            return
                        }
                    }
                }
            }
            
        }
        
        let mainDict = NSMutableDictionary()
        mainDict["assetCoordinates"] = latLongArray
        mainDict["assetTypeId"] = assetTypeBtn.tag
        mainDict["name"] = nameField.text!
        mainDict["assetType"] = assetTypeAttributes
        mainDict["assetTypeAttributeValues"] = valueArray
        
        print(mainDict)
        
        let jsonDict : NSDictionary = mainDict as NSDictionary
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: jsonDict, options: [])
            if let jsonString = String(data: jsonData, encoding: String.Encoding.utf8) {
                print(jsonString)
                let urlstring = BaseUrl + kGetAssets
                let userdata = fetchuserdata()
                let tokenstring:String! = userdata.userid
                showprogress(view: self.view)
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    Alamofire.request("\(urlstring)", method: .post, parameters: (jsonDict as! Parameters), encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenstring!)", "Content-Type": "application/json"]).responseJSON { response in
                        if(response.response?.statusCode == 201){
                            hideprogress()
                            let assetsvc = myAssetsVC()
                            let backItem = UIBarButtonItem()
                            backItem.title = "Assets"
                            self.navigationItem.backBarButtonItem = backItem
                            self.navigationController?.pushViewController(assetsvc, animated: false)
                        }else{
                            hideprogress()
                            print("no data!");
                            showerrorprogress(title: "\(response.response?.statusCode)", view: self.view)                        }
                    }
                } else {
                    print("Internet connection FAILED")
                    //showAlertMessage(titleStr: kInterenetAlert, messageStr: kInterenetAlertMessage)
                    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                }
            }
        } catch {
            print(error)
            showAlertMessage(titleStr: "Error", messageStr: error as! String)
            return
        }
    }
    
    func startSpreadClicked(){
        guard nameField.text != "" else {
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter the Name")
            return
        }
        guard descriptionField.text != "" else {
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter the Description")
            return
        }
        
        
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributes") as! NSArray
        let valueArray = NSMutableArray()
        
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary()
                            
                            let subDic = NSMutableDictionary()
                            subDic["id"] = textFid.tag
                            subDic["name"] = customdict["name"] as! String
                            
                            dict["attributeValue"] = textFid.text!
                            dict["assetTypeAttribute"] = subDic
                            valueArray.add(dict)
                        }
                        else{
                            showAlertMessage(titleStr: "Alert", messageStr: textFid.placeholder!)
                            return
                        }
                    }
                }
            }
            
        }
        self.globalStop.isUserInteractionEnabled = true
        self.globalStart.isEnabled = false
        self.loaderView.frame = CGRect(x: 0, y: 0, width: self.parentView.width, height: self.parentView.contentSize.height)
        self.loaderView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        self.parentView.isUserInteractionEnabled = false
        self.parentView.addSubview(self.loaderView)
        self.spreadLatLongArray.removeAllObjects()
        let spreadLatLongDict = NSMutableDictionary()
        spreadLatLongDict["latitude"] = globalLat
        spreadLatLongDict["longitude"] = globalLong
        self.spreadLatLongArray.add(spreadLatLongDict)
        print(self.spreadLatLongArray)
        self.updateTimer = Timer.scheduledTimer(timeInterval: 10.0, target: self, selector: #selector(self.startSpreadTimer), userInfo: nil, repeats: true)
    }
    
    func startSpreadTimer(){
        let spreadLatLongDict = NSMutableDictionary()
        spreadLatLongDict["latitude"] = globalLat
        spreadLatLongDict["longitude"] = globalLong
        self.spreadLatLongArray.add(spreadLatLongDict)
        print(self.spreadLatLongArray)
    }
    
    func stopSpreadClicked(){
        self.globalStart.isEnabled = true
        self.loaderView.removeFromSuperview()
        self.parentView.isUserInteractionEnabled = true
        if(self.updateTimer.isValid){
            self.updateTimer.invalidate()
        }
        self.updateTimer = nil
        self.callUpdateService()
    }
    
    func callUpdateService(){
    
        
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributes") as! NSArray
        let valueArray = NSMutableArray()
        
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary()
                            
                            let subDic = NSMutableDictionary()
                            subDic["id"] = textFid.tag
                            subDic["name"] = customdict["name"] as! String
                            
                            dict["attributeValue"] = textFid.text!
                            dict["assetTypeAttribute"] = subDic
                            valueArray.add(dict)
                        }
                        else{
                            showAlertMessage(titleStr: "Alert", messageStr: textFid.placeholder!)
                            return
                        }
                    }
                }
            }
            
        }
        
        let mainDict = NSMutableDictionary()
        mainDict["assetCoordinates"] = spreadLatLongArray
        mainDict["assetTypeId"] = assetTypeBtn.tag
        mainDict["name"] = nameField.text!
        mainDict["description"] = descriptionField.text!
        mainDict["assetType"] = assetTypeAttributes
        mainDict["assetTypeAttributeValues"] = valueArray
        
        print(mainDict)
        
        let jsonDict : NSDictionary = mainDict as NSDictionary
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: jsonDict, options: [])
            if let jsonString = String(data: jsonData, encoding: String.Encoding.utf8) {
                print(jsonString)
                let urlstring = BaseUrl + kGetAssets
                let userdata = fetchuserdata()
                let tokenstring:String! = userdata.userid
                showprogress(view: self.view)
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    Alamofire.request("\(urlstring)", method: .post, parameters: (jsonDict as! Parameters), encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenstring!)", "Content-Type": "application/json"]).responseJSON { response in
                        if(response.response?.statusCode == 201){
                            hideprogress()
                            let assetsvc = myAssetsVC()
                            let backItem = UIBarButtonItem()
                            backItem.title = "Assets"
                            self.navigationItem.backBarButtonItem = backItem
                            self.navigationController?.pushViewController(assetsvc, animated: false)
                        }else{
                            hideprogress()
                            print("no data!");
                            showerrorprogress(title: "\(response.response?.statusCode)", view: self.view)                        }
                    }
                } else {
                    print("Internet connection FAILED")
                    showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                }
            }
        } catch {
            print(error)
            showAlertMessage(titleStr: "Error", messageStr: error as! String)
            return
        }

    }
}
