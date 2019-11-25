package in.devco.dailypadho.view;

import java.util.List;

import in.devco.dailypadho.model.Source;

public interface FilterView {
    void setSources(List<Source> sources);
    void error(int error);
}
