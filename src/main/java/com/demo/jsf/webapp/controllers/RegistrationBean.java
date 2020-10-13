package com.demo.jsf.webapp.controllers;

import com.demo.jsf.webapp.dao.UserManagementDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "registration")
@ViewScoped
public class RegistrationBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBean.class);

    @ManagedProperty(value = "#{userManagementDAO}")
    transient private UserManagementDAO userDao;
    private String userName;
    private String operationMessage;

    private Integer num1;
    private Integer num2;

    public String getStrToOut() {
        return strToOut;
    }

    public void setStrToOut(String strToOut) {
        this.strToOut = strToOut;
    }

    private String strToOut;

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }


    public void createNewUser() {
        try {
            LOGGER.info("Creating new user");
            FacesContext context = FacesContext.getCurrentInstance();
            boolean operationStatus = userDao.createUser(userName);
            context.isValidationFailed();
            if (operationStatus) {
                operationMessage = "User " + userName + " created";
            }
        } catch (Exception ex) {
            LOGGER.error("Error registering new user ");
            ex.printStackTrace();
            operationMessage = "Error " + userName + " not created";
        }
    }

    public void clearUsers(){
        userDao.clearUsers();
    }

    public void add() {
        operationMessage = "Vysledok je " + (num2 + num1);
    }

    public String printStr() {
        return strToOut;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDao(UserManagementDAO userDao) {
        this.userDao = userDao;
    }

    public UserManagementDAO getUserDao() {
        return this.userDao;
    }

    public String getOperationMessage() {
        return operationMessage;
    }

    public void setOperationMessage(String operationMessage) {
        this.operationMessage = operationMessage;
    }
}
