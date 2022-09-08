package com.example.intermediate.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum CostColumnInfo implements ExcelColumnInfo {

    CONTENT("지출 내역", 0, 1, new Color(245,244,255)),
    PAY("금액", 0, 2,  new Color(245,244,255));

    private final String text;
    private final int row;
    private final int column;
    private final Color color;

    public static Map<Integer, List<CostColumnInfo>> getAllColumns() {
        return Arrays.stream(values())
                .collect(Collectors.groupingBy(ExcelColumnInfo::getRow));
    }
}

