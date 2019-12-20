package com.vian.admin.service;

import com.vian.admin.VianAdminApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VianAdminApp.class)
public class AdminUserServiceTest {
  @Inject AdminUserService adminUserService;

  @Inject PasswordEncoder passwordEncoder;

  @Test
  public void test() {}
}
