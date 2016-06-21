/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;

/**
 *
 * @author Administrator
 */
public class ControlParam {
        /**常开*/
        public final int CtrlType_NO=1;
        /**常闭*/
        public final int CtrlType_NC=2;
        /** 在线控制*/
        
        public final int CtrlType_Online = 3;
        /// <summary>
        /// 门号
        /// </summary>
        public int DoorNo;
        /// <summary>
        /// 控制方式
        /// </summary>
        public int CtrlType;
        /// <summary>
        /// 开门延时(秒)
        /// </summary>
        public int OpenDuration;
        /// <summary>
        /// 权限判断方式
        /// </summary>
        public int PriviligeType;
        /// <summary>
        /// 按钮开门使能
        /// </summary>
        public Boolean PressEnable;
        /// <summary>
        /// 警笛功能使能
        /// </summary>
        public Boolean AlarmEnable;
        /// <summary>
        /// 关闭提醒延时(秒)
        /// </summary>
        public int CloseAttenDuration;
        /// <summary>
        /// 技术开锁报警
        /// </summary>
        public Boolean DestroyAlarm;
        /// <summary>
        /// 反潜入 
        /// </summary>
        public Boolean DefendSlip;
        /// <summary>
        /// 防尾随
        /// </summary>
        public Boolean DefendShadow;
}
