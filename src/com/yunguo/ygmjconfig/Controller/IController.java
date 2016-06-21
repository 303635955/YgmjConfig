/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;
import java.util.List;
import java.util.Calendar;
/**
 * 门禁控制器协议封装
 * @author Administrator
 */
public abstract class  IController {
        public static final int CONCTROLLER_YG = 1;
        public static final int CONCTROLLER_WG = 2;
        
        /**
         * 搜索控制器
        * @param type: 控制器类型, CONCTROLLER_YG(1):云果控制器；CONCTROLLER_WG(2):微耕控制器
         */
        public static List<IController> Search(int type){
            List<IController> ctrl = null;
            try{
                
                switch(type){
                    case CONCTROLLER_YG:
                        Class<?> c = YGController.class;
                        ctrl = (List<IController>)c.getMethod("Search").invoke(null);
                        break;
                    default:
                        break;
                }
            }catch(Exception e){
            	e.printStackTrace();
            }
            return ctrl;
        
        }
        
        
        
        /// <summary>
        /// 序列号
        /// </summary>
        public abstract long GetSerialNo();
        /// <summary>
        /// IP
        /// </summary>
        public abstract String GetIP();
        /// <summary>
        /// 子网掩码
        /// </summary>
        public abstract String GetMask();
        /// <summary>
        /// 网关
        /// </summary>
        public abstract String GetGetway();
        /// <summary>
        /// MAC地址
        /// </summary>
        public abstract String GetMAC();
        /// <summary>
        /// 版本号
        /// </summary>
        public abstract String GetVersion();
        
        /// <summary>
        /// 刷卡事件
        /// </summary>
        //event RecordEventHandler OnNewRecord;
        /// <summary>
        /// 开启实时监听【主动监听】
        /// </summary>
        public abstract void StartListen();
        /// <summary>
        /// 停止实时监听
        /// </summary>
        public abstract void StopListen();

        
        /// <summary>
        /// 获取状态
        /// </summary>
        /// <returns></returns>
        public abstract Record GetStatus();
        /// <summary>
        /// 设置IP
        /// </summary>
        /// <param name="ip"></param>
        /// <param name="mask"></param>
        /// <param name="getway"></param>
        public abstract void SetIP(String ip, String mask, String getway, Boolean dhcp);
        /// <summary>
        /// 设置时间
        /// </summary>
        /// <param name="time"></param>
        /// <returns></returns>
        public abstract Boolean SetTime(Calendar time);
        /// <summary>
        /// 获取时间
        /// </summary>
        /// <returns></returns>
        public abstract Calendar GetTime();

        /// <summary>
        /// 获取控制器权限条数
        /// </summary>
        /// <returns></returns>
        public abstract int GetPriviligeCnt();
        /// <summary>
        /// 获取指定ID的权限信息。
        /// </summary>
        /// <param name="Index"></param>
        /// <returns></returns>
        public abstract Privilige GetPriviligeAt(int Index);
        /// <summary>
        /// 添加权限
        /// </summary>
        /// <param name="p"></param>
        /// <returns></returns>
        public abstract Boolean AddPrivilige(Privilige p);
        /// <summary>
        /// 编辑权限
        /// </summary>
        /// <param name="p"></param>
        /// <returns></returns>
        public abstract Boolean EditPrivilige(Privilige p);
        /// <summary>
        /// 删除权限
        /// </summary>
        /// <param name="cardno"></param>
        /// <returns></returns>
        public abstract Boolean DeletePrivilige(long cardno);
        /// <summary>
        /// 查询权限
        /// </summary>
        /// <param name="cardno"></param>
        /// <returns></returns>
        public abstract Privilige GetPrivilige(long cardno);
        /// <summary>
        /// 统计权限数量
        /// </summary>
        /// <returns></returns>
        public abstract int CountPrivilige();
        /// <summary>
        /// 清空权限
        /// </summary>
        /// <returns></returns>
        public abstract  Boolean ClearPrivilige();
        /// <summary>
        /// 根据索引读取记录
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public abstract Record GetRecordAt(int index);
        /// <summary>
        /// 设置记录已读
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public abstract Boolean SetRecordReaded(int index);
        /// <summary>
        /// 统计记录
        /// </summary>
        /// <returns></returns>
        public abstract int CountUnreadRecord();
        /// <summary>
        /// 获取记录总数。
        /// </summary>
        /// <returns></returns>
        public abstract int CountRecordTotal();
        /// <summary>
        /// 删除记录
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public abstract Boolean DeleteRecord(int index);
        /// <summary>
        /// 远程开门
        /// </summary>
        /// <returns></returns>
        public abstract Boolean OpenDoor();
        /// <summary>
        /// 获取控制参数
        /// </summary>
        /// <param name="doorno"></param>
        /// <returns></returns>
        public abstract ControlParam GetControlParam(long doorno);
        /// <summary>
        /// 设置控制参数
        /// </summary>
        /// <param name="param"></param>
        /// <returns></returns>
        public abstract Boolean SetControlParam(ControlParam param);

        /// <summary>
        /// 模拟刷卡
        /// </summary>
        /// <param name="cardno"></param>
        /// <param name="readerno"></param>
        /// <returns></returns>
        public abstract Boolean SimuSwipe(long cardno, int readerno);
        
        /// <summary>
        /// 设置门禁平台地址
        /// </summary>
        /// <param name="cardno"></param>
        /// <param name="readerno"></param>
        /// <returns></returns>
        public abstract String GetDevCtrlPlatform(long sn, String IP);

        /// <summary>
        /// 获取门禁平台地址
        /// </summary>
        /// <param name="cardno"></param>
        /// <param name="readerno"></param>
        /// <returns></returns>
        public abstract boolean SetDevCtrlPlatform(String url,long sn,String IP);

}
