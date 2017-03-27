//
//  CreatecaseVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 18/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire
import Photos
import TPKeyboardAvoiding
import AVFoundation

enum TimerState: NSInteger {
    case stopped = 10
  
}

class CreatecaseVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UITextFieldDelegate, UITableViewDelegate,UITableViewDataSource, AVAudioRecorderDelegate {

    var descriptionField = FloatLabelTextField()
    var escalated = FloatLabelTextField()
    var uploadimageField = FloatLabelTextField()
    var cancelBtn = UIButton()
    var saveBtn = UIButton()
    var checkboxBtn = UIButton()
    var imageuploadBtn = UIButton()
    var isclicked : Bool = false
    var imagePicker = UIImagePickerController()
    var strBase64 = String()
    var Pickedimage = UIImage()
    var tableView = UITableView()
    var arrayCases = NSMutableArray()
    var arrayPriority: [String] = ["LOW","MEDIUM","HIGH","CRITICAL"]
    var AttributesArray = NSArray()
    var dropdownBtn = UIButton()
    var proirityDropdownBtn = UIButton()
    var dropdownTableView = UITableView()
    var prioritydropdownTableView = UITableView()
    var index = Int()
    var attributedDict  = NSDictionary()
    var selected :Bool = false
    var scroll = TPKeyboardAvoidingScrollView()
    var imageData: NSData? = nil
    var recordingSession: AVAudioSession!
    var audioRecorder: AVAudioRecorder!
    var recordButton: UIButton!
    var audioFilename: URL? = nil
    var audio = String()
    var audioBase64String = String()
    var nextAPICallIndex : Int = 1
    var searchtext = UITextField()
    var dropview = UIView()
    var address = String()
    var attributeView  = UIView()
    var filteredUserData = NSArray()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        //self.arrayCases.add("Select Case Type")
        
        let navigationBar = UINavigationBar(frame: CGRect(x: 0, y: 0, width: view.width, height: 64))
        navigationBar.backgroundColor = LOGIN_BG_COLOR
        let navigationItem = UINavigationItem()
        navigationBar.items = [navigationItem]
        
