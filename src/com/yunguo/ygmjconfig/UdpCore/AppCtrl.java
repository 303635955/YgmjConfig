/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.net.SocketException;

/**
 *
 * @author jp
 */
class AppCtrl {
    private static AppCtrl appctrl = new AppCtrl();
    private Udp udp;
    public static AppCtrl GetAppCtrl(){
        return appctrl;
    }
    
    public Udp GetUdp(){
        if(udp == null){
            try {
                udp = new Udp();
            } catch (SocketException ex) {
            }
        }
        return udp;
    }
    
    private AppCtrl(){
        
    }
}
