package nl.ica.oose.a2.zorgrit.persistance.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormat birthDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private DateFormat dateFormatWOTime = new SimpleDateFormat("yyyy-MM-dd");

    public String date(Date dateTime) {
        return dateFormat.format(dateTime);
    }

    public String formatSQLTimestampToStringDateWithoutMilliseconds(java.sql.Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp).toString();
    }

    public String birthDateFormatter(Date dateTime) {
        return birthDateFormat.format(dateTime);
    }

    public String standardDateFormat(Date dateTime) {
        return dateFormatWOTime.format(dateTime);
    }

}