        let headingLabel = UILabel()
        headingLabel.frame = CGRect(x: view.width/2 - 42.5, y: 25, width: ScreenSize.SCREEN_WIDTH/2, height: 35)
        headingLabel.text = "Create Case"
        headingLabel.textColor = UIColor.white
        headingLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 17)
        headingLabel.textAlignment = NSTextAlignment.left
        navigationBar.addSubview(headingLabel)
        navigationBar.tintColor = UIColor.white
        let image = UIImage(named: "back_img")
        //
        let leftButton = UIBarButtonItem(image: image, style: UIBarButtonItemStyle.plain, target: self, action: #selector(backClicked))
        navigationItem.leftBarButtonItem = leftButton
        self.view.addSubview(navigationBar)
        
        scroll = TPKeyboardAvoidingScrollView(frame: CGRect(x: 0, y: 64, width: view.width, height: view.height-114))
        self.view.addSubview(scroll)
        
        
        descriptionField.frame = CGRect(x: 10, y: 10, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        descriptionField.keyboardType = UIKeyboardType.alphabet
        descriptionField.delegate = self
        descriptionField.placeholder = "Description"
        descriptionField.textColor = UIColor.black
        descriptionField.font = UIFont(name: "Futura-Medium", size: 14)
        descriptionField.textAlignment = NSTextAlignment.left
        descriptionField.setSignInBottomBorder()
        scroll.addSubview(descriptionField)
        
//        escalated.frame = CGRect(x: 10, y: 140, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
//        escalated.keyboardType = UIKeyboardType.alphabet
//        escalated.isUserInteractionEnabled = false
//        escalated.placeholder = "Escalated"
//        escalated.textColor = UIColor.black
//        escalated.font = UIFont(name: "Futura-Medium", size: 14)
//        escalated.returnKeyType=UIReturnKeyType.done
//        escalated.textAlignment = NSTextAlignment.left
//        escalated.setSignInBottomBorder()
//        self.view.addSubview(escalated)
        
        
        let escalatedlbl = UILabel()
        escalatedlbl.frame = CGRect(x: 10, y: descriptionField.maxYOrigin, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        escalatedlbl.text = "Escalated"
        escalatedlbl.textColor = LOGIN_BG_COLOR
        escalatedlbl.font = UIFont(name: fontName, size: 15)
        escalatedlbl.textAlignment = NSTextAlignment.left
        scroll.addSubview(escalatedlbl)
        
        let mdTitle = UILabel()
        mdTitle.frame = CGRect(x: 10, y: escalatedlbl.maxYOrigin , width: ScreenSize.SCREEN_WIDTH-20, height: 1)
        mdTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.6)
        scroll.addSubview(mdTitle)
        
        
        checkboxBtn.frame = CGRect(x: escalatedlbl.frame.size.width-40, y: descriptionField.maxYOrigin + 5 , width: 25, height: 25)
        let checkbox = UIImage.init(named: "Boxgrey")
        checkboxBtn.setImage(checkbox, for: UIControlState.normal)
        checkboxBtn.addTarget(self, action: #selector(CreatecaseVC.checkboxClicked), for: UIControlEvents.touchDown)
        scroll.addSubview(checkboxBtn)
        
        
        proirityDropdownBtn.frame = CGRect(x: 10, y: mdTitle.maxYOrigin + 5 , width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        proirityDropdownBtn.layer.borderWidth = 1.0
        proirityDropdownBtn.layer.cornerRadius = 5.0
        proirityDropdownBtn.isSelected = false
        proirityDropdownBtn.setTitle("Priority", for: UIControlState.normal)
        proirityDropdownBtn.setTitleColor(UIColor.black, for: UIControlState.normal)
        proirityDropdownBtn.titleLabel?.textAlignment = NSTextAlignment.center
        proirityDropdownBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        proirityDropdownBtn.addTarget(self, action: #selector(CreatecaseVC.prioritydropDownView), for: UIControlEvents.touchDown)
        scroll.addSubview(proirityDropdownBtn)
        
        let uploadimagelbl = UILabel()
        uploadimagelbl.frame = CGRect(x: 10, y: proirityDropdownBtn.maxYOrigin, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        //uploadimagelbl.attributedText = myString
        uploadimagelbl.text = "Upload Image"
        uploadimagelbl.textColor = LOGIN_BG_COLOR
        uploadimagelbl.font = UIFont(name: fontName, size: 15)
        uploadimagelbl.textAlignment = NSTextAlignment.left
        scroll.addSubview(uploadimagelbl)
        
        let uploadTitle = UILabel()
        uploadTitle.frame = CGRect(x: 10, y: uploadimagelbl.maxYOrigin , width: ScreenSize.SCREEN_WIDTH-20, height: 1)
        uploadTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.6)
        scroll.addSubview(uploadTitle)
        
        imageuploadBtn.frame = CGRect(x: uploadimagelbl.frame.size.width-40, y: proirityDropdownBtn.maxYOrigin + 5 , width: 25, height: 25)
        let photoupload = UIImage.init(named: "Camera")
        imageuploadBtn.setImage(photoupload, for: UIControlState.normal)
        imageuploadBtn.addTarget(self, action: #selector(CreatecaseVC.imageuploaClicked), for: UIControlEvents.touchDown)
        scroll.addSubview(imageuploadBtn)
        
        dropdownBtn.frame = CGRect(x: 10, y: (uploadimagelbl.frame.origin.y + uploadimagelbl.frame.size.height) + 10 , width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        dropdownBtn.layer.borderWidth = 1.0
        dropdownBtn.layer.cornerRadius = 5.0
        dropdownBtn.isSelected = false
        dropdownBtn.setTitle("Case Type", for: UIControlState.normal)
        dropdownBtn.setTitleColor(UIColor.black, for: UIControlState.normal)
        dropdownBtn.titleLabel?.textAlignment = NSTextAlignment.center
        dropdownBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        dropdownBtn.addTarget(self, action: #selector(CreatecaseVC.dropDownView), for: UIControlEvents.touchDown)
        scroll.addSubview(dropdownBtn)
        
        
        let footerview = UIView()
        footerview.frame =  CGRect(x: 0, y: ScreenSize.SCREEN_HEIGHT-80, width: ScreenSize.SCREEN_WIDTH, height: 50)
        self.view.addSubview(footerview)
        
        
        cancelBtn.frame = CGRect(x: 20, y: 5, width: (ScreenSize.SCREEN_WIDTH-60)/2, height: 40)
        cancelBtn.backgroundColor = LOGIN_BG_COLOR
        cancelBtn.layer.cornerRadius = 5.0
        cancelBtn.titleLabel?.textColor = UIColor.black
        cancelBtn.setTitle("Cancel", for: UIControlState.normal)
        cancelBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
        cancelBtn.titleLabel?.textAlignment = NSTextAlignment.center
        cancelBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        cancelBtn.addTarget(self, action: #selector(CreatecaseVC.cancelClicked), for: UIControlEvents.touchDown)
        footerview.addSubview(cancelBtn)
        
        
        saveBtn.frame = CGRect(x: (cancelBtn.frame.origin.x + cancelBtn.frame.size.width) + 20 , y: 5, width: (ScreenSize.SCREEN_WIDTH-60)/2, height: 40)
        saveBtn.backgroundColor = LOGIN_BG_COLOR
        saveBtn.layer.cornerRadius = 5.0
        saveBtn.titleLabel?.textColor = UIColor.white
        saveBtn.setTitle("Save", for: UIControlState.normal)
        saveBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
        saveBtn.titleLabel?.textAlignment = NSTextAlignment.center
        saveBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        saveBtn.addTarget(self, action: #selector(CreatecaseVC.savelClicked), for: UIControlEvents.touchDown)
        footerview.addSubview(saveBtn)
        
        
        recordingSession = AVAudioSession.sharedInstance()
        
        do {
            try recordingSession.setCategory(AVAudioSessionCategoryPlayAndRecord)
            try recordingSession.setActive(true)
            recordingSession.requestRecordPermission() { [unowned self] allowed in
                DispatchQueue.main.async {
                    if allowed {
                        self.loadRecordingUI()
                    } else {
                        // failed to record!
                    }
                }
            }
        } catch {
            // failed to record!
        }
        
        let utliti = Utilities()
        let parameters:[String: Any] = ["page" : "0", "size" : "10", "sort" : "id,asc"]
        
        print(parameters)
        
        if UserDefaults.standard.bool(forKey: "networkStatus"){
            utliti.getdatafromserverwithstring(input: kGetCasetypes,parameters: parameters) { (true,result) in
                if let response = result as? NSArray{
                     print(response)
                    self.arrayCases.addObjects(from: response as! [Any])
                    if self.arrayCases.count>0{
                         self.tableView.reloadData()
                    }
                   
                }
                else
                {
                    print(result as! NSDictionary)
                }
            }

            
        }else{
            showAlertMessage(titleStr: kInterenetAlert, messageStr: kInterenetAlertMessage)
        }
        
        dropdownTableView = UITableView()
        prioritydropdownTableView = UITableView()
        
        let lat  = UserDefaults.standard.value(forKey: "lat") as! NSNumber
        let long  = UserDefaults.standard.value(forKey: "long") as! NSNumber
        
        let longitude :CLLocationDegrees = CLLocationDegrees(long)
        let latitude :CLLocationDegrees = CLLocationDegrees(lat)
        
        let location = CLLocation(latitude: latitude, longitude: longitude) //changed!!!
        print(location)
        
        CLGeocoder().reverseGeocodeLocation(location, completionHandler: {(placemarks, error) -> Void in
            print(location)
            
            if error != nil {
                print("Reverse geocoder failed with error" + (error?.localizedDescription)!)
                return
            }
            
            if (placemarks?.count)! > 0 {
                let pm = (placemarks?[0])! as CLPlacemark
                self.displayLocationInfo(placemark: pm)
            }
            else {
                print("Problem with the data received from geocoder")
            }
        })
        
        
    }

    //TODO: Audio recording
    
    func loadRecordingUI() {
        recordButton = UIButton(frame: CGRect(x: descriptionField.maxXOrigin, y: 64, width: 80, height: 64))
        //recordButton.titleLabel?.text = "Tap to Record"
        recordButton.titleLabel?.textColor = UIColor.red
        recordButton.isHidden = true
        recordButton.setTitle("Tap to Record", for: UIControlState.normal)
        recordButton.setTitleColor(UIColor.red, for: UIControlState.normal)
        recordButton.titleLabel?.font = UIFont.systemFont(ofSize: 8)
        recordButton.addTarget(self, action: #selector(recordTapped), for: .touchUpInside)
        self.view.addSubview(recordButton)
    }
    
    func startRecording() {
        audioFilename = getDocumentsDirectory().appendingPathComponent("recording.m4a")
        
        let settings = [
            AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
            AVSampleRateKey: 12000,
            AVNumberOfChannelsKey: 1,
            AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
        ]
        
        do {
            audioRecorder = try AVAudioRecorder(url: audioFilename!, settings: settings)
            audioRecorder.delegate = self
            audioRecorder.prepareToRecord()
            audioRecorder.record()
            
            recordButton.setTitle("Tap to Stop", for: .normal)
        } catch {
            finishRecording(success: false)
        }
    }
    
    func finishRecording(success: Bool) {
        audioRecorder.stop()
        audioRecorder = nil
        if success {
            recordButton.setTitle("Tap to Record", for: .normal)
        } else {
            recordButton.setTitle("Tap to Record", for: .normal)
            // recording failed :(
        }
        print(self.getDocumentsDirectory())
    }
    
    func audioRecorderDidFinishRecording(_ recorder: AVAudioRecorder, successfully flag: Bool) {
        
        let data =  NSData(contentsOf: recorder.url)
        audioBase64String = (data?.base64EncodedString(options: .lineLength64Characters))!
        print(String(describing: audioBase64String))
        if !flag {
            finishRecording(success: false)
        }
        
    }
    
    func recordTapped() {
        if audioRecorder == nil {
            startRecording()
        } else {
            finishRecording(success: true)
        }
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let documentsDirectory = paths[0]
        return documentsDirectory
    }

    //MARK: DropDown View
    
    func createDropdownview()
    {
        dropview.frame =  CGRect(x: 0, y: 64, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-64)
        dropview.backgroundColor = UIColor.black
        dropview.alpha = 1.0
        dropview.clipsToBounds = true
        self.view.addSubview(dropview)
        
        let Tabledropview = UIView()
        Tabledropview.frame =  CGRect(x: 20, y: 10, width: dropview.width-40, height: dropview.height-50)
        Tabledropview.backgroundColor = UIColor.white
        Tabledropview.layer.cornerRadius = 5.0
        Tabledropview.clipsToBounds = true
        dropview.addSubview(Tabledropview)
        
        APP_DELEGATE.window?.bringSubview(toFront: Tabledropview)
        
        searchtext.frame = CGRect(x: 0, y: 0, width: Tabledropview.width, height: 40)
        searchtext.delegate = self
        searchtext.placeholder = "Search by Case Type"
        searchtext.addTarget(self, action: #selector(textFieldTyping), for: .editingChanged)
        searchtext.leftViewMode = UITextFieldViewMode.always
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let image = UIImage(named: "search.png")
        imageView.image = image
        searchtext.leftView = imageView
        Tabledropview.addSubview(searchtext)
        
        dropdownTableView = UITableView()
        dropdownTableView = UITableView(frame: CGRect(x:0, y:searchtext.maxYOrigin, width:Tabledropview.width, height:Tabledropview.height-150), style: UITableViewStyle.plain)
        dropdownTableView.backgroundColor = UIColor.white
        dropdownTableView.delegate = self
        dropdownTableView.dataSource = self
        dropdownTableView.tableFooterView = UIView()
        dropdownTableView.layoutMargins = UIEdgeInsets.zero
        dropdownTableView.separatorInset = UIEdgeInsets.zero
        dropdownTableView.register(UITableViewCell.self,forCellReuseIdentifier: "cell")
        dropdownTableView.separatorStyle = UITableViewCellSeparatorStyle.singleLine
        Tabledropview.addSubview(dropdownTableView)
        
        let cancelBtn = UIButton()
        cancelBtn.frame = CGRect(x: 20, y: dropdownTableView.maxYOrigin + 40, width: Tabledropview.width-40, height: 40)
        cancelBtn.backgroundColor = LOGIN_BG_COLOR
        cancelBtn.layer.cornerRadius = 5.0
        cancelBtn.layer.borderWidth = 1.0
        cancelBtn.layer.borderColor = UIColor.darkGray.cgColor
        cancelBtn.clipsToBounds = true
        cancelBtn.setTitle("Cancel", for: UIControlState.normal)
        cancelBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
        cancelBtn.titleLabel?.textAlignment = NSTextAlignment.center
        cancelBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        cancelBtn.addTarget(self, action: #selector(CreatecaseVC.dropviewCancel), for: UIControlEvents.touchDown)
        Tabledropview.addSubview(cancelBtn)
        
    }
    
    func dropviewCancel()
    {
        dropview.removeFromSuperview()
    }
    
    func textFieldTyping(textField:UITextField)
    {
        if(searchtext.text?.characters.count)!>2{
            let resultPredicate = NSPredicate(format: "name contains[c] %@", textField.text!)
            filteredUserData = self.arrayCases.filtered(using: resultPredicate) as NSArray
            dropdownTableView.reloadData()
        }else
        {
            dropdownTableView.reloadData()
        }
    }

    
    //TODO: Button Action
    
    func backClicked() {
        self.navigationController?.pop(animated: true)
        //self.dismiss(animated: true, completion: nil)
    }
    
    func cancelClicked()
    {
        self.navigationController?.pop(animated: true)
    }
    func savelClicked(){
        self.uploadImageAndData()
    }
    func checkboxClicked()
    {
        if isclicked == false {
            let checkbox = UIImage.init(named: "Boxblue")
            checkboxBtn.setImage(checkbox, for: UIControlState.normal)
            
            isclicked = true
        }else
        {
            let checkbox = UIImage.init(named: "Boxgrey")
            checkboxBtn.setImage(checkbox, for: UIControlState.normal)
            isclicked = false
        }
    }
    
    func imageuploaClicked()
    {
        let settingsActionSheet: UIAlertController = UIAlertController(title:nil, message:nil, preferredStyle:UIAlertControllerStyle.actionSheet)
        settingsActionSheet.addAction(UIAlertAction(title:"Camera", style:UIAlertActionStyle.default, handler:{ action in
            if UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.camera){
                self.imagePicker.delegate = self
                self.imagePicker.allowsEditing = false
                self.imagePicker.sourceType = .camera
                self.present(self.imagePicker, animated: true, completion: nil)
            }else
            {
                showAlertMessage(titleStr: Title, messageStr: "Camera is not Available in this device.")
            }
        }))
        settingsActionSheet.addAction(UIAlertAction(title:"choose from Gallery", style:UIAlertActionStyle.default, handler:{ action in
            self.imagePicker.delegate = self
            self.imagePicker.allowsEditing = false
            self.imagePicker.sourceType = .photoLibrary
            self.present(self.imagePicker, animated: true, completion: nil)
        }))
        settingsActionSheet.addAction(UIAlertAction(title:"Cancel", style:UIAlertActionStyle.cancel, handler:nil))
        present(settingsActionSheet, animated: false, completion: nil)
        
    }
    
    func dropDownView(sender:UIButton){
        if self.arrayCases.count>0{
//            if(sender.isSelected){
//                UIView.animate(
//                    withDuration: 0.5,
//                    delay: 0,
//                    options: .curveEaseInOut,
//                    animations: {
//                        self.dropdownTableView.frame.size.height = 0
//                }
//                )
//                dropdownTableView.removeFromSuperview()
//                sender.isSelected = false
//            }
//            else{
//                sender.isSelected = true
//                dropdownTableView = UITableView()
//                
//                dropdownTableView = UITableView(frame: CGRect(x:sender.xOrigin, y:sender.maxYOrigin, width:sender.width, height:0), style: UITableViewStyle.plain)
//                dropdownTableView.delegate = self
//                dropdownTableView.dataSource = self
//                dropdownTableView.register(UITableViewCell.self,forCellReuseIdentifier: "cell")
//                dropdownTableView.separatorStyle = UITableViewCellSeparatorStyle.none
//                
//                sender.superview!.addSubview(dropdownTableView)
//                dropdownTableView.reloadData()
//                UIView.animate(
//                    withDuration: 0.5,
//                    delay: 0,
//                    options: .curveEaseInOut,
//                    animations: {
//                        self.dropdownTableView.frame.size.height = -130
//                }
//                )
//            }
            
            
            
            self.createDropdownview()
        }
    }
    
    
    func prioritydropDownView(sender:UIButton){
        
            if(sender.isSelected){
                UIView.animate(
                    withDuration: 0.5,
                    delay: 0,
                    options: .curveEaseInOut,
                    animations: {
                        self.prioritydropdownTableView.frame.size.height = 0
                }
                )
                prioritydropdownTableView.removeFromSuperview()
                sender.isSelected = false
            }
            else{
                sender.isSelected = true
                prioritydropdownTableView = UITableView()
                
                prioritydropdownTableView = UITableView(frame: CGRect(x:sender.xOrigin, y:sender.maxYOrigin, width:sender.width, height:0), style: UITableViewStyle.plain)
                prioritydropdownTableView.delegate = self
                prioritydropdownTableView.dataSource = self
                prioritydropdownTableView.register(UITableViewCell.self,forCellReuseIdentifier: "cell")
                prioritydropdownTableView.separatorStyle = UITableViewCellSeparatorStyle.none
                
                sender.superview!.addSubview(prioritydropdownTableView)
                prioritydropdownTableView.reloadData()
                UIView.animate(
                    withDuration: 0.5,
                    delay: 0,
                    options: .curveEaseInOut,
                    animations: {
                        self.prioritydropdownTableView.frame.size.height = -130
                }
                )
            }
        
        
    }
    
    func uploadImageAndData(){
        
        
        let alert = UIAlertController(title: title, message: "Create case with your location \(globalAddress) \(globalLat,globalLong).", preferredStyle: UIAlertControllerStyle.alert);
        let okAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
            self.createcase()
        }
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.cancel, handler: nil))
        
        alert.addAction(okAction)
        self.present(alert, animated: true, completion: nil)
        
       
        //parameters
    }
    
    
    func createcase(){
        if (descriptionField.text?.isEmpty)!{
            showAlertMessage(titleStr: "ALert", messageStr: "Please enter description")
        }else{
            
            
            var parameters: [String:Any] = [String:Any]()
            
            //Pickedimage.resizeWith(percentage: 0.1)
            //let myThumb2 = Pickedimage.resizeWith(width: 72.0)
            
            if imageData == nil{
                self.strBase64 = ""
            }else
            {
                self.strBase64 = (imageData?.base64EncodedString(options: .lineLength64Characters))!
            }
            
            
            
            let casetypeArray = NSMutableArray()
            
            let casetypeDict = NSMutableDictionary()
            
            
            for jj in  self.AttributesArray{
                let customdict = jj as! NSDictionary
                for sbViews in attributeView.subviews{
                    if let textFid:UITextField = sbViews as? UITextField{
                        if(textFid.tag == customdict["id"] as! NSInteger){
                            if(textFid.text != ""){
                                casetypeDict.setValue(textFid.text, forKey: "attributeValue")
                                casetypeDict.setValue(customdict, forKey: "caseTypeAttribute")
                                casetypeArray.add(casetypeDict)
                                
                            }
                            else{
                                showAlertMessage(titleStr: "Alert", messageStr: textFid.placeholder!)
                                return
                            }
                        }
                    }
                }
                
            }
            
            //
            //            self.AttributesArray.enumerateObjects({ (element, i, Bool) in
            //
            //                casetypeField.tag = (self.AttributesArray.object(at: i) as AnyObject).value(forKey: "id") as! NSInteger
            //
            ////                let cell:  UITableViewCell = self.tableView.cellForRow(at: IndexPath(row: i, section: 0))!
            ////                let textfield  = cell.viewWithTag(i) as! UITextField
            //
            //                casetypeDict.setValue(casetypeField.text, forKey: "attributeValue")
            //                casetypeDict.setValue(self.AttributesArray.object(at: i), forKey: "caseTypeAttribute")
            //
            //                casetypeArray.add(casetypeDict)
            //
            //            })
            
            
            
            let description :String = descriptionField.text!
            let lat  = UserDefaults.standard.value(forKey: "lat") as! NSNumber
            let long  = UserDefaults.standard.value(forKey: "long") as! NSNumber
            
            
            var imagedict : [String : Any]
            
            if audio == "audio"
            {
                imagedict = ["audio" : audioBase64String]
            }else
            {
                imagedict = ["image" : strBase64]
            }
            
            let imagearray = NSMutableArray()
            imagearray.add(imagedict)
            
            let priority : String = (proirityDropdownBtn.titleLabel?.text)!
            
            
            let dictAtt = NSMutableDictionary()
            
            dictAtt.setValue(attributedDict.value(forKey: "userId"), forKey: "userId")
            dictAtt.setValue(attributedDict.value(forKey: "updateDate"), forKey: "updateDate")
            dictAtt.setValue(attributedDict.value(forKey: "createdDate"), forKey: "createdDate")
            dictAtt.setValue(attributedDict.value(forKey: "id"), forKey: "id")
            dictAtt.setValue(attributedDict.value(forKey: "caseTypeAttribute"), forKey: "caseTypeAttribute")
            dictAtt.setValue(attributedDict.value(forKey: "description"), forKey: "description")
            dictAtt.setValue(attributedDict.value(forKey: "name"), forKey: "name")
            dictAtt.setValue(attributedDict.value(forKey: "user"), forKey: "user")
            
            if imageData == nil{
                parameters = [
                    "description": description,
                    "address": address,
                    "caseType" : dictAtt,
                    "caseTypeAttributeValues" : casetypeArray,
                    "pinLat":lat,
                    "pinLong":long,
                    "priority":priority
                ]
                
            }else
            {
                parameters = [
                    "description": description,
                    "address": address,
                    "caseImages" : imagearray,
                    "caseType" : dictAtt,
                    "caseTypeAttributeValues" : casetypeArray,
                    "pinLat":lat,
                    "pinLong":long,
                    "priority":priority
                ]
                
            }
            parameters["escalated"] = "\(isclicked)"
            
            
            
            do {
                let jsonData = try JSONSerialization.data(withJSONObject: parameters, options: [])
                if let jsonString = String(data: jsonData, encoding: String.Encoding.utf8) {
                    
                    print(jsonString)
                    
                    let urlstring = BaseUrl + KGetmycases
                    
                    let userdata = fetchuserdata()
                    
                    let tokenstring:String! = userdata.userid
                    
                    
                    let headers: HTTPHeaders = [
                        "Authorization": "Bearer \(tokenstring!)",
                        "Content-Type": "application/json",
                        ]
                    
                    showprogress(view: self.view)
                    
                    
                    Alamofire.request(urlstring, method: .post, parameters: self.convertToDictionary(text: jsonString), encoding: JSONEncoding.default, headers: headers).responseJSON { response in
                        if(response.result.isFailure){
                            print(response.result);
                            hideprogress()
                        }else{
                            if(response.response?.statusCode==201)
                            {
                                hideprogress()
                                print(response.result.value!)
                                
                                //showSuccessprogress(title: "Case created successfully", view: self.view)
                                self.navigationController!.popViewController(animated: true)
                                showAlertMessage(titleStr: "", messageStr: "Case created successfully")
                            }else if(response.response?.statusCode==401)
                            {
                                hideprogress()
                                print(response.result.value!)
                            }
                            else
                            {
                                print(response.result.value!)
                                hideprogress()
                            }
                        }
                    }
                    
                    
                    
                }
            } catch {
                print(error)
                showAlertMessage(titleStr: "Error", messageStr: error as! String)
                return
            }
            
            
        }
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    
    func displayLocationInfo(placemark: CLPlacemark?)
    {
        if let containsPlacemark = placemark
        {
            //address = ABCreateStringWithAddressDictionary(placemark.addressDictionary!, false).componentsSeparatedByString("\n").joinWithSeparator(", ")

            
            //stop updating location to save battery life
            // locationManager.stopUpdatingLocation()
            let subThoroughfare = (containsPlacemark.subThoroughfare != nil) ? containsPlacemark.subThoroughfare : ""
            let subLocality = (containsPlacemark.subLocality != nil) ? containsPlacemark.subLocality : ""
            let locality = (containsPlacemark.locality != nil) ? containsPlacemark.locality : ""
            let administrativeArea = (containsPlacemark.administrativeArea != nil) ? containsPlacemark.administrativeArea : ""
            let country = (containsPlacemark.country != nil) ? containsPlacemark.country : ""
            
            address  = "\(subThoroughfare!), \(subLocality!), \(locality!) , \(administrativeArea!), \(country!) "
            print(address)
            
        }
    }
    
    
    //TODO: UIImagepicker Delegate Methodes
    
    private func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
        if (info[UIImagePickerControllerOriginalImage] as? UIImage) != nil {
            
            
            Pickedimage = info[UIImagePickerControllerOriginalImage] as! UIImage
        }
        
    }

    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        if (info[UIImagePickerControllerOriginalImage] as? UIImage) != nil {
            
            Pickedimage = info[UIImagePickerControllerOriginalImage] as! UIImage
            
            //let myThumb1 = commonFunctions().ResizeImage(image: Pickedimage, targetSize: CGSize(width: 70.0, height: 70.0))
            
            let myThumb1 = resizeImage(image: Pickedimage, newWidth: 50)
            imageData = UIImagePNGRepresentation(myThumb1)! as NSData
           // imageData = Pickedimage.jpeg(.lowest) as NSData?
            strBase64 = (imageData?.base64EncodedString(options: .lineLength64Characters))!
            
            imageuploadBtn.setImage(myThumb1, for: UIControlState.normal)
            dismiss(animated: true, completion: nil)
        }
        
        
    }
    
    func getAssetThumbnail(asset: PHAsset) -> UIImage {
        let manager = PHImageManager.default()
        let option = PHImageRequestOptions()
        var thumbnail = UIImage()
        option.isSynchronous = true
        manager.requestImage(for: asset, targetSize: CGSize(width: 100, height: 100), contentMode: .aspectFit, options: option, resultHandler: {(result, info)->Void in
            thumbnail = result!
        })
        return thumbnail
    }
    
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
    }
    
    
    
    //TODO: Tableview Delegate Methodes
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(tableView == dropdownTableView){
            if (searchtext.text?.characters.count)!>2 {
                return filteredUserData.count
            }
            return self.arrayCases.count
        }else if(tableView == prioritydropdownTableView){
            return arrayPriority.count
        }
        return 0
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 30.0
    }
//    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
//        if(tableView == dropdownTableView){
//            let lastElement = self.arrayCases.count - 1
//            if indexPath.row == lastElement {
//                 self.pagebasedData()
//            }
//        }
//        
//    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(tableView == dropdownTableView){
            let cell = UITableViewCell(style: .value1, reuseIdentifier: "dropdowncell")
            cell.layoutMargins = UIEdgeInsets.zero
            
            if (searchtext.text?.characters.count)!>2{
                let k = filteredUserData[indexPath.row] as! NSDictionary
                cell.textLabel?.textColor = UIColor.black
                cell.textLabel?.text = "\(k.value(forKey: "name") as! String)"
            }else
            {
                let k = self.arrayCases[indexPath.row] as! NSDictionary
                cell.textLabel?.textColor = UIColor.black
                cell.textLabel?.text = "\(k.value(forKey: "name") as! String)"
            }
            return cell
          
        }else
        {
            let cell = UITableViewCell(style: .value1, reuseIdentifier: "prioritycell")
            cell.layoutMargins = UIEdgeInsets.zero
            cell.backgroundColor = UIColor.groupTableViewBackground
            
            cell.textLabel?.textColor = UIColor.black
            cell.textLabel?.text = self.arrayPriority[indexPath.row]
            return cell
        }
//        else{
//            let cell = UITableViewCell(style: .value1, reuseIdentifier: "cell")
//            cell.layoutMargins = UIEdgeInsets.zero
//            
//            let k = self.AttributesArray[indexPath.row] as! NSDictionary
//            
//            let idLabel = UILabel()
//            idLabel.frame = CGRect(x: 10, y: 0, width: ScreenSize.SCREEN_WIDTH-20, height: 20)
//            idLabel.textColor = LOGIN_BG_COLOR
//            idLabel.text = "\(k.value(forKey: "name") as! String)"
//            idLabel.font = UIFont(name: fontName, size: 13)
//            idLabel.textAlignment = NSTextAlignment.left
//            cell.addSubview(idLabel)
//            
//            casetypeField = UITextField()
//            casetypeField.borderStyle = .bezel
//            casetypeField.frame = CGRect(x: 10, y: 30, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
//            casetypeField.keyboardType = UIKeyboardType.alphabet
//            //let padding = UIEdgeInsets(top: 0, left: 5, bottom: 0, right: 5);
//            //casetypeField.layer.borderWidth = 1.0
//            casetypeField.tag = k.value(forKey: "id") as! NSInteger
//            //casetypeField.layer.borderColor = UIColor.black.cgColor
//            casetypeField.isUserInteractionEnabled = true
//            casetypeField.placeholder = "\(k.value(forKey: "name") as! String)"
//            casetypeField.textColor = UIColor.black
//            casetypeField.font = UIFont(name: "Futura-Medium", size: 14)
//            casetypeField.textAlignment = NSTextAlignment.left
//            casetypeField.delegate = self
//            cell.addSubview(casetypeField)
//            return cell
//            
//        }
        
        
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(tableView == dropdownTableView){
            if(searchtext.text?.characters.count)!>2{
                let k = filteredUserData[indexPath.row] as! NSDictionary
                dropdownBtn.setTitle("\(k.value(forKey: "name") as! String)", for: UIControlState.normal)
                
                let ks = filteredUserData[indexPath.row] as! NSDictionary
                
                attributedDict = ks
                
                self.AttributesArray = ks.value(forKey : "caseTypeAttribute") as! NSArray
                
                self.createAttributesview()
                
                //            for i in 0..< self.AttributesArray{
                //
                //                self.audio = self.AttributesArray.value(forKey: "name") as! String
                //
                //                if self.audio == "audio"
                //                {
                //                    self.recordButton.isHidden = false
                //                }else
                //                {
                //                    self.recordButton.isHidden = true
                //                }
                //
                //
                //            }
                
                
                self.tableView.reloadData()
                dropview.removeFromSuperview()
                
                //            UIView.animate(
                //                withDuration: 0.5,
                //                delay: 0,
                //                options: .curveEaseInOut,
                //                animations: {
                //                    self.dropdownTableView.frame.size.height = 0
                //            }
                //            )
                //            dropdownTableView.removeFromSuperview()
                dropdownBtn.isSelected = false
                selected = true
            }else
            {
                let k = self.arrayCases[indexPath.row] as! NSDictionary
                dropdownBtn.setTitle("\(k.value(forKey: "name") as! String)", for: UIControlState.normal)
                
                let ks = self.arrayCases[indexPath.row] as! NSDictionary
                
                attributedDict = ks
                
                self.AttributesArray = ks.value(forKey : "caseTypeAttribute") as! NSArray
                
                self.createAttributesview()
                
                //            for i in 0..< self.AttributesArray{
                //
                //                self.audio = self.AttributesArray.value(forKey: "name") as! String
                //
                //                if self.audio == "audio"
                //                {
                //                    self.recordButton.isHidden = false
                //                }else
                //                {
                //                    self.recordButton.isHidden = true
                //                }
                //
                //
                //            }
                
                
                self.tableView.reloadData()
                dropview.removeFromSuperview()
                
                //            UIView.animate(
                //                withDuration: 0.5,
                //                delay: 0,
                //                options: .curveEaseInOut,
                //                animations: {
                //                    self.dropdownTableView.frame.size.height = 0
                //            }
                //            )
                //            dropdownTableView.removeFromSuperview()
                dropdownBtn.isSelected = false
                selected = true
            }
            
        }else if(tableView == prioritydropdownTableView)
        {
            
            proirityDropdownBtn.setTitle(arrayPriority[indexPath.row], for: UIControlState.normal)
            
            UIView.animate(
                withDuration: 0.5,
                delay: 0,
                options: .curveEaseInOut,
                animations: {
                    self.prioritydropdownTableView.frame.size.height = 0
            }
            )
            prioritydropdownTableView.removeFromSuperview()
            proirityDropdownBtn.isSelected = false
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        return textField.resignFirstResponder()
    }
    
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
            
        if scrollView == dropdownTableView{
            let currentOffset = scrollView.contentOffset.y
            let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
                
                // Change 10.0 to adjust the distance from bottom
            if maximumOffset - currentOffset <= 10.0 {
                self.pagebasedData()
                    
            }
        }else{
            return
        }
            // UITableView only moves in one direction, y axis
    }
    
    
    func pagebasedData()
    {
        
        let utliti = Utilities()
        
        
        let parameters:[String: Any] = ["page" : self.nextAPICallIndex, "size" : "10", "sort" : "id,asc"]
        
        showprogress(view: self.view)
        utliti.getdatafromserverwithstring(input: kGetCasetypes,parameters: parameters) { (true,result) in
            if (true){
                hideprogress()
                let newData = result as! NSArray
                let array_OldCount : Int =  self.arrayCases.count
                
                for i in 0 ..< newData.count {
                    
                    if(!self.arrayCases.contains(newData.object(at: i)))
                    {
                        self.arrayCases.add(newData.object(at: i))
                    }
                }
                if (array_OldCount != self.arrayCases.count){
                    
                    let indexPath = IndexPath(row: array_OldCount, section: 0)
                    print(indexPath)
                }
                
            }
            
        }
        self.nextAPICallIndex += 1
        DispatchQueue.main.async(execute: {
            // UI Updates
            self.dropdownTableView.reloadData()
        })
        
        
    }
    
    func createAttributesview()
    {
        
        var yPos : CGFloat = 0
        attributeView.subviews.forEach({$0.removeFromSuperview()})
        attributeView.frame = CGRect(x: 0, y: dropdownBtn.maxYOrigin + 10, width: ScreenSize.SCREEN_WIDTH, height: ScreenSize.SCREEN_HEIGHT-400)
        scroll.addSubview(attributeView)
        
        for i in 0 ..< self.AttributesArray.count{
            let data = (self.AttributesArray[i]) as! NSDictionary
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
            attField.tag = (self.AttributesArray[i] as AnyObject).value(forKey: "id") as! NSInteger
            attField.font = UIFont(name: "Futura-Medium", size: 15)
            attField.textAlignment = NSTextAlignment.left
            attField.setSignInBottomBorder()
            attributeView.addSubview(attField)
            yPos = yPos + 70
            self.attributeView.frame.size.height = attField.maxYOrigin + 5
        }
        scroll.contentSize.height = attributeView.maxYOrigin + 150
    }
    
    
    
}
