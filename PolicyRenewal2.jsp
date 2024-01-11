<%@page import="com.rws.utility.BrandingUtility"%>
<%@page import="com.rws.utility.PolicyEmailCampaign"%>
<%@page import="com.rws.utility.PolicyRenewalUtility"%>
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

boolean isAdvantagePlan = renewalLink1 != null && renewalLink1.contains("renew-advantage-plan-application");

%>
<img src="http://signup.residentialwarrantyservices.com/images/renewals/v1<%=imagePrefix %>/PolicyRenewal2.jpg"/>
<div style="width: 560px; padding: 20px; font-family: sans-serif; margin-top: 20px">
	<span style="font-weight: bold; font-size: 18px">
		Your Home Warranty Expires on <%=endDate %>
	</span>
	<div style="width: 520px; padding: 20px; font-family: sans-serif; font-size: 14px; margin-top: 20px">
		Hello <%=name %>,<br/>
		<br/>
		Your <%=companyName %> home warranty expires on <%=endDate %>.<br/>
		<br/>
		To make sure there are no gaps in your coverage, <b><a href="<%=renewalLink1%>">visit the secure <%=companyAbbreviation %> Customer Portal today</a></b> to update 
		your information and submit your order.<br/>
		<br/>
		As a renewing customer, you're eligible to receive the same coverage at a reduced monthly price, <b>Saving You $60</b> per year.<br/>
		<br/>
		<br/>
		<b>FACT:</b> <i>A home warranty customer averages one claim per year. Reduce the financial burden of a mechanical malfunction by extending your coverage 
		today.</i><br/>
		<br/>
		Prefer to talk with us? We're here at <b>1-800-544-8156.</b>
	</div>
</div>
<div style="width: 600px; text-align: center; margin-top: 20px">
	<a href="<%=renewalLink2%>">
		<img src="http://signup.residentialwarrantyservices.com/images/renewals/v1<%=imagePrefix %>/renew-button.jpg"/>
	</a>
</div>
<div style="width: 600px; text-align: center; margin-top: 40px">
	<img src="<%=footerImageLink%>"/>
</div>
<div style="width: 600px; font-family: sans-serif; text-align: center; font-size: 10px; margin-top: 15px;">
	<a href="<%=declineLink%>">Decline Coverage / Opt-out</a>
</div>
<%
if (ValidationUtility.isValidString(requestURL)) {
%>
<div style="width: 580px; padding: 10px; font-family: sans-serif;margin-top: 15px">
	Links not working? Trouble viewing this email? Try copy and pasting the link below into your web browser.<br/>
	<br/>
	<a style="" href="<%=requestURL%>"><%=requestURL %></a><br/>
	<br/>
</div>
<%
}
%>