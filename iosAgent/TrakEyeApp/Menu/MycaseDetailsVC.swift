//
//  MycaseDetailsVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 23/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

class MycaseDetailsVCDict {
//    var createdDate = UILabel()
//    var modifiedDate = UILabel()
//    var description = UITextView()
//    var serviceDate = UILabel()
//    var userId = UILabel()
//    var caseId = UILabel()
//    var serviceType = UILabel()
//    var statusField = UITextField()
//    var notesField = UITextView()
    var imagesScrollView = UIScrollView()
    var uploadedImages = UIButton()
    var addImage = UIButton()
    
    var createDateField = UITextField()
    var descriptionField = UITextField()
    var satusField = UITextField()
    var subjectField = UITextField()
    var alertTypeField = UITextField()
    var UpdatedDateField = UITextField()
    var toUserField = UITextField()
    var caseField = UITextField()
    var updatedField = UITextField()
    
    
    static var INPROGRESS = "INPROGRESS"
    static var PENDING = "PENDING"
    static var CLOSED = "CLOSED"
    static var CANCELLED = "CANCELLED"
}
import UIKit

class MycaseDetailsVC: UIViewController {

    var mycasedettail = MycaseDetailsVCDict()
    var parentView = UIScrollView()
    var arrayCases =  NSDictionary()
    var idValue = NSInteger()
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.

        
        let rightButtonimage = UIImage(named: "edit")
        let rightButton = UIBarButtonItem(image: rightButtonimage, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.rightMenuClicked))
        self.navigationItem.rightBarButtonItem = rightButton
        
        parentView = UIScrollView(frame: CGRect(x: 0, y: 0, width: view.width, height: view.height))
        
        self.view.addSubview(parentView)
        
        self.createView()
        
       
       
    }

    override func viewDidAppear(_ animated: Bool) {
         self.callService()
    }
    
    //TODO: GET DATA FROM API
    
    func callService(){
        if UserDefaults.standard.bool(forKey: "networkStatus") {
            let urlString = "\(KGetmycases)/\(idValue)"
            print(urlString)
            
            let utliti = Utilities()
            let parameters:[String: Any] = ["page" : "0", "size" : "20", "sort" : "id,asc"]
            showprogress(view: self.view)
            self.navigationItem.rightBarButtonItem?.isEnabled = false
            utliti.getDICTIONARYfromserverwithstring(input: urlString,parameters: parameters) { (true,result) in
                
                
                    self.arrayCases  = result as! NSDictionary
                    print(self.arrayCases)
                    
                    self.navigationItem.rightBarButtonItem?.isEnabled = true
                    
                    self.mycasedettail.descriptionField.text = self.arrayCases.value(forKey: "description") as! String?
                    
                    let milisecond = self.arrayCases.value(forKey: "createDate") as! Double?
                    
                    
                    self.mycasedettail.createDateField.text = milisecond?.toHour//commonFunctions().millsecToDate(milli: milisecond!)
                    
                    let updatemilisecond = self.arrayCases.value(forKey: "updateDate") as! Double?
                    
                    self.mycasedettail.UpdatedDateField.text = updatemilisecond?.toHour//commonFunctions().millsecToDate(milli: updatemilisecond!)
                    
                    if let xz = self.arrayCases.value(forKey: "escalated") as? NSNumber{
                        let x = Bool(xz)
                        if x == true{
                            self.mycasedettail.alertTypeField.text = "YES"
                        }else
                        {
                            self.mycasedettail.alertTypeField.text = "NO"
                        }
                    }else
                    {
                        self.mycasedettail.alertTypeField.text = "NO"
                    }
                    self.mycasedettail.subjectField.text = self.arrayCases.value(forKey: "address") as! String?
                    
                    self.mycasedettail.satusField.text = self.arrayCases.value(forKey: "status") as! String?
                    self.mycasedettail.toUserField.text = self.arrayCases.value(forKey: "reportedByUser") as! String?
                    
                    self.mycasedettail.caseField.text = self.arrayCases.value(forKey: "assignedToUser") as! String?
                    self.mycasedettail.updatedField.text = self.arrayCases.value(forKey: "updatedByUser") as! String?
                    
                    self.mycasedettail.imagesScrollView.subviews.forEach({ $0.removeFromSuperview() })
                    
                    if let serviceImages = self.arrayCases.value(forKey: "caseImages") as? NSArray{
                        if(serviceImages.count > 0){
                            var xpos:CGFloat = 10
                            for i in 0..<serviceImages.count{
                                if let imgDict = serviceImages[i] as? NSDictionary{
                                    if let imgBse64Str = imgDict["image"] as? String{
                                        if imgBse64Str.isEmpty{
                                            
                                        }else{
                                            let dataDecoded:NSData = NSData(base64Encoded: imgBse64Str, options: NSData.Base64DecodingOptions(rawValue: 0))!
                                            let decodedimage:UIImage = UIImage(data: dataDecoded as Data)!
                                            
                                            let imgButton = UIImageView()
                                            imgButton.frame = CGRect(x: xpos, y: 0, width: 60, height: 60)
                                            imgButton.layer.cornerRadius = 5.0
                                            //imgButton.layer.borderColor = LOGIN_BG_COLOR.cgColor
                                            imgButton.clipsToBounds = true
                                            imgButton.contentMode = UIViewContentMode.scaleAspectFill
                                            imgButton.image = decodedimage
                                            xpos = xpos + imgButton.width + 10
                                            
                                            self.mycasedettail.imagesScrollView.addSubview(imgButton)
                                            self.mycasedettail.imagesScrollView.contentSize.width = imgButton.maxXOrigin + 20
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
                    
                    hideprogress()
                
               
            }
        }
    }
    
    //TODO: CREATE VIEW ELEMENTS
     func createView(){
        
        let width = ScreenSize.SCREEN_WIDTH-20
        
        let descriptionTitle = UILabel()
        descriptionTitle.frame = CGRect(x: 10, y: 0, width: view.width - 20, height: 25)
        descriptionTitle.text = "Description"
        parentView.addSubview(descriptionTitle)
        
        mycasedettail.descriptionField = UITextField()
        mycasedettail.descriptionField.frame = CGRect(x: 10, y: descriptionTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.descriptionField)
        designtext123(firstLab: descriptionTitle, secondLab: mycasedettail.descriptionField)
        
        
    
        
        let createDateTitle = UILabel()
        createDateTitle.frame = CGRect(x: 10, y: (mycasedettail.descriptionField.maxYOrigin) + 5, width: width, height: 25)
        createDateTitle.text = "Created Date"
        parentView.addSubview(createDateTitle)
        
        mycasedettail.createDateField = UITextField()
        mycasedettail.createDateField.frame = CGRect(x: 10, y: createDateTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.createDateField)
        designtext123(firstLab: createDateTitle, secondLab: mycasedettail.createDateField)
        
        
        let updatedDateTitle = UILabel()
        updatedDateTitle.frame = CGRect(x: 10, y: (mycasedettail.createDateField.maxYOrigin) + 5, width: width, height: 25)
        updatedDateTitle.text = "Updated Date"
        parentView.addSubview(updatedDateTitle)
        
        mycasedettail.UpdatedDateField = UITextField()
        mycasedettail.UpdatedDateField.frame = CGRect(x: 10, y: updatedDateTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.UpdatedDateField)
        designtext123(firstLab: updatedDateTitle, secondLab: mycasedettail.UpdatedDateField)
        
        let addressTitle = UILabel()
        addressTitle.frame = CGRect(x: 10, y: (mycasedettail.UpdatedDateField.maxYOrigin) + 5, width: width, height: 25)
        addressTitle.text = "Address"
        parentView.addSubview(addressTitle)
        
        mycasedettail.subjectField = UITextField()
        mycasedettail.subjectField.frame = CGRect(x: 10, y: addressTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.subjectField)
        designtext123(firstLab: addressTitle, secondLab: mycasedettail.subjectField)
        
        let escalatedTitle = UILabel()
        escalatedTitle.frame = CGRect(x: 10, y: (mycasedettail.subjectField.maxYOrigin) + 5, width: width, height: 25)
        escalatedTitle.text = "Escalated"
        parentView.addSubview(escalatedTitle)
        
        mycasedettail.alertTypeField = UITextField()
        mycasedettail.alertTypeField.frame = CGRect(x: 10, y: escalatedTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.alertTypeField)
        designtext123(firstLab: escalatedTitle, secondLab: mycasedettail.alertTypeField)
        
        let statusTitle = UILabel()
        statusTitle.frame = CGRect(x: 10, y: (mycasedettail.alertTypeField.maxYOrigin) + 5, width: width, height: 25)
        statusTitle.text = "Status"
        parentView.addSubview(statusTitle)
        
        mycasedettail.satusField = UITextField()
        mycasedettail.satusField.frame = CGRect(x: 10, y: statusTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.satusField)
        designtext123(firstLab: statusTitle, secondLab: mycasedettail.satusField)
        
        let reportedTitle = UILabel()
        reportedTitle.frame = CGRect(x: 10, y: (mycasedettail.satusField.maxYOrigin) + 5, width: width, height: 25)
        reportedTitle.text = "Reported By"
        parentView.addSubview(reportedTitle)
        
        mycasedettail.toUserField = UITextField()
        mycasedettail.toUserField.frame = CGRect(x: 10, y: reportedTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.toUserField)
        designtext123(firstLab: reportedTitle, secondLab: mycasedettail.toUserField)
        
        let assignedTitle = UILabel()
        assignedTitle.frame = CGRect(x: 10, y: (mycasedettail.toUserField.maxYOrigin) + 5, width: width, height: 25)
        assignedTitle.text = "Updated By"
        parentView.addSubview(assignedTitle)
        
        mycasedettail.caseField = UITextField()
        mycasedettail.caseField.frame = CGRect(x: 10, y: assignedTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.caseField)
        designtext123(firstLab: assignedTitle, secondLab: mycasedettail.caseField)
        
        let updatedTitle = UILabel()
        updatedTitle.frame = CGRect(x: 10, y: (mycasedettail.caseField.maxYOrigin) + 5, width: width, height: 25)
        updatedTitle.text = "Updated By"
        parentView.addSubview(updatedTitle)
        
        mycasedettail.updatedField = UITextField()
        mycasedettail.updatedField.frame = CGRect(x: 10, y: updatedTitle.maxYOrigin, width: width, height: 40)
        parentView.addSubview(mycasedettail.updatedField)
        designtext123(firstLab: updatedTitle, secondLab: mycasedettail.updatedField)
        
        
        let imageTitle = UILabel()
        imageTitle.frame = CGRect(x: 10, y: (mycasedettail.updatedField.maxYOrigin) + 5, width: width, height: 25)
        imageTitle.text = "Images"
        imageTitle.font = UIFont(name: fontName, size: 14)
        imageTitle.textColor = LOGIN_BG_COLOR
        parentView.addSubview(imageTitle)
        
        
        mycasedettail.imagesScrollView = UIScrollView()
        mycasedettail.imagesScrollView.frame = CGRect(x: 10, y: imageTitle.maxYOrigin + 5, width: width, height: 80)
        parentView.addSubview(mycasedettail.imagesScrollView)
        
        self.parentView.contentSize.height = mycasedettail.imagesScrollView.maxYOrigin + 100
        
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    //TODO: Button Action
    
    func rightMenuClicked(){
        
        let vc = EditMycaseVC()
        vc.descriptvalue = mycasedettail.descriptionField.text!
        vc.caseDict = self.arrayCases
        self.navigationController?.pushViewController(vc, animated: false)
    }
    
    func rectForText(text: String, font: UIFont, maxSize: CGSize) -> CGSize {
        let attrString = NSAttributedString.init(string: text, attributes: [NSFontAttributeName:font])
        let rect = attrString.boundingRect(with: maxSize, options: NSStringDrawingOptions.usesLineFragmentOrigin, context: nil)
        let size = CGSize(width: rect.size.width, height: rect.size.height)
        return size
    }

}


