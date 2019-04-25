package Dlement.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	
	public static String replaceStart(String input,String startRegex,String replaceWith){
		String regex = "^("+startRegex+")(.+)?";
		Matcher m = Pattern.compile(regex).matcher(input);
		String startStr = "";
		if(m.find()){
			startStr = m.group(1);
		}
		return input.replaceFirst(startStr, replaceWith);
	}
	
	public static String replaceEnd(String sql) {
		String oldChar = sql.substring(sql.lastIndexOf(","));
		String newChar = sql.substring(sql.lastIndexOf(",")).replaceAll(", '(.+?)'\\)", ")").replaceAll("\\)", "\\)");
		return sql.replace(oldChar, newChar);
	}
	
	public static void main(String[] args) {
		String sql = "insert into pub_user (USER_ID, ORG_CODE, LOGIN_NAME, USER_NAME, USER_PASSWORD, EMAIL, MOBILE_PHONE, IS_VALID, USER_LEVEL, USER_CODE, ID_CARD, GENDER, NATION, BIRTHDAY, BIRTHPLACE, ADDRESS, QQ, WE_CHAT, GRADUATE_COLLEGE, EDUCATION, MAJOR, USER_CATEGORY, PROBATIONARY_DATE, OFFICIAL_DATE, ENTER_DATE, PHOTO, FIRST_CONTACTOR, FIRST_CONTACT_PHONE, USER_RESUME, INNER_JOB, OUTER_JOB, LINK_ORG, LINK_TYPE, POSTCODE, IS_FLOATING, IS_CERTIFIED, HAS_FLOW_CERT, FLOW_TO, FLOW_TO_NAME, ORG_UNIT, DOMICILE_PLACE, NATIVE_PLACE, REC_UNIT_TYPE, TAX_WAGE, IS_ACTIVATION, HAS_ORG, IS_INFO_ENOUGH, CERTIFY_WAYS, EDU_DEGREE, TAX_WAGE_STATUS, UNIT_NAME, UNIT_ADDRESS, PROB_DATE_BAK, UPDATE_TIME, NEW_NATION_CODE, JOB, PARTY_STATUS, ADDRESS_PRE, IS_DISCONNECT, DISCONNECT_TIME, COMBINED_ORG, IS_COMBINED, TELEPHONE, CARE_INFO, NATION_BAK, EDU_START, EDU_END, JOB_START, JOB_END, FLOW_CARD, PROOF, PROOF_RESULT, SECRET_TYPE, CADRE_TYPE, USER_TITLE, USER_NUM, DFISH_USER_ID, NATIVE_PLACE_BAK, IS_SET_FEE, IS_RETIRED_MANA, RETIRED_TYPE, ARCHIVES_SITUATION, ARCHIVES_UNIT, ARCHIVES_EXPLAIN, ORG_NAME)values ('1ae3pm40e1f80000', 'UNDEFINED', '352202197712032033', '郑志坚', null, null, '13950505505', '1', '0', null, '352202197712032033', '1', '01', to_date('03-12-1977', 'dd-mm-yyyy'), null, null, null, null, null, null, null, '06', null, null, to_date('29-01-2018 09:51:32', 'dd-mm-yyyy hh24:mi:ss'), null, null, null, null, null, null, null, null, null, '0', '1', '0', null, null, null, null, null, null, null, '0', null, '1', '00000001', null, '0', null, null, null, to_date('29-01-2018 09:53:34', 'dd-mm-yyyy hh24:mi:ss'), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, null, null, null, null, null, null, '0', '0', null, null, null, null, '党员未知组织');";
		System.out.println(replaceEnd(sql));
	}
}
