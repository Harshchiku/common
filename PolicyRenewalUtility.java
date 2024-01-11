package com.rws.utility;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import net.isg.data.export.recallchek.SendEmail;
import net.rws.common.FormatUtility;
import net.rws.common.ValidationUtility;
import net.rws.common.data.PersistenceInterface;
import net.rws.common.servlets.GenericForm;
import net.rws.common.util.DatabaseRoutines;
import net.rws.common.util.HttpUtil;
import net.rws.common.util.SearchCriteria;
import net.rws.common.util.SearchCriteria.searchTypes;

import com.rws.data.EmailEvent;
import com.rws.data.EmailEventLink;
import com.rws.data.User;
import com.rws.data.policy.Policy;
import com.rws.servlets.Constants;
import com.rws.servlets.FormActions;

public class PolicyRenewalUtility {

	private static Logger log = Logger.getLogger(PolicyRenewalUtility.class);
	
	private static final int EVERGREEN_RENEWAL_WINDOW = 21;
	
	public static String NAME = "name";
	public static String END_DATE = "endDate";
	public static String RENEWAL_LINK_1 = "renewalLink1";
	public static String RENEWAL_LINK_2 = "renewalLink2";
	public static String DECLINE_LINK = "declineLink";
	public static String FOOTER_IMAGE_LINK = "footerImageLink";
	public static String MONTHLY_PAYMENT = "monthlyPayment";
	public static String RENEW_BY = "renewBy";
	
	public static int RENEWAL_CALL_DAYS_FROM_EXPIRATION = 20;
	public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_180 = 180;
	//public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_173 = 173;
	//public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_159 = 159;
	//public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_30 = 30;
	public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_20 = 20;
	public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_5 = 5;
	//public static int RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_2 = 2;
	public static final int EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION = 0;
	//public static final int EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION_2 = 2;
	public static final int EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION_10 = 10;
	
	public static final String RENEWAL_NOTICE_180_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal180.jsp";
	//public static final String RENEWAL_NOTICE_173_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal173.jsp";
	//public static final String RENEWAL_NOTICE_159_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal159.jsp";
	//public static final String RENEWAL_NOTICE_30_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal30.jsp";
	public static final String RENEWAL_NOTICE_20_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal20.jsp";
	public static final String RENEWAL_NOTICE_5_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal5.jsp";
	//public static final String RENEWAL_NOTICE_2_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyRenewal2.jsp";
	public static final String EXPIRATION_NOTICE_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyExpiration.jsp";
	//public static final String EXPIRATION_NOTICE_2_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyExpiration2.jsp";
	public static final String EXPIRATION_NOTICE_10_LINK = "http://signup.residentialwarrantyservices.com/emails/renewals/v1/PolicyExpiration10.jsp";
	
	private static final String EXPIRATION_DATE = "$EXPIRATION_DATE";
	
	private static final String RENEWAL_NOTICE_180_SUBJECT = "Elevate Your Porch Home Warranty + Handyman Services!";
	//private static final String RENEWAL_NOTICE_173_SUBJECT = "You still have time to Add a Structural Warranty at No Cost for the remaining Term of Your Warranty";
	//private static final String RENEWAL_NOTICE_159_SUBJECT = "Last Day to Add a Structural Warranty at No Cost for the Remaining Term of Your Home Warranty";
	//private static final String RENEWAL_NOTICE_30_SUBJECT = "Your Home Warranty Expires on "+EXPIRATION_DATE;
	private static final String RENEWAL_NOTICE_20_SUBJECT = "Your Home Warranty is Expiring Soon - Act Now & Save Big!";
	private static final String RENEWAL_NOTICE_5_SUBJECT = "Last Chance: Renew Your Home Warranty with RWS powered by Porch!";
	//private static final String RENEWAL_NOTICE_2_SUBJECT = "Just two days until your home warranty expires";
	public static final String EXPIRATION_NOTICE_SUBJECT = "Urgent: Your Home Warranty Expires Today!";
	//private static final String EXPIRATION_NOTICE_2_SUBJECT = "Your home warranty with Residential Warranty Services has expired";
	public static final String EXPIRATION_NOTICE_10_SUBJECT = "Reinstate Your Home Warranty Coverage Now!";
	
