//
//  MenuViewController.swift
//  TrakEyeApp
//
//  Created by Apple on 14/11/16.
//  Copyright © 2016 Deepu. All rights reserved.
//

import UIKit

class MenuViewController: BaseviewController, UICollectionViewDelegate, UICollectionViewDataSource {

    var menuCollectionView: UICollectionView!
   
  //  var imageArray : NSMutableArray = NSMutableArray()
    var imageArray: [UIImage] = [
        UIImage(named: "CREATE_CASES")!,
        UIImage(named: "MYCASES")!,
        UIImage(named: "SERVICES")!,
        UIImage(named: "Notifications")!,
        UIImage(named: "Profile")!,
        UIImage(named: "HELP")!,
    ]

    override func viewDidLoad() {
        super.viewDidLoad()
        
        searechbackgrndview.isHidden = true
        geoFenceBtn.isHidden = true
        self.edgesForExtendedLayout = []
             //   imageArray = ["CREATE_CASES","MYCASES","SERVICES","Notifications","Profile","HELP"]
        // Create a navigation item with a title
        
//        
//        let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
//        layout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
//        layout.itemSize = CGSize(width: view.width/2 - 15 , height: 150)
        
//        menuCollectionView = UICollectionView(frame: CGRect(x: 0, y: 0, width: view.width, height: view.height), collectionViewLayout: layout)
//        menuCollectionView.tag = 1
//        menuCollectionView.dataSource = self
//        menuCollectionView.delegate = self
//        menuCollectionView.register(UICollectionViewCell.self, forCellWithReuseIdentifier: "Cell")
//        menuCollectionView.backgroundColor = UIColor.clear
//        self.view.addSubview(menuCollectionView)

        let width = ScreenSize.SCREEN_WIDTH/2
        let height = (ScreenSize.SCREEN_HEIGHT-64)/4
        
        let createBtn = UIButton()
        createBtn.frame = CGRect(x: 0, y: 0, width: width , height: height)
        createBtn.setImage(UIImage(named: "menu_create_cases")!, for: UIControlState.normal)
        createBtn.addTarget(self, action: #selector(createBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(createBtn)
        
        let mdline = UILabel()
        mdline.frame = CGRect(x: width - 1, y: 20, width: 1, height: height-60)
        mdline.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(mdline)
        
        
        let mdTitle = UILabel()
        mdTitle.frame = CGRect(x: 30, y: createBtn.maxYOrigin , width: width-60, height: 1)
        mdTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(mdTitle)
        
        
        let mycaseBtn = UIButton()
        mycaseBtn.frame = CGRect(x: width , y: 0, width: width, height: height)
        mycaseBtn.setImage(UIImage(named: "menu_mycases")!, for: UIControlState.normal)
        mycaseBtn.addTarget(self, action: #selector(mycaseBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(mycaseBtn)
        
        
        let mycaseTitle = UILabel()
        mycaseTitle.frame = CGRect(x:  width + 30 , y: mycaseBtn.maxYOrigin , width: width-60, height: 1)
        mycaseTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(mycaseTitle)
        
        
        let createAssets = UIButton()
        createAssets.frame = CGRect(x: 0, y: mycaseTitle.maxYOrigin, width: width , height: height)
        createAssets.setImage(UIImage(named: "menu_create_asset")!, for: UIControlState.normal)
        createAssets.addTarget(self, action: #selector(createAssetBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(createAssets)
        
        let serviceline = UILabel()
        serviceline.frame = CGRect(x: width - 1, y: mycaseTitle.maxYOrigin + 30, width: 1, height: height-60)
        serviceline.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(serviceline)
        
        let serviceTitle = UILabel()
        serviceTitle.frame = CGRect(x: 30, y: createAssets.maxYOrigin , width: width-60, height: 1)
        serviceTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(serviceTitle)
        
        let myAssetsBtn = UIButton()
        myAssetsBtn.frame = CGRect(x: width , y: mycaseTitle.maxYOrigin, width: width, height: height)
        myAssetsBtn.setImage(UIImage(named: "menu_assets")!, for: UIControlState.normal)
        myAssetsBtn.addTarget(self, action: #selector(self.assetBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(myAssetsBtn)
        
        let notificationeTitle = UILabel()
        notificationeTitle.frame = CGRect(x:  width + 30 , y: myAssetsBtn.maxYOrigin , width: width-60, height: 1)
        notificationeTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(notificationeTitle)
        
        
        let serviceBtn = UIButton()
        serviceBtn.frame = CGRect(x: 0, y: notificationeTitle.maxYOrigin, width: width , height: height)
        serviceBtn.setImage(UIImage(named: "menu_services")!, for: UIControlState.normal)
        serviceBtn.addTarget(self, action: #selector(serviceBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(serviceBtn)
        
        let profileline = UILabel()
        profileline.frame = CGRect(x: width - 1, y: notificationeTitle.maxYOrigin + 30, width: 1, height: height-60)
        profileline.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(profileline)

        let profileTitle = UILabel()
        profileTitle.frame = CGRect(x: 30 , y: serviceBtn.maxYOrigin , width: width-60, height: 1)
        profileTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(profileTitle)
        
        let notificationeBtn = UIButton()
        notificationeBtn.frame = CGRect(x: width , y: notificationeTitle.maxYOrigin, width: width, height: height)
        notificationeBtn.setImage(UIImage(named: "menu_notifications")!, for: UIControlState.normal)
        notificationeBtn.addTarget(self, action: #selector(notificationeBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(notificationeBtn)
        
        let helpTitle = UILabel()
        helpTitle.frame = CGRect(x:  width + 30 , y: notificationeBtn.maxYOrigin , width: width-60, height: 1)
        helpTitle.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(helpTitle)
        
        let profileBtn = UIButton()
        profileBtn.frame = CGRect(x: 0, y: helpTitle.maxYOrigin, width: width , height: height)
        profileBtn.setImage(UIImage(named: "menu_profile")!, for: UIControlState.normal)
        profileBtn.addTarget(self, action: #selector(profileBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(profileBtn)
        
        let createAssetsline = UILabel()
        createAssetsline.frame = CGRect(x: width - 1, y: helpTitle.maxYOrigin + 30, width: 1, height: height-60)
        createAssetsline.backgroundColor = UIColor.darkGray.withAlphaComponent(0.8)
        self.view.addSubview(createAssetsline)
        

        let helpBtn = UIButton()
        helpBtn.frame = CGRect(x: width , y: helpTitle.maxYOrigin , width: width, height: height-10)
        helpBtn.setImage(UIImage(named: "menu_help")!, for: UIControlState.normal)
        helpBtn.addTarget(self, action: #selector(helpBtnClicked), for: UIControlEvents.touchDown)
        self.view.addSubview(helpBtn)
        
       
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func createBtnClicked()
    {
        let createcase = CreatecaseVC()
        self.navigationController?.pushViewController(createcase, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Create Case"
        navigationItem.backBarButtonItem = backItem
    }
    func mycaseBtnClicked()
    {
        let mycase = MycasesVC()
        self.navigationController?.pushViewController(mycase, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "My Cases"
        navigationItem.backBarButtonItem = backItem
    }
    func serviceBtnClicked()
    {
        let Servic = ServicesVC()
        self.navigationController?.pushViewController(Servic, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Services"
        navigationItem.backBarButtonItem = backItem
    }
    
    
    func notificationeBtnClicked()
    {
        let notification = NotificationsVC()
        self.navigationController?.pushViewController(notification, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Notifications"
        navigationItem.backBarButtonItem = backItem
    }
    
    func profileBtnClicked()
    {
        let myProfile = ProfileVC()
        self.navigationController?.pushViewController(myProfile, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "My Profile"
        navigationItem.backBarButtonItem = backItem
    }
    
    func helpBtnClicked()
    {
         showerrorprogress(title: "This feature curruntly not available.", view: self.view)
    }
    
    func assetBtnClicked()
    {
        let assest = myAssetsVC()
        self.navigationController?.pushViewController(assest, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Assets"
        navigationItem.backBarButtonItem = backItem
    }
    
    
    //TODO: Collectionview Delegate Methodes
    
    func createAssetBtnClicked()
    {
        let createAsset = CreateAssetsViewController()
        self.navigationController?.pushViewController(createAsset, animated: false)
        let backItem = UIBarButtonItem()
        backItem.title = "Create Asset"
        navigationItem.backBarButtonItem = backItem
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
            return 6
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
           let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "Cell", for: indexPath as IndexPath)
            cell.backgroundColor = UIColor.white
            cell.layer.borderColor = UIColor.lightGray.cgColor
            cell.layer.borderWidth = 1.0
        
       let menuIcons = UIImageView()
        menuIcons.frame = CGRect(x: 20, y: 20, width: cell.frame.size.width - 40, height: cell.frame.size.height - 40)
        menuIcons.image = imageArray[indexPath.row]
        cell.contentView.addSubview(menuIcons)
            return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.item==0 {
            let createcase = CreatecaseVC()
            self.navigationController?.pushViewController(createcase, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Create Case"
            navigationItem.backBarButtonItem = backItem
        }else if indexPath.item==1{
            let mycase = MycasesVC()
            self.navigationController?.pushViewController(mycase, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "My Cases"
            navigationItem.backBarButtonItem = backItem
        }else if indexPath.item==2{
            let Servic = ServicesVC()
            self.navigationController?.pushViewController(Servic, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Services"
            navigationItem.backBarButtonItem = backItem
        }
        else if indexPath.item==3{
            let notification = NotificationsVC()
            self.navigationController?.pushViewController(notification, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "Notifications"
            navigationItem.backBarButtonItem = backItem
        }else if indexPath.item==4{
            let myProfile = ProfileVC()
            self.navigationController?.pushViewController(myProfile, animated: false)
            let backItem = UIBarButtonItem()
            backItem.title = "My Profile"
            navigationItem.backBarButtonItem = backItem
        }else if indexPath.item==5{
            showerrorprogress(title: "This feature will be coming soon", view: self.view)
        }
        
    }
    
    
    func backClicked() {
       self.dismiss(animated: true, completion: nil)
    }
}
