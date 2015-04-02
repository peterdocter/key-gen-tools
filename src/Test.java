import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liyong on 15/2/1.
 */
public class Test {
    public static long setSizeForavliable(long size, int cat) {
        long result = size + 0L;
        try {
            if ((cat == 6) && size < 4 * 1024 * 1024 * 1024L) {
                result = (4550 - 970) * 1024 * 1024L + size;
            }
        } catch (Exception e) {

        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(setSizeForavliable(7 * 1024 * 1024 * 1024L, 6));
        List<String> list = new ArrayList<String>();
        list.addAll(new ArrayList<String>());
        String[] answerItem = "213er|中文|23fsg|".split("\\|");
        List<String> answerList = new ArrayList<String>();
        for (int i = 0; i < answerItem.length; i++) {
            System.out.println(answerItem[i]);
        }
        System.out.println(j("23415bedb18cd88eb5cc0d1024").substring(0, 8));
        System.out.println(regFunc());
    }

    private static final String j(String paramString)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i1 = 0; i1 < arrayOfByte.length;) {
                //if (i1 >= arrayOfByte.length)
                String str;
                for (Object localObject = Integer.toHexString(0xFF & arrayOfByte[i1]); ; localObject = str) {
                    if (((String) localObject).length() >= 2) {
                        localStringBuffer.append((String) localObject);
                        i1++;
                        break;
                    }
                    str = "0" + (String) localObject;
                }
            }
                return localStringBuffer.toString();

            }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
           // Log.e("TeslaMultiSCADA", "Preference:" + localNoSuchAlgorithmException.toString());
        }
        return (String)"";
    }
    private static String regFunc() {
        Calendar localCalendar = Calendar.getInstance();
        int i = 321 * ((1 + localCalendar.get(Calendar.MONTH)) * localCalendar.get(Calendar.DATE));
        String str;
        if (i >= 1000000)
            str = String.valueOf(i - 1000000 * (i / 1000000));
        for (str = String.valueOf(i); str.length() != 6; str = str + "0") ;
        return str;
    }
}
