package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DateUtil {

	/**
	 * dd/MM/yyyy
	 */
	public static final String DATA_PATTERN = "dd/MM/yyyy";

	/**
	 * dd/MM/yyyy HH:mm
	 */
	public static final String DATA_HORA_PATTERN2 = "dd/MM/yyyy HH:mm";

	/**
	 * dd/MM/yyyy HH:mm:ss
	 */
	public static final String DATA_HORA_PATTERN1 = "dd/MM/yyyy HH:mm:ss";

	private DateUtil() {
	}

	/**
	 * Formata a data com o padrão {@link DateUtil#DATA_PATTERN}
	 * 
	 * @param date Data para formatar
	 * @return String da data formatada
	 */
	public static String format(Date date) {
		return format(date, DATA_PATTERN);
	}

	public static String format(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT-3"));
		return sdf.format(date);
	}
	
	public static String formatNoTimeZone(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date today() {
		return new Date();
	}

	/**
	 * Solve Sonar's issue: Malicious code vulnerability - May expose internal
	 * representation by incorporating reference to mutable object.
	 * 
	 * http://stackoverflow.com/a/18954919
	 * 
	 * "The default clone() will create shallow copy, so it might not fix the problem"
	 * 
	 * @param date original date
	 * @return deep copy of date.
	 */
	public static Date deepCopy(Date date) {
		return date != null ? new Date(date.getTime()) : null;
	}
	
	/**
	 * Seta a hora para meio dia.
	 *
	 * @param data the data
	 * @return the date
	 */
	public static Date setHoraMeioDia(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * Compara datas desprezando as horas
	 * 
	 * @param data
	 * @return
	 */
	public static boolean compareOnlyDate(Date data1, Date data2) {
		
		boolean retorno = false;
		
		Calendar gc1 = Calendar.getInstance(); 
		gc1.setTime(data1);
		
		Calendar gc2 = Calendar.getInstance(); 
		gc2.setTime(data2);
		
		if (gc1.get(Calendar.DAY_OF_MONTH) == gc2.get(Calendar.DAY_OF_MONTH) &&
				gc1.get(Calendar.MONTH) == gc2.get(Calendar.MONTH) &&
				gc1.get(Calendar.YEAR) == gc2.get(Calendar.YEAR)) {
			
			retorno = true;
		}
		
		return retorno; 
	}
	
	
}
