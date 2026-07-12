/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MITUSER-2
 */
public class Department {
    private String department_name;
    private int department_id;

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public Department(int department_id, String department_name) {
        this.department_name = department_name;
        this.department_id = department_id;
    }

    @Override
    public String toString() {
        return  department_name ;
    }

   
    
}