	private static final String SIMPLE_WARRANTY_RENEWAL_LINK = "https://residentialwarrantyservices.com/renew-simple-warranty-application.xhtml?";
	private static final String ADVANTAGE_PLAN_RENEWAL_LINK = "https://residentialwarrantyservices.com/renew-advantage-plan-application.xhtml?";
	
	private static final String DECLINE_RENEWAL_LINK = "https://residentialwarrantyservices.com/decline-renewal.xhtml?";
	
	private static final String FOOTER_IMAGE_URL = "http://signup.residentialwarrantyservices.com/images/renewals/v1";
	private static final String FOOTER_IMAGE_FILE = "/footer-logo.png";
	
	private static final Map<String, String> urlToEmailType = getUrlToEmailTypes();
	
	public static final String[] COVERAGE_BUYERS_RENEWAL = 
			new String[]{Constants.COVERAGE_OPTION_BUYERS, Constants.COVERAGE_OPTION_RENEWAL, Constants.COVERAGE_OPTION_COMPETITOR_RENEWAL};
	private static final String[] COVERAGE_BUYERS = new String[]{Constants.COVERAGE_OPTION_BUYERS};
	
	/*private static boolean isEvergreen(String link) {
		
		List<String> evergreenLinks = 
				Arrays.asList(
						new String[] {RENEWAL_NOTICE_180_LINK, RENEWAL_NOTICE_173_LINK, RENEWAL_NOTICE_159_LINK});
		
		return evergreenLinks.contains(link);
		
	}*/
	
	public static void sendNotices(Date currentDate) {
		
		Date targetEndDate = null;
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_CALL_DAYS_FROM_EXPIRATION).toDate());
		sendCalls(targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_180).toDate());
		sendNotices(RENEWAL_NOTICE_180_LINK, RENEWAL_NOTICE_180_SUBJECT, targetEndDate, null, COVERAGE_BUYERS);
		
