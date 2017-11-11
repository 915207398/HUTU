package com.example.cai.hutu.plot.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ab.view.chart.CategorySeries;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.example.cai.hutu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class PlotActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    // 折线说明文字
    private String[] titles = new String[] { "业务一", "业务二" };
    // 数据
    private List<double[]> values;
    // 每个数据点的颜色
    private List<int[]> colors;
    // 每个数据点的简要 说明
    private List<String[]> explains;
    // 折线的线条颜色
    private int[] mSeriescolors;
    // 渲染器
    private XYMultipleSeriesRenderer renderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        linearLayout = (LinearLayout) findViewById(R.id.chart);
        SlidingMenu menu=new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_set_wight);
        menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidingmenu);
        initValues();
        drawChart();

    }
    /**
     * 初始化数据
     */
    private void initValues() {
        values = new ArrayList<double[]>();
        colors = new ArrayList<int[]>();
        explains = new ArrayList<String[]>();

        values.add(new double[] { 500, 450, 700, 710, 420, 430, 400, 390, 290,
                400, 340, 500 });
        values.add(new double[] { 523, 450, 320, 370, 480, 570, 420, 350, 400,
                450, 700, 710 });

        colors.add(new int[] { Color.RED, Color.RED, Color.RED, Color.RED,
                Color.RED, Color.RED, Color.RED, Color.RED, Color.RED,
                Color.RED, Color.RED, Color.RED });
        colors.add(new int[] { Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,
                Color.GREEN, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,
                Color.BLUE, Color.BLUE, Color.BLUE });

        explains.add(new String[] { "1月业绩", "2月业绩", "3月业绩", "4月业绩", "5月业绩",
                "6月业绩", "7月业绩", "8月业绩", "9月业绩", "10月业绩", "11月业绩", "12月业绩" });
        explains.add(new String[] { "1月业绩", "2月业绩", "3月业绩", "4月业绩", "5月业绩",
                "6月业绩", "7月业绩", "8月业绩", "9月业绩", "10月业绩", "11月业绩", "12月业绩" });

        mSeriescolors = new int[] { Color.rgb(153, 204, 0),
                Color.rgb(247, 185, 64) };
    }

    /**
     * 开始绘折线图
     */
    private void drawChart() {
        renderer = new XYMultipleSeriesRenderer();
        int length = mSeriescolors.length;
        for (int i = 0; i < length; i++) {
            // 创建SimpleSeriesRenderer单一渲染器
            XYSeriesRenderer r = new XYSeriesRenderer();
            // 设置渲染器颜色
            r.setColor(mSeriescolors[i]);
            r.setFillPoints(true);
            r.setPointStyle(PointStyle.SQUARE);
            r.setLineWidth(1);
            r.setChartValuesTextSize(16);
            renderer.addSeriesRenderer(r);
        }
        // 坐标轴标题文字大小
        renderer.setAxisTitleTextSize(16);
        // 图形标题文字大小
        renderer.setChartTitleTextSize(25);
        // 轴线上标签文字大小
        renderer.setLabelsTextSize(15);
        // 说明文字大小
        renderer.setLegendTextSize(15);
        // 图表标题
        renderer.setChartTitle("测量指标折线图");
        // X轴标题
        renderer.setXTitle("时间/次");
        // Y轴标题
        renderer.setYTitle("指标/（+-）");
        // X轴最小坐标点
        renderer.setXAxisMin(0);
        // X轴最大坐标点
        renderer.setXAxisMax(12);
        // Y轴最小坐标点
        renderer.setYAxisMin(0);
        // Y轴最大坐标点
        renderer.setYAxisMax(800);
        // 坐标轴颜色
        renderer.setAxesColor(Color.rgb(51, 181, 229));
        renderer.setXLabelsColor(Color.rgb(51, 181, 229));
        renderer.setYLabelsColor(0, Color.rgb(51, 181, 229));
        // 设置图表上标题与X轴与Y轴的说明文字颜色
        renderer.setLabelsColor(Color.GRAY);
        // 设置字体加粗
        renderer.setTextTypeface("sans_serif", Typeface.BOLD);
        // 设置在图表上是否显示值标签
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
        // 显示屏幕可见取区的XY分割数
        renderer.setXLabels(12);
        renderer.setYLabels(8);
        // X刻度标签相对X轴位置
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        // Y刻度标签相对Y轴位置
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setPanEnabled(true, true);
        renderer.setZoomEnabled(true);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);

        // 标尺开启
        renderer.setScaleLineEnabled(true);
        // 设置标尺提示框高
        renderer.setScaleRectHeight(10);
        // 设置标尺提示框宽
        renderer.setScaleRectWidth(150);
        // 设置标尺提示框背景色
        renderer.setScaleRectColor(Color.argb(150, 52, 182, 232));
        renderer.setScaleLineColor(Color.argb(175, 150, 150, 150));
        renderer.setScaleCircleRadius(35);
        // 第一行文字的大小
        renderer.setExplainTextSize1(20);
        // 第二行文字的大小
        renderer.setExplainTextSize2(20);

        // 临界线
        double[] limit = new double[] { 15000, 12000, 4000, 9000 };
        renderer.setmYLimitsLine(limit);
        int[] colorsLimit = new int[] { Color.rgb(100, 255, 255),
                Color.rgb(100, 255, 255), Color.rgb(0, 255, 255),
                Color.rgb(0, 255, 255) };
        renderer.setmYLimitsLineColor(colorsLimit);

        // 显示表格线
        renderer.setShowGrid(true);
        // 如果值是0是否要显示
        renderer.setDisplayValue0(true);
        // 创建渲染器数据填充器
        XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int[] c = colors.get(i);
            String[] e = explains.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                // 设置每个点的颜色
                series.add(v[k], c[k], e[k]);
            }
            mXYMultipleSeriesDataset.addSeries(series.toXYSeries());
        }
        // 背景
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.rgb(222, 222, 200));
        renderer.setMarginsColor(Color.rgb(222, 222, 200));

        // 线图
        View chart = ChartFactory.getLineChartView(this,
                mXYMultipleSeriesDataset, renderer);
        linearLayout.addView(chart);
    }

}
