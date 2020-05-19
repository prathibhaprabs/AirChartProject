package com.mumayank.airchart.charts.bar

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.mumayank.airchart.R
import com.mumayank.airchart.data_classes.AdditionalValue
import com.mumayank.airchart.data_classes.Value
import com.mumayank.airchart.util.AirChartUtil
import com.mumayank.airchart.util.AirChartUtil.Companion.STANDARD_BAR_WIDTH
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.air_chart_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class AirChartCombined {

    interface ICombined {
        fun getTitle(): String
        fun getXAxisTitle(): String
        fun getXAxisLabels(): java.util.ArrayList<String>
        fun getYLeftAxisTitle(): String
        fun getYRightAxisTitle(): String
        fun getYAxisBarValues(): java.util.ArrayList<Value>
        fun getYAxisLineValues(): java.util.ArrayList<Value>

        fun getColors(): ArrayList<String>? {
            return null
        }

        fun getSubTitle(): String? {
            return ""
        }

        fun getDecimalFormatPattern(): String? {
            return "0.#"
        }

        fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
            return null
        }

        fun getCustomViewLayoutResId(): Int? {
            return null
        }

        fun getIsAnimationRequired(): Boolean? {
            return false
        }

        fun onValueSelected(e: Entry?) {
            return
        }

        fun onNoValueSelected() {
            return
        }

        fun getIsLineCurved(): Boolean? {
            return false
        }
    }

    companion object {

        fun show(
            activity: Activity,
            layoutInflater: LayoutInflater?,
            chartHolderViewGroup: ViewGroup?,
            iCombined: ICombined,
            getCombinedChart: ((lineChart: CombinedChart) -> Unit)? = null
        ) {
            // make chart
            val combinedChart = CombinedChart(activity)
            val airChartValueItems = arrayListOf<Value>()

            val airChartBarValueItems = arrayListOf<Value>()
            val airChartLineValueItems = arrayListOf<Value>()

            // do in BG
            AirCoroutine.execute(activity, object : AirCoroutine.Callback {

                var valuesCount = 0
                val colors = arrayListOf<Int>()
                val xLabels = arrayListOf<String>()

                override suspend fun doTaskInBg(viewModel: ViewModel): Boolean? {

                    // add data
                    for (i in iCombined.getYAxisBarValues()) {
                        airChartBarValueItems.add(i)
                    }

                    // add data
                    for (i in iCombined.getYAxisLineValues()) {
                        airChartLineValueItems.add(i)
                    }

                    // add x labels
                    for (i in iCombined.getXAxisLabels()) {
                        xLabels.add(i)
                    }

                    // transform colors
                    if (iCombined.getColors() == null) {
                        colors.add(activity.resources.getColor(android.R.color.holo_orange_light))
                        colors.add(activity.resources.getColor(android.R.color.holo_blue_light))
                    } else {
                        for (color in iCombined.getColors()!!) {
                            colors.add(Color.parseColor(color))
                        }
                    }

                    // label count
                    valuesCount = AirChartUtil.getValuesCount(valuesCount, airChartBarValueItems)
                    valuesCount = AirChartUtil.getValuesCount(valuesCount, airChartLineValueItems)

                    // setup data
                    val combinedData = CombinedData()
                    val lineDataSetList = arrayListOf<LineDataSet>()
                    val barDataSetList = arrayListOf<BarDataSet>()

                    for ((index, value) in airChartLineValueItems.withIndex()) {
                        val entries = arrayListOf<Entry>()
                        for ((index2, value2) in value.values.withIndex()) {
                            entries.add(Entry(index2.toFloat(), value2.toFloat()))
                        }
                        val lineDataSet = LineDataSet(entries, value.legendLabel)

//                        lineDataSet.color = Color.rgb(0, 100, 0)
//                        lineDataSet.lineWidth = 2.5f
//                        lineDataSet.setCircleColor(Color.rgb(0, 100, 0))
//                        lineDataSet.circleRadius = 5f
//                        lineDataSet.fillColor = Color.rgb(0, 100, 0)
//                        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
//                        lineDataSet.setDrawValues(true)
//                        lineDataSet.valueTextSize = 10f
//                        lineDataSet.valueTextColor = Color.rgb(0, 100, 0)
//                        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT


                        lineDataSet.valueTextSize = 12f
                        lineDataSet.valueTextColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        if (iCombined.getColors().isNullOrEmpty().not()) {
                            if (airChartLineValueItems.size > 1) {
                                lineDataSet.color =
                                    AirChartUtil.getItemFromArrayAtIndexCyclically(colors, index)
                            } else {
                                lineDataSet.colors = colors
                            }
                        } else {
                            lineDataSet.color =
                                ContextCompat.getColor(
                                    activity,
                                    android.R.color.holo_blue_light
                                )
                        }

                        if (iCombined.getIsLineCurved() == true) {
                            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                        }

                        lineDataSet.setDrawCircleHole(false)
                        lineDataSet.lineWidth = 2f
                        lineDataSet.circleRadius = 2f
                        lineDataSet.setCircleColor(
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )
                        )
                        lineDataSet.setDrawValues(false)
                        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT

                        lineDataSetList.add(lineDataSet)

                        lineDataSet.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val decimalFormatPattern =
                                    iCombined.getDecimalFormatPattern() ?: "0.#"
                                val mFormat = DecimalFormat(decimalFormatPattern)
                                return mFormat.format(value.toDouble())
                            }
                        }
                    }

                    for ((index, value) in airChartBarValueItems.withIndex()) {
                        val barEntries = arrayListOf<BarEntry>()
                        for ((index2, value2) in value.values.withIndex()) {
                            barEntries.add(BarEntry(index2.toFloat(), value2.toFloat()))
                        }
                        val barDataSet = BarDataSet(barEntries, value.legendLabel)

//                        barDataSet.color = Color.rgb(60, 220, 78)
//                        barDataSet.valueTextColor = Color.rgb(60, 220, 78)
//                        barDataSet.valueTextSize = 10f
//                        barDataSet.axisDependency = YAxis.AxisDependency.RIGHT


                        barDataSet.valueTextSize = 12f
                        barDataSet.valueTextColor =
                            ContextCompat.getColor(
                                activity,
                                android.R.color.black
                            )

                        barDataSet.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val decimalFormatPattern =
                                    iCombined.getDecimalFormatPattern() ?: "0.#"
                                val mFormat = DecimalFormat(decimalFormatPattern)
                                return mFormat.format(value.toDouble())
                            }
                        }

                        if (iCombined.getColors().isNullOrEmpty().not()) {
                            if (airChartValueItems.size > 1) {
                                barDataSet.color =
                                    AirChartUtil.getItemFromArrayAtIndexCyclically(colors, index)
                            } else {
                                barDataSet.colors = colors
                            }
                        } else {
                            barDataSet.color =
                                ContextCompat.getColor(
                                    activity,
                                    android.R.color.holo_orange_light
                                )
                        }

                        barDataSet.setDrawValues(false)

                        val barData = BarData(barDataSet)
                        when (valuesCount) {
                            1 -> {
                                barData.barWidth = 0.4f
                            }
                            2 -> {
                                barData.barWidth = 0.4f
                            }
                            3 -> {
                                barData.barWidth = 0.4f
                            }
                            else -> {
                                barData.barWidth = STANDARD_BAR_WIDTH
                            }
                        }

                        barDataSet.axisDependency = YAxis.AxisDependency.RIGHT
                        barDataSetList.add(barDataSet)
                    }

                    val barData = BarData(barDataSetList.toList())
                    val lineData = LineData(lineDataSetList.toList())

                    combinedData.setData(barData)
                    combinedData.setData(lineData)
                    combinedChart.data = combinedData

                    combinedChart.setDrawMarkers(true)

                    combinedChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                    combinedChart.xAxis?.textColor =
                        ContextCompat.getColor(activity, android.R.color.black)

                    combinedChart.xAxis?.setDrawAxisLine(true)
                    combinedChart.xAxis?.setDrawGridLines(true)
                    combinedChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
                    combinedChart.xAxis?.isGranularityEnabled = true
                    combinedChart.xAxis?.granularity = 1f
                    combinedChart.xAxis?.setDrawLabels(true)
                    combinedChart.xAxis.labelRotationAngle = -90f
                    combinedChart.xAxis.enableGridDashedLine(8f, 10f, 0f)
                    combinedChart.xAxis.gridColor =
                        ContextCompat.getColor(
                            activity,
                            R.color.colorLightGray
                        )
                    combinedChart.xAxis.textSize = 12f
                    combinedChart.xAxis.setAvoidFirstLastClipping(false)
                    combinedChart.xAxis.labelCount = if (xLabels.size > 20) 20 else xLabels.size

                    combinedChart.xAxis.axisLineColor =
                        ContextCompat.getColor(
                            activity,
                            android.R.color.black
                        )

                    if (barData.colors.isNotEmpty())
                        combinedChart.axisRight.axisLineColor = barData.colors[0]
                    if (lineData.colors.isNotEmpty())
                        combinedChart.axisLeft.axisLineColor = lineData.colors[0]

                    applyAxisSettings(combinedChart.axisLeft, activity)
                    applyAxisSettings(combinedChart.axisRight, activity)

                    combinedChart.renderer =
                        CustomCombinedChartRenderer(
                            combinedChart,
                            combinedChart.animator,
                            combinedChart.viewPortHandler
                        )

                    // set zoom operations

                    combinedChart.onChartGestureListener = object : OnChartGestureListener {
                        override fun onChartGestureEnd(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {
                            // do nothing
                        }

                        override fun onChartFling(
                            me1: MotionEvent?,
                            me2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                        ) {
                            // do nothing
                        }

                        override fun onChartSingleTapped(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartGestureStart(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {
                            // do nothing
                        }

                        override fun onChartScale(
                            me: MotionEvent?,
                            scaleX: Float,
                            scaleY: Float
                        ) {
                            AirChartUtil.drawValuesAccordinglyInCombinedChart(combinedChart)
                        }

                        override fun onChartLongPressed(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartDoubleTapped(me: MotionEvent?) {
                            // do nothing
                        }

                        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                            // do nothing
                        }

                    }

                    combinedChart.setExtraOffsets(0f, 16f, 0f, 16f)

                    // setup chart
                    combinedChart.description = null
                    combinedChart.setVisibleXRangeMinimum(2f)
                    combinedChart.setNoDataText("No data to display")
                    combinedChart.setGridBackgroundColor(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.white
                        )
                    )
                    combinedChart.setPinchZoom(false)
                    combinedChart.isDoubleTapToZoomEnabled = false
                    combinedChart.setDrawBorders(false)
                    combinedChart.setBorderWidth(.4f)
                    combinedChart.dragDecelerationFrictionCoef = 1f
                    combinedChart.isAutoScaleMinMaxEnabled = false
                    combinedChart.setHardwareAccelerationEnabled(false)
                    combinedChart.isFocusableInTouchMode = false
                    combinedChart.isFocusable = false
                    combinedChart.isLongClickable = false
                    combinedChart.isHighlightPerDragEnabled = false
                    combinedChart.extraBottomOffset = 8f
                    combinedChart.legend?.isEnabled = false

                    //fixme
                    /*
                    combinedChart.setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            iCombined.onNoValueSelected()
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) {
                                return
                            }
                            combinedChart.highlightValues(null)
                            iCombined.onValueSelected(e)
                        }
                    })
                    */

                    // setup spacing
                    val times = 0.5f

                    //line spacing
                    val lineMin = if (lineData.yMin < 0) lineData.yMin else 0f
                    val lineMax = if (lineData.yMax > 0) lineData.yMax else 0f
                    combinedChart.axisLeft.axisMaximum =
                        lineMax + (lineMax - lineMin).absoluteValue * times

                    if (lineMin < 0) {
                        // limit line not required for line chart
//                        val limitLine = LimitLine(0f, "")
//                        limitLine.lineColor = Color.BLACK
//                        limitLine.lineWidth = 0.5f
//                        combinedChart.axisLeft.addLimitLine(limitLine)

                        combinedChart.axisLeft.axisMinimum = lineMin + (lineMin - lineMax) * times
                    } else {
                        combinedChart.axisLeft.axisMinimum = 0f
                    }

                    //bar spacing
                    val barMin = if (barData.yMin < 0) barData.yMin else 0f
                    val barMax = if (barData.yMax > 0) barData.yMax else 0f
                    combinedChart.axisRight.axisMaximum =
                        barMax + (barMax - barMin).absoluteValue * times //bar data axis

                    if (barMin < 0) {
                        val limitLine = LimitLine(0f, "")
                        limitLine.lineColor = Color.BLACK
                        limitLine.lineWidth = 0.5f
                        combinedChart.axisRight.addLimitLine(limitLine)
                        combinedChart.axisRight.axisMinimum = barMin + (barMin - barMax) * times
                    } else {
                        combinedChart.axisRight.axisMinimum = 0f
                    }


                    combinedChart.xAxis?.axisMinimum = combinedData.xMin - 0.5f
                    combinedChart.xAxis?.axisMaximum = combinedData.xMax + 0.5f

                    combinedChart.setMaxVisibleValueCount(20)
                    combinedChart.drawOrder = arrayOf(DrawOrder.BAR, DrawOrder.LINE)

                    return true
                }

                override fun isTaskTypeCalculation(): Boolean {
                    return true
                }

                override fun onFailure(viewModel: ViewModel) {
                    // do nothing
                }

                override fun onSuccess(viewModel: ViewModel) {

                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                        // setup views
                        chartHolderViewGroup?.title?.text =
                            iCombined.getTitle()
                        chartHolderViewGroup?.subTitle?.text =
                            iCombined.getSubTitle()
                        chartHolderViewGroup?.title?.visibility =
                            if (iCombined.getTitle().isEmpty()) View.GONE else View.VISIBLE
                        chartHolderViewGroup?.subTitle?.visibility =
                            if (iCombined.getSubTitle()
                                    ?.isEmpty() == false
                            ) View.VISIBLE else View.GONE
                        chartHolderViewGroup?.yLabelRightLayout?.visibility =
                            if (iCombined.getYRightAxisTitle() == null) View.GONE else View.VISIBLE

                        // add chart
                        chartHolderViewGroup?.xLabel?.text = iCombined.getXAxisTitle()
                        val yLabelLeft =
                            chartHolderViewGroup?.yLabelLeft as TextView?
                        yLabelLeft?.text = iCombined.getYLeftAxisTitle()

                        if (iCombined.getYRightAxisTitle() != null) {
                            val yLabelRight = chartHolderViewGroup?.yLabelRight as TextView?
                            yLabelRight?.text = iCombined.getYRightAxisTitle()
                        }
                        combinedChart.setScaleEnabled(true)
                        combinedChart.isScaleYEnabled = false
                        if (valuesCount > 15) {
                            combinedChart.zoom(valuesCount / 15f, 1f, 0f, 0f)
                        } else {
                            combinedChart.zoom(1f, 1f, 0f, 0f)
                        }
                        AirChartUtil.drawValuesAccordinglyInCombinedChart(combinedChart)

                        chartHolderViewGroup?.chart?.removeAllViews()
                        chartHolderViewGroup?.chart?.addView(
                            combinedChart,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )

                        // check for no data
                        chartHolderViewGroup?.noData?.visibility =
                            if (valuesCount == 0) View.VISIBLE else View.GONE

                        combinedChart.isDragEnabled = true

                        // setup legend
                        val colorsTemp = arrayListOf<Int>()
                        for (i in colors.size - 1 downTo 0) {
                            colorsTemp.add(colors[i])
                        }

                        airChartValueItems.addAll(airChartBarValueItems)
                        airChartValueItems.addAll(airChartLineValueItems)

                        AirChartUtil.setupLegend(
                            activity,
                            colors,
                            chartHolderViewGroup?.rvHolderLegends,
                            airChartValueItems,
                            false
                        )

                        // setup additional data
                        AirChartUtil.setupAdditionalData(
                            activity,
                            chartHolderViewGroup?.rvHolderAdditionalData,
                            iCombined.getAdditionalValues()
                        )

                        // setup custom view
                        AirChartUtil.setupCustomView(
                            activity,
                            layoutInflater,
                            iCombined.getCustomViewLayoutResId(),
                            chartHolderViewGroup?.customView
                        )

                        chartHolderViewGroup?.progressView?.visibility = View.GONE

                        if (iCombined.getIsAnimationRequired() == true) {
                            combinedChart.animateXY(
                                AirChartUtil.ANIMATION_TIME,
                                AirChartUtil.ANIMATION_TIME
                            )
                        } else {
                            combinedChart.invalidate()
                        }
                        getCombinedChart?.invoke(combinedChart)
                    }
                }
            })
        }

        private fun applyAxisSettings(axis: YAxis?, activity: Activity) {

            axis?.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
            axis?.textColor =
                ContextCompat.getColor(activity, android.R.color.black)
            axis?.isGranularityEnabled = true
            axis?.granularity = 1f
            axis?.textSize = 12f
            axis?.axisMinimum = 0f
            axis?.setDrawTopYLabelEntry(true)
            axis?.setDrawAxisLine(true)
            axis?.setDrawLabels(true)
            axis?.setDrawGridLines(false)
            axis?.axisLineWidth = 3f
        }
    }
}