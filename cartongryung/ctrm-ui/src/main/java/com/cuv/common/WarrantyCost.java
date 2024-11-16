package com.cuv.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WarrantyCost{ // WARRANTY 값 편리하게 수정

    LOCAL_BASIC_QUARTER_PV16("84,700"),
    LOCAL_BASIC_QUARTER_PV20("96,800"),
    LOCAL_BASIC_QUARTER_PV30("118,800"),
    LOCAL_BASIC_QUARTER_PV31("140,800"),
    LOCAL_BASIC_QUARTER_RVNT("140,800"),
    LOCAL_BASIC_HALF_PV16("119,900"),
    LOCAL_BASIC_HALF_PV20("139,700"),
    LOCAL_BASIC_HALF_PV30("171,600"),
    LOCAL_BASIC_HALF_PV31("211,200"),
    LOCAL_BASIC_HALF_RVNT("211,200"),
    LOCAL_BASIC_YEAR_PV16("138,600"),
    LOCAL_BASIC_YEAR_PV20("154,000"),
    LOCAL_BASIC_YEAR_PV30("195,800"),
    LOCAL_BASIC_YEAR_PV31("227,700"),
    LOCAL_BASIC_YEAR_RVNT("227,700"),

    LOCAL_STANDARD_QUARTER_PV16("108,900"),
    LOCAL_STANDARD_QUARTER_PV20("150,700"),
    LOCAL_STANDARD_QUARTER_PV30("172,700"),
    LOCAL_STANDARD_QUARTER_PV31("202,400"),
    LOCAL_STANDARD_QUARTER_RVNT("202,400"),
    LOCAL_STANDARD_HALF_PV16("173,800"),
    LOCAL_STANDARD_HALF_PV20("229,900"),
    LOCAL_STANDARD_HALF_PV30("269,500"),
    LOCAL_STANDARD_HALF_PV31("311,300"),
    LOCAL_STANDARD_HALF_RVNT("311,300"),
    LOCAL_STANDARD_YEAR_PV16("279,400"),
    LOCAL_STANDARD_YEAR_PV20("357,500"),
    LOCAL_STANDARD_YEAR_PV30("424,600"),
    LOCAL_STANDARD_YEAR_PV31("486,200"),
    LOCAL_STANDARD_YEAR_RVNT("486,200"),

    LOCAL_ALL_QUARTER_PV16("143,000"),
    LOCAL_ALL_QUARTER_PV20("198,000"),
    LOCAL_ALL_QUARTER_PV30("275,000"),
    LOCAL_ALL_QUARTER_PV31("396,000"),
    LOCAL_ALL_QUARTER_RVNT("396,000"),
    LOCAL_ALL_HALF_PV16("242,000"),
    LOCAL_ALL_HALF_PV20("297,000"),
    LOCAL_ALL_HALF_PV30("374,000"),
    LOCAL_ALL_HALF_PV31("495,000"),
    LOCAL_ALL_HALF_RVNT("495,000"),
    LOCAL_ALL_YEAR_PV16("429,000"),
    LOCAL_ALL_YEAR_PV20("484,000"),
    LOCAL_ALL_YEAR_PV30("561,000"),
    LOCAL_ALL_YEAR_PV31("682,000"),
    LOCAL_ALL_YEAR_RVNT("682,000"),

    IMPORT_BASIC_QUARTER_PV16("214,500"),
    IMPORT_BASIC_QUARTER_PV20("264,000"),
    IMPORT_BASIC_QUARTER_PV30("337,700"),
    IMPORT_BASIC_QUARTER_PV31("337,700"),
    IMPORT_BASIC_QUARTER_RVNT("337,700"),
    IMPORT_BASIC_HALF_PV16("363,000"),
    IMPORT_BASIC_HALF_PV20("457,600"),
    IMPORT_BASIC_HALF_PV30("600,600"),
    IMPORT_BASIC_HALF_PV31("600,600"),
    IMPORT_BASIC_HALF_RVNT("600,600"),
    IMPORT_BASIC_YEAR_PV16("426,800"),
    IMPORT_BASIC_YEAR_PV20("520,300"),
    IMPORT_BASIC_YEAR_PV30("689,700"),
    IMPORT_BASIC_YEAR_PV31("689,700"),
    IMPORT_BASIC_YEAR_RVNT("689,700"),

    IMPORT_STANDARD_QUARTER_PV16("392,700"),
    IMPORT_STANDARD_QUARTER_PV20("418,000"),
    IMPORT_STANDARD_QUARTER_PV30("467,500"),
    IMPORT_STANDARD_QUARTER_PV31("467,500"),
    IMPORT_STANDARD_QUARTER_RVNT("467,500"),
    IMPORT_STANDARD_HALF_PV16("712,800"),
    IMPORT_STANDARD_HALF_PV20("778,800"),
    IMPORT_STANDARD_HALF_PV30("905,300"),
    IMPORT_STANDARD_HALF_PV31("905,300"),
    IMPORT_STANDARD_HALF_RVNT("905,300"),
    IMPORT_STANDARD_YEAR_PV16("1,226,500"),
    IMPORT_STANDARD_YEAR_PV20("1,355,200"),
    IMPORT_STANDARD_YEAR_PV30("1,606,000"),
    IMPORT_STANDARD_YEAR_PV31("1,606,000"),
    IMPORT_STANDARD_YEAR_RVNT("1,606,000"),

    IMPORT_ALL_QUARTER_PV16("550,000"),
    IMPORT_ALL_QUARTER_PV20("869,000"),
    IMPORT_ALL_QUARTER_PV30("1,353,000"),
    IMPORT_ALL_QUARTER_PV31("1,353,000"),
    IMPORT_ALL_QUARTER_RVNT("1,353,000"),
    IMPORT_ALL_HALF_PV16("891,000"),
    IMPORT_ALL_HALF_PV20("1,210,000"),
    IMPORT_ALL_HALF_PV30("1,694,000"),
    IMPORT_ALL_HALF_PV31("1,694,000"),
    IMPORT_ALL_HALF_RVNT("1,694,000"),
    IMPORT_ALL_YEAR_PV16("1,452,000"),
    IMPORT_ALL_YEAR_PV20("1,771,000"),
    IMPORT_ALL_YEAR_PV30("2,255,000"),
    IMPORT_ALL_YEAR_PV31("2,255,000"),
    IMPORT_ALL_YEAR_RVNT("2,255,000");

    private final String costName; // warranty 비용

    public String costLabel(){
        return costName;
    }
/*
    private static final Map<String, WarrantyCost> BY_COSTNAME =
            Stream.of(values()).collect(Collectors.toMap(WarrantyCost::costName, Function.identity()));

    public static WarrantyCost valueOfCostName(String label) {
        return BY_COSTNAME.get(label);
    }*/

    public static WarrantyCost valueOfCostNameFor(String label) {
        return Arrays.stream(WarrantyCost.values())
                .filter(value -> value.getCostName().equals(label))
                .findAny()
                .orElse(null);
    }

    public static WarrantyCost fromString(String label) {
        for (WarrantyCost wc : WarrantyCost.values()) {
            if (wc.costName.equalsIgnoreCase(label)) {
                return wc;
            }
        }
        return null;
    }

    public static WarrantyCost valueOfLabel(String label) {
        for (WarrantyCost e : values()) {
            if (e.costName.equals(label)) {
                return e;
            }
        }
        return null;
    }
}