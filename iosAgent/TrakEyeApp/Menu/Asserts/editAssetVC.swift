//
//  editAssetVC.swift
//  TrakEyeApp
//
//  Created by Tresbu Technologies Pvt Ltd on 14/12/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import TPKeyboardAvoiding
import Alamofire
import GoogleMaps
import GooglePlaces

class editAssetVC: UIViewController, GMSMapViewDelegate, CLLocationManagerDelegate {
    var parentView = TPKeyboardAvoidingScrollView()
    var nameField = FloatLabelTextField()
    var descriptionField = FloatLabelTextField()
    var assetTypeBtn = UIButton()
    var tokenStr  :String!
    var responseArray = NSDictionary()
    var footerView = UIView()
    var attributeView  = UIView()
    var assetTypeAttributes = NSDictionary()
    var latLongArray = NSMutableArray()
    var spreadLatLongArray = NSMutableArray()
    var updateTimer : Timer!
    var isStartClicked : Bool = false
    var globalStart = UIButton()
    var loaderView = UIView()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let navigationBar = UINavigationBar(frame: CGRect(x: 0, y: 0, width: view.width, height: 64))
        navigationBar.backgroundColor = LOGIN_BG_COLOR
        let navigationItem = UINavigationItem()
        navigationBar.items = [navigationItem]
        
