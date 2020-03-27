/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pcare.common.entity;


import com.pcare.common.util.CommonUtil;

import org.greenrobot.greendao.annotation.Entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class GlucoseEntity {
	private String id;//血糖记录ID
	private String user_id;//用户ID
	private String sequence_num;//序列号
	private String check_time;//时间
	private double glucose;//血糖值，默认0
	private String unit;//单位
	private int sample_type;//测量类型，默认0
	private int sample_location;//测量位置，默认0
	private int status = 1;//状态码，默认0
	private int result;
	private int sport;
	private int emptiness;
	private String robot_id;


	@Generated(hash = 1174760670)
	public GlucoseEntity(String id, String user_id, String sequence_num, String check_time, double glucose, String unit, int sample_type,
			int sample_location, int status, int result, int sport, int emptiness, String robot_id) {
		this.id = id;
		this.user_id = user_id;
		this.sequence_num = sequence_num;
		this.check_time = check_time;
		this.glucose = glucose;
		this.unit = unit;
		this.sample_type = sample_type;
		this.sample_location = sample_location;
		this.status = status;
		this.result = result;
		this.sport = sport;
		this.emptiness = emptiness;
		this.robot_id = robot_id;
	}

	@Generated(hash = 1590621335)
	public GlucoseEntity() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof GlucoseEntity) {
			return this.getCheck_time().equals(((GlucoseEntity) obj).getCheck_time())
					&& this.getGlucose()==((GlucoseEntity) obj).getGlucose();
		}
		return false;
	}

	@Override
	public String toString() {
		return CommonUtil.entityToJson(this);
//		return "{\"user_id\":\"" + getUserId() +
//				"\",\"id\":\"" + getGluId() +
//				"\",\"sequence_num\":\"" + getSequenceNumber() +
//				"\",\"check_time\":\"" + getTimeDate() +
//				"\",\"glucose\":\"" + getGlucoseConcentration() +
//				"\",\"sample_type\":\"" + getSampleType() +
//				"\",\"sample_location\":\"" + getSampleLocation() +
//				"\",\"status\":\"" + getStatus() +
//				"\"}";
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_d() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSequence_num() {
		return this.sequence_num;
	}

	public void setSequence_num(String sequence_num) {
		this.sequence_num = sequence_num;
	}

	public String getCheck_time() {
		return this.check_time;
	}

	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}

	public double getGlucose() {
		return this.glucose;
	}

	public void setGlucose(double glucose) {
		this.glucose = new BigDecimal(glucose * 1000.00).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public int getSample_type() {
		return this.sample_type;
	}

	public void setSample_type(int sample_type) {
		this.sample_type = sample_type;
	}

	public int getSample_location() {
		return this.sample_location;
	}

	public void setSample_location(int sample_location) {
		this.sample_location = sample_location;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
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

	public String getRobot_id() {
		return robot_id;
	}

	public void setRobot_id(String robot_id) {
		this.robot_id = robot_id;
	}

	public String getUser_id() {
		return this.user_id;
	}
}
