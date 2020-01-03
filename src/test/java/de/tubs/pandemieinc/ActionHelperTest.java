package de.tubs.pandemieinc;

import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.Strength;
import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import de.tubs.pandemieinc.events.OutbreakEvent;
import de.tubs.pandemieinc.events.QuarantineEvent;
import de.tubs.pandemieinc.events.PathogenEncounteredEvent;
import de.tubs.pandemieinc.events.VaccineInDevelopmentEvent;
import de.tubs.pandemieinc.events.VaccineAvailableEvent;
import de.tubs.pandemieinc.events.VaccineDeployedEvent;
import de.tubs.pandemieinc.events.MedicationInDevelopmentEvent;
import de.tubs.pandemieinc.events.MedicationAvailableEvent;
import de.tubs.pandemieinc.events.MedicationDeployedEvent;

import de.tubs.pandemieinc.Round;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
* Testing the ActionHelper class.
* @see de.tubs.pandemieinc.ActionHelper
*/
class ActionHelperTests {

    @Test
    void testGetPossibleActions() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();

        // Get all Actions
        round.points = 1000;
        List<Action> actions = ActionHelper.getPossibleActions(round);
        assertEquals(actions.size(), Action.nonDependentActions().length);

        // Only endRound
        round.points = 0;
        actions = ActionHelper.getPossibleActions(round);
        assertEquals(actions.size(), 1);
        assertEquals(actions.get(0), Action.ENDROUND);


