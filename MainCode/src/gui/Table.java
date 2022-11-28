package gui;

import java.util.Arrays;
import java.util.List;

public enum Table {

    COMPANY(Arrays.asList("company_id")),
    JOBS(Arrays.asList()),
    LOCATION(Arrays.asList()),
    CYCLE(Arrays.asList()),
    PRESTIGE(Arrays.asList()),
    BENEFITS(Arrays.asList()),
    COMPANIES_FOLLOWED(Arrays.asList()),
    JOBS_FOLLOWED(Arrays.asList()),
    LOCATIONS_FOLLOWED(Arrays.asList()),
    JOBS_APPLIED_TO(Arrays.asList());

    private List<String> primaryKeys;

    Table(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