        let headingLabel = UILabel()
        headingLabel.frame = CGRect(x: view.width/2 - 42.5, y: 25, width: ScreenSize.SCREEN_WIDTH/2, height: 35)
        headingLabel.text = "Edit Asset"
        headingLabel.textColor = UIColor.white
        headingLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 17)
        headingLabel.textAlignment = NSTextAlignment.left
        navigationBar.addSubview(headingLabel)
        navigationBar.tintColor = UIColor.white
        let image = UIImage(named: "back_img")
        let leftButton = UIBarButtonItem(image: image, style: UIBarButtonItemStyle.plain, target: self, action: #selector(backClicked))
        navigationItem.leftBarButtonItem = leftButton
        self.view.addSubview(navigationBar)
        
        parentView = TPKeyboardAvoidingScrollView(frame: CGRect(x: 0, y: 64, width: view.width, height: view.height - 144))
        //parentView.backgroundColor = UIColor.red
        self.view.addSubview(parentView)
        
        nameField.frame = CGRect(x: 10, y: 10, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        nameField.keyboardType = UIKeyboardType.alphabet
        nameField.placeholder = "Name"
        nameField.textColor = UIColor.black
        nameField.font = UIFont(name: "Futura-Medium", size: 15)
        nameField.textAlignment = NSTextAlignment.left
        nameField.setSignInBottomBorder()
        parentView.addSubview(nameField)
        
        descriptionField.frame = CGRect(x: 10, y: nameField.maxYOrigin + 10, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        descriptionField.keyboardType = UIKeyboardType.alphabet
        descriptionField.placeholder = "Description"
        descriptionField.textColor = UIColor.black
        descriptionField.font = UIFont(name: "Futura-Medium", size: 15)
        descriptionField.textAlignment = NSTextAlignment.left
        descriptionField.setSignInBottomBorder()
        parentView.addSubview(descriptionField)
        
        assetTypeBtn.frame = CGRect(x: 10, y: descriptionField.maxYOrigin + 20, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        assetTypeBtn.layer.borderWidth = 1.0
        assetTypeBtn.layer.cornerRadius = 5.0
        assetTypeBtn.setTitleColor(UIColor.black, for: UIControlState.normal)
        assetTypeBtn.titleLabel?.textAlignment = NSTextAlignment.center
        assetTypeBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        parentView.addSubview(assetTypeBtn)
        
        
        
        footerView.frame = CGRect(x: 0, y: parentView.maxYOrigin, width: ScreenSize.SCREEN_WIDTH, height: 80)
        //footerView.backgroundColor = UIColor.blue
        self.view.addSubview(footerView)
        
        self.displayValues()
        
        
        //self.assetTypesData()
        // Do any additional setup after loading the view.
    }
    
    func displayValues(){
        let responsedata = self.responseArray
        guard responsedata.count > 0 else{
            return
        }
        if let description = responsedata.value(forKey: "description") as? String{
            self.descriptionField.text = description
        }
        
        if let name = responsedata.value(forKey: "name") as? String{
            self.nameField.text = name
        }
        
        if let assetType = responsedata.value(forKey: "assetType") as? NSDictionary{
            let layout = assetType.value(forKey: "layout") as! String
            assetTypeBtn.setTitle((assetType.value(forKey: "name") as! String), for: .normal)
            if(layout.lowercased() == "fixed"){
                let pinLocButton = UIButton()
                pinLocButton.frame = CGRect(x: ScreenSize.SCREEN_WIDTH/2 - 35 , y: 3, width: 70, height: 70)
                pinLocButton.setTitle("Start", for: .normal)
                pinLocButton.addTarget(self, action: #selector(self.startFixedClicked), for: .touchDown)
                pinLocButton.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                pinLocButton.setTitleColor(UIColor.white, for: .normal)
                pinLocButton.layer.cornerRadius = pinLocButton.width/2
                globalStart = pinLocButton
                footerView.addSubview(pinLocButton)
            }else {
                let startBtn = UIButton()
                startBtn.frame = CGRect(x: (footerView.width/2)/2 - 35 , y: 3, width: 70, height: 70)
                startBtn.setTitle("Start", for: .normal)
                startBtn.layer.cornerRadius = startBtn.width/2
                startBtn.addTarget(self, action: #selector(self.startSpreadClicked), for: .touchDown)
                footerView.addSubview(startBtn)
                globalStart = startBtn
                let stopBtn = UIButton()
                stopBtn.frame = CGRect(x: footerView.width/2 + (footerView.width/2)/2 - 35 , y: 3, width: 70, height: 70)
                stopBtn.setTitle("Save", for: .normal)
                stopBtn.layer.cornerRadius = stopBtn.width/2
                footerView.addSubview(stopBtn)
                startBtn.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                startBtn.setTitleColor(UIColor.white, for: .normal)
                stopBtn.backgroundColor = self.navigationController?.navigationBar.backgroundColor
                stopBtn.addTarget(self, action: #selector(self.stopSpreadClicked), for: .touchDown)
                stopBtn.setTitleColor(UIColor.white, for: .normal)
            }
        }
        
        
        self.assetTypeAttributes = responsedata
        let attributes = responsedata.value(forKey: "assetTypeAttributeValues") as! NSArray
        var yPos : CGFloat = 0
        attributeView.subviews.forEach({$0.removeFromSuperview()})
        attributeView.frame = CGRect(x: 0, y: assetTypeBtn.maxYOrigin + 10, width: ScreenSize.SCREEN_WIDTH, height: 70)
        parentView.addSubview(attributeView)
        for i in 0 ..< attributes.count{
            let data = (attributes[i] as AnyObject).value(forKey: "assetTypeAttribute") as! NSDictionary
            let attLabel = UILabel()
            attLabel.frame = CGRect(x: 10, y: yPos , width: ScreenSize.SCREEN_WIDTH - 20, height: 20)
            attLabel.text = data.value(forKey: "name") as? String
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
        
        let camera = GMSCameraPosition.camera(withLatitude: 18.5203, longitude: 73.8567, zoom: 4.0)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.delegate = self
        mapView.isMyLocationEnabled = true
        mapView.frame = CGRect(x: 0.0, y: self.attributeView.maxYOrigin + 20, width: self.view.width, height: 250)
        parentView.addSubview(mapView)
        parentView.contentSize.height = mapView.maxYOrigin + 100
        self.createMapView(responsedata: responsedata, mapView: mapView)
    }
    
    func createMapView(responsedata:NSDictionary,mapView:GMSMapView){
        let assetType = responsedata.value(forKey: "assetType") as! NSDictionary
        let layout = assetType.value(forKey: "layout") as! String
        
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
                    mark.icon = resizeImage(image: myImage!, newWidth: 20)//myImage
                    bounds = bounds.includingCoordinate(mark.position)
                }
                mapView.animate(with: GMSCameraUpdate.fit(bounds))
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
            
            let fMarker = GMSMarker()
            fMarker.position = CLLocationCoordinate2DMake(plotValues.first?.value(forKey: "latitude") as! CLLocationDegrees, plotValues.first?.value(forKey: "longitude") as! CLLocationDegrees)
            //fMarker.map = mapView
            
            let sMarker = GMSMarker()
            sMarker.position = CLLocationCoordinate2DMake(plotValues.last?.value(forKey: "latitude") as! CLLocationDegrees, plotValues.last?.value(forKey: "longitude") as! CLLocationDegrees)
            //sMarker.map = mapView
            mapView.animate(with: GMSCameraUpdate.fit(bounds))
        }
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
    }
    override func viewWillDisappear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = false
    }
    
    func backClicked() {
        //updateTimer.invalidate()
        self.navigationController?.pop(animated: true)
        //self.dismiss(animated: true, completion: nil)
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
        
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributeValues") as! NSArray
        let valueArray = NSMutableArray()
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary(dictionary: customdict)
                            dict["attributeValue"] = textFid.text!
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
        print(valueArray)
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
    
    func startFixedClicked(){
        guard nameField.text != "" else {
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter the Name")
            return
        }
        guard descriptionField.text != "" else {
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter the Description")
            return
        }
        
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributeValues") as! NSArray
        let valueArray = NSMutableArray()
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary(dictionary: customdict)
                            dict["attributeValue"] = textFid.text!
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
        
        let spreadLatLongDict = NSMutableDictionary()
        spreadLatLongDict["latitude"] = globalLat
        spreadLatLongDict["longitude"] = globalLong
        self.spreadLatLongArray.add(spreadLatLongDict)
        print(self.spreadLatLongArray)
        
        
        self.callUpdateService()
        
    }
     
    
    func callUpdateService(){
        let attributes = assetTypeAttributes.value(forKey: "assetTypeAttributeValues") as! NSArray
        let valueArray = NSMutableArray()
        
        for jj in attributes{
            let customdict = jj as! NSDictionary
            for sbViews in attributeView.subviews{
                if let textFid:UITextField = sbViews as? UITextField{
                    if(textFid.tag == customdict["id"] as! NSInteger){
                        if(textFid.text != ""){
                            let dict = NSMutableDictionary(dictionary: customdict)
                            dict["attributeValue"] = textFid.text!
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
        mainDict["assetTypeId"] = 0
        mainDict["id"] = self.responseArray.value(forKey: "id") as! NSInteger
        mainDict["name"] = nameField.text!
        mainDict["description"] = descriptionField.text!
        mainDict["assetType"] = self.responseArray.value(forKey: "assetType") as! NSDictionary//assetTypeAttributes
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
                    Alamofire.request("\(urlstring)", method: .put, parameters: (jsonDict as! Parameters), encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenstring!)", "Content-Type": "application/json"]).responseJSON { response in
                        if(response.response?.statusCode == 200){
                            hideprogress()
                            self.navigationController?.pop(animated: true)
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