/*		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_173).toDate());
		sendNotices(RENEWAL_NOTICE_173_LINK, RENEWAL_NOTICE_173_SUBJECT, targetEndDate, null, COVERAGE_BUYERS);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_159).toDate());
		sendNotices(RENEWAL_NOTICE_159_LINK, RENEWAL_NOTICE_159_SUBJECT, targetEndDate, null, COVERAGE_BUYERS);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_30).toDate());
		sendNotices(RENEWAL_NOTICE_30_LINK, RENEWAL_NOTICE_30_SUBJECT, targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
*/		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_20).toDate());
		sendNotices(RENEWAL_NOTICE_20_LINK, RENEWAL_NOTICE_20_SUBJECT, targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_5).toDate());
		sendNotices(RENEWAL_NOTICE_5_LINK, RENEWAL_NOTICE_5_SUBJECT, targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
		
		//targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).plusDays(RENEWAL_NOTICE_DAYS_FROM_EXPIRATION_2).toDate());
		//sendNotices(RENEWAL_NOTICE_2_LINK, RENEWAL_NOTICE_2_SUBJECT, targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).minusDays(EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION).toDate());
		sendNotices(EXPIRATION_NOTICE_LINK, EXPIRATION_NOTICE_SUBJECT, targetEndDate, null ,COVERAGE_BUYERS_RENEWAL);
		
		//targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).minusDays(EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION_2).toDate());
		//sendNotices(EXPIRATION_NOTICE_2_LINK, EXPIRATION_NOTICE_2_SUBJECT, targetEndDate, Constants.SIMPLE, COVERAGE_BUYERS_RENEWAL);
		
		targetEndDate = FormatUtility.adjustDateBeginningOfDay(new DateTime(currentDate).minusDays(EXPIRATION_NOTICE_DAYS_FROM_EXPIRATION_10).toDate());
		sendNotices(EXPIRATION_NOTICE_10_LINK, EXPIRATION_NOTICE_10_SUBJECT, targetEndDate, null, COVERAGE_BUYERS_RENEWAL);
		
	}
	
	private static void sendNotices(String contentLink, String subjectPrefix, Date targetDate, String baseCoverageType, String[] coverage) {
		
		PersistenceInterface rwsPi = null;
		List<Long> policyIds = null;
		
		try {
			
			rwsPi = new PersistenceInterface(Constants.DB_RWS);
			policyIds = getEligiblePolicies(rwsPi, targetDate, baseCoverageType, coverage, false); //isEvergreen(contentLink));
			
			for (Long policyId : policyIds) {
				
				Policy policy = (Policy)DatabaseRoutines.getObjectById(rwsPi, policyId, Policy.class);
				EmailEvent emailEvent = sendNotice(contentLink, subjectPrefix, policy);
				
				if (emailEvent != null) { policy.getEmailEvents().add(emailEvent); }
				
			}
			
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
		} finally {
			
			if (rwsPi != null) {
				
				rwsPi.close();
				
			}
			
		}
		
	}
	
	public static String getPolicyRenewalLinks(Policy policy) {
		
		return Constants.RWS_WEBSITE+FormActions.GET_POLICY_RENEWAL_LINKS+"?"+GenericForm.ID+"="+policy.getId()+"&"+Constants.POLICY_NUMBER+"="+policy.getPolicyNumber();
		
	}
	
	public static void sendCalls(Date targetDate, String baseCoverageType, String[] coverage) {
		
		PersistenceInterface rwsPi = null;
		List<Long> policyIds = null;
		
		try {
			
			rwsPi = new PersistenceInterface(Constants.DB_RWS);
			policyIds = getEligiblePolicies(rwsPi, targetDate, baseCoverageType, coverage, false);
			
			for (Long policyId : policyIds) {
				
				Policy policy = (Policy)DatabaseRoutines.getObjectById(rwsPi, policyId, Policy.class);
				int policiesRenewed = getRenewedPolicies(policy);
				String confirmation = ZohoUtility.getInstance().insertPolicy(policy, policiesRenewed, getPolicyRenewalLinks(policy));
				
				if (ValidationUtility.isValidString(confirmation)) { policy.getMetaData().put(Constants.ZOHO_ID, confirmation); }
				
			}
			
		} finally {
			
			if (rwsPi != null) {
				
				rwsPi.close();
				
			}
			
		}
		
	}
	
	//Returns the number of times this policy has been renewed
	public static int getRenewedPolicies(Policy policy) {
		
		int renewedPolicies = 0;
		Policy renewedPolicy = policy;
		
		while ((renewedPolicy = renewedPolicy.getRenewedPolicy()) != null) {
			
			if (Constants.COVERAGE_OPTION_LISTING.equals(renewedPolicy.getCoverage())) break;
			
			renewedPolicies++;
			
		}
		
		
		return renewedPolicies;
		
	}
	
	public static EmailEvent sendNotice(String contentLink, String subjectPrefix, Policy policy) throws UnsupportedEncodingException {
		
		String subject = subjectPrefix.replace(EXPIRATION_DATE, FormatUtility.getDateString(policy.getEndDate())) + " for " + policy.getPolicyStreet();
		Map<String, String> parameters = getParameters(policy, contentLink);
		String url = null;
		EmailEvent emailEvent = null;
		String body = null;
		List<String> recipients = SendEmail.getEmailList(policy.getEmail());
		String renewalLink = parameters.get(RENEWAL_LINK_1);
		String footerImageLink = parameters.get(FOOTER_IMAGE_LINK);
		EmailEventLink renewalLink1Event = new EmailEventLink(renewalLink);
		EmailEventLink renewalLink2Event = new EmailEventLink(renewalLink);
		EmailEventLink footerImageLinkEvent = new EmailEventLink(footerImageLink);
		
		footerImageLinkEvent.setTracksOpen(true);
		
		parameters.put(RENEWAL_LINK_1, renewalLink1Event.getLink());
		parameters.put(RENEWAL_LINK_2, renewalLink2Event.getLink());
		parameters.put(FOOTER_IMAGE_LINK, footerImageLinkEvent.getLink());
		
		url = contentLink+HttpUtil.getParameterString(parameters);		
				
		try {
			url = url + "&"+GenericForm.REQUEST_URI+"="+URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e, e);
		}
		
		body = HttpUtil.getWebPage(url);
		
		if (recipients.size() == 0) {
			
			log.error("No valid recipients for Policy "+policy.getPolicyNumber());
			
		} else if (ValidationUtility.isValidString(body)) {
			
			log.info("Sending "+subject+" with end date "+policy.getEndDate()+": "+ url);
			
			if (SendEmail.sendMandrillHtmlEmail(Constants.RENEWAL_EMAIL, Constants.RENEWAL_EMAIL, recipients, new ArrayList<String>(), subject, body)) {
				
				emailEvent = 
						new EmailEvent(policy.getEmail(), urlToEmailType.get(contentLink), "", new Date(), url, null, new User(Constants.SYSTEM_AUTOMATION_USER_ID));
				
				renewalLink1Event.setEmailEvent(emailEvent);
				renewalLink2Event.setEmailEvent(emailEvent);
				footerImageLinkEvent.setEmailEvent(emailEvent);
				
				emailEvent.getLinks().add(renewalLink1Event);
				emailEvent.getLinks().add(renewalLink2Event);
				emailEvent.getLinks().add(footerImageLinkEvent);
				
			}
			
		} else {
			
			log.error("No content from "+url);
			
		}
		
		return emailEvent;
		
	}
	
	public static String getRenewalLink(Policy policy) throws UnsupportedEncodingException {
		
		String renewalLink = SIMPLE_WARRANTY_RENEWAL_LINK;
		String parameters = getRenewalLinkParameters(policy);
		
		renewalLink += parameters;
		
		return renewalLink;
		
	}
	
	private static String getRenewalLinkParameters(Policy policy) throws UnsupportedEncodingException {
		
		return "policyId="+policy.getId()+"&policyNumber="+policy.getPolicyNumber()+"&policyZipCode="+policy.getPolicyZipCode();
		
	}
	
	private static List<Long> getEligiblePolicies(PersistenceInterface rwsPi, Date targetDate, String baseCoverageType, String[] coverage, boolean isEvergreen) {
		
		List<Long> policies = null;
		List<SearchCriteria> constraints = new ArrayList<SearchCriteria>();
		
		constraints.add(new SearchCriteria("coverages", "orderedCoverage", searchTypes.ALIAS));
		constraints.add(new SearchCriteria("orderedCoverage.coverage", "coverage", searchTypes.ALIAS));
		
		constraints.add(new SearchCriteria(isEvergreen ? Constants.EVERGREEN_DECLINED : Constants.RENEWAL_DECLINED, false, searchTypes.EQUALITY));
		constraints.add(new SearchCriteria(Constants.EMAIL, targetDate, searchTypes.NOT_NULL));
		constraints.add(new SearchCriteria("endDate", targetDate, searchTypes.EQUALITY));
		constraints.add(new SearchCriteria(Constants.COVERAGE, Arrays.asList(coverage), searchTypes.IN));
		constraints.add(new SearchCriteria(Constants.PAYMENT_OPTIONS, Constants.PAYMENT_OPTION_EVERGREEN, searchTypes.INEQUALITY));
		constraints.add(new SearchCriteria("renewalPolicy", false, searchTypes.NULL));
		constraints.add(new SearchCriteria(Constants.STATUS, Arrays.asList(new String[]{Constants.STATUS_PAID, Constants.STATUS_EXPIRED, Constants.STATUS_PAYMENTS, Constants.STATUS_LATE, Constants.STATUS_PAST_DUE}), searchTypes.IN));
		
		if (baseCoverageType == null) {
			constraints.add(new SearchCriteria("coverage.warrantyType", Arrays.asList(new String[]{Constants.SIMPLE, Constants.ADVANTAGE_PLAN}), searchTypes.IN));
		} else {
			constraints.add(new SearchCriteria("coverage.warrantyType", baseCoverageType, searchTypes.EQUALITY));
		}
		
		constraints.add(new SearchCriteria("id", "id", searchTypes.DISTINCT_PROJECTION));
		
		policies = rwsPi.searchClassByCriteria(Policy.class, constraints);
		
		return policies;
		
	}
	
	public static Map<String, String> getParameters(Policy policy, String contentLink) throws UnsupportedEncodingException {
		
		Map<String, String> parameters = new HashMap<String, String>();
		int numClaims = policy.getValidClaims().size();
		float monthlyPayment = PolicyUtility.getRenewalMinPayment(PolicyUtility.convertToRenewalCoverages(policy.getCoverages(), numClaims) , policy.getRenewalDiscount());
		String declineLink = 
				//DECLINE_RENEWAL_LINK + getRenewalLinkParameters(policy) + (isEvergreen(contentLink) ? Constants.EVERGREEN_DECLINED : Constants.RENEWAL_DECLINED)+"=true";
				DECLINE_RENEWAL_LINK + getRenewalLinkParameters(policy) + Constants.RENEWAL_DECLINED+"=true";
		String renewalLink = getRenewalLink(policy);
		String state = policy.getPolicyState();
		String zipCode = policy.getPolicyZipCode();
		String imagePrefix = BrandingUtility.getInstance().getWebImagePrefix(state, zipCode);
		String footerImageLink = FOOTER_IMAGE_URL + imagePrefix + FOOTER_IMAGE_FILE;
		
		parameters.put(NAME, policy.getFirstName());
		parameters.put(GenericForm.ID, policy.getId()+"");
		parameters.put(Constants.POLICY_NUMBER, policy.getPolicyNumber());
		parameters.put(Constants.STATE, policy.getPolicyState());
		parameters.put(Constants.ZIP_CODE, policy.getPolicyZipCode());
		parameters.put(END_DATE, FormatUtility.getDateString(policy.getEndDate()));
		parameters.put(MONTHLY_PAYMENT, FormatUtility.formatPrice(monthlyPayment, 2));
		parameters.put(RENEW_BY, getRenewByDate(policy, contentLink));
		parameters.put(DECLINE_LINK, declineLink);
		parameters.put(RENEWAL_LINK_1, renewalLink);
		parameters.put(RENEWAL_LINK_2, renewalLink);
		parameters.put(FOOTER_IMAGE_LINK, footerImageLink);
		
		return parameters;
		
	}
	
	private static String getRenewByDate(Policy policy, String contentLink) {
		
		String renewByDate = null;
		
		if (RENEWAL_NOTICE_180_LINK.equals(contentLink)) {
			
			renewByDate = FormatUtility.getDateString(new DateTime().plusDays(EVERGREEN_RENEWAL_WINDOW).toDate());
			
		}/* else if (RENEWAL_NOTICE_173_LINK.equals(contentLink)) {
			
			renewByDate = FormatUtility.getDateString(new DateTime().plusDays(EVERGREEN_RENEWAL_WINDOW - 7).toDate());
			
		} */else {
			
			renewByDate = FormatUtility.getDateString(new DateTime(policy.getEndDate()).plusDays(Constants.RENEWAL_PERIOD).toDate());
			
		}
		
		return renewByDate;
		
	}
	
	private static Map<String, String> getUrlToEmailTypes() {
		
		Map<String, String> urlToEmailTypes = new HashMap<String, String>();
		
		urlToEmailTypes.put(RENEWAL_NOTICE_180_LINK, "Renewal Notice 180");
		/*urlToEmailTypes.put(RENEWAL_NOTICE_173_LINK, "Renewal Notice 173");
		urlToEmailTypes.put(RENEWAL_NOTICE_159_LINK, "Renewal Notice 159");*/
		//urlToEmailTypes.put(RENEWAL_NOTICE_30_LINK, "Renewal Notice 30");
		urlToEmailTypes.put(RENEWAL_NOTICE_20_LINK, "Renewal Notice 20");
		urlToEmailTypes.put(RENEWAL_NOTICE_5_LINK, "Renewal Notice 5");
		//urlToEmailTypes.put(RENEWAL_NOTICE_2_LINK, "Renewal Notice 2");
		urlToEmailTypes.put(EXPIRATION_NOTICE_LINK, "Expiration Notice");
		//urlToEmailTypes.put(EXPIRATION_NOTICE_2_LINK, "Expiration Notice 2");
		urlToEmailTypes.put(EXPIRATION_NOTICE_10_LINK, "Expiration Notice 10");
		
		return urlToEmailTypes;
		
	}
	
	public static void updateRenewalCommissionFieldsInZoho(Date currentDate) {
		
		PersistenceInterface pi = new PersistenceInterface(Constants.DB_RWS);
		Date paymentDate = new DateTime(currentDate).minusDays(1).toDate();
		List<Object[]> policyInfos = getRenewalPoliciesWithFirstPayment(pi, paymentDate);
		
		updateRenewalCommissionFieldsInZoho(pi, policyInfos);
		
		pi.close();
		
	}
	
	private static List<Object[]> getRenewalPoliciesWithFirstPayment(PersistenceInterface pi, Date paymentDate) {
		
		String startDateString = FormatUtility.getDateTimeSQLString(FormatUtility.adjustDateBeginningOfDay(paymentDate));
		String endDateString = FormatUtility.getDateTimeSQLString(FormatUtility.adjustDateEndOfDay(paymentDate));
		String query = 
				"select p.id, min(t.transactiondate) from policy p inner join transaction t on t.policy_id=p.id where p.coverage='"+Constants.COVERAGE_OPTION_RENEWAL+
				"' and p.status != '"+Constants.STATUS_CANCELED+"' and t.status='"+Constants.APPROVED+"' group by 1 having min(t.transactiondate) between '"+
				startDateString+"' and '"+endDateString+"'";
		List<Object[]> policyInfos = pi.getSession().createSQLQuery(query).list();
		
		log.info("Renewal Policies w/ 1st Payment Query: "+query);
		
		return policyInfos;
		
	}
	
	private static void updateRenewalCommissionFieldsInZoho(PersistenceInterface pi, List<Object[]> policyInfos) {
		
		for (Object[] policyInfo : policyInfos) {
			
			long policyId = ((BigInteger)policyInfo[0]).longValue();
			Date firstPaymentDate = (Date)policyInfo[1];
			Policy policy = (Policy)DatabaseRoutines.getObjectById(pi, policyId, Policy.class);
			
			updateRenewalCommissionFieldsInZoho(policy, firstPaymentDate);
			
		}
		
	}
	
	private static void updateRenewalCommissionFieldsInZoho(Policy policy, Date firstPaymentDate) {
		
		Policy renewedPolicy = policy.getRenewedPolicy();
		Date firstContactMade = renewedPolicy != null && renewedPolicy.getMetaData().containsKey(Constants.FIRST_CONTACT_MADE) ? 
				FormatUtility.getDateFromField((String)renewedPolicy.getMetaData().get(Constants.FIRST_CONTACT_MADE)) : null;
		boolean commissionEligible = firstContactMade != null && firstContactMade.before(new DateTime(firstPaymentDate).plusMinutes(30).toDate());//increase paid date/time by a half hour to give leeway if rep processes payment on phone before logging contact event
		String zohoId = renewedPolicy != null ? renewedPolicy.getMetaData().get(Constants.ZOHO_ID) : null;
		
		if (ValidationUtility.isValidString(zohoId)) {
			
			log.info("Setting Zoho Renewal Commissions Fields for "+zohoId+" on renewal policy "+policy.getPolicyNumber()+" Commission Eligible: "+commissionEligible);
			
			ZohoUtility.getInstance().updateRenewalProcessedFields(zohoId, true, commissionEligible);
			
		}
		
	}
	
}