package com.jabwrb.nutridiary.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Food implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String brand;

    private int servingSizeUnit;

    private String servingSizeMeasurement;

    private int calories;

    private float fat;

    private float cholesterol;

    private float sodium;

    private float saturatedFat;

    private float carbohydrates;

    private float dietaryFiber;

    private float sugars;

    private float protein;

    public Food() {
    }

    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        brand = in.readString();
        servingSizeUnit = in.readInt();
        servingSizeMeasurement = in.readString();
        calories = in.readInt();
        fat = in.readFloat();
        cholesterol = in.readFloat();
        sodium = in.readFloat();
        saturatedFat = in.readFloat();
        carbohydrates = in.readFloat();
        dietaryFiber = in.readFloat();
        sugars = in.readFloat();
        protein = in.readFloat();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(int servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    public String getServingSizeMeasurement() {
        return servingSizeMeasurement;
    }

    public void setServingSizeMeasurement(String servingSizeMeasurement) {
        this.servingSizeMeasurement = servingSizeMeasurement;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(float saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(float dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public float getSugars() {
        return sugars;
    }

    public void setSugars(float sugars) {
        this.sugars = sugars;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(brand);
        parcel.writeInt(servingSizeUnit);
        parcel.writeString(servingSizeMeasurement);
        parcel.writeInt(calories);
        parcel.writeFloat(fat);
        parcel.writeFloat(cholesterol);
        parcel.writeFloat(sodium);
        parcel.writeFloat(saturatedFat);
        parcel.writeFloat(carbohydrates);
        parcel.writeFloat(dietaryFiber);
        parcel.writeFloat(sugars);
        parcel.writeFloat(protein);
    }
}
