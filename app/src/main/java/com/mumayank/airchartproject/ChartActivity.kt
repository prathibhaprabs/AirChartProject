package com.mumayank.airchartproject

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.data_classes.Bar
import com.mumayank.airchart.data_classes.Combined
import com.mumayank.airchart.data_classes.Line
import com.mumayank.airchart.data_classes.Value
import kotlinx.android.synthetic.main.chart_activity.*
import kotlinx.android.synthetic.main.chart_rv_item.view.*
import mumayank.com.airrecyclerview.AirRv


class ChartActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_CHART_TYPE = "INTENT_EXTRA_CHART_TYPE"
        const val INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA"

        private fun getData(
            noOfLegends: Int,
            noOfDataItems: Int,
            minDataVal: Int,
            maxDataVal: Int,
            legendNum: Int? = null
        ): ArrayList<Value> {
            val legendsArrayList = java.util.ArrayList<Value>()
            for (k in 1..noOfLegends) {
                val arrayList = java.util.ArrayList<Double>()
                for (i in 1..noOfDataItems) {
                    val nextRandomFloat =
                        kotlin.random.Random.nextInt(minDataVal, maxDataVal + 1).toDouble()
                    arrayList.add(nextRandomFloat)
                }
                legendsArrayList.add(
                    Value(
                        if (legendNum == null) "legend$k" else "legend$legendNum",
                        arrayList
                    )
                )
            }
            return legendsArrayList
        }

        private fun getLabels(size: Int): ArrayList<String> {
            val arrayList = arrayListOf<String>()
            for (i in 1..size) {
                arrayList.add("val$i")
            }
            return arrayList
        }
    }

    enum class ChartType {
        BAR,
        HORIZONTAL_BAR,
        LINE,
        COMBINED
    }

    private var chartType = ChartType.BAR

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)

        val intentExtraData = intent.getStringExtra(INTENT_EXTRA_DATA) ?: ""

        if (intentExtraData.isEmpty()) {
            AssetHelper.readFile(
                this,
                "chart_data",
                fun(string: String) {
                    showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                        showChartsInternal(
                            chartLayout,
                            string
                        )
                    })
                }
            )
        } else {
            showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                showChartsInternal(
                    chartLayout,
                    intentExtraData
                )
            })
        }

        val intentExtraString = intent.getStringExtra(INTENT_EXTRA_CHART_TYPE) ?: ""
        chartType = ChartType.valueOf(intentExtraString)

        when (chartType) {
            ChartType.BAR -> {
                val barDatas = arrayListOf(

                    // todo: POSITIVE VALUES

                    BarData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    BarData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30)
                    ),
                    BarData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30)
                    ),
                    BarData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30)
                    ),
                    BarData(
                        "20 values - far away - highest values",
                        getLabels(25),
                        getData(1, 25, 1000000, 5000000, 1)
                    ),

                    BarData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30)
                    ),
                    BarData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30)
                    ),
                    BarData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000)
                    ),
                    BarData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30)
                    ),
                    BarData(
                        "32 values",
                        getLabels(32),
                        getData(1, 32, 0, 30)
                    ),
                    BarData(
                        "32 values - far apart",
                        getLabels(32),
                        getData(1, 32, 0, 30000)
                    ),
                    BarData(
                        "50 values",
                        getLabels(50),
                        getData(1, 50, 0, 30)
                    ),
                    BarData(
                        "100 values",
                        getLabels(100),
                        getData(1, 100, 0, 30)
                    ),
                    BarData(
                        "500 values",
                        getLabels(500),
                        getData(1, 500, 0, 30)
                    ),
                    BarData(
                        "1000 values",
                        getLabels(1000),
                        getData(1, 1000, 0, 30)
                    ),

                    // todo: NEGATIVE VALUES
                    BarData(
                        "1 value - negative",
                        getLabels(1),
                        getData(1, 1, -30, -10)
                    ),
                    BarData(
                        "2 values - negative",
                        getLabels(2),
                        getData(1, 2, -30, -10)
                    ),
                    BarData(
                        "3 values - negative",
                        getLabels(3),
                        getData(1, 3, -30, -10)
                    ),
                    BarData(
                        "4 values - negative",
                        getLabels(4),
                        getData(1, 4, -30, 5)
                    ),
                    BarData(
                        "8 values - negative",
                        getLabels(8),
                        getData(1, 8, -30, 10)
                    ),
                    BarData(
                        "8 values - negative - large",
                        getLabels(8),
                        getData(1, 8, -3000, -100)
                    ),
                    BarData(
                        "32 values - far apart - negative",
                        getLabels(32),
                        getData(1, 32, -3000, 3000)
                    ),

                    // todo: GROUPED POSITIVE

                    BarData(
                        "2 grouped",
                        getLabels(2),
                        getData(2, 2, 10, 30)
                    ),
                    BarData(
                        "some grouped",
                        getLabels(4),
                        getData(4, 4, 0, 30)
                    ),
                    BarData(
                        "some grouped - far apart",
                        getLabels(4),
                        getData(4, 4, 0, 3000)
                    ),
                    BarData(
                        "many grouped",
                        getLabels(16),
                        getData(4, 16, 0, 30)
                    ),

                    // todo: GROUPED NEGATIVE

                    BarData(
                        "2 grouped - negative",
                        getLabels(2),
                        getData(2, 2, -30, -10)
                    ),
                    BarData(
                        "some grouped - negative",
                        getLabels(4),
                        getData(4, 4, -30, 10)
                    ),
                    BarData(
                        "some grouped - far apart - negative",
                        getLabels(4),
                        getData(4, 4, -3000, 3000)
                    ),
                    BarData(
                        "many grouped - negative",
                        getLabels(16),
                        getData(4, 16, -30, 30)
                    )
                )

                showRv(barDatas.size, fun(chartLayout: LinearLayout, position: Int) {
                    val barData = barDatas[position]
                    showChartsInternal(
                        chartLayout,
                        Gson().toJson(
                            Bar(
                                barData.title,
                                "x axis",
                                barData.xLabels,
                                "y axis",
                                barData.yLefts,
                                barData.colors,
                                null,
                                null,
                                null,
                                false,
                                null
                            )
                        )
                    )
                })
