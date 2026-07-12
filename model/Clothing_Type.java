/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Clothing_Type {
    private String clothing_type;
    private String icon_name;

    public Clothing_Type(String clothing_type, String icon_name) {
        this.clothing_type = clothing_type;
        this.icon_name = icon_name;
    }

    public String getClothing_type() {
        return clothing_type;
    }

    public void setClothing_type(String clothing_type) {
        this.clothing_type = clothing_type;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }
    
    
    
}