        // Test for VaccineAvailable
        round.points = 5;
        VaccineAvailableEvent event = new VaccineAvailableEvent();
        event.sinceRound = 1;
        event.pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                      Strength.MIDDLE, Strength.MIDDLE);
        round.events.add(event);

        // Expected Result
        List<Action> expectedActions = new ArrayList();
        expectedActions.add(Action.ENDROUND);
        expectedActions.add(Action.DEPLOYVACCINE);
        expectedActions.add(Action.EXERTINFLUENCE);
        expectedActions.add(Action.CALLELECTIONS);
        expectedActions.add(Action.APPLYHYGIENICMEASURES);
        expectedActions.add(Action.LAUNCHCAMPAIGN);

        actions = ActionHelper.getPossibleActions(round);
        assertEquals(actions.size(), expectedActions.size());
        assertTrue(actions.containsAll(expectedActions));
    }

    @Test
    void testGetCitiesWithOnePathogen() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();

        // Test without cities
        Map<City, Pathogen> result = ActionHelper.getCitiesWithOnePathogen(round);
        assertNotNull(result);
        assertEquals(result.size(), 0);

        // Test without a Pathogen in a City;
        City city = new City("Test");
        city.events = new ArrayList();
        round.cities.put(city.name, city);
        result = ActionHelper.getCitiesWithOnePathogen(round);
        assertNotNull(result);
        assertEquals(result.size(), 0);


        // Test with a Pathogen in a City;
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        OutbreakEvent event = new OutbreakEvent(pathogen, 0.5, 1);
        city.events.add(event);
        result = ActionHelper.getCitiesWithOnePathogen(round);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(city), pathogen);

        // Test with a city with and a city without Pathogen
        City city2 = new City("Other City");
        city2.events = new ArrayList();
        round.cities.put(city2.name, city2);
        result = ActionHelper.getCitiesWithOnePathogen(round);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(city), pathogen);
    }

    @Test
    void testFindDeadlyPathogen() {
        List<Pathogen> pathogens = new ArrayList();
        // Test with empty Array
        Pathogen deadlyPathogen = ActionHelper.findDeadlyPathogen(pathogens);
        assertNull(deadlyPathogen);

        // Test with single Pathogen
        Pathogen testPath1 = new Pathogen("Test 1", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        pathogens.add(testPath1);
        deadlyPathogen = ActionHelper.findDeadlyPathogen(pathogens);
        assertNotNull(deadlyPathogen);
        assertEquals(deadlyPathogen, testPath1);

        // Test with multiple Pathogen
        // testPath2 should be preferred over testPath1
        Pathogen testPath2 = new Pathogen("Test 2", Strength.HIGH, Strength.HIGH,
                                          Strength.HIGH, Strength.HIGH);
        pathogens.add(testPath2);
        deadlyPathogen = ActionHelper.findDeadlyPathogen(pathogens);
        assertNotNull(deadlyPathogen);
        assertEquals(deadlyPathogen, testPath2);
    }

    @Test
    void testFindFastlyPathogen() {
        List<Pathogen> pathogens = new ArrayList();
        // Test with empty Array
        Pathogen fastPathogen = ActionHelper.findFastlyPathogen(pathogens);
        assertNull(fastPathogen);

        // Test with single Pathogen
        Pathogen testPath1 = new Pathogen("Test 1", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        pathogens.add(testPath1);
        fastPathogen = ActionHelper.findFastlyPathogen(pathogens);
        assertNotNull(fastPathogen);
        assertEquals(fastPathogen, testPath1);

        // Test with multiple Pathogen
        // testPath2 should be preferred over testPath1
        Pathogen testPath2 = new Pathogen("Test 2", Strength.HIGH, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        pathogens.add(testPath2);
        fastPathogen = ActionHelper.findFastlyPathogen(pathogens);
        assertNotNull(fastPathogen);
        assertEquals(fastPathogen, testPath2);
    }

    @Test
    void testFindSlowlyPathogen() {
        List<Pathogen> pathogens = new ArrayList();
        // Test with empty Array
        Pathogen slowPathogen = ActionHelper.findSlowlyPathogen(pathogens);
        assertNull(slowPathogen);

        // Test with single Pathogen
        Pathogen testPath1 = new Pathogen("Test 1", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        pathogens.add(testPath1);
        slowPathogen = ActionHelper.findSlowlyPathogen(pathogens);
        assertNotNull(slowPathogen);
        assertEquals(slowPathogen, testPath1);

        // Test with multiple Pathogen
        // testPath1 should be preferred over testPath2
        Pathogen testPath2 = new Pathogen("Test 2", Strength.HIGH, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        pathogens.add(testPath2);
        slowPathogen = ActionHelper.findSlowlyPathogen(pathogens);
        assertNotNull(slowPathogen);
        assertEquals(slowPathogen, testPath1);
    }

    @Test
    void testPathogensNotInQuarantine() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();
        round.pathogens = new HashMap();

        // Test without pathogens or cities
        List<Pathogen> result = ActionHelper.pathogensNotInQuarantine(round);
        assertNotNull(result);
        assertEquals(result.size(), 0);

        // Test with a Pathogen in a City
        City city1 = new City("Test 1");
        city1.events = new ArrayList();
        round.cities.put(city1.name, city1);

        // Test with a Pathogen in a City;
        Pathogen pathogen1 = new Pathogen("Test 1", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        round.pathogens.put(pathogen1.name, pathogen1);
        city1.events.add(new OutbreakEvent(pathogen1, 0.5, 1));
        result = ActionHelper.pathogensNotInQuarantine(round);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), pathogen1);

        // Test with other Pathogen in Quarantine
        City city2 = new City("Test 2");
        city2.events = new ArrayList();
        round.cities.put(city1.name, city1);
        Pathogen pathogen2 = new Pathogen("Test 2", Strength.LOW, Strength.LOW,
                                         Strength.LOW, Strength.LOW);
        round.pathogens.put(pathogen2.name, pathogen2);
        city2.events.add(new OutbreakEvent(pathogen2, 0.5, 1));

        // Add QuarantineEvent to city
        city2.events.add(new QuarantineEvent(1, 2));

        result = ActionHelper.pathogensNotInQuarantine(round);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), pathogen1);
    }

    @Test
    void testIsVaccineInDevelopment() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        round.events.add(new PathogenEncounteredEvent(1, pathogen));

        // Test without VaccineInDevelopmentEvent
        boolean vaccineInDevelopment = ActionHelper.isVaccineInDevelopment(round, pathogen);
        assertFalse(vaccineInDevelopment);

        // Test with VaccineInDevelopmentEvent
        round.events.add(new VaccineInDevelopmentEvent(1, 2, pathogen));
        vaccineInDevelopment = ActionHelper.isVaccineInDevelopment(round, pathogen);
        assertTrue(vaccineInDevelopment);        
    }

    @Test
    void testIsVaccineAvailable() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        round.events.add(new PathogenEncounteredEvent(1, pathogen));

        // Test without VaccineInDevelopmentEvent
        boolean vaccineAvailable = ActionHelper.isVaccineInDevelopment(round, pathogen);
        assertFalse(vaccineAvailable);

        // Test with VaccineInDevelopmentEvent
        round.events.add(new VaccineAvailableEvent(pathogen, 1));
        vaccineAvailable = ActionHelper.isVaccineAvailable(round, pathogen);
        assertTrue(vaccineAvailable);
    }

    @Test
    void testIsVaccineDeployed() {
        City city = new City("Test");
        city.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        city.events.add(new OutbreakEvent(pathogen, 0.5, 1));

        // Test without VaccineDeployedEvent
        boolean vaccineDeployed = ActionHelper.isVaccineDeployed(city, pathogen);
        assertFalse(vaccineDeployed);

        // Test with VaccineDeployedEvent
        city.events.add(new VaccineDeployedEvent(pathogen, 1));
        vaccineDeployed = ActionHelper.isVaccineDeployed(city, pathogen);
        assertTrue(vaccineDeployed);
    }

    @Test
    void testIsCityInfectedWithPathogen() {
        City city = new City("Test");
        city.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);

        // Test without Pathogen in City
        boolean isCityInfected = ActionHelper.isCityInfectedWithPathogen(city, pathogen);
        assertFalse(isCityInfected);

        // Test with Pathogen
        city.events.add(new OutbreakEvent(pathogen, 0.5, 1));
        isCityInfected = ActionHelper.isCityInfectedWithPathogen(city, pathogen);
        assertTrue(isCityInfected);
    }

    @Test
    void testIsMedicationInDevelopment() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        round.events.add(new PathogenEncounteredEvent(1, pathogen));

        // Test without MedicationInDevelopmentEvent
        boolean medicationInDevelopment = ActionHelper.isMedicationInDevelopment(round, pathogen);
        assertFalse(medicationInDevelopment);

        // Test with MedicationInDevelopmentEvent
        round.events.add(new MedicationInDevelopmentEvent(1, 2, pathogen));
        medicationInDevelopment = ActionHelper.isMedicationInDevelopment(round, pathogen);
        assertTrue(medicationInDevelopment);   
    }

    @Test
    void testIsMedicationAvailable() {
        Round round = new Round();
        round.round = 1;
        round.outcome = "pending";
        round.cities = new HashMap();
        round.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        round.events.add(new PathogenEncounteredEvent(1, pathogen));

        // Test without MedicationAvailableEvent
        boolean medicationAvailable = ActionHelper.isMedicationAvailable(round, pathogen);
        assertFalse(medicationAvailable);

        // Test with MedicationAvailableEvent
        round.events.add(new MedicationAvailableEvent(pathogen, 1));
        medicationAvailable = ActionHelper.isMedicationAvailable(round, pathogen);
        assertTrue(medicationAvailable);
    }

    @Test
    void testIsMedicationDeployed() {
        City city = new City("Test");
        city.events = new ArrayList();
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                         Strength.MIDDLE, Strength.MIDDLE);
        city.events.add(new OutbreakEvent(pathogen, 0.5, 1));

        // Test without MedicationDeployedEvent
        boolean medicationDeployed = ActionHelper.isMedicationDeployed(city, pathogen);
        assertFalse(medicationDeployed);

        // Test with MedicationDeployedEvent
        city.events.add(new MedicationDeployedEvent(pathogen, 1));
        medicationDeployed = ActionHelper.isMedicationDeployed(city, pathogen);
        assertTrue(medicationDeployed);
    }

    @Test
    void testFindHighestPathogenRound() {
        List<City> cities = new ArrayList();
        Pathogen pathogen1 = new Pathogen("Test 1", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        // Test without a City
        int rounds = ActionHelper.findHighestPathogenRound(cities, pathogen1);
        assertEquals(rounds, 0);
        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen1,
                                                       (city, pathogen) -> (false));

        // Test with a city (without the relevant Pathogen)
        City city1 = new City("City 1");
        city1.events = new ArrayList();
        city1.events.add(new OutbreakEvent(pathogen1, 0.5, 1));
        cities.add(city1);

        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen1);
        assertEquals(rounds, 0);
        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen1,
                                                       (city, pathogen) -> (false));
        assertEquals(rounds, 0);

        // Test with relevant Pathogen
        rounds = ActionHelper.findHighestPathogenRound(cities, null);
        assertEquals(rounds, 1);
        rounds = ActionHelper.findHighestPathogenRound(cities, null,
                                                       (city, pathogen) -> (false));
        assertEquals(rounds, 1);

        // Test with multiple cities
        Pathogen pathogen2 = new Pathogen("Test 2", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        City city2 = new City("City 2");
        city2.events = new ArrayList();
        city2.events.add(new OutbreakEvent(pathogen2, 0.5, 2));
        cities.add(city2);

        // Test without restriction => should return 2 from pathogen2
        rounds = ActionHelper.findHighestPathogenRound(cities, null);
        assertEquals(rounds, 2);
        rounds = ActionHelper.findHighestPathogenRound(cities, null,
                                                       (city, pathogen) -> (false));
        assertEquals(rounds, 2);

        // Test with filter pathogen2 => should return 1
        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen2);
        assertEquals(rounds, 1);
        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen2,
                                                       (city, pathogen) -> (false));
        assertEquals(rounds, 1);

        // Test the BiFunc
        rounds = ActionHelper.findHighestPathogenRound(cities, null,
                                                       (city, pathogen) -> (city == city2));
        assertEquals(rounds, 1);
        rounds = ActionHelper.findHighestPathogenRound(cities, pathogen1,
                                                       (city, pathogen) -> (city == city2));
        assertEquals(rounds, 0);
    }

    @Test
    void testGetCitiesWithGivenOutbreakRound() {
        List<City> cities = new ArrayList();

        // Test without a city
        List<City> outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(0, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 0);

        // Test with a City, but without a Pathogen
        City city1 = new City("City 1");
        city1.events = new ArrayList();
        cities.add(city1);
        outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(0, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 0);

        // Test with a City, with a Pathogen
        Pathogen pathogen = new Pathogen("Test", Strength.MIDDLE, Strength.MIDDLE,
                                          Strength.MIDDLE, Strength.MIDDLE);
        city1.events.add(new OutbreakEvent(pathogen, 0.5, 1));
        outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(1, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 1);
        assertEquals(outbreakCities.get(0), city1);

        // With differend round attribute
        outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(0, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 0);

        // Test with multiple cities
        City city2 = new City("City 2");
        city2.events = new ArrayList();
        city2.events.add(new OutbreakEvent(pathogen, 0.5, 1));
        cities.add(city2);

        outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(1, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 2);

        City city3 = new City("City 3");
        city3.events = new ArrayList();
        city3.events.add(new OutbreakEvent(pathogen, 0.5, 3));
        cities.add(city3);
        outbreakCities = ActionHelper.getCitiesWithGivenOutbreakRound(3, cities);
        assertNotNull(outbreakCities);
        assertEquals(outbreakCities.size(), 1);
        assertEquals(outbreakCities.get(0), city3);
    }
}
