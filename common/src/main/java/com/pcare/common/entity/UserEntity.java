package com.pcare.common.entity;

import android.text.TextUtils;

import com.pcare.common.util.CommonUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Author: gl
 * @CreateDate: 2019/11/20
 * @Description:
 */
@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private Long id;
    private String userId;//用户ID
    private String name;//用户名，唯一
    private String nickname;//昵称
    private String password;
    private int type;//用户类型：爷爷:0，奶奶:1，叔叔:2，阿姨:3等
    private int gender;//性别 男：0，女：1
    private int birth_year;//出生年份: 1999
    private int height;//身高: 173cm
    private int weight;//体重：50kg
    private String robot_id;//用户使用的机器人ID号或设备号
    private String disease_history;//用户病史
    private boolean currentUser;//当前使用用户
    @Generated(hash = 58266905)
    public UserEntity(Long id, String userId, String name, String nickname,
            String password, int type, int gender, int birth_year, int height,
            int weight, String robot_id, String disease_history,
            boolean currentUser) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.type = type;
        this.gender = gender;
        this.birth_year = birth_year;
        this.height = height;
        this.weight = weight;
        this.robot_id = robot_id;
        this.disease_history = disease_history;
        this.currentUser = currentUser;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getUserType() {
        return this.type;
    }
    public void setUserType(int userType) {
        this.type = userType;
    }
    public int getUserBirthYear() {
        return this.birth_year;
    }
    public void setUserBirthYear(int userBirthYear) {
        this.birth_year = userBirthYear;
    }
    public int getUserGender() {
        return this.gender;
    }
    public void setUserGender(int userGender) {
        this.gender = userGender;
    }
    public String getUserName() {
        return this.name;
    }
    public void setUserName(String username) {
        this.name = username;
    }
    public int getUserStature() {
        return this.height;
    }
    public void setUserStature(int userStature) {
        this.height = userStature;
    }
    public int getUserWeight() {
        return this.weight;
    }
    public void setUserWeight(int userWeight) {
        this.weight = userWeight;
    }
    public String getUserRobotId() {
        if(TextUtils.isEmpty(this.robot_id))
            this.robot_id = "0";
        return this.robot_id;
    }
    public void setUserRobotId(String userRobotId) {
        this.robot_id = userRobotId;
    }

    public String getPassword(){
        return "123456";
    }
    public int getStatus(){
        return 1;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getUserHistoty() {
        return disease_history;
    }

    public void setUserHistoty(String userHistoty) {
        this.disease_history = userHistoty;
    }


    public boolean getCurrentUser() {
        return this.currentUser;
    }
    public UserEntity setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
        return this;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.name;
    }
    public void setUsername(String username) {
        this.name = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return CommonUtil.entityToJson(this,new String[]{"currentUser"});
//        return "UserEntity{" +
//                "id=" + id +
//                ", userId='" + userId + '\'' +
//                ", name='" + name + '\'' +
//                ", nickname='" + nickname + '\'' +
//                ", password='" + password + '\'' +
//                ", type=" + type +
//                ", gender=" + gender +
//                ", birth_year=" + birth_year +
//                ", height='" + height + '\'' +
//                ", weight='" + weight + '\'' +
//                ", robot_id='" + robot_id + '\'' +
//                ", disease_history='" + disease_history + '\'' +
//                ", currentUser=" + currentUser +
//                '}';
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getGender() {
        return this.gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getBirth_year() {
        return this.birth_year;
    }
    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getRobot_id() {
        return this.robot_id;
    }
    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }
    public String getDisease_history() {
        return this.disease_history;
    }
    public void setDisease_history(String disease_history) {
        this.disease_history = disease_history;
    }
}
