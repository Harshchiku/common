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
String address = request.getParameter(Constants.STREET);
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
String linkColor = BrandingUtility.getInstance().getThemeColor(state, zipCode);
boolean isAdvantagePlan = renewalLink1 != null && renewalLink1.contains("renew-advantage-plan-application");

%>
<div style="width: 560px; padding: 20px; font-family: sans-serif; margin-top: 20px">
	<div style="width: 520px; padding: 20px; font-family: sans-serif; font-size: 14px; margin-top: 20px">
		Hello <%=name %>,<br/>
		<br/>
		This message is regarding Policy # <%=policyNumber %> at <%=address %>, set to expire on <%=endDate %>.
		<br/>
		As a valued policy holder, we're delighted to share our newest warranty product that offers additional value to all homeowners. With the new Handyman service addition, you'll want to refer our warranty to all your friends and family!
		<br/>
		<br/>
		Our Porch Warranty will now be available for all policyholders set to expire within the next 30days. All policyholders that renew within that timeframe will have immediate access to our handyman services, as well as receive an additional 5% discount. Any renewal after will be obliged to our 30-day mandatory wait period.
		<br/>
		<br/>
		<br/>
		Below are the two coverage options available for renewal. 
		<br/>
		<br/>
		Option # 1 <br/>
		Premier Warranty: <br/>
		Pricing based on square footage and other factors of the home<br/>
		(Available to existing policy holders that chose this product for one final year as we launch our newest product - Porch Warranty)
		<br/>
		<br/>
		Option # 2<br/>
		Porch Warranty:<br/>
		Flat rate pricing regardless of size of home <br/>
		(Available to all existing and new policyholders and now includes handyman services in your area as an added value to better serve our homeowner community!)
		<br/>
		All Policyholders will have the option to renew and/or upgrade to the Premier Warranty by July 1st 2023. Policyholders that have not opted for their choice of warranty by July 1st 2023, will be automatically opted for our Porch Warranty come time of their renewal.
		<br/>
		<br/>
		
		Please give us a call at your earliest convenience so you don't miss out on all the discounts and offers available to you. Our Office line is 317-660-7038. Thank you for the opportunity to serve!
		
	</div>
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