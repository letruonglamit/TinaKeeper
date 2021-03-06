package com.example.tina.doanmang_tinakeeper.model;


import java.sql.Date;

/**
 * Created by TiNa on 06/03/2017.
 */

public class Expense {
    private int id;
    private String category;
    private String notes;
    private double money;
    private Date date;
    private int photoID;
    public Expense() {
        this.category = null;
        this.notes = null;
        this.money = 0;
        this.date = null;
    }
    public Expense(String expenseList, String notes, double money, Date date) {
        this.category = category;
        this.notes = notes;
        this.money = money;
        this.date = date;
    }

    public Expense(String category, String notes, double money, int photoID) {
        this.category = category;
        this.notes = notes;
        this.money = money;
        this.photoID = photoID;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Expense(int id, String category, String notes, double money, Date date) {
        this.id = id;
        this.category = category;
        this.notes = notes;
        this.money = money;
        this.date = date;

    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {this.date = date;}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }



}
