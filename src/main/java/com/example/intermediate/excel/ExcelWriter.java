package com.example.intermediate.excel;

import com.example.intermediate.controller.response.CostResponseDto;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import static com.example.intermediate.excel.CostColumnInfo.CONTENT;
import static com.example.intermediate.excel.CostColumnInfo.PAY;

@NoArgsConstructor
public class ExcelWriter {

    public static void renderHeader(Workbook workbook,Row row) {
        for (Map.Entry<Integer, List<CostColumnInfo>> entry : CostColumnInfo.getAllColumns().entrySet()) {
            for (CostColumnInfo columnInfo : entry.getValue()) {
                renderCell(row, columnInfo, columnInfo.getText(), createCellStyle(workbook, new Color(225,235,245)));
            }
        }
    }

    public static void renderBody(Row row, CostResponseDto dto, CellStyle cellStyle) {
        renderCell(row, CONTENT, dto.getContent(), cellStyle);
        renderAmountCell(row, PAY, dto.getPay(), cellStyle);
    }

    public static CellStyle createCellStyle(Workbook wb, Color color) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();

        // 배경색 지정
        cellStyle.setFillForegroundColor(new XSSFColor(color, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 테두리
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    public static void renderCell(Row row, ExcelColumnInfo columnInfo, String value, CellStyle cellStyle) {
        Cell cell = row.createCell(columnInfo.getColumn());
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }

    public static void renderAmountCell(Row row, ExcelColumnInfo columnInfo, int value, CellStyle cellStyle) {
        Cell cell = row.createCell(columnInfo.getColumn());
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }
}
