<%@page import="com.rws.utility.BrandingUtility"%>
<%@page import="com.rws.utility.PolicyRenewalUtility"%>
<%@page import="com.rws.utility.PolicyEmailCampaign"%>
<%@page import="net.rws.common.ValidationUtility"%>
<%@ page import="net.rws.common.servlets.GenericForm" %>
<%@ page import="com.rws.servlets.Constants" %>
<%@ page import="com.rws.servlets.FormActions" %>
<%
String requestURL = request.getParameter(GenericForm.REQUEST_URI);
String name = request.getParameter(PolicyRenewalUtility.NAME);
String id = request.getParameter(GenericForm.ID);
String policyNumber = request.getParameter(Constants.POLICY_NUMBER);
String state = request.getParameter(Constants.STATE);
String zipCode = request.getParameter(Constants.ZIP_CODE);
String endDate = request.getParameter(PolicyRenewalUtility.END_DATE);
String renewBy = request.getParameter(PolicyRenewalUtility.RENEW_BY);
String monthlyPayment = request.getParameter(PolicyRenewalUtility.MONTHLY_PAYMENT);

String userId = request.getParameter(Constants.USER_ID);

String renewalLink1 = request.getParameter(PolicyRenewalUtility.RENEWAL_LINK_1);
String renewalLink2 = request.getParameter(PolicyRenewalUtility.RENEWAL_LINK_2);
String declineLink = request.getParameter(PolicyRenewalUtility.DECLINE_LINK);
String footerImageLink = request.getParameter(PolicyRenewalUtility.FOOTER_IMAGE_LINK);

String companyName = BrandingUtility.getInstance().getCompanyName(state, zipCode);
String companyAbbreviation = BrandingUtility.getInstance().getCompanyAbbreviation(state, zipCode);
String imagePrefix = BrandingUtility.getInstance().getWebImagePrefix(state, zipCode);
String loginLink = PolicyEmailCampaign.getPolicyHolderLoginLink(userId, id, policyNumber);

%>
<img src="http://signup.residentialwarrantyservices.com/images<%=imagePrefix %>/EmailHeader.jpg"/>
<div style="width: 560px; background-color: black; color: white; font-family: sans-serif; font-size: 16px; padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 20px">
	<a style="color: white; text-decoration: none" href="<%=renewalLink1%>">Order Now</a> | 
	<a style="color: white; text-decoration: none" href="http://maintenanceguide.residentialwarrantyservices.com">Maintenance Guide</a> | 
	<a style="color: white; text-decoration: none" href="http://residentialwarrantyservices.com/submit-a-claim/">Make A Claim</a> | 
	<a style="color: white; text-decoration: none" href="http://residentialwarrantyservices.com/request-info/">Contact Us</a> | 
	<a style="color: white; text-decoration: none" href="<%=loginLink%>">Account Login</a>
</div>
<div style="width: 580px; padding: 10px; font-family: sans-serif;">
	Good morning <%=name %>,<br/>
	<br/>
	We appreciate you entrusting us with the protection of your home and want to remind you that your home warranty will be expiring on <%=endDate %>. Our job is to make 
	sure that there are no gaps in your coverage. All we need to do to ensure your warranty protection continues is update your payment information by clicking 
	<a href="<%=renewalLink1%>">HERE</a>.<br/>
	<br/>
	The BEST part is that we offer renewing clients monthly payments of only $<%=monthlyPayment %> with the same great coverage you're used to. This saves you $60 per 
	year!<br/>
	<br/>
	Click <a href="<%=renewalLink2%>">HERE</a> now to extend your coverage.
</div>
<img src="http://signup.residentialwarrantyservices.com/images<%=imagePrefix %>/EmailFooter.jpg"/>
<div style="width: 600px; font-family: sans-serif; text-align: center; font-size: 10px; margin-top: 15px;">
	<a href="<%=declineLink%>">Decline Coverage / Opt-out</a>
</div>
<%
if (ValidationUtility.isValidString(requestURL)) {
%>
<div style="width: 580px; padding: 10px; font-family: sans-serif;margin-top: 15px">
	Links not working? Trouble viewing this email? Try copy and pasting the link below into your web browser.<br/>
	<br/>
	<a href="<%=requestURL%>"><%=requestURL %></a><br/>
	<br/>
</div>
<%
}
%>