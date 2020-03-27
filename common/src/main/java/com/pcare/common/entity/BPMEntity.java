package com.pcare.common.entity;

import androidx.annotation.NonNull;

import com.pcare.common.util.CommonUtil;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author: gl
 * @CreateDate: 2019/11/26
 * @Description: 血压实体类
 */
@Entity
public class BPMEntity {
    private String user_id;//用户ID
    private String id;//血压记录ID
    private int systolic; //高压值
    private int diastolic;//低压值
    private int mean;   //平均压
    private String unit;  //血压单位
    private int pulse; //脉搏
    private String check_time;//时间
    private int result;//用户结果类型，正常，偏高，偏低等
    private int sport = 0;
    private int emptiness = 0;
    private String robot_id;
    @Generated(hash = 673142445)
    public BPMEntity(String user_id, String id, int systolic, int diastolic, int mean,
            String unit, int pulse, String check_time, int result, int sport, int emptiness,
            String robot_id) {
        this.user_id = user_id;
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.mean = mean;
        this.unit = unit;
        this.pulse = pulse;
        this.check_time = check_time;
        this.result = result;
        this.sport = sport;
        this.emptiness = emptiness;
        this.robot_id = robot_id;
    }
    @Generated(hash = 549838386)
    public BPMEntity() {
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getSystolic() {
        return this.systolic;
    }
    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }
    public int getDiastolic() {
        return this.diastolic;
    }
    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }
    public int getMean() {
        return this.mean;
    }
    public void setMean(int mean) {
        this.mean = mean;
    }
    public int getPulse() {
        return this.pulse;
    }
    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
    public String getCheck_time() {

        return this.check_time;
    }
    public void setCheck_time(String check_time) {
        this.check_time = check_time;
    }
    public String getRobot_id() {
        return this.robot_id;
    }
    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public int getEmptiness() {
        return emptiness;
    }

    public void setEmptiness(int emptiness) {
        this.emptiness = emptiness;
    }


    @Override
    public String toString() {
        return CommonUtil.entityToJson(this);
//        return "{\"user_id\":\"" + getUser_id() +
//                "\",\"id\":\"" + getBpmId() +
//                "\",\"systolic\":\"" + getSystolicData() +
//                "\",\"diastolic\":\"" + getDiastolicData() +
//                "\",\"mean\":\"" + getMeanAPData() +
//                "\",\"unit\":\"" + getUnit() +
//                "\",\"pulse\":\"" + getPulseData() +
//                "\",\"check_time\":\"" + CommonUtil.getDateStr(getTimeData(), null) +
//                "\",\"result\":\"" + getResult() +
//                "\",\"robot_id\":\"" + getRobot_id() +
//                "\",\"sport\":\"" + getSport() +
//                "\",\"emptiness\":\"" + getEmptiness() +
//                "\",\"status\":\"" + getStatus() +
//                "\"}";
    }

}
