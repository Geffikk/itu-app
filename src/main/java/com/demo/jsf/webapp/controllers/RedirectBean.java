package com.demo.jsf.webapp.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
@ViewScoped
public class RedirectBean {
    public RedirectBean(){}

    public void redirectMe(String str) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("/"+str+".xhtml");
    }

}
