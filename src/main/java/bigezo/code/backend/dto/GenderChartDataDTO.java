package bigezo.code.backend.dto;

import java.util.List;

public class GenderChartDataDTO {
    private List<String> labels;
    private List<Number> boys;
    private List<Number> girls;

    public GenderChartDataDTO() {
    }

    public GenderChartDataDTO(List<String> labels, List<Number> boys, List<Number> girls) {
        this.labels = labels;
        this.boys = boys;
        this.girls = girls;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Number> getBoys() {
        return boys;
    }

    public void setBoys(List<Number> boys) {
        this.boys = boys;
    }

    public List<Number> getGirls() {
        return girls;
    }

    public void setGirls(List<Number> girls) {
        this.girls = girls;
    }
}
