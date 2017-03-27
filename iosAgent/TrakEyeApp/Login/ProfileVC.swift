//
//  ProfileVC.swift
//  TrakEyeApp
//
//  Created by Mitansh on 21/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import Alamofire

class ProfileVC: UIViewController {

    var firstnameField = FloatLabelTextField()
    var lastnameField = FloatLabelTextField()
    var emaileField = FloatLabelTextField()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let navigationBar = UINavigationBar(frame: CGRect(x: 0, y: 0, width: view.width, height: 64))
        navigationBar.backgroundColor = LOGIN_BG_COLOR
        let navigationItem = UINavigationItem()
        navigationBar.items = [navigationItem]
        
        let headingLabel = UILabel()
        headingLabel.frame = CGRect(x: view.width/2 - 42.5, y: 25, width: ScreenSize.SCREEN_WIDTH/2, height: 35)
        headingLabel.text = "My Profile"
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
        
        let userdata = fetchuserdata()
        
        
        firstnameField.frame = CGRect(x: 10, y: 74, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        firstnameField.keyboardType = UIKeyboardType.alphabet
        firstnameField.isUserInteractionEnabled = false
        firstnameField.placeholder = "First Name"
        firstnameField.textColor = UIColor.black
        firstnameField.text = userdata.firstname
        firstnameField.font = UIFont(name: "Futura-Medium", size: 14)
        firstnameField.returnKeyType=UIReturnKeyType.done
        firstnameField.textAlignment = NSTextAlignment.left
        firstnameField.setSignInBottomBorder()
        self.view.addSubview(firstnameField)
        
        lastnameField.frame = CGRect(x: 10, y: 140, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        lastnameField.keyboardType = UIKeyboardType.alphabet
        lastnameField.isUserInteractionEnabled = false
        lastnameField.placeholder = "Last Name"
        lastnameField.textColor = UIColor.black
        lastnameField.text = userdata.lastname
        lastnameField.font = UIFont(name: "Futura-Medium", size: 14)
        lastnameField.returnKeyType=UIReturnKeyType.done
        lastnameField.textAlignment = NSTextAlignment.left
        lastnameField.setSignInBottomBorder()
        self.view.addSubview(lastnameField)
        
        
        
        emaileField.frame = CGRect(x: 10, y: 210, width: ScreenSize.SCREEN_WIDTH-20, height: 60)
        emaileField.keyboardType = UIKeyboardType.alphabet
        emaileField.isUserInteractionEnabled = false
        emaileField.placeholder = "Email"
        emaileField.textColor = UIColor.black
        emaileField.text = userdata.email
        emaileField.font = UIFont(name: "Futura-Medium", size: 14)
        emaileField.returnKeyType=UIReturnKeyType.done
        emaileField.textAlignment = NSTextAlignment.left
        emaileField.setSignInBottomBorder()
        self.view.addSubview(emaileField)
        
        
    }

    
    //TODO: Button Action
    func backClicked() {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    


}
