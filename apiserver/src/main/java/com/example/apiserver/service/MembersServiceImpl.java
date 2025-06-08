package com.example.apiserver.service;


import com.example.apiserver.dto.MembersDTO;
import com.example.apiserver.entity.Members;
import com.example.apiserver.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public MembersDTO getMembers(Long mid) {
    Optional<Members> result = membersRepository.findById(mid);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public MembersDTO getMembersByEmail(String email) {
    Optional<Members> result = membersRepository.findByEmail(email);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public void removeMembers(Long mid) {
    membersRepository.deleteById(mid); // 가급적 사용하지 말라.
  }

  @Override
  public Long updateMembers(MembersDTO membersDTO) {
    Optional<Members> result = membersRepository.findById(membersDTO.getMid());
    if (result.isPresent()) {
      Members members = result.get();
      /* 변경할 내용은 members에 membersDTO의 내용을 변경하시오 */
      return membersRepository.save(members).getMid();
    }
    return 0L;
  }

  @Override
  public String registerMembers(MembersDTO membersDTO) {
    membersDTO.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    return String.valueOf(membersRepository.save(dtoToEnitity(membersDTO)).getMid());
  }

  @Override
  public MembersDTO login(String email, String password) {
    // 이메일로 회원 정보 검색
    Optional<Members> member = membersRepository.findByEmail(email);

    if (member.isPresent()) {
      // 비밀번호 비교
      Members foundMember = member.get();
      if (passwordEncoder.matches(password, foundMember.getPassword())) {
        // 비밀번호가 일치하면 해당 회원 DTO 반환
        return entityToDTO(foundMember);
      }
    }

    // 이메일 또는 비밀번호가 일치하지 않으면 null 반환
    return null;
  }
}
