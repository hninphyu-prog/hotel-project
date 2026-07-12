/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Cloth {
    private int cloth_id;
    private int clothing_type_id;
    private String clothing_type;
    private String cloth_name;
    private String cloth_img;

    public Cloth(int cloth_id, int clothing_type_id, String clothing_type, String cloth_name, String cloth_img) {
        this.cloth_id = cloth_id;
        this.clothing_type_id = clothing_type_id;
        this.clothing_type = clothing_type;
        this.cloth_name = cloth_name;
        this.cloth_img = cloth_img;
    }

    public int getCloth_id() {
        return cloth_id;
    }

    public void setCloth_id(int cloth_id) {
        this.cloth_id = cloth_id;
    }

    public int getClothing_type_id() {
        return clothing_type_id;
    }

    public void setClothing_type_id(int clothing_type_id) {
        this.clothing_type_id = clothing_type_id;
    }

    public String getClothing_type() {
        return clothing_type;
    }

    public void setClothing_type(String clothing_type) {
        this.clothing_type = clothing_type;
    }

    public String getCloth_name() {
        return cloth_name;
    }

    public void setCloth_name(String cloth_name) {
        this.cloth_name = cloth_name;
    }

    public String getCloth_img() {
        return cloth_img;
    }

    public void setCloth_img(String cloth_img) {
        this.cloth_img = cloth_img;
    }
    
    
}
