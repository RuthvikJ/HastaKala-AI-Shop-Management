package com.hastakala.shop.ui.components

import android.graphics.Color as AndroidColor
import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.hastakala.shop.data.model.DailySales
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanTextDark

@Composable
fun SalesBarChart(
    dailySales: List<DailySales>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Weekly Sales Trend",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(14.dp))
            AndroidView(
                factory = { context ->
                    BarChart(context).apply {
                        description.isEnabled = false
                        legend.isEnabled = false
                        setDrawGridBackground(false)
                        setFitBars(true)
                        setTouchEnabled(false)
                        setScaleEnabled(false)
                        setPinchZoom(false)
                        setDrawBarShadow(false)
                        setExtraBottomOffset(8f)

                        // X axis
                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                            granularity = 1f
                            textColor = AndroidColor.parseColor("#7A5C4A")
                            textSize = 11f
                        }

                        // Y axes
                        axisLeft.apply {
                            setDrawGridLines(true)
                            gridColor = AndroidColor.parseColor("#F0E6DC")
                            setDrawAxisLine(false)
                            textColor = AndroidColor.parseColor("#7A5C4A")
                            textSize = 10f
                            axisMinimum = 0f
                        }
                        axisRight.isEnabled = false
                    }
                },
                update = { chart ->
                    if (dailySales.isNotEmpty()) {
                        val entries = dailySales.mapIndexed { index, sale ->
                            BarEntry(index.toFloat(), sale.totalSales.toFloat())
                        }

                        val dataSet = BarDataSet(entries, "Sales").apply {
                            color = AndroidColor.parseColor("#F26419")
                            setDrawValues(true)
                            valueTextColor = AndroidColor.parseColor("#2F1D11")
                            valueTextSize = 10f
                            valueTypeface = Typeface.DEFAULT_BOLD
                        }

                        val labels = dailySales.map { it.date }
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                        chart.data = BarData(dataSet).apply { barWidth = 0.55f }
                        chart.animateY(800)
                        chart.invalidate()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
