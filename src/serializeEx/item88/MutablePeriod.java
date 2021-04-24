package serializeEx.item88;

import java.io.*;
import java.util.Date;

public class MutablePeriod {
    // Period instance
    public final Period period;

    // start time field
    public final Date start;

    // end time field
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            // Serialize valid Period instance
            out.writeObject(new Period(new Date(), new Date()));

            /*
             * previous object reference
             */
            byte[] ref = { 0x71, 0, 0x7e, 0, 5 };
            bos.write(ref); // start field
            ref[4] = 4;
            bos.write(ref); // end field

            // steal Date reference after serialize Period
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            period = (Period) in.readObject();
            start = (Date) in.readObject();
            end = (Date) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }
}
