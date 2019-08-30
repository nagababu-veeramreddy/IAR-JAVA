/**
 * 
 */
package com.fb.in.app.reporting.controller;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.BindingProvider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fb.in.app.reporting.generated.FishbowlSSO;
import com.fb.in.app.reporting.generated.FishbowlSSOSoap;
import com.fb.in.app.reporting.model.auth.UserAuth;
import com.fb.in.app.reporting.model.vo.BrandVo;
import com.fb.in.app.reporting.request.BrandRequest;
import com.fb.in.app.reporting.request.PagingRequest;
import com.fb.in.app.reporting.response.BrandListResponse;
import com.fb.in.app.reporting.service.UserService;
import com.fb.in.app.reporting.sso.auth.AuthUtil;

/**
 * @author SKumar7
 *
 */
@Controller
//@RequestMapping("/ir/userAuth")
public class ReportController {
	@Autowired
	UserService userService;

	private final static Logger logger = LoggerFactory.getLogger(ReportController.class);
	private static final String fbLoginCookieName = "ASPXFORMSAUTH";

	@RequestMapping("/report")
	public String getReport(@RequestParam(name = "ID", required = false) String Id,
			@RequestParam(required = false) String SiteId, @RequestParam int bid, HttpServletRequest request,
			HttpServletResponse httpServletResponse) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

		logger.info("ID : " + Id);
		logger.info("SiteId : " + SiteId);
		logger.info("bid : " + bid);

		boolean isAspxCookiePresent = false;
		// Need to fetch from properties file
		int appId = 1000;

		String fishFrameSessionEnv = null;
		String fishFrameSessionId = null;
		String irDomain = null;
		String redirectServerName = null;
		String soapUrl = null;
		String redirectUrl = null;

		Cookie[] cookies = request.getCookies();
		String domain = request.getServerName();
		logger.info("domain : " + domain);
		fishFrameSessionEnv = AuthUtil.getFishFrameSessionEnv(domain);
		irDomain = AuthUtil.getDomain(domain);
		redirectServerName = AuthUtil.getRedirectServerName(domain);
		soapUrl = AuthUtil.getSoapUrl(domain);

		logger.info("fishFrameSessionEnv : " + fishFrameSessionEnv);
		logger.info("irDomain : " + irDomain);
		logger.info("redirectServerName : " + redirectServerName);
		logger.info("soapUrl : " + soapUrl);
		logger.info("domain : " + domain);

