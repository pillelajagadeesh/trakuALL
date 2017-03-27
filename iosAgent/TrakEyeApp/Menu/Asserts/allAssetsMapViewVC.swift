//
//  allAssetsMapViewVC.swift
//  TrakEyeApp
//
//  Created by Tresbu Technologies Pvt Ltd on 19/12/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import Foundation
import UIKit
import Alamofire
import CoreTelephony
import TPKeyboardAvoiding
import GoogleMaps
import GooglePlaces
class allAssetsMapViewVC: UIViewController, GMSMapViewDelegate, CLLocationManagerDelegate {
    
    var mapView = GMSMapView()
    var responseArray = NSMutableArray()
    var resultArray = [NSDictionary]()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.edgesForExtendedLayout = []
        self.mapView = GMSMapView()
        let camera = GMSCameraPosition.camera(withLatitude: 20.5937, longitude: 78.9629, zoom: 10.0)
        self.mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        self.mapView.delegate = self
        //self.mapView.frame = CGRect(x: 0, y: 0, width: self.view.width, height: self.view.height)
        //self.view.addSubview(self.mapView)
        self.view = self.mapView
        if(self.responseArray.count > 0){
            //let deadlineTime = DispatchTime.now() + .seconds(1)
            self.getData()
        }
    }
    func getData(){
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.03) {
            let _ = self.responseArray.map { (self.createMapView(responsedata: $0 as! NSDictionary)) }
        }
    }
    /*func getData(){
        let assetIds = self.responseArray.map { (($0 as! NSDictionary).value(forKey: "id") as! NSInteger) }
        let deadlineTime = DispatchTime.now() + .seconds(1)
        var count = 0
        for idValue in assetIds{
            DispatchQueue.main.asyncAfter(deadline: deadlineTime) {
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    let urlString = "\(kGetAssets)/\(idValue)"
                    print(urlString)
                    let utliti = Utilities()
                    let parameters:[String: Any] = ["page" : "0", "size" : "30", "sort" : "id,asc"]
                    utliti.getDICTIONARYfromserverwithstring(input: urlString,parameters: parameters) { (true,result) in
                        count += 1
                        let responseData = result as! NSDictionary
                        self.createMapView(responsedata: responseData,count: count,assetIdsCount: assetIds.count)
                        if(count == assetIds.count){
                            hideprogress()
                            self.callFinal()
                        }
                    }
                }
            }
        }
    }*/
    
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
    
}
