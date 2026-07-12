/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MITUSER-2
 */
 
public class Position {
    private String position_name;
    private int position_id;

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public Position(int position_id, String position_name) {
        this.position_name = position_name;
        this.position_id = position_id;
    }

    @Override
    public String toString() {
        return  position_name;
    }

   
    
}
