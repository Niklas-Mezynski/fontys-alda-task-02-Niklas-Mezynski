package appointmentplanner;

import appointmentplanner.api.*;
import org.assertj.core.api.SoftAssertions;

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TestData {

    static final AbstractAPFactory fac = ServiceFinder.getFactory();
    static final LocalDay TODAY = new LocalDay();
    //    Instant I08_30 = LocalTime.of( 8, 30 ).;
    static final LocalTime T08_30 = LocalTime.of( 8, 30 );
    static final LocalTime T09_00 = LocalTime.of( 9, 0 );
    static final LocalTime T09_30 = LocalTime.of( 9, 30 );
    static final LocalTime T10_00 = LocalTime.of( 10, 0 );
    static final LocalTime T10_30 = LocalTime.of( 10, 30 );
    static final LocalTime T10_45 = LocalTime.of( 10, 45 );
    static final LocalTime T11_00 = LocalTime.of( 11, 0 );
    static final LocalTime T11_10 = LocalTime.of( 11, 10 );
    static final LocalTime T14_00 = LocalTime.of( 14, 0);
    static final LocalTime T14_30 = LocalTime.of( 14, 30 );
    static final LocalTime T15_00 = LocalTime.of( 15, 0 );
    static final LocalTime T15_15 = LocalTime.of( 15, 15 );
    static final LocalTime T15_30 = LocalTime.of( 15, 30 );
    static final LocalTime T15_45 = LocalTime.of( 15, 45 );
    static final LocalTime T16_00 = LocalTime.of( 16, 0 );
    static final LocalTime T17_30 = LocalTime.of( 17, 30 );
    static final Duration D15 = Duration.ofMinutes( 15 );
    static final Duration D30 = Duration.ofMinutes( 30 );
    static final Duration D80 = Duration.ofMinutes( 80 );
    static final Duration D90 = Duration.ofMinutes( 90 );
    static final Duration D200 = Duration.ofMinutes( 200 );
    static final AppointmentData DATA0 = fac.createAppointmentData( "app0 30 min @8:30", D30, Priority.LOW );
    static final AppointmentData DATA1 = fac.createAppointmentData( "app1 30 min @9:00", D30, Priority.LOW );
    static final AppointmentData DATA2 = fac.createAppointmentData( "app2 30 min @10:30", D30, Priority.LOW );
    static final AppointmentData DATA3 = fac.createAppointmentData( "app3 15 min @10:30", D15, Priority.MEDIUM );
    static final AppointmentData DATA4 = fac.createAppointmentData( "app4 15 min @10:45", D15, Priority.HIGH );
    static final AppointmentData DATA5 = fac.createAppointmentData( "app5 200 min @11:10", D200, Priority.HIGH );
    static final AppointmentData DATA6 = fac.createAppointmentData( "app6 30 min @14:30", D30, Priority.LOW );
    static final AppointmentData DATA7 = fac.createAppointmentData( "app7 90 min @16:00", D90, Priority.LOW );
    static final AppointmentData DATA8 = fac.createAppointmentData( "app8 80 min @09:00", D80, Priority.LOW );
    static final AppointmentRequest AR1 = fac.createAppointmentRequest( DATA1, T09_00, TimePreference.UNSPECIFIED );
    static final AppointmentRequest AR2 = fac.createAppointmentRequest( DATA2, T10_30 );
    static final AppointmentRequest AR3 = fac.createAppointmentRequest( DATA3, T10_30 );
    static final AppointmentRequest AR4 = fac.createAppointmentRequest( DATA4, T10_45 );
    static final AppointmentRequest AR5 = fac.createAppointmentRequest( DATA5, T11_10 );
    static final AppointmentRequest AR6 = fac.createAppointmentRequest( DATA6, T14_30 );
    static final AppointmentRequest AR7 = fac.createAppointmentRequest( DATA7, T16_00, TimePreference.EARLIEST );

//    static LocalDayPlan standardDay() {
//        LocalDayPlan td = emptyWorkingDay();
//        addApps( td, AR1, AR2, AR3, AR4, AR5, AR6, AR7 );
//        return td;
//    }

    static LocalDayPlan emptyWorkingDay() {
        return fac.createLocalDayPlan( TODAY, LocalTime.of( 8, 30 ), LocalTime.of( 17, 30 ) );
    }

    public static <T> void verifyEqualsAndHashCode( T ref, T equal, T... unEqual ) {
        Object object = "Hello";
        T tnull = null;
        String cname = ref.getClass().getCanonicalName();
        // I got bitten here, assertJ equalTo does not invoke equals on the
        // object when ref and 'other' are same.
        // THAT's why the first one differs from the rest.
        SoftAssertions.assertSoftly(softly-> {
            softly.assertThat( ref.equals( ref ) )
                    .as( cname + ".equals(this): with self should produce true" )
                    .isTrue();
            softly.assertThat( ref.equals( tnull ) )
                    .as( cname + ".equals(null): ref object "
                            + safeToString( ref ) + " and null should produce false"
                    )
                    .isFalse();
            softly.assertThat( ref.equals( object ) )
                    .as( cname + ".equals(new Object()): ref object"
                            + " compared to other type should produce false"
                    )
                    .isFalse();
            softly.assertThat( ref.equals( equal ) )
                    .as( cname + " ref object [" + safeToString( ref )
                            + "] and equal object [" + safeToString( equal )
                            + "] should report equal"
                    )
                    .isTrue();
            for ( int i = 0; i < unEqual.length; i++ ) {
                T ueq = unEqual[ i ];
                softly.assertThat( ref )
                        .as("testing supposed unequal objects")
                        .isNotEqualTo( ueq );
            }
            // ref and equal should have same hashCode
            softly.assertThat( ref.hashCode() )
                    .as( cname + " equal objects "
                            + ref + " and "
                            + equal + " should have same hashcode"
                    )
                    .isEqualTo( equal.hashCode() );
        });
    }

    /**
     * ToString that deals with any exceptions that are thrown during its
     * invocation.
     *
     * When x.toString triggers an exception, the returned string contains a
     * message with this information plus class and system hashCode of the
     * object.
     *
     * @param x to turn into string or a meaningful message.
     * @return "null" when x==null, x.toString() when not.
     */
    public static String safeToString( Object x ) {
        if ( x == null ) {
            return "null";
        }
        try {
            return x.toString();
        } catch ( Throwable e ) {
            return "invoking toString on instance "
                    + x.getClass().getCanonicalName() + "@"
                    + Integer.toHexString( System.identityHashCode( x ) )
                    + " causes an exception " + e;
        }
    }

}