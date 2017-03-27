//
//  BaseviewController.swift
//  TrakEyeApp
//
//  Created by Mitansh on 21/12/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class BaseviewController: UIViewController, UITextFieldDelegate {

    
    var searechbackgrndview = UIView()
    var searchtext = UITextField()
    var initialpageBtn = UIButton()
    var previousBtn = UIButton()
    var curruntpagelbl = UILabel()
    var nextBtn = UIButton()
    var lastpageBtn = UIButton()
    var geoFenceBtn = UIButton()
    var myCaseBtn = UIButton()
    var notificationBtn = UIButton()
    var noResultlbl = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
//        let buttonsview = UIView()
//        buttonsview.frame = CGRect(x: 10, y: 0, width: ScreenSize.SCREEN_WIDTH/1.5, height: 64)
//        buttonsview.backgroundColor = UIColor.clear
//        buttonsview.layer.cornerRadius = 5.0
//        
//        geoFenceBtn.frame = CGRect(x: buttonsview.frame.size.width/1.8, y: 19, width: 25, height: 25)
//        geoFenceBtn.layer.cornerRadius = 5.0
//        geoFenceBtn.setImage(UIImage(named : "geofence"), for: UIControlState.normal)
//        geoFenceBtn.addTarget(self, action: #selector(BaseviewController.geoFenceBtnclicked), for: UIControlEvents.touchDown)
//        buttonsview.addSubview(geoFenceBtn)
//        
//        myCaseBtn.frame = CGRect(x: geoFenceBtn.maxXOrigin, y: 19, width: 25, height: 25)
//        myCaseBtn.layer.cornerRadius = 5.0
//        myCaseBtn.setImage(UIImage(named : "cases"), for: UIControlState.normal)
//        myCaseBtn.addTarget(self, action: #selector(BaseviewController.myCaseBtnclicked), for: UIControlEvents.touchDown)
//        buttonsview.addSubview(myCaseBtn)
//        
//        notificationBtn.frame = CGRect(x: myCaseBtn.maxXOrigin, y: 19, width: 25, height: 25)
//        notificationBtn.layer.cornerRadius = 5.0
//        notificationBtn.setImage(UIImage(named : "bell"), for: UIControlState.normal)
//        notificationBtn.addTarget(self, action: #selector(BaseviewController.notificationBtnclicked), for: UIControlEvents.touchDown)
//        buttonsview.addSubview(notificationBtn)
//        
//        self.navigationItem.titleView = buttonsview
        
        searechbackgrndview.frame = CGRect(x: 0, y: 64, width: ScreenSize.SCREEN_WIDTH, height: 50)
        searechbackgrndview.backgroundColor = UIColor(rgb: 0x03a9f5)
        searechbackgrndview.alpha = 0.8
        self.view.addSubview(searechbackgrndview)
        
        let searechview = UIView()
        searechview.frame = CGRect(x: 10, y: 5, width: ScreenSize.SCREEN_WIDTH-20, height: 40)
        searechview.backgroundColor = UIColor.white
        searechview.layer.cornerRadius = 5.0
        searechbackgrndview.addSubview(searechview)
        
        searchtext.frame = CGRect(x: 10, y: 5, width: ScreenSize.SCREEN_WIDTH-20, height: 30)
        searchtext.delegate = self
        searchtext.placeholder = "Search by description"
        searchtext.addTarget(self, action: #selector(textFieldTyping), for: .editingChanged)
        searchtext.leftViewMode = UITextFieldViewMode.always
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let image = UIImage(named: "search.png")
        imageView.image = image
        searchtext.leftView = imageView
        searechview.addSubview(searchtext)
        
        
        
        noResultlbl.frame = CGRect(x: ScreenSize.SCREEN_WIDTH/2, y: ScreenSize.SCREEN_HEIGHT/2, width: ScreenSize.SCREEN_WIDTH/2, height: 35)
        noResultlbl.text = "No Result Found."
        noResultlbl.textColor = UIColor.white
        noResultlbl.font = UIFont(name: "HelveticaNeue-Bold", size: 17)
        noResultlbl.textAlignment = NSTextAlignment.left
        self.view.addSubview(noResultlbl)
       
        
        let pagenationview = UIView()
        pagenationview.frame = CGRect(x: 0, y: ScreenSize.SCREEN_HEIGHT - 60, width: ScreenSize.SCREEN_WIDTH, height: 60)
        pagenationview.backgroundColor = UIColor.lightText
        pagenationview.layer.cornerRadius = 5.0
        self.view.addSubview(pagenationview)
        
        let width = (ScreenSize.SCREEN_WIDTH - 220)/2
        
        initialpageBtn.frame = CGRect(x: width, y: 20, width: 40, height: 40)
        let initImage = UIImage(named: "first")
        initialpageBtn.setImage(initImage, for: UIControlState.normal)
        initialpageBtn.layer.cornerRadius = 5.0
        initialpageBtn.addTarget(self, action: #selector(BaseviewController.initialpageBtnclicked), for: UIControlEvents.touchDown)
        pagenationview.addSubview(initialpageBtn)
        
        previousBtn.frame = CGRect(x: initialpageBtn.maxXOrigin + 5, y: 20, width: 40, height: 40)
        previousBtn.layer.cornerRadius = 5.0
        let prevousImage = UIImage(named: "previous")
        previousBtn.setImage(prevousImage, for: UIControlState.normal)
        previousBtn.addTarget(self, action: #selector(BaseviewController.previousBtnClicked), for: UIControlEvents.touchDown)
        pagenationview.addSubview(previousBtn)
        
        
        let countImageView = UIImageView(frame: CGRect(x: previousBtn.maxXOrigin , y: 20, width: 40, height: 40))
        let countimage = UIImage(named: "button_bg")
        countImageView.image = countimage
        pagenationview.addSubview(countImageView)
        
        curruntpagelbl.frame = CGRect(x: previousBtn.maxXOrigin + 5, y: 25, width: 30, height: 30)
        curruntpagelbl.text = "1"
        curruntpagelbl.textColor = UIColor.black
        curruntpagelbl.textAlignment = NSTextAlignment.center
        curruntpagelbl.numberOfLines = 0
        pagenationview.addSubview(curruntpagelbl)
        
        nextBtn.frame = CGRect(x: curruntpagelbl.maxXOrigin + 5 , y: 20, width: 40, height: 40)
        nextBtn.layer.cornerRadius = 5.0
        let nextBtnImage = UIImage(named: "next")
        nextBtn.setImage(nextBtnImage, for: UIControlState.normal)
        nextBtn.addTarget(self, action: #selector(BaseviewController.nextBtnClicked), for: UIControlEvents.touchDown)
        pagenationview.addSubview(nextBtn)
        
        lastpageBtn.frame = CGRect(x: nextBtn.maxXOrigin + 5 , y: 20, width: 40, height: 40)
        lastpageBtn.layer.cornerRadius = 5.0
        let lastpageBtnImage = UIImage(named: "last")
        lastpageBtn.setImage(lastpageBtnImage, for: UIControlState.normal)
        lastpageBtn.addTarget(self, action: #selector(BaseviewController.lastpagebtnclicked), for: UIControlEvents.touchDown)
        pagenationview.addSubview(lastpageBtn)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    func textFieldTyping(textField:UITextField){

    }
    
    
    func previousBtnClicked(){
        
    }
    
    func nextBtnClicked()
    {
        
    }
    func lastpagebtnclicked()
    {
        
    }
    func initialpageBtnclicked()
    {
        
    }
    func notificationBtnclicked()
    {
        let notification = NotificationsVC()
        self.navigationController?.pushViewController(notification, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Notifications"
        navigationItem.backBarButtonItem = backItem
    }
    func myCaseBtnclicked()
    {
        let mycase = MycasesVC()
        self.navigationController?.pushViewController(mycase, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "My Cases"
        navigationItem.backBarButtonItem = backItem
    }
    func geoFenceBtnclicked()
    {
        
    }
    
    

    

}