/*                AssetHelper.readFile(
                    this,
                    "chart_data",
                    fun(string: String) {
                        showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, string)
                        })
                    }
                )*/
            }

            ChartType.HORIZONTAL_BAR -> {
                val barDatas = arrayListOf(

                    /**
                     * todo: POSITIVE VALUES
                     */

                    BarData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    BarData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),
                    BarData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - far apart",
                        getLabels(16),
                        getData(1, 16, 0, 30000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "19 values",
                        getLabels(19),
                        getData(1, 19, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),

                    /**
                     * todo: NEGATIVE VALUES
                     */
                    BarData(
                        "2 values - negative",
                        getLabels(2),
                        getData(1, 2, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "3 values - negative",
                        getLabels(3),
                        getData(1, 3, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400)
                        )
                    ),
                    BarData(
                        "4 values - negative",
                        getLabels(4),
                        getData(1, 4, -30, 5),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "8 values - negative",
                        getLabels(8),
                        getData(1, 8, -30, 10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - negative - large",
                        getLabels(16),
                        getData(1, 16, -3000, -100),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "16 values - far apart - negative",
                        getLabels(16),
                        getData(1, 16, -3000, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),

                    /**
                     * todo: GROUPED POSITIVE
                     */

                    BarData(
                        "2 grouped",
                        getLabels(2),
                        getData(2, 2, 10, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "some grouped",
                        getLabels(4),
                        getData(4, 4, 0, 30),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "some grouped - far apart",
                        getLabels(4),
                        getData(4, 4, 0, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400),
                            resources.getString(R.color.red400)
                        )
                    ),

                    /**
                     * todo: GROUPED NEGATIVE
                     */

                    BarData(
                        "2 grouped - negative",
                        getLabels(2),
                        getData(2, 2, -30, -10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400)
                        )
                    ),
                    BarData(
                        "some grouped - negative",
                        getLabels(4),
                        getData(4, 4, -30, 10),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    ),
                    BarData(
                        "some grouped - far apart - negative",
                        getLabels(4),
                        getData(4, 4, -3000, 3000),
                        arrayListOf(
                            resources.getString(R.color.red400),
                            resources.getString(R.color.purple400),
                            resources.getString(R.color.amber400),
                            resources.getString(R.color.green400)
                        )
                    )
                )

                showRv(barDatas.size, fun(chartLayout: LinearLayout, position: Int) {
                    val barData = barDatas[position]
                    showChartsInternal(
                        chartLayout, Gson().toJson(
                            Bar(
                                barData.title,
                                "x axis",
                                barData.xLabels,
                                "y axis",
                                barData.yLefts,
                                barData.colors,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                    )
                })
            }

            ChartType.LINE -> {

                val lineData = arrayListOf(

                    // todo: POSITIVE VALUES

                    LineData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    LineData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30)
                    ),
                    LineData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30)
                    ),
                    LineData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30)
                    ),
                    LineData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30)
                    ),
                    LineData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30)
                    ),
                    LineData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 1000, 3000)
                    ),
                    LineData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30)
                    ),
                    LineData(
                        "32 values",
                        getLabels(32),
                        getData(1, 32, 0, 30)
                    ),
                    LineData(
                        "32 values - far apart",
                        getLabels(32),
                        getData(1, 32, 0, 30000)
                    ),
                    LineData(
                        "50 values",
                        getLabels(50),
                        getData(1, 50, 0, 30)
                    ),
                    LineData(
                        "100 values",
                        getLabels(100),
                        getData(1, 100, 0, 30)
                    ),
                    LineData(
                        "500 values",
                        getLabels(500),
                        getData(1, 500, 0, 30)
                    ),
                    LineData(
                        "1000 values",
                        getLabels(1000),
                        getData(1, 1000, 0, 30)
                    ),

                    // todo: NEGATIVE VALUES
                    LineData(
                        "1 value - negative",
                        getLabels(1),
                        getData(1, 1, -30, -10)
                    ),
                    LineData(
                        "2 values - negative",
                        getLabels(2),
                        getData(1, 2, -30, -10)
                    ),
                    LineData(
                        "3 values - negative",
                        getLabels(3),
                        getData(1, 3, -30, -10)
                    ),
                    LineData(
                        "4 values - negative",
                        getLabels(4),
                        getData(1, 4, -30, 5)
                    ),
                    LineData(
                        "8 values - negative",
                        getLabels(8),
                        getData(1, 8, -30, 10)
                    ),
                    LineData(
                        "8 values - negative - large",
                        getLabels(8),
                        getData(1, 8, -3000, -100)
                    ),
                    LineData(
                        "32 values - far apart - negative",
                        getLabels(32),
                        getData(1, 32, -3000, 3000)
                    ),
                    LineData(
                        "50 values - negative",
                        getLabels(50),
                        getData(1, 50, -30, 30)
                    ),
                    LineData(
                        "100 values - negative",
                        getLabels(100),
                        getData(1, 100, -80, 80)
                    ),
                    LineData(
                        "500 values - negative",
                        getLabels(500),
                        getData(1, 500, -300, 300)
                    ),
                    LineData(
                        "1000 values - negative",
                        getLabels(1000),
                        getData(1, 1000, -5000, 3000)
                    ),

                    // todo: POSITIVE 2 right axis VALUES

                    LineData(
                        "No value",
                        arrayListOf(),
                        arrayListOf()
                    ),
                    LineData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30, 1),
                        getData(1, 1, 10, 30, 2)
                    ),
                    LineData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30, 1),
                        getData(1, 2, 10, 30, 2)
                    ),
                    LineData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30, 1),
                        getData(1, 3, 0, 30, 2)
                    ),
                    LineData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30, 1),
                        getData(1, 4, 0, 30, 2)
                    ),
                    LineData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30, 1),
                        getData(1, 8, 0, 30, 2)
                    ),
                    LineData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000, 1),
                        getData(1, 8, 100, 3000, 2)
                    ),
                    LineData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30, 1),
                        getData(1, 16, 0, 30, 2)
                    ),
                    LineData(
                        "32 values",
                        getLabels(32),
                        getData(1, 32, 0, 30, 1),
                        getData(1, 32, 0, 30, 2)
                    ),
                    LineData(
                        "32 values - far apart",
                        getLabels(32),
                        getData(1, 32, 0, 30000, 1),
                        getData(1, 32, 0, 30000, 2)
                    ),
                    LineData(
                        "50 values",
                        getLabels(50),
                        getData(1, 50, 0, 30, 1),
                        getData(1, 50, 0, 30, 2)
                    ),
                    LineData(
                        "100 values",
                        getLabels(100),
                        getData(1, 100, 0, 30, 1),
                        getData(1, 100, 0, 30, 2)
                    ),
                    LineData(
                        "500 values",
                        getLabels(500),
                        getData(1, 500, 0, 30, 1),
                        getData(1, 500, 0, 30, 2)
                    ),
                    LineData(
                        "1000 values",
                        getLabels(1000),
                        getData(1, 1000, 0, 30, 1),
                        getData(1, 1000, 0, 30, 2)
                    ),

                    // todo: NEGATIVE VALUES
                    LineData(
                        "1 value - negative",
                        getLabels(1),
                        getData(1, 1, -30, -10, 1),
                        getData(1, 1, -30, -10, 2)
                    ),
                    LineData(
                        "2 values - negative",
                        getLabels(2),
                        getData(1, 2, -30, -10, 1),
                        getData(1, 2, -3, -1, 2)
                    ),
                    LineData(
                        "3 values - negative",
                        getLabels(3),
                        getData(1, 3, -30, -10, 1),
                        getData(1, 3, -27, -15, 2)
                    ),
                    LineData(
                        "4 values - negative",
                        getLabels(4),
                        getData(1, 4, -30, 5, 1),
                        getData(1, 4, -10, 25, 2)
                    ),
                    LineData(
                        "5 values - negative",
                        getLabels(5),
                        getData(1, 5, -30, 5, 1),
                        getData(1, 5, -25, 15, 2)
                    ),
                    LineData(
                        "6 values - negative",
                        getLabels(6),
                        getData(1, 6, -30, 5, 1),
                        getData(1, 6, -300, 5, 2)
                    ),
                    LineData(
                        "7 values - negative",
                        getLabels(7),
                        getData(1, 7, -30, 5, 1),
                        getData(1, 7, -50, 15, 2)
                    ),
                    LineData(
                        "8 values - negative",
                        getLabels(8),
                        getData(1, 8, -30, 10, 1),
                        getData(1, 8, -60, 10, 2)
                    ),
                    LineData(
                        "8 values - negative - large",
                        getLabels(8),
                        getData(1, 8, -3000, -100, 1),
                        getData(1, 8, -2000, -150, 2)
                    ),
                    LineData(
                        "32 values - far apart - negative",
                        getLabels(32),
                        getData(1, 32, -3000, 3000, 1),
                        getData(1, 32, -4000, 3000, 2)
                    ),
                    LineData(
                        "50 values - negative",
                        getLabels(50),
                        getData(1, 50, -2000, 3000, 1),
                        getData(1, 50, -2500, 3000, 2)
                    ),
                    LineData(
                        "100 values - negative",
                        getLabels(100),
                        getData(1, 100, -1000, 3000, 1),
                        getData(1, 100, -1000, 3000, 2)
                    ),
                    LineData(
                        "500 values - negative",
                        getLabels(500),
                        getData(1, 500, -2000, 3000, 1),
                        getData(1, 500, -2000, 3000, 2)
                    ),
                    LineData(
                        "1000 values - negative",
                        getLabels(1000),
                        getData(1, 1000, -1500, 3000, 1),
                        getData(1, 1000, -1500, 3000, 2)
                    ),
                    LineData(
                        "20 values - far away - highest values",
                        getLabels(20),
                        getData(1, 20, 10000, 1000000, 1),
                        getData(1, 20, 10000, 1000000, 2)
                    ),
                    LineData(
                        "20 values - far away - highest values - negative",
                        getLabels(1000),
                        getData(1, 20, -1000000, -10000, 1),
                        getData(1, 20, -1000000, -10000, 2)
                    ),
                    LineData(
                        "20 values - no line",
                        getLabels(20),
                        getData(1, 20, 100, 200)
                    ),
                    LineData(
                        "20 values - no line - negative",
                        getLabels(20),
                        getData(1, 20, -100, 200)
                    ),
                    LineData(
                        "20 values - no line - 2 yaxis",
                        getLabels(20),
                        getData(1, 20, 100, 200, 1),
                        getData(1, 20, 100, 200, 2)
                    ),
                    LineData(
                        "20 values - no line - 2 yaxis - negative",
                        getLabels(20),
                        getData(1, 20, -200, -100, 1),
                        getData(1, 20, -200, -100, 2)
                    )
                )

                showRv(lineData.size, fun(chartLayout: LinearLayout, position: Int) {
                    val data = lineData[position]
                    showChartsInternal(
                        chartLayout, Gson().toJson(
                            Line(
                                data.title,
                                "x axis",
                                data.xLabels,
                                "y axis",
                                data.yLefts,
                                if (data.yRights == null) null else "y axis right",
                                data.yRights,
                                data.colors,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                    )
                })

                /*
                AssetHelper.readFile(
                    this,
                    "line_chart_minimal_data",
                    fun(string: String) {
                        showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, string)
                        })
                    }
                )
                */
            }

            ChartType.COMBINED -> {
                val combinedData = arrayListOf(

                    CombinedData(
                        "No value",
                        arrayListOf(),
                        arrayListOf(),
                        arrayListOf()
                    ),
                    CombinedData(
                        "1 value",
                        getLabels(1),
                        getData(1, 1, 10, 30, 1),
                        getData(1, 1, 100, 300, 2)
                    ),
                    CombinedData(
                        "2 values",
                        getLabels(2),
                        getData(1, 2, 10, 30, 1),
                        getData(1, 2, 100, 300, 2)
                    ),
                    CombinedData(
                        "3 values",
                        getLabels(3),
                        getData(1, 3, 0, 30, 1),
                        getData(1, 3, 0, 300, 2)
                    ),
                    CombinedData(
                        "4 values",
                        getLabels(4),
                        getData(1, 4, 0, 30, 1),
                        getData(1, 4, 0, 300, 2)
                    ),
                    CombinedData(
                        "8 values",
                        getLabels(8),
                        getData(1, 8, 0, 30, 1),
                        getData(1, 8, 0, 300, 2)
                    ),
                    CombinedData(
                        "8 values - large",
                        getLabels(8),
                        getData(1, 8, 100, 3000, 1),
                        getData(1, 8, 1000, 30000, 2)
                    ),
                    CombinedData(
                        "16 values",
                        getLabels(16),
                        getData(1, 16, 0, 30, 1),
                        getData(1, 16, 0, 300, 2)
                    ),
                    CombinedData(
                        "32 values",
                        getLabels(32),
                        getData(1, 32, 0, 30, 1),
                        getData(1, 32, 0, 300, 2)
                    ),
                    CombinedData(
                        "32 values - far apart",
                        getLabels(32),
                        getData(1, 32, 0, 30000, 1),
                        getData(1, 32, 100, 20000, 2)
                    ),
                    CombinedData(
                        "50 values",
                        getLabels(50),
                        getData(1, 50, 0, 30, 1),
                        getData(1, 50, 0, 300, 2)
                    ),
                    CombinedData(
                        "100 values",
                        getLabels(100),
                        getData(1, 100, 0, 30, 1),
                        getData(1, 100, 100, 300, 2)
                    ),
                    CombinedData(
                        "500 values",
                        getLabels(500),
                        getData(1, 500, 0, 300, 1),
                        getData(1, 500, 0, 30, 2)
                    ),
                    CombinedData(
                        "1000 values",
                        getLabels(1000),
                        getData(1, 1000, 0, 300, 1),
                        getData(1, 1000, 0, 30, 2)
                    ),

                    // todo: NEGATIVE VALUES
                    CombinedData(
                        "1 value - negative",
                        getLabels(1),
                        getData(1, 1, -300, -10, 1),
                        getData(1, 1, -30, -10, 2)
                    ),
                    CombinedData(
                        "2 values - negative",
                        getLabels(2),
                        getData(1, 2, -30, -10, 1),
                        getData(1, 2, -3, -1, 2)
                    ),
                    CombinedData(
                        "3 values - negative",
                        getLabels(3),
                        getData(1, 3, -30, -10, 1),
                        getData(1, 3, -27, -15, 2)
                    ),
                    CombinedData(
                        "4 values - negative",
                        getLabels(4),
                        getData(1, 4, -30, 5, 1),
                        getData(1, 4, -10, 25, 2)
                    ),
                    CombinedData(
                        "5 values - negative",
                        getLabels(5),
                        getData(1, 5, -30, 5, 1),
                        getData(1, 5, -25, 15, 2)
                    ),
                    CombinedData(
                        "6 values - negative",
                        getLabels(6),
                        getData(1, 6, -30, 5, 1),
                        getData(1, 6, -300, 5, 2)
                    ),
                    CombinedData(
                        "7 values - negative",
                        getLabels(7),
                        getData(1, 7, -30, 5, 1),
                        getData(1, 7, -50, 15, 2)
                    ),
                    CombinedData(
                        "8 values - negative",
                        getLabels(8),
                        getData(1, 8, -30, 10, 1),
                        getData(1, 8, -60, 10, 2)
                    ),
                    CombinedData(
                        "8 values - negative - large",
                        getLabels(8),
                        getData(1, 8, -3000, -100, 1),
                        getData(1, 8, -2000, -150, 2)
                    ),
                    CombinedData(
                        "32 values - far apart - negative",
                        getLabels(32),
                        getData(1, 32, -3000, 3000, 1),
                        getData(1, 32, -4000, 3000, 2)
                    ),
                    CombinedData(
                        "50 values - negative",
                        getLabels(50),
                        getData(1, 50, -2000, 3000, 1),
                        getData(1, 50, -2500, 3000, 2)
                    ),
                    CombinedData(
                        "100 values - negative",
                        getLabels(100),
                        getData(1, 100, -1000, 3000, 1),
                        getData(1, 100, -1000, 3000, 2)
                    ),
                    CombinedData(
                        "500 values - negative",
                        getLabels(500),
                        getData(1, 500, -2000, 3000, 1),
                        getData(1, 500, -1000, 2000, 2)
                    ),
                    CombinedData(
                        "1000 values - negative",
                        getLabels(1000),
                        getData(1, 1000, -1500, 3000, 1),
                        getData(1, 1000, -1500, 2000, 2)
                    ),
                    CombinedData(
                        "20 values - far away - highest values",
                        getLabels(20),
                        getData(1, 20, 10000, 1000000, 1),
                        getData(1, 20, 1000, 100000, 2)
                    ),
                    CombinedData(
                        "20 values - far away - highest values - negative",
                        getLabels(1000),
                        getData(1, 20, -1000000, -10000, 1),
                        getData(1, 20, -100000, 1000, 2)
                    ),
                    CombinedData(
                        "20 values - no line",
                        getLabels(20),
                        getData(1, 20, 100, 200),
                        getData(1, 20, 90, 290)
                    ),
                    CombinedData(
                        "20 values - no line - negative",
                        getLabels(20),
                        getData(1, 20, -100, 200),
                        getData(1, 20, -150, 150)
                    ),
                    CombinedData(
                        "20 values - no line - 2 yaxis",
                        getLabels(20),
                        getData(1, 20, 100, 200, 1),
                        getData(1, 20, 100, 600, 2)
                    ),
                    CombinedData(
                        "20 values - no line - 2 yaxis - negative",
                        getLabels(20),
                        getData(1, 20, -200, -100, 1),
                        getData(1, 20, -200, -100, 2)
                    )
                )

                showRv(combinedData.size, fun(chartLayout: LinearLayout, position: Int) {
                    val data = combinedData[position]
                    showChartsInternal(
                        chartLayout, Gson().toJson(
                            Combined(
                                data.title,
                                "x axis",
                                data.xLabels,
                                "y axis",
                                "y axis right",
                                data.yBar,
                                data.yLine,
                                data.colors,
                                null,
                                null,
                                null,
                                null,
                                false
                            )
                        )
                    )
                })

                /*
                AssetHelper.readFile(
                    this,
                    "combined_chart_data",
                    fun(string: String) {
                        showRv(1, fun(chartLayout: LinearLayout, position: Int) {
                            showChartsInternal(chartLayout, string)
                        })
                    }
                )
                */
            }
        }
    }

    private fun showRv(size: Int, getBindView: (chartLayout: LinearLayout, position: Int) -> Unit) {
        val airRv = AirRv(object : AirRv.Callback {

            override fun getAppContext(): Context? {
                return this@ChartActivity
            }

            override fun getBindView(
                viewHolder: RecyclerView.ViewHolder,
                viewType: Int,
                position: Int
            ) {
                val customViewHolder = viewHolder as CustomViewHolder
                getBindView.invoke(customViewHolder.chartHolder, position)
            }

            override fun getEmptyView(): View? {
                return null
            }

            override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                return LinearLayoutManager(appContext)
            }

            override fun getRvHolderViewGroup(): ViewGroup? {
                return rvHolder
            }

            override fun getSize(): Int? {
                return size
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return CustomViewHolder(view)
            }

            override fun getViewLayoutId(viewType: Int): Int? {
                return R.layout.chart_rv_item
            }

            override fun getViewType(position: Int): Int? {
                return 0
            }

        })

        LinearSnapHelper().attachToRecyclerView(airRv.rv)
    }

    private fun showChartsInternal(viewGroup: ViewGroup, jsonString: String) {

        when (chartType) {
            ChartType.BAR, ChartType.HORIZONTAL_BAR -> {
                AirChart(this, viewGroup).showBarChart(jsonString)
            }

            ChartType.LINE -> {
                AirChart(this, viewGroup).showLineChart(jsonString)

                /*

                // extended usage example of line chart

                AirChart(this, viewGroup).showLineChart(object : AirChartLine.ILine {

                    //mandatory methods
                    override fun getTitle(): String {
                        return "title of the chart"
                    }

                    override fun getXAxisTitle(): String {
                        return "this is X-axis"
                    }

                    override fun getXAxisLabels(): ArrayList<String> {
                        return arrayListOf("Student1", "Student2", "Student3")
                    }

                    override fun getYLeftAxisTitle(): String {
                        return "this is Y-axis"
                    }

                    override fun getYLeftAxisValues(): ArrayList<Value> {
                        return arrayListOf(
                            Value(
                                "marks in sem 1",
                                arrayListOf(50.0, 40.0, 49.5)
                            )
                        )
                    }

                    // extra methods that can be overridden

                    override fun getColors(): ArrayList<String> {
                        return arrayListOf("#ffa726", "#2196f3")
                    }

                    override fun getSubTitle(): String? {
                        return "this is subtitle of the chart"
                    }

                    override fun getDecimalFormatPattern(): String? {
                        return "0.#"
                    }

                    override fun getAdditionalValues(): java.util.ArrayList<AdditionalValue>? {
                        return arrayListOf(
                            AdditionalValue("Key1", "Value1"),
                            AdditionalValue("Key2", "Value2")
                        )
                    }

                    override fun getCustomViewLayoutResId(): Int? {
                        return R.layout.custom_view
                    }

                    override fun getIsAnimationRequired(): Boolean? {
                        return true
                    }

                    override fun onValueSelected(e: Entry?) {
                        // do something
                    }

                    override fun onNoValueSelected() {
                        // do something
                    }

                    override fun getYRightAxisTitle(): String? {
                        return "this is Y-axis right"
                    }

                    override fun getYRightAxisValues(): ArrayList<Value> {
                        return arrayListOf(
                            Value(
                                "marks in sem 2",
                                arrayListOf(50.5, 40.9, 39.5)
                            )
                        )
                    }

                    override fun getIsCurved(): Boolean? {
                        return true
                    }
                })
                */
            }

            ChartType.COMBINED -> {
                AirChart(this, viewGroup).showCombinedChart(jsonString)

                /*
                AirChart(this, viewGroup).showCombinedChart(object : AirChartCombined.ICombined{
                    override fun getTitle(): String {
                        return "title of the chart"
                    }

                    override fun getXAxisTitle(): String {
                        return "this is X-axis"
                    }

                    override fun getXAxisLabels(): java.util.ArrayList<String> {
                        return arrayListOf("Student1", "Student2", "Student3")
                    }

                    override fun getYLeftAxisTitle(): String {
                        return "this is Y-axis"
                    }

                    override fun getYRightAxisTitle(): String {
                        return "this is Y-axis right"
                    }

                    override fun getYAxisBarValues(): java.util.ArrayList<Value> {
                        return arrayListOf(
                            Value(
                                "marks in sem 2",
                                arrayListOf(50.5, 40.9, 39.5)
                            )
                        )
                    }

                    override fun getYAxisLineValues(): java.util.ArrayList<Value> {
                        return arrayListOf(
                            Value(
                                "marks in sem 1",
                                arrayListOf(150.5, 140.9, 139.5)
                            )
                        )
                    }
                })
                */
            }
        }
    }

    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chartHolder: LinearLayout = view.chartHolder
    }

    class BarData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yLefts: java.util.ArrayList<Value>,
        val colors: ArrayList<String>? = null
    )

    class LineData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yLefts: java.util.ArrayList<Value>,
        val yRights: java.util.ArrayList<Value>? = null,
        val colors: ArrayList<String>? = null
    )

    class CombinedData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yBar: java.util.ArrayList<Value>,
        val yLine: java.util.ArrayList<Value>,
        val colors: ArrayList<String>? = null
    )
}