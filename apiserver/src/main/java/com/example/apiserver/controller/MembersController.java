package com.example.apiserver.controller;

import com.example.apiserver.dto.MembersDTO;
import com.example.apiserver.security.util.JWTUtil;
import com.example.apiserver.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class MembersController {
  private final MembersService membersService;
  private final JWTUtil jwtUtil;


  @PostMapping(value = "/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody MembersDTO membersDTO) {
    log.info("register.....................");
    try {
      // 임시 ID 생성 또는 실제 등록 처리
      String memberId = membersService.registerMembers(membersDTO);

      // 일단 토큰 대신 테스트용 임시 값 리턴 가능
      Map<String, String> tokenMap = new HashMap<>();
      tokenMap.put("accessToken", "test-access-token"); // 실제로는 jwtUtil.generateAccessToken(memberId)
      tokenMap.put("refreshToken", "test-refresh-token");

      return new ResponseEntity<>(tokenMap, HttpStatus.OK);
    } catch (Exception e) {
      log.error("회원가입 실패: " + e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/get/{mid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MembersDTO> read(@PathVariable("mid") Long mid) {
    return new ResponseEntity<>(membersService.getMembers(mid), HttpStatus.OK);
  }
  @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MembersDTO> get(String email) {
    return new ResponseEntity<>(membersService.getMembersByEmail(email), HttpStatus.OK);
  }

}
