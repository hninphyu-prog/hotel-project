/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import java.sql.ResultSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author MITUSER-2
 */

public class Employee {
    
    
    
    
    
    
    
    private int Eid;
    private String Empname;
    private int Pos_id;
    private int Dep_id;
    private ImageView imgView;
    private String image_name;
    /// for Employee detail model
    private String nrc;
    private String gender;
    private String dob;
    private String phno;
    private String gmail;
    private String address;
    private int salary;
    private String hiredate;
    private String leavedate;
    private String status;
   private String Pos_id1;

    public Employee(int Eid, String Empname, int Pos_id, int Dep_id, String image_name) {
        this.Eid = Eid;
        this.Empname = Empname;
        this.Pos_id = Pos_id;
        this.Dep_id = Dep_id;
        this.image_name = image_name;
    }
   

   
    
      

    public Employee(int Eid, String Empname, String nrc, String gender, String dob, String phno, String gmail, int Pos_id, int Dep_id, int salary,  String hiredate, String leavedate,String image_name, String status,String address) {
        this.Eid = Eid;
        this.Empname = Empname;
        //this.imgView = imgView;
        this.nrc = nrc;
        this.gender = gender;
        this.dob = dob;
        this.phno = phno;
        this.gmail = gmail;
        this.Pos_id = Pos_id;
        this.Dep_id = Dep_id;
        this.salary = salary;
        this.hiredate = hiredate;
        this.leavedate = leavedate;
        this.image_name = image_name;
        this.status = status;
        this.address = address;
        File file=new File("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\"+image_name);
        Image img=new Image(file.toURI().toString());//THIS IS FOR input in table imagePICTURE
        
        imgView =new ImageView(img);
        imgView.setFitWidth(235);
        imgView.setFitHeight(165);
        imgView.setPreserveRatio(true);
    }

    public int getEid() {
        return Eid;
    }

    public void setEid(int Eid) {
        this.Eid = Eid;
    }




   
    
    /// for Employee detail model
    public String getNrc() {
        return nrc;
        
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public String getLeavedate() {
        return leavedate;
    }

    public void setLeavedate(String leavedate) {
        this.leavedate = leavedate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /// for Employee detail model
    public Employee(int Eid, String  Empname, int Pos_id,int Dep_id, String image_name,String status) {
        this.Eid = Eid;
        this.Empname = Empname;
        this.Pos_id = Pos_id;
         this.Dep_id = Dep_id;
      
        this.status = status;
        this.image_name=image_name;
        
        
        
        File file=new File("C:\\Users\\MITUSER-2\\Documents\\NetBeansProjects\\JavaFXApplication4\\src\\image\\"+image_name);
        Image img=new Image(file.toURI().toString());//THIS IS FOR input in table imagePICTURE
        
        imgView =new ImageView(img);
        imgView.setFitWidth(235);
        imgView.setFitHeight(165);
        imgView.setPreserveRatio(true);
    }

    public Employee(int Eid, String Empname, String Pos_id1) {
        this.Eid = Eid;
        this.Empname = Empname;
        this.Pos_id1 = Pos_id1;
    }

    public String getPos_id1() {
        return Pos_id1;
    }

    public void setPos_id1(String Pos_id1) {
        this.Pos_id1 = Pos_id1;
    }

    public Employee(String Empname, int Dep_id, String image_name, String nrc, String gender, String dob, String phno, String gmail, String address, String hiredate) {
        this.Empname = Empname;
        this.Dep_id = Dep_id;
        this.image_name = image_name;
        this.nrc = nrc;
        this.gender = gender;
        this.dob = dob;
        this.phno = phno;
        this.gmail = gmail;
        this.address = address;
        this.hiredate = hiredate;
    }
    
    

   
    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
   


    

    public String getEmpname() {
        return Empname;
    }

    public void setEmpname(String Empname) {
        this.Empname = Empname;
    }

    public int getPos_id() {
        return Pos_id;
    }

    public void setPos_id(int Pos_id) {
        this.Pos_id = Pos_id;
    }

    public int getDep_id() {
        return Dep_id;
    }

    public void setDep_id(int Dep_id) {
        this.Dep_id = Dep_id;
    }

    
    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public Object getName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getEmpid() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*public Integer getPosition_id() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Integer getDepartment_id() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/

   
    
}
