package co.edu.unal.loteria.test.domain;

import static co.edu.unal.loteria.service.OfyService.ofy;
import static org.junit.Assert.*;
import co.edu.unal.loteria.domain.Profile;
import co.edu.unal.loteria.domain.ResultadoLoteria;
import co.edu.unal.loteria.form.ResultadoForm;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tests for Conference POJO.
 */
public class ResultadoTest {

    private static final long ID = 123456L;

    private static final String NAME = "GCP Live";

    private static final String DESCRIPTION = "New announcements for Google Cloud Platform";

    private static final String ORGANIZER_USER_ID = "123456789";

    private static final String CITY = "San Francisco";

    private static final int MONTH = 3;

    private static final int CAP = 500;

    private Date startDate;

    private Date endDate;

    private List<String> topics;

    private ResultadoForm resultadoForm;

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        startDate = dateFormat.parse("03/25/2014");
        endDate = dateFormat.parse("03/26/2014");
        topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        resultadoForm = new ResultadoForm(NAME, DESCRIPTION, topics, CITY, startDate, endDate,
                CAP);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() throws Exception {
        ResultadoForm nullConferenceForm = new ResultadoForm(null, DESCRIPTION, topics, CITY,
                startDate, endDate, CAP);
        new ResultadoLoteria(ID, ORGANIZER_USER_ID, nullConferenceForm);
    }

    @Test
    public void testConference() throws Exception {
        ResultadoLoteria conference = new ResultadoLoteria(ID, ORGANIZER_USER_ID, resultadoForm);
        assertEquals(ID, conference.getId());
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(ORGANIZER_USER_ID, conference.getOrganizerUserId());
        assertEquals(topics, conference.getTopics());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(MONTH, conference.getMonth());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        // Test if they are defensive copies.
        assertNotSame(topics, conference.getTopics());
        assertNotSame(startDate, conference.getStartDate());
        assertNotSame(endDate, conference.getEndDate());
    }

    @Test
    public void testGetOrganizerDisplayName() throws Exception {
        String displayName = "Udacity Student";
        Profile profile = new Profile(ORGANIZER_USER_ID, displayName, "", null);
        ofy().save().entity(profile).now();
        ResultadoLoteria conference = new ResultadoLoteria(ID, ORGANIZER_USER_ID, resultadoForm);
        assertEquals(displayName, conference.getOrganizerDisplayName());
    }

    @Test
    public void testBookSeats() throws Exception {
        ResultadoLoteria conference = new ResultadoLoteria(ID, ORGANIZER_USER_ID, resultadoForm);
        conference.bookSeats(1);
        assertEquals(CAP - 1, conference.getSeatsAvailable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookSeatsFailure() throws Exception {
        ResultadoLoteria conference = new ResultadoLoteria(ID, ORGANIZER_USER_ID, resultadoForm);
        conference.bookSeats(500);
        assertEquals(0, conference.getSeatsAvailable());
        // this will fail
        conference.bookSeats(1);
    }

    /*
    @Test
    public void testReturnSeats() throws Exception {
        Conference conference = new Conference(ID, ORGANIZER_USER_ID, conferenceForm);
        conference.bookSeats(1);
        assertEquals(CAP - 1, conference.getSeatsAvailable());
        conference.giveBackSeats(1);
        assertEquals(CAP, conference.getSeatsAvailable());
    }
    */

    /*
    @Test(expected = IllegalArgumentException.class)
    public void testReturnSeatsFailure() throws Exception {
        Conference conference = new Conference(ID, ORGANIZER_USER_ID, conferenceForm);
        conference.giveBackSeats(1);
    }
    */
}
