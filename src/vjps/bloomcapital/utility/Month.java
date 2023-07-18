package vjps.bloomcapital.utility;

/**
 * Enumeração que representa os meses do ano.
 * 
 * @author Vinícius J P Silva
 */
public enum Month {
	JANUARY(1, "Janeiro"),
	FEBRUARY(2, "Fevereiro"),
	MARCH(3, "Março"),
	APRIL(4, "Abril"),
	MAY(5, "Maio"),
	JUNE(6, "Junho"),
	JULY(7, "Julho"),
	AUGUST(8, "Agosto"),
	SEPTEMBER(9, "Setembro"),
	OCTOBER(10, "Outubro"),
	NOVEMBER(11, "Novembro"),
	DECEMBER(12, "Dezembro");
	
	private int number;
	private String name;
	
	private Month(int number, String name) {
		this.number = number;
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Retorna o objeto Month correspondente ao número do mês especificado.
	 *
	 * @param monthNumber o número do mês
	 * @return o objeto Month correspondente ao número do mês, ou null se não houver correspondência
	 */
	public static Month name(int monthNumber) {
		for(Month month : Month.values())
			if(month.number == monthNumber)
				return month;
		return null;
	}
	
	/**
	 * Retorna o objeto Month correspondente ao nome do mês especificado.
	 *
	 * @param monthName o nome do mês
	 * @return o objeto Month correspondente ao nome do mês, ou null se não houver correspondência
	 */
	public static Month name(String monthName) {
		for(Month month : Month.values())
			if(month.name.equalsIgnoreCase(monthName))
				return month;
		return null;
	}

} // enum Month
