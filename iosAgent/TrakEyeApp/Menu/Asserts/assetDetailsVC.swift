//
//  assetDetailsVC.swift
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

class assetsDetailsDict {
    var createdDate = UILabel()
    var updatedDate = UILabel()
    var description = UITextView()
    var nameLbl = UILabel()
    var userId = UILabel()
    var caseId = UILabel()
    var assetType = UILabel()
    var imagesScrollView = UIScrollView()
    var uploadedImages = UIButton()
    var addImage = UIButton()
    
}


class assetDetailsVC: UIViewController, UITextViewDelegate,UINavigationControllerDelegate, GMSMapViewDelegate, CLLocationManagerDelegate{
    var assetsClass = assetsDetailsDict()
    var parentView = UIScrollView()
    var idValue = NSInteger()
    var arrayCases = NSDictionary()
    var mapView: GMSMapView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.edgesForExtendedLayout = []
        self.mapView = GMSMapView()
        self.mapView.delegate = self
        
        let image = UIImage(named: "edit")
        let rightButton = UIBarButtonItem(image: image, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.rightMenuClicked))
        self.navigationItem.rightBarButtonItem = rightButton
        
        parentView = UIScrollView(frame: CGRect(x: 0, y: 0, width: view.width, height: view.height))
        view.addSubview(parentView)
        self.createView()
        
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(assetDetailsVC.dismissKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        //super.viewWillAppear(false)
        self.callService()
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func createView(){
        
        let nmTitle = UILabel()
        nmTitle.frame = CGRect(x: 10, y: 0, width: view.width - 20, height: 25)
        nmTitle.text = "Name"
        parentView.addSubview(nmTitle)
        
        assetsClass.nameLbl = UILabel()
        assetsClass.nameLbl.frame = CGRect(x: 10, y: nmTitle.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(assetsClass.nameLbl)
        self.designLabels123(firstLab: nmTitle, secondLab: assetsClass.nameLbl)
        
//        let dTitle = UILabel()
//        dTitle.frame = CGRect(x: 10, y: assetsClass.nameLbl.maxYOrigin + 2, width: view.width - 20, height: 25)
//        dTitle.text = "Description"
//        parentView.addSubview(dTitle)
//        
//        assetsClass.description = UITextView()
//        assetsClass.description.frame = CGRect(x: 10, y: dTitle.maxYOrigin, width: view.width - 20, height: 50)
//        parentView.addSubview(assetsClass.description)
//        self.designtextV123(firstLab: dTitle, secondLab: assetsClass.description)
//        
//        let cdTitle = UILabel()
//        cdTitle.frame = CGRect(x: 10, y: assetsClass.description.maxYOrigin + 2, width: view.width - 20, height: 25)
//        cdTitle.text = "Created Date"
//        parentView.addSubview(cdTitle)
//        
//        assetsClass.createdDate = UILabel()
//        assetsClass.createdDate.frame = CGRect(x: 10, y: cdTitle.frame.origin.y + cdTitle.height, width: view.width - 20, height: 25)
//        parentView.addSubview(assetsClass.createdDate)
//        self.designLabels123(firstLab: cdTitle, secondLab: assetsClass.createdDate)
//        
//        let mdTitle = UILabel()
//        mdTitle.frame = CGRect(x: 10, y: assetsClass.createdDate.maxYOrigin + 2, width: view.width - 20, height: 25)
//        mdTitle.text = "Updated Date"
//        parentView.addSubview(mdTitle)
//        
//        assetsClass.updatedDate = UILabel()
//        assetsClass.updatedDate.frame = CGRect(x: 10, y: mdTitle.maxYOrigin, width: view.width - 20, height: 25)
//        parentView.addSubview(assetsClass.updatedDate)
//        self.designLabels123(firstLab: mdTitle, secondLab: assetsClass.updatedDate)
        
        
        let camera = GMSCameraPosition.camera(withLatitude: 18.5203, longitude: 73.8567, zoom: 7.0)
        self.mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        self.mapView.delegate = self
        self.mapView.isMyLocationEnabled = true
        self.mapView.frame = CGRect(x: 0.0, y: assetsClass.nameLbl.maxYOrigin + 20, width: self.view.width, height: 350)
        parentView.addSubview(self.mapView)
        parentView.contentSize.height = self.mapView.maxYOrigin + 100
    }
    
    func callService(){
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            showprogress(view: self.view)
            let urlString = "\(kGetAssets)/\(idValue)"
            print(urlString)
            
            let utliti = Utilities()
            let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
            
            utliti.getDICTIONARYfromserverwithstring(input: urlString,parameters: parameters) { (true,result) in
                self.arrayCases  = result as! NSDictionary
                print(self.arrayCases)
                self.updateDetailswithValues()
                hideprogress()
            }
        }
    }
    
    func updateDetailswithValues()
    {
        let responsedata = self.arrayCases
        
        let createdDate = responsedata.value(forKey: "createDate") as! NSInteger
        assetsClass.createdDate.text = createdDate.toHour//commonFunctions().millsecToDate(milli: createdDate)
        
        let modifiedDate = responsedata.value(forKey: "updateDate") as! NSInteger
        assetsClass.updatedDate.text = modifiedDate.toHour//commonFunctions().millsecToDate(milli: modifiedDate)
        
        if let description = responsedata.value(forKey: "description") as? String{
            assetsClass.description.text = description
        }
        
        assetsClass.nameLbl.text = responsedata.value(forKey: "name") as? String
        
        
        assetsClass.imagesScrollView.subviews.forEach({ $0.removeFromSuperview() })
        self.createMapView(responsedata: responsedata, mapView: self.mapView)
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
                let myImage =   UIImage(data: imageData as! Data)
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
    
    
    func rightMenuClicked(){
        let vc = editAssetVC()
        vc.responseArray = self.arrayCases
        self.navigationController?.pushViewController(vc, animated: false)
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if text == "\n"  // Recognizes enter key in keyboard
        {
            textView.resignFirstResponder()
            return false
        }
        return true
    }
    
    func designLabels123(firstLab:UILabel,secondLab:UILabel){
        firstLab.textColor = UIColor.cyan
        firstLab.font = UIFont(name: fontName, size: 14)
        secondLab.textColor = UIColor.black
        secondLab.font = UIFont(name: fontName, size: 14)
        let bord = UILabel()
        bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
        bord.backgroundColor = UIColor.lightGray
        secondLab.superview!.addSubview(bord)
    }
    func designtextV123(firstLab:UILabel,secondLab:UITextView){
        firstLab.textColor = UIColor.cyan
        firstLab.font = UIFont(name: fontName, size: 14)
        secondLab.textColor = UIColor.black
        secondLab.font = UIFont(name: fontName, size: 14)
        let bord = UILabel()
        bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
        bord.backgroundColor = UIColor.lightGray
        secondLab.superview!.addSubview(bord)
        secondLab.isEditable = false
        secondLab.isScrollEnabled = true
    }
    func designtext123(firstLab:UILabel,secondLab:UITextField){
        firstLab.textColor = UIColor.cyan
        firstLab.font = UIFont(name: fontName, size: 14)
        secondLab.textColor = UIColor.black
        secondLab.font = UIFont(name: fontName, size: 14)
        let bord = UILabel()
        bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
        bord.backgroundColor = UIColor.lightGray
        secondLab.superview!.addSubview(bord)
        secondLab.isUserInteractionEnabled = false
    }
}
