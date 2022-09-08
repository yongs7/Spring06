package com.example.intermediate.service;


import com.example.intermediate.controller.response.*;
import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Date;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.excel.ExcelWriter;
import com.example.intermediate.repository.CostRepository;
import com.example.intermediate.repository.DateRepository;
import com.example.intermediate.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.example.intermediate.excel.CostColumnInfo.PAY;

@Service
@RequiredArgsConstructor
public class ExcelService {

  private final TripRepository tripRepository;
  private final DateRepository dateRepository;
  private final CostRepository costRepository;


  @Transactional(readOnly = true)
  public ResponseDto<?> downloadExcel(Long id, HttpServletRequest request, HttpServletResponse response) {
//    if (null == request.getHeader("RefreshToken")) {
//    return ResponseDto.fail("MEMBER_NOT_FOUND",
//            "로그인이 필요합니다.");
//  }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//              "로그인이 필요합니다.");
//    }
//
//    Member member = validateMember(request);
//    if (null == member) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
    Trip trip = isPresentTrip(id);
    if (null == trip) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet("첫번째 시트");
    Row row = null;
    Cell cell = null;
    int rowNum = 0;

    CellStyle body = ExcelWriter.createCellStyle(wb, new Color(255,255,255));

    List<Date> dateList = dateRepository.findAllByTrip(trip);

    for (int i = 0; i < dateList.size(); i++) {

      row = sheet.createRow(rowNum++);
      cell = row.createCell(0);
      cell.setCellStyle(ExcelWriter.createCellStyle(wb,new Color(225,235,245)));
      cell.setCellValue("day "+(i+1));
      ExcelWriter.renderHeader(wb,row);


      List<Cost> costList = costRepository.findAllByDate(dateList.get(i));
      for (Cost cost : costList) {
        row = sheet.createRow(rowNum++);
        ExcelWriter.renderBody(row, CostResponseDto.builder()
                .content(cost.getContent())
                .pay(cost.getPay())
                .build(), body);
      }
      row = sheet.createRow(rowNum++);
      cell = row.createCell(0);
      cell.setCellValue("day"+(i+1)+" 지출");
      cell = row.createCell(PAY.getColumn());
      cell.setCellValue(dateList.get(i).getSubTotal());
      row = sheet.createRow(rowNum++);
    }

    row = sheet.createRow(rowNum++);
    cell = row.createCell(0);
    cell.setCellValue("총 지출");
    cell = row.createCell(PAY.getColumn());
    cell.setCellValue(trip.getTotal());

    // 컨텐츠 타입과 파일명 지정
    response.setContentType("ms-vnd/excel");
    response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

    // Excel File Output
    try {
      wb.write(response.getOutputStream());
      wb.close();
    } catch (IOException e) {
      return ResponseDto.fail("DOWNLOAD_FAIL", "다운로드에 실패하였습니다.");
    }


    return ResponseDto.success("download success");
  }

  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }

}
