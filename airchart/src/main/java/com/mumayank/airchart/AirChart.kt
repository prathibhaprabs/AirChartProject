package com.mumayank.airchart

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.google.gson.Gson
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.charts.bar.AirChartCombined
import com.mumayank.airchart.charts.bar.AirChartLine
import com.mumayank.airchart.data_classes.*

class AirChart(
    val activity: Activity,
    val chartHolderViewGroup: ViewGroup?
) {

    var layoutInflater: LayoutInflater? = null

    init {
        // inflate view so that progress starts showing instantly
        layoutInflater = activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        chartHolderViewGroup?.removeAllViews()
        chartHolderViewGroup?.addView(
            layoutInflater?.inflate(
                R.layout.air_chart_view,
                LinearLayout(activity)
            ),
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun showBarChart(
        IBar: AirChartBar.IBar,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        AirChartBar.show(activity, layoutInflater, chartHolderViewGroup, IBar, getBarChart)
    }

    fun showLineChart(
        iLine: AirChartLine.ILine,
        getLineChart: ((lineChart: LineChart) -> Unit)? = null
    ) {
        AirChartLine.show(activity, layoutInflater, chartHolderViewGroup, iLine, getLineChart)
    }

    fun showCombinedChart(
        iCombined: AirChartCombined.ICombined,
        getCombinedChart: ((lineChart: CombinedChart) -> Unit)? = null
    ) {
        AirChartCombined.show(
            activity,
            layoutInflater,
            chartHolderViewGroup,
            iCombined,
            getCombinedChart
        )
    }

    fun showLineChart(
        jsonString: String,
        getLineChart: ((lineChart: LineChart) -> Unit)? = null
    ) {
        try {
            val line = Gson().fromJson(jsonString, Line::class.java)
            if (line == null) {
                throw Exception()
            } else {
                Line(
                    line.title,
                    line.xAxisTitle,
                    line.xAxisLabels,
                    line.yLeftAxisTitle,
                    line.yLeftAxisValues,
                    line.yRightAxisTitle,
                    line.yRightAxisValues,
                    line.colors,
                    line.subTitle,
                    line.decimalFormatPattern,
                    line.additionalValues,
                    line.isAnimationRequired,
                    line.isCurved
                )

                showLineChart(object : AirChartLine.ILine {

                    override fun getTitle(): String {
                        return line.title
                    }

                    override fun getXAxisTitle(): String {
                        return line.xAxisTitle
                    }

                    override fun getXAxisLabels(): ArrayList<String> {
                        return line.xAxisLabels
                    }

                    override fun getYLeftAxisTitle(): String {
                        return line.yLeftAxisTitle
                    }

                    override fun getYLeftAxisValues(): ArrayList<Value> {
                        return line.yLeftAxisValues
                    }

                    override fun getYRightAxisTitle(): String? {
                        return line.yRightAxisTitle
                    }

                    override fun getYRightAxisValues(): ArrayList<Value>? {
                        return line.yRightAxisValues
                    }

                    override fun getColors(): ArrayList<String>? {
                        return line.colors
                    }

                    override fun getSubTitle(): String? {
                        return line.subTitle
                    }

                    override fun getDecimalFormatPattern(): String? {
                        return line.decimalFormatPattern
                    }

                    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
                        return line.additionalValues
                    }

                    override fun getIsAnimationRequired(): Boolean? {
                        return line.isAnimationRequired
                    }

                    override fun getIsCurved(): Boolean? {
                        return line.isCurved
                    }
                })
            }
        } catch (e: Exception) {
            Log.e("AirChart", e.message ?: "Some error occurred")
        }
    }

    fun showBarChart(
        jsonString: String,
        getBarChart: ((barChart: BarChart) -> Unit)? = null
    ) {
        try {
            val bar = Gson().fromJson(jsonString, Bar::class.java)
            if (bar == null) {
                throw Exception()
            } else {
                Bar(
                    bar.title,
                    bar.xAxisTitle,
                    bar.xAxisLabels,
                    bar.yLeftAxisTitle,
                    bar.yLeftAxisValues,
                    bar.colors,
                    bar.subTitle,
                    bar.decimalFormatPattern,
                    bar.additionalValues,
                    bar.isHorizontal,
                    bar.isAnimationRequired
                )
                showBarChart(object : AirChartBar.IBar {

                    override fun getTitle(): String {
                        return bar.title
                    }

                    override fun getXAxisTitle(): String {
                        return bar.xAxisTitle
                    }

                    override fun getXAxisLabels(): ArrayList<String> {
                        return bar.xAxisLabels
                    }

                    override fun getYLeftAxisTitle(): String {
                        return bar.yLeftAxisTitle
                    }

                    override fun getYLeftAxisValues(): ArrayList<Value> {
                        return bar.yLeftAxisValues
                    }

                    override fun getColors(): ArrayList<String>? {
                        return bar.colors
                    }

                    override fun getSubTitle(): String? {
                        return bar.subTitle
                    }

                    override fun getDecimalFormatPattern(): String? {
                        return bar.decimalFormatPattern
                    }

                    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
                        return bar.additionalValues
                    }

                    override fun getIsHorizontal(): Boolean {
                        return bar.isHorizontal
                    }

                    override fun getIsAnimationRequired(): Boolean? {
                        return bar.isAnimationRequired
                    }

                })
            }
        } catch (e: Exception) {
            Log.e("AirChart", e.message ?: "Some error occurred")
        }
    }

    fun showCombinedChart(
        jsonString: String,
        getCombinedChart: ((combinedChart: CombinedChart) -> Unit)? = null
    ) {
        try {
            val combined = Gson().fromJson(jsonString, Combined::class.java)
            if (combined == null) {
                throw Exception()
            } else {
                Combined(
                    combined.title,
                    combined.xAxisTitle,
                    combined.xAxisLabels,
                    combined.yLeftAxisTitle,
                    combined.yRightAxisTitle,
                    combined.yAxisBarValues,
                    combined.yAxisLineValues,
                    combined.colors,
                    combined.subTitle,
                    combined.decimalFormatPattern,
                    combined.additionalValues,
                    combined.isAnimationRequired,
                    combined.isLineCurved
                )

                showCombinedChart(object : AirChartCombined.ICombined {

                    override fun getTitle(): String {
                        return combined.title
                    }

                    override fun getXAxisTitle(): String {
                        return combined.xAxisTitle
                    }

                    override fun getXAxisLabels(): ArrayList<String> {
                        return combined.xAxisLabels
                    }

                    override fun getYLeftAxisTitle(): String {
                        return combined.yLeftAxisTitle
                    }

                    override fun getYRightAxisTitle(): String {
                        return combined.yRightAxisTitle
                    }

                    override fun getYAxisBarValues(): java.util.ArrayList<Value> {
                        return combined.yAxisBarValues
                    }

                    override fun getYAxisLineValues(): java.util.ArrayList<Value> {
                        return combined.yAxisLineValues
                    }

                    override fun getColors(): ArrayList<String>? {
                        return combined.colors
                    }

                    override fun getSubTitle(): String? {
                        return combined.subTitle
                    }

                    override fun getDecimalFormatPattern(): String? {
                        return combined.decimalFormatPattern
                    }

                    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
                        return combined.additionalValues
                    }

                    override fun getIsAnimationRequired(): Boolean? {
                        return combined.isAnimationRequired
                    }

                    override fun getIsLineCurved(): Boolean? {
                        return combined.isLineCurved
                    }
                })
            }
        } catch (e: Exception) {
            Log.e("AirChart", e.message ?: "Some error occurred")
        }
    }

}