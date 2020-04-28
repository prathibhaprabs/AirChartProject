package com.mumayank.airchart.data_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Combined {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("xAxisTitle")
    @Expose
    private String xAxisTitle;

    @SerializedName("xAxisLabels")
    @Expose
    private ArrayList<String> xAxisLabels = null;

    @SerializedName("yLeftAxisTitle")
    @Expose
    private String yLeftAxisTitle;

    @SerializedName("yRightAxisTitle")
    @Expose
    private String yRightAxisTitle;

    @SerializedName("yAxisBarValues")
    @Expose
    private ArrayList<Value> yAxisBarValues = null;

    @SerializedName("yAxisLineValues")
    @Expose
    private ArrayList<Value> yAxisLineValues = null;

    @SerializedName("colors")
    @Expose
    private ArrayList<String> colors = null;

    @SerializedName("subTitle")
    @Expose
    private String subTitle;

    @SerializedName("decimalFormatPattern")
    @Expose
    private String decimalFormatPattern;

    @SerializedName("additionalValues")
    @Expose
    private ArrayList<AdditionalValue> additionalValues = null;

    @SerializedName("isAnimationRequired")
    @Expose
    private Boolean isAnimationRequired;

    @SerializedName("isLineCurved")
    @Expose
    private Boolean isLineCurved;

    /**
     * No args constructor for use in serialization
     */
    public Combined() {
    }

    public Combined(String title, String xAxisTitle, ArrayList<String> xAxisLabels, String yLeftAxisTitle,String yRightAxisTitle,
                    ArrayList<Value> yAxisBarValues, ArrayList<Value> yAxisLineValues, ArrayList<String> colors,
                    String subTitle, String decimalFormatPattern, ArrayList<AdditionalValue> additionalValues,
                    Boolean isAnimationRequired, Boolean isLineCurved) {
        this.title = title;
        this.xAxisTitle = xAxisTitle;
        this.xAxisLabels = xAxisLabels;
        this.yLeftAxisTitle = yLeftAxisTitle;
        this.yRightAxisTitle = yRightAxisTitle;
        this.yAxisBarValues = yAxisBarValues;
        this.yAxisLineValues = yAxisLineValues;
        this.colors = colors;
        this.subTitle = subTitle;
        this.decimalFormatPattern = decimalFormatPattern;
        this.additionalValues = additionalValues;
        this.isAnimationRequired = isAnimationRequired;
        this.isLineCurved = isLineCurved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public ArrayList<String> getXAxisLabels() {
        return xAxisLabels;
    }

    public void setXAxisLabels(ArrayList<String> xAxisLabels) {
        this.xAxisLabels = xAxisLabels;
    }

    public String getYLeftAxisTitle() {
        return yLeftAxisTitle;
    }

    public void setYLeftAxisTitle(String yLeftAxisTitle) {
        this.yLeftAxisTitle = yLeftAxisTitle;
    }

    public ArrayList<Value> getYLeftAxisValues() {
        return yAxisBarValues;
    }

    public void setYLeftAxisValues(ArrayList<Value> yLeftAxisValues) {
        this.yAxisBarValues = yLeftAxisValues;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDecimalFormatPattern() {
        return decimalFormatPattern;
    }

    public void setDecimalFormatPattern(String decimalFormatPattern) {
        this.decimalFormatPattern = decimalFormatPattern;
    }

    public ArrayList<AdditionalValue> getAdditionalValues() {
        return additionalValues;
    }

    public void setAdditionalValues(ArrayList<AdditionalValue> additionalValues) {
        this.additionalValues = additionalValues;
    }

    public Boolean getIsAnimationRequired() {
        return isAnimationRequired;
    }

    public void setIsAnimationRequired(Boolean isAnimationRequired) {
        this.isAnimationRequired = isAnimationRequired;
    }

    public String getYRightAxisTitle() {
        return yRightAxisTitle;
    }

    public void setYRightAxisTitle(String yRightAxisTitle) {
        this.yRightAxisTitle = yRightAxisTitle;
    }

    public ArrayList<Value> getYAxisBarValues() {
        return yAxisBarValues;
    }

    public void setYAxisBarValues(ArrayList<Value> yAxisBarValues) {
        this.yAxisBarValues = yAxisBarValues;
    }

    public ArrayList<Value> getYAxisLineValues() {
        return yAxisLineValues;
    }

    public void setYAxisLineValues(ArrayList<Value> yAxisLineValues) {
        this.yAxisLineValues = yAxisLineValues;
    }

    public Boolean getIsLineCurved() {
        return isLineCurved;
    }

    public void setIsLineCurved(Boolean lineCurved) {
        isLineCurved = lineCurved;
    }
}