/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;

import java.util.Calendar;

/**
 * 刷卡记录及告警记录
 * @author Administrator
 */
public class Record {
        /// <summary>
        /// 记录索引
        /// </summary>
        public int RecIndex;
        /// <summary>
        /// 记录类型
        /// </summary>
        public int Type;
        /// <summary>
        /// 是否通过 0:禁止通过, 1:允许通过
        /// </summary>
        public byte Passed;
        /// <summary>
        /// 门号
        /// </summary>
        public int DoorNo;
        /// <summary>
        /// 进出标志 1:进门, 2:出门
        /// </summary>
        public int InOut;
        /// <summary>
        /// 卡号
        /// </summary>
        public long CardNo;
        /// <summary>
        /// 刷卡时间
        /// </summary>
        public Calendar Time;
        /// <summary>
        /// 原因代码， 禁止通过时的原因代码
        /// </summary>
        public int ReasonCode;
        /// <summary>
        /// 原因描述
        /// </summary>
        public String ReasonDescription(){
                throw new  java.util.NoSuchElementException();
        }

        /* 门磁开闭状态。 ----------------------- */
        public int DoorStatus;
}
