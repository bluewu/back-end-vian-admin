package com.vian.admin.client;

import com.vian.admin.domain.AccessRequest;
import com.vian.microservice.client.AuthorizedFeignClient;
import com.vian.web.domain.res.VianResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@AuthorizedFeignClient(name = "vian-oauth2")
public interface VianOauth2Client {

  /**
   * 通过logon获得token
   *
   * @param request
   * @return
   */
  @RequestMapping(
      value = "/api/token/accessToken",
      method = RequestMethod.POST,
      produces = "application/json")
  VianResponse<OAuth2AccessToken> postAccessToken(AccessRequest request);
}
