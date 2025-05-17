package com.kgu.life_watch.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.request.UserUpdateRequest;
import com.kgu.life_watch.domain.user.dto.ElderlyAssignmentRequest;
import com.kgu.life_watch.domain.user.dto.UserProfileResponse;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.service.UserService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.security.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "유저 관련 API")
public class UserController {

  private final UserService userService;

  // 현재 인증된 사용자의 정보를 주입받는다.
  // SecurityContextHolder에 저장된 Authentication에서 CustomUserDetails를 꺼내어 자동 주입함
  @GetMapping("/me")
  @Operation(summary = "유저 정보 조회 API", description = "유저 정보를 조회하는 API입니다.")
  public ApiResponse<UserProfileResponse> getCurrentUser(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.user(); // CustomUserDetails 내부에 저장된 실제 User 엔티티를 꺼냄
    return new ApiResponse<>(userService.getProfile(user));
  }

  @GetMapping("/role-check")
  @Operation(summary = "유저 역할 조회 API", description = "유저 역할을 조회하는 API입니다.")
  public ApiResponse<String> checkRole(@AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.user();

    if (user.getRole() == User.Role.SOCIAL_WORKER) {
      return new ApiResponse<>("사회복지사입니다.");
    } else if (user.getRole() == User.Role.USER) {
      return new ApiResponse<>("일반 사용자입니다.");
    }
    return new ApiResponse<>("알 수 없는 역할");
  }

  // 노인 할당
  @PostMapping("/assign-elderly")
  @Operation(summary = "노인 할당 API", description = "담당 노인을 할당하는 API입니다.")
  public ApiResponse<Void> assignElderly(@RequestBody @Valid ElderlyAssignmentRequest request) {
    userService.assignElderly(request.elderlyId(), request.socialWorkerId());
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  // 노인 할당해제
  @PostMapping("/unassign-elderly")
  @Operation(summary = "노인 할당 해제 API", description = "담당 노인 할당을 해제 API입니다.")
  public ApiResponse<Void> unassignElderly(@RequestBody @Valid ElderlyAssignmentRequest request) {
    userService.unassignElderly(request.elderlyId(), request.socialWorkerId());
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PatchMapping("/me")
  @Operation(summary = "회원정보 수정 API", description = "이름, 주소, 전화번호, (노인의 경우 보호자 정보)를 수정합니다.")
  public ApiResponse<Void> updateUserInfo(
      @AuthenticationPrincipal Long userId, @RequestBody @Valid UserUpdateRequest request) {
    userService.updateUserInfo(userId, request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }
}
