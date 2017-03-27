//
//  LoginViewController.swift
//  TrakEye
//
//  Created by Deepu on 09/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit
import TPKeyboardAvoiding
import Alamofire
import CoreLocation

extension UITextField
{
    func setSignInBottomBorder()
    {
        self.borderStyle = UITextBorderStyle.none;
        let border = CALayer()
        let width = CGFloat(0.3)
        border.frame = CGRect(x: 0, y: self.frame.size.height - width,   width:  self.frame.size.width , height: self.frame.size.height)
        
        border.borderWidth = width
        self.layer.addSublayer(border)
        self.layer.masksToBounds = true
    }
}

extension UIColor {
    convenience init(rgb: UInt) {
        self.init(
            red: CGFloat((rgb & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgb & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgb & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
let LOGIN_BG_COLOR = UIColor(rgb: 0x03a9f5)


class LoginViewController: UIViewController, UITextFieldDelegate, CLLocationManagerDelegate {

    ////MARK:- Variables & Constants
    let parentScrollView = TPKeyboardAvoidingScrollView()
    var bg_Img = UIImageView()
    var trakEye_Img = UIImageView()
    
    var usernameField = FloatLabelTextField()
    var passwordField = FloatLabelTextField()
    var loginBtn = UIButton()
    var locationManager = CLLocationManager()
    var wid : CGFloat = 0
    var loginDict = NSDictionary()
    var token : String!
    var userdeatils = UserInfo()
    var gpsStatus : Bool!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        wid = self.view.frame.size.width;
        usernameField.delegate = self
        passwordField.delegate = self
        self.loginView()
     //   self.updateLocationStatus()
        // Do any additional setup after loading the view.
        
        
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
    
    func loginView() {
        
        UIDevice.current.isBatteryMonitoringEnabled = true
        
        var batteryLevel: Float {
            return UIDevice.current.batteryLevel
        }
        
        print("battery  percentage \(batteryLevel)")
        
        parentScrollView.frame = CGRect(x: 0.0, y: 0 , width: wid, height: self.view.frame.size.height)
        parentScrollView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        parentScrollView.isScrollEnabled = false
        view.addSubview(parentScrollView)
        
        bg_Img.frame = CGRect(x: 0.0, y: 0 , width: wid, height: parentScrollView.height)
        //bg_Img.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        //subbView.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        bg_Img.image = UIImage(named: "Login_BG.png")
        parentScrollView.addSubview(bg_Img)
        
        trakEye_Img.frame = CGRect(x: wid/2 - 60, y: 80, width: 120, height: 120)
        trakEye_Img.image = UIImage(named: "Icon120X120.png")
       // parentScrollView.addSubview(trakEye_Img)

        let userImg = UIImageView()
        userImg.frame = CGRect(x: 0, y: ScreenSize.SCREEN_HEIGHT/2 + 10, width: 25, height: 25)
        userImg.image = UIImage(named: "username")
        //parentScrollView.addSubview(userImg)
        
        usernameField.frame = CGRect(x: 15, y: ScreenSize.SCREEN_HEIGHT/2 , width: wid - 80, height: 50)
        usernameField.keyboardType = UIKeyboardType.alphabet
        usernameField.leftViewMode = UITextFieldViewMode.always
        usernameField.leftView = userImg
        usernameField.placeholder = "User Name"
        usernameField.textColor = UIColor.white
        usernameField.font = UIFont(name: "Futura-Medium", size: 14)
        usernameField.returnKeyType=UIReturnKeyType.next
        usernameField.textAlignment = NSTextAlignment.left
        
        usernameField.setSignInBottomBorder()
        parentScrollView.addSubview(usernameField)
        
        let pwdImg = UIImageView()
        pwdImg.frame = CGRect(x: 0, y: usernameField.maxYOrigin + 30, width: 25, height: 25)
        pwdImg.image = UIImage(named: "password")
        //parentScrollView.addSubview(pwdImg)

        passwordField.frame = CGRect(x: 15, y: usernameField.maxYOrigin + 20, width: wid - 80, height: 50)
        passwordField.keyboardType = UIKeyboardType.alphabet
        passwordField.placeholder = "Password"
        passwordField.isSecureTextEntry = true
        passwordField.leftViewMode = UITextFieldViewMode.always
        passwordField.leftView = pwdImg
        passwordField.textColor = UIColor.white
        passwordField.font = UIFont(name: "Futura-Medium", size: 14)
        passwordField.textAlignment = NSTextAlignment.left
        passwordField.returnKeyType=UIReturnKeyType.done
        passwordField.setSignInBottomBorder()
        parentScrollView.addSubview(passwordField)
        
        loginBtn.frame = CGRect(x: 20, y: passwordField.maxYOrigin + 40, width: wid - 40, height: 50)
        loginBtn.backgroundColor = LOGIN_BG_COLOR
        loginBtn.layer.cornerRadius = 5.0
        loginBtn.titleLabel?.textColor = UIColor.white
        loginBtn.setTitle("LOGIN", for: UIControlState.normal)
        loginBtn.setTitleColor(UIColor.white, for: UIControlState.normal)
        loginBtn.titleLabel?.textAlignment = NSTextAlignment.center
        loginBtn.titleLabel?.font = UIFont(name: "HelveticaNeue-Bold", size: 15)
        loginBtn.addTarget(self, action: #selector(LoginViewController.loginClicked), for: UIControlEvents.touchDown)
        parentScrollView.addSubview(loginBtn)
    }
 
    //TODO: Login Button Action
    
    func loginClicked(){
        if(usernameField.text == ""){
            
            showAlertMessage(titleStr: Title, messageStr: "Please enter a valid Username.")
            
        } else if(passwordField.text == ""){
            
            showAlertMessage(titleStr: Title, messageStr: "Please enter a valid Password.")
            
        } else{
            
            let parameters: [String: Any] = [
                
                "username" : usernameField.text!.lowercased(),
                "password": passwordField.text!,
                "rememberMe" : true,
                "fcmToken" : APP_DELEGATE.fcm_Token,
                "imei" : UIDevice.current.identifierForVendor!.uuidString,
                "applicationVersion" : Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String,
                "operatingSystem" : "ios"
            ]
            print(parameters)
            let userDefults = UserDefaults.standard
            userDefults.set(usernameField.text, forKey: "name")
            userDefults.synchronize()
            
            userDefults.set(passwordField.text, forKey: "password")
            userDefults.synchronize()

            if UserDefaults.standard.bool(forKey: "networkStatus") {
                
                showprogress(view: self.view)
                
                Alamofire.request("\(BaseUrl)\(kUserLogin)", method:.post, parameters: parameters, encoding: JSONEncoding.default).responseJSON { response in
                    if(response.result.isFailure){
                        hideprogress()
                        print("no data!");
                    }else{
                        if(response.response?.statusCode==200)
                        {
                            print(response.result.value!)
                            
                            hideprogress()
                            
                            UserDefaults.standard.set("Login", forKey: "YES");
                            UserDefaults.standard.synchronize()
                            

                            self.loginDict = response.result.value as! NSDictionary
                            
                            self.userdeatils.updatewithValues(responsedata: self.loginDict)
                            saveuserdata(user: self.userdeatils)
                            
                            showSuccessprogress(title: "Successfully Logged in", view: self.view)
                            
                            self.token = (self.loginDict.object(forKey: "id_token") as! NSString) as String
                            
                            self.getuserdetails(tokenstring: self.token)
                            if self.token.isEmpty {
                            }else{
                                let updateTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(LoginViewController.navigation), userInfo: nil, repeats: false)
                                print(updateTimer)
                            }
                            
                        }else if(response.response?.statusCode==401)
                        {
                            print(response.result.value!)
                            hideprogress()
                            self.loginDict = response.result.value as! NSDictionary
                            showerrorprogress(title: (self.loginDict.object(forKey: "AuthenticationException") as! String?)!, view: self.view)
                        }
                        else if(response.response?.statusCode==403)
                        {
                            print(response.result.value!)
                            hideprogress()
                        }
                        else
                        {
                            print(response.result.value!)
                            hideprogress()
                        }
                    }
                }
               
            } else {
                
                print("Internet connection FAILED")
                 showerrorprogress(title: kInterenetAlertMessage, view: self.view)
            }
            
        }
        
    }
    
    func getuserdetails(tokenstring : String){
        
        let urlstring = BaseUrl + kGetprofileInfo
        
        Alamofire.request(urlstring, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: ["Authorization" : "Bearer \(tokenstring)", "Content-Type" : "application/x-www-form-urlencoded"]).responseJSON { (response:DataResponse<Any>) in
            
            switch(response.result) {
            case .success(_):
                if response.result.value != nil{
                    print(response.result.value ?? NSDictionary())
                    
                    let responsedict = response.result.value as! NSDictionary
                    
                    self.userdeatils.updateUserwithValues(responsedata: responsedict)
                    saveuserdata(user: self.userdeatils)
                    
                }
                break
                
            case .failure(_):
                print(response.result.error ?? NSDictionary())
                break
                
            }
        }

    }


    func navigation(){
        let mapView = MapViewController()
        //mapView.tokenStr = token
        self.navigationController?.pushViewController(mapView, animated: false)
        //self.present(mapView, animated: false, completion: nil)
    }
    
    //TODO: UITextfield Delegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == usernameField {
            return passwordField.becomeFirstResponder()
        }
        return textField.resignFirstResponder()
    }
    
 }
