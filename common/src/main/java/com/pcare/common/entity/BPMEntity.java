package com.pcare.common.entity;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author: gl
 * @CreateDate: 2019/11/26
 * @Description: 血压实体类
 */
@Entity
public class BPMEntity {
    private String userId;//用户ID
    private String bpmId;//血压记录ID
    private String systolicData; //高压值
    private String diastolicData;//低压值
    private String meanAPData;   //平均压
    private String unit;  //血压单位
    private String pulseData; //脉搏
    private Date timeData;//时间
    private int result;//用户结果类型，正常，偏高，偏低等
    private int sport;
    private int emptiness;
    private String robotId;
    @Generated(hash = 1921063807)
    public BPMEntity(String userId, String bpmId, String systolicData,
            String diastolicData, String meanAPData, String unit, String pulseData,
            Date timeData, int result, int sport, int emptiness, String robotId) {
        this.userId = userId;
        this.bpmId = bpmId;
        this.systolicData = systolicData;
        this.diastolicData = diastolicData;
        this.meanAPData = meanAPData;
        this.unit = unit;
        this.pulseData = pulseData;
        this.timeData = timeData;
        this.result = result;
        this.sport = sport;
        this.emptiness = emptiness;
        this.robotId = robotId;
    }
    @Generated(hash = 549838386)
    public BPMEntity() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSystolicData() {
        return this.systolicData;
    }
    public void setSystolicData(String systolicData) {
        this.systolicData = systolicData;
    }
    public String getDiastolicData() {
        return this.diastolicData;
    }
    public void setDiastolicData(String diastolicData) {
        this.diastolicData = diastolicData;
    }
    public String getMeanAPData() {
        return this.meanAPData;
    }
    public void setMeanAPData(String meanAPData) {
        this.meanAPData = meanAPData;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getPulseData() {
        return this.pulseData;
    }
    public void setPulseData(String pulseData) {
        this.pulseData = pulseData;
    }
    public Date getTimeData() {
        return this.timeData;
    }
    public void setTimeData(Date timeData) {
        this.timeData = timeData;
    }
    public String getBpmId() {
        return this.bpmId;
    }
    public void setBpmId(String bpmId) {
        this.bpmId = bpmId;
    }

    private String getStatus(){
        return "1";
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

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    @Override
    public String toString() {
        return "{\"userId\":\"" + getUserId() +
                "\",\"bpmId\":\"" + getBpmId() +
                "\",\"systolicdata\":\"" + getSystolicData() +
                "\",\"diastolicdata\":\"" + getDiastolicData() +
                "\",\"meanAPData\":\"" + getMeanAPData() +
                "\",\"unit\":\"" + getUnit() +
                "\",\"pulseData\":\"" + getPulseData() +
                "\",\"timeData\":\"" + getTimeData() +
                "\",\"status\":\"" + getStatus() +
                "\"}";
    }
}
