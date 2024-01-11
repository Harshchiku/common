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
String linkColor = BrandingUtility.getInstance().getThemeColor(state, zipCode);
boolean isAdvantagePlan = renewalLink1 != null && renewalLink1.contains("renew-advantage-plan-application");

%>
<img src="http://signup.residentialwarrantyservices.com/images/renewals/v1<%=imagePrefix %>/PolicyRenewal159.jpg"/>
<div style="width: 560px; padding: 20px; font-family: sans-serif; margin-top: 20px">
	<div style="width: 520px; padding: 20px; font-family: sans-serif; font-size: 14px; margin-top: 20px">
		Hello <%=name %>,<br/>
		<br/>
		There is still time to secure this amazing offer. Receive a structural warranty, a minimum of $350 annual value, at no charge through the remaining term of 
		your existing home warranty policy.<br/>
		<br/>
		A limited structural warranty includes guaranteed repairs and the ability to choose your own contractor. For exclusions and limitations, see full contract. To 
		receive the structural warranty, please go to the <a style="text-decoration: none; color: <%=linkColor %>" href="<%=renewalLink1%>"><%=companyAbbreviation %> 
		Customer Portal</a> and select the option to receive the no charge structural warranty in exchange for providing a payment method with automatic monthly 
		installments until you cancel the warranty, which you can do at any time.<br/>
		<br/>
		<div style="text-align: center; width: 100%; font-size: 18px; font-style: italic; margin-top: 20px; margin-bottom: 20px"><b>Final Day! Don't Delay!</b></div>
		<br/>
		Questions? Call us at <b>1-800-544-8156.</b>
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