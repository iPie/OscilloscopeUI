package oscilloscopeui;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyUnbounded;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Ajarax
 */
public class DynamicChart {

    private Chart2D chart;
    private ITrace2D trace;
    private Date startTime;
    private List<Integer> samplesBuffer;
    private DataStorage storage;
    private boolean isCalibrating;
    private List<Double> calibrationValues;

    public DynamicChart() {
        initilizeChart();
    }

    public DynamicChart(JPanel panel) {
        initilizeChart();
        addChartToPanel(panel);
    }

    public void startPlotting() {
        updateChartSettings();
        startTime = new Date();
    }

    public void stopPlotting() {
        isCalibrating = false;
        samplesBuffer.clear();
        try {
            storage.close();
        } catch (IOException ex) {
            // TODO: notify user that writing to disk is not possible
            Logger.getLogger(DynamicChart.class.getName()).log(Level.SEVERE,
                    "Failed to close data storage", ex);
        }
    }

    public void addXY(double x, double y) {
        trace.addPoint(x, y);
    }

    public void addY(double y) {
        // TODO: handle time overflow!
        Date now = new Date();
        this.addXY((now.getTime() - startTime.getTime()), y);
    }

    public void bufferizeValue(int value, int samplesCount) {
        if (samplesBuffer.size() <= samplesCount) {
            samplesBuffer.add(value);
            if (isCalibrating) {
                calibrate(value);
            }
            try {
                Date now = new Date();
                // TODO: this might also overflow
                // TODO: unreliable timestamp, values sometimes repeat
                long mark = now.getTime() - startTime.getTime();
                storage.storeValues(Integer.toString(value), Long.toString(mark));
            } catch (IOException ex) {
                // TODO: notify user that writing to disk is not possible
                Logger.getLogger(DynamicChart.class.getName()).log(Level.SEVERE,
                        "Failed to store plotting data on disk", ex);
            }
        } else {
            int sample = 0;
            for (int d : samplesBuffer) {
                sample += d;
            }
            this.addY(resultToVoltage(sample / samplesCount));
            samplesBuffer.clear();
        }
    }

    /**
     * Calibrates the zero-level. The calibration is performed by analyzing 
     * a set of values transfered over the serial port. The values are processed
     * using the tri-sigma rule and the mean value of the resulting value is then
     * applied to the Config.DynamicChart.GAIN property. The maximum number of
     * values to be analyzed is specified in Config.DynamicChart.CALIBRATION_VALUES_COUNT
     */
    public void calibrate() {
        if (!isCalibrating) {
            isCalibrating = true;
            calibrationValues.clear();
        }
    }

    public void updateChartSettings() {
        if (!Config.DynamicChart.AUTO_SCALE) {
            chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(
                    new Range(Config.DynamicChart.MIN_RANGE, Config.DynamicChart.MAX_RANGE)));
        } else {
            chart.getAxisY().setRangePolicy(new RangePolicyUnbounded());
        }
        chart.removeAllTraces();
        trace = new Trace2DLtd(Config.DynamicChart.BUFFER_OFFSET);
        trace.setName("Channel 1");
        trace.setStroke(new BasicStroke(1.5f));
        trace.setPhysicalUnits("ms", "Volts");
        trace.setColor(Color.RED);
        chart.addTrace(trace);
    }

    private void calibrate(double value) {
        if (calibrationValues.size() == Config.DynamicChart.CALIBRATION_VALUES_COUNT) {
            int n = calibrationValues.size();
            double mean = 0;
            for (double d : calibrationValues) {
                mean += d;
            }
            mean /= n;
            double meanSquare = 0;
            for (double d : calibrationValues) {
                meanSquare += Math.pow((d - mean), 2);
            }
            double sigma = Math.sqrt(meanSquare / (n - 1));
            double cmean = 0;
            n = 0;
            for (double d : calibrationValues) {
                if (d > mean - 3 * sigma && d < mean + 3 * sigma) {
                    cmean += d;
                    n++;
                }
            }
            System.out.println(n);
            cmean /= n;
            this.isCalibrating = false;
            cmean = resultToVoltage(-cmean, 0);
            Logger.getLogger(Config.class.getName()).log(Level.INFO, "Calibrated value = {0}", cmean);
            Config.DynamicChart.setGain(cmean);
        } else {
            calibrationValues.add(value);
        }
    }

    private double resultToVoltage(double value, double gain) {
        return value * (Config.DynamicChart.REFERENCE_VOLTAGE / Config.DynamicChart.ADC_RESOLUTION)
                * Config.DynamicChart.MULTIPLIER + gain;
    }

    private double resultToVoltage(double value) {
        return resultToVoltage(value, Config.DynamicChart.GAIN);
    }

    private void initilizeChart() {
        chart = new Chart2D();
        this.updateChartSettings();
        chart.getAxisY().setPaintGrid(true);
        chart.getAxisX().getAxisTitle().setTitle("t");
        chart.getAxisY().getAxisTitle().setTitle("V");
        samplesBuffer = new ArrayList<>();
        calibrationValues = new ArrayList<>();
        storage = new DataStorage();
        isCalibrating = false;
    }

    private void addChartToPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        panel.add(chart);
        panel.updateUI();
    }
}