		if (Id == null || Id.isEmpty()) {
			String redirectURL = "https://" + soapUrl
					+ "/SSO/Navigator/InitializeTargetApp?bid=34&ReturnUrl=/ir/userAuth/report";
			if (SiteId != null && SiteId.trim().length() > 0) {
				redirectURL = redirectURL + "&SiteId=" + SiteId;
			}
			logger.info("redirectURL : " + redirectURL);
			return "redirect:" + redirectURL;
		}
		if (cookies != null) {
			for (Cookie ck : cookies) {
				logger.info("Cookie Name : " + ck.getName());
				logger.info("Cookie Domain : " + ck.getDomain());
				if (ck.getName().contains(fbLoginCookieName)) {
					logger.info("AspxCookie is Present and hence proceeding");
					isAspxCookiePresent = true;
				}
				if (fishFrameSessionEnv.equalsIgnoreCase(ck.getName())) {
					fishFrameSessionId = ck.getValue().split("=")[1];
					logger.info("fishFrameSessionId : " + fishFrameSessionId);
				}
			}
			if (!isAspxCookiePresent) {
				logger.info("AspxCookie is not present and hence redirecting to login page");
				String redirectURL = "https://" + soapUrl + "/Public/Login.aspx?ReturnUrl=%2f";
				logger.info("redirectURL : " + redirectURL);
				return "redirect:" + redirectURL;
			}
		} else {
			logger.info("There is no Cookie. Hence redirecting to login page");
			String redirectURL = "https://" + soapUrl + "/Public/Login.aspx?ReturnUrl=%2f";
			logger.info("redirectURL : " + redirectURL);
			return "redirect:" + redirectURL;
		}
		FishbowlSSO fishbowlSSO = new FishbowlSSO();
		FishbowlSSOSoap port = fishbowlSSO.getPort(FishbowlSSOSoap.class);
		BindingProvider provider = (BindingProvider) port;

		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"https://" + soapUrl + "/Public/FishbowlSSO.asmx");

		logger.info("AppId : " + appId);
		logger.info("Session Value : " + Id);
		logger.info("Fish Frame SessionId : " + fishFrameSessionId);
		String responseString = port.getAuthTicket(appId, Id, new BigDecimal(fishFrameSessionId));
		logger.info("GetAuthTicket Response : " + responseString);
		if (responseString != null) {
			String tokenString = StringUtils.substringBetween(responseString, ">", "<");
			logger.info("Auth Token : " + tokenString);
			if (tokenString != null && !StringUtils.isEmpty(tokenString)) {
				String userDetailsStr = port.getTicketedUserName(Id, tokenString);
				logger.info("GetTicketedUserName Response : " + userDetailsStr);
				String userDetails = StringUtils.substringBetween(userDetailsStr, ">", "<");
				logger.info("User Details : " + userDetails);
				String encryptedStr = AuthUtil.encrypted(userDetails);
				logger.info("Encrypted User Details  : " + encryptedStr);
				Cookie cookie = new Cookie("IR_SessionId", encryptedStr);
				cookie.setDomain(irDomain);
				cookie.setPath("/");
				httpServletResponse.addCookie(cookie);
				if (cookies != null) {
					for (Cookie ck : cookies) {
						httpServletResponse.addCookie(ck);
					}
				}
				if (SiteId != null && SiteId.trim().length() > 0) {
					BrandVo brandVo = null;
					try {
						brandVo = userService.getBrandRecordBySiteId(SiteId);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (brandVo != null && brandVo.getBrandId() > -1) {
						// redirectUrl = "https://" + redirectServerName + "/#/home?ID=" + Id +
						// "&bid=34&SiteId=" + SiteId;
						httpServletResponse.addCookie(AuthUtil.getBrandIdCookies(brandVo.getBrandId() + "",
								AuthUtil.getDomain(request.getServerName())));
					} else {
						// response is null or brand id is <0
					}

				} else {
					logger.info("Site ID is null");
					logger.info("Checking if user will have only one brand");
					UserAuth userAuth = AuthUtil.getUserDetails(userDetails);
					BrandRequest brandRequest = new BrandRequest();
					PagingRequest paging = new PagingRequest();
					paging.setPageNo(1);
					paging.setPageSize(10);
					brandRequest.setPaging(paging);
					logger.info("userService  getBrand calling to get user Brands");
					BrandListResponse response = null;
					try {
						response = userService.getBrand(userAuth.getUserId(), userAuth.getClientId(), brandRequest);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (response != null && response.getSuccessFlag() == true && response.getBrandsCount() == 1) {
						logger.info("user is having single brand..");
						SiteId = response.getBrandList().get(0).getSiteId().toString();
						// redirectUrl = "https://" + redirectServerName + "/#/home?ID=" + Id +
						// "&bid=34&SiteId=" + SiteId;
						httpServletResponse.addCookie(
								AuthUtil.getBrandIdCookies(response.getBrandList().get(0).getBrandId() + "", irDomain));

					} else {
						logger.info("user is having multiple brands..");
						// redirectUrl = "https://" + redirectServerName + "/#/home?ID=" + Id +
						// "&bid=34&SiteId=" + SiteId;

					}
				}
				logger.info("redirectURL : " + redirectUrl);
				// return "redirect:" + redirectUrl;
				return "reportDashboard";

			} else {
				String redirectURL = "https://" + soapUrl + "/Public/Login.aspx?ReturnUrl=%2f";
				logger.info("redirectURL : " + redirectURL);
				return "redirect:" + redirectURL;
			}
		} else {
			String redirectURL = "https://" + soapUrl + "/Public/Login.aspx?ReturnUrl=%2f";
			logger.info("redirectURL : " + redirectURL);
			return "redirect:" + redirectURL;
		}
	}
}
