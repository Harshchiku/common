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
String address = request.getParameter(Constants.STREET);
String state = request.getParameter(Constants.STATE);
String zipCode = request.getParameter(Constants.ZIP_CODE);
String endDate = request.getParameter(PolicyRenewalUtility.END_DATE);
String renewBy = request.getParameter(PolicyRenewalUtility.RENEW_BY);
String monthlyPayment = request.getParameter(PolicyRenewalUtility.MONTHLY_PAYMENT);

String userId = request.getParameter(Constants.USER_ID);

String renewalLink1 = request.getParameter(PolicyRenewalUtility.RENEWAL_LINK_1);
//String renewalLink2 = request.getParameter(PolicyRenewalUtility.RENEWAL_LINK_2);
String renewalLink2 = "tel:317-660-7038";

String declineLink = request.getParameter(PolicyRenewalUtility.DECLINE_LINK);

String companyName = BrandingUtility.getInstance().getCompanyName(state, zipCode);
String companyAbbreviation = BrandingUtility.getInstance().getCompanyAbbreviation(state, zipCode);
String imagePrefix = BrandingUtility.getInstance().getWebImagePrefix(state, zipCode);
String loginLink = PolicyEmailCampaign.getPolicyHolderLoginLink(userId, id, policyNumber);
String footerImageLink = request.getParameter(PolicyRenewalUtility.FOOTER_IMAGE_LINK) != null ?
		request.getParameter(PolicyRenewalUtility.FOOTER_IMAGE_LINK) : 
			"http://signup.residentialwarrantyservices.com/images/renewals/v1"+imagePrefix+"/footer-logo.png";

boolean isAdvantagePlan = renewalLink1 != null && renewalLink1.contains("renew-advantage-plan-application");

%>
<img src="http://signup.residentialwarrantyservices.com/images/renewals/v1<%=imagePrefix %>/PolicyExpiration.jpg"/>
<div style="width: 560px; padding: 20px; font-family: sans-serif; margin-top: 20px">
	<span style="font-weight: bold; font-size: 18px">
		Policy # <%=policyNumber %> at <%=address %> is expiring today.
		Last Call: Renew Your Home Warranty Now!
	</span>
	<div style="width: 520px; padding: 20px; font-family: sans-serif; font-size: 14px; margin-top: 20px">
		Hi <%=name %>,<br/><br/>

			Your home warranty with RWS powered by Porch is expiring today. Act now to ensure uninterrupted coverage and continued protection for your home.<br/><br/>
			
			Renew Today for Uninterrupted Protection:<br/><br/>
			
			<ul>
			<li>Shield your home from unexpected breakdowns - essential systems and appliances covered.</li>
			<li>Ensure your internal systems and appliances are fully safeguarded, with handyman services and double appliance coverage.</li>
			<li>Say goodbye to unexpected expenses due to wear and tear, thanks to our all-encompassing warranty.</li>
			<li>Hassle-Free Claims: Experience seamless claims managed by our Porch customer care team.</li>
			<li>Access trusted handyman services for all your home needs.</li>
			</ul>
			<br/><br/>
			Don't wait - renew now and stay protected!
			<br/>
			<br/>
			Just give us a call at <a href="tel:317-660-7038">317-660-7038</a>, and a Porch agent will guide you through the reinstatement process.
		<br/>
		Best regards,<br/> 
		
		Porch Specialist<br/> 
		<i>Your protection, our priority.</i><br/><br/>
		
		For customers in California and Washington, you are able to renew in our Premier Whole Home Warranty Program by calling 
		<a href="tel:317-660-7038">317-660-7038</a>.
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