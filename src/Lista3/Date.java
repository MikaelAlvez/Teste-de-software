package Lista3;

public class Date {

    private int month; //1-12
    private int day; //1-31 conforme o mês
    private int year;

    private static final int[] daysPerMonth= {0,31,28,31,30,31,30,31,31,30,31,30,31};

    //dado o ano, confirma o valor adequado para o mês e dia
    public Date(int month, int day, int year){

        //verifica se o mês está no intervalo 1-12
        if (month <= 0 || month > 12)
            throw new IllegalArgumentException("month (" + month + ") must be 1-12");

        //verifica se day está no intervalo para month
        if (day <= 0 || day > daysPerMonth[month] && !(month == 2 && day == 29))
            throw new IllegalArgumentException("day (" + day + ") out-of-range for the specified month and year");

        //verifica no ano bissexto, se o mês é 2 e o dia é 29
        if (month == 2 && day == 29 && !(year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)))
            throw new IllegalArgumentException("day (" + day + ") out-of-range for the specified month and year");

        this.month = month;
        this.day = day;
        this.year = year;
    }

    //retorna uma String no formato mês/dia/ano
    public String toString(){
        return String.format("%d/%d/%d",month,day,year);
    }
}
