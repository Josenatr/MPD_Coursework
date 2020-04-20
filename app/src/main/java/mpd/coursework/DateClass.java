package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
public class DateClass {

    public String convertDate(String dateString) {

        String regex = "\\d+";
        String r = "";
        String[] fullDate = dateString.split(" ");
        for (String datePart : fullDate) {
            if (datePart.matches(regex)) {
                r+= datePart+ "/";
            } else if(!month(datePart).isEmpty()) {
                r+= month(datePart) + "/";
            }
        }
        return r.substring(0, r.length()-1);
    }


    public String month(String month) {
        switch(month) {
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "";
        }
    }
}
