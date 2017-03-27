//
//  ServiceDetailsVC.swift
//  TrakEyeApp
//
//  Created by Apple on 28/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//
import Foundation
import UIKit
import Alamofire
import CoreTelephony
import TPKeyboardAvoiding

class servicesDetailsDict {
    var createdDate = UILabel()
    var modifiedDate = UILabel()
    var description = UITextView()
    var serviceDate = UILabel()
    var userId = UILabel()
    var caseId = UILabel()
    var serviceType = UILabel()
    var statusField = UITextField()
    var notesField = UITextView()
    var imagesScrollView = UIScrollView()
    var uploadedImages = UIButton()
    var addImage = UIButton()
    
    static var INPROGRESS = "INPROGRESS"
    static var PENDING = "PENDING"
    static var CLOSED = "CLOSED"
    static var CANCELLED = "CANCELLED"
}

class rightMenu {
    var rightView = TPKeyboardAvoidingScrollView()
    var uploadImageButton = UIButton()
    var notesEdit = UITextView()
    var statusButton = UIButton()
    var backView = UIView()
    var subScroll = UIScrollView()
}



class ServiceDetailsVC: UIViewController, UITextViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate	 {
    var servicesClass = servicesDetailsDict()
    var parentView = UIScrollView()
    var idValue = NSInteger()
    var arrayCases = NSDictionary()
    var imagePicker = UIImagePickerController()
    let rightMenu1 = rightMenu()
    var rightButton = UIBarButtonItem()
    var saveStatusRadio = ""
    var saveUploadImg = ""
    var Pickedimage:UIImage?
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        self.edgesForExtendedLayout = []
        let image = UIImage(named: "edit")
         rightButton = UIBarButtonItem(image: image, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.rightMenuClicked))
        self.navigationItem.rightBarButtonItem = rightButton
        self.navigationItem.rightBarButtonItem?.isEnabled = false
        
        parentView = UIScrollView(frame: CGRect(x: 0, y: 0, width: view.width, height: view.height))
        view.addSubview(parentView)
        self.createView()
        self.callService()
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(ServiceDetailsVC.dismissKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(false)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func createView(){
        
        let cdTitle = UILabel()
        cdTitle.frame = CGRect(x: 10, y: 0, width: view.width - 20, height: 25)
        cdTitle.text = "Created Date"
        parentView.addSubview(cdTitle)
        
        servicesClass.createdDate = UILabel()
        servicesClass.createdDate.frame = CGRect(x: 10, y: cdTitle.frame.origin.y + cdTitle.height, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.createdDate)
        self.designLabels123(firstLab: cdTitle, secondLab: servicesClass.createdDate)
        
        let mdTitle = UILabel()
        mdTitle.frame = CGRect(x: 10, y: servicesClass.createdDate.maxYOrigin + 2, width: view.width - 20, height: 25)
        mdTitle.text = "Modified Date"
        parentView.addSubview(mdTitle)
        
        servicesClass.modifiedDate = UILabel()
        servicesClass.modifiedDate.frame = CGRect(x: 10, y: mdTitle.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.modifiedDate)
        self.designLabels123(firstLab: mdTitle, secondLab: servicesClass.modifiedDate)
        
        let dTitle = UILabel()
        dTitle.frame = CGRect(x: 10, y: servicesClass.modifiedDate.maxYOrigin + 2, width: view.width - 20, height: 25)
        dTitle.text = "Description"
        parentView.addSubview(dTitle)
        
        servicesClass.description = UITextView()
        servicesClass.description.frame = CGRect(x: 10, y: dTitle.maxYOrigin, width: view.width - 20, height: 50)
        parentView.addSubview(servicesClass.description)
        self.designtextV123(firstLab: dTitle, secondLab: servicesClass.description)
        
        
        let sdTitle = UILabel()
        sdTitle.frame = CGRect(x: 10, y: servicesClass.description.maxYOrigin + 2, width: view.width - 20, height: 25)
        sdTitle.text = "Service Date"
        parentView.addSubview(sdTitle)
        
        servicesClass.serviceDate = UILabel()
        servicesClass.serviceDate.frame = CGRect(x: 10, y: sdTitle.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.serviceDate)
        self.designLabels123(firstLab: sdTitle, secondLab: servicesClass.serviceDate)

        let userTitle = UILabel()
        userTitle.frame = CGRect(x: 10, y: servicesClass.serviceDate.maxYOrigin + 2, width: view.width - 20, height: 25)
        userTitle.text = "User"
        parentView.addSubview(userTitle)
        
        servicesClass.userId = UILabel()
        servicesClass.userId.frame = CGRect(x: 10, y: userTitle.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.userId)
        self.designLabels123(firstLab: userTitle, secondLab: servicesClass.userId)
        
        let caseTitle = UILabel()
        caseTitle.frame = CGRect(x: 10, y: servicesClass.userId.maxYOrigin + 2, width: view.width - 20, height: 25)
        caseTitle.text = "Case"
        parentView.addSubview(caseTitle)
        
        servicesClass.caseId = UILabel()
        servicesClass.caseId.frame = CGRect(x: 10, y: caseTitle.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.caseId)
        self.designLabels123(firstLab: caseTitle, secondLab: servicesClass.caseId)
        
        let servicet = UILabel()
        servicet.frame = CGRect(x: 10, y: servicesClass.caseId.maxYOrigin + 2, width: view.width - 20, height: 25)
        servicet.text = "Service Type"
        parentView.addSubview(servicet)
        
        servicesClass.serviceType = UILabel()
        servicesClass.serviceType.frame = CGRect(x: 10, y: servicet.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.serviceType)
        self.designLabels123(firstLab: servicet, secondLab: servicesClass.serviceType)
        
        let status = UILabel()
        status.frame = CGRect(x: 10, y: servicesClass.serviceType.maxYOrigin + 2, width: view.width - 20, height: 25)
        status.text = "Status"
        parentView.addSubview(status)
        
        servicesClass.statusField = UITextField()
        servicesClass.statusField.frame = CGRect(x: 10, y: status.maxYOrigin, width: view.width - 20, height: 25)
        parentView.addSubview(servicesClass.statusField)
        self.designtext123(firstLab: status, secondLab: servicesClass.statusField)
        
        let noteTitle = UILabel()
        noteTitle.frame = CGRect(x: 10, y: servicesClass.statusField.maxYOrigin + 2, width: view.width - 20, height: 25)
        noteTitle.text = "Notes"
        parentView.addSubview(noteTitle)
        
        servicesClass.notesField = UITextView()
        servicesClass.notesField.frame = CGRect(x: 10, y: noteTitle.maxYOrigin, width: view.width - 20, height: 50)
        parentView.addSubview(servicesClass.notesField)
        self.designtextV123(firstLab: noteTitle, secondLab: servicesClass.notesField)
        
        let imageTitle = UILabel()
        imageTitle.frame = CGRect(x: 10, y: servicesClass.notesField.maxYOrigin + 2, width: view.width - 20, height: 25)
        imageTitle.text = "Images"
        imageTitle.font = UIFont(name: fontName, size: 14)
        imageTitle.textColor = UIColor.cyan
        parentView.addSubview(imageTitle)
        
        servicesClass.imagesScrollView = UIScrollView()
        servicesClass.imagesScrollView.frame = CGRect(x: 10, y: imageTitle.maxYOrigin + 5, width: view.width - 20, height: 80)
        parentView.addSubview(servicesClass.imagesScrollView)
        
        parentView.contentSize.height = servicesClass.imagesScrollView.maxYOrigin + 100
    }
    
    //TODO: GET DATA FROM API
    
    func callService(){
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            let urlString = "\(kGetServices)/\(idValue)"
            print(urlString)
            let utliti = Utilities()
            let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
            showprogress(view: self.view)
            self.navigationItem.rightBarButtonItem?.isEnabled = false
            utliti.getDICTIONARYfromserverwithstring(input: urlString,parameters: parameters) { (true,result) in
                   self.arrayCases  = result as! NSDictionary
                    print(self.arrayCases)
                    self.navigationItem.rightBarButtonItem?.isEnabled = true
                    self.updateDetailswithValues()
                    hideprogress()
            }
        }
    }
    
    func updateDetailswithValues()
    {
        let responsedata = self.arrayCases
        let childDict = responsedata.value(forKey: "trCase") as? NSDictionary
        let serviceChildDict = responsedata.value(forKey: "serviceType") as? NSDictionary
        
        let createdDate = responsedata.value(forKey: "createdDate") as! Double
        servicesClass.createdDate.text = createdDate.toHour//commonFunctions().millsecToDate(milli: createdDate)
        
        let modifiedDate = responsedata.value(forKey: "modifiedDate") as! Double
        servicesClass.modifiedDate.text = modifiedDate.toHour//commonFunctions().millsecToDate(milli: modifiedDate)
        
        servicesClass.description.text = responsedata.object(forKey: "description") as! String
        
        let serviceDate = responsedata.value(forKey: "serviceDate") as! Double
        servicesClass.serviceDate.text = serviceDate.toHour//commonFunctions().millsecToDate(milli: serviceDate)
        
        servicesClass.userId.text = childDict?.object(forKey: "assignedToUser") as? String
        
//        if responsedata.object(forKey: "user") as? NSInteger != nil{
//            let userID = String(responsedata.object(forKey: "user") as! NSInteger)
//            servicesClass.userId.text = userID
//        }else
//        {
//            servicesClass.userId.text = "0"
//        }
        
        if responsedata.object(forKey: "id") as? NSInteger != nil{
            let caseID = String(responsedata.object(forKey: "id") as! NSInteger)
            servicesClass.caseId.text = caseID
        }else
        {
            servicesClass.caseId.text = "0"
        }
        
        servicesClass.serviceType.text = serviceChildDict?.object(forKey: "name") as? String
        
        servicesClass.statusField.text = responsedata.object(forKey: "status") as? String
        
        servicesClass.notesField.text = responsedata.object(forKey: "notes") as? String

        servicesClass.imagesScrollView.subviews.forEach({ $0.removeFromSuperview() })
        
        if let serviceImages = responsedata.value(forKey: "serviceImages") as? NSArray{
            if(serviceImages.count > 0){
                var xpos:CGFloat = 10
                for i in 0..<serviceImages.count{
                    if let imgDict = serviceImages[i] as? NSDictionary{
                        if let imgBse64Str = imgDict["image"] as? String{
                            if(imgBse64Str != "")
                            {
                                let dataDecoded:NSData = NSData(base64Encoded: imgBse64Str, options: NSData.Base64DecodingOptions(rawValue: 0))!
                                let decodedimage:UIImage = UIImage(data: dataDecoded as Data)!
                                
                                let imgButton = UIImageView()
                                imgButton.frame = CGRect(x: xpos, y: 0, width: 60, height: 60)
                                imgButton.layer.cornerRadius = 5.0
                                //imgButton.layer.borderColor = LOGIN_BG_COLOR.cgColor
                                imgButton.clipsToBounds = true
                                imgButton.image = decodedimage
                                imgButton.contentMode = UIViewContentMode.scaleAspectFill
                                xpos = xpos + imgButton.width + 10
                                
                                servicesClass.imagesScrollView.addSubview(imgButton)
                                servicesClass.imagesScrollView.contentSize.width = imgButton.maxXOrigin + 20
                            }
                        }
                    }
                }
            }
        }
        
    }

    func rightMenuClicked(){
        saveUploadImg = ""
        Pickedimage = UIImage()
        rightButton.isEnabled = false
        rightMenu1.backView.subviews.forEach({ $0.removeFromSuperview()})
        rightMenu1.backView.removeFromSuperview()
        if(self.arrayCases.count > 0){
            rightMenu1.backView = UIView()
            rightMenu1.backView.frame = CGRect(x: 0, y: 0, width: view.width, height: view.height)
            rightMenu1.backView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
            view.addSubview(rightMenu1.backView)
            
            rightMenu1.rightView = TPKeyboardAvoidingScrollView()
            rightMenu1.rightView.frame = CGRect(x: view.width/2 - 50, y: 0, width: view.width/2 + 50, height: view.height)
            rightMenu1.rightView.backgroundColor = UIColor.white
            rightMenu1.backView.addSubview(rightMenu1.rightView)
            
            let statusLabel = UILabel()
            statusLabel.frame = CGRect(x: 5, y: 5, width: rightMenu1.rightView.width - 10, height: 20)
            statusLabel.text = "Status"
            statusLabel.font = UIFont(name: fontBold, size: 15)
            statusLabel.textColor = UIColor.black
            rightMenu1.rightView.addSubview(statusLabel)
            
            let radioView = UIView()
            radioView.frame = CGRect(x: 10, y: statusLabel.maxYOrigin, width:rightMenu1.rightView.width - 20, height: 120)
            rightMenu1.rightView.addSubview(radioView)
            let arr = ["INPROGRESS","PENDING","CLOSED","CANCELLED"]
            var yPos:CGFloat = 0
            
            for i in 0 ..< 4{
                let radioButton = UIButton()
                radioButton.frame = CGRect(x: 0, y: yPos, width: radioView.width, height: 30)
                radioButton.setImage(UIImage(named:"radio-select.png"), for: .selected)
                radioButton.setImage(UIImage(named:"radio-unselect.png"), for: .normal)
                radioButton.setTitle(arr[i], for: .normal)
                radioButton.titleLabel?.font = UIFont(name: fontName, size: 14)
                radioButton.titleLabel?.textAlignment = .left
                radioButton.setTitleColor(UIColor.black, for: .normal)
                radioView.addSubview(radioButton)
                radioButton.sizeToFit()
                radioButton.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 0)
                radioButton.addTarget(self, action: #selector(self.radioButtonClick(sender:)), for: .touchUpInside)
                
                yPos = yPos + radioButton.height + 3
                radioView.frame.size.height = radioButton.maxYOrigin
                if(arr[i] == servicesClass.statusField.text){
                    radioButton.isSelected = true
                }
            }
            
            saveStatusRadio = servicesClass.statusField.text!
            
            let notesLabel = UILabel()
            notesLabel.frame = CGRect(x: 5, y: radioView.maxYOrigin + 10, width: rightMenu1.rightView.width - 10, height: 20)
            notesLabel.text = "Notes"
            notesLabel.font = UIFont(name: fontBold, size: 15)
            notesLabel.textColor = UIColor.black
            rightMenu1.rightView.addSubview(notesLabel)
            
            
            rightMenu1.notesEdit = UITextView()
            rightMenu1.notesEdit.frame = CGRect(x: 10, y: notesLabel.maxYOrigin, width: rightMenu1.rightView.width - 20, height: 60)
            rightMenu1.notesEdit.textColor = UIColor.black
            rightMenu1.notesEdit.font = UIFont(name: fontName, size: 14)
            rightMenu1.notesEdit.layer.cornerRadius = 5
            rightMenu1.notesEdit.backgroundColor = UIColor.lightGray.withAlphaComponent(0.3)
            rightMenu1.rightView.addSubview(rightMenu1.notesEdit)
            rightMenu1.notesEdit.delegate = self
            rightMenu1.notesEdit.text = servicesClass.notesField.text
            
            let uploadimageField = UITextField()
            uploadimageField.frame = CGRect(x: 10, y: rightMenu1.notesEdit.maxYOrigin, width: rightMenu1.rightView.width - 20, height: 60)
            uploadimageField.isUserInteractionEnabled = false
            uploadimageField.placeholder = "Upload Image"
            uploadimageField.textColor = UIColor.black
            uploadimageField.font = UIFont(name: fontBold, size: 14)
            uploadimageField.textAlignment = NSTextAlignment.left
            uploadimageField.setSignInBottomBorder()
            rightMenu1.rightView.addSubview(uploadimageField)
            
            rightMenu1.uploadImageButton = UIButton()
            rightMenu1.uploadImageButton.frame = CGRect(x: uploadimageField.width-40, y: uploadimageField.yOrigin + 10, width: 40, height: 40)
            let photoupload = UIImage.init(named: "Camera")
            rightMenu1.uploadImageButton.setImage(photoupload, for: UIControlState.normal)
            rightMenu1.uploadImageButton.addTarget(self, action: #selector(self.imageuploaClicked), for: UIControlEvents.touchDown)
            rightMenu1.rightView.addSubview(rightMenu1.uploadImageButton)
            
            
            rightMenu1.subScroll = UIScrollView()
            rightMenu1.subScroll.subviews.forEach({$0.removeFromSuperview()})
            rightMenu1.subScroll.frame = CGRect(x: 0, y: uploadimageField.maxYOrigin + 10, width: rightMenu1.rightView.width, height: 160)
            rightMenu1.rightView.addSubview(rightMenu1.subScroll)
            
            
            
            if let serviceTypeAttributeValues = self.arrayCases.value(forKey: "serviceTypeAttributeValues") as? NSArray{
                let ypos:CGFloat = 0
                for jj in serviceTypeAttributeValues{
                    if let dataDic = jj as? NSDictionary{
                        let attributeValue = dataDic.value(forKey: "attributeValue") as? String
                        let id = dataDic.value(forKey: "id") as! NSInteger
                        let serviceTypeAttribute = dataDic.value(forKey: "serviceTypeAttribute") as? NSDictionary
                        let name = serviceTypeAttribute?.value(forKey: "name") as? String
                        
                        let serviceTypeAttributeNameLabel = UILabel()
                        serviceTypeAttributeNameLabel.frame = CGRect(x: 5, y: ypos, width: rightMenu1.subScroll.width - 10, height: 20)
                        serviceTypeAttributeNameLabel.text = name
                        serviceTypeAttributeNameLabel.font = UIFont(name: fontBold, size: 15)
                        serviceTypeAttributeNameLabel.textColor = UIColor.black
                        rightMenu1.subScroll.addSubview(serviceTypeAttributeNameLabel)
                        
                        let attributeValueField = UITextField()
                        attributeValueField.frame = CGRect(x: 10, y: serviceTypeAttributeNameLabel.maxYOrigin, width: rightMenu1.subScroll.width - 20, height: 25)
                        attributeValueField.placeholder = attributeValue
                        attributeValueField.textColor = UIColor.black
                        attributeValueField.font = UIFont(name: fontBold, size: 14)
                        attributeValueField.textAlignment = NSTextAlignment.left
                        attributeValueField.setSignInBottomBorder()
                        rightMenu1.subScroll.addSubview(attributeValueField)
                        attributeValueField.tag = id
                        rightMenu1.subScroll.contentSize.height = attributeValueField.maxYOrigin + 10
                        
                        
                        
                        print(attributeValue!)
                    }
                    
                }
                
//                for i in serviceTypeAttributeValues{
//                    if let dataDict = i as? NSDictionary{
//                        let attributeValue = dataDict.value(forKey: "attributeValue") as? String
//                    }
//                }
            }
            
            
            
            
            let cancelBtn = UIButton()
            cancelBtn.frame = CGRect(x: 20, y: rightMenu1.subScroll.maxYOrigin + 20, width: 70, height: 30)
            cancelBtn.backgroundColor = LOGIN_BG_COLOR
            cancelBtn.layer.cornerRadius = 5.0
            cancelBtn.titleLabel?.textColor = UIColor.black
            cancelBtn.setTitle("Cancel", for: UIControlState.normal)
            cancelBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
            cancelBtn.titleLabel?.textAlignment = NSTextAlignment.center
            cancelBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
            cancelBtn.addTarget(self, action: #selector(self.cancelClicked), for: UIControlEvents.touchDown)
            rightMenu1.rightView.addSubview(cancelBtn)
            
            let saveBtn = UIButton()
            saveBtn.frame = CGRect(x: cancelBtn.maxXOrigin + 20 , y: cancelBtn.yOrigin, width: 70, height: 30)
            saveBtn.backgroundColor = LOGIN_BG_COLOR
            saveBtn.layer.cornerRadius = 5.0
            saveBtn.titleLabel?.textColor = UIColor.white
            saveBtn.setTitle("Save", for: UIControlState.normal)
            saveBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
            saveBtn.titleLabel?.textAlignment = NSTextAlignment.center
            saveBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
            saveBtn.addTarget(self, action: #selector(self.savelClicked), for: UIControlEvents.touchDown)
            rightMenu1.rightView.addSubview(saveBtn)
            
        }
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if text == "\n"  // Recognizes enter key in keyboard
        {
            textView.resignFirstResponder()
            return false
        }
        return true
    }
    
    func cancelClicked(){
        rightButton.isEnabled = true
        rightMenu1.backView.subviews.forEach({ $0.removeFromSuperview()})
        rightMenu1.backView.removeFromSuperview()
    }
    func savelClicked(){
        guard rightMenu1.notesEdit.text != "" else{
            showAlertMessage(titleStr: "Alert", messageStr: "Please Enter Notes")
            return
        }
        
        if (rightMenu1.uploadImageButton.currentImage != UIImage(named: "Camera")){
            let myThumb1 = commonFunctions().ResizeImage(image: Pickedimage!, targetSize: CGSize(width: 50.0, height: 50.0))
            let imageData:NSData = UIImagePNGRepresentation(myThumb1)! as NSData
            saveUploadImg = imageData.base64EncodedString(options: .lineLength64Characters)
        }
        else{
            saveUploadImg = ""
        }
        
        
        showprogress(view: self.view)
        
        let description :String = rightMenu1.notesEdit.text!
        let imagedict = [["image":saveUploadImg]]

        let newDict: NSMutableDictionary = NSMutableDictionary(dictionary: self.arrayCases)

        newDict["notes"] = description
        newDict["status"] = saveStatusRadio
        newDict["serviceImages"] = imagedict
        
        let serviceTypeAttributeValues = self.arrayCases.value(forKey: "serviceTypeAttributeValues") as! NSArray
        let valueDict: NSMutableArray = NSMutableArray()//NSMutableDictionary(dictionary: serviceTypeAttributeValues)
        
            for i in serviceTypeAttributeValues{
               let attributeDict = NSMutableDictionary(dictionary: i as! NSDictionary)
                for sbViews in rightMenu1.subScroll.subviews{
                    if let kk:UITextField = sbViews as? UITextField{
                        if(kk.tag == attributeDict["id"] as! NSInteger){
                            if(kk.text != ""){
                                attributeDict["attributeValue"] = kk.text!
                            }
                        }
                    }
                }
                valueDict.add(attributeDict)
            }
        newDict["serviceTypeAttributeValues"] = valueDict
        print(newDict)
        
        let resp:NSDictionary = newDict as NSDictionary
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: newDict, options: [])
            if let jsonString = String(data: jsonData, encoding: String.Encoding.utf8) {
                print(jsonString)
                let urlstring = BaseUrl + kGetServices
                let userdata = fetchuserdata()
                let tokenstring:String! = userdata.userid
                showprogress(view: self.view)
                if UserDefaults.standard.bool(forKey: "networkStatus") {
                    Alamofire.request("\(urlstring)", method: .put, parameters: (resp as! Parameters), encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenstring!)", "Content-Type": "application/json"]).responseJSON { response in
                        if(response.result.isFailure){
                            hideprogress()
                            print("no data!");
                            showerrorprogress(title: kInterenetAlertMessage, view: self.view)
                        }else{
                            if(response.response?.statusCode==200)
                            {
                                self.callService()
                                self.rightButton.isEnabled = true
                                self.rightMenu1.backView.subviews.forEach({ $0.removeFromSuperview()})
                                self.rightMenu1.backView.removeFromSuperview()
                               //hideprogress()
                            }else
                            {
                                //hideprogress()
                                self.callService()
                                self.rightButton.isEnabled = true
                                self.rightMenu1.backView.subviews.forEach({ $0.removeFromSuperview()})
                                self.rightMenu1.backView.removeFromSuperview()
                            }
                        }
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
    
    
    func radioButtonClick(sender:UIButton){
        for i in (sender.superview?.subviews)!{
            (i as! UIButton).isSelected = false
        }
        sender.isSelected = true
        saveStatusRadio = sender.currentTitle!
    }
    
    func imageuploaClicked()
    {
        imagePicker.delegate = self
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }

    //TODO: UIImagepicker Delegate Methodes
    
    private func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
        if (info[UIImagePickerControllerOriginalImage] as? UIImage) != nil {
            Pickedimage = info[UIImagePickerControllerOriginalImage] as? UIImage
            rightMenu1.uploadImageButton.setImage(Pickedimage, for: UIControlState.normal)
        }
    
    }
    
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        if (info[UIImagePickerControllerOriginalImage] as? UIImage) != nil {
            Pickedimage = info[UIImagePickerControllerOriginalImage] as? UIImage
            rightMenu1.uploadImageButton.setImage(Pickedimage, for: UIControlState.normal)
            dismiss(animated: true, completion: nil)
        }
        
    }
    
    func designLabels123(firstLab:UILabel,secondLab:UILabel){
        firstLab.textColor = LOGIN_BG_COLOR
        firstLab.font = UIFont(name: fontName, size: 14)
        secondLab.textColor = UIColor.black
        secondLab.font = UIFont(name: fontName, size: 14)
        let bord = UILabel()
        bord.frame = CGRect(x: secondLab.xOrigin, y: secondLab.maxYOrigin + 1, width: secondLab.width, height: 1)
        bord.backgroundColor = UIColor.lightGray
        secondLab.superview!.addSubview(bord)
    }
    func designtextV123(firstLab:UILabel,secondLab:UITextView){
        firstLab.textColor = LOGIN_BG_COLOR
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
        firstLab.textColor = LOGIN_BG_COLOR
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







